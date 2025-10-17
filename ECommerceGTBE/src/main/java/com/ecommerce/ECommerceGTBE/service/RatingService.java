/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.service;

import com.ecommerce.ECommerceGTBE.model.Articulo;
import com.ecommerce.ECommerceGTBE.model.RatingArticulo;
import com.ecommerce.ECommerceGTBE.model.Usuario;
import com.ecommerce.ECommerceGTBE.repository.DetalleCompraRepository;
import com.ecommerce.ECommerceGTBE.repository.RatingArticuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author ronyrojas
 */
@Service
public class RatingService {

    @Autowired
    private RatingArticuloRepository ratingRepository;

    @Autowired
    private DetalleCompraRepository detalleCompraRepository;

    @Autowired
    private ArticuloService articuloService;

    @Autowired
    private UsuarioService usuarioService;

    public RatingArticulo crearActualizarRating(Integer articuloId, Integer puntuacion, Usuario usuario) {
        Articulo articulo = articuloService.findById(articuloId)
                .orElseThrow(() -> new RuntimeException("articlo no encontrado"));

        if (!haCompradoArticulo(usuario.getId(), articuloId)) {
            throw new RuntimeException("Solo puedes calificar articulos que hayas comprado");
        }

        RatingArticulo ratingExistente = ratingRepository.findByArticuloAndUsuario(articulo, usuario)
                .orElse(null);

        if (ratingExistente != null) {
            ratingExistente.setPuntuacion(puntuacion);
            ratingExistente.setFecha(LocalDateTime.now());
            return ratingRepository.save(ratingExistente);
        } else {
            RatingArticulo nuevoRating = new RatingArticulo();
            nuevoRating.setPuntuacion(puntuacion);
            nuevoRating.setFecha(LocalDateTime.now());
            nuevoRating.setArticulo(articulo);
            nuevoRating.setUsuario(usuario);
            return ratingRepository.save(nuevoRating);
        }
    }

    public RatingArticulo obtenerRatingUsuario(Integer articuloId, Usuario usuario) {
        Articulo articulo = articuloService.findById(articuloId)
                .orElseThrow(() -> new RuntimeException("articulo no encontrado"));
        return ratingRepository.findByArticuloAndUsuario(articulo, usuario)
                .orElse(null);
    }

    public List<RatingArticulo> obtenerRatingsPorArticulo(Integer articuloId) {
        Articulo articulo = articuloService.findById(articuloId)
                .orElseThrow(() -> new RuntimeException("articulo no encontrado"));
        return ratingRepository.findByArticulo(articulo);
    }

    public void eliminarRating(Integer ratingId, Usuario usuario) {
        RatingArticulo rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new RuntimeException("rating no encontrado"));

        if (!rating.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("no puedes eliminar este rating");
        }
        ratingRepository.delete(rating);
    }

    private boolean haCompradoArticulo(Integer usuarioId, Integer articuloId) {
        return detalleCompraRepository.existsByUsuarioAndArticulo(usuarioId, articuloId);
    }
}
