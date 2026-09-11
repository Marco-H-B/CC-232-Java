# CC232 - Algoritmos y Estructuras de Datos

## Control 1 - Semanas 1 y 2

> **Institución:** Universidad Nacional de Ingeniería (UNI) — Facultad de Ciencias  
> **Asignatura:** Algoritmos y Estructuras de Datos (CC232) — Ciclo 2026-2  
> **Profesor:** César Lara Avila  
> **Alumno:** Marco Antonio Huamani Bonifacio (`20232741D`)  
> **Duración:** 60 minutos | **Puntaje:** 8 puntos | **Modalidad:** Individual, presencial, en papel

---

### Indicaciones

_No uses computadora, celular, Internet ni asistentes de inteligencia artificial. Razona a partir de la representación. Toda complejidad debe justificarse indicando qué trabajo depende de $n$. No se exige sintaxis Java perfecta cuando la lógica, los contratos y los invariantes son correctos._

**Distribución:**

- **Problema 1:** 3,0 puntos
- **Problema 2:** 3,0 puntos
- **Problema 3:** 2,0 puntos
- **Total:** 8,0 puntos

---

## Problema 1. La misma operación, dos representaciones (3 puntos)

Dos estructuras representan inicialmente la misma secuencia lógica:

```text
Representación A, arreglo dinámico
a = [4, 9, 2, 7]
n = 4
a.length = 4

Representación B, SLList
head -> 4 -> 9 -> 2 -> 7 -> null
                       ^
                       |
                      tail
n = 4
```

En ambas estructuras se ejecuta una única operación abstracta, `add(5)` al final.

---

### a) Dibuja el estado final completo de A y B. En A indica $n$ y la nueva capacidad. En B indica `head`, `tail` y $n$. _(0,75 puntos)_

#### Representación A (Arreglo dinámico)

Al inicio $n = 4$ y $a.\text{length} = 4$, el arreglo está lleno. Con crecimiento geométrico duplica su capacidad a 8, copia los 4 elementos anteriores y coloca el `5` en la primera posición libre ($a[4]$).

```text
a = [ 4 ,  9 ,  2 ,  7 ,  5 ,  _ ,  _ ,  _ ]
      0    1    2    3    4    5    6    7

n = 5
a.length = 8  (Nueva capacidad: 8)
```

#### Representación B (SLList con tail)

Se crea un nuevo nodo con valor 5 (`u = Node(5)` con `u.next = null`). El antiguo `tail` apunta al nuevo nodo (`tail.next = u`), `tail` pasa a ser `u` y $n$ aumenta en 1.

```text
head                                      tail
 |                                         |
 v                                         v
[4 | *] -> [9 | *] -> [2 | *] -> [7 | *] -> [5 | null]

n = 5
```

---

### b) Describe el trabajo interno que debe realizar cada representación para completar esa única llamada. No escribas el método completo. _(0,75 puntos)_

#### Representación A:

1. **Comprobar capacidad:** Como $n = a.\text{length}$, no hay espacio libre y por eso llama a `resize()`.
2. **Crear nuevo arreglo:** Reserva en heap un nuevo arreglo con el doble de capacidad ($2 \times a.\text{length} = 8$).
3. **Copiar elementos:** Copia los $n = 4$ elementos del arreglo viejo al nuevo ($b[i] = a[i]$ para $0 \le i < 4$).
4. **Cambiar referencia:** Hace $a = b$ para que $a$ apunte al nuevo arreglo (el viejo queda libre para el recolector de basura).
5. **Escribir el elemento:** Coloca el valor en la primera posición libre: $a[n] = 5$ (es decir, $a[4] = 5$).
6. **Actualizar tamaño:** Incrementa el tamaño lógico: $n++$ ($n = 5$).

#### Representación B:

1. **Crear nodo:** Reserva memoria para un nuevo objeto `Node(5)` con `next = null`.
2. **Conectar con el último:** Hace que el antiguo `tail` apunte al nuevo nodo: `tail.next = u`.
3. **Actualizar tail:** Redirige la referencia `tail` para que apunte al nuevo nodo: `tail = u`.
4. **Actualizar tamaño:** Incrementa el tamaño lógico: $n++$ ($n = 5$).

---

### c) Compara el costo de esta llamada concreta en A y B y justifica qué trabajo depende de $n$. _(0,50 puntos)_

- **En la Representación A:** El costo de esta llamada concreta es **$\Theta(n)$** ($O(n)$ o lineal).
  - _Justificación:_ Como el arreglo estaba lleno ($n = a.\text{length}$), esta llamada obligó a ejecutar `resize()`. El trabajo que depende de $n$ es la **copia de los $n$ elementos** del arreglo viejo al nuevo ($n$ lecturas y $n$ escrituras).
- **En la Representación B:** El costo de esta llamada concreta es **$\Theta(1)$** ($O(1)$ o constante).
  - _Justificación:_ Como se guarda la referencia directa a `tail`, no se necesita recorrer la lista. Solo se crea un nodo, se actualizan dos referencias (`tail.next = u` y `tail = u`) e incrementa $n$. Es una cantidad fija de instrucciones que no depende del tamaño $n$.

---

### d) Explica por qué, a pesar del costo de esta llamada en A, agregar al final de un arreglo dinámico con crecimiento geométrico se describe como $O(1)$ amortizado. _(0,50 puntos)_

_(Teoría de Lectura 1 - Costo amortizado y crecimiento geométrico):_

1. Con crecimiento geométrico (factor de duplicación $c = 2$), las operaciones costosas de `resize()` ocurren muy rara vez: solo cuando el tamaño alcanza una potencia de 2 ($n = 1, 2, 4, 8, 16, \dots$).
2. Al duplicar la capacidad a $2n$, se obtienen $n$ posiciones libres consecutivas. Esto garantiza que las siguientes $n$ operaciones `add` al final serán baratas y tomarán tiempo constante estricto $O(1)$ (escritura directa en $a[n]$ sin redimensionar).
3. **Análisis sobre una secuencia:** Si consideramos una secuencia de $m$ inserciones desde un arreglo vacío, el total de elementos copiados en todos los `resize()` sigue la suma:
   $$1 + 2 + 4 + 8 + \dots + 2^{\lfloor \log_2 m \rfloor} < 2m = O(m)$$
   Sumando las $m$ escrituras directas, el trabajo total es $O(m)$.
4. Al repartir el costo total entre las $m$ operaciones:
   $$\text{Costo amortizado} = \frac{O(m)}{m} = O(1)$$
   No es un promedio probabilístico sobre datos al azar, sino una garantía sobre el costo total de una secuencia de operaciones.

---

### e) Escribe un invariante relevante para A, uno para B y una postcondición común de `add(5)` que ambas representaciones deben satisfacer. _(0,50 puntos)_

- **Invariante para A (Arreglo dinámico - Lectura 1):**
  $$0 \le n \le a.\text{length}$$
  y los elementos válidos de la secuencia ocupan las posiciones contiguas $a[0..n-1]$ sin huecos intermedios.

- **Invariante para B (SLList - Lectura 2):**
  $$\text{Si } n > 0 \implies head \neq null \land tail \neq null \land tail.next == null$$
  y partiendo desde `head` y siguiendo $n-1$ enlaces `.next` se llega exactamente a `tail`.

- **Postcondición común de `add(5)`:**
  $$n_{\text{final}} = n_{\text{inicial}} + 1 \quad \land \quad \text{get}(n_{\text{final}} - 1) == 5 \quad \land \quad \forall i \in [0, n_{\text{inicial}} - 1], \; \text{get}_{\text{final}}(i) == \text{get}_{\text{inicial}}(i)$$
  _(El tamaño lógico aumenta en 1, el último elemento es 5, y los elementos anteriores conservan su valor y su orden original)._

---

## Problema 2. Código que compila, referencias que importan (3 puntos)

En una DLList se quiere insertar un nodo nuevo `u` inmediatamente después de un nodo conocido `w`. Ambos fragmentos compilan.

```java
// Versión A
Node u = new Node(x);
u.prev = w;
u.next = w.next;
w.next = u;
u.next.prev = u;
n++;
```

```java
// Versión B
Node u = new Node(x);
u.prev = w;
w.next = u;
u.next = w.next;
u.next.prev = u;
n++;
```

Usa este estado para razonar:

```text
dummy <-> 10 <-> 20 <-> 30 <-> dummy
                 ^
                 |
                 w
```

---

### a) Determina cuál versión es correcta y explica qué referencia debe conservarse antes de modificar `w.next`. _(1,0 punto)_

- **Versión correcta:** La **Versión A**.
- **Explicación:**
  - En una lista enlazada, la única forma de acceder al sucesor de $w$ (el nodo `30`) es a través de `w.next`.
  - Como indica la Lectura 2 (Sección 22: _El orden de actualización de referencias importa_), **antes de sobrescribir `w.next` se debe conservar la referencia al nodo que le sigue**.
  - En la **Versión A**, la línea `u.next = w.next;` se ejecuta **antes** de modificar `w.next`. Así, `u` ya guarda la dirección del nodo `30`. Luego se puede hacer `w.next = u;` con seguridad, y finalmente `u.next.prev = u;` usa esa referencia guardada para conectar el nodo `30` hacia atrás con `u`.
  - En cambio, en la **Versión B** se hace primero `w.next = u;`. Eso sobrescribe de inmediato `w.next` y destruye la única referencia que permitía llegar al nodo `30`.

---

### b) Traza la versión incorrecta hasta el primer estado en el que la estructura deseada ya no puede obtenerse con esas instrucciones. Dibuja las referencias y señala un invariante violado. _(1,0 punto)_

#### 1. Primer estado irreversible (dónde se arruina todo)

Ocurre exactamente al ejecutar la **línea 3** de la Versión B:

```java
w.next = u;
```

**¿Por qué es el punto crítico?**
Antes de esa asignación, la única referencia al nodo `30` estaba en `w.next`. Como nadie guardó esa dirección antes (ni en `u.next` ni en una variable auxiliar), al ejecutar `w.next = u;` **se pierde para siempre el acceso al nodo `30` y al resto de la lista hacia adelante**. Con las instrucciones que quedan en el código ya es imposible reconectar la lista deseada.

#### 2. Traza paso a paso de la Versión B

Partiendo del estado inicial donde $w$ es el nodo `20` y su siguiente original es `30`:

```java
Node u = new Node(x); // 1. Se crea u en heap con valor x
u.prev = w;           // 2. u.prev apunta al nodo 20 (w)
w.next = u;           // 3. ¡PUNTO IRREVERSIBLE! w.next apunta a u. Se pierde el nodo 30
u.next = w.next;      // 4. Como w.next ya es u, evalúa: u.next = u (¡u se apunta a sí mismo!)
u.next.prev = u;      // 5. Como u.next es u, equivale a: u.prev = u (¡u.prev se apunta a sí mismo!)
n++;                  // 6. n aumenta, pero la lista está rota
```

- **Paso 1 (`Node u = new Node(x)`):** Se crea el nodo `u` en heap.
- **Paso 2 (`u.prev = w`):** `u.prev` apunta a `20` ($w$). Hasta aquí todo va bien.
- **Paso 3 (`w.next = u`):** ⚠️ **[ESTADO IRREVERSIBLE]** El enlace `20.next` deja de apuntar a `30` y pasa a apuntar a `u`. La dirección del nodo `30` se destruyó y ya no se puede recuperar.
- **Paso 4 (`u.next = w.next`):** Como en el paso 3 `w.next` pasó a ser `u`, esta sentencia ejecuta:
  $$\text{u.next} = \text{w.next} \implies \mathbf{u.next = u}$$
  **¡El nodo `u` queda apuntando a sí mismo hacia adelante (`u.next == u`)!**
- **Paso 5 (`u.next.prev = u`):** Como `u.next` es `u`, la expresión `u.next.prev` se evalúa sobre el mismo nodo `u`. Es decir:
  $$\mathbf{u.prev = u}$$
  **¡Se sobrescribe el enlace hacia atrás `u.prev = w`! Ahora `u.prev` también se apunta a sí mismo.**
- **Paso 6 (`n++`):** Se incrementa el contador sobre una estructura rota.

#### 3. Diagrama de referencias resultante

```text
Estado inicial:
dummy <-> [10] <-> [20 (w)] <===============> [30] <-> dummy

Estado final tras ejecutar la Versión B:
                    +--------------------+
                    |                    |
                    v                    | (u.prev = u)
dummy <-> [10] <---> [20 (w)]           [ u ] <----+
                            |              ^       |
                            +------------->|       | (u.next = u)
                                           +-------+

                          [30] <-> dummy
                           |
                           +---> 30.prev sigue apuntando a 20,
                                 pero 20 ya no apunta a 30.
```

- $w$ (`20`) tiene `w.next` apuntando a `u`.
- `u` está en un ciclo cerrado sobre sí mismo: `u.next == u` y `u.prev == u`.
- El nodo `30` y el centinela `dummy` quedaron completamente desconectados de la cadena que avanza desde $w$.

#### 4. Invariante violado

Se rompen las propiedades fundamentales de una lista doble (Lectura 2):

1. **Invariante de consistencia bidireccional (Lectura 2, Sección 14):**
   Para todo nodo debe cumplirse que si avanzo por `next` y retrocedo por `prev`, vuelvo al mismo nodo:
   $$p.\text{next}.\text{prev} == p \quad \land \quad p.\text{prev}.\text{next} == p$$
   - En el nodo $w$ (`20`): su `next` es $u$ (`w.next == u`), pero `u.prev` apunta a sí mismo ($u.\text{prev} == u \neq w$). **Invariante roto.**
   - En el nodo $u$: $u.\text{next}$ es $u$, no conecta con ningún vecino lógico.

2. **Invariante de conectividad con el centinela `dummy` (Lectura 2, Sección 13):**
   Partiendo de `dummy` y avanzando por `.next`, se deben recorrer todos los nodos de la secuencia y volver a `dummy`.
   - Aquí, al llegar a $w$ y pasar a $u$, cualquier recorrido hacia adelante entra en un **bucle infinito en `u`** (`u -> u -> u...`) y jamás regresa a `dummy`.

---

### c) Escribe un fragmento correcto para insertar después de $w$ y justifica su complejidad cuando $w$ ya es conocido. _(1,0 punto)_

#### Fragmento de código en Java:

```java
Node u = new Node(x);
u.prev = w;
u.next = w.next;
u.next.prev = u; // el nodo siguiente original (30) reconoce a u como su prev
w.next = u;      // w reconoce a u como su next
n++;
```

_(Nota: El orden de la Versión A: `u.prev = w; u.next = w.next; w.next = u; u.next.prev = u;` también es correcto, porque `u.next` ya resguardó la referencia al sucesor antes de modificar `w.next`)._

#### Justificación de complejidad:

- **Costo:** **$O(1)$** (tiempo constante).
- **Justificación (Lectura 2, Sección 17 y 18 - Localizar no es lo mismo que modificar):**
  - Como el nodo $w$ ya es conocido de antemano, **el costo de localización es cero**.
  - La operación solo realiza una instanciación (`new Node(x)`), actualiza 4 referencias locales (`u.prev`, `u.next`, `u.next.prev`, `w.next`) e incrementa $n$.
  - Ninguna de estas acciones depende del número de nodos $n$ de la lista. Por tanto, la modificación local se ejecuta en tiempo estrictamente constante.

---

## Problema 3. Dos afirmaciones sobre listas (2 puntos)

Analiza cada afirmación. No basta responder verdadero o falso.

---

### a) _"Si una SLList guarda tail, agregar y eliminar desde el final cuestan O(1)"_. Evalúa la afirmación. Explica qué información proporciona tail, qué información falta para una de las dos operaciones e indica el costo correspondiente. _(1,0 punto)_

- **Evaluación:** **FALSO**.
- **Explicación:**
  - **Información que proporciona `tail`:**
    Almacena una referencia directa al último nodo de la lista. Esto es **suficiente para agregar al final** (`push_back` o `add`), porque basta hacer:
    ```java
    tail.next = u;
    tail = u;
    n++;
    ```
    lo cual toma tiempo constante **$O(1)$**.
  - **Información que falta para eliminar desde el final:**
    Para **eliminar el último nodo**, no solo hay que acceder a `tail`; es obligatorio hacer que `tail` retroceda y apunte a su **predecesor inmediato** (el penúltimo nodo) y cortar su enlace (`penúltimo.next = null`).
    Como la lista es **simplemente enlazada (SLList)**, los nodos solo tienen enlace hacia adelante (`next`) y no tienen referencia hacia atrás (`prev`) (Lectura 2, Sección 10). `tail` no sabe quién es su nodo anterior.
  - **Cómo se resuelve y costo correspondiente:**
    Para encontrar el penúltimo nodo, es indispensable empezar desde `head` y recorrer secuencialmente toda la lista enlace por enlace hasta encontrar el nodo cuyo `next` sea `tail`.
    - **Agregar al final:** **$O(1)$**.
    - **Eliminar al final:** **$O(n)$** (el costo está dominado por tener que localizar el penúltimo nodo).

---

### b) _"Como una DLList tiene prev y next, get(i) cuesta O(1)"_. Evalúa la afirmación. Explica por qué tener dos direcciones de recorrido no convierte un índice en una referencia directa al nodo e indica el costo trabajado en clase. _(1,0 punto)_

- **Evaluación:** **FALSO**.
- **Explicación:**
  - **Por qué dos direcciones no equivalen a acceso directo (Lectura 2, Sección 15 y 16):**
    - En un arreglo, los elementos ocupan posiciones físicas contiguas en memoria y un índice permite calcular directamente la dirección en tiempo $O(1)$ mediante aritmética:
      $$\text{dirección}(i) = \text{base} + i \times \text{tamaño}$$
    - En una lista enlazada (incluso en una DLList), los nodos están **dispersos en el heap**. El índice no es una referencia directa al nodo.
    - Tener `prev` y `next` permite avanzar y retroceder, pero **no elimina la necesidad de recorrer nodo por nodo**. Para llegar a la posición $i$, hay que dereferenciar punteros paso a paso.
    - Lo que sí permite la DLList es optimizar el recorrido eligiendo el extremo más cercano: si $i < n/2$, empieza desde `dummy.next` hacia adelante; si $i \ge n/2$, empieza desde `dummy.prev` hacia atrás. Pero esto solo divide el recorrido a la mitad; sigue siendo un recorrido secuencial proporcional a la distancia al borde.
  - **Costo trabajado en clase:**
    $$\mathbf{O(1 + \min(i, n - i))}$$
    En el peor caso (cuando el elemento buscado está al centro, $i \approx n/2$), se recorren $\lfloor n/2 \rfloor$ nodos, por lo que el costo sigue siendo **$O(n)$**.

---
