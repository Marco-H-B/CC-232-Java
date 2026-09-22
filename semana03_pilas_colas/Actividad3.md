# CC232 - Actividad 3 - Semana 3

> **Curso:** CC232 - Algoritmos y Estructuras de Datos  
> **Tema:** Pilas, colas y deques  
> **Duración:** 80 minutos | **Modalidad:** Individual | **Carácter:** Formativa, sin calificación  
> **Objetivo:** Razonar sobre ADT, representación, invariantes, circularidad, costo y verificación antes de implementar  
> **Entrega:** Hojas de la actividad

---

**Nombre:** **Marco Huamani Bonifacio** | **Código:** **20232741D**

**Indicaciones:** Responde primero en papel. Razona a partir de estados, contratos e invariantes y justifica brevemente cuando se solicite. No uses Internet ni asistentes de inteligencia artificial. Antes de codificar, deja una predicción explícita de lo que cada operación debe preservar.

---

## 1. De una secuencia general a una pila (6 min)

Una estructura permite insertar y retirar por el mismo extremo. Se ejecuta:

```text
push('A'), push('B'), push('C'), pop(), push('D'), pop(), pop()
```

**a. Escribe, en orden, los valores retornados por las llamadas `pop()`.**

- Seguimiento paso a paso:
  1. `push('A')` $\to$ pila: `[A]`
  2. `push('B')` $\to$ pila: `[A, B]`
  3. `push('C')` $\to$ pila: `[A, B, C]`
  4. `pop()` $\to$ saca y retorna **`'C'`** (queda `[A, B]`)
  5. `push('D')` $\to$ pila: `[A, B, D]`
  6. `pop()` $\to$ saca y retorna **`'D'`** (queda `[A, B]`)
  7. `pop()` $\to$ saca y retorna **`'B'`** (queda `[A]`)
- **Valores retornados en orden:** `'C'`, `'D'`, `'B'`.

**b. Dibuja el estado final e indica qué elemento quedaría disponible para el siguiente `pop()`.**

- **Estado final:**
  ```text
  tope -> ['A'] -> null
  n = 1
  ```
- El elemento que queda disponible para el siguiente `pop()` es **`'A'`**.

**c. ¿Qué política de acceso observas? Explica brevemente qué significa.**

- Es la política **LIFO** (_Last-In, First-Out_ / Último en entrar, primero en salir).
- Significa que el último elemento que agregas a la colección es obligatoriamente el primero que vas a retirar. Las inserciones y eliminaciones quedan restringidas a un solo extremo (el tope).

---

## 2. LinkedStack: representación e invariante (7 min)

El primer archivo de la semana reutiliza una representación enlazada:

```java
private Node head;
private int n;
int size() { return n; }
boolean isEmpty() { return n == 0; }
```

**a. ¿Qué representa `head` dentro de la pila y por qué no es necesario guardar `tail`?**

- `head` representa el **tope** de la pila (por donde entra todo con `push` y sale todo con `pop`).
- No necesitamos guardar `tail` porque una pila nunca opera al fondo, solo en el tope. En una lista simplemente enlazada insertar en la cabeza y retirar de la cabeza cuesta $O(1)$ directo. Si intentáramos sacar por `tail`, tendríamos que recorrer toda la lista para hallar el penúltimo nodo ($O(n)$). Así que tener `tail` no ayuda en nada y solo gastaría memoria y líneas de código innecesarias.

**b. Completa un invariante mínimo:**

- Si `n == 0`, `head == null`;
- Si `n > 0`, desde `head` deben alcanzarse exactamente **`n`** nodos (con el último nodo apuntando a `null`).

**c. ¿Qué relación debe existir entre `isEmpty()` y `n`?**

- `isEmpty()` retorna `true` si y solo si `n == 0`.
- Ambos deben ser siempre coherentes: la pila está vacía exactamente cuando su número de elementos válidos es 0.

---

## 3. push(), pop() y un algoritmo cliente (9 min)

Completa solo las expresiones faltantes. No agregues recorridos.

```java
void push(char x) {
    head = new Node(x, head);
    n++;
}

char pop() {
    if (n == 0) throw new java.util.NoSuchElementException("pila vacía");
    char x = head.x;
    head = head.next;
    n--;
    return x;
}
```

**a. Justifica por qué `push()` y `pop()` son O(1) respecto de n.**

- Porque ambas operaciones solo manipulan la referencia `head` del inicio. Solo crean un nodo o reasignan un puntero local en un número fijo y constante de pasos, sin importar si la pila tiene 5 o 5 millones de elementos. No hay ningún bucle ni recorrido dependiente de $n$.

**b. Para `text = "DATA"`, indica cómo queda la pila después de los cuatro push y qué salida produce luego el ciclo de pop.**

- Conforme se hace `push` de `'D'`, `'A'`, `'T'`, `'A'`, cada nuevo carácter entra por la cabeza:
  ```text
  head -> ['A'] -> ['T'] -> ['A'] -> ['D'] -> null
  n = 4
  ```
- Al hacer el ciclo de `pop()`, se extraen en orden inverso:
  - 1º `pop()`: `'A'`
  - 2º `pop()`: `'T'`
  - 3º `pop()`: `'A'`
  - 4º `pop()`: `'D'`
- **Salida producida:** `"ATAD"`.

**c. ¿Qué propiedad del ADT explica la inversión sin que `reverse()` conozca Node ni next?**

- La propiedad **LIFO** de la pila. El cliente solo llama a `push()` y `pop()`. Como lo último que metiste sale primero por la propia regla de la pila, la palabra se invierte sola sin que el código de `reverse()` tenga idea de punteros, nodos ni nada de lo que pasa por dentro.

---

## 4. De FIFO a una cola circular (7 min)

Una cola inserta por el final lógico y retira por el frente. Si cada `remove()` desplazara todos los elementos de un arreglo, podría costar $O(n)$. En `ArrayQueue` se mantiene `j` como índice físico del frente.

```text
índice físico   0   1   2   3   4   5   6   7
a              40  50   _   _   _   _  20  30
j = 6, n = 4
secuencia lógica = [20, 30, 40, 50]
```

**a. ¿Qué valor retorna `peek()`?**

- Retorna **`20`** (el valor en `a[j] = a[6]`, que es el elemento al frente de la cola).

**b. Si se ejecuta `remove()`, ¿qué valor se retira y cuáles son los nuevos `j` y `n` antes de cualquier resize?**

- Valor retirado: **`20`**.
- Nuevo `j`: **`7`** (avanza una posición: `(6 + 1) % 8 = 7`).
- Nuevo `n`: **`3`** (se decrementa en 1).

**c. Explica por qué avanzar `j` puede evitar desplazar todos los elementos.**

- Porque en vez de mover en un ciclo for los $n-1$ elementos restantes hacia la izquierda (que sería $O(n)$), simplemente corremos el marcador del frente `j` una casilla hacia adelante en tiempo constante $O(1)$. Redefinimos dónde empieza la cola sin tocar los demás elementos.

---

## 5. Índice lógico, índice físico e invariante circular (8 min)

Completa la correspondencia usando `(j + k) % a.length` para el estado anterior ($j = 6$, $a.\text{length} = 8$):

| Posición lógica $k$ |    Cálculo    | Posición física |
| :-----------------: | :-----------: | :-------------: |
|          0          | `(6 + 0) % 8` |      **6**      |
|          1          | `(6 + 1) % 8` |      **7**      |
|          2          | `(6 + 2) % 8` |      **0**      |
|          3          | `(6 + 3) % 8` |      **1**      |

**a. Explica con tus palabras qué significa: el elemento lógico k está en `a[(j+k) % a.length]`.**

- Significa que para encontrar el $k$-ésimo elemento de la cola, partimos del frente `j`, avanzamos $k$ posiciones hacia adelante, y con el `% a.length` logramos que si nos pasamos del final del arreglo (índice 7), demos la vuelta automáticamente y sigamos contando desde el índice 0.

**b. ¿Por qué el arreglo no es circular físicamente aunque hablemos de cola circular?**

- Porque en la memoria RAM el arreglo es un bloque lineal y plano con celdas del 0 al 7. No tiene forma de círculo en el hardware. La circularidad es una abstracción puramente lógica que logramos mediante la aritmética del operador módulo `%`.

---

## 6. Leer add() y completar resize() (11 min)

```java
boolean add(Integer x) {
    if (n + 1 > a.length) resize();
    a[(j + n) % a.length] = x;
    n++;
    return true;
}
```

**a. ¿Por qué `add()` usa `j + n` y no `a[n]`?**

- Porque el frente de la cola no siempre está en el índice 0, sino en la posición `j`. Por eso, el casillero libre al final de la cola está a una distancia de $n$ posiciones a partir de `j` (haciendo el salto circular `% a.length`). Usar `a[n]` asumiría falsamente que el frente nunca se ha movido del índice 0.

**Completa `resize()` preservando el orden lógico:**

```java
private void resize() {
    Integer[] b = new Integer[Math.max(1, 2 * n)];
    for (int k = 0; k < n; k++) {
        b[k] = a[(j + k) % a.length];
    }
    a = b;
    j = 0;
}
```

**b. ¿Qué debe permanecer igual después de `resize()` y qué puede cambiar?**

- **Debe permanecer igual:** El tamaño lógico `n` y el orden de la secuencia lógica (el primer elemento sigue siendo el primero, el segundo el segundo, etc.).
- **Puede cambiar:** La capacidad física (`a.length`), las posiciones físicas en las que residen los datos, y el índice del frente `j` (que se reinicia a 0).

**c. ¿Por qué conviene que `j` quede en 0 después del resize()?**

- Porque al copiar `b[k] = a[(j + k) % a.length]` en orden lógico, pasas los elementos limpios empezando desde la casilla 0. Al poner `j = 0`, la cola queda desenredada desde el inicio y con todo el espacio libre a la derecha para seguir metiendo datos sin problemas.

---

## 7. Completar remove() y justificar el costo amortizado (10 min)

```java
Integer remove() {
    if (n == 0) {
        throw new NoSuchElementException("cola vacía");
    }
    Integer x = a[j];
    a[j] = null;
    j = (j + 1) % a.length;
    n--;
    if (a.length >= 3 * n) resize();
    return x;
}
```

**a. Completa las expresiones faltantes y explica qué representa cada una.**

- `Integer x = a[j];`: Captura el elemento que está al frente de la cola para poder retornarlo al final.
- `a[j] = null;`: Limpia la referencia en la celda física para no dejar punteros huérfanos (_loitering_) y permitir que el recolector de basura (GC) libere la memoria.
- `j = (j + 1) % a.length;`: Mueve el puntero del frente una casilla hacia adelante de forma circular.
- `n--;`: Reduce en 1 el contador de elementos válidos de la cola.

**b. ¿Por qué `a[j] = null` no equivale a desplazar la cola?**

- Porque solo pone en `null` la celda que acabamos de desocupar. No mueve ninguno de los otros elementos en memoria. La cola "avanza" lógicamente solo porque cambiamos el valor del índice `j`, no porque hayamos corrido datos.

**c. Una llamada concreta a `remove()` puede ejecutar `resize()` en O(n). ¿Por qué se describe `remove()` como O(1) amortizado sobre una secuencia larga?**

- Porque la condición `a.length >= 3 * n` exige que se hayan realizado muchísimas eliminaciones consecutivas antes de que se dispare una reducción de arreglo. El costo $O(n)$ del copiado en `resize()` ocurre tan de vez en cuando que, repartido entre todas las operaciones previas que costaron $O(1)$ directo, el promedio por operación sigue siendo $O(1)$.

---

## 8. Traza integradora de ArrayQueue (10 min)

Estado inicial:

```text
a = [30, _, 10, 20]
a.length = 4
j = 2
n = 3
secuencia lógica = [10, 20, 30]
```

Se ejecuta: `add(40), remove(), add(50), add(60), add(70)`.

|   Operación    |        Vista lógica        |  j  |  n  | Capacidad |
| :------------: | :------------------------: | :-: | :-: | :-------: |
|  **Inicial**   |       `[10, 20, 30]`       |  2  |  3  |     4     |
| **`add(40)`**  |     `[10, 20, 30, 40]`     |  2  |  4  |     4     |
| **`remove()`** |       `[20, 30, 40]`       |  3  |  3  |     4     |
| **`add(50)`**  |     `[20, 30, 40, 50]`     |  3  |  4  |     4     |
| **`add(60)`**  |   `[20, 30, 40, 50, 60]`   |  0  |  5  |     8     |
| **`add(70)`**  | `[20, 30, 40, 50, 60, 70]` |  0  |  6  |     8     |

_Detalle de la traza:_

1. `add(40)`: entra en `(2 + 3) % 4 = 1`. Arreglo: `[30, 40, 10, 20]`. $n=4$.
2. `remove()`: saca `10` de `a[2]`, `a[2] = null`. $j$ pasa a `(2 + 1) % 4 = 3$. $n=3$.
3. `add(50)`: entra en `(3 + 3) % 4 = 2`. Arreglo: `[30, 40, 50, 20]`. $n=4$.
4. `add(60)`: como $n+1 = 5 > 4$, ocurre `resize()`. Se duplica capacidad a $\max(1, 2 \times 4) = 8$. Se copian en orden lógico `[20, 30, 40, 50]` a `b[0..3]`. $j$ pasa a 0. Luego entra el `60` en `(0 + 4) % 8 = 4`. $n = 5$. Arreglo: `[20, 30, 40, 50, 60, _, _, _]`.
5. `add(70)`: entra en `(0 + 5) % 8 = 5`. $n = 6$. Arreglo: `[20, 30, 40, 50, 60, 70, _, _]`.

**a. ¿En qué operación se ejecuta resize() y qué valores se copian, en qué orden, al nuevo arreglo?**

- Se ejecuta en **`add(60)`**, ya que el arreglo estaba lleno ($n = 4$, capacidad 4).
- Se copian en orden lógico: **`20, 30, 40, 50`** en las posiciones físicas `0, 1, 2, 3` del nuevo arreglo de tamaño 8.

**b. Indica una propiedad que permita comprobar que la vista lógica se preservó aunque cambió la distribución física.**

- Para todo $0 \le k < n$, la expresión `a[(j + k) % a.length]` evalúa exactamente a los mismos elementos antes y después del `resize()`. Es decir, la llamada a `logicalView()` produce la misma secuencia independientemente de cómo estén distribuidos físicamente en memoria o de cuál sea el valor de $j$.

---

## 9. Stack, Queue y Deque como ADT (6 min)

Completa la comparación.

| ADT       | Dónde entra    | Dónde sale          | Política / idea                          | Representación natural del curso                     |
| :-------- | :------------- | :------------------ | :--------------------------------------- | :--------------------------------------------------- |
| **Stack** | Tope (`head`)  | Tope (`head`)       | LIFO (_Last-In, First-Out_)              | `SLList` operando solo en `head`                     |
| **Queue** | Final          | Frente (`head`/`j`) | FIFO (_First-In, First-Out_)             | `ArrayQueue` circular o `SLList` con `head` y `tail` |
| **Deque** | Frente y Final | Frente y Final      | Operaciones por ambos extremos en $O(1)$ | `DLList` con nodo centinela `dummy`                  |

**a. ¿Por qué una DLList con dummy resulta una representación natural para un deque?**

- Porque un Deque necesita insertar y retirar en ambos extremos en $O(1)$. En una `DLList` circular con centinela `dummy`, el frente es `dummy.next` y el fondo es `dummy.prev`. Gracias a los enlaces dobles (`next` y `prev`), podemos descolgar o insertar nodos en cualquiera de los dos extremos en tiempo constante sin bucles y sin casos especiales (`null`).

**b. Resume la diferencia entre el ADT y la representación concreta que lo implementa.**

- El **ADT** es la especificación del comportamiento observable ("el qué"): qué operaciones existen y qué reglas siguen (como FIFO o LIFO). La **representación concreta** es la estructura real de memoria ("el cómo"): arreglos con aritmética modular o nodos enlazados con referencias, que definen el consumo de memoria y la complejidad temporal ($O(1)$ vs $O(n)$) de cada operación.

---

## 10. Verificación por propiedades y síntesis (6 min)

Completa o explica las siguientes propiedades:

- LinkedStack vacía: `n == 0 -> head == null`
- `push(x); pop()`: `pop retorna x y el tamaño vuelve al valor original / previo`
- `ArrayQueue.resize()`: `preserva la secuencia / orden lógico y n; después j == 0`
- `ArrayQueue.remove()`: `retorna el antiguo frente (a[j] previo) y reduce n en 1`
- Invariante circular para $0 \le k < n$: `lógico[k] == a[(j + k) % a.length]`

**a. ¿Por qué un resultado correcto de `reverse()` o `logicalView()` no demuestra por sí solo que todos los invariantes estén preservados?**

- Porque solo verifican los datos superficiales de salida (_smoke test_). No revisan el estado interno: por ejemplo, no detectan si olvidamos limpiar referencias huérfanas (`a[j] = null`), si `n` quedó descuadrado respecto al conteo real de nodos, o si un puntero interno quedó apuntando a una posición inválida que explotará en una operación futura.

---

### Síntesis rápida:

- `Stack -> LIFO`
- `Queue -> FIFO`
- `LinkedStack push/pop -> O(1)`
- `ArrayQueue add/remove -> O(1) amortizado`
- `j` separa índice **físico** de índice **lógico**.

---

## Antes de entregar

- [x] Puedo explicar LIFO y FIFO como comportamiento del ADT.
- [x] Puedo justificar por qué LinkedStack usa head como tope y push/pop son O(1).
- [x] Puedo distinguir índice lógico de índice físico en ArrayQueue.
- [x] Puedo usar (j+k) % a.length y explicar el invariante circular.
- [x] Puedo explicar por qué resize() copia en orden lógico y fija j=0.
- [x] Puedo distinguir el costo de una llamada con resize del costo amortizado.
- [x] Puedo usar invariantes y casos frontera como propiedades de verificación.
- [x] Puedo relacionar Stack, Queue y Deque con representaciones apropiadas.
