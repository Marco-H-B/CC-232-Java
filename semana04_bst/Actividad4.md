# CC232 - Actividad 4 (Prueba de Entrada Asistida por IA)

> **Curso:** CC232 - Algoritmos y Estructuras de Datos  
> **Tema:** Panorama de Computación, Algoritmos y Desarrollo de Software  
> **Modalidad:** Individual y domiciliaria | **Asignación:** Semana 3 | **Entrega:** 25 de setiembre, 8:00 a.m.  
> **Presentación:** Respuestas escritas a mano en hojas | **Tiempo estimado:** 6 a 8 horas  
> **IA:** Permitida y recomendada, con registro de su uso | **Función:** Diagnóstica y formativa

---

**Nombre:** **Marco Huamani Bonifacio** | **Código:** **20232741D**

---

## 1. Propósito y Reglas de Juego

Esta prueba muestra todo el mapa de lo que abarca CC232 y cómo se conecta con el resto de las ramas de Ciencias de la Computación. La idea no es memorizar conceptos de memoria, sino saber hacer preguntas con sentido, usar herramientas de IA de forma crítica sin tragar entero, y explicar las cosas con mis propias palabras demostrando que entiendo el fondo del asunto.

---

## 4. Parte A. Panorama del recorrido de CC232

---

### A1. Complejidad

**Consigna:**  
Compara conceptualmente $O(1), O(\log n), O(n), O(n \log n), O(n^2), O(2^n)$ para $n = 10, 10^3, 10^6$. Explica por qué $O(n^2)$ puede ser razonable para una entrada pequeña y desastroso para una grande. Distingue mejor caso, caso promedio, peor caso y costo amortizado. Explica por qué Big-O no mide directamente segundos.

```text
Registro de IA (Protocolo Completo):
- Prompt usado: "Explícame con una tabla y números reales la diferencia entre O(1), O(log n), O(n), O(n log n), O(n^2) y O(2^n) para n=10, 1000 y 1 millón. Explica por qué Big-O no mide segundos en el reloj y los 4 tipos de casos de costo."
- Herramienta y fecha: Gemini / Antigravity (21/09/2026).
- Resumen de ayuda (máx 120 palabras): Me ayudó a dimensionar el salto monstruoso de operaciones. Con un millón de datos, n^2 se va a un billón de operaciones (horas de espera), mientras que n log n termina en menos de un segundo. También me dejó claro que Big-O mide cómo crece el trabajo cuando n se va al infinito, no el tiempo en el cronómetro porque eso depende de la máquina y la memoria caché.
```

**Respuesta con mis palabras:**

**1. Comparación de órdenes de magnitud:**

| Complejidad       | Para $n = 10$ |   Para $n = 10^3$ (mil)   | Para $n = 10^6$ (un millón) | Comportamiento práctico                           |
| :---------------- | :-----------: | :-----------------------: | :-------------------------: | :------------------------------------------------ |
| **$O(1)$**        |       1       |             1             |              1              | Instantáneo siempre.                              |
| **$O(\log n)$**   |  $\approx 3$  |       $\approx 10$        |        $\approx 20$         | Crece lentísimo, casi plano.                      |
| **$O(n)$**        |      10       |          $1,000$          |         $1,000,000$         | Proporcional directo (milésimas de segundo).      |
| **$O(n \log n)$** | $\approx 33$  |     $\approx 10,000$      |   $\approx 2 \times 10^7$   | Excelente para ordenar un millón en $< 1$ s.      |
| **$O(n^2)$**      |      100      |        $1,000,000$        |    $10^{12}$ (un billón)    | Para un millón tarda casi 3 horas en CPU.         |
| **$O(2^n)$**      |    $1,024$    | Incalculable ($2^{1000}$) |          Imposible          | Explota; ni con todas las computadoras del mundo. |

- **Por qué $O(n^2)$ sirve para entradas chicas pero es un desastre para grandes:**  
  Si $n = 10$ o $20$, un algoritmo $O(n^2)$ (como Insertion Sort) hace menos de 400 operaciones; corre en microsegundos y no gasta memoria extra. Pero si le metes un millón de datos, $10^{12}$ pasos colapsa cualquier servidor. Por eso para pocos datos no vale la pena complicarse con algoritmos enredados, pero para datos masivos $O(n^2)$ es inviable.

- **Diferencia entre los casos:**
  - _Mejor caso:_ El escenario ideal donde el algoritmo tiene suerte y hace el mínimo trabajo posible (ej. buscar un elemento y que esté en la primera casilla).
  - _Peor caso:_ El escenario más desfavorable; es la garantía formal de que el algoritmo nunca va a tardar más de esa cota.
  - _Caso promedio:_ Lo que esperas que ocurra en el día a día si los datos vienen repartidos al azar.
  - _Costo amortizado:_ Ocurre cuando tienes una operación que casi siempre es baratísima ($O(1)$), pero que muy de vez en cuando hace una operación pesada ($O(n)$ como el `resize` al llenarse un arreglo). Al promediar todo el costo acumulado entre todas las operaciones de una secuencia larga, cada una te sale a $O(1)$ en promedio.

- **Por qué Big-O no mide segundos directamente:**  
  Porque Big-O es una herramienta matemática que describe cómo se escala el número de operaciones cuando $n$ crece, ignorando constantes y términos menores. El tiempo real en el reloj depende del hardware físico: la frecuencia del procesador, si los datos entran en la caché L1/L2/L3, el costo de las llamadas en la JVM y las constantes ocultas (un algoritmo con $1000000 \times n$ pasos y otro con $2 \times n$ son ambos $O(n)$, pero el segundo corre 500,000 veces más rápido en segundos).

```text
- Antes pensaba... que un algoritmo O(n log n) siempre le ganaba en velocidad a uno O(n^2) sin importar el tamaño del problema.
- Ahora entiendo... que para datos pequeños las constantes y la memoria caché pesan más que la cota asintótica, y que Big-O sirve para ver cómo reacciona el sistema cuando los datos crecen a lo bestia.
- Todavía dudo... sobre cómo calcular el costo amortizado exacto por el método del potencial cuando se mezclan inserciones y borrados al azar.
```

---

### A2. Secuencias, pilas y colas

**Consigna:**  
Compara arreglo dinámico, SLList y DLList para `get(i)`, insertar al inicio y al final, insertar después de un nodo conocido, eliminar un nodo conocido y recorrer todos los elementos. Incluye tamaño, capacidad, resize, head, tail, prev, next y dummy. Relaciona deshacer, atención por orden de llegada, navegación hacia atrás, evaluación de expresiones y BFS con Stack, Queue o Deque. Pregunta central: ¿por qué ninguna representación es la mejor sin conocer el patrón de operaciones? Incluye un caso donde localizar sea más costoso que modificar.

```text
Registro de IA:
- Prompt usado: "Compara arreglo dinámico, SLList y DLList en tabla de costos con términos head, tail, prev, next, dummy, resize. Mapea casos reales (undo, cola de banco, browser history, BFS) a Stack, Queue o Deque y da un ejemplo donde localizar sea más caro que modificar."
- Herramienta y fecha: Gemini / Antigravity (21/09/2026).
- Resumen de ayuda: Me aclaró el contraste entre localizar y modificar: en una lista doble con nodo en mano desconectas en O(1), pero caminar hasta el nodo te cuesta O(n). Y que ninguna estructura es perfecta para todo.
```

**Respuesta con mis palabras:**

**1. Tabla comparativa:**

| Operación                   |    Arreglo dinámico     |       SLList (Simple)        |   DLList (Doble con dummy)    |
| :-------------------------- | :---------------------: | :--------------------------: | :---------------------------: |
| `get(i)`                    |   **$O(1)$ directo**    |  $O(n)$ (recorre de `head`)  |     $O(1 + \min(i, n-i))$     |
| Insertar al inicio          | $O(n)$ (desplaza todo)  |          **$O(1)$**          |          **$O(1)$**           |
| Insertar al final           |  **$O(1)$ amortizado**  |   **$O(1)$** (con `tail`)    | **$O(1)$** (con `dummy.prev`) |
| Insertar tras nodo conocido | $O(n)$ (desplaza resto) |          **$O(1)$**          |          **$O(1)$**           |
| Eliminar nodo conocido $w$  | $O(n)$ (desplaza resto) | $O(n)$ (no sabe el anterior) |   **$O(1)$** (usa `w.prev`)   |
| Recorrer toda la colección  |         $O(n)$          |            $O(n)$            |            $O(n)$             |

- **Conceptos de representación:**
  - _Tamaño ($n$):_ cuántos datos reales guardamos. _Capacidad (`a.length`):_ cuántas celdas físicas tiene el arreglo en memoria.
  - _`resize`_: cuando se llena el arreglo, creas otro del doble de tamaño y copias todo para seguir metiendo en $O(1)$ amortizado.
  - _`head` y `tail`:_ punteros al primer y último nodo de la lista.
  - _`prev` y `next`:_ enlaces hacia atrás y hacia adelante en cada nodo.
  - _`dummy`:_ nodo centinela que une el final con el inicio en círculo. No guarda datos del usuario, pero evita que `head` o `tail` sean `null` y elimina un montón de `if` de casos especiales.

- **Mapeo a problemas reales:**
  - **Deshacer (Ctrl+Z):** $\to$ **Stack** (LIFO, la última acción que hiciste es la primera que tienes que revertir).
  - **Atención por orden de llegada:** $\to$ **Queue** (FIFO, como la cola del banco o del comedor, el primero que llega es el primero en ser atendido).
  - **Navegación atrás/adelante en el navegador:** $\to$ **Deque** (o dos Stacks), porque necesitas moverte por ambos extremos.
  - **Evaluación de expresiones y paréntesis:** $\to$ **Stack** (para guardar los operadores y números que quedan pendientes mientras se respeta la prioridad).
  - **BFS (búsqueda en anchura en grafos):** $\to$ **Queue** (para explorar los vecinos nivel por nivel en orden de llegada).

- **Pregunta central: ¿Por qué ninguna es la mejor para todo?**  
  Porque hay un intercambio directo: el arreglo te da acceso instantáneo por índice ($O(1)$) pero meter o sacar cosas al inicio o al medio te obliga a mover todos los elementos de casillero ($O(n)$). La lista enlazada te deja meter y sacar donde sea en $O(1)$ si ya tienes el nodo en la mano, pero pierdes el acceso directo y para buscar por índice tienes que caminar nodo por nodo ($O(n)$).
  - _Caso donde localizar cuesta más que modificar:_ Eliminar el elemento del medio ($i = n/2$) en una `DLList`. Con los punteros dobles, descolgar el nodo toma solo 2 asignaciones fijas ($O(1)$). Pero llegar a la mitad de la lista te obligó a recorrer $n/2$ enlaces uno tras otro ($O(n)$). La localización costó $O(n)$ y la modificación apenas $O(1)$.

```text
- Antes pensaba... que las listas enlazadas siempre eran mejores que los arreglos porque crecen dinámicamente y no tienen límite de capacidad fija.
- Ahora entiendo... que en el hardware real los arreglos ganan por goleada en casi todo gracias a que están pegados en memoria (localidad de caché), a menos que el problema te exija meter y sacar al inicio a cada rato.
- Todavía dudo... sobre cuándo vale la pena pagar el gasto de memoria extra de los punteros dobles en una DLList en proyectos de alto rendimiento.
```

---

### A3. Búsqueda y ordenamiento

**Consigna:**  
Compara búsqueda secuencial, búsqueda binaria, búsqueda en BST y hashing. Para cada una indica requisito, idea, costo esperado y un caso apropiado. Compara Selection Sort, Insertion Sort, Merge Sort, QuickSort y HeapSort por idea, complejidad, memoria adicional y estabilidad. Responde: ¿por qué búsqueda binaria no sirve sobre cualquier colección y por qué $O(n \log n)$ no garantiza ser mejor que $O(n^2)$ para toda entrada concreta?

```text
Registro de IA:
- Prompt usado: "Compara búsqueda secuencial, binaria, BST y hashing con requisitos y costos. Compara Selection, Insertion, Merge, Quick y HeapSort indicando estabilidad, memoria y complejidad. Explica por qué búsqueda binaria falla en colecciones generales y por qué n log n no siempre gana a n^2."
- Herramienta y fecha: Gemini / Antigravity (21/09/2026).
- Resumen de ayuda: Me explicó el concepto de estabilidad (mantener el orden original entre claves repetidas) y por qué la búsqueda binaria necesita obligatoriamente acceso directo por índice para no arruinar el log n.
```

**Respuesta con mis palabras:**

**1. Métodos de búsqueda:**

| Búsqueda       | Requisito                            | Idea central                                            | Costo esperado | Caso ideal                                                   |
| :------------- | :----------------------------------- | :------------------------------------------------------ | :------------: | :----------------------------------------------------------- |
| **Secuencial** | Ninguno                              | Mirar uno por uno del inicio al final                   |     $O(n)$     | Pocos datos o datos desordenados.                            |
| **Binaria**    | Arreglo ordenado y con índice $O(1)$ | Mirar al medio y descartar la mitad que no sirve        |  $O(\log n)$   | Arreglos grandes donde buscas mucho y casi no insertas.      |
| **En BST**     | Nodos en árbol con orden total       | Si la clave es menor vas a la izq, si es mayor a la der |     $O(h)$     | Búsquedas e inserciones continuas manteniendo todo ordenado. |
| **Hashing**    | Función hash que reparta parejo      | Convertir la clave en un índice numérico de arreglo     |     $O(1)$     | Diccionarios (Map/Set) donde buscas por clave exacta.        |

**2. Algoritmos de ordenamiento:**

| Algoritmo          | Idea central                                            |  Complejidad (Peor / Prom)  | Memoria extra | ¿Estable? |
| :----------------- | :------------------------------------------------------ | :-------------------------: | :-----------: | :-------: |
| **Selection Sort** | Busca el mínimo del resto y lo pone adelante            |      $O(n^2) / O(n^2)$      |    $O(1)$     |    No     |
| **Insertion Sort** | Toma el siguiente y lo acomoda en su sitio ordenado     |      $O(n^2) / O(n^2)$      |    $O(1)$     |  **Sí**   |
| **Merge Sort**     | Parte a la mitad, ordena mitades y las mezcla           | $O(n \log n) / O(n \log n)$ |    $O(n)$     |  **Sí**   |
| **QuickSort**      | Elige pivote, manda menores a un lado y mayores al otro |   $O(n^2) / O(n \log n)$    |  $O(\log n)$  |    No     |
| **HeapSort**       | Mete todo a un max-heap y saca la raíz una por una      | $O(n \log n) / O(n \log n)$ |    $O(1)$     |    No     |

- **¿Por qué la búsqueda binaria no sirve sobre cualquier colección?**  
  Porque si los datos están desordenados no puedes descartar mitades; la clave podría estar en cualquier lado. Y si la colección es una lista enlazada (aunque esté ordenada), para llegar a la mitad tienes que caminar $n/2$ nodos uno por uno ($O(n)$), perdiendo toda la ventaja de saltar en tiempo constante.

- **¿Por qué $O(n \log n)$ no siempre le gana a $O(n^2)$ en la práctica?**  
  Porque para entradas pequeñas ($n < 20$), Insertion Sort hace poquísimas comparaciones, no gasta nada de memoria auxiliar y corre directo en la memoria caché sin el costo de hacer llamadas recursivas ni partir arreglos como Merge Sort.

```text
- Antes pensaba... que QuickSort siempre era el rey de los ordenamientos para cualquier caso.
- Ahora entiendo... que si el pivote sale malo se degrada a O(n^2), que no es estable, y que librerías reales usan algoritmos híbridos como Timsort que mezclan Merge Sort con Insertion Sort.
- Todavía dudo... en cómo implementar HeapSort in-place sin confundir los índices 2i+1 y 2i+2 en el arreglo.
```

---

### A4. BST, AVL, heap y hashing

**Consigna:**  
Construye un BST con `8, 4, 12, 2, 6, 10, 14` y otro con `1, 2, 3, 4, 5, 6, 7`. Explica por qué la forma modifica el costo de búsqueda. Investiga factor de balance y rotaciones AVL. Puedes usar:
$$BF(v) = h(v.\text{left}) - h(v.\text{right})$$
No memorices las rotaciones: explica qué problema corrigen.  
Para heaps, explica heap mínimo, heap máximo, sift-up, sift-down y cola de prioridad. Para hashing, explica función hash, bucket, colisión, encadenamiento, factor de carga y rehashing ($\alpha = n/m$).  
Pregunta integradora: ¿qué información estructural mantiene cada representación para conseguir operaciones eficientes?

```text
Registro de IA:
- Prompt usado: "Dibuja los dos BST con las secuencias dadas. Explica cómo la forma altera el costo entre O(log n) y O(n), la idea intuitiva de las rotaciones AVL para corregir peso, y el funcionamiento básico de heap y hashing con encadenamiento."
- Herramienta y fecha: Gemini / Antigravity (21/09/2026).
- Resumen de ayuda: Me ayudó a ver el contraste entre los dos árboles: uno queda como una pirámide perfecta y el otro como una línea recta bajando a la derecha. Entendí que la rotación AVL solo baja el nodo pesado y sube el del medio para achatar el árbol.
```

**Respuesta con mis palabras:**

**1. Construcción de los dos BST:**

- **Árbol 1 con `8, 4, 12, 2, 6, 10, 14`:**

  ```text
          [8]
        /     \
      [4]     [12]
     /   \   /    \
   [2]   [6][10]  [14]
  ```

  - Forma: Pirámide perfecta y balanceada.
  - Altura: $h = 3$ nodos. Buscar cualquier clave toma a lo más 3 pasos. Costo: **$O(\log n)$**.

- **Árbol 2 con `1, 2, 3, 4, 5, 6, 7`:**

  ```text
  [1]
     \
     [2]
        \
        [3]
           \
           [4]
              \
              [5]
                 \
                 [6]
                    \
                    [7]
  ```

  - Forma: Como las claves entraron ya ordenadas, cada número es mayor que el anterior y siempre se engancha por la derecha.
  - Altura: $h = 7 = n$. Se degeneró totalmente en una lista enlazada simple. Buscar el 7 requiere visitar los 7 nodos uno por uno. Costo: **$O(n)$**.

- **Por qué la forma cambia el costo:**  
  Porque en un BST el algoritmo de búsqueda solo baja por un camino desde la raíz hasta dar con el dato o toparse con `null`. Por eso el costo real de un BST es **$O(h)$** (depende de la altura), no de $n$. Si el árbol está equilibrado, $h \approx \log_2 n$; si está chueco o degenerado, $h \approx n$.

- **Factor de balance y rotaciones AVL:**
  - $BF(v) = h(v.\text{left}) - h(v.\text{right})$. Un nodo está en equilibrio si $BF \in \{-1, 0, 1\}$.
  - Si entra un nodo y la diferencia de alturas se vuelve 2 o -2, significa que una rama creció demasiado y se está volviendo lenta.
  - _Qué corrigen las rotaciones:_ Son un reacomodo local de 3 o 4 punteros. La idea intuitiva es que el nodo del medio sube a tomar el control y el nodo que pesaba mucho baja un nivel. Así el árbol se achata, la altura vuelve a bajar a $\approx \log n$, y todo sigue cumpliendo la regla de que la izquierda es menor y la derecha es mayor.

- **Heaps y colas de prioridad:**
  - _Heap mínimo:_ Árbol binario completo donde cada padre es menor o igual que sus hijos (el mínimo absoluto siempre está arriba en la raíz).
  - _Heap máximo:_ Cada padre es mayor o igual que sus hijos (el máximo está en la raíz).
  - _sift-up:_ Cuando agregas un elemento al final del arreglo y lo vas trepando comparándolo con su padre hasta que ya no viole la regla del heap.
  - _sift-down:_ Cuando sacas la raíz, pones el último elemento arriba y lo vas bajando comparándolo con sus hijos hasta que quede en su sitio correcto.
  - _Cola de prioridad:_ ADT que te deja sacar siempre al más prioritario (el mínimo o el máximo) en tiempo rápido ($O(\log n)$) y consultar quién es en $O(1)$.

- **Hashing y factor de carga:**
  - _Función hash:_ Fórmula matemática que agarra cualquier dato (un nombre, un DNI) y te devuelve un número dentro del tamaño del arreglo.
  - _Bucket:_ Cada una de las casillas físicas del arreglo.
  - _Colisión:_ Cuando dos datos diferentes terminan dando exactamente el mismo número de casilla.
  - _Encadenamiento:_ Solución simple: cada casilla tiene una lista enlazada; si varios datos caen en la misma casilla, los vas colgando en esa lista.
  - _Factor de carga $\alpha = n/m$:_ Es cuántos elementos tienes ($n$) entre cuántas casillas hay ($m$). Si $\alpha > 0.75$, las listas se ponen largas y buscar deja de ser rápido.
  - _Rehashing:_ Crear un arreglo nuevo del doble de tamaño y volver a pasar todos los datos por la función hash para que queden bien repartidos y $\alpha$ vuelva a bajar.

- **Pregunta integradora (información estructural):**
  - El **BST** mantiene orden horizontal (izquierda $<$ padre $<$ derecha) para descartar mitades de camino.
  - El **AVL** guarda además la altura en cada nodo para saber cuándo rotar y no dejar que se alargue.
  - El **Heap** mantiene orden vertical (padre $\le$ hijo) y la forma de árbol compacto en un arreglo continuo.
  - El **Hashing** mantiene la dispersión matemática y el tamaño de tabla para saltar directo a la casilla en $O(1)$.

```text
- Antes pensaba... que un BST por llamarse árbol binario ya era automáticamente rápido O(log n).
- Ahora entiendo... que si insertas datos ordenados un BST normal es igual de lento que una lista enlazada (O(n)), y que por eso existen estructuras auto-balanceables como AVL o Red-Black.
- Todavía dudo... en cómo detectar rápido sin equivocarme si me toca hacer rotación simple o rotación doble cuando un árbol se desbalancea.
```

---

### A5. Grafos y algoritmos

**Consigna:**  
Representa Lima-Arequipa, Lima-Cusco, Cusco-Puno y Arequipa-Puno mediante lista y matriz de adyacencia. Compara memoria y consulta de aristas. Con `A -> B,C`, `B -> D`, `C -> D,E`, `D -> F`, `E -> F`, realiza conceptualmente BFS y DFS desde A e identifica la estructura auxiliar usada.  
Para orden topológico usa `Programación -> Estructuras`, `Estructuras -> Algoritmos`, `Matemática -> Algoritmos`, `Algoritmos -> IA`. Construye un orden válido y explica qué impide un ciclo.  
Investiga Ford-Fulkerson de forma introductoria: fuente, sumidero, capacidad, flujo, camino aumentante, grafo residual, arista residual inversa y flujo máximo. Explica por qué una arista residual hacia atrás permite reconsiderar una decisión previa.

```text
Registro de IA:
- Prompt usado: "Muestra la matriz y lista de adyacencia de las 4 ciudades (Lima, Arequipa, Cusco, Puno). Haz la traza paso a paso de BFS y DFS para el grafo dado con A, B, C, D, E, F. Explica orden topológico de materias y la intuición de las aristas residuales en Ford-Fulkerson."
- Herramienta y fecha: Gemini / Antigravity (21/09/2026).
- Resumen de ayuda: Me ordenó la traza de BFS usando una cola y DFS usando una pila. Lo de Ford-Fulkerson me quedó claro como un mecanismo para poder corregir si mandaste flujo por el camino equivocado.
```

**Respuesta con mis palabras:**

**1. Representación de las ciudades:**

- Nodos: 0: Lima (L), 1: Arequipa (A), 2: Cusco (C), 3: Puno (P).
- Conexiones: (L, A), (L, C), (A, P), (C, P).

```text
Matriz de Adyacencia (4 x 4):
      L  A  C  P
   L [0, 1, 1, 0]
   A [1, 0, 0, 1]
   C [1, 0, 0, 1]
   P [0, 1, 1, 0]

Lista de Adyacencia:
   L: [A, C]
   A: [L, P]
   C: [L, P]
   P: [A, C]
```

- _Comparación:_ La matriz gasta $V^2 = 16$ casillas; saber si dos ciudades están conectadas es instantáneo ($O(1)$ mirando `matriz[i][j]`), pero desperdicia espacio si hay pocas rutas. La lista gasta solo memoria proporcional a las rutas reales ($V + 2E$), pero para saber si A y P están conectadas tienes que buscar en la lista de vecinos de A.

**2. BFS y DFS desde el nodo A:**

- Grafo dirigido: `A -> B, C`, `B -> D`, `C -> D, E`, `D -> F`, `E -> F`.
- **BFS (usa una Queue / Cola FIFO):**
  - Empieza en A $\to$ visita **A**. Encola B y C.
  - Saca B $\to$ visita **B**. Encola D. (Cola tiene: C, D)
  - Saca C $\to$ visita **C**. Encola E. (D ya estaba). (Cola tiene: D, E)
  - Saca D $\to$ visita **D**. Encola F. (Cola tiene: E, F)
  - Saca E $\to$ visita **E**. (F ya estaba encolado). (Cola tiene: F)
  - Saca F $\to$ visita **F**.
  - **Orden de visita BFS:** `A, B, C, D, E, F` (avanza parejo por capas de distancia).
- **DFS (usa una Stack / Pila LIFO o la pila de recursión):**
  - Empieza en A $\to$ va al primer vecino B $\to$ de B va directo a D $\to$ de D va a F (callejón sin salida, retrocede a D, luego a B, luego a A) $\to$ de A va al vecino C $\to$ de C va a E.
  - **Orden de visita DFS:** `A, B, D, F, C, E` (se tira hasta el fondo antes de retroceder).

**3. Orden topológico:**

- Rutas: Programación $\to$ Estructuras $\to$ Algoritmos $\to$ IA; y Matemática $\to$ Algoritmos.
- _Orden topológico válido:_ **Programación, Matemática, Estructuras, Algoritmos, IA** (también se puede poner Matemática al inicio de todo).
- _Qué impide un ciclo:_ Si hubiera un ciclo (por ejemplo que para llevar Programación te pidan IA), tendrías un bucle infinito de requisitos donde ninguna materia puede ser la primera en cursarse. Por eso el orden topológico solo existe en grafos dirigidos sin ciclos (DAG).

**4. Ford-Fulkerson y aristas residuales:**

- _Términos clave:_ Fuente (donde sale el agua/datos), Sumidero (a dónde llega), Capacidad (el grosor del tubo), Flujo (cuánto está pasando realmente), Camino aumentante (una ruta de inicio a fin donde todavía sobra espacio), Grafo residual (el mapa de capacidades que van quedando libres), Flujo máximo (el cuello de botella más grande que puedes mandar de golpe).
- _Por qué la arista residual inversa permite corregir errores:_ Cuando mandas flujo por una arista de $u$ a $v$, en el mapa residual creas una flecha en sentido contrario de $v$ a $u$ con esa misma cantidad. Si más adelante otro camino decide usar esa flecha en reversa, lo que hace en la realidad es "cancelar" o desviar el flujo que habías mandado antes para dejar pasar un flujo mejor por otro lado. Es básicamente la forma matemática de decir "me equivoqué de ruta antes, reasigno el flujo y mando más".

```text
- Antes pensaba... que BFS y DFS solo eran dos formas distintas de escribir un bucle for en grafos.
- Ahora entiendo... que la única diferencia de fondo es la estructura auxiliar (Queue vs Stack), y que eso cambia completamente si exploras por capas concéntricas o hasta el fondo del camino.
- Todavía dudo... sobre cómo implementar la matriz de capacidades residuales en Ford-Fulkerson sin marearme con los índices de ida y vuelta.
```

---

### A6. Invariantes y correctitud

**Consigna:**  
Explica la diferencia entre obtener la respuesta esperada en varios ejemplos y tener un algoritmo correcto. Propón un invariante para un arreglo dinámico, una lista enlazada, un BST y un heap. Explica por qué pasar muchas pruebas no demuestra por sí solo la correctitud.

```text
Registro de IA:
- Prompt usado: "Explícame la diferencia entre pasar pruebas de software y demostrar que un algoritmo es correcto. Da ejemplos de invariantes para arreglo dinámico, DLList con dummy, BST y heap, citando la frase clásica de Dijkstra sobre los tests."
- Herramienta y fecha: Gemini / Antigravity (21/09/2026).
- Resumen de ayuda: Me quedó claro que los tests unitarios son solo muestras aisladas de un universo infinito de entradas, mientras que el invariante es la regla estructural que nunca se puede romper.
```

**Respuesta con mis palabras:**

- **Pasar ejemplos vs algoritmo correcto:**  
  Que tu código pase 5 o 10 ejemplos en tu máquina solo significa que funcionó bien para esos datos exactos. Pero no te asegura que no vaya a explotar cuando le metas una lista vacía, un solo elemento, números repetidos o datos al revés. Un algoritmo es formalmente correcto solo cuando puedes demostrar que para **cualquier** entrada válida que cumpla las condiciones iniciales, el algoritmo siempre termina y devuelve el resultado correcto respetando las reglas de la estructura.

- **Invariantes propuestos:**
  1. _Arreglo dinámico:_ Siempre se cumple $0 \le n \le a.\text{length}$; los elementos válidos están empaquetados del índice $0$ al $n-1$, y todas las celdas desde $n$ hasta $a.\text{length}-1$ tienen que estar en `null`.
  2. _Lista doble con dummy:_ `dummy.next.prev == dummy` y `dummy.prev.next == dummy`; el conteo $n$ coincide exactamente con la cantidad de nodos reales que puedes alcanzar caminando desde `dummy.next`; y si $n = 0$, `dummy.next == dummy` y `dummy.prev == dummy`.
  3. _BST:_ Para todo nodo $u$, cualquier valor en su subárbol izquierdo es estrictamente menor que $u.x$, y cualquier valor en su subárbol derecho es estrictamente mayor que $u.x$. Además, los punteros de ida y vuelta deben coincidir (`u.left.parent == u`).
  4. _Heap (Min-Heap):_ Para todo casillero $i > 0$, el padre nunca es mayor que el hijo ($a[(i-1)/2] \le a[i]$), y el arreglo no tiene huecos entre el índice $0$ y $n-1$.

- **Por qué pasar muchas pruebas no demuestra correctitud:**  
  Como decía el profesor Dijkstra: _"Las pruebas pueden demostrar la presencia de errores, pero nunca su ausencia"_. Como el conjunto de entradas posibles es prácticamente infinito, probar 100 o 1,000 casos solo rasca la superficie. Si tu código tiene un fallo en un caso borde que no se te ocurrió poner en el test, las pruebas saldrán verdes pero el programa seguirá teniendo un bug esperando reventar en producción.

```text
- Antes pensaba... que si mi código pasaba todos los tests automáticos ya estaba 100% libre de errores.
- Ahora entiendo... que los tests solo prueban casos puntuales, y que la verdadera seguridad viene de diseñar pensando en invariantes que nunca se rompan antes y después de cada operación.
- Todavía dudo... sobre cómo diseñar pruebas de propiedades (property-based tests) que generen casos borde raros de forma automática.
```

---

## 5. Parte B. Panorama de Computación

---

### B1. Ingeniería de software y resolución de problemas

```text
- Prompt usado: "Explica cohesión, acoplamiento, deuda técnica y por qué resolver primero un caso pequeño en papel revela el algoritmo correcto."
- Idea nueva: La deuda técnica no es solo código feo, es el costo extra de tiempo que pagas cada vez que quieres agregar una función nueva sobre código mal diseñado.
- Relación con CC232: Si acoplas la lógica del ADT con la representación física, cuando quieras cambiar un arreglo por nodos vas a romper todo el programa.
- Duda: ¿En qué momento exacto conviene refactorizar sin caer en sobreingeniería innecesaria?
```

**Respuesta:**  
Tener código que compila y da el resultado correcto no significa que esté bien hecho. Si tu código tiene bajo acoplamiento y alta cohesión, cada clase se encarga de una sola tarea y no mete las narices en la memoria de las demás; así, si mañana cambias cómo guardas los datos por dentro, no rompes nada afuera. Si no cuidas esto, acumulas deuda técnica: el código se vuelve tan frágil y enredado que cualquier cambio menor te toma horas y da miedo tocarlo. La mejor estrategia para no trabarse ante un problema difícil es resolverlo primero en papel con un caso chico (por ejemplo $n=3$ o $4$). Al hacerlo a mano ves cómo se mueven los punteros y qué condiciones se repiten, lo que te revela el invariante y el bucle exacto antes de meter la pata en la computadora.

---

### B2. Concurrencia y programación funcional

```text
- Prompt usado: "Por qué counter++ no es atómico en Java y cómo la inmutabilidad de la programación funcional ayuda a evitar carreras de datos."
- Idea nueva: Una simple línea como x++ se traduce en 3 pasos de CPU (leer, modificar, escribir), y ahí se meten los otros hilos.
- Relación con CC232: Si dos hilos insertan a la vez en una lista enlazada o en un arreglo sin protección, se pisan los punteros y n queda desfasado.
- Duda: ¿Qué tanta sobrecarga de memoria genera crear objetos nuevos inmutables a cada rato frente a modificar el mismo objeto con mutex?
```

**Respuesta:**  
La concurrencia es manejar varias tareas a la vez, y el paralelismo es ejecutarlas literalmente al mismo milisegundo en diferentes núcleos de CPU. Una línea tan inocente como `counter++` no es atómica: la CPU primero lee el valor de la memoria a un registro, luego le suma 1, y luego lo escribe de vuelta. Si dos hilos hacen eso a la vez, se intercalan en el medio, se sobrescriben el resultado y la cuenta final queda mal (condición de carrera). Si esto pasa sobre una lista o un arreglo propio, dos hilos pueden intentar escribir en la misma casilla física o desconectar el mismo puntero, dejando la estructura corrupta. La programación funcional ayuda un montón con esto porque promueve la inmutabilidad: si los datos no se pueden modificar una vez creados, diez hilos pueden leer al mismo tiempo sin estorbarse y sin necesidad de usar bloqueos pesados como semáforos o mutex.

---

### B3. Pruebas de software

```text
- Prompt usado: "Para int max(int a, int b) qué pruebas límite hacer y cómo formular una prueba basada en propiedades sobre un BST."
- Idea nueva: Probar por propiedades en lugar de ejemplos fijos permite que la computadora genere miles de árboles aleatorios y verifique si el in-order sigue ordenado.
- Relación con CC232: En lugar de probar si add(5) funciona, pruebas que tras cualquier add(x) el invariante subárbol izquierdo < nodo < derecho se conserve siempre.
- Duda: ¿Qué herramientas en Java se usan para Property-Based Testing como QuickCheck de Haskell?
```

**Respuesta:**  
Para una función simple como `int max(int a, int b)`, las pruebas básicas deben cubrir casos normales ($a > b$, $a < b$), pero sobre todo los casos frontera: cuando son iguales ($a == b$), cuando uno es cero, y los límites del sistema como `Integer.MAX_VALUE` y `Integer.MIN_VALUE` con signos cambiados. En estructuras de datos, probar ejemplos fijos es muy limitado; es mucho más potente usar pruebas basadas en propiedades (Property-Based Testing). Por ejemplo, en un BST la propiedad estructural es que un recorrido in-order debe ser siempre estrictamente creciente. Con esa sola regla, puedes hacer que un script meta 10,000 números al azar en el árbol y revise si la lista resultante sigue ordenada. Una propiedad general te pesca casos raros y esquinas escondidas que a un humano jamás se le ocurriría escribir en un test manual.

---

### B4. Bases de datos y transacciones

```text
- Prompt usado: "Por qué los motores de bases de datos usan Árboles B en vez de BST y por qué un índice acelera SELECT pero frena INSERT."
- Idea nueva: El Árbol B tiene nodos gordos con cientos de claves para coincidir con el tamaño de bloque del disco duro y hacer la menor cantidad de lecturas posibles.
- Relación con CC232: Es la evolución directa de los árboles de búsqueda aplicados a almacenamiento secundario persistente.
- Duda: ¿Cómo manejan las bases de datos modernas las anomalías de Write Skew bajo aislamiento Snapshot Isolation?
```

**Respuesta:**  
En bases de datos relacionales, los datos se guardan en disco duro o SSD, donde leer bloques es órdenes de magnitud más lento que en la memoria RAM. Por eso no usan BST comunes, sino Árboles B: árboles donde cada nodo tiene cientos de claves y muchos hijos, haciendo que la altura sea cortísima (3 o 4 niveles para millones de registros). Esto permite que una consulta `SELECT` encuentre cualquier dato en poquísimas lecturas de disco ($O(\log n)$ en vez de escanear toda la tabla en $O(n)$). La desventaja es que no puedes poner índices a lo loco: cada vez que haces un `INSERT` o `DELETE`, la base de datos no solo tiene que escribir la fila, sino también reacomodar y balancear el árbol de cada índice que creaste, haciendo las escrituras mucho más caras. En transacciones, el estándar ACID asegura que los cambios no se pierdan ni se mezclen; por ejemplo, una "lectura sucia" ocurre cuando lees datos de otra transacción que todavía no ha hecho commit y que luego se cancela.

---

### B5. Distribución y consenso

```text
- Prompt usado: "Explica el Teorema CAP, partición de red y por qué el hashing consistente es vital para repartir datos entre varios servidores."
- Idea nueva: En un sistema distribuido la red tarde o temprano se corta, así que estás obligado a elegir entre devolver datos desactualizados o dar error.
- Relación con CC232: El hashing consistente es aplicar la función hash sobre un círculo para no tener que mover todos los datos cuando agregas un servidor nuevo.
- Duda: ¿Cómo eligen las bases de datos NoSQL entre consistencia eventual y latencia baja en la práctica?
```

**Respuesta:**  
En un solo servidor la memoria física es el único punto de verdad. Pero cuando tus datos no caben en una máquina y tienes que repartirlos en un clúster, aparece el problema de la red: los cables fallan, los routers se caen o hay lag (partición de red). El Teorema CAP demuestra que cuando la red se parte, tienes que elegir entre Consistencia (que todos los nodos respondan exactamente el mismo dato al instante) o Disponibilidad (que todos respondan aunque sea con un dato viejo). Llegar a acuerdos entre máquinas independientes para elegir un líder o confirmar un dato (consenso con algoritmos como Raft o Paxos) es dificilísimo porque si un nodo no contesta, nunca sabes si se murió o si el mensaje sigue en camino. Aquí el hashing de CC232 se vuelve clave con el _hashing consistente_: reparte las claves en un anillo virtual para que si un servidor se cae, solo tengas que reubicar una fracción chica de los datos y no toda la base de datos completa.

---

### B6. Aproximación, rendimiento y percepción de latencia

```text
- Prompt usado: "Cómo funciona un Filtro de Bloom, por qué puede dar falsos positivos pero nunca falsos negativos, y cuándo conviene usar algoritmos aproximados."
- Idea nueva: Un Filtro de Bloom no guarda los datos reales, solo prende bits con funciones hash; si un bit da 0, es matemáticamente imposible que el dato exista.
- Relación con CC232: Combina funciones hash con arreglos compactos de bits para descartar búsquedas pesadas en disco en O(1).
- Duda: ¿Cómo se calcula la cantidad óptima de funciones hash para minimizar la tasa de falsos positivos?
```

**Respuesta:**  
A veces en ingeniería de software calcular la respuesta exacta es tan caro o tarda tanto que conviene usar una respuesta aproximada para responder al instante. Un ejemplo clásico es el Filtro de Bloom: es una estructura probabilística que usa varias funciones hash sobre un arreglo de bits. Si buscas una clave y al aplicar los hashes encuentras algún bit en 0, tienes la certeza absoluta del 100% de que el elemento NO existe en la base de datos. Pero si todos los bits están en 1, puede que el dato esté o puede que sea casualidad de que otros datos prendieron esos mismos bits (falso positivo). Esto es genial para motores como Cassandra o Google Bigtable: consultan primero el filtro en memoria RAM ($O(1)$) y si dice que no está, se ahorran totalmente la costosa búsqueda en el disco. Además, en la experiencia de usuario muchas veces importa más la percepción: mostrar una pantalla de carga animada o respuestas parciales hace que el sistema se sienta rápido aunque el cálculo de fondo siga corriendo.

---

### B7. Confiabilidad y análisis causal

```text
- Prompt usado: "Explica la falacia post hoc ergo propter hoc en optimización de rendimiento y técnicas de resiliencia como circuit breaker y retry."
- Idea nueva: Correlación no es causalidad; que el sistema corra más rápido tras cambiar un método no prueba que tu cambio haya sido el motivo.
- Relación con CC232: Medir algoritmos con System.currentTimeMillis() engaña porque la JVM compila en caliente y el recolector de basura mete pausas al azar.
- Duda: ¿Cómo diseñar benchmarks justos en Java que aíslen el calentamiento de la JVM (JIT compiler)?
```

**Respuesta:**  
Construir software confiable significa asumir que todo lo que pueda fallar va a fallar: los servidores se cuelgan, las APIs externas botan error 500 y las bases de datos se saturan. Para evitar que una caída tire todo el sistema se usan patrones de resiliencia como el _circuit breaker_ (cortar llamadas a un servicio roto para no acumular hilos trabados) y degradación controlada (mostrar datos cacheados si el servicio principal no responde). Por otro lado, al analizar el rendimiento es facilísimo caer en la trampa de _post hoc ergo propter hoc_ (creer que porque cambiaste el algoritmo y el sistema anduvo más rápido, tu cambio causó toda la mejora). Pudo haber sido casualidad: tal vez en esa prueba entraron menos peticiones, o los datos ya estaban en la memoria caché del sistema operativo, o el Garbage Collector de Java no se disparó durante esa corrida. Para demostrar causalidad real tienes que segmentar datos, usar herramientas de microbenchmarking serias (como JMH) y hacer pruebas comparativas bajo las mismas condiciones.

---

### B8. Arquitectura, sistemas y Linux

```text
- Prompt usado: "Por qué 8 hilos no rinden 8x respecto a 1 hilo (Ley de Amdahl), cómo participa la caché de CPU en el rendimiento y comandos básicos de Linux."
- Idea nueva: La contención por el bus de memoria RAM y el cambio de contexto entre hilos pueden hacer que meter más núcleos vuelva más lento el programa.
- Relación con CC232: Los arreglos son amigables con las líneas de caché de la CPU (localidad espacial); las listas enlazadas hacen saltar la CPU a direcciones dispersas (cache miss).
- Duda: ¿Cómo ayuda la herramienta perf en Linux a contar los fallos de caché de un algoritmo Java?
```

**Respuesta:**  
Pensar que un programa con 8 hilos va a correr 8 veces más rápido que con un solo hilo es un error común. La Ley de Amdahl demuestra que la aceleración máxima siempre está limitada por la parte del código que es puramente secuencial y no se puede paralelizar. Además, si los 8 hilos intentan escribir en la misma zona de memoria o compiten por el bus de datos, el costo de coordinarse, alternar contextos y sincronizar cachés termina haciendo que el sistema ande más lento. En el rendimiento real, el algoritmo no corre en el aire: corre sobre un procesador con predicción de saltos y memoria caché L1/L2/L3. Por eso recorrer un arreglo continuo es infinitamente más rápido que saltar por nodos de una lista dispersa en memoria RAM (localidad espacial). En Linux, la terminal es la herramienta de trabajo: comandos como `ls` para ver archivos, `cd` para navegar, `cat` para leer rápido y monitores como `htop` o `top` te permiten vigilar en tiempo real cómo tu programa exprime la CPU y la memoria.

---

### B9. Beyond Code

```text
- Prompt usado: "Explica el problema XY, el sesgo de confirmación, y cómo explicarle un árbol AVL a una persona sin conocimientos técnicos."
- Idea nueva: El problema XY te hace perder horas preguntando por una solución parchada que inventaste en tu cabeza en vez de explicar el problema raíz.
- Relación con CC232: En tutoría pasa siempre: un alumno pregunta cómo enlazar tres punteros raros (Y), cuando en realidad solo quería insertar al inicio (X).
- Duda: ¿Cómo organizar notas de estudio con el método PARA sin pasar más tiempo acomodando carpetas que estudiando?
```

**Respuesta:**  
En ingeniería, saber comunicarse y pensar críticamente es tan importante como picar código. El _problema XY_ ocurre cuando tienes un problema X, te imaginas una solución rebuscada Y, y vas a pedir ayuda preguntando cómo hacer funcionar Y en vez de explicar cuál era el problema original X. Reconocer con honestidad un "no lo sé" no te resta nivel; al contrario, evita perder tiempo en soluciones falsas y te hace un profesional mucho más confiable.  
Si tuviera que explicarle qué es un árbol AVL a una persona que no sabe nada de programación, le diría: _"Imagínate un archivador de oficina donde cada cajón se divide en dos carpetas: papeles con números menores a la izquierda y mayores a la derecha. Para que ningún cajón se vuelva gigante y sea un martirio buscar, el archivador tiene un mecanismo inteligente: cada vez que metes muchos papeles seguidos en un mismo lado, el mueble se reacomoda solo y pasa un par de carpetas al otro lado para que siempre encuentres cualquier papel en menos de 3 pasos"_. Al quitar el lenguaje técnico ganas claridad inmediata para que cualquiera entienda el propósito, pero pierdes el detalle fino de cómo se mueven las referencias en memoria.

---

## 6. Mapa de Computación

| Área                         | Antes                                                                            | Después                                                                            | Conexión con CC232                                                         |  Interés  |
| :--------------------------- | :------------------------------------------------------------------------------- | :--------------------------------------------------------------------------------- | :------------------------------------------------------------------------- | :-------: |
| **Algoritmos y complejidad** | Pensaba que Big-O medía segundos directos en la computadora.                     | Entiendo que mide cómo escala el trabajo cuando $n$ crece al infinito.             | Es la regla para justificar por qué una estructura es mejor que otra.      | **Alto**  |
| **Estructuras de datos**     | Creía que las listas enlazadas siempre eran mejores que los arreglos.            | Entiendo que el arreglo casi siempre gana por la memoria contigua y la caché.      | Base de todo el curso: arreglos, listas simples, dobles y circulares.      | **Alto**  |
| **Árboles y hashing**        | Pensaba que un BST normal ya garantizaba búsqueda rápida $O(\log n)$.            | Sé que si entran datos ordenados se degenera en lista $O(n)$ si no tiene balanceo. | Semana 4 a 10: BST, AVL, Heaps, Tablas Hash y colisiones.                  | **Alto**  |
| **Grafos**                   | Creía que BFS y DFS eran algoritmos con lógicas completamente ajenas.            | Vi que la única diferencia de fondo es la estructura auxiliar: Cola vs Pila.       | Representación por listas/matrices y recorridos en las semanas 11 y 12.    | **Alto**  |
| **Concurrencia**             | Pensaba que una línea corta de código se ejecutaba de un solo golpe.             | Entendí que instrucciones simples tienen varios pasos de CPU y generan carreras.   | Proteger estructuras propias cuando varios hilos acceden a la vez.         | **Medio** |
| **Programación funcional**   | Creía que era solo sintaxis moderna o funciones lambda raras.                    | Vi que la inmutabilidad elimina dolores de cabeza en concurrencia.                 | Diseñar operaciones que no alteren estado mutable sin necesidad.           | **Medio** |
| **Ingeniería de software**   | Pensaba que con tal de que el código corra y dé el resultado bastaba.            | Entendí que el código acoplado y desordenado genera deuda técnica pesada.          | Diseñar clases limpias separando el ADT de su implementación interna.      | **Alto**  |
| **Pruebas de software**      | Creía que pasar los tests del profesor garantizaba que el código era perfecto.   | Sé que los tests solo prueban casos aislados y que se requieren invariantes.       | Validar contratos, casos frontera (vacío, 1 nodo) y correctitud.           | **Alto**  |
| **Bases de datos**           | No sabía por qué los motores de bases de datos usaban Árboles B.                 | Entendí que los nodos anchos reducen los accesos a disco a solo 3 o 4 lecturas.    | Aplicación masiva de árboles de búsqueda y tablas hash en almacenamiento.  | **Medio** |
| **Sistemas distribuidos**    | Pensaba que conectar servidores era igual que programar en una máquina.          | Comprendí el Teorema CAP y la dificultad de lograr consenso con fallas de red.     | Hashing consistente para repartir claves en clústeres sin rehashing total. | **Medio** |
| **Rendimiento**              | Creía que optimizar era meter trucos de código en cualquier línea.               | Sé que la memoria caché, los saltos de CPU y el algoritmo mandan en la velocidad.  | Comparar tiempo asintótico frente a comportamiento en hardware real.       | **Alto**  |
| **Confiabilidad**            | Pensaba que los servidores casi nunca fallaban si el código estaba bien.         | Entendí que todo falla y que se deben usar patrones como circuit breaker.          | Diseñar estructuras robustas que manejen excepciones y estados frontera.   | **Medio** |
| **Análisis causal**          | Creía que si el sistema mejoraba tras un commit, el cambio era la causa.         | Sé que hay variables ocultas (JIT compiler, Garbage Collector, caché de SO).       | Benchmarking riguroso para comparar algoritmos sin autoengañarse.          | **Medio** |
| **Arquitectura y Linux**     | Pensaba que meter más hilos siempre multiplicaba la velocidad directo.           | Entendí la Ley de Amdahl y el costo del cambio de contexto de la CPU.              | Manejo de terminal, perfiles de memoria y compilación eficiente en Linux.  | **Alto**  |
| **Comunicación**             | Creía que explicar algo técnico era usar palabras raras y difíciles.             | Vi que explicar una idea con analogías cotidianas demuestra dominio real.          | Sustentaciones orales claras y justificaciones concisas en actividades.    | **Alto**  |
| **Pensamiento crítico**      | Creía que las respuestas de la IA o de un autor siempre eran la verdad absoluta. | Aprendí a auditar las respuestas buscando errores de invariantes o casos borde.    | Revisar críticamente soluciones antes de implementarlas en el código.      | **Alto**  |
| **Gestión del conocimiento** | Guardaba código y notas sueltas sin un orden claro de estudio.                   | Entendí el valor de registrar errores comunes para no tropezar dos veces.          | Mantener un repositorio ordenado con bitácoras y ejercicios resueltos.     | **Alto**  |

---

### Selección de conceptos:

1. **Tres conceptos que ya conocía:**
   - La diferencia básica entre Pila (LIFO) y Cola (FIFO).
   - El acceso por índice directo $O(1)$ en arreglos continuos.
   - La búsqueda secuencial revisando casilla por casilla en $O(n)$.
2. **Tres conceptos que creía conocer pero resultaron mucho más profundos:**
   - **BST:** Creía que siempre era $O(\log n)$, pero vi que la historia de inserciones puede degenerarlo en una lista lenta de $O(n)$.
   - **Complejidad Big-O:** Creía que medía tiempo en el reloj, pero vi que mide escalabilidad y que constantes ocultas mandan en $n$ chico.
   - **Pruebas unitarias:** Creía que pasar los tests demostraba que todo estaba bien; ahora sé que solo prueban presencia de errores y no su ausencia.
3. **Tres conceptos completamente nuevos:**
   - El **Filtro de Bloom** y cómo descarta búsquedas con probabilidad de falsos positivos en $O(1)$.
   - El funcionamiento de las aristas residuales inversas en **Ford-Fulkerson** para desviar flujos previos.
   - El Teorema **CAP** y los algoritmos de consenso en sistemas distribuidos.
4. **Uno que quiero estudiar con mayor detalle:**
   - **Árboles AVL y Rotaciones:** Quiero dominar la mecánica exacta de rebalanceo en papel y código para que ninguna inserción o borrado rompa la altura logarítmica.

---

## 7. Cierre

- **Concepto que más cambió entre mi respuesta inicial y final:**  
  El costo de los árboles binarios de búsqueda. Antes juraba que por tener forma de árbol ya garantizaban $O(\log n)$ en automático. Ahora entiendo que el costo real es $O(h)$ y que si no usas una estructura auto-balanceable como AVL, meter datos ordenados los vuelve igual de lentos que una lista enlazada común.

- **Ocasión en que la primera respuesta de la IA no fue suficiente y qué hice para mejorarla:**  
  Cuando le pedí que me explicara las rotaciones AVL: al principio me soltó un bloque denso de fórmulas con nombres formales que no ayudaban a visualizar nada. Para corregirla, le pedí explícitamente que me hiciera un dibujo paso a paso en texto con un árbol chiquito de 3 nodos y me explicara cómo se reasignan los punteros como si fuera una balanza.

- **Tipo de prompt que produjo mejores explicaciones:**  
  Pedirle siempre un ejemplo numérico concreto con un dibujo simple antes de que me dé definiciones teóricas, y exigirle que me diga qué problema de la vida real resuelve cada estructura.

- **Reflexión final:**  
  _Antes de esta prueba pensaba que Ciencia de la Computación era aprenderse la sintaxis de varios lenguajes y picar código rápido hasta que el programa corra. Ahora la describiría como la disciplina de razonar con rigor matemático sobre cómo organizar la información, elegir la estructura de datos exacta pensando en la memoria y el procesador, y diseñar algoritmos con invariantes sólidos que garanticen que el software sea correcto, eficiente y escalable antes de tocar el teclado._
