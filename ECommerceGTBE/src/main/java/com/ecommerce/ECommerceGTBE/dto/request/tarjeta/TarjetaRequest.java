/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.dto.request.tarjeta;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ronyrojas
 */
@Getter
@Setter
public class TarjetaRequest {

    @NotBlank(message = "La numeracion de la tarjeta es obligatoria")
    @Pattern(regexp = "^[0-9]{16,19}$", message = "La numeracion no es valida")
    private String numeracion;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    @Future(message = "La tarjeta debe ser vigente")
    private LocalDate fechaVencimiento;

    @NotBlank(message = "El CVV es obligatorio")
    @Pattern(regexp = "^[0-9]{3,4}$", message = "El CVV debe tener 3 o 4 numeros")
    private String cvv;


}
