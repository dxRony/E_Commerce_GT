/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 *
 * @author ronyrojas
 */
@Entity
@Table(name = "notificacion")
@Data
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "tipo", nullable = false, length = 30)
    private String tipo;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Column(name = "descripcion", nullable = false, length = 250)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_entrega")
    private Entrega entrega;

    @ManyToOne
    @JoinColumn(name = "id_articulo")
    private Articulo articulo;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    @Column(name = "fecha_estimada")
    private LocalDateTime fechaEstimada;
    
    @Column(name = "leida", nullable = false)
    private Boolean leida = false;
}
