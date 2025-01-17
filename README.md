# Biblioteca App

Esta es una aplicación de gestión de bibliotecas desarrollada en Android utilizando **Kotlin**, **Jetpack Compose** y **Room**. La aplicación permite registrar, listar, editar y eliminar libros, autores y miembros de una biblioteca, así como gestionar los préstamos de libros. 

## Funcionalidades

- **Registro de libros**: Añadir nuevos libros especificando título, género y asociar autores.
- **Gestión de autores**: Listar autores.
- **Registro de miembros**: Añadir miembros a la biblioteca para poder gestionar los préstamos de libros.
- **Préstamos de libros**: Los miembros pueden solicitar préstamos de libros registrados.
- **Editar y eliminar libros**: Editar o eliminar información de los libros registrados.
- **Visualización de libros con autores**: Mostrar el listado de libros junto con el nombre del autor en la interfaz.

## Modelo de base de datos relacional

Este diagrama es un modelo de base de datos relacional que representa una biblioteca con cuatro tablas: **libros**, **autores**, **miembros** y **préstamos**. A continuación, se detallan las relaciones entre ellas:

### 1. Tabla `libros`:
Contiene información sobre los libros, con los siguientes campos:
- `libro_id`: Identificador único para cada libro.
- `titulo`: Título del libro.
- `genero`: Género del libro.
- `autor_id`: Relación con la tabla autores (clave externa).

### 2. Tabla `autores`:
Almacena información sobre los autores, con los siguientes campos:
- `autor_id`: Identificador único para cada autor.
- `nombre`: Nombre del autor.
- `apellido`: Apellido del autor.
- `nacionalidad`: Nacionalidad del autor.

**Relación con libros**:
- Un autor puede tener muchos libros (1:N), lo que está representado por la clave externa `autor_id` en la tabla `libros`.

### 3. Tabla `miembros`:
Almacena información sobre los miembros de la biblioteca, con los siguientes campos:
- `miembro_id`: Identificador único para cada miembro.
- `nombre`: Nombre del miembro.
- `apellido`: Apellido del miembro.
- `fecha_inscripcion`: Fecha de inscripción del miembro.

### 4. Tabla `prestamos`:
Representa los préstamos de libros, con los siguientes campos:
- `prestamo_id`: Identificador único del préstamo.
- `libro_id`: Clave externa que se relaciona con la tabla `libros`.
- `miembro_id`: Clave externa que se relaciona con la tabla `miembros`.
- `fecha_prestamo`: Fecha en la que se realizó el préstamo.
- `fecha_devolucion`: Fecha en la que se devolvió el libro.

**Relaciones**:
- **Con libros**: Un préstamo está asociado a un solo libro, pero un libro puede estar relacionado con varios préstamos a lo largo del tiempo (1:N).
- **Con miembros**: Un miembro puede realizar varios préstamos, pero un préstamo específico está relacionado con un solo miembro (1:N).

### Conclusión:
Las relaciones principales del modelo son de uno a muchos (1:N):
- Un autor puede tener muchos libros.
- Un libro puede tener muchos préstamos, pero cada préstamo está relacionado con un solo libro.
- Un miembro puede hacer muchos préstamos.

Este modelo es adecuado para gestionar un sistema de préstamos de libros en una biblioteca.
