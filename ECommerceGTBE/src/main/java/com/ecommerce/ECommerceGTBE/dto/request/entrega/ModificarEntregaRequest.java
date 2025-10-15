/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.dto.request.entrega;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ronyrojas
 */
@Getter
@Setter
public class ModificarEntregaRequest {

    @NotNull(message = "La fecha estimada es obligatoria")
    @Future(message = "La fecha estimada debe ser despues de la fecha actual")
    private LocalDateTime fechaEstimada;

}
