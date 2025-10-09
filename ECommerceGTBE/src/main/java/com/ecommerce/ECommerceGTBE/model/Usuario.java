/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import lombok.Data;
import java.util.List;

/**
 *
 * @author ronyrojas
 */
@Entity
@Table(name = "usuario")
@Data
public class Usuario {

    public Usuario() {
        this.tarjetas = new ArrayList<>();
        this.carrito = new ArrayList<>();
        this.comentarios = new ArrayList<>();
        this.articulos = new ArrayList<>();
        this.ratings = new ArrayList<>();
        this.compras = new ArrayList<>();
        this.sancionesRecibidas = new ArrayList<>();
        this.sancionesAplicadas = new ArrayList<>();
        this.notificaciones = new ArrayList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "rol", nullable = false)
    private Integer rol;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "celular", nullable = false)
    private Integer celular;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;

    @Column(name = "suspendido", nullable = false)
    private Boolean suspendido = false;

    @OneToMany(mappedBy = "usuario")
    private List<Articulo> articulos;

    @OneToMany(mappedBy = "titular")
    private List<Tarjeta> tarjetas;

    @OneToMany(mappedBy = "usuario")
    private List<DetalleCarrito> carrito;

    @OneToMany(mappedBy = "usuario")
    private List<ComentarioArticulo> comentarios;

    @OneToMany(mappedBy = "usuario")
    private List<RatingArticulo> ratings;

    @OneToMany(mappedBy = "usuario")
    private List<Compra> compras;
    
    @OneToMany(mappedBy = "usuarioSancionado")
    private List<Sancion> sancionesRecibidas;
    
    @OneToMany(mappedBy = "usuarioModerador")
    private List<Sancion> sancionesAplicadas;
    
    @OneToMany(mappedBy = "usuario")
    private List<Notificacion> notificaciones;
}
