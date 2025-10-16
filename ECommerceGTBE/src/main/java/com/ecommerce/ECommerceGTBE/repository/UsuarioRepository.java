/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.repository;

import com.ecommerce.ECommerceGTBE.model.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author ronyrojas
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmail(String email);

    Boolean existsByEmail(String email);

    List<Usuario> findByRol(Integer rol);

    List<Usuario> findBySuspendidoTrue();

    List<Usuario> findBySuspendidoFalse();

    Long countByRol(Integer rol);

    Long countByRolInAndSuspendidoFalse(List<Integer> roles);

    Long countByRolInAndSuspendidoTrue(List<Integer> roles);
}
