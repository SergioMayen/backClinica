package com.example.backDocker.dto;

/** Optional search criteria and bounded, zero-based pagination for patients. */
public class PacienteFilter {
    private String nombre;
    private String apellido;
    private String dpi;
    private Boolean activo;
    private int page = 0;
    private int size = 50;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getDpi() { return dpi; }
    public void setDpi(String dpi) { this.dpi = dpi; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public void validar() {
        if (page < 0) throw new IllegalArgumentException("page debe ser mayor o igual a 0.");
        if (size != 50 && size != 100 && size != 200) {
            throw new IllegalArgumentException("size debe ser uno de estos valores: 50, 100 o 200.");
        }
    }
}
