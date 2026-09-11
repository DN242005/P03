package com.uv.inventario.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * RNF02: las credenciales se leen de variables de entorno / propiedades del
 * sistema, nunca quedan escritas en el codigo fuente.
 *
 * Variables esperadas:
 *   DB_URL   (ej. jdbc:postgresql://localhost:5432/pr01_inventario)
 *   DB_USER
 *   DB_PASSWORD
 */
public final class ConexionBD {

    private ConexionBD() {}

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se encontro el driver de PostgreSQL", e);
        }
    }

    private static String valor(String variable, String porDefecto) {
        String v = System.getenv(variable);
        if (v == null || v.isBlank()) {
            v = System.getProperty(variable, porDefecto);
        }
        return v;
    }

    public static Connection obtenerConexion() throws SQLException {
        String url = valor("DB_URL", "jdbc:postgresql://localhost:5432/pr01_inventario");
        String user = valor("DB_USER", "postgres");
        String password = valor("DB_PASSWORD", "postgres");
        return DriverManager.getConnection(url, user, password);
    }
}
