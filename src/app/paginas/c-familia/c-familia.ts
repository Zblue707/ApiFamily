import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';

import { AuthService } from '../../servicios/auth';
import { Integrante, ServFamilia } from '../../servicios/familia';

@Component({
  selector: 'app-c-familia',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './c-familia.html',
  styleUrl: './c-familia.css',
})
export class CFamilia implements OnInit {
  private readonly servicioFamilia = inject(ServFamilia);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  familia: Integrante[] = [];
  cargando = true;
  error = '';
  readonly usuario = this.authService.usuario;

  ngOnInit(): void {
    this.authService.verificarSesion().subscribe({
      next: (usuario) => {
        if (!usuario) {
          void this.router.navigate(['/login']);
          return;
        }

        this.cargarFamilia();
      },
      error: () => {
        this.cargando = false;
        this.error = 'No se pudo validar la sesion actual.';
      },
    });
  }

  iniciarSesion(): void {
    void this.router.navigate(['/login']);
  }

  private cargarFamilia(): void {
    this.cargando = true;
    this.error = '';

    this.servicioFamilia.consultarFamilia().subscribe({
      next: (data) => {
        this.familia = data;
        this.cargando = false;
      },
      error: () => {
        this.cargando = false;
        this.error =
          'No fue posible cargar los integrantes. Revisa que el backend Spring este corriendo en http://localhost:8080.';
      },
    });
  }
}
