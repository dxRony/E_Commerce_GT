/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.repository;

import com.ecommerce.ECommerceGTBE.model.Articulo;
import com.ecommerce.ECommerceGTBE.model.RatingArticulo;
import com.ecommerce.ECommerceGTBE.model.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author ronyrojas
 */
public interface RatingArticuloRepository extends JpaRepository<RatingArticulo, Integer> {

    Optional<RatingArticulo> findByArticuloAndUsuario(Articulo articulo, Usuario usuario);

    List<RatingArticulo> findByArticulo(Articulo articulo);

    List<RatingArticulo> findByUsuario(Usuario usuario);

    Boolean existsByArticuloAndUsuario(Articulo articulo, Usuario usuario);

    @Query("SELECT AVG(r.puntuacion) FROM RatingArticulo r WHERE r.articulo = :articulo")
    Double findAverageRatingByArticulo(@Param("articulo") Articulo articulo);

    Long countByArticulo(Articulo articulo);

    List<RatingArticulo> findByArticuloAndPuntuacion(Articulo articulo, Integer puntuacion);

    void deleteByArticuloAndUsuario(Articulo articulo, Usuario usuario);
}
