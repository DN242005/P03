package com.uv.inventario.dao;

import com.uv.inventario.model.Categoria;
import com.uv.inventario.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public List<Categoria> listar() throws SQLException {
        String sql = "SELECT id_categoria, nombre, descripcion FROM categoria ORDER BY nombre";
        List<Categoria> resultado = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultado.add(new Categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")));
            }
        }
        return resultado;
    }
}
