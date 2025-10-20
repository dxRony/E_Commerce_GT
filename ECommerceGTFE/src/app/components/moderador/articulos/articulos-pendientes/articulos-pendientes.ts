import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { RouterModule } from '@angular/router';
import { ArticuloResponse } from '../../../../models/articulos.model';
import { ModeratorService } from '../../../../services/moderador.service';


@Component({
  selector: 'app-articulos-pendientes',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './articulos-pendientes.html',
  styleUrl: './articulos-pendientes.css'
})
export class ArticulosPendientes implements OnInit {
  articulosPendientes: ArticuloResponse[] = [];
  isLoading: boolean = true;
  error: string = '';
  success: string = '';

  searchTerm: string = '';
  selectedCategoria: string = 'all';
  categorias: string[] = [];

  constructor(private moderatorService: ModeratorService) { }

  ngOnInit(): void {
    this.cargarArticulosPendientes();
  }

  cargarArticulosPendientes(): void {
    this.isLoading = true;
    this.moderatorService.getArticulosPendientes().subscribe({
      next: (articulos) => {
        this.articulosPendientes = articulos;
        this.extraerCategorias();
        this.isLoading = false;
      },
      error: (error) => {
        this.error = ' al cargar los productos pendientes.';
        this.isLoading = false;
        console.error('Error:', error);
      }
    });
  }

  extraerCategorias(): void {
    const categoriasUnicas = new Set(this.articulosPendientes.map(p => p.categoria));
    this.categorias = Array.from(categoriasUnicas);
  }

  aplicarFiltros(): ArticuloResponse[] {
    let filtered = this.articulosPendientes;

    if (this.searchTerm) {
      const term = this.searchTerm.toLowerCase();
      filtered = filtered.filter(articulo =>
        articulo.nombre.toLowerCase().includes(term) ||
        articulo.descripcion.toLowerCase().includes(term) ||
        articulo.usuarioNombre.toLowerCase().includes(term)
      );
    }

    if (this.selectedCategoria !== 'all') {
      filtered = filtered.filter(articulo =>
        articulo.categoria === this.selectedCategoria
      );
    }
    return filtered;
  }

  get articulosFiltrados(): ArticuloResponse[] {
    return this.aplicarFiltros();
  }

  aprobarArticulo(articulo: ArticuloResponse): void {
    if (confirm(` seguro de que deseas aprobar el producto "${articulo.nombre}"?`)) {
      this.moderatorService.aprobarArticulo(articulo.id).subscribe({
        next: (articuloAprobado) => {
          this.success = `Producto "${articulo.nombre}" aprobado correctamente.`;
          this.articulosPendientes = this.articulosPendientes.filter(a => a.id !== articulo.id);
          alert(this.success);
          this.extraerCategorias();
        },
        error: (error) => {
          this.error = ' al aprobar el producto.';
          console.error('Error :', error);
        }
      });
    }
  }

  rechazarArticulo(articulo: ArticuloResponse): void {
    if (confirm(`¿Estás seguro de que deseas rechazar el producto "${articulo.nombre}"?`)) {
      this.moderatorService.rechazarArticulo(articulo.id).subscribe({
        next: (articuloRechazado) => {
          this.success = `Producto "${articulo.nombre}" rechazado correctamente.`;
          this.articulosPendientes = this.articulosPendientes.filter(a => a.id !== articulo.id);
          this.extraerCategorias();
        },
        error: (error) => {
          this.error = 'Error al rechazar el producto. Por favor, intenta nuevamente.';
          console.error('Error rejecting product:', error);
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

  limpiarFiltros(): void {
    this.searchTerm = '';
    this.selectedCategoria = 'all';
  }
}
