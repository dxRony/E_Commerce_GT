/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.dto.response.tarjeta;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ronyrojas
 */
@Getter
@Setter
public class TarjetaResponse {

    private Integer id;
    private String numeracion;
    private LocalDate fechaVencimiento;
    private Boolean activa;

    public TarjetaResponse(Integer id, String numeracion, LocalDate fechaVencimiento, Boolean activa) {
        this.id = id;
        this.numeracion = numeracion;
        this.fechaVencimiento = fechaVencimiento;
        this.activa = activa;
    }

}
