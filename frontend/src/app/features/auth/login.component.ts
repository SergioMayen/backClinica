import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../core/auth/auth.service';
@Component({ standalone: true, imports: [CommonModule, FormsModule], template: `<main class="login-page"><form class="login-card" (ngSubmit)="submit()"><div class="brand-mark">✚</div><p class="eyebrow">PORTAL DE ATENCIÓN</p><h1>Bienvenido</h1><p class="muted">Ingresa con tu cuenta de clínica.</p><label>Usuario<input name="username" [(ngModel)]="username" autocomplete="username" required></label><label>Contraseña<input name="password" [(ngModel)]="password" type="password" autocomplete="current-password" required></label><p class="error" *ngIf="error">{{ error }}</p><button class="primary full" [disabled]="loading">{{ loading ? 'Verificando…' : 'Iniciar sesión' }}</button></form></main>` })
export class LoginComponent {
  username = ''; password = ''; error = ''; loading = false;
  constructor(private auth: AuthService, private router: Router, private route: ActivatedRoute) {}
  submit(): void { this.loading = true; this.error = ''; this.auth.login(this.username, this.password).subscribe({ next: () => { const returnUrl = this.route.snapshot.queryParamMap.get('returnUrl') || '/pacientes'; void this.router.navigateByUrl(returnUrl); }, error: () => { this.error = 'Usuario o contraseña incorrectos, o servicio no disponible.'; this.loading = false; } }); }
}
