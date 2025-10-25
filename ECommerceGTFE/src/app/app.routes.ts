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
      },
      {
        path: 'usuarios/listar',
        loadComponent: () => import('./components/admin/usuarios/lista-usuarios/lista-usuarios').then(m => m.ListaUsuarios)
      },
      {
        path: 'usuarios/editar/:id',
        loadComponent: () => import('./components/admin/usuarios/editar-usuario/editar-usuario').then(m => m.EditarUsuario)
      },
      {
        path: 'usuarios/registrar',
        loadComponent: () => import('./components/admin/usuarios/registrar-usuario/registrar-usuario').then(m => m.RegistrarUsuario)
      },
      {
        path: 'articulos',
        loadComponent: () => import('./components/admin/articulos/listar-articulos/listar-articulos').then(m => m.ListarArticulos)
      },
      {
        path: 'reportes/reporte1',
        loadComponent: () => import('./components/admin/reportes/reporte1/reporte1').then(m => m.Reporte1)
      },
      {
        path: 'reportes/reporte2',
        loadComponent: () => import('./components/admin/reportes/reporte2/reporte2').then(m => m.Reporte2)
      }
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
      {
        path: 'perfil',
        loadComponent: () => import('./components/comun/editar-perfil/editar-perfil').then(m => m.EditarPerfil)
      },
      {
        path: 'publicar-producto',
        loadComponent: () => import('./components/comun/articulos/publicar-articulo/publicar-articulo').then(m => m.PublicarArticulo)
      },
      {
        path: 'mis-productos',
        loadComponent: () => import('./components/comun/articulos/mis-articulos/mis-articulos').then(m => m.MisArticulos)
      },
      {
        path: 'editar-articulo/:id',
        loadComponent: () => import('./components/comun/articulos/editar-articulo/editar-articulo').then(m => m.EditarArticulo)
      },
      {
        path: 'articulos-disponibles',
        loadComponent: () => import('./components/comun/articulos/articulos-disponibles/articulos-disponibles').then(m => m.ArticulosDisponibles)
      },
      {
        path: 'carrito',
        loadComponent: () => import('./components/comun/carrito/carrito').then(m => m.Carrito)
      },
      {
        path: 'compras',
        loadComponent: () => import('./components/comun/compras/compras').then(m => m.Compras)
      },
      {
        path: 'ventas',
        loadComponent: () => import('./components/comun/ventas/ventas').then(m => m.Ventas)
      }
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
      {
        path: 'productos/pendientes',
        loadComponent: () => import('./components/moderador/articulos/articulos-pendientes/articulos-pendientes').then(m => m.ArticulosPendientes)
      }
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
      {
        path: 'pedidos/en-curso',
        loadComponent: () => import('./components/logistica/pedidos/pedidos-en-curso/pedidos-en-curso').then(m => m.PedidosEnCurso)
      }
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