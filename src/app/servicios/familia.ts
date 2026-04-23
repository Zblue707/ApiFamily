import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from './api-config';

export interface Integrante {
  idIntegrante: number;
  nombre: string;
  apellido: string;
  fechaNacimiento: string;
  foto: string | null;
  telefono: string | null;
  direccion: string | null;
  parentesco: string | null;
  sexo: string | null;
  nacionalidad: string | null;
  correo: string | null;
  fechaRegistro: string | null;
}

@Injectable({
  providedIn: 'root',
})
export class ServFamilia {
  private readonly apiUrl = `${API_BASE_URL}/integrantes`;

  constructor(private readonly http: HttpClient) {}

  consultarFamilia(): Observable<Integrante[]> {
    return this.http.get<Integrante[]>(this.apiUrl, {
      withCredentials: true,
    });
  }
}
