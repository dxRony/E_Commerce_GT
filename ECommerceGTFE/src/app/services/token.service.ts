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

  /**
   * obtiene el almacenamiento local del navegador
   * si la ejecucion es en el navegador
   * @returns 
   */
  private getLocalStorage(): Storage | null {
    if (isPlatformBrowser(this.platformId)) {
      return localStorage;
    }
    return null;
  }

  /**
   * guarda el token en el almacenamiento del navegador
   * @param token 
   */
  guardarToken(token: string): void {
    const storage = this.getLocalStorage();
    if (storage) {
      storage.setItem(this.TOKEN_KEY, token);
    }
  }

  /**
   * obtiene el token del almacenamiento del navegador
   * @returns 
   */
  obtenerToken(): string | null {
    const storage = this.getLocalStorage();
    return storage ? storage.getItem(this.TOKEN_KEY) : null;
  }

  /**
   * elimina el token del almacenamiento del navegador
   */
  eliminarToken(): void {
    const storage = this.getLocalStorage();
    if (storage) {
      storage.removeItem(this.TOKEN_KEY);
      storage.removeItem(this.USER_KEY);
    }
  }

  /**
   * guarda al usuario en el almacenamiento del nav
   * @param usuario 
   */
  guardarUsuario(usuario: any): void {
    const storage = this.getLocalStorage();
    if (storage) {
      storage.setItem(this.USER_KEY, JSON.stringify(usuario));
    }
  }

  /**
   * obtiene al usuario del almacenamiento del nav
   * @returns 
   */
  obtenerUsuario(): any {
    const storage = this.getLocalStorage();
    if (!storage) return null;

    const usuario = storage.getItem(this.USER_KEY);
    return usuario ? JSON.parse(usuario) : null;
  }

  /**
   * verifica si el usuario esta logueado
   * a traves del token
   * @returns 
   */
  estaLogueado(): boolean {
    const token = this.obtenerToken();
    return !!token;
  }

  /**
   * obtiene al rol del usuario
   * @returns 
   */
  obtenerRolUser(): number | null {
    const usuario = this.obtenerUsuario();
    return usuario ? usuario.rol : null;
  }

  /**
   * obtiene al id del usuario
   * @returns 
   */
  obtenerIdUser(): number | null {
    const usuario = this.obtenerUsuario();
    return usuario ? usuario.id : null;
  }
}