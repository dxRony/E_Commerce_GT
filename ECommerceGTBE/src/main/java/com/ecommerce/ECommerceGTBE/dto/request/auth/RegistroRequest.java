/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ronyrojas
 */
@Getter
@Setter
public class RegistroRequest {

    @NotBlank(message = "nombre obligatorio")
    @Size(min = 2, max = 100, message = "el nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "email obligatorio")
    @Email(message = "email debe tener un formato valido")
    private String email;

    @NotBlank(message = "contrasenia obligatoria")
    @Size(min = 6, message = "la contrasenia debe tener al menos 6 caracteres")
    private String password;

    @NotNull(message = "celular obligatorio")
    private Integer celular;

    @NotBlank(message = "direccion obligatoria")
    @Size(max = 200, message = "direccion debe ser menor de 200 caracteres")
    private String direccion;

}
