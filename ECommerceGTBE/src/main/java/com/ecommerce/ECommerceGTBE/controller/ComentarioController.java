/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.controller;

import com.ecommerce.ECommerceGTBE.dto.request.articulo.ComentarioRequest;
import com.ecommerce.ECommerceGTBE.dto.response.articulo.ComentarioResponse;
import com.ecommerce.ECommerceGTBE.dto.response.auth.MensajeResponse;
import com.ecommerce.ECommerceGTBE.model.ComentarioArticulo;
import com.ecommerce.ECommerceGTBE.model.Usuario;
import com.ecommerce.ECommerceGTBE.service.ComentarioService;
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
@RequestMapping("/api/comentarios")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private UsuarioService usuarioService;

    private Integer obtenerIdUsuarioSesion() {
        Object userIdAttr = org.springframework.web.context.request.RequestContextHolder
                .getRequestAttributes().getAttribute("userId", org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST);

        if (userIdAttr != null) {
            return (Integer) userIdAttr;
        }

        throw new RuntimeException("No se pudo obtener el ID del usuario autenticado");
    }

    //usuarios sin sesion
    @GetMapping("/articulo/{articuloId}")
    public ResponseEntity<List<ComentarioResponse>> obtenerComentariosPorArticulo(@PathVariable Integer articuloId) {
        List<ComentarioArticulo> comentarios = comentarioService.obtenerComentariosDeArticulo(articuloId);

        List<ComentarioResponse> response = comentarios.stream()
                .map(this::crearResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    //usuarios comunes en sesion
    @PostMapping
    @PreAuthorize("hasRole('COMUN')")
    public ResponseEntity<ComentarioResponse> crearComentario(@Valid @RequestBody ComentarioRequest request) {
        Integer usuarioId = obtenerIdUsuarioSesion();
        Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("user no encontrado"));

        ComentarioArticulo comentario = comentarioService.crearComentario(
                request.getArticuloId(),
                request.getComentario(),
                usuario
        );
        ComentarioResponse response = crearResponse(comentario);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/mis-comentarios")
    @PreAuthorize("hasRole('COMUN')")
    public ResponseEntity<List<ComentarioResponse>> obtenerMisComentarios() {
        Integer usuarioId = obtenerIdUsuarioSesion();

        List<ComentarioArticulo> comentarios = comentarioService.obtenerComentariosDeUsuario(usuarioId);

        List<ComentarioResponse> response = comentarios.stream()
                .map(this::crearResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{comentarioId}")
    @PreAuthorize("hasRole('COMUN')")
    public ResponseEntity<MensajeResponse> eliminarComentario(@PathVariable Integer comentarioId) {
        Integer usuarioId = obtenerIdUsuarioSesion();
        Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        comentarioService.eliminarComentario(comentarioId, usuario);

        return ResponseEntity.ok(new MensajeResponse("Comentario eliminado exitosamente"));
    }

    private ComentarioResponse crearResponse(ComentarioArticulo comentario) {
        return new ComentarioResponse(
                comentario.getId(),
                comentario.getComentario(),
                comentario.getFecha(),
                comentario.getArticulo().getId(),
                comentario.getUsuario().getId(),
                comentario.getUsuario().getNombre()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MensajeResponse> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(new MensajeResponse(ex.getMessage()));
    }
}
