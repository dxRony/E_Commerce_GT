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

  /**
   * metodoq ue obtiene el carrito del usuario
   * junto con sus detalles (articulos)
   */
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

  /**
   * metodo que abre el modal del pago donde se
   * elije la tarjeta a usar
   */
  abrirModalPago(): void {
    this.mostrarModalPago = true;
    this.cargarTarjetas();
  }

  /**
   * metodo que cierra el modal
   */
  cerrarModalPago(): void {
    this.mostrarModalPago = false;
    this.mostrarFormTarjeta = false;
    this.tarjetaSeleccionada = null;
    this.nuevaTarjeta = { numeracion: '', fechaVencimiento: '', cvv: '' };
  }

  /**
   * metodo que obtiene las tarjetas del usuario
   * para que elija una y realice el pago
   */
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

  /**
   * metodo que realiza el pago de los productos, dada una tarjeta seleccionada
   * @returns 
   */
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
        this.mostrarNotificacion('pago realizado!', 'success');
        this.cerrarModalPago();
        this.cargarCarrito();
      },
      error: (error) => {
        this.procesandoPago = false;
        this.mostrarNotificacion('Error al procesar el pago', 'error');
      }
    });
  }

  /**
   * metodo que agrega una tarjeta para el usuario y realice compras con
   * esta tarjeta
   */
  agregarNuevaTarjeta(): void {
    this.tarjetaService.agregarTarjeta(this.nuevaTarjeta).subscribe({
      next: (tarjeta) => {
        this.mostrarNotificacion('trajeta agregada', 'success');
        this.mostrarFormTarjeta = false;
        this.nuevaTarjeta = { numeracion: '', fechaVencimiento: '', cvv: '' };
        this.cargarTarjetas();
        this.tarjetaSeleccionada = tarjeta.id;
      },
      error: (error) => {
        this.mostrarNotificacion('Error al agregar tarjeta', 'error');
      }
    });
  }

  /**
   * metodo que oculta los primeros numeros de la tarjeta de 
   * un usuario
   * @param numeracion 
   * @returns 
   */
  ocutlarNumeroTarjeta(numeracion: string): string {
    return `****-****-****-${numeracion.slice(-4)}`;
  }

  /**
   * metodo que actualiza la cantidad de un articulo en el carrito
   * validando stock o eliminacion si es 0 la cantidad
   * @param item 
   * @param nuevaCantidad 
   * @returns 
   */
  actualizarCantidad(item: CarritoDetalleResponse, nuevaCantidad: number): void {
    if (nuevaCantidad < 1) {
      this.eliminarDelCarrito(item.id);
      return;
    }
    if (nuevaCantidad > item.stockDisponible) {
      this.mostrarNotificacion(`No hay stock, disponible: ${item.stockDisponible}`, 'warning');
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

  /**
   * metodo que elimina el articulo seleccionado del carrito
   * @param itemId 
   */
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

  /**
   * metodo que calcula el total de los detalles en el carrito
   * dado el precio del articulo y las cantidades seleccionadas
   * @param item 
   * @returns 
   */
  calcularSubtotal(item: CarritoDetalleResponse): number {
    return item.articuloPrecio * item.cantidad;
  }

  private mostrarNotificacion(mensaje: string, tipo: 'success' | 'error' | 'warning' = 'success') {
    this.mensajeAlerta = mensaje;
    this.tipoAlerta = tipo;
    this.mostrarAlerta = true;
  }

}
