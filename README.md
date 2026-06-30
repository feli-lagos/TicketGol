# TicketGol 🎟️⚽

Sistema distribuido de gestión de tickets para eventos de fútbol, desarrollado con *Spring Boot 3* bajo arquitectura de microservicios. Permite la compra de entradas, gestión de clubes, eventos, estadios, merchandising y control de acceso a recintos deportivos.

---

## Integrantes del equipo

| Nombre            | Microservicios a cargo                                                       |
|-------------------|------------------------------------------------------------------------------|
| Francisca  Espina | clubes, eventos, merch                                                       |
| Martín Simón      | merch_ordenes, tickets,                                                      |
| Felipe Lagos      | usuarios, gateway, eureka, usuarios_sancionados, estadios, estadio_secciones |
| Nicolás Fernández | compras, pases_temporada                                                     |

---

## Arquitectura del sistema

El sistema utiliza *Eureka Server* como registro de servicios y *Spring Cloud Gateway* como punto único de entrada. Cada microservicio se registra automáticamente en Eureka al iniciar, y el Gateway descubre sus ubicaciones de forma dinámica sin necesidad de URLs fijas.


Cliente (Postman / Browser)
        │
        ▼
  API Gateway :8080
        │  (consulta Eureka para resolver servicios)
        ▼
  Eureka Server :8761
        │
        ├── servicio-usuarios        :8082
        ├── servicio-usuarios-sancionados :8081
        ├── servicio-estadios        :8090
        ├── servicio-estadio-secciones :8091
        ├── servicio-eventos         :8100
        ├── servicio-tickets         :8101
        ├── servicio-pasestemporadas :8102
        ├── servicio-clubes          :8110
        ├── merchandise              :8111
        ├── servicio-merch-ordenes   :8112
        └── servicio-compras         :8113


---

## Microservicios implementados

| Microservicio | Nombre en Eureka | Puerto | Base de datos |
|---|---|---|---|
| eureka-server | eureka-server | 8761 | — |
| api-gateway | api-gateway | 8080 | — |
| usuarios | servicio-usuarios | 8082 | db_ticketgol_usuarios |
| usuarios_sancionados | servicio-usuarios-sancionados | 8081 | db_ticketgol_sancionados |
| estadios | servicio-estadios | 8090 | db_ticketgol_estadios |
| estadio_secciones | servicio-estadio-secciones | 8091 | db_ticketgol_secciones |
| eventos | servicio-eventos | 8100 | db_ticketgol_eventos |
| tickets_M | servicio-tickets | 8101 | db_ticketgol_tickets |
| pases_temporada | servicio-pasestemporadas | 8102 | db_ticketgol_pasestemporadas |
| clubes | servicio-clubes | 8110 | db_ticketgol_clubes |
| merch | merchandise | 8111 | db_ticketgol_merchandise |
| merch_ordenes_M | servicio-merch-ordenes | 8112 | db_ticketgol_merchordenes |
| compras | servicio-compras | 8113 | db_ticketgol_compras_test |

---

## Rutas principales del API Gateway

El Gateway usa Eureka Discovery Locator con lower-case-service-id: true, lo que genera rutas automáticas en base al nombre del servicio registrado en Eureka. Todas las peticiones entran por el *puerto 8080*.

| Ruta Gateway | Servicio destino | Puerto directo |
|---|---|---|
| http://localhost:8080/servicio-usuarios/** | usuarios | 8082 |
| http://localhost:8080/servicio-usuarios-sancionados/** | usuarios_sancionados | 8081 |
| http://localhost:8080/servicio-estadios/** | estadios | 8090 |
| http://localhost:8080/servicio-estadio-secciones/** | estadio_secciones | 8091 |
| http://localhost:8080/servicio-eventos/** | eventos | 8100 |
| http://localhost:8080/servicio-tickets/** | tickets_M | 8101 |
| http://localhost:8080/servicio-pasestemporadas/** | pases_temporada | 8102 |
| http://localhost:8080/servicio-clubes/** | clubes | 8110 |
| http://localhost:8080/merchandise/** | merch | 8111 |
| http://localhost:8080/servicio-merch-ordenes/** | merch_ordenes_M | 8112 |
| http://localhost:8080/servicio-compras/** | compras | 8113 |

> El panel de Eureka para ver todos los servicios registrados está en: http://localhost:8761

---

## Documentación Swagger / OpenAPI

### Acceso directo por microservicio (local)

| Microservicio | URL Swagger |
|---|---|
| clubes | http://localhost:8110/doc/swagger-ui.html |
| eventos | http://localhost:8100/doc/swagger-ui.html |
| merch | http://localhost:8111/doc/swagger-ui.html |
| merch_ordenes | http://localhost:8112/doc/swagger-ui.html |
| tickets | http://localhost:8101/doc/swagger-ui.html |
| pases_temporada | http://localhost:8102/doc/swagger-ui.html |
| compras | http://localhost:8113/doc/swagger-ui.html |
| estadios | http://localhost:8090/doc/swagger-ui.html |
| estadio_secciones | http://localhost:8091/doc/swagger-ui.html |
| usuarios | http://localhost:8082/doc/swagger-ui.html |
| usuarios_sancionados | http://localhost:8081/doc/swagger-ui.html |

### Acceso remoto (Railway) — completar tras despliegue

| Microservicio | URL Swagger remota |
|---|---|
| clubes | https://[URL-RAILWAY-CLUBES]/doc/swagger-ui.html |
| eventos | https://[URL-RAILWAY-EVENTOS]/doc/swagger-ui.html |
| merch | https://[URL-RAILWAY-MERCH]/doc/swagger-ui.html |

---

## Instrucciones de ejecución local

### Requisitos previos
- Java 21+
- Maven 3.9+
- MySQL 8 corriendo en localhost:3306 con usuario root sin contraseña (o ajustar en el application.yml de cada servicio)
- IntelliJ IDEA (recomendado)

### Orden de arranque obligatorio

> ⚠️ El orden importa: Eureka debe estar listo antes que los microservicios, y el Gateway debe arrancar último.

*1. Primero — Eureka Server*

Abrir: ticketgol/eureka-server
Ejecutar: EurekaServerApplication.java
Verificar en: http://localhost:8761


*2. Luego — Microservicios (en cualquier orden)*

ticketgol/usuarios          → UsuariosApplication.java
ticketgol/usuarios_sancionados → UsuariosSancionadosApplication.java
ticketgol/estadios          → EstadiosApplication.java
ticketgol/estadio_secciones → EstadioSeccionesApplication.java
ticketgol/clubes            → ClubesApplication.java
ticketgol/eventos           → EventosApplication.java
ticketgol/tickets_M         → TicketsApplication.java
ticketgol/pases_temporada   → PasesTemporadaApplication.java
ticketgol/merch             → MerchandiseApplication.java
ticketgol/merch_ordenes_M   → MerchOrdenesApplication.java
ticketgol/compras           → ComprasApplication.java


*3. Último — API Gateway*

Abrir: ticketgol/api-gateway
Ejecutar: ApiGatewayApplication.java


Las bases de datos se crean automáticamente gracias a createDatabaseIfNotExist=true en la URL de conexión de cada microservicio.

### Verificar que todo funciona

# Panel Eureka (todos los servicios deben aparecer aquí)
GET http://localhost:8761

# Ejemplo de petición a través del Gateway
GET http://localhost:8080/servicio-clubes/api/clubes


---

## Instrucciones de ejecución remota (Railway)

Cada microservicio está desplegado como servicio independiente en Railway. El perfil activo en producción se configura con la variable de entorno:


SPRING_PROFILES_ACTIVE=prod


### URLs remotas — completar tras despliegue

| Servicio | URL pública |
|---|---|
| Eureka Server | https://[eureka-URL].up.railway.app |
| API Gateway | https://[gateway-URL].up.railway.app |
| clubes | https://[clubes-URL].up.railway.app |
| eventos | https://[eventos-URL].up.railway.app |
| merch | https://[merch-URL].up.railway.app |

---

## Pruebas unitarias

Los tests se ejecutan con ./mvnw test dentro del módulo correspondiente.

| Microservicio | Clase de test | Framework |
|---|---|---|
| clubes | ClubServiceTest | JUnit 5 + Mockito |
| eventos | EventoServiceTest | JUnit 5 + Mockito |
| merch | MerchandiseServiceTest | JUnit 5 + Mockito |

Todos los tests usan @ExtendWith(MockitoExtension.class) con patrón *Given-When-Then*, mocks de repositorios y no requieren base de datos activa para ejecutarse.
