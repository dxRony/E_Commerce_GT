import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ArticuloService } from '../../services/articulos.service';
import { ArticuloResponse } from '../../models/articulos.model';

@Component({
  selector: 'app-articulos',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './articulos.html',
  styleUrl: './articulos.css'
})
export class Articulos implements OnInit {

  productos: ArticuloResponse[] = [];
  productosFiltrados: ArticuloResponse[] = [];
  categorias: string[] = [];
  isLoading: boolean = true;
  error: string = '';

  searchTerm: string = '';
  selectedCategory: string = 'all';
  sortBy: string = 'name';

  constructor(private productService: ArticuloService) { }

  /**
   * metodo que se sejecuta al llamar al componente
   */
  ngOnInit(): void {
    this.cargarProductos();
  }

  /**
   * carga los productos en una lista
   */
  cargarProductos(): void {
    this.isLoading = true;
    this.productService.getCatalogoPublico().subscribe({
      next: (productos) => {
        this.productos = productos;
        this.productosFiltrados = productos;
        this.obtenerCategorias();
        this.isLoading = false;
      },
      error: (error) => {
        this.error = 'Error al cargar los productos';
        this.isLoading = false;
      }
    });
  }

  /**
   * obtiene las categorias existentes en los productos
   */
  obtenerCategorias(): void {
    const categoriasUnicas = new Set(this.productos.map(p => p.categoria));
    this.categorias = Array.from(categoriasUnicas);
  }

  /**
   * metodo que aplica los filtros de busqueda
   * nombre, descripcion, categoria, 
   */
  aplicarFiltros(): void {
    let filtered = this.productos;

    if (this.searchTerm) {
      const term = this.searchTerm.toLowerCase();
      filtered = filtered.filter(producto =>
        producto.nombre.toLowerCase().includes(term) ||
        producto.descripcion.toLowerCase().includes(term)
      );
    }

    if (this.selectedCategory !== 'all') {
      filtered = filtered.filter(producto =>
        producto.categoria === this.selectedCategory
      );
    }

    filtered = this.ordenarProductos(filtered);

    this.productosFiltrados = filtered;
  }

  /**
   * metodo que ordena los productos por preciom nombre y rating (no hice el rating XD)
   * @param productos 
   * @returns 
   */
  ordenarProductos(productos: ArticuloResponse[]): ArticuloResponse[] {
    switch (this.sortBy) {
      case 'price-low':
        return productos.sort((a, b) => a.precio - b.precio);
      case 'price-high':
        return productos.sort((a, b) => b.precio - a.precio);
      case 'name':
        return productos.sort((a, b) => a.nombre.localeCompare(b.nombre));
      case 'rating':
        return productos.sort((a, b) => (b.ratingPromedio || 0) - (a.ratingPromedio || 0));
      default:
        return productos;
    }
  }

  /**
   * metodo que aplica los filtros al ir teclaando
   */
  onSearchChange(): void {
    this.aplicarFiltros();
  }

  /**
   * metodo que aplica los filtros al ir teclaando
   */
  onCategoryChange(): void {
    this.aplicarFiltros();
  }

  /**
   * metodo que aplica los filtros al ir teclaando
   */
  onSortChange(): void {
    this.aplicarFiltros();
  }

  limpiarFiltros(): void {
    this.searchTerm = '';
    this.selectedCategory = 'all';
    this.sortBy = 'name';
    this.aplicarFiltros();
  }

  formatearPrecio(precio: number): string {
    return new Intl.NumberFormat('es-GT', {
      style: 'currency',
      currency: 'GTQ'
    }).format(precio);
  }

}
