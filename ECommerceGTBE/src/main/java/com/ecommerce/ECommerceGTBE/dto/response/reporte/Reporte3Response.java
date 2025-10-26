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
public class Reporte3Response {

    private Integer vendedorId;
    private String vendedorNombre;
    private String vendedorEmail;
    private Integer totalProductosVendidos;
    private Integer totalArticulosVendidos;
    private BigDecimal totalVentas;
    private BigDecimal gananciasVendedor;

    public Reporte3Response(Integer vendedorId, String vendedorNombre,
            String vendedorEmail, Integer totalProductosVendidos,
            Integer totalArticulosVendidos, BigDecimal totalVentas,
            BigDecimal gananciasVendedor) {
        this.vendedorId = vendedorId;
        this.vendedorNombre = vendedorNombre;
        this.vendedorEmail = vendedorEmail;
        this.totalProductosVendidos = totalProductosVendidos;
        this.totalArticulosVendidos = totalArticulosVendidos;
        this.totalVentas = totalVentas;
        this.gananciasVendedor = gananciasVendedor;
    }
}
