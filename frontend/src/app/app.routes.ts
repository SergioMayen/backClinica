import { Routes } from '@angular/router';
import { authGuard, roleGuard } from './core/guards/auth.guard';
import { ShellComponent } from './layout/shell.component';
export const routes: Routes = [
  { path: 'login', loadComponent: () => import('./features/auth/login.component').then(m => m.LoginComponent) },
  { path: 'acceso-denegado', loadComponent: () => import('./shared/denied.component').then(m => m.DeniedComponent) },
  { path: '', component: ShellComponent, canActivate: [authGuard], children: [
    { path: '', pathMatch: 'full', redirectTo: 'pacientes' },
    { path: 'pacientes', canActivate: [roleGuard], data: { roles: ['ROLE_USER', 'ROLE_ADMIN'] }, loadComponent: () => import('./features/pacientes/pacientes.component').then(m => m.PacientesComponent) },
    { path: 'administracion', canActivate: [roleGuard], data: { roles: ['ROLE_ADMIN'] }, loadComponent: () => import('./features/pacientes/pacientes.component').then(m => m.PacientesComponent) }
  ] }, { path: '**', redirectTo: 'pacientes' }
];
