/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.security.controller;

import com.ecommerce.ECommerceGTBE.dto.request.auth.LoginRequest;
import com.ecommerce.ECommerceGTBE.dto.request.auth.RegistroVendedorRequest;
import com.ecommerce.ECommerceGTBE.dto.response.auth.JwtResponse;
import com.ecommerce.ECommerceGTBE.model.Usuario;
import com.ecommerce.ECommerceGTBE.model.UsuarioDetailsImpl;
import com.ecommerce.ECommerceGTBE.repository.UsuarioRepository;
import com.ecommerce.ECommerceGTBE.security.jwt.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 *
 * @author ronyrojas
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // autenticando usuario
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UsuarioDetailsImpl userDetails = (UsuarioDetailsImpl) authentication.getPrincipal();

            // creando token jwt
            String jwt = jwtService.generarToken(userDetails);

            // obteniendo data de usuario
            Usuario usuario = userDetails.getUsuario();

            // respuesta
            return ResponseEntity.ok(new JwtResponse(
                    jwt,
                    usuario.getId(),
                    usuario.getEmail(),
                    usuario.getNombre(),
                    usuario.getRol()
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "eror en el login: " + e.getMessage()));
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@Valid @RequestBody RegistroVendedorRequest registroRequest) {
        try {
            // verificando si email ya existe
            if (usuarioRepository.existsByEmail(registroRequest.getEmail())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Error: email no disponible"));
            }
            // creando nuevo usuario
            Usuario usuario = new Usuario();
            usuario.setNombre(registroRequest.getNombre());
            usuario.setEmail(registroRequest.getEmail());
            usuario.setPassword(passwordEncoder.encode(registroRequest.getPassword()));
            usuario.setCelular(registroRequest.getCelular());
            usuario.setDireccion(registroRequest.getDireccion());
            usuario.setRol(1);
            usuario.setSuspendido(false);

            // guardando usuario en db
            Usuario usuarioGuardado = usuarioRepository.save(usuario);

            // autenticando y generando token al registrar
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(registroRequest.getEmail(), registroRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UsuarioDetailsImpl userDetails = (UsuarioDetailsImpl) authentication.getPrincipal();
            String jwt = jwtService.generarToken(userDetails);

            // respuesta
            return ResponseEntity.ok(new JwtResponse(
                    jwt,
                    usuarioGuardado.getId(),
                    usuarioGuardado.getEmail(),
                    usuarioGuardado.getNombre(),
                    usuarioGuardado.getRol()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "error en registro: " + e.getMessage()));
        }
    }

    @GetMapping("/verificar")
    public ResponseEntity<?> verificarToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (jwtService.validarToken(token)) {
                    String email = jwtService.obtenerUsername(token);
                    Usuario usuario = usuarioRepository.findByEmail(email)
                            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                    return ResponseEntity.ok(Map.of(
                            "valido", true,
                            "usuario", Map.of(
                                    "id", usuario.getId(),
                                    "email", usuario.getEmail(),
                                    "nombre", usuario.getNombre(),
                                    "rol", usuario.getRol()
                            )
                    ));
                }
            }
            return ResponseEntity.ok(Map.of("valido", false));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("valido", false));
        }
    }

}
