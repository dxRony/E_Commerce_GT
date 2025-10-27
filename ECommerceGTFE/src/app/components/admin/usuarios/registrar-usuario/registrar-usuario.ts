import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AdminService } from '../../../../services/admin.service';
import { RegistroEmpleadoRequest, EmpleadoResponse } from '../../../../models/user.model';

@Component({
  selector: 'app-registrar-usuario',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './registrar-usuario.html',
  styleUrl: './registrar-usuario.css'
})
export class RegistrarUsuario {
registerForm: FormGroup;
  isSubmitting: boolean = false;
  error: string = '';
  success: string = '';

  roles = [
    { id: 2, nombre: 'Moderador' },
    { id: 3, nombre: 'Logistica' },
    { id: 4, nombre: 'Administrador' }
  ];

  constructor(
    private fb: FormBuilder,
    private adminService: AdminService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]],
      celular: ['', [Validators.required, Validators.pattern('^[0-9]{8}$')]],
      direccion: ['', [Validators.required, Validators.maxLength(200)]],
      rol: ['', [Validators.required, Validators.min(2), Validators.max(4)]]
    }, { validators: this.passwordMatchValidator });
  }

  /**
   * metodo que valida que las contrasenias sean iguales
   * @param form
   * @returns 
   */
  passwordMatchValidator(form: AbstractControl): ValidationErrors | null {
    const password = form.get('password');
    const confirmPassword = form.get('confirmPassword');
    
    if (password && confirmPassword && password.value !== confirmPassword.value) {
      confirmPassword.setErrors({ passwordMismatch: true });
      return { passwordMismatch: true };
    } else {
      confirmPassword?.setErrors(null);
      return null;
    }
  }

  /**
   * disparador del registro de empleado
   */
  onSubmit(): void {
    if (this.registerForm.valid) {
      this.isSubmitting = true;
      this.error = '';
      this.success = '';

      const { confirmPassword, ...request } = this.registerForm.value;
      const empleadoRequest: RegistroEmpleadoRequest = {
        ...request,
        celular: Number(request.celular),
        rol: Number(request.rol)
      };

      this.adminService.registrarEmpleado(empleadoRequest).subscribe({
        next: (empleadoCreado: EmpleadoResponse) => {
          this.success = 'Empleado registrado';
          this.isSubmitting = false;
          alert(this.success);
          this.volverALista();
        },
        error: (error) => {
          this.isSubmitting = false;
          if (error.error?.message) {
            this.error = error.error.message;
          } else {
            this.error = ' al registrar el empleado';
          }
          console.error('Error:', error);
        }
      });
    } else {
      Object.keys(this.registerForm.controls).forEach(key => {
        const control = this.registerForm.get(key);
        control?.markAsTouched();
      });
    }
  }

  getRolNombre(rolId: number): string {
    const rol = this.roles.find(r => r.id === rolId);
    return rol ? rol.nombre : 'Desconocido';
  }

  volverALista(): void {
    this.router.navigate(['/admin/usuarios/listar']);
  }
}
