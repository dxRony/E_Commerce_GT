/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.controller;

import com.ecommerce.ECommerceGTBE.dto.request.tarjeta.TarjetaRequest;
import com.ecommerce.ECommerceGTBE.dto.response.auth.MensajeResponse;
import com.ecommerce.ECommerceGTBE.dto.response.tarjeta.TarjetaResponse;
import com.ecommerce.ECommerceGTBE.model.Tarjeta;
import com.ecommerce.ECommerceGTBE.model.Usuario;
import com.ecommerce.ECommerceGTBE.service.TarjetaService;
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
@RequestMapping("/api/tarjetas")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TarjetaController {

    @Autowired
    private TarjetaService tarjetaService;

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
    public ResponseEntity<List<TarjetaResponse>> getMisTarjetas() {
        Integer usuarioId = obtenerIdUsuarioSesion();
        Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Tarjeta> tarjetas = tarjetaService.findActivasByUsuario(usuario);

        List<TarjetaResponse> response = tarjetas.stream()
                .map(this::crearResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/todas")
    @PreAuthorize("hasRole('COMUN')")
    public ResponseEntity<List<TarjetaResponse>> getTodasMisTarjetas() {
        Integer usuarioId = obtenerIdUsuarioSesion();
        Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Tarjeta> tarjetas = tarjetaService.findByUsuario(usuario);

        List<TarjetaResponse> response = tarjetas.stream()
                .map(this::crearResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('COMUN')")
    public ResponseEntity<?> agregarTarjeta(@Valid @RequestBody TarjetaRequest tarjetaRequest) {
        Integer usuarioId = obtenerIdUsuarioSesion();
        Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setNumeracion(tarjetaRequest.getNumeracion());
        tarjeta.setFecha_vencimiento(tarjetaRequest.getFechaVencimiento());
        tarjeta.setCvv(tarjetaRequest.getCvv());

        Tarjeta tarjetaCreada = tarjetaService.agregarTarjeta(tarjeta, usuario);

        TarjetaResponse response = crearResponse(tarjetaCreada);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('COMUN')")
    public ResponseEntity<MensajeResponse> eliminarTarjeta(@PathVariable Integer id) {
        Integer usuarioId = obtenerIdUsuarioSesion();
        Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        tarjetaService.desactivarTarjeta(id, usuario);

        return ResponseEntity.ok(new MensajeResponse("Tarjeta eliminada"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('COMUN')")
    public ResponseEntity<TarjetaResponse> getTarjeta(@PathVariable Integer id) {
        Integer usuarioId = obtenerIdUsuarioSesion();
        Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Tarjeta tarjeta = tarjetaService.getTarjetaByIdAndUsuario(id, usuario);

        TarjetaResponse response = crearResponse(tarjeta);
        return ResponseEntity.ok(response);
    }

    private TarjetaResponse crearResponse(Tarjeta tarjeta) {
        String numeracionProtegida = "****-****-****-"
                + tarjeta.getNumeracion().substring(tarjeta.getNumeracion().length() - 4);

        return new TarjetaResponse(
                tarjeta.getId(),
                numeracionProtegida,
                tarjeta.getFecha_vencimiento(),
                tarjeta.getActiva()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MensajeResponse> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(new MensajeResponse(ex.getMessage()));
    }

}
