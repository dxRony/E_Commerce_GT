/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.service;

import com.ecommerce.ECommerceGTBE.model.Usuario;
import com.ecommerce.ECommerceGTBE.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * busca a todos los usuarios
     * @return lista de usuarios
     */
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    /**
     * busca un usuario por su id
     * @param id del user
     * @return usuario si es encontrado
     */
    public Optional<Usuario> findById(Integer id) {
        return usuarioRepository.findById(id);
    }

    /**
     * busca a un usuario por su email
     * @param email del usuario
     * @return usuario si lo encuenta
     */
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    /**
     * actualiza un usuario 
     * @param id del usuario
     * @param usuarioUpdate con la informacion actualizada
     * @return conformacion de la operacion
     */
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

    /**
     * actualiza el perfil de un usuario comun
     * @param id del usuario
     * @param usuarioActualizado con la informacion
     * @return confirmacion de la operacion
     */
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

    /**
     * alterna el atrib suspemdio de un usuario
     * @param id del usuario
     * @return confirmacion de la operacion
     */
    public Usuario alternarActivoUsuario(Integer id) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setSuspendido(!usuario.getSuspendido());
                    return usuarioRepository.save(usuario);
                })
                .orElseThrow(() -> new RuntimeException("user no encontrado con id: " + id));
    }

    /**
     * busca un usuario por si emal
     * @param email a buscar
     * @return confirmacion de la busqueda
     */
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    /**
     * busca usuarios por su rol
     * @param rol del usuario
     * @return lista de usuarios con el rol coincidente
     */
    public List<Usuario> findByRol(Integer rol) {
        return usuarioRepository.findByRol(rol);
    }

    /**
     * busca usuarios con  suspendido = true
     * @return lista de usuarios coincidentes
     */
    public List<Usuario> findSuspendidos() {
        return usuarioRepository.findBySuspendidoTrue();
    }

    /**
     * cuenta los usuarios de un rol
     * @param rol a contar
     * @return conteo de usuarios
     */
    public Long countByRol(Integer rol) {
        return usuarioRepository.countByRol(rol);
    }

}
