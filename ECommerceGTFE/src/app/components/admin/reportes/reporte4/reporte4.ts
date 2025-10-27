import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Reporte4Response, ReporteRequest } from '../../../../models/reporte.model';
import { AdminService } from '../../../../services/admin.service';

@Component({
  selector: 'app-reporte4',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reporte4.html',
  styleUrl: './reporte4.css'
})
export class Reporte4 implements OnInit {

  private adminService = inject(AdminService);

  fechaInicio: string = '';
  fechaFin: string = '';

  clientes: Reporte4Response[] = [];
  isLoading: boolean = false;
  error: string = '';
  mostrarResultados: boolean = false;

  ngOnInit(): void {
    this.establecerFechasPorDefecto();
  }

  /**
   * metodo que carga las fechas por defecto (hoy)
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
    this.adminService.obtenerTop10ClientesMasPedidos(request).subscribe({
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

  formatearPrecio(precio: number): string {
    return new Intl.NumberFormat('es-GT', {
      style: 'currency',
      currency: 'GTQ'
    }).format(precio);
  }

  /**
   * metodo que formatea el precioa  la moneda local
   * @param precio 
   * @returns 
   */
  formatearFecha(fecha: string): string {
    return new Date(fecha).toLocaleDateString('es-GT', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  /**
   * metodo que calcula el total de los pedidos de los clientes
   * @returns 
   */
  calcularTotalPedidos(): number {
    return this.clientes.reduce((total, cliente) => total + cliente.totalPedidos, 0);
  }

  /**
   * metodo que calcula el total que un cliente ha gastado
   * @returns 
   */
  calcularTotalGastado(): number {
    return this.clientes.reduce((total, cliente) => total + cliente.totalGastado, 0);
  }

}
