/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.dto.response.carrito;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ronyrojas
 */
@Getter
@Setter
public class CarritoDetalleResponse {

    private Integer id;
    private Integer articuloId;
    private String articuloNombre;
    private String articuloImagen;
    private BigDecimal articuloPrecio;
    private Integer cantidad;
    private BigDecimal subtotal;
    private Integer stockDisponible;

    public CarritoDetalleResponse(Integer id, Integer articuloId, String articuloNombre, String articuloImagen, BigDecimal articuloPrecio,
            Integer cantidad, Integer stockDisponible) {
        this.id = id;
        this.articuloId = articuloId;
        this.articuloNombre = articuloNombre;
        this.articuloImagen = articuloImagen;
        this.articuloPrecio = articuloPrecio;
        this.cantidad = cantidad;
        this.stockDisponible = stockDisponible;
        this.subtotal = articuloPrecio.multiply(BigDecimal.valueOf(cantidad));
    }
}
