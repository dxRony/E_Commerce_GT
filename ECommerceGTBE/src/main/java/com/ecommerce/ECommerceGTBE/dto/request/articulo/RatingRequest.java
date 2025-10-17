/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.dto.request.articulo;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ronyrojas
 */
@Getter
@Setter
public class RatingRequest {

    @NotNull(message = "ID del articulo obligatorio")
    private Integer articuloId;

    @NotNull(message = "puntuacion es obligatoria")
    @Min(value = 1, message = "La puntuacion minima debe ser 1")
    @Max(value = 5, message = "La puntuacion maxima debe ser 5")
    private Integer puntuacion;
}
