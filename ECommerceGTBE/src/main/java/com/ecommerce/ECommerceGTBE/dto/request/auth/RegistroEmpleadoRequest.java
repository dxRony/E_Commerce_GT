/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.dto.request.auth;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ronyrojas
 */
@Getter
@Setter
public class RegistroEmpleadoRequest {

    @NotBlank(message = "nombre obligatorio")
    @Size(min = 2, max = 100, message = "el nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "email obligatorio")
    @Email(message = "el email debe tener un formato valido")
    private String email;

    @NotBlank(message = "contrasenia obligatoria")
    @Size(min = 6, message = "la contrasenia debe tener al menos 6 caracteres")
    private String password;

    @NotNull(message = "celular obligatorio")
    private Integer celular;

    @NotBlank(message = "direccion obligatoria")
    @Size(max = 200, message = "la direccion debe ser menor de 200 caracteres")
    private String direccion;

    @NotNull(message = "el rol es obligatorio")
    @Min(value = 2, message = "el rol debe ser un numero entre 2 y 4.\n2-Moderador, 3-Logistica, 4-Administrador")
    @Max(value = 4, message = "el rol debe ser un numero entre 2 y 4.\n2-Moderador, 3-Logistica, 4-Administrador")
    private Integer rol;
}
