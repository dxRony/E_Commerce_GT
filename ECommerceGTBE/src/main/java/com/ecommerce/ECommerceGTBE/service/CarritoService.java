/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.service;

import com.ecommerce.ECommerceGTBE.model.Articulo;
import com.ecommerce.ECommerceGTBE.model.DetalleCarrito;
import com.ecommerce.ECommerceGTBE.model.Usuario;
import com.ecommerce.ECommerceGTBE.repository.DetalleCarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author ronyrojas
 */
@Service
public class CarritoService {

    @Autowired
    private DetalleCarritoRepository detalleCarritoRepository;

    @Autowired
    private ArticuloService articuloService;

    /**
     * agrega un articulo al carrito
     * @param idArticulo del articulo
     * @param cantidad de unidades a agregar
     * @param usuario poseedor del carrito
     * @return confirmacion de la operacion
     */
    public DetalleCarrito agregarArticuloCarrito(Integer idArticulo, Integer cantidad, Usuario usuario) {
        Articulo articulo = articuloService.findById(idArticulo)
                .orElseThrow(() -> new RuntimeException("articulo no encontrado"));

        if (!"Aprobado".equals(articulo.getEstadoAprobacion())) {
            throw new RuntimeException("El articulo no ha sido aprobado para vender");
        }

        if (articulo.getStock() < cantidad) {
            throw new RuntimeException("Stock no sufuciente, stock disponible actualmente: " + articulo.getStock());
        }

        Optional<DetalleCarrito> detalleExiste = detalleCarritoRepository.findByUsuarioAndArticulo(usuario, articulo);

        if (detalleExiste.isPresent()) {
            DetalleCarrito detalle = detalleExiste.get();
            int nuevaCantidad = detalle.getCantidad() + cantidad;

            if (articulo.getStock() < nuevaCantidad) {
                throw new RuntimeException("stock no suficiente, stock disponible actualmente: " + articulo.getStock());
            }

            detalle.setCantidad(nuevaCantidad);
            return detalleCarritoRepository.save(detalle);
        } else {
            DetalleCarrito nuevoDetalle = new DetalleCarrito();
            nuevoDetalle.setUsuario(usuario);
            nuevoDetalle.setArticulo(articulo);
            nuevoDetalle.setCantidad(cantidad);
            return detalleCarritoRepository.save(nuevoDetalle);
        }
    }

    /**
     * obtiene los dealltes carrito de un usuario
     * @param usuario dueño del carrito
     * @return lista de detalles carrito
     */
    public List<DetalleCarrito> obtenerCarrito(Usuario usuario) {
        return detalleCarritoRepository.findByUsuario(usuario);
    }

    /**
     * actualiza la cantidad de unidades de un carrito
     * @param itemId ide del detalle carrito
     * @param nuevaCantidad a actualizar en el detalle
     * @param usuario dueño del carrito
     * @return confirmacion de la operacion
     */
    public DetalleCarrito actualizarCarrito(Integer itemId, Integer nuevaCantidad, Usuario usuario) {
        DetalleCarrito detalle = detalleCarritoRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("detalle carrito no encontrado"));

        Articulo articulo = detalle.getArticulo();
        if (articulo.getStock() < nuevaCantidad) {
            throw new RuntimeException("stock no suficiente, stock disponible actualmente: " + articulo.getStock());
        }
        if (nuevaCantidad <= 0) {
            detalleCarritoRepository.delete(detalle);
            return null;
        } else {
            detalle.setCantidad(nuevaCantidad);
            return detalleCarritoRepository.save(detalle);
        }
    }

    /**
     * elimina un detalle del carrito
     * @param itemId del detalle carrito
     * @param usuario poseedor del carrito
     */
    public void eliminarDelCarrito(Integer itemId, Usuario usuario) {
        DetalleCarrito item = detalleCarritoRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("detalle carrito no encontrado"));

        detalleCarritoRepository.delete(item);
    }

    /**
     * vacia todos los detalles carrito
     * @param usuario poseedor del carrito
     */
    public void limpiarCarrito(Usuario usuario) {
        List<DetalleCarrito> items = detalleCarritoRepository.findByUsuario(usuario);
        detalleCarritoRepository.deleteAll(items);
    }

    /**
     * obtiene el precio de todos los articulos en el carrito
     * @param usuario deuño del carrito
     * @return valor del carrtio
     */
    public Integer obtenerCantidadTotalCarrito(Usuario usuario) {
        return detalleCarritoRepository.sumCantidadByUsuario(usuario);
    }

    /**
     * valida si un articulo ya esta en el carrito
     * @param usuario deuño del carrito
     * @param articulo a buscar 
     * @return confirmacion de la busqyeda
     */
    public boolean existeEnCarrito(Usuario usuario, Articulo articulo) {
        return detalleCarritoRepository.existsByUsuarioAndArticulo(usuario, articulo);
    }

}
