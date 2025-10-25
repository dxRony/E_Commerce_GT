export interface CompraRequest {
  tarjetaId: number;
}

export interface CompraResponse {
  id: number;
  fecha: string;
  total: number;
  estadoEntrega: string;
  comisionUsuario: number;
  comisionPagina: number;
  tarjetaNumeracion: string;
  detalles: DetalleCompraResponse[];
}

export interface DetalleCompraResponse {
  id: number;
  articuloId: number;
  articuloNombre: string;
  articuloImagen: string;
  precioUnitario: number;
  cantidad: number;
  subtotal: number;
  vendedorId: number;
  vendedorNombre: string;
}

export interface MensajeResponse {
  message: string;
}