/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.dto.request.usuario;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ronyrojas
 */
@Getter
@Setter
public class ActualizarEmpleadoRequest {

    @NotBlank(message = "nombre  obligatorio")
    @Size(min = 2, max = 100, message = "nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotNull(message = "celular obligatorio")
    private Integer celular;

    @NotBlank(message = "direccion  obligatoria")
    @Size(max = 200, message = "dirección debe ser menor de 200 caracteres")
    private String direccion;
}
