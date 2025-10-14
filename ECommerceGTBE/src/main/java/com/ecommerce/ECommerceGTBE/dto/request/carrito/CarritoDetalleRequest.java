/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.dto.request.carrito;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ronyrojas
 */
@Getter
@Setter
public class CarritoDetalleRequest {

    @NotNull(message = "El ID del articulo es obligatorio")
    private Integer articuloId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La minima cantidad es 1")
    private Integer cantidad;

}
