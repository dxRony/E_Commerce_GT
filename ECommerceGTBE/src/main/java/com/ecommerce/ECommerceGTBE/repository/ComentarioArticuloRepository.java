/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.repository;

import com.ecommerce.ECommerceGTBE.model.Articulo;
import com.ecommerce.ECommerceGTBE.model.ComentarioArticulo;
import com.ecommerce.ECommerceGTBE.model.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author ronyrojas
 */
public interface ComentarioArticuloRepository extends JpaRepository<ComentarioArticulo, Integer> {

    List<ComentarioArticulo> findByArticulo(Articulo articulo);

    List<ComentarioArticulo> findByUsuario(Usuario usuario);

    List<ComentarioArticulo> findByUsuarioAndArticulo(Usuario usuario, Articulo articulo);

    Long countByArticulo(Articulo articulo);

    List<ComentarioArticulo> findByArticuloOrderByFechaDesc(Articulo articulo);

    Boolean existsByUsuarioAndArticulo(Usuario usuario, Articulo articulo);

    void deleteByArticulo(Articulo articulo);

    void deleteByUsuario(Usuario usuario);
}
