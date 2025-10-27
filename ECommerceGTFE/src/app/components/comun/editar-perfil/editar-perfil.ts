import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { UserService } from '../../../services/user.service';
import { TokenService } from '../../../services/token.service';
import { VendedorResponse, ActualizarVendedorRequest } from '../../../models/user.model';

@Component({
  selector: 'app-editar-perfil',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './editar-perfil.html',
  styleUrl: './editar-perfil.css'
})
export class EditarPerfil implements OnInit {
  profileForm: FormGroup;
  usuario: VendedorResponse | null = null;
  isLoading: boolean = true;
  isSubmitting: boolean = false;
  error: string = '';
  success: string = '';

  roles = [
    { id: 1, nombre: 'Comun' }
  ];

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private tokenService: TokenService,
    private router: Router
  ) {
    this.profileForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      email: [{ value: '', disabled: true }],
      celular: ['', [Validators.required, Validators.pattern('^[0-9]{8}$')]],
      direccion: ['', [Validators.required, Validators.maxLength(200)]],
      rol: [{ value: '', disabled: true }]
    });
  }

  ngOnInit(): void {
    this.cargarPerfil();
  }

  /**
   * metodo que obtiene y carga el perfil del usuario
   */
  cargarPerfil(): void {
    this.isLoading = true;
    this.userService.getMiPerfil().subscribe({
      next: (usuario) => {
        this.usuario = usuario;
        this.cargarDatosEnFormulario();
        this.isLoading = false;
      },
      error: (error) => {
        this.error = ' al cargar el perfil.';
        this.isLoading = false;
        console.error('Error:', error);
      }
    });
  }

  /**
   * metodo que carga los datos del usuario en el form
   */
  cargarDatosEnFormulario(): void {
    if (this.usuario) {
      this.profileForm.patchValue({
        nombre: this.usuario.nombre,
        email: this.usuario.email,
        celular: this.usuario.celular,
        direccion: this.usuario.direccion,
        rol: this.getRolNombre(this.usuario.rol)
      });
    }
  }

  /**
   * disparador para realizar la edicion del usuario
   */
  onSubmit(): void {
    if (this.profileForm.valid && this.usuario) {
      this.isSubmitting = true;
      this.error = '';
      this.success = '';
      // creabdo request del usuariu
      const request: ActualizarVendedorRequest = {
        nombre: this.profileForm.get('nombre')?.value,
        celular: Number(this.profileForm.get('celular')?.value),
        direccion: this.profileForm.get('direccion')?.value
      };

      //actualizando el perfil
      this.userService.updatePerfil(request).subscribe({
        next: (usuarioActualizado) => {
          this.usuario = usuarioActualizado;
          this.success = 'Perfil actualizado.';
          this.isSubmitting = false;

          this.tokenService.guardarUsuario({
            id: usuarioActualizado.id,
            nombre: usuarioActualizado.nombre,
            email: usuarioActualizado.email,
            rol: usuarioActualizado.rol
          });
        },
        error: (error) => {
          this.error = ' al actualizar el perfil.';
          this.isSubmitting = false;
          console.error('Error :', error);
        }
      });
    } else {
      Object.keys(this.profileForm.controls).forEach(key => {
        const control = this.profileForm.get(key);
        if (control?.enabled) {
          control?.markAsTouched();
        }
      });
    }
  }

  getRolNombre(rolId: number): string {
    const rol = this.roles.find(r => r.id === rolId);
    return rol ? rol.nombre : 'Desconocido';
  }

  getEstadoTexto(suspendido: boolean): string {
    return suspendido ? 'Suspendido' : 'Activo';
  }

  getEstadoClase(suspendido: boolean): string {
    return suspendido ? 'estado-suspendido' : 'estado-activo';
  }

  volverAInicio(): void {
    this.router.navigate(['/comun/dashboard']);

  }
}
