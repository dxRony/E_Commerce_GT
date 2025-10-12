/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author ronyrojas
 */
public class UsuarioDetailsImpl implements UserDetails {

    private final Usuario usuario;

    public UsuarioDetailsImpl(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = switch (usuario.getRol()) {
            
            case 1 ->
                "ROLE_COMUN";
            case 2 ->
                "ROLE_MODERADOR";
            case 3 ->
                "ROLE_LOGISTICA";
            case 4 ->
                "ROLE_ADMINISTRADOR";
            default ->
                "ROLE_COMUN"; // Por defecto
        };
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
    
    

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !usuario.getSuspendido();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
