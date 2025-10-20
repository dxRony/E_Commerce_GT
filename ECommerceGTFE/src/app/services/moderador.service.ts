import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ArticuloResponse } from '../models/articulos.model';

@Injectable({
  providedIn: 'root'
})
export class ModeratorService {
  private apiUrl = 'http://localhost:8080/api/articulos';

  constructor(private http: HttpClient) { }

  getArticulosPendientes(): Observable<ArticuloResponse[]> {
    return this.http.get<ArticuloResponse[]>(`${this.apiUrl}/moderador/pendientes`);
  }

  aprobarArticulo(id: number): Observable<ArticuloResponse> {
    return this.http.put<ArticuloResponse>(`${this.apiUrl}/moderador/${id}/aprobar`, {});
  }

  rechazarArticulo(id: number): Observable<ArticuloResponse> {
    return this.http.put<ArticuloResponse>(`${this.apiUrl}/moderador/${id}/rechazar`, {});
  }
}