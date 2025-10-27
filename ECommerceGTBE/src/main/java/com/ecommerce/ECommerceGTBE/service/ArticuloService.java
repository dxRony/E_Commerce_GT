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

    /**
     * crea un articulo en la db
     * @param articulo a insertar
     * @param usuario que crea el articulo
     * @return confirmacion de la operacion
     */
    public Articulo crearArticulo(Articulo articulo, Usuario usuario) {
        articulo.setUsuario(usuario);
        articulo.setEstadoAprobacion("Pendiente");
        return articuloRepository.save(articulo);
    }

    /**
     * busca un articulo
     * @param id del articulo
     * @return articulo
     */
    public Optional<Articulo> findById(Integer id) {
        return articuloRepository.findById(id);
    }

    /**
     * busca articulos por el usuario
     * @param usuario que posee el articulo
     * @return lista de articulos
     */
    public List<Articulo> findByUsuario(Usuario usuario) {
        return articuloRepository.findByUsuario(usuario);
    }

    /**
     * busca articulos por su estado de aprobacion
     * @param estadoAprobacion requerido para retornar
     * @return lista de articulos con el estado de aprobacion
     */
    public List<Articulo> findByEstadoAprobacion(String estadoAprobacion) {
        return articuloRepository.findByEstadoAprobacion(estadoAprobacion);
    }

    /**
     * busca articulos con estado de aprobacion Aprobado
     * @return lista de articulos aprobados
     */
    public List<Articulo> findAprobados() {
        return articuloRepository.findByEstadoAprobacion("Aprobado");
    }

    /**
     * buscar articulos por categoria
     * @param categoria del articulo
     * @return articulos pertenecientes a la categoria indicada
     */
    public List<Articulo> findByCategoria(String categoria) {
        return articuloRepository.findByCategoria(categoria);
    }

    /**
     * actualiza un articulo
     * @param id deñ articulo
     * @param articuloActualizado para registrar en la db
     * @return confirmacion de la operacion
     */
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

    /**
     * aprueba im articulo
     * @param id del articulo a aprobar
     * @return confirmacion de la operacion
     */
    public Articulo aprobarArticulo(Integer id) {
        return articuloRepository.findById(id)
                .map(articulo -> {
                    articulo.setEstadoAprobacion("Aprobado");
                    return articuloRepository.save(articulo);
                })
                .orElseThrow(() -> new RuntimeException("articulo con id no encontrado: " + id));
    }

    /**
     * rechaza un articulo
     * @param id del articulo a rechazar
     * @return confirmacion de la operacion
     */
    public Articulo rechazarArticulo(Integer id) {
        return articuloRepository.findById(id)
                .map(articulo -> {
                    articulo.setEstadoAprobacion("Rechazado");
                    return articuloRepository.save(articulo);
                })
                .orElseThrow(() -> new RuntimeException("articulo con id no encontrado: " + id));
    }

    /**
     * busca un articulo por el nombre especificado
     * @param nombre del articulo
     * @return articulo si es encontrado
     */
    public List<Articulo> buscarPorNombre(String nombre) {
        return articuloRepository.findByNombreContainingIgnoreCaseAndEstadoAprobacion(nombre, "Aprobado");
    }

    /**
     * busca articulos en un rango de precio espedificado
     * @param precioMin a buscar
     * @param precioMax a buscar
     * @return lista de articulos encontrados
     */
    public List<Articulo> findByPrecioBetween(BigDecimal precioMin, BigDecimal precioMax) {
        return articuloRepository.findByPrecioBetweenAndEstadoAprobacion(
                precioMin, precioMax, "Aprobado");
    }

    /**
     * actualiza el stock de un producto
     * @param id del articulo
     * @param cantidadVendida stock a modificar
     * @return confirmacion de la operacion
     */
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
