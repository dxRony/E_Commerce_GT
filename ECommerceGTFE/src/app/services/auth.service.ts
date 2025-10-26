import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { LoginRequest, RegistroVendedorRequest, RegistroVendedorResponse, AuthResponse, TokenValidation } from '../models/auth.model';
import { TokenService } from './token.service';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;

  constructor(
    private http: HttpClient,
    private tokenService: TokenService,
    private router: Router
  ) {}

  /**
   * envia los datos de login al backend y guarda el token y datos del usuario en el almacenamiento local
   * @param loginRequest 
   * @returns 
   */
  login(loginRequest: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, loginRequest)
      .pipe(
        tap(response => {
          this.tokenService.guardarToken(response.token);
          this.tokenService.guardarUsuario({
            id: response.id,
            email: response.email,
            nombre: response.nombre,
            rol: response.rol
          });
        })
      );
  }

  /**
   * envia los datos de registro al backend y guarda el token y datos del usuario en el almacenamiento local
   * @param registroRequest 
   * @returns 
   */
  registro(registroRequest: RegistroVendedorRequest): Observable<RegistroVendedorResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/registro`, registroRequest)
      .pipe(
        tap(response => {
          this.tokenService.guardarToken(response.token);
          this.tokenService.guardarUsuario({
            id: response.id,
            email: response.email,
            nombre: response.nombre,
            rol: response.rol
          });
        })
      );
  }

  /**
   * remueve el token del usuario y es redirigido al login automaticamente
   */
  logout(): void {
    this.tokenService.eliminarToken();
  }

  verificarToken(): Observable<TokenValidation> {
    return this.http.get<TokenValidation>(`${this.apiUrl}/verificar`);
  }

  estaLogueado(): boolean {
  return this.tokenService.estaLogueado();
}

  obtenerRolUser(): number {
  const user = this.tokenService.obtenerUsuario();
  return user?.rol || 0;
}

  obtenerUsuario(): any {
    return this.tokenService.obtenerUsuario();
  }

  esAdmin(): boolean {
    return this.obtenerRolUser() === 4;
  }

  esModerador(): boolean {
    return this.obtenerRolUser() === 2;
  }

  esLogistica(): boolean {
    return this.obtenerRolUser() === 3;
  }

  esComun(): boolean {
    return this.obtenerRolUser() === 1;
  }
}