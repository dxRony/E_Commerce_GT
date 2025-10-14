/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.dto.response.pago;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ronyrojas
 */
@Getter
@Setter
public class CompraResponse {

    private Integer id;
    private LocalDateTime fecha;
    private BigDecimal total;
    private String estadoEntrega;
    private BigDecimal comisionUsuario;
    private BigDecimal comisionPagina;
    private String tarjetaNumeracion;
    private List<DetalleCompraResponse> detalles;

    public CompraResponse(Integer id, LocalDateTime fecha, BigDecimal total,
            String estadoEntrega, BigDecimal comisionUsuario,
            BigDecimal comisionPagina, String tarjetaNumeracion) {
        this.id = id;
        this.fecha = fecha;
        this.total = total;
        this.estadoEntrega = estadoEntrega;
        this.comisionUsuario = comisionUsuario;
        this.comisionPagina = comisionPagina;
        this.tarjetaNumeracion = tarjetaNumeracion;
    }

}
