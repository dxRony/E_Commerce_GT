/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.controller;

import com.ecommerce.ECommerceGTBE.dto.request.entrega.ModificarEntregaRequest;
import com.ecommerce.ECommerceGTBE.dto.response.auth.MensajeResponse;
import com.ecommerce.ECommerceGTBE.dto.response.entrega.DetalleEntregaResponse;
import com.ecommerce.ECommerceGTBE.dto.response.entrega.EntregaResponse;
import com.ecommerce.ECommerceGTBE.model.DetalleEntrega;
import com.ecommerce.ECommerceGTBE.model.Entrega;
import com.ecommerce.ECommerceGTBE.service.EntregaService;
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
@RequestMapping("/api/entregas")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EntregaController {

    @Autowired
    private EntregaService entregaService;

    private Integer obtenerIdUsuarioSesion() {
        Object userIdAttr = org.springframework.web.context.request.RequestContextHolder
                .getRequestAttributes().getAttribute("userId", org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST);

        if (userIdAttr != null) {
            return (Integer) userIdAttr;
        }

        throw new RuntimeException("No se pudo obtener el ID del usuario autenticado");
    }

    //usuarios comunes en sesion

    /**
     * obtiene las entregas de un usuario comun
     * @return lista de entregas
     */
    @GetMapping("/mis-entregas")
    @PreAuthorize("hasRole('COMUN')")
    public ResponseEntity<List<EntregaResponse>> obtenerMisEntregas() {
        Integer usuarioId = obtenerIdUsuarioSesion();

        List<Entrega> entregas = entregaService.obtenerEntregasUsuario(usuarioId);

        List<EntregaResponse> response = entregas.stream()
                .map(this::crearEntregaResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * obtiene una entrega especifica del usuario
     * @param entregaId de la entrega a buscar
     * @return entrega si existe
     */
    @GetMapping("/{entregaId}")
    @PreAuthorize("hasRole('COMUN')")
    public ResponseEntity<EntregaResponse> obtenerEntrega(@PathVariable Integer entregaId) {
        Entrega entrega = entregaService.obtenerEntrega(entregaId);

        List<DetalleEntrega> detalles = entregaService.obtenerDetallesEntrega(entregaId);

        EntregaResponse response = crearEntregaResponse(entrega);
        response.setDetalles(detalles.stream()
                .map(this::crearDetalleEntregaResponse)
                .collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }

    // usuaarios de logistica en sesion

    /**
     * obtiene las entregas en curso para el usuario de logistica
     * @return
     */
    @GetMapping("/logistica/en-curso")
    @PreAuthorize("hasRole('LOGISTICA')")
    public ResponseEntity<List<EntregaResponse>> obtenerEntregasEnCurso() {
        List<Entrega> entregas = entregaService.obtenerEntregasEnCurso();

        List<EntregaResponse> response = entregas.stream()
                .map(this::crearEntregaResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * actualiza la fecha estimada de la entrega
     * @param entregaId de la entrega
     * @param request de la modificacion de fecha
     * @return confirmacion de la operacion
     */
    @PutMapping("/logistica/{entregaId}/fecha-estimada")
    @PreAuthorize("hasRole('LOGISTICA')")
    public ResponseEntity<EntregaResponse> actualizarFechaEstimada(
            @PathVariable Integer entregaId,
            @Valid @RequestBody ModificarEntregaRequest request) {

        Entrega entrega = entregaService.actualizarFechaEstimada(entregaId, request.getFechaEstimada());

        EntregaResponse response = crearEntregaResponse(entrega);
        return ResponseEntity.ok(response);
    }

    /**
     * marca un detalle entrega como listo
     * @param detalleId del detalle entrega
     * @return confirmacion de la operacion
     */
    @PutMapping("/logistica/detalles/{detalleId}/listo")
    @PreAuthorize("hasRole('LOGISTICA')")
    public ResponseEntity<DetalleEntregaResponse> marcarDetalleListo(@PathVariable Integer detalleId) {
        DetalleEntrega detalle = entregaService.marcarDetalleEntregaListo(detalleId);

        DetalleEntregaResponse response = crearDetalleEntregaResponse(detalle);
        return ResponseEntity.ok(response);
    }

    /**
     * marca una entrega como entregada
     * @param entregaId de la entrega
     * @return confirmacion de la operacion
     */
    @PutMapping("/logistica/{entregaId}/entregar")
    @PreAuthorize("hasRole('LOGISTICA')")
    public ResponseEntity<EntregaResponse> marcarComoEntregada(@PathVariable Integer entregaId) {
        Entrega entrega = entregaService.marcarEntregaComoEntregada(entregaId);

        EntregaResponse response = crearEntregaResponse(entrega);
        return ResponseEntity.ok(response);
    }

    private EntregaResponse crearEntregaResponse(Entrega entrega) {
        List<DetalleEntrega> detalles = entregaService.obtenerDetallesEntrega(entrega.getId());

        EntregaResponse response = new EntregaResponse(
                entrega.getId(),
                entrega.getEstado(),
                entrega.getFechaEstimada(),
                entrega.getFechaEntrega(),
                entrega.getCompra().getId(),
                entrega.getCompra().getTotal(),
                entrega.getCompra().getUsuario().getNombre(),
                entrega.getCompra().getUsuario().getDireccion()
        );
        List<DetalleEntregaResponse> detallesResponse = detalles.stream()
                .map(this::crearDetalleEntregaResponse)
                .collect(Collectors.toList());

        response.setDetalles(detallesResponse);

        return response;

    }

    private DetalleEntregaResponse crearDetalleEntregaResponse(DetalleEntrega detalle) {
        return new DetalleEntregaResponse(
                detalle.getId(),
                detalle.getArticulo().getId(),
                detalle.getArticulo().getNombre(),
                detalle.getArticulo().getImagen(),
                this.obtenerCantidadArticuloCompra(detalle),
                detalle.getListo()
        );
    }

    private Integer obtenerCantidadArticuloCompra(DetalleEntrega detalle) {
        return detalle.getEntrega().getCompra().getDetallesCompra().stream()
                .filter(dc -> dc.getArticulo().getId().equals(detalle.getArticulo().getId()))
                .findFirst()
                .map(dc -> dc.getCantidad())
                .orElse(1);
    }

    /**
     * manejardo de errores
     * @param ex
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MensajeResponse> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(new MensajeResponse(ex.getMessage()));
    }
}
