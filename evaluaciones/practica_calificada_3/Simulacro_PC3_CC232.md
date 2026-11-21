# CC232 - Algoritmos y Estructuras de Datos
## Práctica Calificada 3 --- Semanas 5 a 7 (Ciclo 2026-2)
**Universidad Nacional de Ingeniería --- Facultad de Ciencias**  
**Cátedra:** Prof. César Lara Ávila

| Parámetro | Detalle |
| :--- | :--- |
| **Duración** | 150 minutos |
| **Puntaje** | 20 puntos |
| **Modalidad** | Individual, presencial, lápiz y papel |
| **Alcance** | Semanas 5 a 7 (BST con parent y splice, Árboles AVL con rotaciones, BinaryHeap con heapify) |

---

### Indicaciones

- **Regla general de calificación:** Esta regla se aplica a todos los problemas e incisos y no se repetirá en cada uno. Si la respuesta omite la evidencia solicitada, el inciso obtiene 0. Esto incluye, según corresponda, resultados o complejidades sin justificación, gráficos sin explicar qué representan y por qué son válidos, pseudocódigo o Java simplificado sin explicación de los pasos, explicación algorítmica sin el pseudocódigo solicitado, y conceptos técnicos mencionados sin relacionarlos con el estado y las operaciones concretas del problema.
- Cuando indiques una complejidad, explica qué trabajo depende de $n$ o de $h$ y **separa localización de modificación** cuando corresponda.
- Antes de modificar una estructura, conserva las referencias necesarias y verifica los invariantes al terminar. No uses Java Collections.

---

### Distribución sugerida del tiempo

| Problema | Tema y estructura de datos | Puntaje | Tiempo sugerido |
| :--- | :--- | :---: | :---: |
| **Problema 1** | BST: Eliminación con `splice`, sucesor y punteros `parent` (Semana 5) | 4,0 pts | 30 min |
| **Problema 2** | AVL: Inserción, rotaciones múltiples y orden de actualización de alturas (Semana 6) | 4,0 pts | 30 min |
| **Problema 3** | AVL: Eliminación en cascada, método recursivo `deleteMax` y poda (S6, S5) | 4,0 pts | 30 min |
| **Problema 4** | BinaryHeap: `heapify` $O(n)$, colas de prioridad e invariante de forma (Semana 7) | 4,0 pts | 30 min |
| **Problema 5** | Integrador: Coexistencia, flujos ordenados y matriz de decisión (S5, S6, S7) | 4,0 pts | 25 min |
| **Revisión** | Comprobación de invariantes de enlaces y casos frontera | --- | 5 min |
| **Total** | **Evaluación completa** | **20,0 pts** | **150 min** |

> **Criterio general:** Se evalúa la coherencia entre ADT, representación, algoritmo, invariantes y complejidad, de acuerdo con la regla general de calificación indicada al inicio.

---

## Problema 1. BST: invariante de parent, eliminación con `splice` y sucesor --- 4 puntos

**Semana 5.** Un índice en memoria mantiene claves en un BST sin balanceo automático. Cada nodo posee referencias `left`, `right` y `parent`. La eliminación se realiza mediante la operación auxiliar `splice(Node u)` y el sucesor inorder:

```java
private void splice(Node u) {
    Node s = (u.left != null) ? u.left : u.right;
    Node p = u.parent;
    if (u == root) {
        root = s;
        if (s != null) {
            s.parent = null;
        }
    } else {
        if (p.left == u) {
            p.left = s;
        } else {
            p.right = s;
        }
        if (s != null) {
            s.parent = p;
        }
    }
}

boolean remove(int x) {
    Node u = findLast(x);
    if (u == null || u.x != x) {
        return false;
    }
    if (u.left == null || u.right == null) {
        splice(u);
    } else {
        Node w = u.right;
        while (w.left != null) {
            w = w.left;
        }
        u.x = w.x;
        splice(w);
    }
    n--;
    return true;
}
```

El árbol contiene inicialmente el siguiente conjunto de claves ($n = 12$):

```text
                 50
               /    \
             25      75
            /  \    /  \
          15    35 65   85
         /  \  / \   \    \
        10  20 30 40  70   90
```

Se ejecutan, en este orden: `remove(10)`, `remove(65)`, `remove(25)` y `remove(50)`.

### Incisos:

- **a) (1,5 puntos)** Realiza la traza gráfica de las cuatro operaciones. Para cada una indica: el caso de eliminación (hoja, un hijo o dos hijos), el nodo que se desvincula físicamente mediante `splice`, la sustitución de clave lógica (si aplica) y dibuja el árbol resultante tras cada llamada.
- **b) (0,75 puntos)** Para `remove(25)`, detalla la secuencia exacta de modificaciones de punteros en memoria (`parent`, `left`, `right`) y el valor copiado. Explica formalmente por qué el sucesor inorder $w$ de cualquier nodo con dos hijos nunca puede tener hijo izquierdo ($w.left == null$), pero sí hijo derecho, y cómo responde `splice(w)`.
- **c) (0,75 puntos)** Un estudiante omite la actualización de `s.parent` en el bloque `else` de `splice` (omite la comprobación `if (s != null) s.parent = p;`). Considera que se ejecuta esa versión defectuosa en `remove(65)`. ¿El recorrido inorder posterior sigue siendo correcto? Identifica qué referencia de puntero queda rota y describe con precisión técnica qué ocurrirá si después se invoca `remove(70)`.
- **d) (1,0 punto)** Separa de forma explícita el costo de localización (`findLast`) del costo de modificación (`splice` y reasignación) en función de $h$. Explica por qué es un error grave escribir automáticamente $O(\log n)$ y dibuja un árbol degenerado de $n$ nodos donde `remove` cueste $\Theta(n)$.

---

## Problema 2. AVL: los cuatro casos de rotación y orden de actualización --- 4 puntos

**Semana 6.** Un conmutador de paquetes mantiene rutas en un árbol AVL donde cada nodo tiene campos `x`, `height` ($h(\text{hoja})=1, h(\text{null})=0$), `left` y `right` (sin puntero `parent`).

Operaciones auxiliares de cátedra:
```java
int height(Node u) { return u == null ? 0 : u.height; }
int balanceFactor(Node u) { return u == null ? 0 : height(u.left) - height(u.right); }
void updateHeight(Node u) { u.height = 1 + Math.max(height(u.left), height(u.right)); }
```

Rotaciones elementales implementadas por el profesor:
```java
private Node rotateRight(Node y) {
    Node x = y.left;
    Node middle = x.right;
    x.right = y;
    y.left = middle;
    updateHeight(y); // Paso 1
    updateHeight(x); // Paso 2
    return x;
}

private Node rotateLeft(Node x) {
    Node y = x.right;
    Node middle = y.left;
    y.left = x;
    x.right = middle;
    updateHeight(x); // Paso 1
    updateHeight(y); // Paso 2
    return y;
}
```

A partir de un árbol AVL inicialmente vacío, se insertan en este orden las siguientes claves:
```text
45, 20, 10, 30, 25, 60, 80, 70
```

### Incisos:

- **a) (1,5 puntos)** Realiza la traza paso a paso de cada inserción. Para cada inserción que provoque una violación del invariante AVL:
  1. Identifica el nodo más profundo (el primero detectado en el retorno recursivo) donde $|FE| = 2$.
  2. Clasifica el caso (LL, RR, LR o RL) justificando con el factor de balance del nodo y de su hijo.
  3. Dibuja el subárbol afectado antes y después de aplicar las rotaciones simples o dobles.
  4. Dibuja el árbol completo final tras insertar las 8 claves.
- **b) (0,75 puntos)** Analiza críticamente `rotateRight(Node y)`. Explica con rigor por qué es imprescindible ejecutar `updateHeight(y)` **antes** que `updateHeight(x)`. ¿Qué valor numérico erróneo de altura adoptaría $x$ si un programador invirtiese el orden de esas dos líneas? Demuéstralo trazando un subárbol concreto.
- **c) (0,75 puntos)** Un estudiante afirma: *“Si un nodo tiene $FE = +2$, la anomalía siempre está hacia la izquierda, por lo que basta ejecutar una única `rotateRight(u)` directa sin consultar el signo de su hijo izquierdo”*. Aplica su algoritmo erróneo a la inserción de la clave `25` (caso LR): dibuja el árbol resultante, calcula sus factores de balance y demuestra que el resultado no es un AVL válido.
- **d) (0,5 puntos)** Construye una tabla con los campos `x`, `height` y `balanceFactor` para cada uno de los 8 nodos del árbol AVL final obtenido en el inciso a).
- **e) (0,5 puntos)** Sea $N(h)$ la mínima cantidad de nodos que puede contener un AVL válido de altura $h$, con $N(1) = 1, N(2) = 2$ y $N(h) = 1 + N(h-1) + N(h-2)$ para $h \ge 3$. Calcula $N(h)$ hasta $h = 6$. A partir de estos valores, deduce la altura máxima posible de un AVL con $n = 8$ nodos y compárala con la altura máxima de un BST sin balanceo con 8 nodos.

---

## Problema 3. AVL: eliminación con rebalanceo en cascada y poda en rango --- 4 puntos

**Semanas 5 y 6.** A diferencia de la inserción en un AVL (donde a lo sumo una rotación simple o doble rebalancea todo el árbol), la eliminación de un nodo puede reducir la altura de un subárbol y desencadenar desbalances sucesivos en múltiples niveles ascendentes hacia la raíz ($O(\log n)$ rotaciones).

Considere el siguiente árbol AVL balanceado ($n = 11$, con $h = 4$):

```text
                 50
               /    \
             30      80
            /  \    /  \
          20    40 70   90
         /     /  / \     \
        10    35 60  75    95
```

### Incisos:

- **a) (1,25 puntos)** Sobre el árbol dado, se ejecuta la eliminación de la clave `10` (`remove(10)`):
  1. Realiza la traza del retroceso recursivo actualizando alturas y factores de balance desde el punto de desvinculación.
  2. Identifica el primer nodo donde se produce desbalance, clasifica el caso (LL, RR, LR o RL) y describe la rotación ejecutada.
  3. Muestra si dicho rebalanceo local propaga o no un desbalance a sus ancestros superiores (incluyendo la raíz).
  4. Dibuja el árbol balanceado final. Explica por qué, en el peor caso, la eliminación en AVL es $O(\log n)$ en cantidad de rotaciones mientras que la inserción es $O(1)$.
- **b) (1,25 puntos)** Siguiendo la modalidad de las prácticas calificadas, diseña en **pseudocódigo riguroso** (o Java simplificado) el método recursivo:
  ```java
  Node deleteMax(Node u)
  ```
  Explica primero la estrategia algorítmica: cómo localizas el máximo descendiendo hasta una hoja o nodo con a lo sumo un hijo izquierdo, cómo desconectas y devuelves el subárbol hijo, cómo actualizas alturas con `updateHeight` y cómo aplicas `rebalance(u)` durante el desapilado recursivo. Especifica formalmente precondición y postcondición.
- **c) (0,75 puntos)** Demuestra formalmente el costo temporal en el peor caso de `deleteMax(Node u)` en función de $n$. Explica por qué el diseño recursivo no requiere punteros al padre (`parent`) para restaurar el balance.
- **d) (0,75 puntos)** Se requiere una operación `int countInRange(Node u, int low, int high)` que retorne la cantidad de claves del AVL dentro del intervalo cerrado $[low, high]$. Diseña en **pseudocódigo riguroso** el algoritmo con poda inteligente: no desciendas a subárboles que no puedan contener elementos dentro del rango. Explica con precisión técnica qué ramas descartas y demuestra por qué su costo en el mejor caso es $O(\log n)$ a diferencia del $O(n)$ de un recorrido inorder ciego.

---

## Problema 4. BinaryHeap: `heapify` $O(n)$, operaciones y forma --- 4 puntos

**Semana 7.** Un sistema de triaje hospitalario gestiona pacientes con un `BinaryHeap` (Min-Heap: menor valor numérico representa mayor gravedad). Se implementa sobre un arreglo secuencial `Integer[] a` y un contador `n`.

Relaciones base 0: $\text{left}(i) = 2i + 1$, $\text{right}(i) = 2i + 2$, $\text{parent}(i) = \lfloor (i - 1)/2 \rfloor$.  
Invariantes: (1) **Orden:** $a[\text{parent}(i)] \le a[i]$; (2) **Forma:** elementos contiguos en $a[0..n-1]$.

Operaciones de cátedra:
```java
private void trickleDown(int i) {
    while (true) {
        int smallest = i;
        int l = left(i), r = right(i);
        if (l < n && a[l] < a[smallest]) smallest = l;
        if (r < n && a[r] < a[smallest]) smallest = r;
        if (smallest == i) return;
        swap(i, smallest);
        i = smallest;
    }
}

private void heapify() {
    for (int i = n / 2 - 1; i >= 0; i--) {
        trickleDown(i);
    }
}

int remove() {
    if (n == 0) throw new NoSuchElementException();
    int min = a[0];
    a[0] = a[--n];
    a[n] = null;
    if (n > 0) trickleDown(0);
    return min;
}
```

Se recibe el siguiente arreglo desordenado de $n = 10$ prioridades:
```text
índice:   0   1   2   3   4   5   6   7   8   9
a:      [22, 14, 35, 10,  8, 28, 40,  5, 12,  6]       n = 10, a.length = 16
```

### Incisos:

- **a) (1,5 puntos)** Ejecuta la construcción del heap mediante el algoritmo de Floyd: `heapify()`.
  1. Indica el índice del primer nodo interno evaluado ($i = \lfloor n/2 \rfloor - 1$) y explica por qué se omiten los nodos con índices mayores.
  2. Detalla para cada valor de $i$ (desde $\lfloor n/2 \rfloor - 1$ hasta $0$): los hijos comparados, los intercambios (`swap`) efectuados por `trickleDown(i)` y el contenido del arreglo tras finalizar cada paso.
  3. Dibuja el árbol binario casi completo final y cuenta la cantidad exacta de intercambios ejecutados.
- **b) (1,0 punto)** Sobre el Min-Heap obtenido en el inciso a), ejecuta en este orden:
  1. `remove()`: extracción del paciente más prioritario.
  2. `remove()`: extracción del segundo paciente.
  3. `add(4)`: ingreso de una nueva emergencia con prioridad 4.
  Para cada operación, indica los elementos comparados, la trayectoria de desplazamiento (`trickleDown` o `bubbleUp`), el valor retornado (en `remove`) y el estado físico final del arreglo con su nuevo valor de `n`.
- **c) (0,75 puntos)** Demuestra analíticamente por qué `heapify()` construye el montículo en tiempo $O(n)$, mientras que insertar los $n$ elementos uno tras otro con llamadas a `add(x)` requiere $O(n \log n)$ en el peor caso. Formula la sumatoria por niveles y demuestra su convergencia asintótica:
  $$T(n) = \sum_{k=0}^{\lfloor \log_2 n \rfloor} \left\lceil \frac{n}{2^{k+1}} \right\rceil O(k) \le O(n)$$
- **d) (0,75 puntos)** Un practicante propone optimizar `remove()` ahorrándose subir el último elemento a la raíz: sugiere reemplazar la raíz vacía por el menor de sus hijos ($a[0] = \min(a[1], a[2])$) y repetir ese corrimiento hacia abajo hasta dejar un hueco en una hoja. Explica detalladamente qué invariante de estructura se destruye con esta propuesta y demuestra por qué las fórmulas $2i+1$ y $2i+2$ dejarían de mapear los nodos correctamente en operaciones futuras.

---

## Problema 5. Cuando un flujo ordenado de Heap degrada un BST y por qué AVL lo salva --- 4 puntos

**Semanas 5, 6 y 7.** En un sistema de analítica de eventos, se procesan datos dinámicos comparando el comportamiento de un **BST sin balanceo**, un **Árbol AVL** y un **BinaryHeap (Min-Heap)**.

### Incisos:

- **a) (1,25 puntos)** Degradación topológica en BST:  
  Supón que se tienen $n$ elementos en un Min-Heap válido. Se extraen los $n$ elementos mediante $n$ invocaciones sucesivas a `remove()`, y a medida que salen, cada elemento se inserta de inmediato con `add(x)` en un árbol BST (inicialmente vacío) sin balanceo automático.
  1. ¿En qué orden numérico relativo emergen los elementos desde el Min-Heap?
  2. Describe con precisión la topología del árbol BST resultante (forma geométrica y ramificación).
  3. Indica la altura exacta $h(n)$ que alcanzará dicho BST en términos de $n$.
  4. ¿A qué complejidad asintótica degeneran `contains(x)` y `remove(x)` sobre este BST? Explica por qué este escenario representa el peor caso absoluto para un BST.
- **b) (1,0 punto)** Inmunidad y rebalanceo sistemático en AVL:  
  Si la misma secuencia de elementos extraídos del Min-Heap se inserta consecutivamente en un árbol AVL inicialmente vacío:
  1. ¿Qué tipo específico de desbalance (LL, RR, LR o RL) se detectará repetitivamente durante la inserción?
  2. ¿Qué rotación elemental resuelve cada uno de estos desbalances?
  3. Demuestra la cota superior estricta de la altura del AVL resultante y explica cómo el rebalanceo neutraliza la degradación observada en el BST.
- **c) (0,75 puntos)** La falacia de la búsqueda en Heap vs AVL:  
  Un ingeniero afirma: *“Dado que tanto el BinaryHeap como el árbol AVL son árboles con altura $O(\log n)$, la operación `contains(x)` para determinar si una clave arbitraria $x$ existe toma $O(\log n)$ en ambos casos”*. Refuta formalmente la afirmación. Explica la diferencia entre el orden parcial vertical del heap y el orden total horizontal del árbol de búsqueda. Demuestra cuál es el costo real de `contains(x)` en un BinaryHeap de $n$ elementos en el peor caso.
- **d) (1,0 punto)** Matriz de decisión de ingeniería:  
  Completa la tabla indicando la complejidad en el peor caso ($O(1)$, $O(\log n)$, $O(n)$ u $O(n \log n)$):

| Operación de ingeniería | BST sin balanceo | Árbol AVL | BinaryHeap (Min) |
| :--- | :---: | :---: | :---: |
| Construcción a partir de $n$ elementos desordenados | | | |
| Extracción del elemento mínimo (`removeMin`) | | | |
| Búsqueda de una clave arbitraria (`contains(x)`) | | | |
| Emisión de todas las claves ordenadas de menor a mayor | | | |

Fundamenta qué estructura elegirías en cada escenario:
- **Escenario Alpha:** Carga de $95\%$ inserciones y extracciones del mínimo, y $5\%$ reportes batch.
- **Escenario Beta:** Carga de $70\%$ búsquedas puntuales (`contains`) y $30\%$ consultas de rango (`countInRange`).
