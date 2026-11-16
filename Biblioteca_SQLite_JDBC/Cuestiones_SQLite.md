# Cuestiones Teórico-Prácticas: Java y SQLite con JDBC
**Módulo:** Acceso a Datos (2º DAM)  
**Unidad 1:** Acceso a bases de datos relacionales mediante conectores (JDBC)

---

### Cuestión C1
**Pregunta:** Si se cambia SQLite por MySQL, ¿qué partes de un programa JDBC hay que modificar y cuáles no? ¿Qué papel tiene el driver?

**Respuesta:**
- **Partes que hay que modificar:**
  1. La **dependencia Maven** en el `pom.xml` (cambiar `sqlite-jdbc` por `mysql-connector-j`).
  2. Los **parámetros de conexión** en la URL y credenciales (la URL pasa de `jdbc:sqlite:archivo.db` a `jdbc:mysql://host:puerto/base_datos`, requiriendo ahora usuario y contraseña de red).
  3. Comandos específicos del motor (como `PRAGMA foreign_keys = ON`, funciones de fecha como `DATE('now')` frente a `NOW()` o `CURDATE()`, autoincrementos `AUTOINCREMENT` vs `AUTO_INCREMENT`, y tipos específicos).
- **Partes que NO hay que modificar:**
  El código Java estándar que interactúa con la API de JDBC (`Connection`, `Statement`, `PreparedStatement`, `CallableStatement`, `ResultSet`, transacciones con `commit()` y `rollback()`, y manejo de excepciones `SQLException`).
- **Papel del driver:**
  El driver actúa como un adaptador (patrón *Adapter*). Implementa las interfaces estándar de `java.sql` traduciendo las llamadas genéricas de Java al protocolo binario nativo que entiende el motor de base de datos específico (comunicación directa por ficheros en SQLite o protocolo de red TCP/IP cliente-servidor en MySQL).

---

### Cuestión C2
**Pregunta:** ¿Qué ocurre si la URL `jdbc:sqlite:biblioteca.db` apunta a un fichero que no existe? ¿Por qué «Conexión OK» no garantiza que el programa esté usando la base de datos correcta?

**Respuesta:**
- En SQLite, si el archivo indicado en la cadena de conexión no existe en la ruta de trabajo, **el driver lo crea automáticamente como un archivo nuevo y completamente vacío**.
- Por ello, obtener «Conexión OK» solo indica que el motor pudo crear o abrir un archivo SQLite válido en esa ruta. NO garantiza que estemos conectados a la base de datos que contiene nuestras tablas y datos. Si el nombre tiene una errata o difiere en mayúsculas/minúsculas (en Linux), el programa conectará a un archivo vacío y cualquier consulta (`SELECT`) lanzará la excepción `[SQLITE_ERROR] no such table`.

---

### Cuestión C3
**Pregunta:** SQLite solo comprueba las claves ajenas si se ejecuta `PRAGMA foreign_keys = ON`. ¿Por qué hay que ejecutarlo en cada conexión y no basta con hacerlo una vez? ¿Por qué un `INSERT` que falla en DB Browser puede funcionar desde Java?

**Respuesta:**
- El soporte de claves ajenas en SQLite está desactivado por defecto por motivos históricos de retrocompatibilidad. La directiva `PRAGMA foreign_keys = ON` es una variable de configuración **con ámbito de sesión/conexión** (`connection scope`), no una propiedad permanente guardada en el archivo de la base de datos. Al cerrarse la conexión, esa configuración se pierde.
- DB Browser for SQLite tiene activada por defecto la opción de comprobar claves foráneas en sus ajustes de interfaz gráfica, por lo que bloquea los `INSERT` con claves ajenas inválidas. En cambio, si una aplicación Java abre una nueva conexión JDBC y no ejecuta explícitamente `PRAGMA foreign_keys = ON`, las restricciones foráneas son ignoradas y el `INSERT` erróneo se insertará sin fallar.

---

### Cuestión C4
**Pregunta:** ¿Qué diferencia hay entre `executeQuery()`, `executeUpdate()` y `execute()`? ¿Qué devuelve cada uno?

**Respuesta:**
1. **`executeQuery()`**:
   - Se utiliza exclusivamente para consultas de lectura (`SELECT`).
   - Devuelve un objeto **`ResultSet`** con las filas obtenidas para ser recorridas mediante `rs.next()`.
2. **`executeUpdate()`**:
   - Se utiliza para sentencias DML de modificación de datos (`INSERT`, `UPDATE`, `DELETE`) o sentencias DDL (`CREATE TABLE`, `DROP TABLE`).
   - Devuelve un **`int`** que indica el número de filas afectadas en la base de datos (o 0 para sentencias DDL que no modifican tuplas).
3. **`execute()`**:
   - Es el método genérico para ejecutar cualquier sentencia SQL cuando no se sabe de antemano qué tipo de resultado producirá (o para scripts con múltiples operaciones).
   - Devuelve un **`boolean`**: `true` si la primera instrucción generó un `ResultSet` (que se recupera con `getResultSet()`), o `false` si fue una actualización o no produjo resultados (cuyo conteo de filas se obtiene con `getUpdateCount()`).

---

### Cuestión C5
**Pregunta:** ¿Qué es la inyección SQL? ¿Por qué un `PreparedStatement` la evita y una cadena concatenada no?

**Respuesta:**
- La **inyección SQL** es una vulnerabilidad de seguridad que se produce cuando los datos introducidos por un usuario se concatenan directamente dentro del texto de una sentencia SQL. Esto permite a un atacante inyectar comandos SQL maliciosos alterando la lógica de la consulta original (pudiendo saltarse autenticaciones mediante `' OR '1'='1`, o borrar datos con `; DROP TABLE`).
- **`PreparedStatement`** evita la inyección porque la estructura y el árbol sintáctico del comando SQL se compilan y pre-analizan en el motor de base de datos **antes** de enviar los valores. Los marcadores de posición (`?`) se transmiten de forma aislada como literales puros (datos), garantizando que los caracteres especiales (como comillas simples o guiones) sean tratados como texto literal y nunca como código ejecutable.

---

### Cuestión C6
**Pregunta:** El driver `sqlite-jdbc` contiene una librería nativa para cada sistema operativo. ¿Por qué la necesita, si es un driver para Java? ¿Por qué en algunos equipos Windows falla con `UnsatisfiedLinkError`?

**Respuesta:**
- El motor central de SQLite está escrito en lenguaje C nativo (biblioteca de alto rendimiento), no en bytecode Java. El driver `sqlite-jdbc` utiliza **JNI** (Java Native Interface) para enlazar el código Java con el motor nativo en C compilado para cada arquitectura y sistema operativo (`.so` en Linux, `.dll` en Windows, `.dylib` en macOS).
- En sistemas Windows corporativos o educativos, el driver intenta desempaquetar la DLL nativa dentro de la carpeta temporal del sistema (`%TEMP%` o `AppData\Local\Temp`). Si las políticas de seguridad o el antivirus del equipo bloquean la carga o ejecución de binarios ejecutables/DLLs desde directorios temporales, la máquina virtual lanza `UnsatisfiedLinkError: Acceso denegado`. La solución consiste en redirigir la propiedad del sistema a una carpeta permitida del usuario:  
  `System.setProperty("org.sqlite.tmpdir", System.getProperty("user.home") + "\\Documents");`
