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

    Long countByUsuarioSancionado(Usuario usuario);

    Long countByUsuarioModerador(Usuario moderador);

    List<Sancion> findByOrderByFechaDesc();

    List<Sancion> findByMotivoContainingIgnoreCase(String motivo);
}
