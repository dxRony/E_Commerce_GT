import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CarritoDetalleResponse, CarritoResponse } from '../../../models/carrito.model';
import { CarritoService } from '../../../services/carrito.service';
import { TarjetaService } from '../../../services/tarjeta.service';
import { CompraService } from '../../../services/compra.service';
import { TarjetaResponse } from '../../../models/tarjeta.model';
import { CompraRequest } from '../../../models/compra.model';

@Component({
  selector: 'app-carrito',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './carrito.html',
  styleUrl: './carrito.css'
})
export class Carrito implements OnInit {
  private carritoService = inject(CarritoService);
  private tarjetaService = inject(TarjetaService);
  private compraService = inject(CompraService);

  carrito: CarritoResponse | null = null;
  isLoading: boolean = true;
  error: string = '';

  mostrarModalPago: boolean = false;
  tarjetas: TarjetaResponse[] = [];
  tarjetaSeleccionada: number | null = null;
  procesandoPago: boolean = false;

  mostrarFormTarjeta: boolean = false;
  nuevaTarjeta = {
    numeracion: '',
    fechaVencimiento: '',
    cvv: ''
  };

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

  abrirModalPago(): void {
    this.mostrarModalPago = true;
    this.cargarTarjetas();
  }

  cerrarModalPago(): void {
    this.mostrarModalPago = false;
    this.mostrarFormTarjeta = false;
    this.tarjetaSeleccionada = null;
    this.nuevaTarjeta = { numeracion: '', fechaVencimiento: '', cvv: '' };
  }

  cargarTarjetas(): void {
    this.tarjetaService.getMisTarjetas().subscribe({
      next: (tarjetas) => {
        this.tarjetas = tarjetas;
      },
      error: (error) => {
        console.error('Error al cargar tarjetas:', error);
        this.mostrarNotificacion('Error al cargar tarjetas', 'error');
      }
    });
  }

  procesarPago(): void {
    if (!this.tarjetaSeleccionada) {
      this.mostrarNotificacion('Por favor selecciona una tarjeta', 'warning');
      return;
    }

    this.procesandoPago = true;
    const compraRequest: CompraRequest = {
      tarjetaId: this.tarjetaSeleccionada
    };

    this.compraService.procesarPago(compraRequest).subscribe({
      next: (compra) => {
        this.procesandoPago = false;
        this.mostrarNotificacion('¡Pago procesado exitosamente!', 'success');
        this.cerrarModalPago();
        this.cargarCarrito();

        console.log('Compra realizada:', compra);
      },
      error: (error) => {
        this.procesandoPago = false;
        console.error('Error al procesar pago:', error);
        this.mostrarNotificacion('Error al procesar el pago', 'error');
      }
    });
  }

  agregarNuevaTarjeta(): void {
    this.tarjetaService.agregarTarjeta(this.nuevaTarjeta).subscribe({
      next: (tarjeta) => {
        this.mostrarNotificacion('Tarjeta agregada exitosamente', 'success');
        this.mostrarFormTarjeta = false;
        this.nuevaTarjeta = { numeracion: '', fechaVencimiento: '', cvv: '' };
        this.cargarTarjetas();
        this.tarjetaSeleccionada = tarjeta.id;
      },
      error: (error) => {
        console.error('Error al agregar tarjeta:', error);
        this.mostrarNotificacion('Error al agregar tarjeta', 'error');
      }
    });
  }

  ocutlarNumeroTarjeta(numeracion: string): string {
    return `****-****-****-${numeracion.slice(-4)}`;
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

    if (confirm('seguro de que quieres limpiar todo el carrito?')) {
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
