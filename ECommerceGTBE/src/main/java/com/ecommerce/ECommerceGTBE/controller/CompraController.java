/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.controller;

import com.ecommerce.ECommerceGTBE.dto.request.pago.CompraRequest;
import com.ecommerce.ECommerceGTBE.dto.response.auth.MensajeResponse;
import com.ecommerce.ECommerceGTBE.dto.response.pago.CompraResponse;
import com.ecommerce.ECommerceGTBE.dto.response.pago.DetalleCompraResponse;
import com.ecommerce.ECommerceGTBE.model.Compra;
import com.ecommerce.ECommerceGTBE.model.DetalleCompra;
import com.ecommerce.ECommerceGTBE.service.CompraService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author ronyrojas
 */
@RestController
@RequestMapping("/api/compras")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CompraController {

    @Autowired
    private CompraService compraService;

    private Integer obtenerIdUsuarioSesion() {
        Object userIdAttr = org.springframework.web.context.request.RequestContextHolder
                .getRequestAttributes().getAttribute("userId", org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST);

        if (userIdAttr != null) {
            return (Integer) userIdAttr;
        }
        throw new RuntimeException("no se pudo obtener el id del usuario");
    }

    // usuarios comunes en sesion

    /**
     * procesa el pago del carrito para un usuario
     * @param pagoRequest de la compra, contiene la tarjeta a usar
     * @return confirmacion de la operacion
     */
    @PostMapping("/pagar")
    @PreAuthorize("hasRole('COMUN')")
    public ResponseEntity<CompraResponse> procesarPago(@Valid @RequestBody CompraRequest pagoRequest) {
        Integer usuarioId = obtenerIdUsuarioSesion();

        Compra compra = compraService.procesarPago(usuarioId, pagoRequest.getTarjetaId());

        CompraResponse response = crearCompraResponse(compra);
        return ResponseEntity.ok(response);
    }

    /**
     * obtiene todas las compras de un usuario comun
     * @return lista de compras 
     */
    @GetMapping
    @PreAuthorize("hasRole('COMUN')")
    public ResponseEntity<List<CompraResponse>> obtenerMisCompras() {
        Integer usuarioId = obtenerIdUsuarioSesion();

        List<Compra> compras = compraService.obtenerComprasUsuario(usuarioId);

        List<CompraResponse> response = compras.stream()
                .map(this::crearCompraResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * obtiene una compra especifica del usuario
     * @param compraId de la compra a buscar
     * @return compra si existe
     */
    @GetMapping("/{compraId}")
    @PreAuthorize("hasRole('COMUN')")
    public ResponseEntity<CompraResponse> obtenerCompra(@PathVariable Integer compraId) {
        Integer usuarioId = obtenerIdUsuarioSesion();

        Compra compra = compraService.obtenerCompra(compraId, usuarioId);
        List<DetalleCompra> detalles = compraService.obtenerDetallesCompra(compraId);

        CompraResponse response = crearCompraResponse(compra);
        response.setDetalles(detalles.stream()
                .map(this::crearDetalleCompraResponse)
                .collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }

    /**
     * obtiene todas las ventas del usuario comun en sesion
     * @return lista de detales compra
     */
    @GetMapping("/ventas")
    @PreAuthorize("hasRole('COMUN')")
    public ResponseEntity<List<DetalleCompraResponse>> obtenerMisVentas() {
        Integer usuarioId = obtenerIdUsuarioSesion();

        List<DetalleCompra> ventas = compraService.obtenerVentasVendedor(usuarioId);

        List<DetalleCompraResponse> response = ventas.stream()
                .map(this::crearDetalleCompraResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    private CompraResponse crearCompraResponse(Compra compra) {
        String numeracionOculta = "****-****-****-"
                + compra.getTarjeta().getNumeracion().substring(compra.getTarjeta().getNumeracion().length() - 4);

        CompraResponse response = new CompraResponse(
                compra.getId(),
                compra.getFecha(),
                compra.getTotal(),
                compra.getEstadoEntrega(),
                compra.getComisionUsuario(),
                compra.getComisionPagina(),
                numeracionOculta
        );

        List<DetalleCompra> detalles;
        if (compra.getDetallesCompra() != null && !compra.getDetallesCompra().isEmpty()) {
            detalles = compra.getDetallesCompra();
        } else {
            detalles = compraService.obtenerDetallesCompra(compra.getId());
        }

        response.setDetalles(detalles.stream()
                .map(this::crearDetalleCompraResponse)
                .collect(Collectors.toList()));

        return response;
    }

    private DetalleCompraResponse crearDetalleCompraResponse(DetalleCompra detalle) {
        return new DetalleCompraResponse(
                detalle.getId(),
                detalle.getArticulo().getId(),
                detalle.getArticulo().getNombre(),
                detalle.getArticulo().getImagen(),
                detalle.getArticulo().getPrecio(),
                detalle.getCantidad(),
                detalle.getSubtotal(),
                detalle.getArticulo().getUsuario().getId(),
                detalle.getArticulo().getUsuario().getNombre()
        );
    }

    /**
     * manejador de errores
     * @param ex
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MensajeResponse> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(new MensajeResponse(ex.getMessage()));
    }
}
