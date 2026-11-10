export interface Paciente { id: number; nombre: string; apellido: string; dpi: string; telefono: string; correo: string; fechaNacimiento: string; activo: boolean; }
export type PacienteCreate = Omit<Paciente, 'id'>;
export interface PacientePage {
  data: Paciente[];
  metadata: { totalRecords: number; page: number; pageSize: number; totalPages: number; hasPreviousPage: boolean; hasNextPage: boolean };
}
export interface PacienteFilters { nombre: string; apellido: string; dpi: string; activo: string; }
