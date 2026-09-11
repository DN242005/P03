package com.uv.inventario.dao;

/** Se lanza cuando una regla de negocio del movimiento no se cumple (ej. salida mayor a la existencia). */
public class MovimientoInvalidoException extends Exception {
    public MovimientoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
