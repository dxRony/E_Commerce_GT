/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.controller;

import com.ecommerce.ECommerceGTBE.dto.request.auth.RegistroEmpleadoRequest;
import com.ecommerce.ECommerceGTBE.dto.request.reporte.ReporteRequest;
import com.ecommerce.ECommerceGTBE.dto.request.usuario.ActualizarEmpleadoRequest;
import com.ecommerce.ECommerceGTBE.dto.response.auth.MensajeResponse;
import com.ecommerce.ECommerceGTBE.dto.response.reporte.Reporte1Response;
import com.ecommerce.ECommerceGTBE.dto.response.reporte.Reporte2Response;
import com.ecommerce.ECommerceGTBE.dto.response.reporte.Reporte3Response;
import com.ecommerce.ECommerceGTBE.dto.response.reporte.Reporte4Response;
import com.ecommerce.ECommerceGTBE.dto.response.reporte.Reporte5Response;
import com.ecommerce.ECommerceGTBE.dto.response.usuario.EmpleadoResponse;
import com.ecommerce.ECommerceGTBE.model.Usuario;
import com.ecommerce.ECommerceGTBE.service.AdminService;
import com.ecommerce.ECommerceGTBE.service.ReporteService;
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

    @Autowired
    private ReporteService reporteService;

    // admins en sesion
    /**
     * registra un empleado
     *
     * @param request del empleado
     * @return confirmacion de la operacion
     */
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

    /**
     * obtiene todos los empleados rol 2-4
     * @return lista de empleados
     */
    @GetMapping("/empleados")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<EmpleadoResponse>> obtenerTodosEmpleados() {
        List<Usuario> empleados = adminService.obtenerEmpleados();

        List<EmpleadoResponse> response = empleados.stream()
                .map(this::crearResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * obtiene todos los empleados por su rol
     * @param rol a buscar
     * @return lista de empleados
     */
    @GetMapping("/empleados/rol/{rol}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<EmpleadoResponse>> obtenerEmpleadosPorRol(@PathVariable Integer rol) {
        List<Usuario> empleados = adminService.obtenerEmpleadosPorRol(rol);

        List<EmpleadoResponse> response = empleados.stream()
                .map(this::crearResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * obtiene un empleado
     * @param id del empleado
     * @return empleado si existe
     */
    @GetMapping("/empleados/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<EmpleadoResponse> obtenerEmpleado(@PathVariable Integer id) {
        Usuario empleado = adminService.obtenerEmpleado(id);

        EmpleadoResponse response = crearResponse(empleado);
        return ResponseEntity.ok(response);
    }

    /**
     * actualiza un empleado
     * @param id del empleado
     * @param request del empleado a actualizar
     * @return confirmacion de la operacion
     */
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

    /**
     * alterna el atributo suspendido de un empleado
     * @param id
     * @return
     */
    @PutMapping("/empleados/{id}/suspender")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<EmpleadoResponse> alternarActivoEmpleado(@PathVariable Integer id) {
        Usuario empleado = adminService.alternarActivoEmpleado(id);

        EmpleadoResponse response = crearResponse(empleado);
        return ResponseEntity.ok(response);
    }

    /**
     * obtiene lista de productos del reporte1
     * @param request del reporte 1
     * @return lista de productos del reporte1
     */
    @PostMapping("/reportes/top10-productos-vendidos")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<Reporte1Response>> obtenerTop10ProductosVendidos(
            @Valid @RequestBody ReporteRequest request) {

        List<Reporte1Response> productos = reporteService
                .obtenerTop10ProductosMasVendidos(request.getFechaInicio(), request.getFechaFin());

        return ResponseEntity.ok(productos);
    }

    /**
     * obtiene lista de clientes del reporte2
     * @param request del reporte2
     * @return lista de clientes del reporte2
     */
    @PostMapping("/reportes/top5-clientes-ganancias")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<Reporte2Response>> obtenerTop5ClientesMasGanancias(
            @Valid @RequestBody ReporteRequest request) {

        List<Reporte2Response> clientes = reporteService
                .obtenerTop5ClientesMasGanancias(request.getFechaInicio(), request.getFechaFin());

        return ResponseEntity.ok(clientes);
    }

    /**
     * obtiene la lista del reporte3
     * @param request del reporte3
     * @return lista de clientes del reporte3
     */
    @PostMapping("/reportes/top5-clientes-ventas")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<Reporte3Response>> obtenerTop5ClientesMasVentas(
            @Valid @RequestBody ReporteRequest request) {

        List<Reporte3Response> vendedores = reporteService
                .obtenerTop5ClientesMasVentas(request.getFechaInicio(), request.getFechaFin());
        return ResponseEntity.ok(vendedores);
    }

    /**
     * obtiene lista de clientes del reporte4
     * @param request del reporte4
     * @return lista de clientes del reporte4
     */
    @PostMapping("/reportes/top10-clientes-pedidos")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<Reporte4Response>> obtenerTop10ClientesMasPedidos(
            @Valid @RequestBody ReporteRequest request) {

        List<Reporte4Response> clientes = reporteService
                .obtenerTop10ClientesMasPedidos(request.getFechaInicio(), request.getFechaFin());
        return ResponseEntity.ok(clientes);
    }

    /**
     * lista de clientes del reporte5
     * @return lista de clientes del reporte5
     */
    @GetMapping("/reportes/top10-clientes-productos-venta")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<Reporte5Response>> obtenerTop10ClientesMasProductosVenta() {

        List<Reporte5Response> vendedores = reporteService
                .obtenerTop10ClientesMasProductosVenta();

        return ResponseEntity.ok(vendedores);
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
