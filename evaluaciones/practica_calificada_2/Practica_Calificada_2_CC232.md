# CC232 - Algoritmos y Estructuras de Datos
## Práctica Calificada 2 --- Semanas 1 a 4
**Universidad Nacional de Ingeniería --- Facultad de Ciencias --- Ciclo 2026-2**  
**Cátedra:** Prof. César Lara Ávila

| Parámetro | Detalle |
| :--- | :--- |
| **Duración** | 150 minutos |
| **Puntaje** | 20 puntos |
| **Modalidad** | Individual, presencial, lápiz y papel |
| **Alcance** | Semanas 1 a 4 (Arreglos dinámicos, Listas enlazadas, Pilas/Colas, BST) |

---

### Indicaciones

- **Regla general de calificación:** esta regla se aplica a todos los problemas e incisos y no se repetirá en cada uno. Si la respuesta omite la evidencia solicitada, el inciso obtiene 0. Esto incluye, según corresponda, resultados o complejidades sin justificación, gráficos sin explicar qué representan y por qué son válidos, pseudocódigo o Java simplificado sin explicación de los pasos, explicación algorítmica sin el pseudocódigo solicitado, y conceptos técnicos mencionados sin relacionarlos con el estado y las operaciones concretas del problema.
- Cuando indiques una complejidad, explica qué trabajo depende de $n$ o de $h$ y **separa localización de modificación** cuando corresponda.
- Antes de modificar una estructura, conserva las referencias necesarias y verifica los invariantes al terminar. No uses Java Collections.

---

### Distribución sugerida del tiempo

| Problema | Tema | Tiempo sugerido |
| :--- | :--- | :---: |
| **Problema 1** | Semanas 1 y 2 (Arreglo dinámico vs DLList) | 25 min |
| **Problema 2** | Semana 3 (ArrayQueue circular y LinkedStack) | 45 min |
| **Problema 3** | Semana 4 (BST: invariante global, búsqueda y lowerBound) | 45 min |
| **Problema 4** | Semanas 3 y 4 (Interrelación Queue $\to$ BST) | 25 min |
| **Revisión** | Comprobación de estados e invariantes | 10 min |
| **Total** | | **150 min** |

> **Criterio general:** Se evalúa la coherencia entre ADT, representación, algoritmo, invariantes y complejidad, de acuerdo con la regla general de calificación indicada al inicio.

---

## Problema 1. Secuencia editable con dos representaciones --- 4 puntos

**Semanas 1 y 2.** Un editor mantiene la secuencia lógica `A, B, C, D, E, F`. Se comparan dos implementaciones del mismo comportamiento:

- **Arreglo dinámico:**  
  `a = [A, B, C, D, E, F, _, _]`, `n = 6`, `a.length = 8`, `currentIndex = 2`  
  *(la posición física 2 contiene el elemento 'C' con el cursor actual).*
- **DLList circular:**  
  `dummy <-> A <-> B <-> C <-> D <-> E <-> F <-> dummy`  
  `current = nodo C`

Se ejecutan, en este orden:
1. `insertAfterCurrent(X)` `// current sigue siendo C`
2. `current` avanza dos posiciones
3. `removeCurrent()` `// current pasa al sucesor del nodo eliminado`
4. `get(4)`

### Incisos:

- **a) (1,0 puntos)** Traza las cuatro operaciones en ambas representaciones. Muestra el estado final, la posición o referencia de `current` y el valor obtenido por `get(4)`.
- **b) (1,0 puntos)** Cuenta el trabajo estructural concreto: elementos desplazados en el arreglo, referencias modificadas en la DLList y nodos recorridos cuando corresponda. Separa localizar de modificar.
- **c) (1,0 puntos)** Decide qué representación preferirías si el $80\%$ de las operaciones fueran locales alrededor de `current` y qué representación preferirías si el $80\%$ fueran `get(i)` para índices arbitrarios. Justifica con el patrón de operaciones, no con una frase memorizada.
- **d) (1,0 puntos)** Escribe un invariante relevante del arreglo dinámico y dos invariantes de la DLList circular. Explica qué error concreto detectaría cada uno después de esta historia.

---

## Problema 2. Planificador con cola circular y registro de procesos terminados --- 6 puntos

**Semana 3.** El planificador mantiene procesos listos en una `ArrayQueue` circular. Los procesos que terminan se registran en una `LinkedStack`.

**Estado inicial de la cola ready (`ArrayQueue`):**
```text
índice:      0     1     2     3     4     5     6     7
a:         [P4]  [P5]  [P6]  [ _]  [ _]  [P1]  [P2]  [P3]
```
- `a.length = 8`, `j = 5`, `n = 6`
- **Vista lógica:** `[P1, P2, P3, P4, P5, P6]`
- **Pila finished (`LinkedStack`):** `head = null`, `n = 0`

**Eventos ejecutados:**
1. `P1` recibe CPU y **NO** termina: sale del frente y vuelve al final.
2. Llega `P7`.
3. `P2` recibe CPU y **TERMINA**: sale de ready y se apila en `finished`.
4. Llega `P8`.
5. Llega `P9`.
6. Llega `P10`, esta inserción obliga a crecer `ready`.

### Incisos:

- **a) (1,5 puntos)** Realiza la traza. Muestra los estados de control suficientes para justificar `j`, `n`, vista lógica y posición física modificada. Dibuja obligatoriamente el estado justo antes del resize, justo después del resize y después de insertar `P10`.
- **b) (1,25 puntos)** Un estudiante implementa resize con `b[k] = a[k]`. Usando el estado real anterior al crecimiento, identifica el primer valor lógico que queda mal copiado, explica por qué se rompe FIFO y escribe la expresión correcta para copiar el elemento lógico `k`.
- **c) (1,25 puntos)** Diseña `executeQuantum(ready, finished, terminates)`. Debe retirar el frente, reinsertarlo al final si no termina y apilarlo en `finished` si termina. Explica primero los pasos y después escribe pseudocódigo o Java simplificado.
- **d) (1,0 puntos)** Usando el estado anterior al resize, verifica para tres valores distintos de `k` la relación `a[(j + k) % a.length]`. Explica qué significa `k` y qué significa el índice físico obtenido.
- **e) (1,0 puntos)** Justifica los costos de `LinkedStack.push`, `LinkedStack.pop`, `ArrayQueue.remove`, `ArrayQueue.add` y `ArrayQueue.resize`. Distingue $O(1)$, $O(n)$ y $O(1)$ amortizado.

---

## Problema 3. BST: invariante global, búsqueda y una operación nueva --- 6 puntos

**Semana 4.** Un índice en memoria utiliza un BST simple, sin balanceo automático. Inserta, en este orden:
```text
52, 28, 81, 14, 39, 67, 93, 33, 45, 61, 72
```
Después se intenta `add(70)` y luego `add(39)`.

### Incisos:

- **a) (1,5 puntos)** Dibuja el árbol final. Traza `findLast(70)` antes de insertar 70, indica cada comparación y decisión, señala `parent` de 70 y explica por qué el segundo `add(39)` no modifica `n`.
- **b) (1,0 puntos)** Considera ahora este árbol distinto:
  ```text
        40
       /  \
      20  60
       \
        50
  ```
  Cada relación padre-hijo mostrada parece localmente razonable para 20 y 50, pero el árbol no es un BST válido. Identifica la violación y explica por qué comprobar solo cada padre con sus hijos inmediatos no verifica el invariante global.
- **c) (2,0 puntos)** Se desea agregar al BST una operación que no ha sido implementada en clase:
  ```java
  Node lowerBound(int x) // retorna la menor clave del árbol que sea >= x, o null si no existe
  ```
  Diseña `lowerBound` sin ejecutar `inorder` completo ni almacenar todas las claves. Explica qué información debes recordar mientras sigues un único camino de búsqueda, escribe pseudocódigo y prueba tu algoritmo para $x = 68$, $x = 73$ y $x = 94$ sobre el árbol del inciso a).
- **d) (1,5 puntos)** Justifica por qué `findLast`, `contains`, `add` y `lowerBound` cuestan $O(h)$. Compara el mismo conjunto de claves en un BST de poca altura y en uno degenerado. Explica por qué no debes escribir automáticamente $O(\log n)$.

---

## Problema 4. Cuando un error de Queue cambia el costo futuro de un BST --- 4 puntos

**Semanas 3 y 4.** Una `ArrayQueue` contiene exactamente ocho claves que luego se retirarán por FIFO e insertarán, en ese mismo orden, en un BST vacío.

**Estado inicial de la cola:**
```text
índice:      0     1     2     3     4     5     6     7
a:         [60]  [10]  [30]  [50]  [70]  [80]  [40]  [20]
```
- `a.length = 8`, `j = 6`, `n = 8`

### Incisos:

- **a) (1,0 puntos)** Reconstruye la vista lógica completa de la cola. Para cada posición lógica $k$ indica el índice físico `(j + k) % a.length`.
- **b) (1,0 puntos)** Retira las claves correctamente por FIFO e insértalas en un BST vacío. Dibuja el BST resultante y determina su altura usando una convención explícita: número de aristas del camino raíz-hoja más largo.
- **c) (1,5 puntos)** Supón que antes de drenar la cola un `resize` defectuoso copia `b[k] = a[k]` y luego fija `j = 0`. Escribe el orden que ahora observaría la cola, construye el BST que produciría ese orden y compara su altura con la del inciso b).
- **d) (0,5 puntos)** Explica por qué este caso no es solo un error de representación de Queue: las mismas claves pueden terminar en un BST con forma y costo de búsqueda distintos. Relaciona representación, historia de inserción y $O(h)$.
