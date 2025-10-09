/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.repository;

import com.ecommerce.ECommerceGTBE.model.Articulo;
import com.ecommerce.ECommerceGTBE.model.Entrega;
import com.ecommerce.ECommerceGTBE.model.Notificacion;
import com.ecommerce.ECommerceGTBE.model.Usuario;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author ronyrojas
 */
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {

    List<Notificacion> findByUsuario(Usuario usuario);

    List<Notificacion> findByUsuarioAndLeidaFalse(Usuario usuario);

    List<Notificacion> findByTipo(String tipo);

    List<Notificacion> findByUsuarioAndTipo(Usuario usuario, String tipo);

    List<Notificacion> findByEntrega(Entrega entrega);

    List<Notificacion> findByArticulo(Articulo articulo);

    List<Notificacion> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<Notificacion> findByUsuarioOrderByFechaDesc(Usuario usuario);

    Long countByUsuarioAndLeidaFalse(Usuario usuario);

    @Modifying
    @Query("UPDATE Notificacion n SET n.leida = true WHERE n.id = :id")
    void marcarComoLeida(@Param("id") Integer id);

    @Modifying
    @Query("UPDATE Notificacion n SET n.leida = true WHERE n.usuario = :usuario")
    void marcarTodasComoLeidas(@Param("usuario") Usuario usuario);

    List<Notificacion> findByEstado(String estado);

    @Query("SELECT n FROM Notificacion n WHERE n.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<Notificacion> findNotificacionesParaEnvio(@Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);

    void deleteByFechaBefore(LocalDateTime fecha);

    @Query("SELECT n.tipo, COUNT(n) FROM Notificacion n GROUP BY n.tipo")
    List<Object[]> countNotificacionesByTipo();

}
