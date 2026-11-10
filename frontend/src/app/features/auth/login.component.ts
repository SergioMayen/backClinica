import { AfterViewInit, Component, ElementRef, OnDestroy, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../core/auth/auth.service';
import { turnstileSiteKey } from '../../core/config';

type TurnstileApi = { render: (element: HTMLElement, options: Record<string, unknown>) => string; reset: (widgetId: string) => void };
declare global { interface Window { turnstile?: TurnstileApi; } }

@Component({ standalone: true, imports: [CommonModule, FormsModule], template: `<main class="login-page"><form class="login-card" (ngSubmit)="submit()"><div class="brand-mark">✚</div><p class="eyebrow">PORTAL DE ATENCIÓN</p><h1>Bienvenido</h1><p class="muted">Ingresa con tu cuenta de clínica.</p><label>Usuario<input name="username" [(ngModel)]="username" autocomplete="username" required></label><label>Contraseña<input name="password" [(ngModel)]="password" type="password" autocomplete="current-password" required></label><div #turnstileContainer class="turnstile"></div><p class="error" *ngIf="error">{{ error }}</p><button class="primary full" [disabled]="loading || !turnstileToken">{{ loading ? 'Verificando…' : 'Iniciar sesión' }}</button></form></main>` })
export class LoginComponent implements AfterViewInit, OnDestroy {
  @ViewChild('turnstileContainer') private turnstileContainer!: ElementRef<HTMLElement>;
  username = ''; password = ''; error = ''; loading = false;
  turnstileToken = '';
  private widgetId = '';
  private destroyed = false;
  constructor(private auth: AuthService, private router: Router, private route: ActivatedRoute) {}
  ngAfterViewInit(): void {
    if (!turnstileSiteKey) { this.error = 'La verificación de seguridad no está configurada.'; return; }
    const render = () => {
      if (this.destroyed || !window.turnstile) return;
      this.widgetId = window.turnstile.render(this.turnstileContainer.nativeElement, {
        sitekey: turnstileSiteKey,
        action: 'login',
        size: 'compact',
        callback: (token: string) => { this.turnstileToken = token; this.error = ''; },
        'expired-callback': () => { this.turnstileToken = ''; },
        'error-callback': () => { this.turnstileToken = ''; this.error = 'No se pudo completar la verificación de seguridad. Recarga la página e inténtalo de nuevo.'; }
      });
    };
    if (window.turnstile) { render(); return; }
    const existing = document.querySelector<HTMLScriptElement>('script[data-turnstile]');
    const script = existing ?? document.createElement('script');
    if (!existing) {
      script.src = 'https://challenges.cloudflare.com/turnstile/v0/api.js?render=explicit';
      script.async = true; script.defer = true; script.dataset['turnstile'] = 'true';
      document.head.appendChild(script);
    }
    script.addEventListener('load', render, { once: true });
    if (existing && window.turnstile) render();
  }
  ngOnDestroy(): void { this.destroyed = true; }
  submit(): void {
    if (!this.turnstileToken) { this.error = 'Completa la verificación de seguridad.'; return; }
    this.loading = true; this.error = '';
    const token = this.turnstileToken;
    this.auth.login(this.username, this.password, token).subscribe({
      next: () => { const returnUrl = this.route.snapshot.queryParamMap.get('returnUrl') || '/pacientes'; void this.router.navigateByUrl(returnUrl); },
      error: () => {
        this.error = 'Usuario o contraseña incorrectos, o servicio no disponible.';
        this.loading = false;
        this.turnstileToken = '';
        if (this.widgetId && window.turnstile) window.turnstile.reset(this.widgetId);
      }
    });
  }
}
