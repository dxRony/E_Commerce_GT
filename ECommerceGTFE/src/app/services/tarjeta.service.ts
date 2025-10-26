import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TarjetaRequest, TarjetaResponse } from '../models/tarjeta.model';
import { MensajeResponse } from '../models/carrito.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TarjetaService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/tarjetas`;

  getMisTarjetas(): Observable<TarjetaResponse[]> {
    return this.http.get<TarjetaResponse[]>(this.apiUrl);
  }

  agregarTarjeta(tarjeta: TarjetaRequest): Observable<TarjetaResponse> {
    return this.http.post<TarjetaResponse>(this.apiUrl, tarjeta);
  }

  eliminarTarjeta(id: number): Observable<MensajeResponse> {
    return this.http.delete<MensajeResponse>(`${this.apiUrl}/${id}`);
  }

  getTarjeta(id: number): Observable<TarjetaResponse> {
    return this.http.get<TarjetaResponse>(`${this.apiUrl}/${id}`);
  }
}

