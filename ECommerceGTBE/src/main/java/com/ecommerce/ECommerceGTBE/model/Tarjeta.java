/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 *
 * @author ronyrojas
 */
@Entity
@Table(name = "tarjeta")
@Data
public class Tarjeta {

    public Tarjeta() {
        this.compras = new ArrayList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "numeracion", nullable = false, length = 19)
    private String numeracion;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fecha_vencimiento;

    @ManyToOne
    @JoinColumn(name = "id_titular", nullable = false)
    private Usuario titular;

    @Column(name = "cvv", nullable = false, length = 4)
    private String cvv;

    @Column(name = "activa", nullable = false)
    private Boolean activa;

    @OneToMany(mappedBy = "tarjeta")
    private List<Compra> compras;
}
