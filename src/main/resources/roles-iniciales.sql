INSERT INTO rol (nombre, nivel_acceso, descripcion)
VALUES ('ADMIN', 'ADMIN', 'Administración de FixCampus'),
       ('USUARIO', 'USUARIO', 'Registro y seguimiento de incidencias')
ON CONFLICT (nombre) DO NOTHING;
