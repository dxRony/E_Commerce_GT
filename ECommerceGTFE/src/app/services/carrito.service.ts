import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CarritoDetalleRequest, CarritoDetalleResponse, CarritoResponse, MensajeResponse } from '../models/carrito.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CarritoService {
  private apiUrl = `${environment.apiUrl}/carrito`;

  constructor(private http: HttpClient) { }

  obtenerCarrito(): Observable<CarritoResponse> {
    return this.http.get<CarritoResponse>(this.apiUrl);
  }

  agregarAlCarrito(request: CarritoDetalleRequest): Observable<CarritoDetalleResponse> {
    return this.http.post<CarritoDetalleResponse>(`${this.apiUrl}/agregar`, request);
  }

  actualizarCantidad(itemId: number, cantidad: number): Observable<CarritoDetalleResponse | MensajeResponse> {
    return this.http.put<CarritoDetalleResponse | MensajeResponse>(`${this.apiUrl}/${itemId}/cantidad?cantidad=${cantidad}`, {});
  }

  eliminarDelCarrito(itemId: number): Observable<MensajeResponse> {
    return this.http.delete<MensajeResponse>(`${this.apiUrl}/${itemId}`);
  }

  limpiarCarrito(): Observable<MensajeResponse> {
    return this.http.delete<MensajeResponse>(`${this.apiUrl}/limpiar`);
  }

  obtenerCantidadTotal(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/cantidad-total`);
  }
}