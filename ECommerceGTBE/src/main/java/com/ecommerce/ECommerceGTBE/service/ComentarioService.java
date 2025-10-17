/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.service;

import com.ecommerce.ECommerceGTBE.model.Articulo;
import com.ecommerce.ECommerceGTBE.model.ComentarioArticulo;
import com.ecommerce.ECommerceGTBE.model.Usuario;
import com.ecommerce.ECommerceGTBE.repository.ComentarioArticuloRepository;
import com.ecommerce.ECommerceGTBE.repository.DetalleCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author ronyrojas
 */
@Service
public class ComentarioService {

    @Autowired
    private ComentarioArticuloRepository comentarioRepository;

    @Autowired
    private DetalleCompraRepository detalleCompraRepository;

    @Autowired
    private ArticuloService articuloService;

    @Autowired
    private UsuarioService usuarioService;

    public ComentarioArticulo crearComentario(Integer articuloId, String comentario, Usuario usuario) {
        Articulo articulo = articuloService.findById(articuloId)
                .orElseThrow(() -> new RuntimeException("articulo no encontrado"));

        if (!haCompradoArticulo(usuario.getId(), articuloId)) {
            throw new RuntimeException("Solo puedes comentar articulos que hayas comprado");
        }

        ComentarioArticulo nuevoComentario = new ComentarioArticulo();
        nuevoComentario.setComentario(comentario);
        nuevoComentario.setFecha(LocalDateTime.now());
        nuevoComentario.setArticulo(articulo);
        nuevoComentario.setUsuario(usuario);

        return comentarioRepository.save(nuevoComentario);
    }

    public List<ComentarioArticulo> obtenerComentariosDeArticulo(Integer articuloId) {
        Articulo articulo = articuloService.findById(articuloId)
                .orElseThrow(() -> new RuntimeException("articulo no encontrado"));
        return comentarioRepository.findByArticuloOrderByFechaDesc(articulo);
    }

    public List<ComentarioArticulo> obtenerComentariosDeUsuario(Integer usuarioId) {
        Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("user no encontrado"));
        return comentarioRepository.findByUsuario(usuario);
    }

    public void eliminarComentario(Integer comentarioId, Usuario usuario) {
        ComentarioArticulo comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new RuntimeException("comentario no encontrado"));

        if (!comentario.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("no puedes eliminar este comentario");
        }
        comentarioRepository.delete(comentario);
    }

    private boolean haCompradoArticulo(Integer usuarioId, Integer articuloId) {
        return detalleCompraRepository.existsByUsuarioAndArticulo(usuarioId, articuloId);

    }
}
