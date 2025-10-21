import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ArticuloService } from '../../../../services/articulos.service';
import { ArticuloResponse } from '../../../../models/articulos.model';

@Component({
  selector: 'app-mis-articulos',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './mis-articulos.html',
  styleUrl: './mis-articulos.css'
})
export class MisArticulos implements OnInit {

  articulos: ArticuloResponse[] = [];
  articulosFiltrados: ArticuloResponse[] = [];
  isLoading: boolean = true;
  error: string = '';

  searchTerm: string = '';
  selectedCategoria: string = 'all';
  selectedEstadoAprobacion: string = 'all';
  categorias: string[] = [];

  estadosAprobacion = [
    { value: 'all', label: 'Todos los estados' },
    { value: 'Pendiente', label: 'Pendiente' },
    { value: 'Aprobado', label: 'Aprobado' },
    { value: 'Rechazado', label: 'Rechazado' }
  ];

  constructor(private articleService: ArticuloService) { }

  ngOnInit(): void {
    this.cargarMisArticulos();
  }

  cargarMisArticulos(): void {
    this.isLoading = true;
    this.articleService.getMisArticulos().subscribe({
      next: (articulos) => {
        this.articulos = articulos;
        this.articulosFiltrados = articulos;
        this.extraerCategorias();
        this.isLoading = false;
      },
      error: (error) => {
        this.error = ' al cargar tus articulos.';
        this.isLoading = false;
        console.error('Error:', error);
      }
    });
  }

  extraerCategorias(): void {
    const categoriasUnicas = new Set(this.articulos.map(p => p.categoria));
    this.categorias = Array.from(categoriasUnicas);
  }

  aplicarFiltros(): void {
    let filtered = this.articulos;

    if (this.searchTerm) {
      const term = this.searchTerm.toLowerCase();
      filtered = filtered.filter(articulo =>
        articulo.nombre.toLowerCase().includes(term) ||
        articulo.descripcion.toLowerCase().includes(term)
      );
    }

    if (this.selectedCategoria !== 'all') {
      filtered = filtered.filter(articulo =>
        articulo.categoria === this.selectedCategoria
      );
    }

    if (this.selectedEstadoAprobacion !== 'all') {
      filtered = filtered.filter(articulo =>
        articulo.estadoAprobacion === this.selectedEstadoAprobacion
      );
    }
    this.articulosFiltrados = filtered;
  }

  onSearchChange(): void {
    this.aplicarFiltros();
  }

  onCategoryChange(): void {
    this.aplicarFiltros();
  }

  onEstadoAprobacionChange(): void {
    this.aplicarFiltros();
  }

  formatearPrecio(precio: number): string {
    return new Intl.NumberFormat('es-GT', {
      style: 'currency',
      currency: 'GTQ'
    }).format(precio);
  }

  getEstadoAprobacionTexto(estado: string): string {
    const estados: { [key: string]: string } = {
      'Pendiente': 'Pendiente',
      'Aprobado': 'Aprobado',
      'Rechazado': 'Rechazado'
    };
    return estados[estado] || estado;
  }

  getEstadoAprobacionClase(estado: string): string {
    const clases: { [key: string]: string } = {
      'Pendiente': 'estado-pendiente',
      'Aprobado': 'estado-aprobado',
      'Rechazado': 'estado-rechazado'
    };
    return clases[estado] || 'estado-desconocido';
  }

  getEstadoArticuloTexto(estado: string): string {
    return estado === 'Nuevo' ? 'Nuevo' : 'Usado';
  }

  limpiarFiltros(): void {
    this.searchTerm = '';
    this.selectedCategoria = 'all';
    this.selectedEstadoAprobacion = 'all';
    this.aplicarFiltros();
  }
}
