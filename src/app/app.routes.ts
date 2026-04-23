import { Routes } from '@angular/router';
import { CFamilia } from './paginas/c-familia/c-familia';
import { Acercade } from './paginas/acercade/acercade';
import { Noticias } from './paginas/noticias/noticias';
import { Noticia } from './paginas/noticia/noticia';
import { InicioSesion } from './paginas/inicio-sesion/inicio-sesion';


export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: InicioSesion },
  { path: 'inicio-sesion', redirectTo: 'login', pathMatch: 'full' },
  { path: 'familia', component: CFamilia },
  { path: 'acercade', component: Acercade },
  { path: 'noticias', component: Noticias },
  { path: 'noticia/:id', component: Noticia },
  { path: '**', redirectTo: 'login' }
];
