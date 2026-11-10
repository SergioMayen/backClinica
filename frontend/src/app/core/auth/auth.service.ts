import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { authUrl } from '../config';
export interface LoginResponse { accessToken: string; tokenType: string; expiresIn: number; }
export interface Session { username: string; roles: string[]; }
@Injectable({ providedIn: 'root' })
export class AuthService {
  readonly session = signal<Session | null>(this.readSession());
  constructor(private http: HttpClient, private router: Router) {}
  login(username: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${authUrl}/auth/login`, { username, password }).pipe(tap(response => {
      const roles = this.decodeRoles(response.accessToken);
      const session = { username, roles };
      localStorage.setItem('clinic_token', response.accessToken);
      localStorage.setItem('clinic_session', JSON.stringify(session));
      this.session.set(session);
    }));
  }
  token(): string | null { return localStorage.getItem('clinic_token'); }
  hasRole(allowed: string[] = []): boolean { const roles = this.session()?.roles ?? []; return allowed.length === 0 || allowed.some(role => roles.includes(role)); }
  logout(): void { localStorage.removeItem('clinic_token'); localStorage.removeItem('clinic_session'); this.session.set(null); void this.router.navigate(['/login']); }
  private decodeRoles(token: string): string[] {
    try { const payload = JSON.parse(atob(token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/'))) as { scope?: string; roles?: string[] }; return payload.roles ?? payload.scope?.split(' ').filter(Boolean) ?? []; } catch { return []; }
  }
  private readSession(): Session | null { try { const value = localStorage.getItem('clinic_session'); return value ? JSON.parse(value) as Session : null; } catch { return null; } }
}
