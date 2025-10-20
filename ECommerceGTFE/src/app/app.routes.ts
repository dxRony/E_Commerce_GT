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
    //rutas de admin
    path: 'admin',
    canActivate: [authGuard, roleGuard],
    data: { expectedRole: 4 },
    loadComponent: () => import('./components/admin/admin-layout/admin-layout').then(m => m.AdminLayout),
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        loadComponent: () => import('./components/admin/dashboard/dashboard').then(m => m.AdminDashboardComponent)
      },/**
      
      {
        path: 'usuarios/listar',
        loadComponent: () => import('./components/admin/users/user-list/user-list.component').then(m => m.UserListComponent)
      }, */
    ]
  },
  {
    //rutas de comun
    path: 'comun',
    canActivate: [authGuard, roleGuard],
    data: { expectedRole: 1 },
    loadComponent: () => import('./components/comun/comun-layout/comun-layout').then(m => m.ComunLayout),
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        loadComponent: () => import('./components/comun/dashboard/dashboard').then(m => m.Dashboard)
      },
    ]
  },
  {
    //rutas de moderador
    path: 'moderador',
    canActivate: [authGuard, roleGuard],
    data: { expectedRole: 2 },
    loadComponent: () => import('./components/moderador/moderador-layout/moderador-layout').then(m => m.ModeradorLayout),
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        loadComponent: () => import('./components/moderador/dashboard/dashboard').then(m => m.Dashboard)
      },
    ]
  },
  {
    //rutas de logistica
    path: 'logistica',
    canActivate: [authGuard, roleGuard],
    data: { expectedRole: 3 },
    loadComponent: () => import('./components/logistica/logistica-layout/logistica-layout').then(m => m.LogisticaLayout),
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        loadComponent: () => import('./components/logistica/dashboard/dashboard').then(m => m.Dashboard)
      },
    ]
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