/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.controller;

import com.ecommerce.ECommerceGTBE.dto.request.carrito.CarritoDetalleRequest;
import com.ecommerce.ECommerceGTBE.dto.response.auth.MensajeResponse;
import com.ecommerce.ECommerceGTBE.dto.response.carrito.CarritoDetalleResponse;
import com.ecommerce.ECommerceGTBE.dto.response.carrito.CarritoResponse;
import com.ecommerce.ECommerceGTBE.model.DetalleCarrito;
import com.ecommerce.ECommerceGTBE.model.Usuario;
import com.ecommerce.ECommerceGTBE.service.CarritoService;
import com.ecommerce.ECommerceGTBE.service.UsuarioService;
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
@RequestMapping("/api/carrito")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private UsuarioService usuarioService;

    private Integer obtenerIdUsuarioSesion() {
        Object userIdAttr = org.springframework.web.context.request.RequestContextHolder
                .getRequestAttributes().getAttribute("userId", org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST);

        if (userIdAttr != null) {
            return (Integer) userIdAttr;
        }

        throw new RuntimeException("No se pudo obtener el id del usuario");
    }

    // usuarios en sesion
    @GetMapping
    @PreAuthorize("hasRole('COMUN')")
    public ResponseEntity<CarritoResponse> obtenerCarrito() {
        Integer usuarioId = obtenerIdUsuarioSesion();
        Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<DetalleCarrito> detalles = carritoService.obtenerCarrito(usuario);

        List<CarritoDetalleResponse> detallesResponse = detalles.stream()
                .map(this::crearResponse)
                .collect(Collectors.toList());

        CarritoResponse response = new CarritoResponse(detallesResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/agregar")
    @PreAuthorize("hasRole('COMUN')")
    public ResponseEntity<CarritoDetalleResponse> agregarArticuloCarrito(@Valid @RequestBody CarritoDetalleRequest carritoRequest) {
        Integer usuarioId = obtenerIdUsuarioSesion();
        Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        DetalleCarrito detalle = carritoService.agregarArticuloCarrito(
                carritoRequest.getArticuloId(),
                carritoRequest.getCantidad(),
                usuario
        );

        CarritoDetalleResponse response = crearResponse(detalle);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{itemId}/cantidad")
    @PreAuthorize("hasRole('COMUN')")
    public ResponseEntity<?> actualizarCarrito(
            @PathVariable Integer itemId,
            @RequestParam Integer cantidad) {

        Integer usuarioId = obtenerIdUsuarioSesion();
        Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        DetalleCarrito item = carritoService.actualizarCarrito(itemId, cantidad, usuario);

        if (item == null) {
            return ResponseEntity.ok(new MensajeResponse("detalle eliminado del carrito"));
        }

        CarritoDetalleResponse response = crearResponse(item);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{itemId}")
    @PreAuthorize("hasRole('COMUN')")
    public ResponseEntity<MensajeResponse> eliminarDelCarrito(@PathVariable Integer itemId) {
        Integer usuarioId = obtenerIdUsuarioSesion();
        Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        carritoService.eliminarDelCarrito(itemId, usuario);

        return ResponseEntity.ok(new MensajeResponse("detalle eliminado del carrito"));
    }

    @DeleteMapping("/limpiar")
    @PreAuthorize("hasRole('COMUN')")
    public ResponseEntity<MensajeResponse> limpiarCarrito() {
        Integer usuarioId = obtenerIdUsuarioSesion();
        Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        carritoService.limpiarCarrito(usuario);

        return ResponseEntity.ok(new MensajeResponse("carrito limpiado exitosamente"));
    }

    @GetMapping("/cantidad-total")
    @PreAuthorize("hasRole('COMUN')")
    public ResponseEntity<Integer> obtenerCantidadTotal() {
        Integer usuarioId = obtenerIdUsuarioSesion();
        Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Integer cantidadTotal = carritoService.obtenerCantidadTotalCarrito(usuario);
        return ResponseEntity.ok(cantidadTotal);
    }

    private CarritoDetalleResponse crearResponse(DetalleCarrito item) {
        return new CarritoDetalleResponse(
                item.getId(),
                item.getArticulo().getId(),
                item.getArticulo().getNombre(),
                item.getArticulo().getImagen(),
                item.getArticulo().getPrecio(),
                item.getCantidad(),
                item.getArticulo().getStock()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MensajeResponse> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(new MensajeResponse(ex.getMessage()));
    }
}
