package com.uv.inventario.web1;

import com.uv.inventario.dao.CategoriaDAO;
import com.uv.inventario.dao.ProductoDAO;
import com.uv.inventario.model.Producto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

/** Web 1.0: alta de productos en el catalogo mediante formulario HTML clasico. */
@WebServlet("/producto")
public class ProductoServlet extends HttpServlet {

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("categorias", categoriaDAO.listar());
            req.getRequestDispatcher("/jsp/producto_form.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Error al preparar el formulario de producto", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            Producto p = new Producto();
            p.setCodigo(req.getParameter("codigo").trim());
            p.setNombre(req.getParameter("nombre").trim());
            p.setIdCategoria(Integer.valueOf(req.getParameter("idCategoria")));
            p.setUnidadMedida(req.getParameter("unidadMedida").trim());
            p.setMinimoPermitido(new BigDecimal(req.getParameter("minimoPermitido")));
            p.setExistencia(BigDecimal.ZERO);

            if (p.getCodigo().isEmpty() || p.getNombre().isEmpty()) {
                req.setAttribute("error", "El codigo y el nombre son obligatorios.");
                req.setAttribute("categorias", categoriaDAO.listar());
                req.getRequestDispatcher("/jsp/producto_form.jsp").forward(req, resp);
                return;
            }

            productoDAO.guardar(p);
            resp.sendRedirect(req.getContextPath() + "/catalogo");
        } catch (NumberFormatException e) {
            req.setAttribute("error", "El minimo permitido debe ser un numero valido.");
            try {
                req.setAttribute("categorias", categoriaDAO.listar());
            } catch (SQLException ignored) {}
            req.getRequestDispatcher("/jsp/producto_form.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Error al guardar el producto", e);
        }
    }
}
