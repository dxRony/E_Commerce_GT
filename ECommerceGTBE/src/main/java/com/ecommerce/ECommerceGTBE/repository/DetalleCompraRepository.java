/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.repository;

import com.ecommerce.ECommerceGTBE.model.Articulo;
import com.ecommerce.ECommerceGTBE.model.Compra;
import com.ecommerce.ECommerceGTBE.model.DetalleCompra;
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
public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Integer> {

    List<DetalleCompra> findByCompra(Compra compra);

    List<DetalleCompra> findByArticulo(Articulo articulo);

    @Query("SELECT dc FROM DetalleCompra dc WHERE dc.articulo.usuario = :vendedor")
    List<DetalleCompra> findByVendedor(@Param("vendedor") Usuario vendedor);

    @Query("SELECT SUM(dc.subtotal) FROM DetalleCompra dc WHERE dc.articulo.usuario = :vendedor AND dc.compra.fecha BETWEEN :fechaInicio AND :fechaFin")
    BigDecimal sumVentasByVendedorAndFechaBetween(@Param("vendedor") Usuario vendedor,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT COUNT(dc) FROM DetalleCompra dc WHERE dc.articulo.usuario = :vendedor")
    Long countByVendedor(@Param("vendedor") Usuario vendedor);

    @Query("SELECT dc.articulo, SUM(dc.cantidad) as totalVendido FROM DetalleCompra dc WHERE dc.compra.fecha BETWEEN :fechaInicio AND :fechaFin GROUP BY dc.articulo ORDER BY totalVendido DESC")
    List<Object[]> findTopProductosVendidos(@Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT SUM(dc.cantidad) FROM DetalleCompra dc WHERE dc.articulo = :articulo")
    Integer sumCantidadVendidaByArticulo(@Param("articulo") Articulo articulo);

    Boolean existsByArticulo(Articulo articulo);

    @Query("SELECT COUNT(dc) > 0 FROM DetalleCompra dc WHERE dc.compra.usuario.id = :usuarioId AND dc.articulo.id = :articuloId")
    Boolean existsByUsuarioAndArticulo(@Param("usuarioId") Integer usuarioId, @Param("articuloId") Integer articuloId);

    @Query("SELECT a.id, a.nombre, a.categoria, SUM(dc.cantidad) as totalCantidad, "
            + "SUM(dc.subtotal) as totalVendido, u.nombre "
            + "FROM DetalleCompra dc "
            + "JOIN dc.articulo a "
            + "JOIN a.usuario u "
            + "JOIN dc.compra c "
            + "WHERE c.fecha BETWEEN :fechaInicio AND :fechaFin "
            + "GROUP BY a.id, a.nombre, a.categoria, u.nombre "
            + "ORDER BY totalCantidad DESC "
            + "LIMIT 10")
    List<Object[]> findTop10ProductosMasVendidos(@Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);
}
