import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';
export const authGuard: CanActivateFn = (_route, state) => { const auth = inject(AuthService); return auth.token() ? true : inject(Router).createUrlTree(['/login'], { queryParams: { returnUrl: state.url } }); };
export const roleGuard: CanActivateFn = route => { const auth = inject(AuthService); return auth.hasRole(route.data['roles'] as string[] ?? []) ? true : inject(Router).createUrlTree(['/acceso-denegado']); };
