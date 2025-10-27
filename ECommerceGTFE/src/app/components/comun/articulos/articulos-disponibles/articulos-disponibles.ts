import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ArticuloResponse } from '../../../../models/articulos.model';
import { ArticuloService } from '../../../../services/articulos.service';
import { CarritoService } from '../../../../services/carrito.service';
import { TokenService } from '../../../../services/token.service';
import { CarritoDetalleRequest } from '../../../../models/carrito.model';

@Component({
  selector: 'app-articulos-disponibles',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './articulos-disponibles.html',
  styleUrl: './articulos-disponibles.css'
})
export class ArticulosDisponibles implements OnInit {

  productos: ArticuloResponse[] = [];
  productosFiltrados: ArticuloResponse[] = [];
  categorias: string[] = [];
  isLoading: boolean = true;
  error: string = '';
  usuarioIdActual: any = '';

  searchTerm: string = '';
  selectedCategory: string = 'all';
  sortBy: string = 'name';

  mensajeAlerta: string = '';
  mostrarAlerta: boolean = false;
  tipoAlerta: 'success' | 'error' | 'warning' = 'success';

  constructor(
    private articuloService: ArticuloService,
    private tokenService: TokenService,
    private carritoService: CarritoService
  ) { }

  ngOnInit(): void {
    this.cargarProductos();
  }

  /**
   * carga los productos en una lista
   */
  cargarProductos(): void {
    this.isLoading = true;
    this.usuarioIdActual = this.tokenService.obtenerIdUser()?.toString() || '';
    this.articuloService.getCatalogoPublico().subscribe({
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

  private mostrarNotificacion(mensaje: string, tipo: 'success' | 'error' | 'warning' = 'success') {
    this.mensajeAlerta = mensaje;
    this.tipoAlerta = tipo;
    this.mostrarAlerta = true;
  }

  cerrarAlerta(): void {
    this.mostrarAlerta = false;
  }

  /**
   * metodo para agregar productos al carrito
   * @param producto a agregar al carrito
   * @returns 
   */
  agregarAlCarrito(producto: any) {
    if (producto.stock === 0) {
      this.mostrarNotificacion('el producto no tiene stock', 'warning');
      return;
    }

    if (producto.usuarioId === this.usuarioIdActual) {
      this.mostrarNotificacion('No puedes agregar tus productos al carrito', 'warning');
      return;
    }

    const request: CarritoDetalleRequest = {
      articuloId: producto.id,
      cantidad: 1
    };

    this.carritoService.agregarAlCarrito(request).subscribe({
      next: (response) => {
        this.mostrarNotificacion(' agregado al carrito ');
        console.log('Producto agregado:', response);
      },
      error: (error) => {
        console.error('Error al agregar al carrito:', error);
        this.mostrarNotificacion('Error al agregar producto al carrito', 'error');
      }
    });
  }
}