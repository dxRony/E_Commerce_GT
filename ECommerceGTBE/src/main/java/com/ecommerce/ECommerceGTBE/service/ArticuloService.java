/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.service;

import com.ecommerce.ECommerceGTBE.model.Articulo;
import com.ecommerce.ECommerceGTBE.model.Usuario;
import com.ecommerce.ECommerceGTBE.repository.ArticuloRepository;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author ronyrojas
 */
@Service
public class ArticuloService {

    @Autowired
    private ArticuloRepository articuloRepository;

    public Articulo crearArticulo(Articulo articulo, Usuario usuario) {
        articulo.setUsuario(usuario);
        articulo.setEstadoAprobacion("Pendiente");
        return articuloRepository.save(articulo);
    }

    public Optional<Articulo> findById(Integer id) {
        return articuloRepository.findById(id);
    }

    public List<Articulo> findByUsuario(Usuario usuario) {
        return articuloRepository.findByUsuario(usuario);
    }

    public List<Articulo> findByEstadoAprobacion(String estadoAprobacion) {
        return articuloRepository.findByEstadoAprobacion(estadoAprobacion);
    }

    public List<Articulo> findAprobados() {
        return articuloRepository.findByEstadoAprobacion("Aprobado");
    }

    public List<Articulo> findByCategoria(String categoria) {
        return articuloRepository.findByCategoria(categoria);
    }

    public Articulo updateArticulo(Integer id, Articulo articuloActualizado) {
        return articuloRepository.findById(id)
                .map(articulo -> {
                    articulo.setNombre(articuloActualizado.getNombre());
                    articulo.setDescripcion(articuloActualizado.getDescripcion());
                    articulo.setImagen(articuloActualizado.getImagen());
                    articulo.setPrecio(articuloActualizado.getPrecio());
                    articulo.setStock(articuloActualizado.getStock());
                    articulo.setEstadoArticulo(articuloActualizado.getEstadoArticulo());
                    articulo.setCategoria(articuloActualizado.getCategoria());
                    articulo.setEstadoAprobacion("Pendiente");
                    return articuloRepository.save(articulo);
                })
                .orElseThrow(() -> new RuntimeException("articulo con id no encontrado: " + id));
    }

    public Articulo aprobarArticulo(Integer id) {
        return articuloRepository.findById(id)
                .map(articulo -> {
                    articulo.setEstadoAprobacion("Aprobado");
                    return articuloRepository.save(articulo);
                })
                .orElseThrow(() -> new RuntimeException("articulo con id no encontrado: " + id));
    }

    public Articulo rechazarArticulo(Integer id) {
        return articuloRepository.findById(id)
                .map(articulo -> {
                    articulo.setEstadoAprobacion("Rechazado");
                    return articuloRepository.save(articulo);
                })
                .orElseThrow(() -> new RuntimeException("articulo con id no encontrado: " + id));
    }

    public List<Articulo> buscarPorNombre(String nombre) {
        return articuloRepository.findByNombreContainingIgnoreCaseAndEstadoAprobacion(nombre, "Aprobado");
    }

    public List<Articulo> findByPrecioBetween(BigDecimal precioMin, BigDecimal precioMax) {
        return articuloRepository.findByPrecioBetweenAndEstadoAprobacion(
                precioMin, precioMax, "Aprobado");
    }

    public Articulo actualizarStock(Integer id, Integer cantidadVendida) {
        return articuloRepository.findById(id)
                .map(articulo -> {
                    if (articulo.getStock() < cantidadVendida) {
                        throw new RuntimeException("stock insuficiente");
                    }
                    articulo.setStock(articulo.getStock() - cantidadVendida);
                    return articuloRepository.save(articulo);
                })
                .orElseThrow(() -> new RuntimeException("articulo con id no encontrado:" + id));
    }

}
