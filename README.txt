RV Solutions - Sistema de Servicio Técnico

Proyecto Java Swing + MySQL con estructura MVC.

USUARIOS DE PRUEBA
- Administrador: admin / admin123
- Técnico: tecnico / tecnico123

MEJORAS APLICADAS EN ESTA VERSIÓN
- Se mantuvo la estructura principal del proyecto: modelo, vista, controlador, DAO y conexión.
- Se mejoró el diseño de las pantallas principales para una presentación más amplia en pantalla grande.
- Se dejó la ventana Usuarios dentro del panel del administrador para registrar, actualizar y eliminar usuarios.
- Se retiró del menú el módulo Imágenes de fondo; los fondos se cambian directamente en Visual Studio Code dentro de assets/fondos.
- El resumen del administrador y del técnico está vinculado a datos reales de la base de datos.
- Se conserva la descarga de reportes en los módulos principales.
- Se integró configuración Maven para declarar las librerías solicitadas en el avance 3.

LIBRERÍAS INTEGRADAS EN EL PROYECTO
Dentro del archivo pom.xml se declararon estas librerías:
- Apache POI
- Google Guava
- Apache Commons Lang
- Logback
- MySQL Connector/J

Adicionalmente, en la carpeta /lib se mantiene el conector físico:
- mysql-connector-j-9.7.0.jar

BASE DE DATOS
1. Ejecutar el script database/bd_servicio_tecnico.sql en MySQL Workbench.
2. Revisar src/conexion/ConexionMySQL.java y cambiar la clave de MySQL si es necesario.

COMPILACIÓN MANUAL CON JAVAC
Desde la carpeta del proyecto:

javac -encoding UTF-8 -cp "lib/*" -d bin src/Main.java src/conexion/*.java src/modelo/*.java src/dao/*.java src/controlador/*.java src/vista/*.java

Ejecución en Windows:
java -cp "bin;lib/*" Main

Ejecución en Linux/Mac:
java -cp "bin:lib/*" Main

COMPILACIÓN CON MAVEN
Si tiene Maven instalado, puede usar:
- mvn compile
- mvn exec:java

Esto descargará automáticamente las dependencias declaradas en pom.xml.

MÓDULOS DISPONIBLES
Administrador:
- Panel de control
- Clientes
- Equipos
- Órdenes y servicios
- Pagos
- Usuarios

Técnico:
- Panel de control
- Clientes
- Equipos
- Órdenes y servicios

CONTROL DE VERSIONES SUGERIDO
Desde la carpeta RV-Solutions-Java:

git init
git add .
git commit -m "Avance 3: mejora UI, dashboard, usuarios, reportes y librerías Maven"
git branch -M main
git remote add origin URL_DEL_REPOSITORIO
git push -u origin main

AJUSTES SOLICITADOS ADICIONALES
- En la ventana Clientes, el ID visible del cliente ahora es el DNI. El número consecutivo de MySQL queda solo como ID técnico interno para mantener relaciones con equipos y órdenes.
- Se agregó carpeta assets/fondos con tres imágenes editables:
  * assets/fondos/login.png
  * assets/fondos/administrador.png
  * assets/fondos/tecnico.png
- Se agregó el módulo Imágenes de fondo en el panel administrador para escoger y reemplazar las imágenes de fondo.
- Las pantallas principales se abren maximizadas para ocupar el tamaño de la laptop.
- Las ventanas quedaron redimensionables para poder encoger o ampliar la pantalla desde los bordes.

AJUSTE DE DISEÑO PANTALLA COMPLETA
- Se corrigió el Login para que el bloque de acceso quede centrado en cualquier tamaño de pantalla.
- Se reemplazó el posicionamiento rígido de los paneles principales por diseño adaptable con BorderLayout y GridLayout.
- El contenido del panel Administrador y Técnico ahora ocupa todo el espacio disponible de la pantalla, evitando que quede acumulado a la izquierda.
- Las ventanas siguen maximizadas y pueden redimensionarse según el tamaño de la laptop.


MEJORAS AGREGADAS EN ESTA VERSIÓN COMPLETA
------------------------------------------
1. Se agregó la ventana FormDiagnosticos.java para completar el módulo de diagnósticos técnicos.
2. El panel Administrador ahora incluye acceso directo a Diagnósticos.
3. El panel Técnico ahora incluye acceso directo a Diagnósticos.
4. El dashboard muestra el total de diagnósticos registrados.
5. Se agregó test/controlador/DiagnosticoControllerTest.java como evidencia inicial de TDD con JUnit.
6. Se mejoró el fondo visual con superposición degradada para mejor lectura de las interfaces.
7. Se mantiene la arquitectura MVC + DAO: modelo, vista, controlador, dao y conexión.
8. Se actualizó pom.xml con JUnit para pruebas, además de las librerías solicitadas.

MÓDULOS FUNCIONALES
-------------------
- Login con roles: Administrador y Técnico.
- Clientes.
- Equipos.
- Órdenes y servicios.
- Diagnósticos técnicos.
- Pagos.
- Usuarios.
- Dashboard con indicadores.
- Reportes CSV desde las tablas.

COMANDO RÁPIDO CON JAVAC
------------------------
Desde la carpeta del proyecto:

javac -encoding UTF-8 -cp "lib/mysql-connector-j-9.7.0.jar" -d bin $(find src -name "*.java")
java -cp "bin:lib/mysql-connector-j-9.7.0.jar" Main

En Windows PowerShell, si usas javac manual, compila todos los .java de src y agrega el conector MySQL al classpath.

ACTUALIZACIÓN DE DISEÑO MODERNO
- Se rediseñó el panel administrador y técnico con estilo dashboard moderno.
- Se incorporó menú lateral oscuro con opción Configuración y Reportes.
- Se reemplazó el uso de emojis/cuadritos por textos limpios para evitar símbolos mal renderizados.
- Se actualizó el logo como imagen en assets/logo/logo_rv.png e icon_rv.png.
- Se aplicó fondo plomito claro, tarjetas blancas, sombras suaves, tablas claras y botones modernos.
- Se agregaron accesos rápidos, resumen operativo, actividad reciente y panel financiero visual.

COMANDOS DE EJECUCIÓN SIN MAVEN:
javac -encoding UTF-8 -cp "lib/*" -d bin (Get-ChildItem -Recurse -Filter *.java src).FullName
java -cp "bin;lib/*;assets" Main

COMANDOS CON MAVEN:
mvn clean compile
mvn exec:java


MÓDULO DE IMÁGENES AGREGADO
--------------------------
Se incorporó un nuevo módulo llamado "Imágenes" en el menú lateral del administrador y del técnico.
Este módulo muestra las imágenes usadas por el sistema y el nombre exacto de cada archivo.

Imágenes incluidas:
1. assets/logo/logo_rv.png - Logo principal del sistema.
2. assets/logo/icon_rv.png - Icono de ventana de la aplicación.
3. assets/fondos/login.png - Fondo de la pantalla de login.
4. assets/fondos/administrador.png - Fondo del panel administrador.
5. assets/fondos/tecnico.png - Fondo del panel técnico.
6. assets/modulo_imagenes/img_login_banner.png - Banner decorativo para login.
7. assets/modulo_imagenes/img_dashboard_admin.png - Imagen de apoyo para dashboard administrador.
8. assets/modulo_imagenes/img_dashboard_tecnico.png - Imagen de apoyo para dashboard técnico.
9. assets/modulo_imagenes/img_clientes.png - Imagen del módulo clientes.
10. assets/modulo_imagenes/img_equipos.png - Imagen del módulo equipos.
11. assets/modulo_imagenes/img_ordenes.png - Imagen del módulo órdenes y servicios.
12. assets/modulo_imagenes/img_reportes.png - Imagen del módulo reportes.
13. assets/modulo_imagenes/img_configuracion.png - Imagen del módulo configuración.


===============================================
MÓDULO DE IMÁGENES EDITABLE
===============================================
Se agregó un módulo llamado "Imágenes" desde donde el usuario puede:
1. Ver la galería actual del sistema.
2. Seleccionar una imagen desde su computadora.
3. Reemplazar fácilmente el logo, fondos y banners del sistema.
4. Guardar la nueva imagen automáticamente en la carpeta assets.

Nombres de imágenes configurados:
- assets/logo/logo_rv.png
- assets/logo/icon_rv.png
- assets/fondos/login.png
- assets/fondos/administrador.png
- assets/fondos/tecnico.png
- assets/modulo_imagenes/img_login_banner.png
- assets/modulo_imagenes/img_dashboard_admin.png
- assets/modulo_imagenes/img_dashboard_tecnico.png
- assets/modulo_imagenes/img_clientes.png
- assets/modulo_imagenes/img_equipos.png
- assets/modulo_imagenes/img_ordenes.png
- assets/modulo_imagenes/img_diagnosticos.png
- assets/modulo_imagenes/img_pagos.png
- assets/modulo_imagenes/img_usuarios.png
- assets/modulo_imagenes/img_reportes.png
- assets/modulo_imagenes/img_configuracion.png
