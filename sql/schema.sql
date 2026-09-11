
-- Ejecutar este archivo con psql desde cualquier base de datos existente,
-- por ejemplo: psql -U postgres -f sql/schema.sql
SELECT 'CREATE DATABASE pr01_inventario'
WHERE NOT EXISTS (
    SELECT FROM pg_database WHERE datname = 'pr01_inventario'
)\gexec

\connect pr01_inventario

DROP TABLE IF EXISTS lectura_sensor CASCADE;
DROP TABLE IF EXISTS movimiento CASCADE;
DROP TABLE IF EXISTS producto CASCADE;
DROP TABLE IF EXISTS categoria CASCADE;
DROP TABLE IF EXISTS usuario CASCADE;

CREATE TABLE categoria (
    id_categoria    SERIAL PRIMARY KEY,
    nombre          VARCHAR(80)  NOT NULL UNIQUE,
    descripcion     VARCHAR(255)
);

CREATE TABLE usuario (
    id_usuario      SERIAL PRIMARY KEY,
    nombre          VARCHAR(120) NOT NULL,
    correo          VARCHAR(150) NOT NULL UNIQUE,
    rol             VARCHAR(30)  NOT NULL CHECK (rol IN ('ALMACEN','CAPTURA','COORDINACION')),
    activo          BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE producto (
    id_producto     SERIAL PRIMARY KEY,
    codigo          VARCHAR(30)  NOT NULL UNIQUE,
    nombre          VARCHAR(120) NOT NULL,
    id_categoria    INTEGER      NOT NULL REFERENCES categoria(id_categoria),
    unidad_medida   VARCHAR(20)  NOT NULL DEFAULT 'pieza',
    minimo_permitido NUMERIC(12,2) NOT NULL DEFAULT 0 CHECK (minimo_permitido >= 0),
    existencia      NUMERIC(12,2) NOT NULL DEFAULT 0 CHECK (existencia >= 0),
    activo          BOOLEAN      NOT NULL DEFAULT TRUE
);
CREATE TABLE movimiento (
    id_movimiento   SERIAL PRIMARY KEY,
    id_producto     INTEGER NOT NULL REFERENCES producto(id_producto),
    id_usuario      INTEGER NOT NULL REFERENCES usuario(id_usuario),
    tipo            VARCHAR(10) NOT NULL CHECK (tipo IN ('ENTRADA','SALIDA')),
    cantidad        NUMERIC(12,2) NOT NULL CHECK (cantidad > 0),
    fecha_hora      TIMESTAMP NOT NULL DEFAULT NOW(),
    comentario      VARCHAR(255)
);

CREATE TABLE lectura_sensor (
    id_lectura      SERIAL PRIMARY KEY,
    id_producto     INTEGER NOT NULL REFERENCES producto(id_producto),
    variable        VARCHAR(40) NOT NULL,
    unidad          VARCHAR(20) NOT NULL,
    valor           NUMERIC(12,2) NOT NULL,
    instante        TIMESTAMP NOT NULL DEFAULT NOW(),
    simulado        BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_movimiento_producto ON movimiento(id_producto);
CREATE INDEX idx_producto_categoria ON producto(id_categoria);

-- Datos sinteticos de ejemplo
INSERT INTO categoria (nombre, descripcion) VALUES
 ('Papeleria', 'Insumos de oficina'),
 ('Limpieza', 'Insumos de limpieza'),
 ('Computo', 'Consumibles de computo');

INSERT INTO usuario (nombre, correo, rol) VALUES
 ('Ana Torres', 'ana.torres@example.com', 'ALMACEN'),
 ('Luis Perez', 'luis.perez@example.com', 'CAPTURA'),
 ('Coordinacion General', 'coordinacion@example.com', 'COORDINACION');

INSERT INTO producto (codigo, nombre, id_categoria, unidad_medida, minimo_permitido, existencia) VALUES
 ('PAP-001', 'Resma de papel carta', 1, 'paquete', 10, 25),
 ('LIM-001', 'Cloro 1L', 2, 'pieza', 5, 3),
 ('COM-001', 'Tinta impresora negra', 3, 'pieza', 4, 8);
