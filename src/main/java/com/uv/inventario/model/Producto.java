package com.uv.inventario.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Producto implements Serializable {
    private Integer idProducto;
    private String codigo;
    private String nombre;
    private Integer idCategoria;
    private String nombreCategoria; // para mostrar en tablas sin join manual
    private String unidadMedida;
    private BigDecimal minimoPermitido = BigDecimal.ZERO;
    private BigDecimal existencia = BigDecimal.ZERO;
    private boolean activo = true;

    public Producto() {}

    // RF05 / RF06: alerta cuando la existencia cae por debajo del minimo permitido
    public boolean isAlertaStockBajo() {
        if (existencia == null || minimoPermitido == null) return false;
        return existencia.compareTo(minimoPermitido) < 0;
    }

    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Integer getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Integer idCategoria) { this.idCategoria = idCategoria; }
    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }
    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }
    public BigDecimal getMinimoPermitido() { return minimoPermitido; }
    public void setMinimoPermitido(BigDecimal minimoPermitido) { this.minimoPermitido = minimoPermitido; }
    public BigDecimal getExistencia() { return existencia; }
    public void setExistencia(BigDecimal existencia) { this.existencia = existencia; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
