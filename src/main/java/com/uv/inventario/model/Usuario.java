package com.uv.inventario.model;

import java.io.Serializable;

public class Usuario implements Serializable {
    private Integer idUsuario;
    private String nombre;
    private String correo;
    private String rol; // ALMACEN, CAPTURA, COORDINACION
    private boolean activo = true;

    public Usuario() {}

    public Usuario(Integer idUsuario, String nombre, String correo, String rol, boolean activo) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
        this.activo = activo;
    }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
