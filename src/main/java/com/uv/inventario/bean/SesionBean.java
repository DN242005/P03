package com.uv.inventario.bean;

import com.uv.inventario.dao.UsuarioDAO;
import com.uv.inventario.model.Usuario;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

@ManagedBean(name = "sesionBean")
@SessionScoped
public class SesionBean implements Serializable {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private List<Usuario> usuarios;
    private Integer idUsuarioSeleccionado;
    private Usuario usuarioActual;

    @PostConstruct
    public void init() {
        try {
            usuarios = usuarioDAO.listar();
        } catch (SQLException e) {
            usuarios = java.util.Collections.emptyList();
        }
    }

    public String iniciarSesion() {
        if (idUsuarioSeleccionado == null) {
            mensaje(FacesMessage.SEVERITY_ERROR, "Selecciona un usuario para iniciar la sesion.");
            return null;
        }
        try {
            Usuario candidato = usuarioDAO.buscarPorId(idUsuarioSeleccionado);
            if (candidato == null || !candidato.isActivo()) {
                mensaje(FacesMessage.SEVERITY_ERROR, "El usuario no existe o esta inactivo.");
                return null;
            }
            usuarioActual = candidato;
            return "productos?faces-redirect=true";
        } catch (SQLException e) {
            mensaje(FacesMessage.SEVERITY_ERROR, "No fue posible validar el usuario.");
            return null;
        }
    }

    public String cerrarSesion() {
        usuarioActual = null;
        idUsuarioSeleccionado = null;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/faces/login.xhtml?faces-redirect=true";
    }

    public boolean isAutenticado() {
        return usuarioActual != null;
    }

    public boolean isPuedeRegistrarMovimientos() {
        return usuarioDAO.puedeRegistrarMovimientos(usuarioActual);
    }

    private void mensaje(FacesMessage.Severity severidad, String texto) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severidad, texto, null));
    }

    public List<Usuario> getUsuarios() { return usuarios; }
    public Integer getIdUsuarioSeleccionado() { return idUsuarioSeleccionado; }
    public void setIdUsuarioSeleccionado(Integer idUsuarioSeleccionado) { this.idUsuarioSeleccionado = idUsuarioSeleccionado; }
    public Usuario getUsuarioActual() { return usuarioActual; }
}