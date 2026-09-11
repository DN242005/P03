package com.uv.inventario.web1;

import com.uv.inventario.dao.MovimientoDAO;
import com.uv.inventario.dao.MovimientoInvalidoException;
import com.uv.inventario.dao.ProductoDAO;
import com.uv.inventario.dao.UsuarioDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;


@WebServlet("/movimiento")
public class MovimientoServlet extends HttpServlet {

    private final MovimientoDAO movimientoDAO = new MovimientoDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("productos", productoDAO.listar());
            req.setAttribute("usuarios", usuarioDAO.listar());
            req.setAttribute("recientes", movimientoDAO.listarRecientes(10));
            req.getRequestDispatcher("/jsp/movimiento_form.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Error al preparar el formulario de movimiento", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String mensaje = null;
        String error = null;
        try {
            int idProducto = Integer.parseInt(req.getParameter("idProducto"));
            int idUsuario = Integer.parseInt(req.getParameter("idUsuario"));
            String tipo = req.getParameter("tipo");
            BigDecimal cantidad = new BigDecimal(req.getParameter("cantidad"));
            String comentario = req.getParameter("comentario");

            movimientoDAO.registrar(idProducto, idUsuario, tipo, cantidad, comentario);
            mensaje = "Movimiento registrado correctamente.";
        } catch (MovimientoInvalidoException e) {
            error = e.getMessage();
        } catch (NumberFormatException e) {
            error = "Verifica que la cantidad y los identificadores sean numeros validos.";
        } catch (SQLException e) {
            throw new ServletException("Error al registrar el movimiento", e);
        }

        try {
            req.setAttribute("mensaje", mensaje);
            req.setAttribute("error", error);
            req.setAttribute("productos", productoDAO.listar());
            req.setAttribute("usuarios", usuarioDAO.listar());
            req.setAttribute("recientes", movimientoDAO.listarRecientes(10));
            req.getRequestDispatcher("/jsp/movimiento_form.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Error al recargar el formulario de movimiento", e);
        }
    }
}
