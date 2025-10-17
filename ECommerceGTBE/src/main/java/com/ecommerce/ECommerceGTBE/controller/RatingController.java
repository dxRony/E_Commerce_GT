/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.controller;

import com.ecommerce.ECommerceGTBE.dto.request.articulo.RatingRequest;
import com.ecommerce.ECommerceGTBE.dto.response.articulo.RatingResponse;
import com.ecommerce.ECommerceGTBE.dto.response.auth.MensajeResponse;
import com.ecommerce.ECommerceGTBE.model.RatingArticulo;
import com.ecommerce.ECommerceGTBE.model.Usuario;
import com.ecommerce.ECommerceGTBE.service.RatingService;
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
@RequestMapping("/api/ratings")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RatingController {

    @Autowired
    private RatingService ratingService;

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

    //usuarios publicos - sin sesion
    @GetMapping("/articulo/{articuloId}")
    public ResponseEntity<List<RatingResponse>> obtenerRatingsDeArticulo(@PathVariable Integer articuloId) {
        List<RatingArticulo> ratings = ratingService.obtenerRatingsPorArticulo(articuloId);

        List<RatingResponse> response = ratings.stream()
                .map(this::crearResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    //usuarios comunes en sesion
    @PostMapping
    @PreAuthorize("hasRole('COMUN')")
    public ResponseEntity<RatingResponse> crearActualizarRating(@Valid @RequestBody RatingRequest request) {
        Integer usuarioId = obtenerIdUsuarioSesion();
        Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("ser no encontrado"));

        RatingArticulo rating = ratingService.crearActualizarRating(
                request.getArticuloId(),
                request.getPuntuacion(),
                usuario
        );

        RatingResponse response = crearResponse(rating);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/articulo/{articuloId}/mi-rating")
    @PreAuthorize("hasRole('COMUN')")
    public ResponseEntity<RatingResponse> obtenerMiRating(@PathVariable Integer articuloId) {
        Integer usuarioId = obtenerIdUsuarioSesion();
        Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("user no encontrado"));

        RatingArticulo rating = ratingService.obtenerRatingUsuario(articuloId, usuario);
        if (rating == null) {
            return ResponseEntity.notFound().build();
        }

        RatingResponse response = crearResponse(rating);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{ratingId}")
    @PreAuthorize("hasRole('COMUN')")
    public ResponseEntity<MensajeResponse> eliminarRating(@PathVariable Integer ratingId) {
        Integer usuarioId = obtenerIdUsuarioSesion();
        Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("user no encontrado"));

        ratingService.eliminarRating(ratingId, usuario);

        return ResponseEntity.ok(new MensajeResponse("Rating eliminado"));
    }

    private RatingResponse crearResponse(RatingArticulo rating) {
        return new RatingResponse(
                rating.getId(),
                rating.getPuntuacion(),
                rating.getFecha(),
                rating.getArticulo().getId(),
                rating.getUsuario().getId(),
                rating.getUsuario().getNombre()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MensajeResponse> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(new MensajeResponse(ex.getMessage()));
    }
}
