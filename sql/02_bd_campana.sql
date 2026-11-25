-- Base de datos del microservicio de campanas
CREATE DATABASE IF NOT EXISTS bd_campana_dev
  DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE bd_campana_dev;

CREATE TABLE IF NOT EXISTS campana (
    id               BIGINT        NOT NULL AUTO_INCREMENT,
    nombre           VARCHAR(120)  NOT NULL,
    descripcion      VARCHAR(300)  NULL,
    meta_recaudacion DECIMAL(12,2) NOT NULL,
    fecha_inicio     DATE          NOT NULL,
    fecha_fin        DATE          NOT NULL,
    estado           VARCHAR(10)   NOT NULL DEFAULT 'ACTIVA',
    PRIMARY KEY (id),
    UNIQUE KEY uk_campana_nombre (nombre)
) ENGINE = InnoDB;

INSERT INTO campana (nombre, descripcion, meta_recaudacion, fecha_inicio, fecha_fin, estado) VALUES
  ('Navidad Solidaria 2026', 'Recoleccion de juguetes y alimentos para ninos de Santa Anita', 15000.00, '2026-11-01', '2026-12-24', 'ACTIVA'),
  ('Techo Digno',            'Materiales de construccion para familias afectadas por lluvias', 42000.00, '2026-09-15', '2026-12-31', 'ACTIVA'),
  ('Becas Tecsup',           'Fondo de becas para estudiantes de bajos recursos',              80000.00, '2026-03-01', '2026-10-31', 'CERRADA');
