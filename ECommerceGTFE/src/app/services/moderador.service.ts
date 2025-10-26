import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ArticuloResponse } from '../models/articulos.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ModeratorService {
  private apiUrl = `${environment.apiUrl}/articulos`;

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