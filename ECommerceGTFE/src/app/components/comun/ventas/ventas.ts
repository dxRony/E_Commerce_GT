import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CompraService } from '../../../services/compra.service';
import { DetalleCompraResponse } from '../../../models/compra.model';

@Component({
  selector: 'app-ventas',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './ventas.html',
  styleUrl: './ventas.css'
})
export class Ventas implements OnInit {
  private compraService = inject(CompraService);

  ventas: DetalleCompraResponse[] = [];
  isLoading: boolean = true;
  error: string = '';

  ngOnInit(): void {
    this.cargarVentas();
  }

  cargarVentas(): void {
    this.isLoading = true;
    this.compraService.obtenerMisVentas().subscribe({
      next: (ventas) => {
        this.ventas = ventas;
        this.isLoading = false;
      },
      error: (error) => {
        this.error = 'Error al cargar las ventas';
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

}
