/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.dto.response.pago;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ronyrojas
 */
@Getter
@Setter
public class DetalleCompraResponse {

    private Integer id;
    private Integer articuloId;
    private String articuloNombre;
    private String articuloImagen;
    private BigDecimal precioUnitario;
    private Integer cantidad;
    private BigDecimal subtotal;
    private Integer vendedorId;
    private String vendedorNombre;

    public DetalleCompraResponse(Integer id, Integer articuloId, String articuloNombre, String articuloImagen, BigDecimal precioUnitario,
            Integer cantidad, BigDecimal subtotal, Integer vendedorId, String vendedorNombre) {
        this.id = id;
        this.articuloId = articuloId;
        this.articuloNombre = articuloNombre;
        this.articuloImagen = articuloImagen;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.vendedorId = vendedorId;
        this.vendedorNombre = vendedorNombre;
    }

}
