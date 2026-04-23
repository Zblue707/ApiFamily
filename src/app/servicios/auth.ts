import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, map, Observable, of, tap } from 'rxjs';

import { API_BASE_URL } from './api-config';

export interface SesionUsuario {
  authenticated: boolean;
  id: number;
  googleId: string;
  nombre: string;
  email: string;
  fotoUrl: string;
  fechaRegistro?: string;
  ultimaSesion: string;
  logoutUrl?: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly apiUrl = API_BASE_URL;
  readonly usuario = signal<SesionUsuario | null>(null);
  readonly cargandoSesion = signal(false);

  constructor(private readonly http: HttpClient) {}

  verificarSesion(): Observable<SesionUsuario | null> {
    this.cargandoSesion.set(true);

    return this.http
      .get<Partial<SesionUsuario>>(`${this.apiUrl}/auth/status`, {
        withCredentials: true,
      })
      .pipe(
        map((respuesta) => (respuesta.authenticated ? (respuesta as SesionUsuario) : null)),
        tap((usuario) => {
          this.usuario.set(usuario);
          this.cargandoSesion.set(false);
        }),
        catchError(() => {
          this.usuario.set(null);
          this.cargandoSesion.set(false);
          return of(null);
        }),
      );
  }

  iniciarSesionConGoogle(): void {
    window.location.href = `${this.apiUrl}/auth/login/google`;
  }

  cerrarSesion(): Observable<void> {
    return this.http
      .post<void>(
        `${this.apiUrl}/auth/logout`,
        {},
        {
          withCredentials: true,
        },
      )
      .pipe(
        tap(() => {
          this.usuario.set(null);
        }),
      );
  }
}
