import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { DetalleEntregaResponse, EntregaResponse, ModificarEntregaRequest } from '../../../../models/entrega.model';
import { LogisticaService } from '../../../../services/logistica.service';

@Component({
  selector: 'app-pedidos-en-curso',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './pedidos-en-curso.html',
  styleUrl: './pedidos-en-curso.css'
})
export class PedidosEnCurso implements OnInit {
  entregas: EntregaResponse[] = [];
  isLoading: boolean = true;
  error: string = '';
  success: string = '';

  searchTerm: string = '';
  selectedEstado: string = 'all';

  selectedEntrega: EntregaResponse | null = null;
  showDateModal: boolean = false;
  newFechaEstimada: string = '';

  constructor(private logisticaService: LogisticaService) { }

  ngOnInit(): void {
    this.cargarEntregasEnCurso();
  }

  /**
   * metodo que obtiene las entregas en curso para 
   * que el usuario las pueda ver
   */
  cargarEntregasEnCurso(): void {
    this.isLoading = true;
    this.logisticaService.obtenerEntregasEnCurso().subscribe({
      next: (entregas) => {
        this.entregas = entregas;
        this.isLoading = false;
      },
      error: (error) => {
        this.error = 'al cargar los pedidos en curso.';
        this.isLoading = false;
        console.error('Error: ', error);
      }
    });
  }

  /**
   * metodo que aplica filtros sobre las entregas obtenidas
   * nombre del usuario, direccion, compra, estado
   * @returns 
   */
  aplicarFiltros(): EntregaResponse[] {
    let filtered = this.entregas;

    if (this.searchTerm) {
      const term = this.searchTerm.toLowerCase();
      filtered = filtered.filter(entrega =>
        entrega.usuarioNombre.toLowerCase().includes(term) ||
        entrega.usuarioDireccion.toLowerCase().includes(term) ||
        entrega.compraId.toString().includes(term)
      );
    }

    if (this.selectedEstado !== 'all') {
      filtered = filtered.filter(entrega =>
        entrega.estado === this.selectedEstado
      );
    }
    return filtered;
  }

  get entregasFiltradas(): EntregaResponse[] {
    return this.aplicarFiltros();
  }

  formatearPrecio(precio: number): string {
    return new Intl.NumberFormat('es-GT', {
      style: 'currency',
      currency: 'GTQ'
    }).format(precio);
  }

  getEstadoTexto(estado: string): string {
    const estados: { [key: string]: string } = {
      'EN_CURSO': 'En Curso',
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

  abrirModalFecha(entrega: EntregaResponse): void {
    this.selectedEntrega = entrega;
    this.newFechaEstimada = entrega.fechaEstimada.split('T')[0];
    this.showDateModal = true;
  }

  cerrarModalFecha(): void {
    this.showDateModal = false;
    this.selectedEntrega = null;
    this.newFechaEstimada = '';
  }

  /**
   * metodo que actualiza la fecha estimada de la entrega
   */
  actualizarFechaEstimada(): void {
    if (this.selectedEntrega && this.newFechaEstimada) {
      console.error('new fechaEstimada:', this.newFechaEstimada);
      const request: ModificarEntregaRequest = {
        fechaEstimada: this.newFechaEstimada
      };

      this.logisticaService.actualizarFechaEstimada(this.selectedEntrega.id, request).subscribe({
        next: (entregaActualizada) => {
          this.success = 'Fecha estimada actualizada correctamente.';
          const index = this.entregas.findIndex(e => e.id === entregaActualizada.id);
          if (index !== -1) {
            this.entregas[index] = entregaActualizada;
          }
          this.cerrarModalFecha();
        },
        error: (error) => {
          this.error = 'al actualizar la fecha estimada.';
          console.error('Error :', error);
        }
      });
    }
  }

  /**
   * metodo que cuenta los detalles entregas listos para mostrar el progreso de la compra-entrega
   * @param detalles 
   * @returns 
   */
  contarDetallesListos(detalles: DetalleEntregaResponse[] | undefined): number {
    if (!detalles) return 0;
    return detalles.filter(detalle => detalle.listo).length;
  }

  /**
   * metodo que marca el detalle como listo
   * @param detalle 
   * @param entregaId 
   */
  marcarDetalleListo(detalle: DetalleEntregaResponse, entregaId: number): void {
    this.logisticaService.marcarDetalleListo(detalle.id).subscribe({
      next: (detalleActualizado) => {
        this.success = `Artículo "${detalle.articuloNombre}" marcado como listo.`;

        const entregaIndex = this.entregas.findIndex(e => e.id === entregaId);
        if (entregaIndex !== -1 && this.entregas[entregaIndex].detalles) {
          const detalleIndex = this.entregas[entregaIndex].detalles!.findIndex(d => d.id === detalle.id);
          if (detalleIndex !== -1) {
            this.entregas[entregaIndex].detalles![detalleIndex] = detalleActualizado;
          }
        }
        this.success = '';
      },
      error: (error) => {
        this.error = 'Error al marcar el articulo como listo.';
        console.error('Error marking detail as ready:', error);
        this.error = '';
      }
    });
  }

  /**
   * metodo que marca la entrega como entregada, una vez que todos los detalles de la entrega
   * esten marcados como listos
   * @param entrega 
   */
  marcarComoEntregada(entrega: EntregaResponse): void {
    if (confirm(`seguro de que deseas marcar la entrega #${entrega.compraId} como entregada?`)) {
      this.logisticaService.marcarComoEntregada(entrega.id).subscribe({
        next: (entregaActualizada) => {
          this.success = `Entrega #${entrega.compraId} marcada como entregada correctamente.`;
          this.entregas = this.entregas.filter(e => e.id !== entrega.id);
          alert(this.success);
        },
        error: (error) => {
          this.error = 'Error al marcar la entrega como entregada.';
          console.error('Error marking delivery as delivered:', error);
        }
      });
    }
  }

  limpiarFiltros(): void {
    this.searchTerm = '';
    this.selectedEstado = 'all';
  }

  /**
   * metoodo que verifica que todos los detalles entregas esten listos
   * para poder marcar la venta como entregada
   * @param detalles 
   * @returns 
   */
  todosDetallesListos(detalles: DetalleEntregaResponse[]): boolean {
    return detalles.every(detalle => detalle.listo);
  }

}
