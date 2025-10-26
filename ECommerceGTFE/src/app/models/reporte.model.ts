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

export interface Reporte3Response {
  vendedorId: number;
  vendedorNombre: string;
  vendedorEmail: string;
  totalProductosVendidos: number;
  totalArticulosVendidos: number;
  totalVentas: number;
  gananciasVendedor: number;
}

export interface Reporte4Response {
  clienteId: number;
  clienteNombre: string;
  clienteEmail: string;
  totalPedidos: number;
  totalGastado: number;
  promedioPorPedido: number;
  ultimaCompra: string;
}

export interface Reporte5Response {
  vendedorId: number;
  vendedorNombre: string;
  vendedorEmail: string;
  totalProductos: number;
}
