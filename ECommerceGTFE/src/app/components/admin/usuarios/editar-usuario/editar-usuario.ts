import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { AdminService } from '../../../../services/admin.service';
import { EmpleadoResponse, ActualizarEmpleadoRequest } from '../../../../models/user.model';

@Component({
  selector: 'app-editar-usuario',
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  standalone: true,
  templateUrl: './editar-usuario.html',
  styleUrl: './editar-usuario.css'
})
export class EditarUsuario implements OnInit {
  editForm: FormGroup;
  empleadoId: number = 0;
  empleado: EmpleadoResponse | null = null;
  isLoading: boolean = true;
  isSubmitting: boolean = false;
  error: string = '';
  success: string = '';

  roles = [
    { id: 1, nombre: 'Administrador' },
    { id: 2, nombre: 'Moderador' },
    { id: 3, nombre: 'Logistica' }
  ];

  constructor(
    private fb: FormBuilder,
    private adminService: AdminService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.editForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      celular: ['', [Validators.required, Validators.pattern('^[0-9]{8}$')]],
      direccion: ['', [Validators.required, Validators.maxLength(200)]]
    });
  }

  ngOnInit(): void {
    this.empleadoId = Number(this.route.snapshot.paramMap.get('id'));
    this.cargarEmpleado();
  }

  cargarEmpleado(): void {
    this.isLoading = true;
    this.adminService.obtenerEmpleado(this.empleadoId).subscribe({
      next: (empleado) => {
        this.empleado = empleado;
        this.cargarDatosEnForm();
        this.isLoading = false;
      },
      error: (error) => {
        this.error = 'cargando datos';
        this.isLoading = false;
        console.error('Error: ', error);
      }
    });
  }

  cargarDatosEnForm(): void {
    if (this.empleado) {
      this.editForm.patchValue({
        nombre: this.empleado.nombre,
        celular: this.empleado.celular,
        direccion: this.empleado.direccion
      });
    }
  }

  onSubmit(): void {
    if (this.editForm.valid && this.empleado) {
      this.isSubmitting = true;
      this.error = '';
      this.success = '';

      const request: ActualizarEmpleadoRequest = {
        nombre: this.editForm.get('nombre')?.value,
        celular: this.editForm.get('celular')?.value,
        direccion: this.editForm.get('direccion')?.value
      };

      this.adminService.actualizarEmpleado(this.empleadoId, request).subscribe({
        next: (empleadoActualizado) => {
          this.empleado = empleadoActualizado;
          this.success = 'Empleado actualizado correctamente.';
          this.isSubmitting = false;
          alert(this.success);
          this.volverALista();
        },
        error: (error) => {
          this.error = ' al actualizar el empleado.';
          this.isSubmitting = false;
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

  suspenderEmpleado(): void {
    if (this.empleado && confirm(`seguro de que deseas ${this.empleado.suspendido ? 'activar' : 'suspender'} a ${this.empleado.nombre}?`)) {
      this.adminService.alternarActivoEmpleado(this.empleadoId).subscribe({
        next: (empleadoActualizado) => {
          this.empleado = empleadoActualizado;
          this.success = `Empleado ${empleadoActualizado.suspendido ? 'suspendido' : 'activado'} correctamente.`;
          alert(this.success);
          this.volverALista();
        },
        error: (error) => {
          this.error = 'al cambiar el estado del empleado.';
          console.error('Error:', error);
        }
      });
    }
  }

  getRolNombre(rol: number): string {
    const rolEncontrado = this.roles.find(r => r.id === rol);
    return rolEncontrado ? rolEncontrado.nombre : 'Desconocido';
  }

  getEstadoTexto(suspendido: boolean): string {
    return suspendido ? 'Suspendido' : 'Activo';
  }

  getEstadoClase(suspendido: boolean): string {
    return suspendido ? 'estado-suspendido' : 'estado-activo';
  }

  volverALista(): void {
    this.router.navigate(['/admin/usuarios/listar']);
  }
}
