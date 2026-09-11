package com.uv.inventario.web1;

import com.uv.inventario.dao.CategoriaDAO;
import com.uv.inventario.dao.ProductoDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Web 1.0: renderizado en servidor con JSP/Servlets.
 * RF01 (catalogo), RF06 (consulta y alerta): parametro ?alerta=1 filtra
 * solo los productos por debajo del minimo permitido.
 */
@WebServlet("/catalogo")
public class CatalogoServlet extends HttpServlet {

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            boolean soloAlerta = "1".equals(req.getParameter("alerta"));
            req.setAttribute("productos", soloAlerta ? productoDAO.listarEnAlerta() : productoDAO.listar());
            req.setAttribute("categorias", categoriaDAO.listar());
            req.setAttribute("soloAlerta", soloAlerta);
            req.getRequestDispatcher("/jsp/catalogo.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Error al consultar el catalogo de productos", e);
        }
    }
}
