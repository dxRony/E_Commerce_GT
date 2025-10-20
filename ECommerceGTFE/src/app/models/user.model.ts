export interface User {
  id: number;
  nombre: string;
  email: string;
  celular: number;
  direccion: string;
  rol: number;
  suspendido: boolean;
}

export interface UpdateUserRequest {
  nombre: string;
  celular: number;
  direccion: string;
}

export interface ChangePasswordRequest {
  currentPassword: string;
  newPassword: string;
}

export interface Empleado {
  id: number;
  nombre: string;
  email: string;
  celular: number;
  direccion: string;
  rol: number;
  suspendido: boolean;
}

export interface EmpleadoResponse {
  id: number;
  nombre: string;
  email: string;
  celular: number;
  direccion: string;
  rol: number;
  suspendido: boolean;
}

export interface ActualizarEmpleadoRequest {
  nombre: string;
  celular: number;
  direccion: string;
}

export interface RegistroEmpleadoRequest {
  nombre: string;
  email: string;
  password: string;
  celular: number;
  direccion: string;
  rol: number;
}