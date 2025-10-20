import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EntregaResponse, DetalleEntregaResponse, ModificarEntregaRequest } from '../models/entrega.model';

@Injectable({
  providedIn: 'root'
})
export class LogisticaService {
  private apiUrl = 'http://localhost:8080/api/entregas';

  constructor(private http: HttpClient) { }

  obtenerEntregasEnCurso(): Observable<EntregaResponse[]> {
    return this.http.get<EntregaResponse[]>(`${this.apiUrl}/logistica/en-curso`);
  }

  actualizarFechaEstimada(entregaId: number, request: ModificarEntregaRequest): Observable<EntregaResponse> {
    return this.http.put<EntregaResponse>(`${this.apiUrl}/logistica/${entregaId}/fecha-estimada`, request);
  }

  marcarDetalleListo(detalleId: number): Observable<DetalleEntregaResponse> {
    return this.http.put<DetalleEntregaResponse>(`${this.apiUrl}/logistica/detalles/${detalleId}/listo`, {});
  }

  marcarComoEntregada(entregaId: number): Observable<EntregaResponse> {
    return this.http.put<EntregaResponse>(`${this.apiUrl}/logistica/${entregaId}/entregar`, {});
  }
}