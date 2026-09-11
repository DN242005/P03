package com.uv.inventario.bean;

import com.uv.inventario.dao.MovimientoDAO;
import com.uv.inventario.dao.MovimientoInvalidoException;
import com.uv.inventario.dao.ProductoDAO;
import com.uv.inventario.dao.UsuarioDAO;
import com.uv.inventario.model.Movimiento;
import com.uv.inventario.model.Producto;
import com.uv.inventario.model.Usuario;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;


@ManagedBean(name = "movimientoBean")
@ViewScoped
public class MovimientoBean implements Serializable {

    private final MovimientoDAO movimientoDAO = new MovimientoDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    private List<Producto> productos;
    private List<Usuario> usuarios;
    private List<Movimiento> recientes;

    private Integer idProducto;
    private Integer idUsuario;
    private String tipo = "ENTRADA";
    private BigDecimal cantidad;
    private String comentario;

    @javax.annotation.PostConstruct
    public void init() {
        cargarListas();
    }

    private void cargarListas() {
        try {
            productos = productoDAO.listar();
            usuarios = usuarioDAO.listar();
            recientes = movimientoDAO.listarRecientes(10);
        } catch (SQLException e) {
            agregarMensaje(FacesMessage.SEVERITY_ERROR, "No fue posible cargar la informacion del formulario.");
        }
    }

    public void registrar() {
        try {
            movimientoDAO.registrar(idProducto, idUsuario, tipo, cantidad, comentario);
            agregarMensaje(FacesMessage.SEVERITY_INFO, "Movimiento registrado correctamente.");
            cantidad = null;
            comentario = null;
            cargarListas();
        } catch (MovimientoInvalidoException e) {
            agregarMensaje(FacesMessage.SEVERITY_ERROR, e.getMessage());
        } catch (SQLException e) {
            agregarMensaje(FacesMessage.SEVERITY_ERROR, "Error de base de datos al registrar el movimiento.");
        }
    }

    private void agregarMensaje(FacesMessage.Severity severidad, String texto) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severidad, texto, null));
    }

    public List<Producto> getProductos() { return productos; }
    public List<Usuario> getUsuarios() { return usuarios; }
    public List<Movimiento> getRecientes() { return recientes; }
    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public BigDecimal getCantidad() { return cantidad; }
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}
