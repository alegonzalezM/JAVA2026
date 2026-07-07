# E-Commerce API & Store

Aplicación web de comercio electrónico desarrollada con **Spring Boot 3** en el backend y una interfaz dinámica con **JavaScript Vanilla** en el frontend. Incluye gestión de catálogos, carritos de compra dinámicos por sesión, un módulo CRUD completo para la administración de clientes y productos, y seguridad mediante Tokens JWT.

## 🚀 Tecnologías Utilizadas

* **Backend**: Java 17, Spring Boot 3, Spring Security, Spring Data JPA, Hibernate, Lombok, JWT (JJWT).
* **Base de Datos**: MySQL.
* **Frontend**: HTML5, CSS3, JavaScript (Vanilla ES6).

## 📦 Configuración del Proyecto

   La aplicación estará disponible en: `http://localhost:8081`
   Puede accederse desde `productos.html` (o `index.html`) 

## 🔀 Endpoints Principales (API)

### Productos  #####################
### 🛒 Productos
* `GET /productos` - Listar todos los productos del catálogo.
* `GET /productos/{id}` - Obtener los detalles de un producto por su ID.
* `POST /productos` - Agregar un nuevo producto al catálogo.
* `PUT /productos/{id}` - Modificar un producto existente por su ID.
* `DELETE /productos/{id}` - Eliminar un producto del sistema por su ID.
* `GET /categorias` - Listar todas las categorías del Enum.
* `GET /productos/categorias/{categoria}` - Listar productos de una categoría (Ej: `/productos/categorias/repuestos`).

### 🏷️Marcas  #####################
* `GET /marcas` - Listar todas las marcas del catálogo.
* `GET /marcas/{id}` - Mostrar los detalles de una marca por su ID.
* `POST /marcas` - Agregar una nueva marca al catálogo.
  *Ejemplo de cuerpo (JSON):*
  ```json
  {
    "nombre": "Specialized"
  }
  ```
* `PUT /marcas/{id}` - Modificar una marca existente por su ID.
  *Ejemplo de cuerpo (JSON):*
  ```json
  {
    "nombre": "Giyo"
  }
  ```
* `DELETE /marcas/{id}` - Eliminar una marca del sistema.

### 👥 Clientes #####################
* `GET /clientes` - Listar todos los clientes de la base de datos.
* `GET /clientes/{dni}` - Buscar un cliente específico utilizando su **DNI** como clave primaria numérica (`Integer`).
* `POST /clientes` - Registrar un nuevo cliente validando DNI, campos obligatorios y formato de email único.
* `PUT /clientes/{dni}` - Modificar los datos de contacto (nombre, apellido, email, teléfono) de un cliente existente.
Ej: /clientes/22222222
{
        "dni": 22222222,
        "nombre": "Juan",
        "apellido": "Perez",
        "email": "javier.p@email.com",
        "telefono": "1156147878"
    }
* `DELETE /clientes/{dni}` - Eliminar un cliente del sistema de forma permanente. 
 Ej : /clientes/22222222

### 🛍️ Carrito de Compras
* `GET /carritos` - Listar todos los carritos del sistema.
* `POST /carritos` - Crear un nuevo carrito dinámico vacío (asigna un ID autogenerado)
* `POST /carritos/{carritoId}/productos/{productoId}` - Añadir o incrementar un producto en el carrito. Resta stock en MySQL de forma inmediata y agrupa cantidades si el producto ya existía.
* `DELETE /carritos/{id}/vaciar` - Vaciar por completo las relaciones de la tabla intermedia para ese carrito y restablecer el stock original de los productos en la base de datos.

### 🔍 Búsqueda y Filtrado
* `GET /productos/buscar?nombre={texto}` - Buscar productos por coincidencia de nombre (JPQL personalizado). 
  *Ej: http://localhost:8081/productos/buscar?nombre=bici*
* `GET /productos/buscar?categoria={ENUM}` - Filtrar productos por categoría Enum de Java. 
  *Ej: http://localhost:8081/productos/buscar?categoria=REPUESTOS*
* `GET /productos/buscar?precioMax={cantidad}` - Filtrar productos con precio menor al ingresado (Query Methods). 
  *Ej: http://localhost:8081/productos/buscar?precioMax=150000*
* `GET /productos/buscar?marcaId={id}` - Filtrar productos que pertenecen a una marca por su ID. 
  *Ej: http://localhost:8081/productos/buscar?marcaId=3*
* `GET /clientes/{dni}` - Buscar un único cliente de la base de datos por su número de DNI. 
  *Ej: http://localhost:8081/clientes/34895612*

## 🔒 Seguridad

La aplicación cuenta con un filtro de seguridad `JwtFilter`. Los endpoints de consulta y manipulación del catálogo y carrito (`/productos`, `/carritos`, `/marcas`, `/admin`, `/clientes`, etc) se encuentran configurados como **públicos** para permitir el ingreso en producción/desarrollo, omitiendo la restricción del token 

### Manejo Eficiente de Enums para Categorías
* **Categoría como Enum**: El atributo `categoria` en la entidad `Producto` está implementado estrictamente como un **Enum de Java (`Categoria.java`)** con los valores fijos del negocio (`BICICLETAS`, `REPUESTOS`, `ACCESORIOS`, `HERRAMIENTAS`, 'VARIOS'). 
* Para solucionar problemas de validación, se implementó la anotación `@JsonCreator` en el modelo. Si se ingresa una categoría errónea por teclado desde el panel de administración, el sistema convierte la entrada a String, la procesa para asegurar que siempre llegue en mayúsculas, reconvierte al Enum correspondiente y, en caso de ingresar un texto inválido, fuerza un retorno controlado `null` que dispara un `CategoriaInexistenteException` manejado por la API.


## 📦 Configuración y Arquitectura del Proyecto

### Gestión de Datos de Prueba (Carga Inicial)
El sistema cuenta con un mecanismo automatizado mediante `CommandLineRunner` en la clase principal que inicializa las marcas, el catálogo de productos de bicicletas, un usuario de prueba y una lista de **10 clientes reales** si las tablas de la base de datos se encuentran vacías al arrancar. Las imágenes de los artículos se sirven localmente desde la ruta `static/imagenes/`.

### Persistencia y Optimización
* **Uso de Lombok**: La entidad `Producto` conserva intencionalmente sus getters, setters y `toString` manuales para control estricto de relaciones, mientras que todas las demás entidades del sistema (`Cliente`, `Carrito`, `Marca`, etc.) han sido actualizadas y optimizadas utilizando las anotaciones de Lombok.
* **Manejo Eficiente de Enums**: El atributo `Categoria` está diseñado como un `Enum`. Para solucionar problemas de validación y evitar fallos de deserialización (errores 404 o caídas de Jackson), se implementó la anotación `@JsonCreator` en el modelo. Si se ingresa una categoría errónea por teclado, el sistema la convierte a String, procesa la entrada para asegurar que siempre llegue en mayúsculas, reconvierte al Enum correspondiente y, en caso de fallar, fuerza un retorno controlado `null` que dispara un `CategoriaInexistenteException`.

### Gestión de Errores Centralizada
El backend implementa un controlador global de excepciones (`GlobalExceptionHandler`). Se han encauzado todos los errores generados por la aplicación (como `StockInsuficienteException`, `CarritoNoEncontradoException` o las excepciones de nombres de marcas duplicados), asegurando que el servidor devuelva respuestas semánticas en formato JSON comprensibles para el frontend, evitando la fuga de excepciones genéricas de Java.

### Token Estático de Desarrollo
La aplicación cuenta con un filtro de seguridad activo (`JwtFilter`). **Nota para la revisión del proyecto:** Con el fin de facilitar la evaluación inmediata del sistema sin necesidad de completar un flujo manual de registro o login en cada reinicio, se ha configurado un **Token JWT estático de desarrollo** en el archivo `app.js`. 


