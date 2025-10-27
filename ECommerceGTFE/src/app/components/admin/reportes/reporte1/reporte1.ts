import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Reporte1Response, ReporteRequest } from '../../../../models/reporte.model';
import { AdminService } from '../../../../services/admin.service';

@Component({
  selector: 'app-reporte1',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reporte1.html',
  styleUrl: './reporte1.css'
})
export class Reporte1 implements OnInit {
  private adminService = inject(AdminService);

  fechaInicio: string = '';
  fechaFin: string = '';

  productos: Reporte1Response[] = [];
  isLoading: boolean = false;
  error: string = '';
  mostrarResultados: boolean = false;

  ngOnInit(): void {
    this.establecerFechasPorDefecto();
  }

  /**
   * metodoq que carga las fechas por defecto (hoy)
   */
  establecerFechasPorDefecto(): void {
    const hoy = new Date();
    this.fechaFin = hoy.toISOString().split('T')[0];
    this.fechaInicio = hoy.toISOString().split('T')[0];
  }

  /**
   * metodo que generea los reportes dados las fechas
   * @returns 
   */
  generarReporte(): void {
    if (!this.fechaInicio || !this.fechaFin) {
      this.error = 'selecciona ambas fechas';
      alert(this.error);
      return;
    }

    if (this.fechaInicio > this.fechaFin) {
      this.error = 'La fecha de inicio no puede ser mayor a la fecha fin';
      alert(this.error);
      return;
    }
    this.isLoading = true;
    this.error = '';
    this.mostrarResultados = false;

    const request: ReporteRequest = {
      fechaInicio: this.fechaInicio,
      fechaFin: this.fechaFin
    };

    this.adminService.obtenerTop10ProductosVendidos(request).subscribe({
      next: (productos) => {
        this.productos = productos;
        this.isLoading = false;
        this.mostrarResultados = true;
      },
      error: (error) => {
        this.error = 'Error al generar el reporte';
        this.isLoading = false;
        console.error('Error:', error);
      }
    });
  }

  /**
   * metodo que formatea el precioa  la moneda local
   * @param precio 
   * @returns 
   */
  formatearPrecio(precio: number): string {
    return new Intl.NumberFormat('es-GT', {
      style: 'currency',
      currency: 'GTQ'
    }).format(precio);
  }

  /**
   * calcula el total de las ventas (suma los presio de los productos vendidos)
   * @returns 
   */
  calcularTotalVendido(): number {
    return this.productos.reduce((total, producto) => total + producto.totalVendido, 0);
  }

  /**
   * calcula el total de las unidades vendidades (suma las cantidades vendidas de cada producto)
   * @returns 
   */
  calcularTotalUnidades(): number {
    return this.productos.reduce((total, producto) => total + producto.cantidadVendida, 0);
  }

}
