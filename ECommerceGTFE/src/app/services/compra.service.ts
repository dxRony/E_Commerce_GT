import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CompraRequest, CompraResponse, DetalleCompraResponse } from '../models/compra.model';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class CompraService {
    private http = inject(HttpClient);
    private apiUrl = `${environment.apiUrl}/compras`;

    procesarPago(compraRequest: CompraRequest): Observable<CompraResponse> {
        return this.http.post<CompraResponse>(`${this.apiUrl}/pagar`, compraRequest);
    }

    obtenerMisCompras(): Observable<CompraResponse[]> {
        return this.http.get<CompraResponse[]>(this.apiUrl);
    }

    obtenerCompra(compraId: number): Observable<CompraResponse> {
        return this.http.get<CompraResponse>(`${this.apiUrl}/${compraId}`);
    }

    obtenerMisVentas(): Observable<DetalleCompraResponse[]> {
        return this.http.get<DetalleCompraResponse[]>(`${this.apiUrl}/ventas`);
    }
}