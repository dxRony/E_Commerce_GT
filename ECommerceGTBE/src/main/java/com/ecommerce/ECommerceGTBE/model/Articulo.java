/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 *
 * @author ronyrojas
 */
@Entity
@Table(name = "articulo")
@Data
public class Articulo {

    public Articulo() {
        this.enCarritos = new ArrayList<>();
        this.comentarios = new ArrayList<>();
        this.ratings = new ArrayList<>();
        this.detallesEnCompras = new ArrayList<>();
        this.detallesEnEntregas = new ArrayList<>();
        this.notificaciones = new ArrayList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "descripcion", nullable = false, length = 250)
    private String descripcion;

    @Column(name = "imagen", nullable = false, length = 500)
    private String imagen;

    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "estado_articulo", nullable = false, length = 10)
    private String estadoArticulo;

    @Column(name = "categoria", nullable = false, length = 20)
    private String categoria;

    @Column(name = "estado_aprobacion", nullable = false, length = 15)
    private String estadoAprobacion;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "articulo")
    private List<DetalleCarrito> enCarritos;

    @OneToMany(mappedBy = "articulo")
    private List<ComentarioArticulo> comentarios;

    @OneToMany(mappedBy = "articulo")
    private List<RatingArticulo> ratings;

    @OneToMany(mappedBy = "articulo")
    private List<DetalleCompra> detallesEnCompras;

    @OneToMany(mappedBy = "articulo")
    private List<DetalleEntrega> detallesEnEntregas;

    @OneToMany(mappedBy = "articulo")
    private List<Notificacion> notificaciones;
}
