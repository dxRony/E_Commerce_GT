import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';
import { roleGuard } from './guards/rol.guard';

export const routes: Routes = [
  {
    //ruta de bienvenida
    path: 'welcome',
    loadComponent: () => import('./components/welcome/welcome').then(m => m.Welcome)
  },
  {
    //ruta de inicio de sesion
    path: 'login',
    loadComponent: () => import('./components/auth/login/login.component').then(m => m.LoginComponent)
  },
  {
    //ruta de registro
    path: 'registro',
    loadComponent: () => import('./components/auth/register/register').then(m => m.RegisterComponent)
  },
   {
    //ruta de articulos al publico
    path: 'articulos',
    loadComponent: () => import('./components/articulos/articulos').then(m => m.Articulos)
  },
  {
    path: '',
    redirectTo: '/welcome',
    pathMatch: 'full'
  },
  {
    path: '**',
    redirectTo: '/welcome'
  }
];