import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { TokenService } from '../../../services/token.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-admin-layout',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './admin-layout.html',
  styleUrls: ['./admin-layout.css']
})
export class AdminLayout implements OnInit {
  isNavbarOpen = false;
  openDropdown: string | null = null;
  userName: string = '';

  constructor(
    private tokenService: TokenService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarNombre();
  }

  cargarNombre(): void {
    const user = this.tokenService.obtenerUsuario();
    this.userName = user?.nombre || 'Administrador';
  }

  toggleNavbar(): void {
    this.isNavbarOpen = !this.isNavbarOpen;
  }

  toggleDropdown(dropdown: string): void {
    this.openDropdown = this.openDropdown === dropdown ? null : dropdown;
  }

  closeDropdowns(): void {
    this.openDropdown = null;
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}