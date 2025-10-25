/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.dto.response.reporte;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ronyrojas
 */
@Getter
@Setter
public class Reporte2Response {

    private Integer clienteId;
    private String clienteNombre;
    private String clienteEmail;
    private Integer totalCompras;
    private BigDecimal totalGastado;
    private BigDecimal gananciaGenerada;

    public Reporte2Response(Integer clienteId, String clienteNombre,
            String clienteEmail, Integer totalCompras,
            BigDecimal totalGastado, BigDecimal gananciaGenerada) {
        this.clienteId = clienteId;
        this.clienteNombre = clienteNombre;
        this.clienteEmail = clienteEmail;
        this.totalCompras = totalCompras;
        this.totalGastado = totalGastado;
        this.gananciaGenerada = gananciaGenerada;
    }
}
