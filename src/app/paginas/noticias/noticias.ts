import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ServNoticias } from '../../servicios/noticias';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-noticias',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './noticias.html',
  styleUrls: ['./noticias.css'],
})
export class Noticias implements OnInit {
  noticias: any[] = [];

  constructor(private readonly servNoticias: ServNoticias) {}

  ngOnInit(): void {
    this.servNoticias.consultarNoticias().subscribe({
      next: (data) => {
        this.noticias = data;
      },
      error: (error) => {
        console.error('Error cargando noticias', error);
      },
    });
  }
}
