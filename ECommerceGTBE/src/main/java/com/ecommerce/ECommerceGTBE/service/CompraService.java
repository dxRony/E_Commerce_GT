/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.service;

import com.ecommerce.ECommerceGTBE.model.Compra;
import com.ecommerce.ECommerceGTBE.model.DetalleCarrito;
import com.ecommerce.ECommerceGTBE.model.DetalleCompra;
import com.ecommerce.ECommerceGTBE.model.Tarjeta;
import com.ecommerce.ECommerceGTBE.model.Usuario;
import com.ecommerce.ECommerceGTBE.repository.CompraRepository;
import com.ecommerce.ECommerceGTBE.repository.DetalleCarritoRepository;
import com.ecommerce.ECommerceGTBE.repository.DetalleCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author ronyrojas
 */
@Service
public class CompraService {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private DetalleCompraRepository detalleCompraRepository;

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private ArticuloService articuloService;

    @Autowired
    private TarjetaService tarjetaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EntregaService entregaService;

    /**
     * procesa el pago de una compra
     * @param usuarioId del usuario que hace la compra
     * @param tarjetaId de la tarjeta que realizara el pago
     * @return compra registrada
     */
    @Transactional
    public Compra procesarPago(Integer usuarioId, Integer tarjetaId) {
        Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("usuario no encontrado"));

        //obteniendo tarjeta de usuario
        Tarjeta tarjeta = tarjetaService.getTarjetaByIdAndUsuario(tarjetaId, usuario);
        //obteniendo y validando carrito del usuario
        List<DetalleCarrito> detallesCarrito = carritoService.obtenerCarrito(usuario);

        if (detallesCarrito.isEmpty()) {
            throw new RuntimeException("el carrito esta vacio");
        }

        BigDecimal total = calcularTotalCompra(detallesCarrito);
        BigDecimal comisionPagina = total.multiply(new BigDecimal("0.05"));
        BigDecimal comisionUsuario = total.subtract(comisionPagina);

        //creando compra
        Compra compra = new Compra();
        compra.setFecha(LocalDateTime.now());
        compra.setTotal(total);
        compra.setEstadoEntrega("Pendiente");
        compra.setComisionUsuario(comisionUsuario);
        compra.setComisionPagina(comisionPagina);
        compra.setUsuario(usuario);
        compra.setTarjeta(tarjeta);

        Compra compraRegistrada = compraRepository.save(compra);

        //por cada articulo en la compra se crea un detalleCompra
        for (DetalleCarrito detalleCarrito : detallesCarrito) {
            DetalleCompra detalleCompra = new DetalleCompra();
            detalleCompra.setCantidad(detalleCarrito.getCantidad());
            detalleCompra.setSubtotal(detalleCarrito.getArticulo().getPrecio().multiply(BigDecimal.valueOf(detalleCarrito.getCantidad())));
            detalleCompra.setCompra(compraRegistrada);
            detalleCompra.setArticulo(detalleCarrito.getArticulo());

            detalleCompraRepository.save(detalleCompra);
            //disminuir stock
            articuloService.actualizarStock(detalleCarrito.getArticulo().getId(), detalleCarrito.getCantidad());
        }
        //creando entrega de la compra
        entregaService.crearEntrega(compraRegistrada);

        //limppiar carrito luego de compra
        carritoService.limpiarCarrito(usuario);

        return compraRegistrada;
    }

    /*
    * calcula el total de una compra
    */
    private BigDecimal calcularTotalCompra(List<DetalleCarrito> itemsCarrito) {
        return itemsCarrito.stream()
                .map(item -> item.getArticulo().getPrecio().multiply(BigDecimal.valueOf(item.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * obtiene las compras de un usuario
     * @param usuarioId del usuario
     * @return lista de compras del usuario
     */
    public List<Compra> obtenerComprasUsuario(Integer usuarioId) {
        Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("user no encontrado"));
        return compraRepository.findByUsuario(usuario);
    }

    /**
     * obtiene una compra especifica
     * @param compraId de la compra
     * @param usuarioId del usuario
     * @return compra si existe
     */
    public Compra obtenerCompra(Integer compraId, Integer usuarioId) {
        Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("user no encontrado"));

        Compra compra = compraRepository.findById(compraId)
                .orElseThrow(() -> new RuntimeException("compra no encontrada"));

        return compra;
    }

    /**
     * obtiene los detalles de una compra
     * @param compraId de la compra
     * @return lista de detalles compra 
     */
    public List<DetalleCompra> obtenerDetallesCompra(Integer compraId) {
        Compra compra = compraRepository.findById(compraId)
                .orElseThrow(() -> new RuntimeException("compra no encontrada"));
        return detalleCompraRepository.findByCompra(compra);
    }

    /**
     * obtiene los detalles compra de un vendedor
     * @param vendedorId del usuario vendedor
     * @return lista de detalles compra
     */
    public List<DetalleCompra> obtenerVentasVendedor(Integer vendedorId) {
        Usuario vendedor = usuarioService.findById(vendedorId)
                .orElseThrow(() -> new RuntimeException("vendedor no encontrado"));
        return detalleCompraRepository.findByVendedor(vendedor);
    }

}
