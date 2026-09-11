package com.uv.inventario.bean;

import com.uv.inventario.dao.CategoriaDAO;
import com.uv.inventario.dao.ProductoDAO;
import com.uv.inventario.model.Categoria;
import com.uv.inventario.model.Producto;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Web 2.0: bean respaldando la vista JSF/PrimeFaces productos.xhtml.
 * RF01 catalogo, RF05 minimo permitido, RF06 consulta y alerta.
 */
@ManagedBean(name = "productoBean")
@ViewScoped
public class ProductoBean implements Serializable {

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    private List<Producto> productos;
    private List<Categoria> categorias;
    private Producto nuevo = new Producto();
    private boolean soloAlerta = false;

    @javax.annotation.PostConstruct
    public void init() {
        cargarCategorias();
        cargarProductos();
    }

    private void cargarCategorias() {
        try {
            categorias = categoriaDAO.listar();
        } catch (SQLException e) {
            agregarError("No fue posible cargar las categorias.");
        }
    }

    public void cargarProductos() {
        try {
            productos = soloAlerta ? productoDAO.listarEnAlerta() : productoDAO.listar();
        } catch (SQLException e) {
            agregarError("No fue posible cargar el catalogo de productos.");
        }
    }

    public void alternarFiltroAlerta() {
        soloAlerta = !soloAlerta;
        cargarProductos();
    }

    public void guardarNuevo() {
        try {
            if (nuevo.getExistencia() == null) nuevo.setExistencia(BigDecimal.ZERO);
            productoDAO.guardar(nuevo);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Producto guardado", nuevo.getNombre()));
            nuevo = new Producto();
            cargarProductos();
        } catch (SQLException e) {
            agregarError("No fue posible guardar el producto: " + e.getMessage());
        }
    }

    private void agregarError(String texto) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, texto, null));
    }

    public List<Producto> getProductos() { return productos; }
    public List<Categoria> getCategorias() { return categorias; }
    public Producto getNuevo() { return nuevo; }
    public void setNuevo(Producto nuevo) { this.nuevo = nuevo; }
    public boolean isSoloAlerta() { return soloAlerta; }
    public void setSoloAlerta(boolean soloAlerta) { this.soloAlerta = soloAlerta; }
}
