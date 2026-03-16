CREATE DATABASE IF NOT EXISTS MenuBD;
USE MenuBD;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS valoraciones;
DROP TABLE IF EXISTS menu_plato;
DROP TABLE IF EXISTS menu_diario;
DROP TABLE IF EXISTS plato;
DROP TABLE IF EXISTS restaurante;
DROP TABLE IF EXISTS usuario;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE IF NOT EXISTS usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    empresa VARCHAR(255) NOT NULL,
    `contraseña` VARCHAR(255) NOT NULL,
    es_empleado BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS restaurante (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    Direccion VARCHAR(255) NOT NULL,
    latitud DECIMAL(10, 2) NOT NULL,
    longitud DECIMAL(10, 2) NOT NULL,
    capacidad_maxima INT NOT NULL,
    id_usuario INT NULL,
    CONSTRAINT fk_restaurante_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id)
        ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS plato (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    categoria VARCHAR(255) NOT NULL,
    tipo_cocina VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS menu_diario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE NOT NULL,
    id_restaurante INT NOT NULL,
    url_imagen VARCHAR(255) NOT NULL,
    precio_menu DECIMAL(10, 2) NOT NULL,
    CONSTRAINT fk_menu_diario_restaurante
        FOREIGN KEY (id_restaurante) REFERENCES restaurante(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS menu_plato (
    id_menu_diario INT NOT NULL,
    id_plato INT NOT NULL,
    PRIMARY KEY (id_menu_diario, id_plato),
    CONSTRAINT fk_menu_plato_menu_diario
        FOREIGN KEY (id_menu_diario) REFERENCES menu_diario(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_menu_plato_plato
        FOREIGN KEY (id_plato) REFERENCES plato(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS valoraciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_restaurante INT NOT NULL,
    id_plato INT NOT NULL,
    puntuacion INT NOT NULL,
    comentario TEXT,
    CONSTRAINT fk_valoraciones_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id),
    CONSTRAINT fk_valoraciones_restaurante
        FOREIGN KEY (id_restaurante) REFERENCES restaurante(id),
    CONSTRAINT fk_valoraciones_plato
        FOREIGN KEY (id_plato) REFERENCES plato(id)
);

-- Puntos clave para evitar desajustes con las entidades JPA actuales:
-- 1) restaurante debe tener la columna id_usuario.
-- 2) valoraciones debe tener la columna id_plato.
-- 3) menu_diario debe tener precio_menu NOT NULL.
-- 4) la columna Direccion está con D mayúscula porque así está mapeada en la entidad Restaurante.

