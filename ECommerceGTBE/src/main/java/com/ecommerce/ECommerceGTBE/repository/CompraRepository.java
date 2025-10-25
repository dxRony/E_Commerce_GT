/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.repository;

import com.ecommerce.ECommerceGTBE.model.Compra;
import com.ecommerce.ECommerceGTBE.model.Usuario;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author ronyrojas
 */
public interface CompraRepository extends JpaRepository<Compra, Integer> {

    List<Compra> findByUsuario(Usuario usuario);

    List<Compra> findByEstadoEntrega(String estadoEntrega);

    List<Compra> findByUsuarioAndEstadoEntrega(Usuario usuario, String estadoEntrega);

    List<Compra> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<Compra> findByUsuarioAndFechaBetween(Usuario usuario, LocalDateTime fechaInicio, LocalDateTime fechaFin);

    @Query("SELECT SUM(c.total) FROM Compra c WHERE c.fecha BETWEEN :fechaInicio AND :fechaFin")
    BigDecimal sumTotalByFechaBetween(@Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT COUNT(c) FROM Compra c WHERE c.usuario = :usuario AND c.fecha BETWEEN :fechaInicio AND :fechaFin")
    Long countByUsuarioAndFechaBetween(@Param("usuario") Usuario usuario,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT SUM(c.comisionPagina) FROM Compra c WHERE c.fecha BETWEEN :fechaInicio AND :fechaFin")
    BigDecimal sumComisionPaginaByFechaBetween(@Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);

    List<Compra> findByOrderByFechaDesc();

    List<Compra> findByUsuarioOrderByFechaDesc(Usuario usuario);

    Boolean existsByUsuario(Usuario usuario);

    @Query("SELECT u.id, u.nombre, u.email, COUNT(c) as totalCompras, "
            + "SUM(c.total) as totalGastado, SUM(c.comisionPagina) as gananciaGenerada "
            + "FROM Compra c "
            + "JOIN c.usuario u "
            + "WHERE c.fecha BETWEEN :fechaInicio AND :fechaFin "
            + "GROUP BY u.id, u.nombre, u.email "
            + "ORDER BY gananciaGenerada DESC "
            + "LIMIT 5")
    List<Object[]> findTop5ClientesMasGanancias(@Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);
}
