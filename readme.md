### Proyecto: CineManager

**Tecnologías:** Java Spring Boot, Hibernate, Angular

CineManager es una aplicación web diseñada para la gestión integral de un cine, que consta de dos capas principales:

- **Back Office:** Una interfaz de administración que permite gestionar las operaciones internas del cine.
- **Front Office:** Una plataforma para que los clientes interactúen con los servicios del cine, como la compra de entradas y la consulta de horarios. A pesar de estar divididas, la vista de la capa Front Office del cliente estará siempre actualizada y ajustada a la gestión realizada en la capa Back Office.

### Metodología de Desarrollo

La aplicación se desarrolla bajo la metodología de Test-Driven Development (TDD), utilizando JUnit para garantizar la calidad del código. Este enfoque permite que se escriban pruebas antes de implementar las funcionalidades, asegurando que cada componente del sistema se comporte según lo esperado.

Además, se aplican los principios de Clean Code y SOLID para mantener un código legible, mantenible y escalable. Estos principios ayudan a evitar la complejidad y a mejorar la comprensión del código por parte de otros desarrolladores.

Dentro de este proceso, se utilizará Postman como herramienta para probar las APIs de los microservicios. Postman facilitará la verificación de las respuestas y el comportamiento de los servicios, permitiendo asegurar que cada funcionalidad se implemente correctamente antes de pasar a la siguiente fase de desarrollo.

Además, se utilizará Swagger para generar automáticamente la documentación de la API de cada microservicio, Swagger permitirá describir de forma estructurada las diferentes rutas y modelos de datos de la API.


### Arquitectura de Microservicios

Se implementa una arquitectura de microservicios utilizando Java Spring Boot para el desarrollo del back-end y Angular para el front-end. La conexión a la base de datos se gestiona mediante Hibernate, facilitando la persistencia de datos.

### BACK-END

#### Tecnologías Utilizadas en Spring Boot

Las siguientes tecnologías se han seleccionado para cumplir con tareas específicas en el proyecto:

1. **Gestión de Configuración Centralizada:**
   
   - Se utiliza un repositorio Git para almacenar archivos de configuración.
   - Spring Cloud Config permite la gestión y actualización centralizada de la configuración de los microservicios.

2. **Escalado Dinámico de Microservicios:**
   
   - Spring Cloud Gateway facilita el enrutamiento y el balanceo de carga, permitiendo el escalado dinámico según la demanda del sistema.

3. **Monitoreo y Visualización de Métricas:**
   
   - Prometheus, Grafana y Micrometer se implementan para monitorizar el rendimiento y la salud de los microservicios, proporcionando gráficos en tiempo real.

4. **Visibilidad y Descubrimiento de Microservicios (DNS):**
   
   - Eureka permite el descubrimiento y la gestión de las instancias de microservicios, mejorando la comunicación y la conectividad entre ellos.

5. **Comunicación entre Microservicios:**
   
   - Kafka se utiliza para la comunicación asíncrona entre microservicios, garantizando la entrega de mensajes y la gestión de eventos de manera eficiente.

6. **Tracedo Distribuido de Logs:**
   
   - Se utiliza Jaeger para implementar el rastreo distribuido, lo que proporciona un registro centralizado y claro del flujo de solicitudes a través de los microservicios, facilitando la depuración y el análisis de rendimiento.

7. **Tolerancia a Fallos:**
   
   - Se utiliza Resilience4j para garantizar la resiliencia de la aplicación ante fallos, usando las siguientes estrategias:
     - **Circuit Breaker:** Detiene la ejecución de un microservicio si este devuelve un número elevado de errores, protegiendo el sistema de fallos en cascada.
     - **Retry (Reintentos):** Permite establecer un número máximo de intentos para reejecutar una solicitud en caso de fallos transitorios.
     - **Timeout:** Se configura un tiempo máximo para la respuesta de un servicio; si se supera, el proceso se detiene, evitando esperas prolongadas.
     - **Rate Limiting:** Se implementa para limitar la cantidad de solicitudes que un microservicio puede manejar en un periodo determinado, protegiendo la infraestructura de sobrecargas.
     - **Fallback:** Se definen respuestas alternas o mecanismos de recuperación en caso de que un microservicio no esté disponible, mejorando la experiencia del usuario.
       8- **Seguridad de la Aplicación:**
   - **Spring Security y JWT (JSON Web Tokens):** Se utiliza Spring Security en combinación con JWT para gestionar la autenticación y autorización de usuarios en la aplicación. Esta estrategia permite que, una vez autenticado, el usuario reciba un token JWT, que se incluye en cada solicitud subsiguiente. Así, el sistema valida que solo los usuarios autorizados pueden acceder a recursos específicos sin la necesidad de autenticarse en cada solicitud.
     - **Autenticación:** Spring Security verifica las credenciales del usuario y, al autenticarse, se genera un token JWT que el cliente almacena de manera segura.
     - **Autorización:** Spring Security verifica los permisos específicos que tiene cada usuario en función del token JWT, protegiendo endpoints de acceso no autorizado.
     - **Vencimiento de Tokens y Renovación:** Los tokens JWT tienen un tiempo de expiración, y se implementan mecanismos para la renovación segura de estos sin comprometer la seguridad.

### Microservicios

**Microservicios de Infraestructura:**  
Estos microservicios son esenciales para la arquitectura de la aplicación y pueden ser reutilizados en diferentes proyectos. Se centran en la gestión de la configuración, el monitoreo, la comunicación entre servicios y la tolerancia a fallos.

![microservicios-de-arquitectura](https://github.com/user-attachments/assets/a7936030-3da1-4f37-a471-d41c3a3bf092)



**Microservicios de Funcionalidad:**  
Estos microservicios desarrollan la funcionalidad específica de la aplicación para la gestión de cine (Back Office) y la plataforma de interacción para los clientes (Front Office). 

La comunicación entre microservicios se hace mediante la comunicación basada en eventos haciendo uso de Kafka y de la mensajería asíncrona.

- Back Office
  
![microservicios-de-funcionalidad-back-office](https://github.com/user-attachments/assets/b2c7fcc9-79b6-43ae-bcfc-58af585ae0e9)


- Front Office
  
 ![microservicios-de-funcionalidad-front-office png](https://github.com/user-attachments/assets/3be8a2f8-5e5c-4a84-8539-c642cd5507f2)

  
  ### 

### FRONT-END

#### Uso de Angular en el Proyecto

Angular se utilizará para desarrollar el front-end de la aplicación, integrándose con el backend de Spring Boot que implementa una arquitectura de microservicios. Las principales capacidades y enfoques de integración incluyen:

1. **Servicios para Comunicación:** Se utilizarán servicios en Angular para establecer la comunicación con los microservicios de Spring Boot. Esto permitirá realizar solicitudes HTTP para obtener, crear, actualizar y eliminar datos relacionados con las funcionalidades del cine.

2. **Componentes Modulares y Reutilizables:** Se crearán componentes en Angular para construir una interfaz de usuario modular y reutilizable, facilitando el mantenimiento y la escalabilidad de la aplicación.

3. **Observables para Actualización de Vistas:** Angular empleará observables para que las vistas se actualicen automáticamente con los datos provenientes del backend, asegurando que la interfaz de usuario esté siempre sincronizada con el estado actual de la aplicación.

4. **Formularios Dinámicos con Validaciones:** Se implementarán formularios reactivos que permitirán validar la información antes de enviarla al backend, garantizando que los datos ingresados sean correctos y cumplan con los requisitos establecidos.

5. **Enrutamiento Eficiente:** Se implementará un sistema de enrutamiento para dirigir a los usuarios a diferentes partes de la aplicación sin necesidad de recargar la página completa. Esto se logrará cargando solo los módulos necesarios mediante lazy loading, mejorando así el rendimiento y la experiencia del usuario.

### BASES DE DATOS

#### Uso de Hibernate con PostgreSQL

Hibernate se usará en el proyecto para interactuar con la base de datos PostgreSQL. Aquí te explico por qué:

1. **Mapeo Objeto-Relacional:** Se utilizará Hibernate para transformar automáticamente las clases de Java en tablas de la base de datos.
2. **Gestión de Transacciones:** Asegura que todas las operaciones CRUD se realicen correctamente, permitiendo deshacer cambios si algo sale mal para mantener los datos en un estado coherente.
3. **Consultas Sencillas:** Facilita la búsqueda de datos en la base de datos utilizando un lenguaje de consulta similar a SQL, pero más simple.
4. **Rendimiento Mejorado:** Está diseñado para hacer que las operaciones con la base de datos sean más rápidas y eficientes.
5. **Mantenimiento Simplificado:** Facilita el mantenimiento de la aplicación, permitiendo realizar ajustes sin mucho esfuerzo.

### CONCLUSIÓN

A lo largo del desarrollo del proyecto CineManager, he adquirido valiosos conocimientos sobre la implementación de una arquitectura de microservicios utilizando Java Spring Boot y Angular. La metodología de desarrollo basada en TDD, junto con los principios de Clean Code y SOLID, ha sido fundamental para asegurar la calidad y mantenibilidad del código. Además, la experiencia con herramientas como Hibernate y PostgreSQL ha mejorado mis habilidades en la gestión de bases de datos. La integración de tecnologías para el monitoreo, la comunicación asíncrona y la tolerancia a fallos ha proporcionado una comprensión más profunda de cómo construir aplicaciones robustas y escalables. Este proyecto no solo ha ampliado mis conocimientos técnicos, sino que también ha reforzado la importancia de una planificación y diseño arquitectónico adecuados para el éxito de aplicaciones complejas.
