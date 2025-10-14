/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.dto.response.carrito;

import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ronyrojas
 */
@Setter
@Getter
public class CarritoResponse {

    private List<CarritoDetalleResponse> detalles;
    private Integer totalDetalles;
    private BigDecimal totalPrecio;

    public CarritoResponse(List<CarritoDetalleResponse> detalles) {
        this.detalles = detalles;
        this.totalDetalles = detalles.stream().mapToInt(CarritoDetalleResponse::getCantidad).sum();
        this.totalPrecio = detalles.stream()
                .map(CarritoDetalleResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void setDetalles(List<CarritoDetalleResponse> detalles) {
        this.detalles = detalles;
        this.totalDetalles = detalles.stream().mapToInt(CarritoDetalleResponse::getCantidad).sum();
        this.totalPrecio = detalles.stream()
                .map(CarritoDetalleResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
