# CC232 - Actividad 1 - Semana 1

> **Curso:** CC232 - Algoritmos y Estructuras de Datos
>
> **Actividad:** Arreglo dinámico
>
> **Duración:** 60 minutos | **Modalidad:** Individual | **Carácter:** Formativa, sin calificación
>
> **Objetivo:** Razonar sobre representación, invariantes y costo antes de implementar

---

**Nombre:** **Marco** **Código:** **20232741D**

**Indicaciones.** Responde primero en papel. Justifica brevemente cuando se solicite. No uses Internet ni asistentes de inteligencia artificial. Si la sesión es virtual, usa la computadora únicamente para conectarte a la clase y visualizar las indicaciones del docente.

---

## 1. ADT, representación, tamaño y capacidad (10 min)

```
a = [4, 7, 1, _, _, _, _, _]
n = 3
```

**a.** La estructura ofrece `size()`, `get(i)` y `add(x)`. ¿Qué parte corresponde al ADT y qué parte a la implementación?

    - Los tres (size(), get(i), add(x)) son operaciones del ADT (el comportamiento observable). La implementación decide cómo se realizan esas operaciones internamente.

**b.** ¿Cuántos elementos contiene la estructura y cuántas posiciones tiene el arreglo `a`?

    - La estructura contiene 3 elementos y El arreglo tiene 8 posiciones (0,1,...,7).

**c.** ¿Qué posiciones contienen elementos válidos?

    - Las posiciones 0, 1 y 2.

**d.** Explica la diferencia entre `n` y `a.length`.

    - n es el tamaño logico y a.length la capacidad fisica.

## 2. Invariante y acceso (8 min)

```java
Integer get(int i) {
    return a[i];
}
```

**a.** ¿Qué retorna `get(2)`? ¿Qué problema conceptual existe con `get(6)` si `n = 3`?

    - get(2) retorna 1, y el problema con get(6) es que no existe elemento ya que n=3, lo cual no retorna nada si no que habria un error.

**b.** Si existen un millón de elementos, ¿`get(2)` necesita revisar los anteriores? Clasifica el costo como O(1) u O(n) y justifica.

    - No necesita revisar lo anteriores solo busca al que necesita que esta en la posicion 2, lo cual clasifica el costo con un O(1).

**c.** Formula una condición que siempre debe cumplirse entre `n` y `a.length` y explica por qué es un invariante.

    - 0 <= n <= a.length

## 3. Cuando el arreglo se llena: resize() (10 min)

```
a = [4, 7, 1, 9]
n = 4
a.length = 4
```

Se desea agregar el valor `6`.

**a.** ¿Existe una posición disponible?

    - No existe posicion disponible ya que la capacidad esta al limite n=4 <= a.length=4.

**b.** En Java un arreglo no cambia su longitud después de ser creado. Propón una solución para poder agregar `6`.

    - Pues crear otro arreglo con el doble de capacidad y luego copiar el contenido del arreglo antiguo al nuevo y que el arreglo apunte al nuevo.

**c.** Dibuja el estado después de aplicar tu propuesta.

    - a = [4, 7, 1, 9, 6, _, _, _,] con n = 5 y a.length = 8.

**d.** ¿Qué información debe conservarse al reemplazar el arreglo?

    - Los elementos y el tamaño logico.

**e.** Si `resize()` debe copiar `n` elementos, ¿su trabajo se aproxima más a O(1) o a O(n)? Justifica.

    - Es mas un O(n) porque tener que hacer copiar cada elemento es mucho trabajo.

## 4. Dos estrategias de crecimiento (10 min)

```
Estrategia A: 1, 2, 3, 4, 5, 6, ...
Estrategia B: 1, 2, 4, 8, 16, 32, ...
```

**a.** ¿Cuál elegirías para realizar muchas inserciones al final?

    - Elegiria la Estrategia B.

**b.** Explica tu decisión considerando cuántas veces habría que crear y copiar arreglos.

    - La diferencia clave con la Estrategia A (1,2,3,4...), resize() se ejecuta en cada inserción. Con la B (1,2,4,8...), resize() se ejecuta cada vez con menos frecuencia relativa, manteniendo O(1) amortizado.

**c.** ¿Qué desventaja puede tener reservar más posiciones de las que se usan?

    - Pues que esas posiciones que no se usan consumen espacio y si en algun momento te quedas sin RAM y necesitan esos espacios vacio no puede usarlo y quedas en un cuello de botella.

## 5. Completa add al final (10 min)

```java
boolean add(Integer x) {
    if (n + 1 > a.length) {
        resize();
    }
    a[n] = x;
    n++;
    return true;
}
```

Completa las dos instrucciones faltantes. Luego explica qué representa cada una y por qué el orden es importante.

    - El primero solo es que en la posicion n poner el elemento x y el segundo es simplemente aumentar el n en 1. Y el orden esta bien asi ya que si primero sumamos el n seria ilogico porque no tendriamos ningun elementos agregado.

## 6. Traza y cierre: idea de costo amortizado (12 min)

Inicialmente `n = 0` y `a.length = 1`. Cada vez que el arreglo se llena, la capacidad se duplica. Se ejecutan `add(4)`, `add(7)`, `add(1)` y `add(9)`. Completa la tabla.

| Operación | Elementos almacenados |  n  | a.length |
| :-------: | :-------------------: | :-: | :------: |
|  Inicial  |         `[ ]`         |  0  |    1     |
| `add(4)`  |         `[4]`         |  1  |    1     |
| `add(7)`  |       `[4, 7]`        |  2  |    2     |
| `add(1)`  |      `[4, 7, 1]`      |  3  |    4     |
| `add(9)`  |    `[4, 7, 1, 9]`     |  4  |    4     |

**a.** ¿En qué inserciones fue necesario crear un arreglo nuevo?

    - En add(7) y add (1).

**b.** ¿Todas las llamadas a `add` necesitaron copiar elementos? Explica.

    - No ya que mientras no se llene la capacidad no es necesario hacer un resize().

**c.** Una llamada que ejecuta `resize()` puede costar O(n). ¿Por qué eso no ocurre en todas las inserciones?

    - No que todas las inserciones llaman a resize(). Solo cuando n+1 > a.length. Con crecimiento geométrico, hay cada vez más inserciones entre cada resize().

**d.** Completa la conclusión:

    - Una inserción individual puede llegar a costar O(n), pero al duplicar la capacidad los `resize` son cada vez menos frecuentes. Por eso `add` al final tiene costo O(1) amortizado.

## Antes de entregar

- [x] Puedo distinguir ADT de implementación.
- [x] Puedo explicar tamaño y capacidad.
- [x] Puedo formular el invariante `0 <= n <= a.length`.
- [x] Puedo justificar por qué `get(i)` es O(1) y `resize()` es O(n).
- [x] Puedo explicar por qué `add` al final es O(1) amortizado con crecimiento geométrico.
