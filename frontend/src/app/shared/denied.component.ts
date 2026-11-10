import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
@Component({ standalone: true, imports: [RouterLink], template: `<section class="denied panel"><span>403</span><h1>Acceso restringido</h1><p>Tu cuenta no tiene permisos para consultar esta página.</p><a routerLink="/pacientes">Volver al directorio</a></section>` })
export class DeniedComponent {}
