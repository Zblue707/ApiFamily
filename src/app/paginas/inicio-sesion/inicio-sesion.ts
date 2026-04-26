import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { AuthService } from '../../servicios/auth';

@Component({
  selector: 'app-inicio-sesion',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './inicio-sesion.html',
  styleUrl: './inicio-sesion.css',
})
export class InicioSesion implements OnInit {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  cargando = true;
  enviando = false;
  correo = '';
  contrasena = '';
  error = '';

  ngOnInit(): void {
    if (this.route.snapshot.queryParamMap.get('error') === 'google') {
      this.error = 'No fue posible iniciar sesion con Google. Intenta otra vez o usa correo y contrasena.';
    }

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

  iniciarSesionConCorreo(): void {
    const correo = this.correo.trim();
    const contrasena = this.contrasena.trim();

    if (!correo || !contrasena) {
      this.error = 'Escribe tu correo y tu contrasena para continuar.';
      return;
    }

    this.enviando = true;
    this.error = '';

    this.authService.iniciarSesionConCorreo(correo, contrasena).subscribe({
      next: () => {
        this.enviando = false;
        void this.router.navigate(['/familia']);
      },
      error: (error) => {
        this.enviando = false;
        this.error = error?.error?.message ?? 'No fue posible iniciar sesion con correo y contrasena.';
      },
    });
  }
}
