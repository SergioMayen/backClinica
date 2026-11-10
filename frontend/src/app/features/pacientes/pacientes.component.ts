import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../core/auth/auth.service';
import { Paciente, PacienteCreate, PacienteFilters, PacientePage } from './paciente.model';
import { PacienteService } from './paciente.service';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <section class="page-heading">
      <div><p class="eyebrow">DIRECTORIO CLÍNICO</p><h1>Pacientes</h1><p class="muted">Busca, filtra y revisa pacientes por páginas.</p></div>
      <button class="primary" (click)="showForm = !showForm">{{ showForm ? 'Cerrar' : '+ Nuevo paciente' }}</button>
    </section>

    <form class="panel filters-panel" (ngSubmit)="buscar()">
      <label>Nombre<input name="filterNombre" [(ngModel)]="filters.nombre" placeholder="Ej. María"></label>
      <label>Apellido<input name="filterApellido" [(ngModel)]="filters.apellido" placeholder="Ej. López"></label>
      <label>DPI<input name="filterDpi" [(ngModel)]="filters.dpi" placeholder="Buscar DPI"></label>
      <label>Estado<select name="filterActivo" [(ngModel)]="filters.activo"><option value="">Todos</option><option value="true">Activo</option><option value="false">Inactivo</option></select></label>
      <div class="filter-actions"><button class="primary">Buscar</button><button class="secondary" type="button" (click)="limpiarFiltros()">Limpiar</button></div>
    </form>

    <form class="panel form-grid" *ngIf="showForm" (ngSubmit)="crear()">
      <label>Nombre<input name="nombre" [(ngModel)]="draft.nombre" required></label>
      <label>Apellido<input name="apellido" [(ngModel)]="draft.apellido" required></label>
      <label>DPI<input name="dpi" [(ngModel)]="draft.dpi" required></label>
      <label>Teléfono<input name="telefono" [(ngModel)]="draft.telefono"></label>
      <label>Correo<input name="correo" [(ngModel)]="draft.correo" type="email"></label>
      <label>Fecha de nacimiento<input name="fechaNacimiento" [(ngModel)]="draft.fechaNacimiento" type="date"></label>
      <button class="primary">Guardar paciente</button>
    </form>

    <div class="notice" *ngIf="error">{{ error }}</div>
    <div class="panel table-panel">
      <div class="table-top"><strong>{{ metadata.totalRecords }} pacientes encontrados</strong><button class="text-button" type="button" (click)="cargar()">Actualizar ↻</button></div>
      <div class="loading" *ngIf="loading">Consultando esta página en la API clínica…</div>
      <div class="empty" *ngIf="!loading && !error && !pacientes.length">No se encontraron pacientes con estos criterios.</div>
      <div class="table-scroll" *ngIf="pacientes.length">
        <table><thead><tr><th>Paciente</th><th>DPI</th><th>Contacto</th><th>Estado</th><th></th></tr></thead>
          <tbody><tr *ngFor="let p of pacientes">
            <td><strong>{{ p.nombre }} {{ p.apellido }}</strong><small>Paciente #{{ p.id }}</small></td><td>{{ p.dpi || '—' }}</td><td>{{ p.correo || p.telefono || '—' }}</td>
            <td><span class="badge" [class.inactive]="!p.activo">{{ p.activo ? 'Activo' : 'Inactivo' }}</span></td>
            <td><button class="delete" *ngIf="canManage" type="button" (click)="eliminar(p)">Eliminar</button></td>
          </tr></tbody>
        </table>
      </div>
      <div class="pagination">
        <div class="page-size">Registros por página
          <select name="pageSize" [ngModel]="pageSize" (ngModelChange)="cambiarTamano($event)"><option [ngValue]="50">50</option><option [ngValue]="100">100</option><option [ngValue]="200">200</option></select>
        </div>
        <div class="page-controls"><span>Página {{ metadata.page + 1 }} de {{ metadata.totalPages || 1 }}</span>
          <button type="button" class="secondary" [disabled]="loading || !metadata.hasPreviousPage" (click)="cambiarPagina(metadata.page - 1)">Anterior</button>
          <button type="button" class="secondary" [disabled]="loading || !metadata.hasNextPage" (click)="cambiarPagina(metadata.page + 1)">Siguiente</button>
        </div>
      </div>
    </div>`
})
export class PacientesComponent implements OnInit {
  pacientes: Paciente[] = [];
  metadata: PacientePage['metadata'] = { totalRecords: 0, page: 0, pageSize: 50, totalPages: 0, hasPreviousPage: false, hasNextPage: false };
  pageSize = 50;
  loading = true;
  error = '';
  showForm = false;
  canManage = false;
  filters: PacienteFilters = { nombre: '', apellido: '', dpi: '', activo: '' };
  draft: PacienteCreate = { nombre: '', apellido: '', dpi: '', telefono: '', correo: '', fechaNacimiento: '', activo: true };

  constructor(private service: PacienteService, auth: AuthService) { this.canManage = auth.hasRole(['ROLE_ADMIN']); }
  ngOnInit(): void { this.cargar(); }

  cargar(page = this.metadata.page): void {
    this.loading = true;
    this.error = '';
    this.service.listar(page, this.pageSize, this.filters).subscribe({
      next: response => { this.pacientes = response.data; this.metadata = response.metadata; this.loading = false; },
      error: () => { this.error = 'No fue posible consultar pacientes. Verifica la API, la base de datos y los parámetros de búsqueda.'; this.loading = false; }
    });
  }
  buscar(): void { this.cargar(0); }
  limpiarFiltros(): void { this.filters = { nombre: '', apellido: '', dpi: '', activo: '' }; this.cargar(0); }
  cambiarPagina(page: number): void { this.cargar(page); }
  cambiarTamano(size: number): void { this.pageSize = size; this.cargar(0); }
  crear(): void {
    this.service.crear(this.draft).subscribe({ next: () => {
      this.showForm = false;
      this.draft = { nombre: '', apellido: '', dpi: '', telefono: '', correo: '', fechaNacimiento: '', activo: true };
      this.cargar(0);
    }, error: () => this.error = 'No fue posible guardar el paciente. Revisa los campos y tus permisos.' });
  }
  eliminar(p: Paciente): void {
    if (confirm(`¿Eliminar a ${p.nombre} ${p.apellido}?`)) this.service.eliminar(p.id).subscribe({ next: () => this.cargar(), error: () => this.error = 'No fue posible eliminar el registro.' });
  }
}
