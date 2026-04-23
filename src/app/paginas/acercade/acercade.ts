import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ServAcercade } from '../../servicios/acerade';

@Component({
  selector: 'app-acercade',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './acercade.html',
  styleUrls: ['./acercade.css'],
})
export class Acercade implements OnInit {
  info: any[] = [];

  constructor(private readonly servicioAcercade: ServAcercade) {}

  ngOnInit(): void {
    this.servicioAcercade.consultarAcercade().subscribe({
      next: (data) => {
        this.info = data;
      },
      error: (error) => {
        console.error('Error cargando acerca de', error);
      },
    });
  }
}
