package com.uv.inventario.dao;

import com.uv.inventario.model.Producto;
import com.uv.inventario.util.ConexionBD;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * RF01 Catalogo de productos, RF04 existencia calculada, RF05 minimo permitido,
 * RF06 consulta y alerta.
 */
public class ProductoDAO {

    private static final String SELECT_BASE =
        "SELECT p.id_producto, p.codigo, p.nombre, p.id_categoria, c.nombre AS nombre_categoria, " +
        "       p.unidad_medida, p.minimo_permitido, p.existencia, p.activo " +
        "  FROM producto p JOIN categoria c ON c.id_categoria = p.id_categoria ";

    public List<Producto> listar() throws SQLException {
        String sql = SELECT_BASE + " WHERE p.activo = TRUE ORDER BY p.nombre";
        List<Producto> resultado = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultado.add(mapear(rs));
            }
        }
        return resultado;
    }

    /** RF06: consulta filtrada de productos en alerta (existencia por debajo del minimo). */
    public List<Producto> listarEnAlerta() throws SQLException {
        String sql = SELECT_BASE + " WHERE p.activo = TRUE AND p.existencia < p.minimo_permitido ORDER BY p.nombre";
        List<Producto> resultado = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultado.add(mapear(rs));
            }
        }
        return resultado;
    }

    public Producto buscarPorId(int idProducto) throws SQLException {
        String sql = SELECT_BASE + " WHERE p.id_producto = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public void guardar(Producto p) throws SQLException {
        if (p.getIdProducto() == null) {
            insertar(p);
        } else {
            actualizar(p);
        }
    }

    private void insertar(Producto p) throws SQLException {
        String sql = "INSERT INTO producto (codigo, nombre, id_categoria, unidad_medida, minimo_permitido, existencia, activo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, TRUE) RETURNING id_producto";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setInt(3, p.getIdCategoria());
            ps.setString(4, p.getUnidadMedida());
            ps.setBigDecimal(5, p.getMinimoPermitido());
            ps.setBigDecimal(6, p.getExistencia() == null ? BigDecimal.ZERO : p.getExistencia());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) p.setIdProducto(rs.getInt(1));
            }
        }
    }

    private void actualizar(Producto p) throws SQLException {
        String sql = "UPDATE producto SET codigo = ?, nombre = ?, id_categoria = ?, unidad_medida = ?, minimo_permitido = ? " +
                     "WHERE id_producto = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setInt(3, p.getIdCategoria());
            ps.setString(4, p.getUnidadMedida());
            ps.setBigDecimal(5, p.getMinimoPermitido());
            ps.setInt(6, p.getIdProducto());
            ps.executeUpdate();
        }
    }

    private Producto mapear(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setIdProducto(rs.getInt("id_producto"));
        p.setCodigo(rs.getString("codigo"));
        p.setNombre(rs.getString("nombre"));
        p.setIdCategoria(rs.getInt("id_categoria"));
        p.setNombreCategoria(rs.getString("nombre_categoria"));
        p.setUnidadMedida(rs.getString("unidad_medida"));
        p.setMinimoPermitido(rs.getBigDecimal("minimo_permitido"));
        p.setExistencia(rs.getBigDecimal("existencia"));
        p.setActivo(rs.getBoolean("activo"));
        return p;
    }
}
