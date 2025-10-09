/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.repository;

import com.ecommerce.ECommerceGTBE.model.Articulo;
import com.ecommerce.ECommerceGTBE.model.DetalleEntrega;
import com.ecommerce.ECommerceGTBE.model.Entrega;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author ronyrojas
 */
public interface DetalleEntregaRepository extends JpaRepository<DetalleEntrega, Integer> {

    List<DetalleEntrega> findByEntrega(Entrega entrega);

    List<DetalleEntrega> findByArticulo(Articulo articulo);

    List<DetalleEntrega> findByListoFalse();

    List<DetalleEntrega> findByEntregaAndListoFalse(Entrega entrega);

    Long countByEntregaAndListoTrue(Entrega entrega);

    Long countByEntrega(Entrega entrega);

    @Query("SELECT COUNT(de) = 0 FROM DetalleEntrega de WHERE de.entrega = :entrega AND de.listo = false")
    Boolean isEntregaCompleta(@Param("entrega") Entrega entrega);

    @Modifying
    @Query("UPDATE DetalleEntrega de SET de.listo = true WHERE de.id = :id")
    void marcarComoListo(@Param("id") Integer id);

    List<DetalleEntrega> findByArticuloIn(List<Articulo> articulos);

    void deleteByEntrega(Entrega entrega);
}
