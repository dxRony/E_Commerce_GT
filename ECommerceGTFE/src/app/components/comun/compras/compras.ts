import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CompraService } from '../../../services/compra.service';
import { CompraResponse } from '../../../models/compra.model';

@Component({
  selector: 'app-compras',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './compras.html',
  styleUrl: './compras.css'
})
export class Compras implements OnInit {
  private compraService = inject(CompraService);

  compras: CompraResponse[] = [];
  isLoading: boolean = true;
  error: string = '';

  ngOnInit(): void {
    this.cargarCompras();
  }

  /**
   * metodo que carga las compras de un usuario
   */
  cargarCompras(): void {
    this.isLoading = true;
    this.compraService.obtenerMisCompras().subscribe({
      next: (compras) => {
        this.compras = compras;
        this.isLoading = false;
      },
      error: (error) => {
        this.error = 'Error al cargar las compras';
        this.isLoading = false;
        console.error('Error:', error);
      }
    });
  }

  /**
   * metodo que formatea el precio de un producto
   * @param precio 
   * @returns 
   */
  formatearPrecio(precio: number): string {
    return new Intl.NumberFormat('es-GT', {
      style: 'currency',
      currency: 'GTQ'
    }).format(precio);
  }

  getEstadoTexto(estado: string): string {
    const estados: { [key: string]: string } = {
      'PENDIENTE': 'Pendiente',
      'EN_CURSO': 'En curso',
      'ENTREGADO': 'Entregado'
    };
    return estados[estado] || estado;
  }

  getEstadoClase(estado: string): string {
    const clases: { [key: string]: string } = {
      'PENDIENTE': 'estado-pendiente',
      'EN_CURSO': 'estado-en-curso',
      'ENTREGADO': 'estado-entregado'
    };
    return clases[estado] || 'estado-desconocido';
  }
}
