/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.repository;

import com.ecommerce.ECommerceGTBE.model.Articulo;
import com.ecommerce.ECommerceGTBE.model.Usuario;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author ronyrojas
 */
public interface ArticuloRepository extends JpaRepository<Articulo, Integer> {

    List<Articulo> findByUsuario(Usuario usuario);

    List<Articulo> findByEstadoAprobacion(String estadoAprobacion);

    List<Articulo> findByCategoria(String categoria);

    List<Articulo> findByEstadoArticulo(String estadoArticulo);

    List<Articulo> findByNombreContainingIgnoreCaseAndEstadoAprobacion(String nombre, String estado);

    List<Articulo> findByPrecioBetweenAndEstadoAprobacion(BigDecimal precioMin, BigDecimal precioMax, String estado);

    List<Articulo> findByStockGreaterThan(Integer stock);

    List<Articulo> findByUsuarioAndEstadoAprobacion(Usuario usuario, String estadoAprobacion);

    Long countByEstadoAprobacion(String estadoAprobacion);

    @Query("SELECT u.id, u.nombre, u.email, "
            + "COUNT(a) as totalProductos, "
            + "SUM(a.precio) as valorTotal "
            + "FROM Articulo a "
            + "JOIN a.usuario u "
            + "GROUP BY u.id, u.nombre, u.email "
            + "ORDER BY totalProductos DESC "
            + "LIMIT 10")
    List<Object[]> findTop10ClientesMasProductosVenta();

}
