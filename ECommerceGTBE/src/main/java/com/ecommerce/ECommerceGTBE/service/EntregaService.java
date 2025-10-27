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
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Lazy;

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
    private CompraRepository compraRepository;

    @Autowired
    @Lazy
    private CompraService compraService;

    @Autowired
    private UsuarioService usuarioService;

    /**
     * metodo que crea una entrega
     *
     * @param compra a registrar en la db
     * @return confirmacion de la operacion
     */
    @Transactional
    public Entrega crearEntrega(Compra compra) {
        //creando entrega
        Entrega entrega = new Entrega();
        entrega.setEstado("En curso");
        entrega.setFechaEstimada(LocalDate.now().plusDays(5));
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

    /**
     * obtiene las entregas de un usuario
     *
     * @param usuarioId dueño de las entregas
     * @return lista de entregas de un usuario
     */
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

    /**
     * obtiene las entregas que estan en curso
     *
     * @return lista de entregas que estan en curso
     */
    public List<Entrega> obtenerEntregasEnCurso() {
        return entregaRepository.findByEstado("En curso");
    }

    /**
     * obtiene una entrega por su id
     *
     * @param entregaId de la entrega
     * @return entraga si existe
     */
    public Entrega obtenerEntrega(Integer entregaId) {
        return entregaRepository.findById(entregaId)
                .orElseThrow(() -> new RuntimeException("Entrega no encontrada"));
    }

    /**
     * actualiza la fecha estimada de una entrega
     *
     * @param entregaId de la entrega
     * @param nuevaFechaEstimada a actualizar
     * @return confirmacio nde la operacion
     */
    public Entrega actualizarFechaEstimada(Integer entregaId, LocalDate nuevaFechaEstimada) {
        Entrega entrega = obtenerEntrega(entregaId);
        entrega.setFechaEstimada(nuevaFechaEstimada);
        return entregaRepository.save(entrega);
    }

    /**
     * maraca una entrega como entregada
     *
     * @param entregaId de la entrega
     * @return confirmacion de la opeacion
     */
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
        entrega.setFechaEntrega(LocalDate.now());

        //marcando compra como finalizada
        Compra compra = entrega.getCompra();
        compra.setEstadoEntrega("Finalizada");
        compraRepository.save(compra);
        return entregaRepository.save(entrega);
    }

    /**
     * marca un detalle de la entrega como listo
     *
     * @param detalleId del detalle entrega
     * @return confirmacion de la operacion
     */
    public DetalleEntrega marcarDetalleEntregaListo(Integer detalleId) {
        DetalleEntrega detalle = detalleEntregaRepository.findById(detalleId)
                .orElseThrow(() -> new RuntimeException("detalleEntrega no existe"));

        detalle.setListo(true);
        return detalleEntregaRepository.save(detalle);
    }

    /**
     * obtiene todos los detalles entregad euna entrega
     *
     * @param entregaId de la entrega
     * @return lista de detalles entrega
     */
    public List<DetalleEntrega> obtenerDetallesEntrega(Integer entregaId) {
        Entrega entrega = obtenerEntrega(entregaId);
        return detalleEntregaRepository.findByEntrega(entrega);
    }

}
