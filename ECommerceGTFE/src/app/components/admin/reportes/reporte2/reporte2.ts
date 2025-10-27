import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Reporte2Response, ReporteRequest } from '../../../../models/reporte.model';
import { AdminService } from '../../../../services/admin.service';

@Component({
  selector: 'app-reporte2',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reporte2.html',
  styleUrl: './reporte2.css'
})
export class Reporte2 implements OnInit {
  private adminService = inject(AdminService);

  fechaInicio: string = '';
  fechaFin: string = '';

  clientes: Reporte2Response[] = [];
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

    this.adminService.obtenerTop5ClientesMasGanancias(request).subscribe({
      next: (clientes) => {
        this.clientes = clientes;
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
   * metodo que calcula el total de las ganancias
   * @returns 
   */
  calcularTotalGanancias(): number {
    return this.clientes.reduce((total, cliente) => total + cliente.gananciaGenerada, 0);
  }

  /**
   * metodo que calcula el total gastado por cliente
   * @returns 
   */
  calcularTotalGastado(): number {
    return this.clientes.reduce((total, cliente) => total + cliente.totalGastado, 0);
  }

  /**
   * metodo que calcula el total de las compras
   * @returns 
   */
  calcularTotalCompras(): number {
    return this.clientes.reduce((total, cliente) => total + cliente.totalCompras, 0);
  }

}
