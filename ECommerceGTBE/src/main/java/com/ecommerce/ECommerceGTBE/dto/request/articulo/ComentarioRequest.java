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
@Setter
@Getter
public class ComentarioRequest {
    @NotNull(message = "ID del articulo obligatorio")
    private Integer articuloId;
    
    @NotBlank(message = "comentario obligatorio")
    @Size(max = 250, message = "comentario deber ser menor de 250 caracteres")
    private String comentario;
}
