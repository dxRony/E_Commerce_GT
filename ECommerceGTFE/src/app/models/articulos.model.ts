export interface ArticuloResponse {
  id: number;
  nombre: string;
  descripcion: string;
  imagen: string;
  precio: number;
  stock: number;
  estadoArticulo: string;
  categoria: string;
  estadoAprobacion: string;
  usuarioId: number;
  usuarioNombre: string;
  ratingPromedio: number;
  totalCalificaciones: number;
}

