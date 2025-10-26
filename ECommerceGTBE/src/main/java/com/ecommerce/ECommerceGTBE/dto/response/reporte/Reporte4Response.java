/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.dto.response.reporte;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ronyrojas
 */
@Getter
@Setter
public class Reporte4Response {

    private Integer clienteId;
    private String clienteNombre;
    private String clienteEmail;
    private Integer totalPedidos;
    private BigDecimal totalGastado;
    private Double promedioPorPedido;
    private LocalDateTime ultimaCompra;

    public Reporte4Response(Integer clienteId, String clienteNombre,
            String clienteEmail, Integer totalPedidos,
            BigDecimal totalGastado, Double promedioPorPedido,
            LocalDateTime ultimaCompra) {
        this.clienteId = clienteId;
        this.clienteNombre = clienteNombre;
        this.clienteEmail = clienteEmail;
        this.totalPedidos = totalPedidos;
        this.totalGastado = totalGastado;
        this.promedioPorPedido = promedioPorPedido;
        this.ultimaCompra = ultimaCompra;
    }
}
