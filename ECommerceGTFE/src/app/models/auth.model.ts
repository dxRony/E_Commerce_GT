export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegistroVendedorRequest {
  nombre: string;
  email: string;
  password: string;
  celular: number;
  direccion: string;
}

export interface RegistroVendedorResponse {
  //token: string;
  id: number;
  email: string;
  nombre: string;
  rol: number;
}

export interface AuthResponse {
  token: string;
  type: string;
  id: number;
  email: string;
  nombre: string;
  rol: number;
}

export interface TokenValidation {
  valido: boolean;
  usuario: {
    id: number;
    email: string;
    nombre: string;
    rol: number;
  };
}