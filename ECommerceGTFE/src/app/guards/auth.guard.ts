import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * funcion que protege las rutas en caso de que el usuario no haya iniciado su sesion
 * @param route 
 * @param state 
 * @returns true si el usuario tiene una sesion, false en caso contrario
 */
export const authGuard: CanActivateFn = (route, state) => {

  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.estaLogueado()) {
    return true;
  } else {
    router.navigate(['/login']);
    return false;
  }
};