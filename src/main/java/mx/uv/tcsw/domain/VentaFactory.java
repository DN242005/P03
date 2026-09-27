package mx.uv.tcsw.domain;

import java.util.List;

public final class VentaFactory {
    public static Venta nueva(String id, List<DetalleVenta> detalles) {
        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException("No se puede crear una venta sin detalles");
        }
        Venta venta = new Venta(id);
        for (DetalleVenta detalle : detalles) {
            venta.agregarPartida(detalle.getProducto(), detalle.getCantidad());
        }
        return venta;
    }
}