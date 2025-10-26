/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.dto.response.reporte;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ronyrojas
 */
@Getter
@Setter
public class Reporte5Response {

    private Integer vendedorId;
    private String vendedorNombre;
    private String vendedorEmail;
    private Integer totalProductos;

    public Reporte5Response(Integer vendedorId, String vendedorNombre,
            String vendedorEmail, Integer totalProductos) {

        this.vendedorId = vendedorId;
        this.vendedorNombre = vendedorNombre;
        this.vendedorEmail = vendedorEmail;
        this.totalProductos = totalProductos;
    }
}
