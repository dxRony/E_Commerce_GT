import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../../../services/admin.service';
import { Reporte3Response, ReporteRequest } from '../../../../models/reporte.model';

@Component({
  selector: 'app-reporte3',
  imports: [CommonModule, FormsModule],
  templateUrl: './reporte3.html',
  styleUrl: './reporte3.css'
})
export class Reporte3 implements OnInit {
  private adminService = inject(AdminService);

  fechaInicio: string = '';
  fechaFin: string = '';

  vendedores: Reporte3Response[] = [];
  isLoading: boolean = false;
  error: string = '';
  mostrarResultados: boolean = false;

  ngOnInit(): void {
    this.establecerFechasPorDefecto();
  }

  establecerFechasPorDefecto(): void {
    const hoy = new Date();

    this.fechaFin = hoy.toISOString().split('T')[0];
    this.fechaInicio = hoy.toISOString().split('T')[0];
  }

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

    this.adminService.obtenerTop5ClientesMasVentas(request).subscribe({
      next: (vendedores) => {
        this.vendedores = vendedores;
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

  calcularTotalVentas(): number {
    return this.vendedores.reduce((total, vendedor) => total + vendedor.totalVentas, 0);
  }

  calcularTotalArticulos(): number {
    return this.vendedores.reduce((total, vendedor) => total + vendedor.totalArticulosVendidos, 0);
  }

  calcularTotalProductos(): number {
    return this.vendedores.reduce((total, vendedor) => total + vendedor.totalProductosVendidos, 0);
  }

}
