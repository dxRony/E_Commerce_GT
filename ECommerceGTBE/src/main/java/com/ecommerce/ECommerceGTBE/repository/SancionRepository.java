/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.repository;

import com.ecommerce.ECommerceGTBE.model.Sancion;
import com.ecommerce.ECommerceGTBE.model.Usuario;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author ronyrojas
 */
public interface SancionRepository extends JpaRepository<Sancion, Integer> {

    List<Sancion> findByUsuarioSancionado(Usuario usuario);

    List<Sancion> findByUsuarioModerador(Usuario moderador);

    List<Sancion> findByEstado(String estado);

    List<Sancion> findByUsuarioSancionadoAndEstado(Usuario usuario, String estado);

    Boolean existsByUsuarioSancionadoAndEstado(Usuario usuario, String estado);

    List<Sancion> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    @Query("SELECT s FROM Sancion s WHERE s.estado = 'En curso' AND s.fecha + s.diasSancion * INTERVAL '1 day' BETWEEN :fechaInicio AND :fechaFin")
    List<Sancion> findSancionesProximasAVencer(@Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);

    Long countByUsuarioSancionado(Usuario usuario);

    Long countByUsuarioModerador(Usuario moderador);

    @Query("SELECT s FROM Sancion s WHERE s.estado = 'En curso' AND s.fecha + s.diasSancion * INTERVAL '1 day' < :fechaActual")
    List<Sancion> findSancionesParaFinalizar(@Param("fechaActual") LocalDateTime fechaActual);

    List<Sancion> findByOrderByFechaDesc();

    List<Sancion> findByMotivoContainingIgnoreCase(String motivo);
}
