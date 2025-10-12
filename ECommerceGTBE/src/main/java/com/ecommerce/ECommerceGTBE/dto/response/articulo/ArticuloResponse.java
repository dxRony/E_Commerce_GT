/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.dto.response.articulo;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ronyrojas
 */
@Getter
@Setter
public class ArticuloResponse {

    private Integer id;
    private String nombre;
    private String descripcion;
    private String imagen;
    private BigDecimal precio;
    private Integer stock;
    private String estadoArticulo;
    private String categoria;
    private String estadoAprobacion;
    private Integer usuarioId;
    private String usuarioNombre;
    private Double ratingPromedio;
    private Integer totalCalificaciones;

    public ArticuloResponse(Integer id, String nombre, String descripcion, String imagen,
            BigDecimal precio, Integer stock, String estadoArticulo,
            String categoria, String estadoAprobacion, Integer usuarioId,
            String usuarioNombre) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.precio = precio;
        this.stock = stock;
        this.estadoArticulo = estadoArticulo;
        this.categoria = categoria;
        this.estadoAprobacion = estadoAprobacion;
        this.usuarioId = usuarioId;
        this.usuarioNombre = usuarioNombre;
    }

}
