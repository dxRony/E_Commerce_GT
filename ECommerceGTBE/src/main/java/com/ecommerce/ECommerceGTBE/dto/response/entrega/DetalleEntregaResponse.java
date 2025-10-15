/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.dto.response.entrega;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ronyrojas
 */
@Getter
@Setter
public class DetalleEntregaResponse {

    private Integer id;
    private Integer articuloId;
    private String articuloNombre;
    private String articuloImagen;
    private Integer cantidad;
    private Boolean listo;

    public DetalleEntregaResponse(Integer id, Integer articuloId, String articuloNombre,
            String articuloImagen, Integer cantidad, Boolean listo) {
        this.id = id;
        this.articuloId = articuloId;
        this.articuloNombre = articuloNombre;
        this.articuloImagen = articuloImagen;
        this.cantidad = cantidad;
        this.listo = listo;
    }

}
