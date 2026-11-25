-- Base de datos del microservicio de donantes
CREATE DATABASE IF NOT EXISTS bd_donante_dev
  DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE bd_donante_dev;

CREATE TABLE IF NOT EXISTS donante (
    id        BIGINT       NOT NULL AUTO_INCREMENT,
    nombres   VARCHAR(80)  NOT NULL,
    apellidos VARCHAR(80)  NOT NULL,
    dni       VARCHAR(8)   NOT NULL,
    email     VARCHAR(120) NOT NULL,
    telefono  VARCHAR(15)  NULL,
    activo    BIT(1)       NOT NULL DEFAULT b'1',
    PRIMARY KEY (id),
    UNIQUE KEY uk_donante_dni (dni)
) ENGINE = InnoDB;

INSERT INTO donante (nombres, apellidos, dni, email, telefono, activo) VALUES
  ('Eder Marcelo', 'Yaycate Bardales', '72345678', 'eder.yaycate@tecsup.edu.pe', '987654321', b'1'),
  ('Lucia',        'Ramos Vega',       '71234567', 'lucia.ramos@correo.com',     '961234567', b'1'),
  ('Carlos',       'Paredes Soto',     '70123456', 'carlos.paredes@correo.com',  '955112233', b'1'),
  ('Ana Maria',    'Flores Quispe',    '73456789', 'ana.flores@correo.com',      NULL,        b'1');
