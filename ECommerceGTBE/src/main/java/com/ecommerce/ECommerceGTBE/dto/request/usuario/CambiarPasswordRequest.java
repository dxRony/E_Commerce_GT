/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.dto.request.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ronyrojas
 */
@Getter
@Setter
public class CambiarPasswordRequest {

    @NotBlank(message = "contraseña actual es obligatoria")
    private String passwordActual;

    @NotBlank(message = "nueva contraseña es obligatoria")
    @Size(min = 6, message = "nueva contraseña debe tener al menos 6 caracteres")
    private String passwordNueva;
    
}
