-- Base de datos del microservicio de donaciones
-- No hay clave foranea hacia donante ni campana: cada microservicio es dueno
-- de su propia base de datos y la relacion se resuelve por API (OpenFeign).
CREATE DATABASE IF NOT EXISTS bd_donacion_dev
  DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE bd_donacion_dev;

CREATE TABLE IF NOT EXISTS donacion (
    id             BIGINT        NOT NULL AUTO_INCREMENT,
    donante_id     BIGINT        NOT NULL,
    campana_id     BIGINT        NOT NULL,
    monto          DECIMAL(12,2) NOT NULL,
    fecha_donacion DATE          NOT NULL,
    metodo_pago    VARCHAR(20)   NOT NULL,
    PRIMARY KEY (id),
    KEY idx_donacion_campana (campana_id),
    KEY idx_donacion_donante (donante_id)
) ENGINE = InnoDB;

INSERT INTO donacion (donante_id, campana_id, monto, fecha_donacion, metodo_pago) VALUES
  (1, 1,  250.00, '2026-11-05', 'YAPE'),
  (2, 1,  500.00, '2026-11-08', 'TRANSFERENCIA'),
  (3, 2, 1200.50, '2026-10-20', 'TARJETA'),
  (1, 2,  300.00, '2026-11-12', 'EFECTIVO');
