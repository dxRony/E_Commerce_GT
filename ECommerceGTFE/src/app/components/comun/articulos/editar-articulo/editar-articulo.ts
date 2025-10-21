import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ArticuloRequest, ArticuloResponse } from '../../../../models/articulos.model';
import { ArticuloService } from '../../../../services/articulos.service';

@Component({
  selector: 'app-editar-articulo',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './editar-articulo.html',
  styleUrl: './editar-articulo.css'
})
export class EditarArticulo implements OnInit {
  editForm: FormGroup;
  articuloId: number = 0;
  articulo: ArticuloResponse | null = null;
  isLoading: boolean = true;
  isSubmitting: boolean = false;
  error: string = '';
  success: string = '';

  categorias = [
    'Tecnologia',
    'Hogar',
    'Personal',
    'Decoracion',
    'Otro'
  ];

  estadosArticulo = [
    { value: 'Nuevo', label: 'Nuevo' },
    { value: 'Usado', label: 'Usado' }
  ];

  constructor(
    private fb: FormBuilder,
    private articleService: ArticuloService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.editForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.maxLength(200)]],
      descripcion: ['', [Validators.required, Validators.maxLength(250)]],
      imagen: ['', [Validators.required]],
      precio: ['', [Validators.required, Validators.min(0.01)]],
      stock: ['', [Validators.required, Validators.min(1)]],
      estadoArticulo: ['', [Validators.required]],
      categoria: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.articuloId = Number(this.route.snapshot.paramMap.get('id'));
    this.cargarArticulo();
  }

  cargarArticulo(): void {
    this.isLoading = true;
    this.articleService.getArticuloById(this.articuloId).subscribe({
      next: (articulo) => {
        this.articulo = articulo;
        this.cargarDatosEnFormulario();
        this.isLoading = false;
      },
      error: (error) => {
        this.error = 'al cargar el artículo.';
        this.isLoading = false;
        console.error('Error:', error);
      }
    });
  }

  cargarDatosEnFormulario(): void {
    if (this.articulo) {
      this.editForm.patchValue({
        nombre: this.articulo.nombre,
        descripcion: this.articulo.descripcion,
        imagen: this.articulo.imagen,
        precio: this.articulo.precio,
        stock: this.articulo.stock,
        estadoArticulo: this.articulo.estadoArticulo,
        categoria: this.articulo.categoria
      });
    }
  }

  onSubmit(): void {
    if (this.editForm.valid && this.articulo) {
      this.isSubmitting = true;
      this.error = '';
      this.success = '';

      const request: ArticuloRequest = {
        nombre: this.editForm.get('nombre')?.value,
        descripcion: this.editForm.get('descripcion')?.value,
        imagen: "pendiente",
        precio: Number(this.editForm.get('precio')?.value),
        stock: Number(this.editForm.get('stock')?.value),
        estadoArticulo: this.editForm.get('estadoArticulo')?.value,
        categoria: this.editForm.get('categoria')?.value
      };

      this.articleService.updateArticulo(this.articuloId, request).subscribe({
        next: (articuloActualizado) => {
          this.articulo = articuloActualizado;
          this.success = 'producto actualizado! Volvera a ser revisado por un moderador.';
          this.isSubmitting = false;
          alert(this.success);
          this.volverAMisProductos();
        },
        error: (error) => {
          this.isSubmitting = false;
          if (error.error?.message) {
            this.error = error.error.message;
          } else {
            this.error = ' al actualizar el producto.';
          }
          console.error('Error:', error);
        }
      });
    } else {
      Object.keys(this.editForm.controls).forEach(key => {
        const control = this.editForm.get(key);
        control?.markAsTouched();
      });
    }
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

  volverAMisProductos(): void {
    this.router.navigate(['/comun/mis-productos']);
  }
}
