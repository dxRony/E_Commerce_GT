/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.controller;

import com.ecommerce.ECommerceGTBE.dto.request.articulo.ArticuloRequest;
import com.ecommerce.ECommerceGTBE.dto.response.articulo.ArticuloResponse;
import com.ecommerce.ECommerceGTBE.dto.response.auth.MensajeResponse;
import com.ecommerce.ECommerceGTBE.model.Articulo;
import com.ecommerce.ECommerceGTBE.model.Usuario;
import com.ecommerce.ECommerceGTBE.service.ArticuloService;
import com.ecommerce.ECommerceGTBE.service.UsuarioService;
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
@RequestMapping("/api/articulos")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ArticuloController {

    @Autowired
    private ArticuloService articuloService;

    @Autowired
    private UsuarioService usuarioService;

    private Integer obtenerIdUsuarioSesion() {
        Object userIdAttr = org.springframework.web.context.request.RequestContextHolder
                .getRequestAttributes().getAttribute("userId", org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST);

        if (userIdAttr != null) {
            return (Integer) userIdAttr;
        }

        throw new RuntimeException("no se pudo obtener el id del usuario");
    }

    // usuarios sin sesion
    @GetMapping("/public/catalogo")
    public ResponseEntity<List<ArticuloResponse>> getCatalogoPublico() {
        List<Articulo> articulos = articuloService.findAprobados();

        List<ArticuloResponse> response = articulos.stream()
                .map(this::crearResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/public/buscar")
    public ResponseEntity<List<ArticuloResponse>> buscarArticulos(@RequestParam String nombre) {
        List<Articulo> articulos = articuloService.buscarPorNombre(nombre);

        List<ArticuloResponse> response = articulos.stream()
                .map(this::crearResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/public/categoria/{categoria}")
    public ResponseEntity<List<ArticuloResponse>> getArticulosPorCategoria(@PathVariable String categoria) {
        List<Articulo> articulos = articuloService.findByCategoria(categoria);

        List<Articulo> articulosAprobados = articulos.stream()
                .filter(a -> "Aprobado".equals(a.getEstadoAprobacion()))
                .collect(Collectors.toList());

        List<ArticuloResponse> response = articulosAprobados.stream()
                .map(this::crearResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<ArticuloResponse> getArticuloPublico(@PathVariable Integer id) {
        Articulo articulo = articuloService.findById(id)
                .orElseThrow(() -> new RuntimeException("articuol no encontrado"));

        if (!"Aprobado".equals(articulo.getEstadoAprobacion())) {
            throw new RuntimeException("articulo no disponible");
        }

        ArticuloResponse response = crearResponse(articulo);
        return ResponseEntity.ok(response);
    }

    // usuarios comunes en sesion
    @GetMapping("/mis-articulos")
    @PreAuthorize("hasRole('COMUN')")
    public ResponseEntity<List<ArticuloResponse>> getMisArticulos() {
        Integer usuarioId = obtenerIdUsuarioSesion();
        Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("user no encontrado"));

        List<Articulo> articulos = articuloService.findByUsuario(usuario);

        List<ArticuloResponse> response = articulos.stream()
                .map(this::crearResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('COMUN')")
    public ResponseEntity<ArticuloResponse> crearArticulo(@Valid @RequestBody ArticuloRequest articuloRequest) {
        Integer usuarioId = obtenerIdUsuarioSesion();
        Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("user no encontrado"));

        Articulo articulo = new Articulo();
        articulo.setNombre(articuloRequest.getNombre());
        articulo.setDescripcion(articuloRequest.getDescripcion());
        articulo.setImagen(articuloRequest.getImagen());
        articulo.setPrecio(articuloRequest.getPrecio());
        articulo.setStock(articuloRequest.getStock());
        articulo.setEstadoArticulo(articuloRequest.getEstadoArticulo());
        articulo.setCategoria(articuloRequest.getCategoria());

        Articulo articuloCreado = articuloService.crearArticulo(articulo, usuario);

        ArticuloResponse response = crearResponse(articuloCreado);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('COMUN')")
    public ResponseEntity<ArticuloResponse> updateArticulo(
            @PathVariable Integer id,
            @Valid @RequestBody ArticuloRequest articuloRequest) {

        Articulo articuloExistente = articuloService.findById(id)
                .orElseThrow(() -> new RuntimeException("articulo no encontrado"));

        Integer usuarioId = obtenerIdUsuarioSesion();
        if (!articuloExistente.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("nno puedes actualizar este articulo");
        }

        Articulo articuloActualizado = new Articulo();
        articuloActualizado.setNombre(articuloRequest.getNombre());
        articuloActualizado.setDescripcion(articuloRequest.getDescripcion());
        articuloActualizado.setImagen(articuloRequest.getImagen());
        articuloActualizado.setPrecio(articuloRequest.getPrecio());
        articuloActualizado.setStock(articuloRequest.getStock());
        articuloActualizado.setEstadoArticulo(articuloRequest.getEstadoArticulo());
        articuloActualizado.setCategoria(articuloRequest.getCategoria());

        Articulo articulo = articuloService.updateArticulo(id, articuloActualizado);

        ArticuloResponse response = crearResponse(articulo);
        return ResponseEntity.ok(response);
    }

    // moderadores en sesion
    @GetMapping("/moderador/pendientes")
    @PreAuthorize("hasRole('MODERADOR')")
    public ResponseEntity<List<ArticuloResponse>> getArticulosPendientes() {
        List<Articulo> articulos = articuloService.findByEstadoAprobacion("Pendiente");

        List<ArticuloResponse> response = articulos.stream()
                .map(this::crearResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/moderador/{id}/aprobar")
    @PreAuthorize("hasRole('MODERADOR')")
    public ResponseEntity<ArticuloResponse> aprobarArticulo(@PathVariable Integer id) {
        Articulo articulo = articuloService.aprobarArticulo(id);

        ArticuloResponse response = crearResponse(articulo);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/moderador/{id}/rechazar")
    @PreAuthorize("hasRole('MODERADOR')")
    public ResponseEntity<ArticuloResponse> rechazarArticulo(@PathVariable Integer id) {
        Articulo articulo = articuloService.rechazarArticulo(id);

        ArticuloResponse response = crearResponse(articulo);
        return ResponseEntity.ok(response);
    }

    private ArticuloResponse crearResponse(Articulo articulo) {
        ArticuloResponse response = new ArticuloResponse(
                articulo.getId(),
                articulo.getNombre(),
                articulo.getDescripcion(),
                articulo.getImagen(),
                articulo.getPrecio(),
                articulo.getStock(),
                articulo.getEstadoArticulo(),
                articulo.getCategoria(),
                articulo.getEstadoAprobacion(),
                articulo.getUsuario().getId(),
                articulo.getUsuario().getNombre()
        );
        return response;
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MensajeResponse> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(new MensajeResponse(ex.getMessage()));
    }

}
