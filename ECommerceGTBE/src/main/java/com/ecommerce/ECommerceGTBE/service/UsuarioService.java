/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.service;

import com.ecommerce.ECommerceGTBE.model.Usuario;
import com.ecommerce.ECommerceGTBE.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author ronyrojas
 */
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(Integer id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario updateUsuario(Integer id, Usuario usuarioUpdate) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setNombre(usuarioUpdate.getNombre());
                    usuario.setCelular(usuarioUpdate.getCelular());
                    usuario.setDireccion(usuarioUpdate.getDireccion());
                    return usuarioRepository.save(usuario);
                })
                .orElseThrow(() -> new RuntimeException("user no encontrado con id: " + id));
    }

    public Usuario updatePerfil(Integer id, Usuario usuarioActualizado) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setNombre(usuarioActualizado.getNombre());
                    usuario.setCelular(usuarioActualizado.getCelular());
                    usuario.setDireccion(usuarioActualizado.getDireccion());
                    return usuarioRepository.save(usuario);
                })
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    public Usuario alternarActivoUsuario(Integer id) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setSuspendido(!usuario.getSuspendido());
                    return usuarioRepository.save(usuario);
                })
                .orElseThrow(() -> new RuntimeException("user no encontrado con id: " + id));
    }

    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public List<Usuario> findByRol(Integer rol) {
        return usuarioRepository.findByRol(rol);
    }

    public List<Usuario> findSuspendidos() {
        return usuarioRepository.findBySuspendidoTrue();
    }

    public Long countByRol(Integer rol) {
        return usuarioRepository.countByRol(rol);
    }

}
