/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.service;

import com.ecommerce.ECommerceGTBE.model.Compra;
import com.ecommerce.ECommerceGTBE.model.DetalleCompra;
import com.ecommerce.ECommerceGTBE.model.DetalleEntrega;
import com.ecommerce.ECommerceGTBE.model.Entrega;
import com.ecommerce.ECommerceGTBE.model.Usuario;
import com.ecommerce.ECommerceGTBE.repository.CompraRepository;
import com.ecommerce.ECommerceGTBE.repository.DetalleCompraRepository;
import com.ecommerce.ECommerceGTBE.repository.DetalleEntregaRepository;
import com.ecommerce.ECommerceGTBE.repository.EntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author ronyrojas
 */
@Service
public class EntregaService {

    @Autowired
    private EntregaRepository entregaRepository;

    @Autowired
    private DetalleEntregaRepository detalleEntregaRepository;

    @Autowired
    private DetalleCompraRepository detalleCompraRepository;
    
    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private CompraService compraService;
    
    @Autowired
    private UsuarioService usuarioService;

    @Transactional
    public Entrega crearEntrega(Compra compra) {

        //creando entrega
        Entrega entrega = new Entrega();
        entrega.setEstado("En curso");
        entrega.setFechaEstimada(LocalDateTime.now().plusDays(5));
        entrega.setCompra(compra);
        //registrando entrega en db
        Entrega entregaRegistrada = entregaRepository.save(entrega);
        //por cada detalle compra se crea un detalleEntrega
        List<DetalleCompra> detallesCompra = compraService.obtenerDetallesCompra(compra.getId());

        for (DetalleCompra detalleCompra : detallesCompra) {
            DetalleEntrega detalleEntrega = new DetalleEntrega();
            detalleEntrega.setEntrega(entregaRegistrada);
            detalleEntrega.setArticulo(detalleCompra.getArticulo());
            detalleEntrega.setListo(false);
            detalleEntregaRepository.save(detalleEntrega);
        }
        return entregaRegistrada;
    }

    public List<Entrega> obtenerEntregasUsuario(Integer usuarioId) {
        Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        //obteniendo "pedidos" de un usuario
        List<Compra> compras = compraService.obtenerComprasUsuario(usuarioId);
        return compras.stream()
                .map(compra -> entregaRepository.findByCompra(compra).orElse(null))
                .filter(entrega -> entrega != null)
                .collect(Collectors.toList());
    }

    public List<Entrega> obtenerEntregasEnCurso() {
        return entregaRepository.findByEstado("En curso");
    }

    public Entrega obtenerEntrega(Integer entregaId) {
        return entregaRepository.findById(entregaId)
                .orElseThrow(() -> new RuntimeException("Entrega no encontrada"));
    }

    public Entrega actualizarFechaEstimada(Integer entregaId, LocalDateTime nuevaFechaEstimada) {
        Entrega entrega = obtenerEntrega(entregaId);
        entrega.setFechaEstimada(nuevaFechaEstimada);
        return entregaRepository.save(entrega);
    }

    @Transactional
    public Entrega marcarEntregaComoEntregada(Integer entregaId) {
        Entrega entrega = obtenerEntrega(entregaId);

        //ultima validacion para ver si todos los articulos estan listos
        List<DetalleEntrega> detalles = detalleEntregaRepository.findByEntrega(entrega);
        boolean todosListos = detalles.stream().allMatch(DetalleEntrega::getListo);

        if (!todosListos) {
            throw new RuntimeException("error, todos los detalles entrega no estan listos");
        }
        //marcando entrega
        entrega.setEstado("Entregado");
        entrega.setFechaEntrega(LocalDateTime.now());

        //marcando compra como finalizada
        Compra compra = entrega.getCompra();
        compra.setEstadoEntrega("Finalizada");
        compraRepository.save(compra);

        return entregaRepository.save(entrega);
    }

    public DetalleEntrega marcarDetalleEntregaListo(Integer detalleId) {
        DetalleEntrega detalle = detalleEntregaRepository.findById(detalleId)
                .orElseThrow(() -> new RuntimeException("detalleEntrega no existe"));

        detalle.setListo(true);
        return detalleEntregaRepository.save(detalle);
    }

    public List<DetalleEntrega> obtenerDetallesEntrega(Integer entregaId) {
        Entrega entrega = obtenerEntrega(entregaId);
        return detalleEntregaRepository.findByEntrega(entrega);
    }

    public boolean puedeMarcarComoEntregada(Integer entregaId) {
        Entrega entrega = obtenerEntrega(entregaId);
        List<DetalleEntrega> detalles = detalleEntregaRepository.findByEntrega(entrega);
        return detalles.stream().allMatch(DetalleEntrega::getListo);
    }

    public List<Entrega> obtenerEntregasAtrasadas() {
        return entregaRepository.findEntregasAtrasadas(LocalDateTime.now());
    }
}
