# Modelo Entidad-Relación final - FixCampus

## Entidades

```mermaid
erDiagram
    ROL ||--o{ USUARIO : tiene
    USUARIO ||--o{ REPORTE : registra
    USUARIO ||--o{ REPORTE : atiende
    CATEGORIA ||--o{ REPORTE : clasifica
    UBICACION ||--o{ REPORTE : contiene
    REPORTE ||--o{ ADJUNTO : incluye
    REPORTE ||--o{ COMENTARIO : recibe
    USUARIO ||--o{ COMENTARIO : escribe

    ROL {
        int id_rol PK
        varchar nombre
        varchar nivel_acceso
        varchar descripcion
    }

    USUARIO {
        int id_usuario PK
        int id_rol FK
        varchar nombre
        varchar apellido
        varchar correo UK
        varchar contrasena_hash
        varchar estado
        timestamp fecha_registro
    }

    CATEGORIA {
        int id_categoria PK
        varchar nombre UK
        varchar descripcion
    }

    UBICACION {
        int id_ubicacion PK
        varchar campus
        varchar edificio
        int piso
        varchar zona
        varchar tipo
    }

    REPORTE {
        int id_reporte PK
        int id_usuario_reportante FK
        int id_tecnico_asignado FK
        int id_categoria FK
        int id_ubicacion FK
        varchar titulo
        text descripcion
        varchar detalle_ubicacion
        varchar prioridad
        varchar estado
        timestamp fecha_creacion
        timestamp fecha_asignacion
        timestamp fecha_resolucion
    }

    ADJUNTO {
        int id_adjunto PK
        int id_reporte FK
        varchar nombre_archivo
        varchar url_archivo
        varchar tipo_archivo
        timestamp fecha_subida
    }

    COMENTARIO {
        int id_comentario PK
        int id_reporte FK
        int id_usuario FK
        text texto_comentario
        timestamp fecha_comentario
    }
```

## Decisiones de corrección

- `COMENTARIO` pertenece a un `REPORTE` y lo escribe un `USUARIO`.
- Un reporte puede tener muchos comentarios.
- Un usuario puede escribir muchos comentarios.
- Un reporte puede tener varios adjuntos.
- `REPORTE.id_tecnico_asignado` referencia a `USUARIO.id_usuario`; el rol del usuario determina si puede atender reportes.
- Las claves foráneas mantienen la trazabilidad entre usuario, categoría, ubicación, reporte, adjuntos y comentarios.

## Flujo representado

Un usuario registra un reporte indicando su categoría y ubicación. El reporte puede ser asignado a otro usuario con rol técnico, recibir adjuntos y registrar comentarios durante su atención.
