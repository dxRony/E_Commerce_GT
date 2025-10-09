/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ronyrojas
 */
@Entity
@Table(name = "entrega")
@Data
public class Entrega {

    public Entrega() {
        this.detallesEntrega = new ArrayList<>();
        this.notificaciones = new ArrayList<>();
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "estado", nullable = false, length = 10)
    private String estado;

    @Column(name = "fecha_estimada", nullable = false)
    private LocalDateTime fechaEstimada;

    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;

    @ManyToOne
    @JoinColumn(name = "id_compra", nullable = false)
    private Compra compra;

    @OneToMany(mappedBy = "entrega", cascade = CascadeType.ALL)
    private List<DetalleEntrega> detallesEntrega;

    @OneToMany(mappedBy = "entrega")
    private List<Notificacion> notificaciones;

}
