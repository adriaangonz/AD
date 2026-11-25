# De un script SQL a una conexión JDBC con SQLite

**Acceso a Datos · SQLite**

Pasos para crear la base de datos `biblioteca.db` con DB Browser for SQLite a partir de un script y conectar con ella desde un proyecto Maven en NetBeans.

## Requisitos

- Necesitas **DB Browser for SQLite**, **NetBeans** con un JDK 17 o superior y el script **`biblioteca_sqlite.sql`**.
- El script tiene que estar escrito **para SQLite**. Verifica que se denomina **`biblioteca_sqlite`**.
- Ten localizada la **carpeta del proyecto de NetBeans**, la que contiene el `pom.xml`. 

---

## Parte 1. Crear la base de datos en DB Browser for SQLite

1. Abre DB Browser for SQLite y pulsa **Nueva base de datos**.
2. En la ventana de guardar:
   - ve a la **carpeta del proyecto de NetBeans**, junto al `pom.xml`;
   - en el tipo de archivo elige **Archivos de BB.DD. SQLite**, para que el fichero lleve la extensión `.db`;
   - escribe el nombre **`biblioteca.db`** y pulsa **Guardar**.
3. Se abre una ventana para crear una tabla. Pulsa **Cancelar**: las tablas las creará el script.
4. Abre la pestaña **Ejecutar SQL** y pulsa **Abrir archivo(s) SQL**. Selecciona `biblioteca_sqlite.sql`.
   > Si no aparece el contenido del script, ábrelo con un editor de texto, copia todo y pégalo en la pestaña.
5. Ejecuta el script con el botón **▶** (o **Ctrl+Intro**). Mira el panel de mensajes de la parte inferior: **no debe aparecer ningún error**.
6. Pulsa **Guardar cambios** (**Ctrl+S**).
   > Sin este paso, las tablas no se guardan en el fichero y Java encontrará la base de datos vacía. Es el error más habitual.
7. Comprueba el resultado:
   - en la pestaña **Estructura de la base de datos** deben aparecer las tablas **socios**, **libros** y **prestamos**;
   - en **Hojas de datos** deben verse 4 socios, 6 libros y 4 préstamos.
8. **Cierra DB Browser**, o al menos asegúrate de que no quedan cambios sin guardar. Si la base de datos queda abierta con cambios pendientes, Java puede encontrarla bloqueada (`database is locked`).



---

## Parte 2. Conectar desde NetBeans

1. Abre NetBeans y el proyecto Maven.
2. Comprueba que **`biblioteca.db` está en la carpeta raíz del proyecto**, junto al `pom.xml` y **no dentro de `src`**. Si la guardaste en otra carpeta en la Parte 1, cópiala ahora.
3. Añade al `pom.xml`, dentro de `<dependencies>`, la dependencia del driver JDBC de SQLite y guarda el fichero. Maven la descarga al compilar el proyecto.
   ```xml
   <dependency>
       <groupId>org.xerial</groupId>
       <artifactId>sqlite-jdbc</artifactId>
       <version>3.53.4.0</version>
   </dependency>
   ```
   > El `artifactId` se escribe en minúsculas y con guion: `sqlite-jdbc`. Maven distingue mayúsculas y minúsculas, así que con otro nombre no encuentra la librería y el proyecto no compila.
4. En el programa, conecta con la base de datos. La URL termina en el nombre **del fichero de la base de datos (`.db`), no del script (`.sql`)**:


5. Ejecuta el programa. Si todo está bien, la conexión tendrá lugar.


---

## Problemas frecuentes

| Mensaje o síntoma | Causa | Solución |
|---|---|---|
| `no such table: libros` | Java ha abierto otro fichero: el nombre o la carpeta no coinciden, o la URL apunta a `.sql` en lugar de `.db`. SQLite ha creado un fichero vacío. | Comprueba el nombre exacto y que `biblioteca.db` está junto al `pom.xml`. Borra el fichero vacío que se haya creado. |
| Las tablas existen pero están vacías, o no existen | No se pulsó **Escribir cambios** en DB Browser. | Repite la Parte 1 y pulsa **Ctrl+S** antes de cerrar. |
| El proyecto no compila: no encuentra `org.sqlite` o la dependencia | El `artifactId` o el `groupId` están mal escritos, o Maven no ha descargado la dependencia. | Revisa que sea `org.xerial` / `sqlite-jdbc` y vuelve a compilar (**Clean and Build**). |
| `[SQLITE_BUSY] database is locked` | DB Browser tiene la base de datos abierta con cambios sin guardar. | Pulsa **Escribir cambios** o cierra DB Browser. |
| `[SQLITE_CANTOPEN] unable to open database file` o `attempt to write a readonly database` | El fichero es de solo lectura o la carpeta está protegida. SQLite necesita escribir en la carpeta, no solo en el fichero. | Mueve el proyecto a una carpeta de usuario (Documentos, Escritorio). En Windows: botón derecho sobre el fichero → **Propiedades** → desmarca **Solo lectura**. |
| `Access denied for user 'root'@'localhost'` | Ese mensaje es de **MySQL**, no de SQLite: el programa está conectando con MySQL. | Revisa que la URL empiece por `jdbc:sqlite:` y que la dependencia sea `sqlite-jdbc`. |
| `UnsatisfiedLinkError: ...\AppData\Local\Temp\...sqlitejdbc.dll: Acceso denegado` (Windows) | El driver copia su librería nativa a la carpeta temporal, y el antivirus o una política del equipo impiden cargar DLL desde ahí. | Ver el apartado siguiente. |



### Linux: dónde está la base de datos

- DB Browser no tiene una carpeta fija: guarda la base de datos donde se elija en el paso 2 de la Parte 1. Los proyectos de NetBeans están por defecto en `~/NetBeansProjects`.
- Para encontrar el fichero si no sabes dónde se guardó:
  ```
  find ~ -iname "*biblioteca*" 2>/dev/null
  ```
- En Linux **las mayúsculas cuentan**: `Biblioteca.db` y `biblioteca.db` son ficheros distintos. El nombre tiene que coincidir exactamente con el de la URL.
- Si DB Browser se instaló desde el Gestor de software como **Flatpak**, puede no dejar ver o guardar en algunas carpetas. En ese caso, guarda la base de datos en la carpeta personal y cópiala después al proyecto, o instala la versión normal con `sudo apt install sqlitebrowser`.

---

## Resumen

| Paso | Dónde | Qué comprobar |
|---|---|---|
| Crear `biblioteca.db` en la carpeta del proyecto | DB Browser | El fichero tiene la extensión `.db` |
| Ejecutar `biblioteca_sqlite.sql` | DB Browser | No hay errores en el panel de mensajes |
| Escribir cambios (Ctrl+S) | DB Browser | Aparecen las 3 tablas con sus datos |
| Añadir `org.xerial:sqlite-jdbc` al `pom.xml` | NetBeans | El proyecto compila |
| Conectar con `jdbc:sqlite:biblioteca.db` | NetBeans | La consulta muestra los 6 libros |
