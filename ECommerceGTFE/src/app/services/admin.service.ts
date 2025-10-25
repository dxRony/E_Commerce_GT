import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EmpleadoResponse, ActualizarEmpleadoRequest, RegistroEmpleadoRequest } from '../models/user.model';
import { Reporte1Response, Reporte2Response, ReporteRequest } from '../models/reporte.model';

@Injectable({
    providedIn: 'root'
})
export class AdminService {
    private apiUrl = 'http://localhost:8080/api/admin';

    constructor(private http: HttpClient) { }

    obtenerTodosEmpleados(): Observable<EmpleadoResponse[]> {
        return this.http.get<EmpleadoResponse[]>(`${this.apiUrl}/empleados`);
    }

    obtenerEmpleadosPorRol(rol: number): Observable<EmpleadoResponse[]> {
        return this.http.get<EmpleadoResponse[]>(`${this.apiUrl}/empleados/rol/${rol}`);
    }

    obtenerEmpleado(id: number): Observable<EmpleadoResponse> {
        return this.http.get<EmpleadoResponse>(`${this.apiUrl}/empleados/${id}`);
    }

    actualizarEmpleado(id: number, request: ActualizarEmpleadoRequest): Observable<EmpleadoResponse> {
        return this.http.put<EmpleadoResponse>(`${this.apiUrl}/empleados/${id}`, request);
    }

    alternarActivoEmpleado(id: number): Observable<EmpleadoResponse> {
        return this.http.put<EmpleadoResponse>(`${this.apiUrl}/empleados/${id}/suspender`, {});
    }

    registrarEmpleado(request: RegistroEmpleadoRequest): Observable<EmpleadoResponse> {
        return this.http.post<EmpleadoResponse>(`${this.apiUrl}/empleados`, request);
    }

    obtenerTop10ProductosVendidos(request: ReporteRequest): Observable<Reporte1Response[]> {
        return this.http.post<Reporte1Response[]>(`${this.apiUrl}/reportes/top10-productos-vendidos`, request);
    }

    obtenerTop5ClientesMasGanancias(request: ReporteRequest): Observable<Reporte2Response[]> {
        return this.http.post<Reporte2Response[]>(`${this.apiUrl}/reportes/top5-clientes-ganancias`, request);
    }
}