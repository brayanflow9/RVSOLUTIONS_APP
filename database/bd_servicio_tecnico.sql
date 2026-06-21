-- ============================================================
-- RV SOLUTIONS - BASE DE DATOS V2
-- MySQL 8.0+
-- ============================================================
-- Criterio de identificación:
--   Usuarios     -> DNI visible y único
--   Clientes     -> DNI visible y único
--   Equipos      -> EQ-AAAA-000001
--   Órdenes      -> ORD-AAAA-000001
--   Diagnósticos -> DIAG-AAAA-000001
--   Pagos        -> PAG-AAAA-000001
--
-- Se conservan IDs numéricos internos para relaciones y rendimiento.
-- Los DNI y códigos son identificadores visibles con restricción UNIQUE.
-- ============================================================

DROP DATABASE IF EXISTS bd_servicio_tecnico;
CREATE DATABASE bd_servicio_tecnico
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
USE bd_servicio_tecnico;

-- ============================================================
-- 1. USUARIOS
-- ============================================================
CREATE TABLE usuarios (
    id_usuario INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    dni CHAR(8) NOT NULL,
    nombre VARCHAR(120) NOT NULL,
    usuario VARCHAR(50) NOT NULL,
    clave VARCHAR(255) NOT NULL,
    rol ENUM('Administrador', 'Tecnico') NOT NULL,
    correo VARCHAR(120) NULL,
    telefono VARCHAR(15) NULL,
    estado ENUM('Activo', 'Inactivo') NOT NULL DEFAULT 'Activo',
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uq_usuarios_dni UNIQUE (dni),
    CONSTRAINT uq_usuarios_usuario UNIQUE (usuario),
    CONSTRAINT uq_usuarios_correo UNIQUE (correo),
    CONSTRAINT chk_usuarios_dni CHECK (dni REGEXP '^[0-9]{8}$')
) ENGINE=InnoDB;

-- ============================================================
-- 2. CLIENTES
-- ============================================================
CREATE TABLE clientes (
    id_cliente INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    dni CHAR(8) NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    telefono VARCHAR(15) NULL,
    correo VARCHAR(120) NULL,
    direccion VARCHAR(180) NULL,
    estado ENUM('Activo', 'Inactivo') NOT NULL DEFAULT 'Activo',
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uq_clientes_dni UNIQUE (dni),
    CONSTRAINT uq_clientes_correo UNIQUE (correo),
    CONSTRAINT chk_clientes_dni CHECK (dni REGEXP '^[0-9]{8}$')
) ENGINE=InnoDB;

-- ============================================================
-- 3. EQUIPOS
-- ============================================================
CREATE TABLE equipos (
    id_equipo INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    codigo_equipo VARCHAR(20) NOT NULL,
    id_cliente INT UNSIGNED NOT NULL,
    tipo_equipo VARCHAR(80) NOT NULL,
    marca VARCHAR(80) NULL,
    modelo VARCHAR(80) NULL,
    serie VARCHAR(100) NULL,
    descripcion TEXT NULL,
    estado ENUM('Operativo', 'En revisión', 'Reparado', 'Entregado')
        NOT NULL DEFAULT 'En revisión',
    fecha_registro DATE NOT NULL DEFAULT (CURRENT_DATE),
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uq_equipos_codigo UNIQUE (codigo_equipo),
    CONSTRAINT uq_equipos_serie UNIQUE (serie),
    -- Esta clave permite validar que una orden no mezcle cliente y equipo.
    CONSTRAINT uq_equipos_id_cliente UNIQUE (id_equipo, id_cliente),
    CONSTRAINT fk_equipos_cliente
        FOREIGN KEY (id_cliente)
        REFERENCES clientes(id_cliente)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT chk_codigo_equipo
        CHECK (codigo_equipo REGEXP '^EQ-[0-9]{4}-[0-9]{6}$')
) ENGINE=InnoDB;

CREATE INDEX idx_equipos_cliente ON equipos(id_cliente);
CREATE INDEX idx_equipos_estado ON equipos(estado);

-- ============================================================
-- 4. ÓRDENES DE SERVICIO
-- ============================================================
CREATE TABLE ordenes_servicio (
    id_orden INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    codigo_orden VARCHAR(22) NOT NULL,
    id_cliente INT UNSIGNED NOT NULL,
    id_equipo INT UNSIGNED NOT NULL,
    id_tecnico INT UNSIGNED NOT NULL,
    fecha_registro DATE NOT NULL DEFAULT (CURRENT_DATE),
    problema_reportado TEXT NOT NULL,
    observaciones TEXT NULL,
    estado ENUM('Pendiente', 'En proceso', 'Finalizado', 'Entregado', 'Cancelado')
        NOT NULL DEFAULT 'Pendiente',
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uq_ordenes_codigo UNIQUE (codigo_orden),
    CONSTRAINT fk_orden_cliente
        FOREIGN KEY (id_cliente)
        REFERENCES clientes(id_cliente)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    -- FK compuesta: evita relacionar una orden con un equipo de otro cliente.
    CONSTRAINT fk_orden_equipo_cliente
        FOREIGN KEY (id_equipo, id_cliente)
        REFERENCES equipos(id_equipo, id_cliente)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_orden_tecnico
        FOREIGN KEY (id_tecnico)
        REFERENCES usuarios(id_usuario)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT chk_codigo_orden
        CHECK (codigo_orden REGEXP '^ORD-[0-9]{4}-[0-9]{6}$')
) ENGINE=InnoDB;

CREATE INDEX idx_ordenes_cliente ON ordenes_servicio(id_cliente);
CREATE INDEX idx_ordenes_equipo ON ordenes_servicio(id_equipo);
CREATE INDEX idx_ordenes_tecnico ON ordenes_servicio(id_tecnico);
CREATE INDEX idx_ordenes_estado ON ordenes_servicio(estado);
CREATE INDEX idx_ordenes_fecha ON ordenes_servicio(fecha_registro);

-- ============================================================
-- 5. DIAGNÓSTICOS
-- ============================================================
CREATE TABLE diagnosticos (
    id_diagnostico INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    codigo_diagnostico VARCHAR(23) NOT NULL,
    id_orden INT UNSIGNED NOT NULL,
    id_tecnico INT UNSIGNED NOT NULL,
    descripcion_diagnostico TEXT NOT NULL,
    solucion_aplicada TEXT NULL,
    repuestos_utilizados TEXT NULL,
    fecha_diagnostico DATE NOT NULL DEFAULT (CURRENT_DATE),
    estado ENUM('Pendiente', 'En proceso', 'Completado')
        NOT NULL DEFAULT 'Pendiente',
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uq_diagnosticos_codigo UNIQUE (codigo_diagnostico),
    CONSTRAINT fk_diagnostico_orden
        FOREIGN KEY (id_orden)
        REFERENCES ordenes_servicio(id_orden)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_diagnostico_tecnico
        FOREIGN KEY (id_tecnico)
        REFERENCES usuarios(id_usuario)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT chk_codigo_diagnostico
        CHECK (codigo_diagnostico REGEXP '^DIAG-[0-9]{4}-[0-9]{6}$')
) ENGINE=InnoDB;

CREATE INDEX idx_diagnosticos_orden ON diagnosticos(id_orden);
CREATE INDEX idx_diagnosticos_tecnico ON diagnosticos(id_tecnico);
CREATE INDEX idx_diagnosticos_estado ON diagnosticos(estado);

-- ============================================================
-- 6. PAGOS
-- ============================================================
CREATE TABLE pagos (
    id_pago INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    codigo_pago VARCHAR(22) NOT NULL,
    id_orden INT UNSIGNED NOT NULL,
    monto DECIMAL(12,2) NOT NULL,
    metodo_pago ENUM('Efectivo', 'Yape', 'Plin', 'Transferencia', 'Tarjeta')
        NOT NULL,
    numero_operacion VARCHAR(80) NULL,
    fecha_pago DATE NOT NULL DEFAULT (CURRENT_DATE),
    estado ENUM('Pendiente', 'Pagado', 'Anulado') NOT NULL DEFAULT 'Pendiente',
    observacion VARCHAR(250) NULL,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uq_pagos_codigo UNIQUE (codigo_pago),
    CONSTRAINT uq_pagos_numero_operacion UNIQUE (numero_operacion),
    CONSTRAINT fk_pago_orden
        FOREIGN KEY (id_orden)
        REFERENCES ordenes_servicio(id_orden)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT chk_pago_monto CHECK (monto > 0),
    CONSTRAINT chk_codigo_pago
        CHECK (codigo_pago REGEXP '^PAG-[0-9]{4}-[0-9]{6}$')
) ENGINE=InnoDB;

CREATE INDEX idx_pagos_orden ON pagos(id_orden);
CREATE INDEX idx_pagos_fecha ON pagos(fecha_pago);
CREATE INDEX idx_pagos_estado ON pagos(estado);

-- ============================================================
-- DATOS INICIALES DE PRUEBA
-- NOTA: las claves se dejan como texto solo para demostración académica.
-- En producción deben guardarse con hash BCrypt.
-- ============================================================
INSERT INTO usuarios
(dni, nombre, usuario, clave, rol, correo, telefono, estado)
VALUES
('45879632', 'Administrador Principal', 'admin', 'admin123',
 'Administrador', 'admin@rvsolutions.pe', '999111222', 'Activo'),
('74125896', 'Técnico Principal', 'tecnico', 'tecnico123',
 'Tecnico', 'tecnico@rvsolutions.pe', '999333444', 'Activo');

INSERT INTO clientes
(dni, nombres, apellidos, telefono, correo, direccion, estado)
VALUES
('12345678', 'Carlos', 'Ramírez Flores', '987654321',
 'carlos@gmail.com', 'Av. Los Olivos 123', 'Activo'),
('87654321', 'María', 'López Torres', '912345678',
 'maria@gmail.com', 'Jr. Las Flores 456', 'Activo'),
('70214563', 'Juan', 'Pérez Soto', '945678123',
 'juan@gmail.com', 'Av. Primavera 789', 'Activo');

INSERT INTO equipos
(codigo_equipo, id_cliente, tipo_equipo, marca, modelo, serie, descripcion, estado, fecha_registro)
VALUES
('EQ-2026-000001', 1, 'Laptop', 'HP', 'Pavilion 15', 'HP123456',
 'Laptop no enciende correctamente', 'En revisión', '2026-06-15'),
('EQ-2026-000002', 2, 'Impresora', 'Epson', 'L3150', 'EP987654',
 'Impresora no imprime a color', 'En revisión', '2026-06-15'),
('EQ-2026-000003', 3, 'PC', 'Lenovo', 'ThinkCentre', 'LN202604',
 'Equipo lento y con fallas de arranque', 'En revisión', '2026-06-15');

INSERT INTO ordenes_servicio
(codigo_orden, id_cliente, id_equipo, id_tecnico, fecha_registro, problema_reportado, estado)
VALUES
('ORD-2026-000001', 1, 1, 2, '2026-06-15',
 'El equipo no prende y presenta fallas en el cargador.', 'Pendiente'),
('ORD-2026-000002', 2, 2, 2, '2026-06-15',
 'La impresora no reconoce los cartuchos.', 'En proceso');

INSERT INTO diagnosticos
(codigo_diagnostico, id_orden, id_tecnico, descripcion_diagnostico,
 solucion_aplicada, repuestos_utilizados, fecha_diagnostico, estado)
VALUES
('DIAG-2026-000001', 1, 2,
 'Se detectó falla en el cargador y posible daño en la batería.',
 'Cambiar cargador y revisar batería.',
 'Cargador compatible HP', '2026-06-15', 'Completado');

INSERT INTO pagos
(codigo_pago, id_orden, monto, metodo_pago, numero_operacion, fecha_pago, estado, observacion)
VALUES
('PAG-2026-000001', 1, 120.00, 'Efectivo', NULL,
 '2026-06-15', 'Pagado', 'Pago inicial del servicio');

-- ============================================================
-- VISTAS PARA MOSTRAR IDENTIFICADORES VISIBLES
-- ============================================================
CREATE OR REPLACE VIEW vw_equipos_detalle AS
SELECT
    e.id_equipo,
    e.codigo_equipo,
    c.dni AS dni_cliente,
    CONCAT(c.nombres, ' ', c.apellidos) AS cliente,
    e.tipo_equipo,
    e.marca,
    e.modelo,
    e.serie,
    e.descripcion,
    e.estado,
    e.fecha_registro
FROM equipos e
INNER JOIN clientes c ON c.id_cliente = e.id_cliente;

CREATE OR REPLACE VIEW vw_ordenes_detalle AS
SELECT
    o.id_orden,
    o.codigo_orden,
    c.dni AS dni_cliente,
    CONCAT(c.nombres, ' ', c.apellidos) AS cliente,
    e.codigo_equipo,
    CONCAT(e.marca, ' ', e.modelo) AS equipo,
    u.dni AS dni_tecnico,
    u.nombre AS tecnico,
    o.fecha_registro,
    o.problema_reportado,
    o.estado
FROM ordenes_servicio o
INNER JOIN clientes c ON c.id_cliente = o.id_cliente
INNER JOIN equipos e ON e.id_equipo = o.id_equipo
INNER JOIN usuarios u ON u.id_usuario = o.id_tecnico;

-- ============================================================
-- CONSULTAS DE COMPROBACIÓN
-- ============================================================
SELECT * FROM usuarios;
SELECT * FROM clientes;
SELECT * FROM vw_equipos_detalle;
SELECT * FROM vw_ordenes_detalle;
SELECT * FROM diagnosticos;
SELECT * FROM pagos;
