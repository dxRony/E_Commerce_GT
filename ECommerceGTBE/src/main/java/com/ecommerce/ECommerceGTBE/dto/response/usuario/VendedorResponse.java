/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.dto.response.usuario;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ronyrojas
 */
@Getter
@Setter
public class VendedorResponse {

    private Integer id;
    private String nombre;
    private String email;
    private Integer celular;
    private String direccion;
    private Integer rol;
    private Boolean suspendido;

    public VendedorResponse(Integer id, String nombre, String email, Integer celular,
            String direccion, Integer rol, Boolean suspendido) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.celular = celular;
        this.direccion = direccion;
        this.rol = rol;
        this.suspendido = suspendido;
    }

    public VendedorResponse(Integer id, String nombre, String email, Integer celular,
            String direccion, Integer rol) {
        this(id, nombre, email, celular, direccion, rol, null);
    }
}
