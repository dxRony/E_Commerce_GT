import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AdminService } from '../../../../services/admin.service';
import { EmpleadoResponse } from '../../../../models/user.model';

@Component({
  selector: 'app-lista-usuarios',
  imports: [CommonModule, FormsModule, RouterModule],
  standalone: true,
  templateUrl: './lista-usuarios.html',
  styleUrl: './lista-usuarios.css'
})
export class ListaUsuarios implements OnInit {
  empleados: EmpleadoResponse[] = [];
  empleadosFiltrados: EmpleadoResponse[] = [];
  isLoading: boolean = true;
  error: string = '';

  searchTerm: string = '';
  selectedRol: string = 'all';
  selectedEstado: string = 'all';

  //roles de empleados
  roles = [
    { id: 4, nombre: 'Administrador' },
    { id: 2, nombre: 'Moderador' },
    { id: 3, nombre: 'Logistica' }
  ];

  constructor(private adminService: AdminService) { }

  ngOnInit(): void {
    this.cargarEmpleados();
  }

  /**
   * metodo que carga a los empleados en la tabla
   */
  cargarEmpleados(): void {
    this.isLoading = true;
    this.adminService.obtenerTodosEmpleados().subscribe({
      next: (empleados) => {
        this.empleados = empleados;
        this.empleadosFiltrados = empleados;
        this.isLoading = false;
      },
      error: (error) => {
        this.error = 'Error al cargar los empleados. Por favor, intenta nuevamente.';
        this.isLoading = false;
        console.error('Error loading employees:', error);
      }
    });
  }

  /**
   * metodo que aplica los filtros 
   * nombre, emaik, direccion, rol, estado
   */
  aplicarFiltros(): void {
    let filtered = this.empleados;

    if (this.searchTerm) {
      const term = this.searchTerm.toLowerCase();
      filtered = filtered.filter(empleado =>
        empleado.nombre.toLowerCase().includes(term) ||
        empleado.email.toLowerCase().includes(term) ||
        empleado.direccion.toLowerCase().includes(term)
      );
    }

    if (this.selectedRol !== 'all') {
      filtered = filtered.filter(empleado =>
        empleado.rol === parseInt(this.selectedRol)
      );
    }

    if (this.selectedEstado !== 'all') {
      const estado = this.selectedEstado === 'activo';
      filtered = filtered.filter(empleado =>
        empleado.suspendido === !estado
      );
    }
    this.empleadosFiltrados = filtered;
  }

  getRolNombre(rol: number): string {
    const rolEncontrado = this.roles.find(r => r.id === rol);
    return rolEncontrado ? rolEncontrado.nombre : 'Desconocido';
  }

  getEstadoTexto(suspendido: boolean): string {
    return suspendido ? 'Suspendido' : 'Activo';
  }

  limpiarFiltros(): void {
    this.searchTerm = '';
    this.selectedRol = 'all';
    this.selectedEstado = 'all';
    this.aplicarFiltros();
  }

}
