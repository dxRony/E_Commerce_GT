import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
/**
 * Servicio para manejar el token de autenticacion y datos del usuario
 */
export class TokenService {
  private readonly TOKEN_KEY = 'auth_token';
  private readonly USER_KEY = 'user_data';

  constructor() {}

  guardarToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  obtenerToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  eliminarToken(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
  }

  guardarUsuario(usuario: any): void {
    localStorage.setItem(this.USER_KEY, JSON.stringify(usuario));
  }

  obtenerUsuario(): any {
    const usuario = localStorage.getItem(this.USER_KEY);
    return usuario ? JSON.parse(usuario) : null;
  }

  estaLogueado(): boolean {
    const token = this.obtenerToken();
    return !!token;
  }

  obtenerRolUser(): number | null {
    const usuario = this.obtenerUsuario();
    return usuario ? usuario.rol : null;
  }

  obtenerIdUser(): number | null {
    const usuario = this.obtenerUsuario();
    return usuario ? usuario.id : null;
  }
}