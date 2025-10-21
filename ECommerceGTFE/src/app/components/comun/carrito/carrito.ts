import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CarritoDetalleResponse, CarritoResponse } from '../../../models/carrito.model';
import { CarritoService } from '../../../services/carrito.service';

@Component({
  selector: 'app-carrito',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './carrito.html',
  styleUrl: './carrito.css'
})
export class Carrito implements OnInit {
  private carritoService = inject(CarritoService);

  carrito: CarritoResponse | null = null;
  isLoading: boolean = true;
  error: string = '';

  mensajeAlerta: string = '';
  mostrarAlerta: boolean = false;
  tipoAlerta: 'success' | 'error' | 'warning' = 'success';

  ngOnInit(): void {
    this.cargarCarrito();
  }

  cargarCarrito(): void {
    this.isLoading = true;
    this.carritoService.obtenerCarrito().subscribe({
      next: (carrito) => {
        this.carrito = carrito;
        this.isLoading = false;
      },
      error: (error) => {
        this.error = 'Error al cargar el carrito';
        this.isLoading = false;
        this.mostrarNotificacion('Error al cargar el carrito', 'error');
      }
    });
  }

  actualizarCantidad(item: CarritoDetalleResponse, nuevaCantidad: number): void {
    if (nuevaCantidad < 1) {
      this.eliminarDelCarrito(item.id);
      return;
    }
    if (nuevaCantidad > item.stockDisponible) {
      this.mostrarNotificacion(`No hay suficiente stock, disponible: ${item.stockDisponible}`, 'warning');
      return;
    }
    this.carritoService.actualizarCantidad(item.id, nuevaCantidad).subscribe({
      next: (response: any) => {
        if (response.message) {
          this.cargarCarrito();
          this.mostrarNotificacion('Producto eliminado del carrito');
        } else {
          this.cargarCarrito();
          this.mostrarNotificacion('Cantidad actualizada');
        }
      },
      error: (error) => {
        console.error('Error al actualizar cantidad:', error);
        this.mostrarNotificacion('Error al actualizar cantidad', 'error');
      }
    });
  }

  eliminarDelCarrito(itemId: number): void {
    this.carritoService.eliminarDelCarrito(itemId).subscribe({
      next: () => {
        this.mostrarNotificacion('Producto eliminado del carrito');
        this.cargarCarrito();
      },
      error: (error) => {
        console.error('Error al eliminar del carrito:', error);
        this.mostrarNotificacion('Error al eliminar producto', 'error');
      }
    });
  }

  limpiarCarrito(): void {
    if (!this.carrito || this.carrito.detalles.length === 0) {
      this.mostrarNotificacion('El carrito ya esta vacio', 'warning');
      return;
    }

    if (confirm('¿Estás seguro de que quieres limpiar todo el carrito?')) {
      this.carritoService.limpiarCarrito().subscribe({
        next: () => {
          this.mostrarNotificacion('Carrito limpiado exitosamente');
          this.cargarCarrito();
        },
        error: (error) => {
          console.error('Error al limpiar carrito:', error);
          this.mostrarNotificacion('Error al limpiar carrito', 'error');
        }
      });
    }
  }

  pagarCarrito(): void {
    this.mostrarNotificacion('Funcionalidad de pago por implementar', 'warning');
  }

  formatearPrecio(precio: number): string {
    return new Intl.NumberFormat('es-GT', {
      style: 'currency',
      currency: 'GTQ'
    }).format(precio);
  }

  calcularSubtotal(item: CarritoDetalleResponse): number {
    return item.articuloPrecio * item.cantidad;
  }

  private mostrarNotificacion(mensaje: string, tipo: 'success' | 'error' | 'warning' = 'success') {
    this.mensajeAlerta = mensaje;
    this.tipoAlerta = tipo;
    this.mostrarAlerta = true;
  }

  getAlertClasses(): string {
    const baseClasses = '';
    
    switch (this.tipoAlerta) {
        case 'success':
            return `${baseClasses} bg-green-100 border border-green-400 text-green-700`;
        case 'error':
            return `${baseClasses} bg-red-100 border border-red-400 text-red-700`;
        case 'warning':
            return `${baseClasses} bg-yellow-100 border border-yellow-400 text-yellow-700`;
        default:
            return `${baseClasses} bg-green-100 border border-green-400 text-green-700`;
    }
}

}
