import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ArticuloRequest, ArticuloResponse } from '../models/articulos.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ArticuloService {
  private apiUrl = `${environment.apiUrl}/articulos`;

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

  /**
   * metodoq ue crea un articulo
   * @param request 
   * @returns 
   */
  crearArticulo(request: ArticuloRequest): Observable<ArticuloResponse> {
    return this.http.post<ArticuloResponse>(this.apiUrl, request);
  }

  /**
   * metodo que obtiene los articulos del usuario autenticado
   * @returns lista de articulos del usuario
   */
  getMisArticulos(): Observable<ArticuloResponse[]> {
    return this.http.get<ArticuloResponse[]>(`${this.apiUrl}/mis-articulos`);
  }

  /**
   * metodo que obtiene un articulo por su id
   * @param id del articulo
   * @returns articulo con el id
   */
  getArticuloById(id: number): Observable<ArticuloResponse> {
    return this.http.get<ArticuloResponse>(`${this.apiUrl}/${id}`);
  }

  /**
   * metodo que actualiza un articulo
   * @param id del articulo
   * @param request 
   * @returns articulo actualizado
   */
  updateArticulo(id: number, request: ArticuloRequest): Observable<ArticuloResponse> {
    return this.http.put<ArticuloResponse>(`${this.apiUrl}/${id}`, request);
  }

}