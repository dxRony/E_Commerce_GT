/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.service;

import com.ecommerce.ECommerceGTBE.model.Tarjeta;
import com.ecommerce.ECommerceGTBE.model.Usuario;
import com.ecommerce.ECommerceGTBE.repository.TarjetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author ronyrojas
 */
@Service
public class TarjetaService {

    @Autowired
    private TarjetaRepository tarjetaRepository;

    public Tarjeta agregarTarjeta(Tarjeta tarjeta, Usuario usuario) {
        Optional<Tarjeta> tarjetaExistente = tarjetaRepository.findByNumeracion(tarjeta.getNumeracion());
        if (tarjetaExistente.isPresent()) {
            throw new RuntimeException("Ya hay una tarjeta con esta numeracion");
        }

        tarjeta.setTitular(usuario);
        tarjeta.setActiva(true);
        return tarjetaRepository.save(tarjeta);
    }

    public Optional<Tarjeta> findById(Integer id) {
        return tarjetaRepository.findById(id);
    }

    public List<Tarjeta> findByUsuario(Usuario usuario) {
        return tarjetaRepository.findByTitular(usuario);
    }

    public List<Tarjeta> findActivasByUsuario(Usuario usuario) {
        return tarjetaRepository.findByTitularAndActivaTrue(usuario);
    }

    public void desactivarTarjeta(Integer id, Usuario usuario) {
        Tarjeta tarjeta = tarjetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada con id: " + id));

        if (!tarjeta.getTitular().getId().equals(usuario.getId())) {
            throw new RuntimeException("Esta tarjeta no te pertenece");
        }
        tarjeta.setActiva(false);
        tarjetaRepository.save(tarjeta);
    }

    public boolean validarTarjetaUsuario(Integer tarjetaId, Usuario usuario) {
        return tarjetaRepository.findByIdAndTitular(tarjetaId, usuario)
                .map(Tarjeta::getActiva)
                .orElse(false);
    }

    public Tarjeta getTarjetaByIdAndUsuario(Integer id, Usuario usuario) {
        return tarjetaRepository.findByIdAndTitular(id, usuario)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));
    }

}
