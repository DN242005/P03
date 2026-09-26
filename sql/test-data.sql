-- Datos sinteticos adicionales para repetir el recorrido de P03.
-- Ejecutar despues de sql/schema.sql sobre pr01_inventario.
INSERT INTO producto (codigo, nombre, id_categoria, unidad_medida, minimo_permitido, existencia)
SELECT 'P03-TEST', 'Producto de prueba P03', c.id_categoria, 'pieza', 5, 10
FROM categoria c
WHERE c.nombre = 'Papeleria'
  AND NOT EXISTS (SELECT 1 FROM producto WHERE codigo = 'P03-TEST');