/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.controller;

import com.ecommerce.ECommerceGTBE.dto.request.usuario.ActualizarVendedorRequest;
import com.ecommerce.ECommerceGTBE.dto.response.auth.MensajeResponse;
import com.ecommerce.ECommerceGTBE.dto.response.usuario.VendedorResponse;
import com.ecommerce.ECommerceGTBE.model.Usuario;
import com.ecommerce.ECommerceGTBE.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 *
 * @author ronyrojas
 */
@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    private Integer obtenerIdUsuarioSesion() {

        Object idUser = RequestContextHolder.getRequestAttributes()
                .getAttribute("userId", RequestAttributes.SCOPE_REQUEST);

        if (idUser != null) {
            return (Integer) idUser;
        }

        throw new RuntimeException("no se pudo obtener el ID del usuario autenticado");
    }

    // usuarios en sesion

    /**
     * obtiene los datos modificables por el mismo usuario comun
     * @return usuario con sus datos
     */
    @GetMapping("/perfil")
    @PreAuthorize("hasAnyRole('COMUN', 'MODERADOR', 'LOGISTICA', 'ADMINISTRADOR')")
    public ResponseEntity<VendedorResponse> getMiPerfil() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Integer usuarioId = this.obtenerIdUsuarioSesion();
        if (usuarioId == null) {
            return ResponseEntity.status(403).build();
        }
        Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("user no encontrado"));

        VendedorResponse response = new VendedorResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getCelular(),
                usuario.getDireccion(),
                usuario.getRol()
        );
        return ResponseEntity.ok(response);
    }

    /**
     * actualiza el perfil del usuario
     * @param updateRequest del perfil a actualizar
     * @return confirmacion de la operacion
     */
    @PutMapping("/perfil")
    @PreAuthorize("hasAnyRole('COMUN', 'MODERADOR', 'LOGISTICA', 'ADMINISTRADOR')")
    public ResponseEntity<VendedorResponse> updatePerfil(@Valid @RequestBody ActualizarVendedorRequest updateRequest) {
        Integer usuarioId = obtenerIdUsuarioSesion();

        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setNombre(updateRequest.getNombre());
        usuarioActualizado.setCelular(updateRequest.getCelular());
        usuarioActualizado.setDireccion(updateRequest.getDireccion());

        Usuario usuario = usuarioService.updateUsuario(usuarioId, usuarioActualizado);

        VendedorResponse response = new VendedorResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getCelular(),
                usuario.getDireccion(),
                usuario.getRol()
        );

        return ResponseEntity.ok(response);
    }

    // admins en sesion

    /**
     * obtiene todos los usuarios para el admin
     * @return lista de usuarios 
     */
    @GetMapping("/admin/usuarios")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<VendedorResponse>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.findAll();

        List<VendedorResponse> response = usuarios.stream()
                .map(usuario -> new VendedorResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getCelular(),
                usuario.getDireccion(),
                usuario.getRol(),
                usuario.getSuspendido()
        ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * obtiene un usuario por su id
     * @param id del usuario
     * @return usuario encontrado
     */
    @GetMapping("/admin/usuarios/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<VendedorResponse> getUsuarioById(@PathVariable Integer id) {
        Usuario usuario = usuarioService.findById(id)
                .orElseThrow(() -> new RuntimeException("usuario no encontrado con id: " + id));

        VendedorResponse response = new VendedorResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getCelular(),
                usuario.getDireccion(),
                usuario.getRol(),
                usuario.getSuspendido()
        );

        return ResponseEntity.ok(response);
    }

    /**
     * suspende a un usuario alternando su atributo suspendido
     * @param id del usuario
     * @return confirmacion de la operacion
     */
    @PutMapping("/admin/usuarios/{id}/suspender")
    @PreAuthorize("hasAnyRole('MODERADOR', 'ADMINISTRADOR')")
    public ResponseEntity<VendedorResponse> suspenderUsuario(@PathVariable Integer id) {
        Usuario usuario = usuarioService.alternarActivoUsuario(id);

        VendedorResponse response = new VendedorResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getCelular(),
                usuario.getDireccion(),
                usuario.getRol(),
                usuario.getSuspendido()
        );
        return ResponseEntity.ok(response);
    }

    /**
     * obtieen a los usarios por su rol
     * @param rol a buscar entre los usuarios
     * @return lista de usuarios con el rol especificado
     */
    @GetMapping("/admin/usuarios/rol/{rol}")
    @PreAuthorize("hasAnyRole('MODERADOR', 'ADMINISTRADOR')")
    public ResponseEntity<List<VendedorResponse>> getUsuariosByRol(@PathVariable Integer rol) {
        List<Usuario> usuarios = usuarioService.findByRol(rol);

        List<VendedorResponse> response = usuarios.stream()
                .map(usuario -> new VendedorResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getCelular(),
                usuario.getDireccion(),
                usuario.getRol(),
                usuario.getSuspendido()
        ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * manejador de errores
     * @param ex
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MensajeResponse> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(new MensajeResponse(ex.getMessage()));
    }
}
