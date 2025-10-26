import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { VendedorResponse, ActualizarVendedorRequest } from '../models/user.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = `${environment.apiUrl}/usuario`;

  constructor(private http: HttpClient) { }

  getMiPerfil(): Observable<VendedorResponse> {
    return this.http.get<VendedorResponse>(`${this.apiUrl}/perfil`);
  }

  updatePerfil(request: ActualizarVendedorRequest): Observable<VendedorResponse> {
    return this.http.put<VendedorResponse>(`${this.apiUrl}/perfil`, request);
  }
}