/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.repository;

import com.ecommerce.ECommerceGTBE.model.Compra;
import com.ecommerce.ECommerceGTBE.model.Entrega;
import java.time.LocalDateTime;
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
public interface EntregaRepository extends JpaRepository<Entrega, Integer> {

    Optional<Entrega> findByCompra(Compra compra);

    List<Entrega> findByEstado(String estado);

    List<Entrega> findByFechaEstimadaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    @Query("SELECT e FROM Entrega e WHERE e.fechaEstimada < :fechaActual AND e.estado = 'En curso'")
    List<Entrega> findEntregasAtrasadas(@Param("fechaActual") LocalDateTime fechaActual);

    List<Entrega> findByFechaEntregaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    Long countByEstado(String estado);

    @Modifying
    @Query("UPDATE Entrega e SET e.estado = :estado, e.fechaEntrega = :fechaEntrega WHERE e.id = :id")
    void updateEstadoAndFechaEntrega(@Param("id") Integer id,
            @Param("estado") String estado,
            @Param("fechaEntrega") LocalDateTime fechaEntrega);

    @Modifying
    @Query("UPDATE Entrega e SET e.fechaEstimada = :fechaEstimada WHERE e.id = :id")
    void updateFechaEstimada(@Param("id") Integer id, @Param("fechaEstimada") LocalDateTime fechaEstimada);

    @Query("SELECT e FROM Entrega e WHERE e.fechaEstimada BETWEEN :fechaInicio AND :fechaFin AND e.estado = 'En curso'")
    List<Entrega> findEntregasProximasAVencer(@Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);

}
