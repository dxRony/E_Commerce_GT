import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ArticuloService } from '../../../../services/articulos.service';
import { ArticuloRequest, ArticuloResponse } from '../../../../models/articulos.model';

@Component({
  selector: 'app-publicar-articulo',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './publicar-articulo.html',
  styleUrl: './publicar-articulo.css'
})
export class PublicarArticulo {
  productForm: FormGroup;
  isSubmitting: boolean = false;
  error: string = '';
  success: string = '';

  //categorias trackeadas
  categorias = [
    'Tecnologia',
    'Hogar',
    'Personal',
    'Decoracion',
    'Otro'
  ];

  //Estados trackeados
  estadoArticulo = [
    { value: 'Nuevo', label: 'Nuevo' },
    { value: 'Usado', label: 'Usado' }
  ];

  constructor(
    private fb: FormBuilder,
    private articleService: ArticuloService,
    private router: Router
  ) {
    this.productForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.maxLength(200)]],
      descripcion: ['', [Validators.required, Validators.maxLength(250)]],
      precio: ['', [Validators.required, Validators.min(0.01)]],
      stock: ['', [Validators.required, Validators.min(1)]],
      estadoArticulo: ['', [Validators.required]],
      categoria: ['', [Validators.required]]
    });
  }

  /**
   * disparador para publicar un articulo
   */
  onSubmit(): void {
    if (this.productForm.valid) {
      this.isSubmitting = true;
      this.error = '';
      this.success = '';

      //creando request del articulo
      const request: ArticuloRequest = {
        nombre: this.productForm.get('nombre')?.value,
        descripcion: this.productForm.get('descripcion')?.value,
        imagen: "pendiente",
        precio: Number(this.productForm.get('precio')?.value),
        stock: Number(this.productForm.get('stock')?.value),
        estadoArticulo: this.productForm.get('estadoArticulo')?.value,
        categoria: this.productForm.get('categoria')?.value
      };

      //llamando al servicio para crear el articulo en la api
      this.articleService.crearArticulo(request).subscribe({
        next: (articuloCreado: ArticuloResponse) => {
          this.success = 'Producto publicado!! En esperada de ser aprobado :D';
          this.isSubmitting = false;
          this.productForm.reset();
          alert(this.success);
          this.router.navigate(['/comun/dashboard']);

        },
        error: (error) => {
          this.isSubmitting = false;
          if (error.error?.message) {
            this.error = error.error.message;
          } else {
            this.error = 'al publicar el producto.';
          }
          console.error('Error:', error);
        }
      });
    } else {
      Object.keys(this.productForm.controls).forEach(key => {
        const control = this.productForm.get(key);
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

  volverAInicio(): void {
    this.router.navigate(['/comun/dashboard']);
  }
}
