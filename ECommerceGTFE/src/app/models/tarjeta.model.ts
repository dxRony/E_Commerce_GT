export interface TarjetaRequest {
  numeracion: string;
  fechaVencimiento: string;
  cvv: string;
}

export interface TarjetaResponse {
  id: number;
  numeracion: string;
  fechaVencimiento: string;
  activa: boolean;
}