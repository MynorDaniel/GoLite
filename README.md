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
git clone git@github.com:MynorDaniel/GoLite.git |
git@github.com:MynorDaniel/GoLite.git
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

    fmt.Println("=== 2. OPERACIONES COMPLEJAS ===")

    var oa int     = 6
    var ob int     = -4
    oc := 3
    od := 10
    oe := 2
    var of1 float64 = 3.5
    var of2 float64 = -1.5

    
    
    
    
    res1 := (oa + ob) * oc - (od / oc + 1) + (-od) % oc + oa
    fmt.Println("1:", res1) 

    
    
    res2 := - - - - - oa
    fmt.Println("2:", res2) 

    
    
    res3 := - - - ob + od
    fmt.Println("3:", res3) 

    
    
    res4 := ! ! ! ! ! true
    fmt.Println("4:", res4) 

    
    
    res5 := ! ! ! (ob < 0 && oc != oe)
    fmt.Println("5:", res5) 

    
    
    
    res6 := -of2 * of1 - of2 > of1 + of1
    fmt.Println("6:", res6) 

    
    
    res7 := oa > od || ob < 0 && oc != oe
    fmt.Println("7:", res7) 

    
    
    
    
    res8 := ! (- - - oe == - oe) || (- - - - oa + ob > 0)
    fmt.Println("8:", res8) 

    
    
    
    
    sum9  := 0
    rest9 := od
    for i := 1; i <= 5; i++ {
        if i%2 != 0 || i > 3 {
            sum9  += -i
            rest9 -= i
        }
    }
    fmt.Println("9a:", sum9)  
    fmt.Println("9b:", rest9) 

    
    
    
    if - - - - ob > oa {
        fmt.Println("10: mayor")
    } else {
        fmt.Println("10: menor") 
    }


    fmt.Println()

    
    fmt.Println("--- 2. PIRAMIDE HUECA ---")

    n := 7 
    var linea string = "exterior"  
    for i := 1; i <= n; i++ {
        linea := ""

        for j := 1; j <= n - i; j++ { 
            linea += " "
        }

        if i == 1 {
            linea += "*" 
        } else if i == n {
            
            for j := 1; j <= n; j++ {
                linea += "* "
            }
        } else {
            linea += "*"
            for j := 1; j <= 2 * (i - 1) - 1; j++ { 
                linea += " "
            }
            linea += "*"
        }

        fmt.Println(linea)
    }
    fmt.Println("scope:", linea) 


    
    fmt.Println("--- 3. RELOJ DE ARENA ---")

    m := 5 

    for i := 0; i < m; i++ {
        linea := ""
        for j := 0; j < i; j++ {
            linea += " "
        }
        for j := 0; j < m - i; j++ {
            linea += "* "
        }
        fmt.Println(linea)
    }

    for i := m - 2; i >= 0; i -= 1 { 
        linea := ""
        for j := 0; j < i; j++ {
            linea += " "
        }
        for j := 0; j < m - i; j++ {
            linea += "* "
        }
        fmt.Println(linea)
    }


    
    fmt.Println("--- 4. PATRON DE ONDAS ---")

    ancho      := 41 
    filasOndas := 4  
    for r := 0; r < filasOndas; r++ {
        linea := ""
        for c := 0; c < ancho; c++ {
            mod := c % 8
            if mod == r || mod == 7 - r {
                linea += "*"
                continue 
            }
            linea += " "
        }
        fmt.Println(linea)
    }


    
    fmt.Println("--- 5. MARCO CON INTERIOR PUNTEADO ---")

    tamMarco := 7 
    for r := 0; r < tamMarco; r++ {
        lineaM := ""
        for c := 0; c < tamMarco; c++ {
            if r == 0 || r == tamMarco - 1 {
                lineaM += "* "
                continue 
            }
            if c == 0 {
                lineaM += "* "
                continue 
            }
            if c == tamMarco - 1 {
                lineaM += "*"
                break    
            }
            lineaM += ". " 
        }
        fmt.Println(lineaM)
    }


    
    fmt.Println("--- 6. FRACTAL DE SIERPINSKI ---")

    filas := 16 
    for i := 0; i < filas; i++ {
        linea := ""

        for s := 0; s < filas - 1 - i; s++ { 
            linea += " "
        }

        for j := 0; j <= i; j++ {
            
            isSubmask := true
            tempI := i
            tempJ := j
            for tempJ > 0 {
                if tempJ % 2 == 1 {
                    if tempI % 2 == 0 {
                        isSubmask = false
                        break 
                    }
                }
                tempI = tempI / 2 
                tempJ = tempJ / 2
            }

            if isSubmask {
                linea += "* " 
            } else {
                linea += "  " 
            }
        }

        fmt.Println(linea)
    }


    
    fmt.Println("--- 11. MATRIZ FILTRADA POR NUMEROS PRIMOS ---")

    colsPrima  := 9  
    numMax     := 82 
    colActual  := 0
    lineaPrima := ""
    num        := 2

    for num <= numMax {

        
        esPrimo  := true
        divisor  := 2
        for divisor * divisor <= num {
            if num % divisor == 0 {
                esPrimo = false
                break 
            }
            divisor += 1
        }

        celda := " ." 
        if esPrimo {
            
            d1 := num / 10  
            d0 := num % 10  

            tens := " " 
            if d1 == 1 { tens = "1" }
            if d1 == 2 { tens = "2" }
            if d1 == 3 { tens = "3" }
            if d1 == 4 { tens = "4" }
            if d1 == 5 { tens = "5" }
            if d1 == 6 { tens = "6" }
            if d1 == 7 { tens = "7" }
            if d1 == 8 { tens = "8" }

            units := "0"
            if d0 == 1 { units = "1" }
            if d0 == 2 { units = "2" }
            if d0 == 3 { units = "3" }
            if d0 == 4 { units = "4" }
            if d0 == 5 { units = "5" }
            if d0 == 6 { units = "6" }
            if d0 == 7 { units = "7" }
            if d0 == 8 { units = "8" }
            if d0 == 9 { units = "9" }

            celda = tens + units
        }

        lineaPrima += celda + "  "
        colActual += 1

        if colActual == colsPrima { 
            fmt.Println(lineaPrima)
            lineaPrima = ""
            colActual  = 0
        }

        num += 1
    }

    if colActual > 0 { 
        fmt.Println(lineaPrima)
    }

}
```

Salida esperada:

```text
=== 2. OPERACIONES COMPLEJAS ===
1: 6.666666666666666
2: -6
3: 14
4: false
5: false
6: false
7: true
8: true
9a: -13
9b: -3
10: menor

--- 2. PIRAMIDE HUECA ---
      *
     * *
    *   *
   *     *
  *       *
 *         *
* * * * * * * 
scope: exterior
--- 3. RELOJ DE ARENA ---
* * * * * 
 * * * * 
  * * * 
   * * 
    * 
   * * 
  * * * 
 * * * * 
* * * * * 
--- 4. PATRON DE ONDAS ---
*      **      **      **      **      **
 *    *  *    *  *    *  *    *  *    *  
  *  *    *  *    *  *    *  *    *  *   
   **      **      **      **      **    
--- 5. MARCO CON INTERIOR PUNTEADO ---
* * * * * * * 
* . . . . . *
* . . . . . *
* . . . . . *
* . . . . . *
* . . . . . *
* * * * * * * 
--- 6. FRACTAL DE SIERPINSKI ---
               * 
              * * 
             *   * 
            * * * * 
           *       * 
          * *     * * 
         *   *   *   * 
        * * * * * * * * 
       *               * 
      * *             * * 
     *   *           *   * 
    * * * *         * * * * 
   *       *       *       * 
  * *     * *     * *     * * 
 *   *   *   *   *   *   *   * 
* * * * * * * * * * * * * * * * 
--- 11. MATRIZ FILTRADA POR NUMEROS PRIMOS ---
 2   3   .   5   .   7   .   .   .  
 1   .   3   .   .   .   7   .   9  
 .   .   .   3   .   .   .   .   .  
 9   .   1   .   .   .   .   .   7  
 .   .   .   1   .   3   .   .   .  
 7   .   .   .   .   .   3   .   .  
 .   .   .   9   .   1   .   .   .  
 .   .   7   .   .   .   1   .   3  
 .   .   .   .   .   9   .   .   .  
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

