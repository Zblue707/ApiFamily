import { CommonModule } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { Navegacion } from './componentes/navegacion/navegacion';    
import { PieDePagina } from './componentes/pie-de-pagina/pie-de-pagina';
import { Titulo } from './componentes/titulo/titulo';
import { filter } from 'rxjs';


@Component({
  selector: 'app-root',
  imports: [CommonModule, RouterOutlet, Navegacion, PieDePagina, Titulo],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  private readonly router = inject(Router);
  protected readonly title = signal('MendozaMartinez');
  protected readonly currentUrl = signal(this.router.url);
  protected readonly mostrarLayout = computed(
    () => this.currentUrl() !== '/inicio-sesion' && this.currentUrl() !== '/login',
  );

  constructor() {
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe((event) => {
        this.currentUrl.set((event as NavigationEnd).urlAfterRedirects);
      });
  }
}

