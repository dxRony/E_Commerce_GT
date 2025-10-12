/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.controller;

import com.ecommerce.ECommerceGTBE.dto.request.usuario.ChangePasswordRequest;
import com.ecommerce.ECommerceGTBE.dto.request.usuario.UpdateUsuarioRequest;
import com.ecommerce.ECommerceGTBE.dto.response.auth.MensajeResponse;
import com.ecommerce.ECommerceGTBE.dto.response.usuario.UsuarioResponse;
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

        throw new RuntimeException("No se pudo obtener el ID del usuario autenticado");
    }

    // usuarios en sesion
    @GetMapping("/perfil")
    @PreAuthorize("hasAnyRole('COMUN', 'MODERADOR', 'LOGISTICA', 'ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponse> getMiPerfil() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Integer usuarioId = this.obtenerIdUsuarioSesion();
        if (usuarioId == null) {
            return ResponseEntity.status(403).build();
        }

        Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UsuarioResponse response = new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getCelular(),
                usuario.getDireccion(),
                usuario.getRol()
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/perfil")
    @PreAuthorize("hasAnyRole('COMUN', 'MODERADOR', 'LOGISTICA', 'ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponse> updatePerfil(@Valid @RequestBody UpdateUsuarioRequest updateRequest) {
        Integer usuarioId = obtenerIdUsuarioSesion();

        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setNombre(updateRequest.getNombre());
        usuarioActualizado.setCelular(updateRequest.getCelular());
        usuarioActualizado.setDireccion(updateRequest.getDireccion());

        Usuario usuario = usuarioService.updateUsuario(usuarioId, usuarioActualizado);

        UsuarioResponse response = new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getCelular(),
                usuario.getDireccion(),
                usuario.getRol()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/cambiar-pass")
    @PreAuthorize("hasAnyRole('COMUN', 'MODERADOR', 'LOGISTICA', 'ADMINISTRADOR')")
    public ResponseEntity<MensajeResponse> changePassword(@Valid @RequestBody ChangePasswordRequest passwordRequest) {
        Integer usuarioId = this.obtenerIdUsuarioSesion();

        usuarioService.cambiarPassword(
                usuarioId,
                passwordRequest.getPasswordActual(),
                passwordRequest.getPasswordNueva()
        );

        return ResponseEntity.ok(new MensajeResponse("Contraseña actualizada"));
    }

    // admins
    @GetMapping("/admin/usuarios")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<UsuarioResponse>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.findAll();

        List<UsuarioResponse> response = usuarios.stream()
                .map(usuario -> new UsuarioResponse(
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

    @GetMapping("/admin/usuarios/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponse> getUsuarioById(@PathVariable Integer id) {
        Usuario usuario = usuarioService.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        UsuarioResponse response = new UsuarioResponse(
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

    @PutMapping("/admin/usuarios/{id}/suspender")
    @PreAuthorize("hasAnyRole('MODERADOR', 'ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponse> suspenderUsuario(@PathVariable Integer id) {
        Usuario usuario = usuarioService.alternarActivoUsuario(id);

        UsuarioResponse response = new UsuarioResponse(
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

    @GetMapping("/admin/usuarios/rol/{rol}")
    @PreAuthorize("hasAnyRole('MODERADOR', 'ADMINISTRADOR')")
    public ResponseEntity<List<UsuarioResponse>> getUsuariosByRol(@PathVariable Integer rol) {
        List<Usuario> usuarios = usuarioService.findByRol(rol);

        List<UsuarioResponse> response = usuarios.stream()
                .map(usuario -> new UsuarioResponse(
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

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MensajeResponse> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(new MensajeResponse(ex.getMessage()));
    }
}
