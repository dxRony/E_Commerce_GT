/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ronyrojas
 */
@Entity
@Table(name = "compra")
@Data
public class Compra {

    public Compra() {
        this.detallesCompra = new ArrayList<>();
        this.entregas = new ArrayList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "estado_entrega", nullable = false, length = 15)
    private String estadoEntrega;

    @Column(name = "comision_usuario", nullable = false, precision = 10, scale = 2)
    private BigDecimal comisionUsuario;

    @Column(name = "comision_pagina", nullable = false, precision = 10, scale = 2)
    private BigDecimal comisionPagina;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_tarjeta", nullable = false)
    private Tarjeta tarjeta;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL)
    private List<DetalleCompra> detallesCompra;

    @OneToMany(mappedBy = "compra")
    private List<Entrega> entregas;

}
