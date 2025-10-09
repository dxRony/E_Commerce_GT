/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.repository;

import com.ecommerce.ECommerceGTBE.model.Tarjeta;
import com.ecommerce.ECommerceGTBE.model.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author ronyrojas
 */
public interface TarjetaRepository extends JpaRepository<Tarjeta, Integer> {

    List<Tarjeta> findByTitular(Usuario titular);

    List<Tarjeta> findByTitularAndActivaTrue(Usuario titular);

    Optional<Tarjeta> findByNumeracion(String numeracion);

    Long countByTitularAndActivaTrue(Usuario titular);

    Optional<Tarjeta> findByIdAndTitular(Integer id, Usuario titular);
}
