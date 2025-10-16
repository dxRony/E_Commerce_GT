/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.controller;

import com.ecommerce.ECommerceGTBE.dto.request.auth.RegistroEmpleadoRequest;
import com.ecommerce.ECommerceGTBE.dto.request.usuario.ActualizarEmpleadoRequest;
import com.ecommerce.ECommerceGTBE.dto.response.auth.MensajeResponse;
import com.ecommerce.ECommerceGTBE.dto.response.usuario.EmpleadoResponse;
import com.ecommerce.ECommerceGTBE.model.Usuario;
import com.ecommerce.ECommerceGTBE.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author ronyrojas
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminController {

    @Autowired
    private AdminService adminService;

    // admins en sesion
    @PostMapping("/empleados")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<EmpleadoResponse> registrarEmpleado(@Valid @RequestBody RegistroEmpleadoRequest request) {
        Usuario empleado = new Usuario();
        empleado.setNombre(request.getNombre());
        empleado.setEmail(request.getEmail());
        empleado.setPassword(request.getPassword());
        empleado.setCelular(request.getCelular());
        empleado.setDireccion(request.getDireccion());
        empleado.setRol(request.getRol());

        Usuario empleadoCreado = adminService.registrarEmpleado(empleado);

        EmpleadoResponse response = crearResponse(empleadoCreado);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/empleados")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<EmpleadoResponse>> obtenerTodosEmpleados() {
        List<Usuario> empleados = adminService.obtenerEmpleados();

        List<EmpleadoResponse> response = empleados.stream()
                .map(this::crearResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/empleados/rol/{rol}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<EmpleadoResponse>> obtenerEmpleadosPorRol(@PathVariable Integer rol) {
        List<Usuario> empleados = adminService.obtenerEmpleadosPorRol(rol);

        List<EmpleadoResponse> response = empleados.stream()
                .map(this::crearResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/empleados/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<EmpleadoResponse> obtenerEmpleado(@PathVariable Integer id) {
        Usuario empleado = adminService.obtenerEmpleado(id);

        EmpleadoResponse response = crearResponse(empleado);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/empleados/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<EmpleadoResponse> actualizarEmpleado(
            @PathVariable Integer id,
            @Valid @RequestBody ActualizarEmpleadoRequest request) {

        Usuario empleadoActualizado = new Usuario();
        empleadoActualizado.setNombre(request.getNombre());
        empleadoActualizado.setCelular(request.getCelular());
        empleadoActualizado.setDireccion(request.getDireccion());

        Usuario empleado = adminService.actualizarEmpleado(id, empleadoActualizado);

        EmpleadoResponse response = crearResponse(empleado);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/empleados/{id}/suspender")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<EmpleadoResponse> alternarActivoEmpleado(@PathVariable Integer id) {
        Usuario empleado = adminService.alternarActivoEmpleado(id);

        EmpleadoResponse response = crearResponse(empleado);
        return ResponseEntity.ok(response);
    }

    private EmpleadoResponse crearResponse(Usuario empleado) {
        return new EmpleadoResponse(
                empleado.getId(),
                empleado.getNombre(),
                empleado.getEmail(),
                empleado.getCelular(),
                empleado.getDireccion(),
                empleado.getRol(),
                empleado.getSuspendido()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MensajeResponse> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(new MensajeResponse(ex.getMessage()));
    }

}
