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
@Table(name = "sancion")
@Data
public class Sancion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "motivo", nullable = false, length = 250)
    private String motivo;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "dias_sancion", nullable = false)
    private Integer diasSancion;

    @Column(name = "estado", nullable = false, length = 15)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuarioSancionado;

    @ManyToOne
    @JoinColumn(name = "id_usuario_moderador", nullable = false)
    private Usuario usuarioModerador;
}
