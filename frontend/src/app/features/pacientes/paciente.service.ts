import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Paciente, PacienteCreate, PacienteFilters, PacientePage } from './paciente.model';
import { apiUrl } from '../../core/config';
@Injectable({ providedIn: 'root' })
export class PacienteService {
  private readonly url = `${apiUrl}/pacientes`;
  constructor(private http: HttpClient) {}
  listar(page: number, size: number, filters: PacienteFilters): Observable<PacientePage> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (filters.nombre.trim()) params = params.set('nombre', filters.nombre.trim());
    if (filters.apellido.trim()) params = params.set('apellido', filters.apellido.trim());
    if (filters.dpi.trim()) params = params.set('dpi', filters.dpi.trim());
    if (filters.activo !== '') params = params.set('activo', filters.activo);
    return this.http.get<PacientePage>(this.url, { params });
  }
  crear(paciente: PacienteCreate): Observable<Paciente> { return this.http.post<Paciente>(this.url, paciente); }
  eliminar(id: number): Observable<void> { return this.http.delete<void>(`${this.url}/${id}`); }
}
