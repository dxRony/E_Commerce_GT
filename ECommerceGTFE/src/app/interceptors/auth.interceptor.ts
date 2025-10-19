import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { isPlatformBrowser } from '@angular/common';
import { TokenService } from '../services/token.service';
import { Router } from '@angular/router';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(
    private tokenService: TokenService,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: any
  ) {}

  /**
   * metodo que intercepta las solicitudes HTTP para agregar el token de autenticacion
   * @param request es la solicitud que se hace
   * @param next envia la peticion al server
   */
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    //si la url tiene public se omite el token (servicios publicos)
    if (request.url.includes('/public/')) {
      return next.handle(request);
    }
    //si la url requiere token, se obtiene el token
    let authReq = request;
    if (isPlatformBrowser(this.platformId)) {
      const token = this.tokenService.obtenerToken();
      //si hay token se agrega al encabezado
      if (token) {
        authReq = request.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`
          }
        });
      }
    }
    //si no se captura el error y se redirige al login
    return next.handle(authReq).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          if (isPlatformBrowser(this.platformId)) {
            this.tokenService.eliminarToken();
          }
          this.router.navigate(['/login']);
        }
        return throwError(() => error);
      })
    );
  }
}