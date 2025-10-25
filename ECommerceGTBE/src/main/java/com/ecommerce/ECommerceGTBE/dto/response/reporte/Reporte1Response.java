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
public class Reporte1Response {

    private Integer productoId;
    private String productoNombre;
    private String productoCategoria;
    private Integer cantidadVendida;
    private BigDecimal totalVendido;
    private String vendedorNombre;

    public Reporte1Response(Integer productoId, String productoNombre,
            String productoCategoria, Integer cantidadVendida,
            BigDecimal totalVendido, String vendedorNombre) {
        this.productoId = productoId;
        this.productoNombre = productoNombre;
        this.productoCategoria = productoCategoria;
        this.cantidadVendida = cantidadVendida;
        this.totalVendido = totalVendido;
        this.vendedorNombre = vendedorNombre;
    }

}
