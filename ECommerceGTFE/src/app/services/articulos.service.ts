import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ArticuloResponse } from '../models/articulos.model';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = 'http://localhost:8080/api/articulos';

  constructor(private http: HttpClient) { }

  /**
   * metodo que obtiene la lista de articulos de la api
   */
  getCatalogoPublico(): Observable<ArticuloResponse[]> {
    return this.http.get<ArticuloResponse[]>(`${this.apiUrl}/public/catalogo`);
  }

  /**
   * metodo que obtiene la lista de articulos de la api
   * @param categoria para el filtro de busqueda de articulos
   */
  getProductosPorCategoria(categoria: string): Observable<ArticuloResponse[]> {
    return this.http.get<ArticuloResponse[]>(`${this.apiUrl}/public/catalogo?categoria=${categoria}`);
  }

  /**
   * metodo busca productos por texto
   * @param busqueda texto para la busqueda
   * @returns 
   */
  buscarProductos(busqueda: string): Observable<ArticuloResponse[]> {
    return this.http.get<ArticuloResponse[]>(`${this.apiUrl}/public/buscar?q=${busqueda}`);
  }
}