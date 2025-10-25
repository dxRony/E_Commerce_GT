import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
/**
 * Servicio para manejar el token de autenticacion y datos del usuario
 */
export class TokenService {
  private readonly TOKEN_KEY = 'auth_token';
  private readonly USER_KEY = 'user_data';

  constructor(@Inject(PLATFORM_ID) private platformId: any) { }

  private getLocalStorage(): Storage | null {
    if (isPlatformBrowser(this.platformId)) {
      return localStorage;
    }
    return null;
  }

  guardarToken(token: string): void {
    const storage = this.getLocalStorage();
    if (storage) {
      storage.setItem(this.TOKEN_KEY, token);
    }
  }

  obtenerToken(): string | null {
    const storage = this.getLocalStorage();
    return storage ? storage.getItem(this.TOKEN_KEY) : null;
  }

  eliminarToken(): void {
    const storage = this.getLocalStorage();
    if (storage) {
      storage.removeItem(this.TOKEN_KEY);
      storage.removeItem(this.USER_KEY);
    }
  }

  guardarUsuario(usuario: any): void {
    const storage = this.getLocalStorage();
    if (storage) {
      storage.setItem(this.USER_KEY, JSON.stringify(usuario));
    }
  }

  obtenerUsuario(): any {
    const storage = this.getLocalStorage();
    if (!storage) return null;
    
    const usuario = storage.getItem(this.USER_KEY);
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