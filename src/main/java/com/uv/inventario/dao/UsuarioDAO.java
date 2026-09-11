package com.uv.inventario.dao;

import com.uv.inventario.model.Usuario;
import com.uv.inventario.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public List<Usuario> listar() throws SQLException {
        String sql = "SELECT id_usuario, nombre, correo, rol, activo FROM usuario WHERE activo = TRUE ORDER BY nombre";
        List<Usuario> resultado = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultado.add(new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("rol"),
                        rs.getBoolean("activo")));
            }
        }
        return resultado;
    }

    public Usuario buscarPorId(int idUsuario) throws SQLException {
        String sql = "SELECT id_usuario, nombre, correo, rol, activo FROM usuario WHERE id_usuario = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(rs.getInt("id_usuario"), rs.getString("nombre"),
                            rs.getString("correo"), rs.getString("rol"), rs.getBoolean("activo"));
                }
            }
        }
        return null;
    }

    /** RNF02: autorizacion por rol para operaciones que cambian estado (entradas/salidas). */
    public boolean puedeRegistrarMovimientos(Usuario usuario) {
        return usuario != null && usuario.isActivo()
                && ("ALMACEN".equals(usuario.getRol()) || "CAPTURA".equals(usuario.getRol()));
    }
}
