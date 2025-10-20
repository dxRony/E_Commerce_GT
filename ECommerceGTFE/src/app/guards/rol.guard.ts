import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * funcion que controla el acceso a las rutas segun el rol que tenga el usuario
 * @param route 
 * @param state 
 * @returns true si el rol es el requerido segun la ruta, false en caso contrario
 */
export const roleGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  
  const requiredRole = route.data?.['expectedRole'] as number;
  const userRole = authService.obtenerRolUser();

  if (authService.estaLogueado() && userRole === requiredRole) {
    return true;
  } else {
    router.navigate(['/welcome']);
    return false;
  }
};