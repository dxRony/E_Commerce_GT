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
import java.util.stream.Collectors;

/**
 *
 * @author ronyrojas
 */
@Service
public class AdminService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario registrarEmpleado(Usuario empleado) {
        if (usuarioRepository.existsByEmail(empleado.getEmail())) {
            throw new RuntimeException("email no disponible");
        }

        if (empleado.getRol() < 2 || empleado.getRol() > 4) {
            throw new RuntimeException("eol invalido (2-4)");
        }

        empleado.setPassword(passwordEncoder.encode(empleado.getPassword()));
        empleado.setSuspendido(false);

        return usuarioRepository.save(empleado);
    }

    public List<Usuario> obtenerEmpleados() {
        return usuarioRepository.findAll().stream()
                .filter(usuario -> usuario.getRol() >= 2 && usuario.getRol() <= 4)
                .collect(Collectors.toList());
    }

    public List<Usuario> obtenerEmpleadosPorRol(Integer rol) {
        if (rol < 2 || rol > 4) {
            throw new RuntimeException("rol invalido 1-4");
        }
        return usuarioRepository.findByRol(rol);
    }

    public Usuario obtenerEmpleado(Integer id) {
        return usuarioRepository.findById(id)
                .filter(usuario -> usuario.getRol() >= 2 && usuario.getRol() <= 4)
                .orElseThrow(() -> new RuntimeException("empleado no encontrado"));
    }

    public Usuario actualizarEmpleado(Integer id, Usuario empleadoActualizado) {
        return usuarioRepository.findById(id)
                .map(empleado -> {
                    if (empleado.getRol() < 2 || empleado.getRol() > 4) {
                        throw new RuntimeException("solo actualizar empleados");
                    }

                    empleado.setNombre(empleadoActualizado.getNombre());
                    empleado.setCelular(empleadoActualizado.getCelular());
                    empleado.setDireccion(empleadoActualizado.getDireccion());

                    return usuarioRepository.save(empleado);
                })
                .orElseThrow(() -> new RuntimeException("empleado no encontrado con id: " + id));
    }

    public Usuario alternarActivoEmpleado(Integer id) {
        return usuarioRepository.findById(id)
                .map(empleado -> {
                    if (empleado.getRol() < 2 || empleado.getRol() > 4) {
                        throw new RuntimeException("solo suspender empleados");
                    }

                    empleado.setSuspendido(!empleado.getSuspendido());
                    return usuarioRepository.save(empleado);
                })
                .orElseThrow(() -> new RuntimeException("empleado no encontrado con id: " + id));
    }

}
