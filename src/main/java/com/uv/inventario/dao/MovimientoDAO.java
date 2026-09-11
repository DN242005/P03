package com.uv.inventario.dao;

import com.uv.inventario.model.Movimiento;
import com.uv.inventario.model.Usuario;
import com.uv.inventario.util.ConexionBD;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * RF02 entradas, RF03 salidas, RF04 existencia calculada.
 * Riesgo principal (ficha PR01): alteracion de existencias -> se controla con
 * autorizacion por rol (UsuarioDAO.puedeRegistrarMovimientos) y bitacora
 * inmutable (esta tabla nunca se actualiza ni se borra, solo se inserta).
 */
public class MovimientoDAO {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Registra un movimiento y ajusta la existencia del producto en una sola
     * transaccion. Prueba minima de la ficha: "no aceptar salidas mayores a
     * la existencia".
     */
    public Movimiento registrar(int idProducto, int idUsuario, String tipo, BigDecimal cantidad, String comentario)
            throws SQLException, MovimientoInvalidoException {

        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new MovimientoInvalidoException("La cantidad debe ser mayor que cero.");
        }
        if (!"ENTRADA".equals(tipo) && !"SALIDA".equals(tipo)) {
            throw new MovimientoInvalidoException("Tipo de movimiento invalido.");
        }

        Usuario usuario = usuarioDAO.buscarPorId(idUsuario);
        if (!usuarioDAO.puedeRegistrarMovimientos(usuario)) {
            throw new MovimientoInvalidoException("El usuario no tiene autorizacion para registrar movimientos.");
        }

        Connection con = null;
        try {
            con = ConexionBD.obtenerConexion();
            con.setAutoCommit(false);

            BigDecimal existenciaActual;
            try (PreparedStatement lock = con.prepareStatement(
                    "SELECT existencia FROM producto WHERE id_producto = ? FOR UPDATE")) {
                lock.setInt(1, idProducto);
                try (ResultSet rs = lock.executeQuery()) {
                    if (!rs.next()) {
                        throw new MovimientoInvalidoException("El producto no existe.");
                    }
                    existenciaActual = rs.getBigDecimal("existencia");
                }
            }

            BigDecimal nuevaExistencia = "ENTRADA".equals(tipo)
                    ? existenciaActual.add(cantidad)
                    : existenciaActual.subtract(cantidad);

            if (nuevaExistencia.compareTo(BigDecimal.ZERO) < 0) {
                con.rollback();
                throw new MovimientoInvalidoException(
                        "No se puede registrar una salida mayor a la existencia disponible (" + existenciaActual + ").");
            }

            try (PreparedStatement update = con.prepareStatement(
                    "UPDATE producto SET existencia = ? WHERE id_producto = ?")) {
                update.setBigDecimal(1, nuevaExistencia);
                update.setInt(2, idProducto);
                update.executeUpdate();
            }

            Movimiento m = new Movimiento();
            try (PreparedStatement insert = con.prepareStatement(
                    "INSERT INTO movimiento (id_producto, id_usuario, tipo, cantidad, comentario) " +
                    "VALUES (?, ?, ?, ?, ?) RETURNING id_movimiento, fecha_hora")) {
                insert.setInt(1, idProducto);
                insert.setInt(2, idUsuario);
                insert.setString(3, tipo);
                insert.setBigDecimal(4, cantidad);
                insert.setString(5, comentario);
                try (ResultSet rs = insert.executeQuery()) {
                    rs.next();
                    m.setIdMovimiento(rs.getInt("id_movimiento"));
                    m.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
                }
            }

            con.commit();

            m.setIdProducto(idProducto);
            m.setIdUsuario(idUsuario);
            m.setTipo(tipo);
            m.setCantidad(cantidad);
            m.setComentario(comentario);
            return m;

        } catch (SQLException | MovimientoInvalidoException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ignored) {}
            }
            throw e;
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (SQLException ignored) {}
            }
        }
    }

    public List<Movimiento> listarRecientes(int limite) throws SQLException {
        String sql = "SELECT m.id_movimiento, m.id_producto, p.nombre AS nombre_producto, " +
                     "       m.id_usuario, u.nombre AS nombre_usuario, m.tipo, m.cantidad, m.fecha_hora, m.comentario " +
                     "  FROM movimiento m " +
                     "  JOIN producto p ON p.id_producto = m.id_producto " +
                     "  JOIN usuario u ON u.id_usuario = m.id_usuario " +
                     " ORDER BY m.fecha_hora DESC LIMIT ?";
        List<Movimiento> resultado = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Movimiento m = new Movimiento();
                    m.setIdMovimiento(rs.getInt("id_movimiento"));
                    m.setIdProducto(rs.getInt("id_producto"));
                    m.setNombreProducto(rs.getString("nombre_producto"));
                    m.setIdUsuario(rs.getInt("id_usuario"));
                    m.setNombreUsuario(rs.getString("nombre_usuario"));
                    m.setTipo(rs.getString("tipo"));
                    m.setCantidad(rs.getBigDecimal("cantidad"));
                    m.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
                    m.setComentario(rs.getString("comentario"));
                    resultado.add(m);
                }
            }
        }
        return resultado;
    }
}
