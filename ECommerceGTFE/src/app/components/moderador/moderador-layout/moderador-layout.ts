import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { TokenService } from '../../../services/token.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-moderador-layout',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './moderador-layout.html',
  styleUrl: './moderador-layout.css'
})
export class ModeradorLayout implements OnInit {

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
    this.userName = user?.nombre || 'Moderador';
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
