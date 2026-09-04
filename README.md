# Flores del Valle

Sistema web para la gestión de la floristería **Flores del Valle**, desarrollado con Java, Spring Boot, Thymeleaf, Spring Data JPA y MySQL.

La aplicación permite consultar información relacionada con el inventario de flores, los pedidos de clientes, las entregas, la facturación y los informes administrativos.

## Descripción del proyecto

Flores del Valle necesita centralizar la información utilizada durante la operación diaria de la floristería. Para responder a esta necesidad, se desarrolló una aplicación web basada en el patrón arquitectónico MVC.

El sistema permite administrar y consultar:

- Inventario de flores.
- Cantidades disponibles.
- Precios de compra y venta.
- Pedidos realizados por los clientes.
- Fechas previstas de entrega.
- Presupuestos.
- Estados de los pedidos.
- Información básica de entregas.
- Facturación.
- Informes administrativos.

La aplicación utiliza una base de datos MySQL para almacenar la información y Thymeleaf para presentar dinámicamente los datos en las páginas web.

## Tecnologías utilizadas

| Tecnología | Uso dentro del proyecto |
|---|---|
| Java | Lenguaje principal de programación |
| Spring Boot | Creación y configuración de la aplicación web |
| Spring MVC | Organización de controladores, modelos y vistas |
| Spring Data JPA | Acceso y persistencia de información |
| Hibernate | Mapeo de las entidades Java a las tablas |
| Thymeleaf | Generación dinámica de las páginas HTML |
| MySQL | Almacenamiento de la información |
| HTML5 | Estructura de las vistas |
| CSS3 | Diseño visual de la aplicación |
| Maven | Administración de dependencias y compilación |
| Git | Control de versiones |
| GitHub | Alojamiento del repositorio |

## Arquitectura de la aplicación

El proyecto utiliza el patrón arquitectónico **Modelo-Vista-Controlador (MVC)**.

### Modelo

Representa la información administrada por la aplicación. Las entidades principales son:

- `Flor`
- `Pedido`
- `EstadoPedido`

Las entidades se encuentran en:

```text
src/main/java/com/floresdelvalle/floresdelvalle/model
```

### Vista

Las vistas muestran al usuario la información procesada por la aplicación. Fueron construidas con HTML, Thymeleaf y CSS.

Se encuentran en:

```text
src/main/resources/templates
```

Los estilos se encuentran en:

```text
src/main/resources/static/css
```

### Controlador

Los controladores reciben las solicitudes del navegador, consultan la información mediante los repositorios y envían los datos a las vistas.

Se encuentran en:

```text
src/main/java/com/floresdelvalle/floresdelvalle/controller
```

### Repositorio

Los repositorios permiten realizar operaciones y consultas sobre la base de datos mediante Spring Data JPA.

Se encuentran en:

```text
src/main/java/com/floresdelvalle/floresdelvalle/repository
```

El flujo general de la aplicación es:

```text
Usuario
   ↓
Controlador
   ↓
Repositorio
   ↓
Base de datos MySQL
   ↓
Modelo
   ↓
Vista Thymeleaf
   ↓
Usuario
```

## Funcionalidades implementadas

### Página de inicio

La página principal presenta una descripción general del sistema y accesos a los diferentes módulos.

También muestra indicadores calculados dinámicamente desde la base de datos:

- Cantidad de referencias disponibles.
- Cantidad de flores con stock bajo.
- Total de pedidos registrados.
- Cantidad de pedidos en curso.

### Inventario

El módulo de inventario consulta las flores almacenadas en MySQL y presenta:

- Tipo de flor.
- Color.
- Variedad.
- Cantidad disponible.
- Precio de compra.
- Precio de venta.
- Estado del inventario.

Una referencia se identifica con **stock bajo** cuando tiene diez unidades disponibles o menos.

### Pedidos

El módulo de pedidos permite consultar:

- Nombre del cliente.
- Dirección.
- Información de contacto.
- Tipo de arreglo floral.
- Ocasión.
- Fecha de entrega.
- Presupuesto.
- Estado del pedido.

Los estados disponibles son:

- En curso.
- Completado.
- Entregado.

La página también muestra indicadores con la cantidad total de pedidos y su distribución por estado.

### Entregas

Contiene la vista destinada a la programación y seguimiento de las entregas asociadas con los pedidos.

### Facturación

Contiene la vista destinada a consultar la información relacionada con facturas, pagos y valores asociados con los pedidos.

### Informes

Contiene la vista destinada a presentar indicadores relacionados con el inventario, las ventas y los pedidos.

## Estructura principal del proyecto

```text
flores-del-valle
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── floresdelvalle
│   │   │           └── floresdelvalle
│   │   │               ├── config
│   │   │               │   └── DatosIniciales.java
│   │   │               ├── controller
│   │   │               │   ├── EntregaController.java
│   │   │               │   ├── FacturacionController.java
│   │   │               │   ├── HomeController.java
│   │   │               │   ├── InformeController.java
│   │   │               │   ├── InventarioController.java
│   │   │               │   └── PedidoController.java
│   │   │               ├── model
│   │   │               │   ├── EstadoPedido.java
│   │   │               │   ├── Flor.java
│   │   │               │   └── Pedido.java
│   │   │               ├── repository
│   │   │               │   ├── FlorRepository.java
│   │   │               │   └── PedidoRepository.java
│   │   │               └── FloresDelValleApplication.java
│   │   └── resources
│   │       ├── static
│   │       │   └── css
│   │       │       └── styles.css
│   │       ├── templates
│   │       │   ├── fragments
│   │       │   │   └── navbar.html
│   │       │   ├── entregas.html
│   │       │   ├── facturacion.html
│   │       │   ├── index.html
│   │       │   ├── informes.html
│   │       │   ├── inventario.html
│   │       │   └── pedidos.html
│   │       └── application.properties
│   └── test
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

## Requisitos previos

Para ejecutar el proyecto es necesario tener instalado:

- Java 17 o una versión compatible con el proyecto.
- MySQL Server 8 o superior.
- MySQL Workbench, opcionalmente.
- Git.
- Visual Studio Code o cualquier IDE compatible con Java.
- Extensiones de Java y Spring Boot para Visual Studio Code.

## Configuración de la base de datos

### 1. Iniciar MySQL

Verificar que el servicio de MySQL se encuentre iniciado.

### 2. Crear la base de datos

Abrir MySQL Workbench o una terminal de MySQL y ejecuta:

```sql
CREATE DATABASE flores_del_valle_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```
### 3. Configurar la conexión

El archivo de configuración se encuentra en:

```text
src/main/resources/application.properties
```

La configuración debe tener una estructura similar a la siguiente:

```properties
spring.application.name=flores-del-valle

spring.datasource.url=jdbc:mysql://localhost:3306/flores_del_valle_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

spring.thymeleaf.cache=false
```

La contraseña no se almacena directamente en el repositorio. La aplicación la obtiene mediante la variable de entorno `DB_PASSWORD`.

Si el usuario configurado en MySQL no es `root`, se debe modificar esta propiedad:

```properties
spring.datasource.username=nombre_del_usuario
```

## Instalación y ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/LauGarciaAgudelo/flores-del-valle
```

Ingresa a la carpeta del proyecto:

```bash
cd flores-del-valle
```

### 2. Configurar la contraseña de MySQL

En PowerShell:

```powershell
$env:DB_PASSWORD="TU_CONTRASEÑA"
```

La variable permanece disponible únicamente en la terminal actual. Si se abre una terminal nueva, es necesario configurarla nuevamente.

### 3. Limpiar y compilar el proyecto

En Windows:

```powershell
.\mvnw.cmd clean package
```

### 4. Ejecutar la aplicación

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

### 5. Abrir la aplicación

Acceder desde el navegador a:

```text
http://localhost:8080
```

## Rutas disponibles

| Módulo | Ruta |
|---|---|
| Inicio | `http://localhost:8080/` |
| Inventario | `http://localhost:8080/inventario` |
| Pedidos | `http://localhost:8080/pedidos` |
| Entregas | `http://localhost:8080/entregas` |
| Facturación | `http://localhost:8080/facturacion` |
| Informes | `http://localhost:8080/informes` |
| Archivo de estilos | `http://localhost:8080/css/styles.css` |

## Datos iniciales

La clase `DatosIniciales.java` registra información de ejemplo cuando las tablas están vacías.

Los datos incluyen:

- Seis referencias de flores.
- Cinco pedidos.
- Diferentes cantidades disponibles.
- Diferentes estados de pedidos.
- Precios de compra y venta.
- Fechas de entrega y presupuestos.

La validación de que la tabla se encuentre vacía evita duplicar la información cada vez que se inicia la aplicación.

## Resultados esperados

Al ejecutar correctamente el proyecto, la página de inicio debe mostrar:

| Indicador | Resultado inicial |
|---|---:|
| Referencias disponibles | 6 |
| Flores con stock bajo | 2 |
| Total de pedidos | 5 |
| Pedidos en curso | 2 |

En el módulo de pedidos deben aparecer:

| Estado | Cantidad inicial |
|---|---:|
| En curso | 2 |
| Completado | 2 |
| Entregado | 1 |

Estos valores se obtienen dinámicamente desde MySQL y se actualizan cuando cambia la información almacenada.

## Pruebas básicas

Para comprobar el funcionamiento del sistema:

1. Iniciar el servicio de MySQL.
2. Configurar la variable `DB_PASSWORD`.
3. Ejecutar la aplicación.
4. Abrir la página de inicio.
5. Verificar los indicadores generales.
6. Ingresar al módulo de inventario.
7. Comprobar que aparezcan las flores registradas.
8. Verificar que los lirios y las orquídeas aparezcan con stock bajo.
9. Ingresar al módulo de pedidos.
10. Comprobar que aparezcan los cinco pedidos.
11. Verificar los indicadores por estado.
12. Consultar las tablas desde MySQL Workbench.
13. Confirmar que todas las rutas del menú funcionen correctamente.


## Practica 2 - FrameworkS MVC

- **Nombre:** Laura Vanessa García Agudelo
- **Programa:** Tecnología en Desarrollo de Software
- **Institución:** IU Digital de Antioquia
- **Año:** 2026
