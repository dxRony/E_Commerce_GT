export interface EntregaResponse {
  id: number;
  estado: string;
  fechaEstimada: string;
  fechaEntrega: string | null;
  compraId: number;
  compraTotal: number;
  usuarioNombre: string;
  usuarioDireccion: string;
  detalles?: DetalleEntregaResponse[];
}

export interface DetalleEntregaResponse {
  id: number;
  articuloId: number;
  articuloNombre: string;
  articuloImagen: string;
  cantidad: number;
  listo: boolean;
}

export interface ModificarEntregaRequest {
  fechaEstimada: string;
}