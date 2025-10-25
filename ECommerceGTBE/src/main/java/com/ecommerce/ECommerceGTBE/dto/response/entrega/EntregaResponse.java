/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.dto.response.entrega;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ronyrojas
 */
@Getter
@Setter
public class EntregaResponse {

    private Integer id;
    private String estado;
    private LocalDate fechaEstimada;
    private LocalDate fechaEntrega;
    private Integer compraId;
    private BigDecimal compraTotal;
    private String usuarioNombre;
    private String usuarioDireccion;
    private List<DetalleEntregaResponse> detalles;

    public EntregaResponse(Integer id, String estado, LocalDate fechaEstimada,
            LocalDate fechaEntrega, Integer compraId,
            BigDecimal compraTotal, String usuarioNombre,
            String usuarioDireccion) {
        this.id = id;
        this.estado = estado;
        this.fechaEstimada = fechaEstimada;
        this.fechaEntrega = fechaEntrega;
        this.compraId = compraId;
        this.compraTotal = compraTotal;
        this.usuarioNombre = usuarioNombre;
        this.usuarioDireccion = usuarioDireccion;
    }
}
