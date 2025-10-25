export interface ReporteRequest {
  fechaInicio: string;
  fechaFin: string;
}

export interface Reporte1Response {
  productoId: number;
  productoNombre: string;
  productoCategoria: string;
  cantidadVendida: number;
  totalVendido: number;
  vendedorNombre: string;
}

export interface Reporte2Response {
  clienteId: number;
  clienteNombre: string;
  clienteEmail: string;
  totalCompras: number;
  totalGastado: number;
  gananciaGenerada: number;
}