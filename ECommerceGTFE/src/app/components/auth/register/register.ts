import { Component, signal } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './register.html',
  styleUrls: ['./register.css']
})
export class RegisterComponent {
  registerForm: FormGroup;
  loading = signal(false);
  error = signal('');

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]],
      celular: ['', [Validators.required, Validators.pattern('^[0-9]{8}$')]],
      direccion: ['', [Validators.required, Validators.minLength(5)]]
    }, { validators: this.passwordMatchValidator });
  }

  /**
   * metodo que valida que las contrasenias sean iguales
   * @param form
   * @returns 
   */
  passwordMatchValidator(form: FormGroup) {
    const password = form.get('password');
    const confirmPassword = form.get('confirmPassword');

    if (password && confirmPassword && password.value !== confirmPassword.value) {
      confirmPassword.setErrors({ passwordMismatch: true });
    } else {
      confirmPassword?.setErrors(null);
    }
  }

  /**
   * disparador del registro de usuario comun
   */
  onSubmit() {
    if (this.registerForm.valid) {
      this.loading.set(true);
      this.error.set('');

      const { confirmPassword, ...userData } = this.registerForm.value;

      this.authService.registro(userData).subscribe({
        next: (response) => {
          this.loading.set(false);
          this.redirectByRole(response.rol);
        },
        error: (error) => {
          this.loading.set(false);
          if (error.error?.message) {
            this.error.set(error.error.message);
          } else {
            this.error.set('Error en el reqistro. Por favor, intenta nuevamente.');
            alert(this.error);
          }
        }
      });
    } else {
      Object.keys(this.registerForm.controls).forEach(key => {
        const control = this.registerForm.get(key);
        control?.markAsTouched();
      });
    }
  }

  /**
   * metodo para redirigir al registrar a un usuario
   * soporta cualquier rol
   * @param role 
   */
  private redirectByRole(role: number): void {

    switch (role) {
      case 1: this.router.navigate(['/comun']); {
        break;
      }
      case 2: this.router.navigate(['/moderador']); {
        break;
      }
      case 3: this.router.navigate(['/logistica']); {
        break;
      }
      case 4: this.router.navigate(['/admin']); {
        break;
      }
      default: this.router.navigate(['/welcome']); {        
      }
    }
  }
}