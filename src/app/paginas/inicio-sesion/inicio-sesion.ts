import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';

import { AuthService } from '../../servicios/auth';

@Component({
  selector: 'app-inicio-sesion',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './inicio-sesion.html',
  styleUrl: './inicio-sesion.css',
})
export class InicioSesion implements OnInit {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  cargando = true;

  ngOnInit(): void {
    this.authService.verificarSesion().subscribe((usuario) => {
      this.cargando = false;

      if (usuario) {
        void this.router.navigate(['/familia']);
      }
    });
  }

  iniciarSesion(): void {
    this.authService.iniciarSesionConGoogle();
  }
}
