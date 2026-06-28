# GoLite

Intérprete para el lenguaje de programación GoLite, un lenguaje diseñado con una sintaxis inspirada en Go.

## Requisitos

Antes de compilar el proyecto asegúrese de tener instalado:

- Java JDK 17 o superior
- Apache Maven 3.8 o superior
- Graphviz

Puede verificar las versiones instaladas con:

```bash
java --version
mvn --version
dot -V

```

---

## Descarga

Descargue la versión más reciente desde la sección de Releases del repositorio y extraiga el archivo en su equipo.

También puede clonar el repositorio:

```bash
git clone https://github.com/MynorDaniel/EJ26_OLC1_3358109340901-202331039.git |
git@github.com:MynorDaniel/EJ26_OLC1_3358109340901-202331039.git
cd GoLite
```

---

## Compilación

Ubíquese en la carpeta raíz del proyecto y ejecute:

```bash
mvn clean package
```

Este comando:

- Limpia archivos generados previamente.
- Compila todo el proyecto.
- Genera el archivo JAR ejecutable dentro de la carpeta `target`.

Si la compilación finaliza correctamente, verá un mensaje similar a:

```text
BUILD SUCCESS
```

---

## Ejecución

Una vez compilado el proyecto, ejecute:

```bash
java -jar target/GoLite-1.0-SNAPSHOT.jar
```

---

## Estructura del proyecto

```text
GoLite/
├── src/
│   └── main/
│       ├── ast/
|       ├── vista/
|       ├── lexer/
|       ├── interprete/
│       └── parser/
├── pom.xml
└── README.md
```

---

## Ejemplo de programa

```go
func main() {
    fmt.Println("Hola mundo")
}
```

Salida esperada:

```text
Hola mundo
```

---

## Características implementadas

- Variables y asignaciones.
- Operaciones aritméticas y lógicas.
- Estructuras condicionales (`if`, `else if`, `else`).
- Control de flujo (`break`, `continue`, `return`).
- Funciones nativas del lenguaje.

---

## Desarrollo

Para recompilar el proyecto después de realizar cambios:

```bash
mvn clean package
```

