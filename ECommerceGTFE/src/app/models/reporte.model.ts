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
