# Store Backend

Propósito: este backend forma parte de un proyecto didáctico para aprender sobre **arquitectura hexagonal** y ver una implementación real que incluye backend y frontend. Puedes ejecutar y explorar el código para entender cómo se comunican las distintas capas.

Nota: este backend incluye configuración para DevContainers. Puedes abrir `back/store` directamente en Visual Studio Code usando Remote - Containers / DevContainers y ejecutar la aplicación en un entorno reproducible.

Si quieres levantar solo el frontend durante el desarrollo (por ejemplo para probar la UI mientras ejecutas el backend manualmente), en la raíz del workspace existe `docker-compose-only-front.yml` que arranca el contenedor del frontend.

Este proyecto es el backend de una solución para la gestión y actualización en tiempo real de productos y precios en tiendas físicas, funcionando como un escaparate virtual o catálogo digital. Permite a los usuarios autenticados crear, editar y eliminar productos, y sincroniza los cambios en tiempo real con todos los clientes conectados mediante WebSocket.

## Historia y objetivo

Este backend forma parte de una solución para crear un **escaparate virtual** o catálogo digital, pensado para ser usado como mostrador en tiendas físicas. El objetivo es que los usuarios con rol **SHOP** puedan visualizar en tiempo real los artículos y precios disponibles, mientras que los usuarios con rol **ADMIN** gestionan el catálogo: pueden crear, editar o eliminar productos.

La información se sincroniza en tiempo real mediante WebSocket, de modo que cualquier cambio realizado por un ADMIN (como la edición o borrado de un producto) se refleja instantáneamente en todos los dispositivos conectados. Además, cuando un ADMIN edita un producto, este queda bloqueado para el resto de administradores, evitando ediciones simultáneas y garantizando la integridad de los datos.

Así, el backend permite mantener un catálogo virtual siempre actualizado, facilitando la consulta de productos y precios vigentes a los empleados de tienda (SHOP) y la gestión eficiente y segura a los responsables (ADMIN).

## Tecnologías principales

-   Java 17+
-   Spring Boot
-   Spring Security (JWT)
-   Spring WebSocket (STOMP/SockJS)
-   JPA/Hibernate
-   PostgreSQL (configurado por defecto)

## Características

-   Autenticación con JWT
-   Gestión de productos (CRUD)
-   Actualización y bloqueo de productos en tiempo real vía WebSocket
-   Control de acceso por roles (ADMIN, SHOP)
-   Configuración de CORS personalizable

## Estructura principal

-   `/src/main/java/com/igarrido/store/` — Código fuente principal
-   `/src/main/resources/application.properties` — Configuración de la aplicación backend
-   `/src/main/resources/db/migration/` — Migraciones de base de datos, ddl para crear tu base de datos

## Configuración rápida

1. **Clona el repositorio y entra en la carpeta**

    ```bash
    git clone <repo-url>
    cd back/store
    ```

2. **Configura los orígenes permitidos para CORS**
   Edita `src/main/resources/application.properties`:

    ```properties
    cors.allowed-origins=http://localhost:5173,http://<tu-ip>:5173
    ```

3. **Compila y ejecuta**

    ```bash
    ./gradlew bootRun
    ```

4. **La API REST estará disponible en** `http://localhost:8081/api`, `http://localhost:8081/swagger-ui/index.htm`

5. **WebSocket endpoint:**
    - URL: `ws://localhost:8081/ws`
    - STOMP topics: `/topic/products/locks`, `/topic/products/updates`

---

## Herramientas de desarrollo

Durante el desarrollo se han utilizado las siguientes herramientas:

-   IntelliJ IDEA
-   Visual Studio Code
-   Google Chrome
-   Docker
-   PostgreSQL
-   DBeaver

---

## DevContainer: ejecutar solo el backend

El backend forma parte de un DevContainer configurado en `.devcontainer/devcontainer.json` que, por defecto, conecta el servicio `app` junto con otros servicios. Si quieres abrir el DevContainer y centrarte únicamente en el desarrollo del backend, edita `.devcontainer/devcontainer.json` y asegúrate de que la propiedad `service` esté establecida en:

```jsonc
"service": "app",
```

Con esto Visual Studio Code usará el contenedor del servicio `app` como entorno de desarrollo. Para levantar servicios adicionales (por ejemplo la base de datos `db`), ajusta `runServices` o modifica `docker-compose.yml` según necesites.

## Endpoints principales

-   `POST /api/auth/login` — Login y obtención de JWT
-   `GET /api/products` — Listar productos
-   `POST /api/products` — Crear producto
-   `PUT /api/products/{id}` — Actualizar producto
-   `DELETE /api/products/{id}` — Eliminar producto

## WebSocket

-   Los cambios y bloqueos de productos se notifican en tiempo real a todos los clientes conectados.
-   Los usuarios pueden ver si un producto está siendo editado por otro usuario.

## Seguridad

-   Todos los endpoints requieren autenticación JWT.
-   Los roles determinan permisos de edición o solo visualización.

-   Puedes cambiar los puertos y orígenes CORS en `application.properties`.
-   La base de datos utilizada es PostgreSQL; puedes ajustar la configuración de conexión en ese archivo si lo necesitas.

## Contacto

Para dudas o soporte, contacta con el equipo de desarrollo.
