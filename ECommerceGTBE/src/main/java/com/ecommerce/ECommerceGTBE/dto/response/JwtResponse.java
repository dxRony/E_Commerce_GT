/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.dto.response;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ronyrojas
 */
@Getter
@Setter
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private Integer id;
    private String email;
    private String nombre;
    private Integer rol;

    public JwtResponse() {
    }

    public JwtResponse(String token, Integer id, String email, String nombre, Integer rol) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.nombre = nombre;
        this.rol = rol;
    }

}
