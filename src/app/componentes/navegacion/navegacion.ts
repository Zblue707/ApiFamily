import { CommonModule } from '@angular/common';
import { Component, OnInit, computed, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';

import { AuthService } from '../../servicios/auth';

@Component({
  selector: 'app-navegacion',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navegacion.html',
  styleUrl: './navegacion.css',
})
export class Navegacion implements OnInit {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  readonly usuario = this.authService.usuario;
  readonly iniciales = computed(() => {
    const nombre = this.usuario()?.nombre ?? '';

    return nombre
      .split(' ')
      .filter(Boolean)
      .slice(0, 2)
      .map((parte) => parte[0]?.toUpperCase() ?? '')
      .join('');
  });

  ngOnInit(): void {
    this.authService.verificarSesion().subscribe();
  }

  iniciarSesion(): void {
    this.authService.iniciarSesionConGoogle();
  }

  cerrarSesion(): void {
    this.authService.cerrarSesion().subscribe(() => {
      void this.router.navigate(['/inicio-sesion']);
    });
  }
}
