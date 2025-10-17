/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.dto.response.articulo;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ronyrojas
 */
@Getter
@Setter
public class RatingResponse {

    private Integer id;
    private Integer puntuacion;
    private LocalDateTime fecha;
    private Integer articuloId;
    private Integer usuarioId;
    private String usuarioNombre;

    public RatingResponse(Integer id, Integer puntuacion, LocalDateTime fecha,
            Integer articuloId, Integer usuarioId, String usuarioNombre) {
        this.id = id;
        this.puntuacion = puntuacion;
        this.fecha = fecha;
        this.articuloId = articuloId;
        this.usuarioId = usuarioId;
        this.usuarioNombre = usuarioNombre;
    }
}
