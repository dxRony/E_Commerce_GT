/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.repository;

import com.ecommerce.ECommerceGTBE.model.Articulo;
import com.ecommerce.ECommerceGTBE.model.DetalleCarrito;
import com.ecommerce.ECommerceGTBE.model.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author ronyrojas
 */
public interface DetalleCarritoRepository extends JpaRepository<DetalleCarrito, Integer> {

    List<DetalleCarrito> findByUsuario(Usuario usuario);

    Optional<DetalleCarrito> findByUsuarioAndArticulo(Usuario usuario, Articulo articulo);

    Boolean existsByUsuarioAndArticulo(Usuario usuario, Articulo articulo);

    Long countByUsuario(Usuario usuario);

    @Query("SELECT SUM(dc.cantidad) FROM DetalleCarrito dc WHERE dc.usuario = :usuario")
    Integer sumCantidadByUsuario(@Param("usuario") Usuario usuario);

    @Modifying
    @Query("DELETE FROM DetalleCarrito dc WHERE dc.usuario = :usuario")
    void deleteByUsuario(@Param("usuario") Usuario usuario);

    @Modifying
    void deleteByUsuarioAndArticulo(Usuario usuario, Articulo articulo);

    @Query("SELECT dc FROM DetalleCarrito dc WHERE dc.usuario = :usuario AND dc.articulo.stock >= dc.cantidad")
    List<DetalleCarrito> findValidItemsByUsuario(@Param("usuario") Usuario usuario);

}
