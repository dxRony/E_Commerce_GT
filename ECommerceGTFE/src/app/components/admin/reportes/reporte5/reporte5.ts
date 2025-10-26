import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminService } from '../../../../services/admin.service';
import { Reporte5Response } from '../../../../models/reporte.model';

@Component({
  selector: 'app-reporte5',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './reporte5.html',
  styleUrl: './reporte5.css'
})
export class Reporte5 implements OnInit {

  private adminService = inject(AdminService);

  vendedores: Reporte5Response[] = [];
  isLoading: boolean = false;
  error: string = '';
  mostrarResultados: boolean = false;

  ngOnInit(): void {
    this.generarReporte();
  }

  generarReporte(): void {
    this.isLoading = true;
    this.error = '';
    this.mostrarResultados = false;

    this.adminService.obtenerTop10ClientesMasProductosVenta().subscribe({
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

  calcularTotalProductos(): number {
    return this.vendedores.reduce((total, vendedor) => total + vendedor.totalProductos, 0);
  }
}
