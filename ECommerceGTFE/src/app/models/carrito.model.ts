export interface CarritoDetalleRequest {
  articuloId: number;
  cantidad: number;
}

export interface CarritoDetalleResponse {
  id: number;
  articuloId: number;
  articuloNombre: string;
  articuloPrecio: number;
  cantidad: number;
  subtotal: number;
  stockDisponible: number;
}

export interface CarritoResponse {
  detalles: CarritoDetalleResponse[];
  totalDetalles: number;
  totalPrecio: number;
}

export interface MensajeResponse {
  message: string;
}