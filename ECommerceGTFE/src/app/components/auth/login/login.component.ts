import { Component, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { LoginRequest } from '../../../models/auth.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  loading = signal(false);
  error = signal('');

  loginForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]]
  });

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.loading.set(true);
      this.error.set('');

      const loginRequest: LoginRequest = this.loginForm.value as LoginRequest;

      this.authService.login(loginRequest).subscribe({
        next: (response) => {
          this.loading.set(false);
          this.redirectByRole(response.rol);
        },
        error: (error) => {
          this.loading.set(false);
          this.error.set('Error al iniciar sesion, credenciales incorrectas.');
          console.error('Login error:', error);
        }
      });
    }
  }

  private redirectByRole(role: number): void {

    switch (role) {
      case 1: this.router.navigate(['/comun']); {
        confirm('Redirigiendo a Usuario Común');
        break;
      }
      case 2: this.router.navigate(['/moderador']); {
        confirm('Redirigiendo a Moderador');
        break;
      }
      case 3: this.router.navigate(['/logistica']); {
        confirm('Redirigiendo a Logística');
        break;
      }
      case 4: this.router.navigate(['/admin']); {
        confirm('Redirigiendo al admin');
        break;
      }


      default: this.router.navigate(['/welcome']); {
        console.warn('Rol de usuario desconocido, redirigiendo a la pantalla principal.');
      }
    }
  }
}