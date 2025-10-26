/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ecommerce.ECommerceGTBE.service;

import com.ecommerce.ECommerceGTBE.dto.response.reporte.Reporte1Response;
import com.ecommerce.ECommerceGTBE.dto.response.reporte.Reporte2Response;
import com.ecommerce.ECommerceGTBE.dto.response.reporte.Reporte3Response;
import com.ecommerce.ECommerceGTBE.dto.response.reporte.Reporte4Response;
import com.ecommerce.ECommerceGTBE.dto.response.reporte.Reporte5Response;
import com.ecommerce.ECommerceGTBE.repository.ArticuloRepository;
import com.ecommerce.ECommerceGTBE.repository.CompraRepository;
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

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private ArticuloRepository articuloRepository;

    public List<Reporte1Response> obtenerTop10ProductosMasVendidos(LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(23, 59, 59);

        List<Object[]> resultados = detalleCompraRepository.findTop10ProductosMasVendidos(inicio, fin);

        return resultados.stream()
                .map(this::crearResponse1)
                .collect(Collectors.toList());
    }

    private Reporte1Response crearResponse1(Object[] resultado) {
        return new Reporte1Response(
                ((Number) resultado[0]).intValue(),
                (String) resultado[1],
                (String) resultado[2],
                ((Number) resultado[3]).intValue(),
                (BigDecimal) resultado[4],
                (String) resultado[5]
        );
    }

    public List<Reporte2Response> obtenerTop5ClientesMasGanancias(LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(23, 59, 59);

        List<Object[]> resultados = compraRepository.findTop5ClientesMasGanancias(inicio, fin);
        return resultados.stream()
                .map(this::crearResponse2)
                .collect(Collectors.toList());
    }

    private Reporte2Response crearResponse2(Object[] resultado) {
        return new Reporte2Response(
                ((Number) resultado[0]).intValue(),
                (String) resultado[1],
                (String) resultado[2],
                ((Number) resultado[3]).intValue(),
                (BigDecimal) resultado[4],
                (BigDecimal) resultado[5]
        );
    }

    public List<Reporte3Response> obtenerTop5ClientesMasVentas(LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(23, 59, 59);

        List<Object[]> resultados = detalleCompraRepository.findTop5ClientesMasVentas(inicio, fin);
        return resultados.stream()
                .map(this::crearResponse3)
                .collect(Collectors.toList());
    }

    private Reporte3Response crearResponse3(Object[] resultado) {
        return new Reporte3Response(
                ((Number) resultado[0]).intValue(),
                (String) resultado[1],
                (String) resultado[2],
                ((Number) resultado[3]).intValue(),
                ((Number) resultado[4]).intValue(),
                (BigDecimal) resultado[5],
                (BigDecimal) resultado[6]
        );
    }

    public List<Reporte4Response> obtenerTop10ClientesMasPedidos(LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(23, 59, 59);

        List<Object[]> resultados = compraRepository.findTop10ClientesMasPedidos(inicio, fin);
        return resultados.stream()
                .map(this::crearResponse4)
                .collect(Collectors.toList());
    }

    private Reporte4Response crearResponse4(Object[] resultado) {
        BigDecimal totalGastado = (BigDecimal) resultado[4];
        Integer totalPedidos = ((Number) resultado[3]).intValue();
        Double promedioPorPedido = totalPedidos > 0 ? totalGastado.doubleValue() / totalPedidos : 0.0;

        return new Reporte4Response(
                ((Number) resultado[0]).intValue(),
                (String) resultado[1],
                (String) resultado[2],
                totalPedidos,
                totalGastado,
                promedioPorPedido,
                (LocalDateTime) resultado[5]
        );
    }

    public List<Reporte5Response> obtenerTop10ClientesMasProductosVenta() {
        List<Object[]> resultados = articuloRepository.findTop10ClientesMasProductosVenta();

        return resultados.stream()
                .map(this::crearResponse5)
                .collect(Collectors.toList());
    }

    private Reporte5Response crearResponse5(Object[] resultado) {
        Integer totalProductos = ((Number) resultado[3]).intValue();

        return new Reporte5Response(
                ((Number) resultado[0]).intValue(),
                (String) resultado[1],
                (String) resultado[2],
                totalProductos
        );
    }

}
