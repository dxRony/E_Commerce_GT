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