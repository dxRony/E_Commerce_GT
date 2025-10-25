/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.service;

import com.ecommerce.ECommerceGTBE.dto.response.reporte.Reporte1Response;
import com.ecommerce.ECommerceGTBE.repository.DetalleCompraRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ronyrojas
 */
@Service
public class ReporteService {
    
     @Autowired
    private DetalleCompraRepository detalleCompraRepository;
    
    public List<Reporte1Response> obtenerTop10ProductosMasVendidos(LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(23, 59, 59);
        
        List<Object[]> resultados = detalleCompraRepository.findTop10ProductosMasVendidos(inicio, fin);
        
        return resultados.stream()
                .map(this::crearResponse)
                .collect(Collectors.toList());
    }
    
    private Reporte1Response crearResponse(Object[] resultado) {
        return new Reporte1Response(
            ((Number) resultado[0]).intValue(),    // productoId
            (String) resultado[1],                 // productoNombre
            (String) resultado[2],                 // productoCategoria
            ((Number) resultado[3]).intValue(),    // cantidadVendida
            (BigDecimal) resultado[4],             // totalVendido
            (String) resultado[5]                  // vendedorNombre
        );
    }
    
}
