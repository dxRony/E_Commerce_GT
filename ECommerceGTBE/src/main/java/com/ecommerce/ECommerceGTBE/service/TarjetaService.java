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

    /**
     * crea un tarjeta del usuario
     * @param tarjeta a crear en la db
     * @param usuario propietario de la tarjeta
     * @return confirmacion de la operacion
     */
    public Tarjeta agregarTarjeta(Tarjeta tarjeta, Usuario usuario) {
        Optional<Tarjeta> tarjetaExistente = tarjetaRepository.findByNumeracion(tarjeta.getNumeracion());
        if (tarjetaExistente.isPresent()) {
            throw new RuntimeException("ya existe una tarjeta con esta numeracion");
        }
        tarjeta.setTitular(usuario);
        tarjeta.setActiva(true);
        return tarjetaRepository.save(tarjeta);
    }

    /**
     * busca una tarjeta por su id
     * @param id de ña tarjeta
     * @return tarjeta buscada
     */
    public Optional<Tarjeta> findById(Integer id) {
        return tarjetaRepository.findById(id);
    }

    /**
     * busca tarjetas de un usuario
     * @param usuario poseedor de las tarjestas
     * @return lista de tarjetas encontradas
     */
    public List<Tarjeta> findByUsuario(Usuario usuario) {
        return tarjetaRepository.findByTitular(usuario);
    }

    /**
     * busca tarjetas activas del usuario
     * @param usuario poseedor de las tarjetas
     * @return lista de tarjetas encontradas
     */
    public List<Tarjeta> findActivasByUsuario(Usuario usuario) {
        return tarjetaRepository.findByTitularAndActivaTrue(usuario);
    }

    /**
     * desactiva una tarjeta 
     * @param id de la tarjeta
     * @param usuario poseedor de la tarjeta
     */
    public void desactivarTarjeta(Integer id, Usuario usuario) {
        Tarjeta tarjeta = tarjetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada con id: " + id));

        if (!tarjeta.getTitular().getId().equals(usuario.getId())) {
            throw new RuntimeException("la tarjeta no te pertenece");
        }
        tarjeta.setActiva(false);
        tarjetaRepository.save(tarjeta);
    }

    /**
     * valida que una tarjeta le pertenezca a un usuario
     * @param tarjetaId id de la tarjeta
     * @param usuario poseedor de la tarjeta
     * @return si la tarjeta le pertenece al usuario
     */
    public boolean validarTarjetaUsuario(Integer tarjetaId, Usuario usuario) {
        return tarjetaRepository.findByIdAndTitular(tarjetaId, usuario)
                .map(Tarjeta::getActiva)
                .orElse(false);
    }

    /**
     * obtiene una tarjeta por el id y usuario
     * @param id de la tarjeta
     * @param usuario poseedor de la tarjeta
     * @return tarjeta
     */
    public Tarjeta getTarjetaByIdAndUsuario(Integer id, Usuario usuario) {
        return tarjetaRepository.findByIdAndTitular(id, usuario)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));
    }

}
