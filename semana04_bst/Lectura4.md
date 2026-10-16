### **<mark>Lectura: árboles binarios de búsqueda, representación jerárquica y costo de las operaciones</mark>**

Esta lectura consolida y amplía las ideas trabajadas en la Semana 4 de CC232. Durante las tres primeras semanas estudiamos estructuras cuyo estado podía entenderse principalmente como una secuencia.

En la Semana 1 utilizamos un arreglo dinámico, donde la posición lógica coincidía con una posición física del arreglo. Esa representación ofrecía acceso directo por índice y obligaba a distinguir tamaño, capacidad y redimensionamiento.

En la Semana 2 utilizamos nodos enlazados (`head -> nodo -> nodo -> nodo`). La secuencia ya no dependía de posiciones contiguas; su orden se reconstruía siguiendo referencias.

En la Semana 3 estudiamos `Stack`, `Queue` y `Deque`. La pregunta dejó de ser únicamente cómo representar una secuencia y pasó a ser qué operaciones permitimos sobre esa secuencia. Una pila restringe el acceso mediante **LIFO**, una cola mediante **FIFO**, y un deque permite operaciones por ambos extremos.

La Semana 4 introduce un cambio estructural más profundo: ¿qué ocurre si los elementos ya no se organizan en una sola línea? Pasaremos de una organización lineal a una **<mark>organización jerárquica (un nodo bifurca hacia dos subestructuras: izquierda y derecha)</mark>**. En lugar de que un nodo conduzca esencialmente a un único siguiente elemento, un nodo podrá conducir a dos subestructuras diferentes, una izquierda y una derecha. Pero tener dos caminos no basta para buscar eficientemente.

La idea decisiva de esta semana será imponer un **<mark>invariante de orden</mark>** que permita **<mark>decidir, después de cada comparación, por cuál camino continuar descartando la mitad del árbol</mark>**. El objetivo no es memorizar un dibujo de árbol ni una implementación particular. El objetivo es continuar la misma línea conceptual de las semanas anteriores: qué comportamiento queremos ofrecer (**ADT**), cómo almacenamos el estado (**representación**), qué propiedades deben permanecer verdaderas (**invariante**), cómo usamos y modificamos la representación (**algoritmo**), y cuánto trabajo exige cada operación (**complejidad**).

Al finalizar la lectura deberías poder explicar por qué un árbol binario de búsqueda permite implementar búsqueda e inserción siguiendo un solo camino desde la raíz, por qué ese camino depende del invariante de orden y por qué el costo real depende de la altura del árbol.

### **<mark>1. De estructuras lineales a estructuras jerárquicas</mark>**

Considera la secuencia `10, 20, 30, 40, 50`. Una representación lineal con nodos podría ser `10 -> 20 -> 30 -> 40 -> 50 -> null`. Existe un camino natural desde el primer elemento hacia los siguientes; si partimos de 10 y queremos llegar a 50, la representación obliga a recorrer la cadena completa.

Ahora considera otra organización de los mismos valores:

```text
       30
     /    \
   20      40
  /          \
10            50
```

Aquí no existe una única relación de siguiente. Desde 30 aparecen dos posibilidades, izquierda o derecha. Desde 20 vuelve a aparecer una posibilidad izquierda, y desde 40 una posibilidad derecha. La estructura ya no es lineal. Es jerárquica.

#### **<mark>Qué significa jerárquica</mark>**

Una **<mark>estructura jerárquica</mark>** organiza elementos mediante **<mark>relaciones de dependencia o descendencia (padre e hijos)</mark>**. Un nodo puede tener nodos por debajo de él, y esos nodos pueden a su vez tener otros nodos. Por ejemplo:

```text
       40
     /    \
   20      60
  /  \    /  \
10   30  50   70
```

40 está por encima de 20 y 60; 20 está por encima de 10 y 30; 60 está por encima de 50 y 70. No interpretaremos "por encima" como una dirección física de memoria, sino como una **<mark>relación estructural de punteros en el heap</mark>**.

#### **<mark>La representación sigue siendo enlazada</mark>**

El cambio de lineal a jerárquico no significa abandonar las referencias Java. En la Semana 2 utilizábamos `Node next` o `Node prev; Node next`. En la Semana 4 utilizaremos **<mark>Node left; Node right; Node parent;</mark>**. La idea de referencia sigue siendo la misma; lo que cambia es la forma de conectar los nodos, de una cadena a una bifurcación. La **<mark>topología de la representación cambia</mark>** (de cadena lineal a bifurcación), y al cambiar la representación **<mark>vuelven a cambiar las operaciones y sus costos</mark>**.

### **<mark>2. Qué es un árbol</mark>**

Un **árbol** es una **<mark>estructura jerárquica formada por nodos conectados sin ciclos</mark>**. Para nuestro estudio utilizaremos árboles con una **<mark>raíz</mark>**, que proporciona el **<mark>punto de entrada a toda la estructura</mark>**. Considerando de nuevo:

```text
       40
     /    \
   20      60
  /  \    /  \
10   30  50   70
```

podemos describir este árbol mediante relaciones: 40 es la raíz; 20 y 60 son hijos de 40 y 40 es padre de ambos; 10 y 30 son hijos de 20; 50 y 70 son hijos de 60. La terminología no es decorativa, nos permitirá describir algoritmos con precisión.

### **<mark>3. Nodo, raíz, padre e hijo</mark>**

Un **nodo** es una unidad de almacenamiento de la estructura. En el código de esta semana cada nodo contiene:

```java
static class Node {
    int x;
    Node left;
    Node right;
    Node parent;

    Node(int x) {
        this.x = x;
    }
}
```

El campo **<mark>x almacena el valor lógico (la clave)</mark>**. Los campos **<mark>left, right y parent describen la estructura enlazada</mark>**. Esta distinción separa el contenido lógico de la información estructural de punteros en memoria.

La **raíz** es el nodo desde el cual accedemos al árbol completo. En la implementación: **<mark>private Node root</mark>**;. Si **<mark>root == null</mark>**, el árbol está vacío; si **<mark>root != null</mark>**, root referencia al primer nodo de la jerarquía, y para un árbol no vacío debe cumplirse obligatoriamente: **<mark>root.parent == null</mark>** (la raíz jamás tiene padre).

Si un nodo `u` contiene `u.left == v` o `u.right == v`, entonces `u` es **padre** de `v`. Para mantener la coherencia bidireccional, debe cumplirse la relación inversa: **<mark>v.parent == u</mark>**.

Un nodo tiene a lo más un **<mark>hijo izquierdo (claves menores)</mark>** y un **<mark>hijo derecho (claves mayores)</mark>**. No son intercambiables porque su posición define el orden de búsqueda.

### **<mark>4. Nodo interno, hoja y subárbol</mark>**

Una **<mark>hoja</mark>** es un nodo que no tiene hijos (**<mark>u.left == null && u.right == null</mark>**); en el árbol de ejemplo, las hojas son 10, 30, 50 y 70. Un **<mark>nodo interno</mark>** es un nodo que tiene **<mark>al menos un hijo</mark>**, como 40, 20 y 60 en ese mismo árbol.

El **<mark>subárbol enraizado en u</mark>** está formado por **<mark>u y TODOS sus descendientes</mark>**. En el árbol de ejemplo, el subárbol enraizado en 20 es `{10, 20, 30}` con 20 como raíz, y el subárbol enraizado en 60 es `{50, 60, 70}`. La noción de subárbol será esencial: el invariante del BST no habla solamente de un hijo inmediato, habla de todos los valores contenidos en un subárbol.

### **<mark>5. Árbol binario</mark>**

Un **árbol binario** es un árbol en el que cada nodo tiene como máximo dos hijos, distinguidos como izquierdo y derecho. Un nodo puede tener 0, 1 o 2 hijos; un árbol binario no tiene que estar "lleno".

Este punto es fundamental: **<mark>todo BST es un árbol binario, pero no todo árbol binario es un BST</mark>**. Por ejemplo, `40` con hijo izquierdo `50` y derecho `20` es un árbol binario válido (cada nodo tiene como máximo dos hijos), pero no satisface la regla de orden que utilizaremos para buscar. Por tanto, "árbol binario" describe una forma estructural, mientras que **BST** es un árbol binario más una propiedad de orden.

### **<mark>6. Qué es una clave</mark>**

Llamaremos **<mark>clave (key)</mark>** al **<mark>valor numérico que la estructura utiliza para realizar las comparaciones</mark>**. En el archivo Java de esta semana el nodo almacena únicamente `int x;`, así que `x` cumple dos papeles a la vez, dato almacenado y clave utilizada para ordenar. Si tuviéramos estudiantes con código, nombre y promedio, podríamos decidir que la clave fuera solamente el código y ordenar el árbol de acuerdo con ese campo; en esta semana evitamos esa complejidad y trabajamos directamente con enteros como clave.

Los enteros permiten comparar cualquier par de claves mediante `<`, `>` e `==` (por ejemplo, `20 < 40`, `60 > 40`, `40 == 40`). Ese **<mark>orden total</mark>** guiará la búsqueda.

### **<mark>7. BST como ADT de conjunto ordenado</mark>**

BST significa _Binary Search Tree_, árbol binario de búsqueda, pero conviene separar nuevamente ADT e implementación.

Durante esta semana queremos una estructura que se comporte como un **<mark>conjunto ordenado de enteros distintos</mark>**, con operaciones relevantes `int size()`, `boolean contains(int x)` y `boolean add(int x)`. La palabra "conjunto" implica que **<mark>una clave aparece como máximo una vez</mark>**: `add(30)` seguido de otro `add(30)` no debe crear dos nodos con valor 30; la segunda inserción debe rechazarse.

Para implementar ese comportamiento utilizamos `root`, `n` y, en cada nodo, `x`, `left`, `right` y `parent`. El BST es la representación concreta que permite aprovechar el orden. Podemos resumir la separación: el ADT de conjunto ordenado ofrece `contains(x)`, `add(x)` y `size()`; la representación BST aporta `root`, `n`, `left`, `right`, `parent` y el invariante de orden. El usuario de `contains(30)` no necesita conocer qué nodos fueron visitados, pero nosotros, como implementadores de la estructura, sí necesitamos comprender ese recorrido.

### **<mark>8. Invariante de orden del BST</mark>**

La propiedad central de un BST con claves distintas es que, para todo nodo `u`, **<mark>todos los valores del subárbol izquierdo de u son menores que u.x</mark>**, y **<mark>todos los valores del subárbol derecho de u son mayores que u.x</mark>**. En símbolos, **<mark>izquierda < nodo < derecha</mark>**, pero esa expresión debe entenderse **<mark>sobre subárboles completos</mark>**, no solo sobre hijos inmediatos.

En el árbol de ejemplo, respecto de 40 se cumple que 10, 20 y 30 son menores y 50, 60 y 70 son mayores; respecto de 20, 10 es menor y 30 es mayor; respecto de 60, 50 es menor y 70 es mayor. El invariante se cumple recursivamente.

### **<mark>9. Por qué comparar solamente padre e hijo no basta</mark>**

Considera el árbol `40` con hijo izquierdo `20` y derecho `60`, y `60` con hijo izquierdo `30`. Si observamos solamente la relación inmediata `30 < 60`, podríamos pensar que el enlace local es correcto. Pero 30 pertenece al subárbol derecho de 40, y por tanto debería cumplirse `30 > 40`, lo cual no se cumple. El árbol no es un BST válido.

No basta verificar `u.left.x < u.x` y `u.right.x > u.x` en cada nodo; debemos pensar en todo el subárbol izquierdo y todo el subárbol derecho. Esta precisión es esencial para comprender por qué las decisiones de búsqueda son correctas: **<mark>el invariante es global respecto del subárbol</mark>**, no solo local respecto del hijo inmediato.

### **<mark>10. El invariante convierte una jerarquía en una estructura de búsqueda</mark>**

Un árbol binario sin orden puede obligarnos a explorar ambos subárboles; **<mark>un BST permite tomar una decisión determinista en cada nodo</mark>** gracias al invariante.

Supongamos que buscamos `x = 55` en el árbol `40` con hijo izquierdo `20` y derecho `60`, y `60` con hijo izquierdo `50`. Primero, `55 > 40`; el invariante nos dice que todo el subárbol izquierdo de 40 contiene valores menores que 40, así que 55 no puede estar allí y descartamos ese subárbol completo, continuando solo por la derecha. Después, `55 < 60`, así que descartamos el subárbol derecho de 60 y continuamos por la izquierda. Luego, `55 > 50`, así que continuamos por la derecha de 50, pero `50.right == null`. La búsqueda termina sin encontrar 55.

### **<mark>11. Camino de búsqueda</mark>**

Llamaremos **<mark>camino de búsqueda</mark>** a la secuencia de nodos visitados desde `root` mientras comparamos la clave buscada. Para buscar 30 en el árbol completo de siete nodos, el camino es `40 -> 20 -> 30` (30 < 40 va a la izquierda, 30 > 20 va a la derecha, 30 == 30 encontrado).

Si buscamos una clave ausente, por ejemplo 55, el camino en el árbol de la sección anterior es `40 -> 60 -> 50`, y la clave no existe. Pero el camino no fue inútil: el último nodo visitado fue 50, y esa información será exactamente la que necesitaremos para insertar 55.

### **<mark>12. La decisión izquierda o derecha</mark>**

En cada nodo `w` hay tres decisiones deterministas:

- Si `x < w.x`: **<mark>w = w.left;</mark>** (bajar a la izquierda).
- Si `x > w.x`: **<mark>w = w.right;</mark>** (bajar a la derecha).
- Si `x == w.x`: **<mark>return w;</mark>** (clave encontrada).

Esta decisión (**<mark>menor va a la izquierda, mayor va a la derecha</mark>**) no es una regla arbitraria: es correcta porque el BST garantiza **<mark>izquierda < nodo < derecha</mark>**. **<mark>Si el invariante se rompiera, la búsqueda binaria dejaría de ser confiable</mark>**.

### **<mark>13. Del camino de búsqueda al último nodo visitado</mark>**

Retomando el árbol `40` con hijo izquierdo `20` y derecho `60`, y `60` con hijo izquierdo `50`, buscar 55 produce el camino `40 -> 60 -> 50 -> null`; la referencia que intentamos seguir desde 50 fue `50.right`. Aunque 55 no exista, queremos recordar que el último nodo visitado fue 50, porque ese nodo nos proporciona dos resultados diferentes, primero que 55 no está en el árbol, y segundo que si quisiéramos insertar 55, debería quedar relacionado con 50. Esta idea motiva el método `findLast(int x)`.

### **<mark>14. Qué debe hacer findLast(int x)</mark>**

La especificación que utilizaremos es: si `x` existe, retornar el nodo que contiene `x`; si `x` no existe, retornar el último nodo visitado; si el árbol está vacío, retornar `null`. Observa que no es exactamente lo mismo que `find` o `contains`; `findLast` devuelve información estructural sobre dónde terminó el camino.

### **<mark>15. Diseñar findLast antes de escribir Java</mark>**

Necesitamos dos referencias temporales: **<mark>w (nodo que estamos examinando ahora)</mark>** y **<mark>prev (último nodo real antes de caer en null)</mark>**. Al comenzar, `w = root` y `prev = null`. Mientras `w` no sea `null`, primero hacemos `prev = w` y luego decidimos el siguiente camino según `x` sea menor, mayor o igual a `w.x`. Si el ciclo termina sin haber encontrado la clave, retornamos `prev`.

Supongamos que `x` no existe: el ciclo termina cuando `w == null`. Si solamente conserváramos `w`, al llegar a null habríamos perdido el punto de inserción; **<mark>prev evita esa pérdida de referencia</mark>**. Esta idea recuerda una lección de la Semana 2, antes de modificar o avanzar una referencia, hay que preguntarse qué información se necesita conservar.

### **<mark>16. Implementación de findLast(int x)</mark>**

```java
private Node findLast(int x) {
    Node w = root;
    Node prev = null;

    while (w != null) {
        prev = w;

        if (x < w.x) {
            w = w.left;
        } else if (x > w.x) {
            w = w.right;
        } else {
            return w;
        }
    }

    return prev;
}
```

No conviene memorizar este método como una secuencia de líneas. Cada línea representa una decisión estructural.

### **<mark>17. Traza completa de findLast(55)</mark>**

Sobre el árbol `40` con hijo izquierdo `20` y derecho `60`, y `60` con hijo izquierdo `50`, el estado inicial es `w = 40`, `prev = null`. En la primera iteración, `prev = 40`, y como `55 > 40`, `w = 60`. En la segunda, `prev = 60`, y como `55 < 60`, `w = 50`. En la tercera, `prev = 50`, y como `55 > 50`, `w = null`, y el ciclo termina. El método retorna `prev`, es decir, el nodo 50. Por tanto `findLast(55) -> nodo 50`, aunque 55 no exista.

### **<mark>18. Traza de una búsqueda exitosa</mark>**

Busquemos 30 en el árbol `40` con hijo izquierdo `20` (que tiene hijos 10 y 30) y derecho `60`. Primera iteración, `w = 40`, `30 < 40`, `w = 20`. Segunda, `w = 20`, `30 > 20`, `w = 30`. Tercera, `30 == 30`, el método ejecuta `return w;`. Por tanto, `findLast(30) -> nodo 30`.

### **<mark>19. El caso del árbol vacío</mark>**

Si `root == null`, entonces `w = null` y `prev = null` desde el inicio. El ciclo `while (w != null)` no se ejecuta, y el método retorna `null`. Por tanto, `findLast(x) -> null` cuando el árbol está vacío. Este resultado será necesario para reconocer la primera inserción.

### **<mark>20. contains(int x) como cliente de findLast</mark>**

El archivo de esta semana utiliza:

```java
boolean contains(int x) {
    **<mark>Node p = findLast(x);</mark>**
    **<mark>return p != null && p.x == x;</mark>**
}
```

Esta implementación es pequeña porque la búsqueda ya está concentrada en `findLast`. No basta con `p != null`: supongamos `findLast(55) -> nodo 50`; entonces `p != null` es verdadero, pero 55 no existe, así que también necesitamos `p.x == x`. En nuestro ejemplo, `50 == 55` es falso, por tanto `contains(55) -> false`. En cambio, si `findLast(30) -> nodo 30`, entonces `p != null` y `p.x == 30`, así que `contains(30) -> true`.

Podemos resumir la separación de responsabilidades: **<mark>findLast(x) responde dónde terminó el camino de búsqueda</mark>**, mientras que **<mark>contains(x) responde si x pertenece al conjunto</mark>**. Un método estructural puede servir de base para varias operaciones del ADT.

### **<mark>21. La búsqueda fallida determina la posición de inserción</mark>**

Esta es una de las ideas centrales de la Semana 4. Retomando el árbol `40` con hijo izquierdo `20` y derecho `60`, y `60` con hijo izquierdo `50`, buscamos 55 y ya sabemos que `findLast(55) = 50`. La última comparación fue `55 > 50`, y `50.right == null`. Entonces la posición de inserción es el hijo derecho de 50. La búsqueda fallida no necesita repetirse, el mismo camino que demuestra que 55 no existe determina dónde debe añadirse.

### **<mark>22. Inserción en un BST</mark>**

Queremos implementar `boolean add(int x)`. La operación debe localizar la posición, rechazar un duplicado, crear el nuevo nodo, enlazarlo correctamente, mantener `parent`, mantener `root` y actualizar `n`. Separamos la inserción en dos fases: **<mark>localización con findLast(x) en O(h)</mark>** y **<mark>modificación estructural en O(1)</mark>**. Esta separación será importante también para analizar el costo.

### **<mark>23. Rechazo de duplicados</mark>**

Nuestra estructura representa un conjunto, así que una clave debe aparecer como máximo una vez. Supongamos `findLast(30) -> nodo 30` y queremos `add(30)`; como `p.x == x`, la clave ya existe, y la operación debe retornar `false` sin modificar `root`, `left`, `right`, `parent` ni `n`. En particular, `n` no aumenta.

Podríamos crear un nodo nuevo inmediatamente y descubrir después que la clave ya existe, pero ese nodo no sería utilizado. Es más claro buscar primero y crear el nodo solo después de confirmar que la clave no existe.

### **<mark>24. Primera inserción y mantenimiento de root</mark>**

Árbol vacío: `root = null` y `n = 0`. Si `add(40)` da `findLast(40) == null`, el nuevo nodo se convierte en raíz: **<mark>root = u; root.parent = null; n = 1;</mark>**.

Para un árbol no vacío debe cumplirse siempre `root != null` y `root.parent == null`, porque el primer nodo no tiene padre.

### **<mark>25. Uso de parent durante la inserción</mark>**

Supongamos que queremos insertar 55 y ya sabemos que `p = nodo 50`. Creamos `Node u = new Node(55);`. Como `55 > 50`, ejecutamos `p.right = u;`, pero esto solo establece una dirección de la relación; también debemos mantener `u.parent = p;`. El estado coherente es `50.right == u` y `u.parent == 50`.

El **<mark>invariante local de parent</mark>** exige que, para todo hijo existente: **<mark>u.left.parent == u</mark>** y **<mark>u.right.parent == u</mark>**. Este tipo de relación recuerda la lista doblemente enlazada de la Semana 2, donde teníamos `u.next.prev == u`; ahora tenemos `u.left.parent == u` y `u.right.parent == u`. Una estructura enlazada correcta debe ser coherente entre referencias relacionadas.

### **<mark>26. Decidir left o right al insertar</mark>**

Después de `findLast(x)` tenemos un nodo `p`. Si la clave no existe, sabemos que uno de los enlaces adecuados de `p` es `null`. Si `x < p.x`: **<mark>p.left = u; u.parent = p;</mark>**. Si `x > p.x`: **<mark>p.right = u; u.parent = p;</mark>**. La comparación final de la búsqueda determina el lado de inserción.

### **<mark>27. Implementación de add(int x)</mark>**

Una implementación completa para esta semana es:

```java
boolean add(int x) {
    Node p = findLast(x);

    if (p != null && p.x == x) {
        return false;
    }

    Node u = new Node(x);

    if (p == null) {
        root = u;
    } else {
        u.parent = p;

        if (x < p.x) {
            p.left = u;
        } else {
            p.right = u;
        }
    }

    n++;
    return true;
}
```

No son casos independientes sin relación entre sí. La lógica completa es: buscar; si ya existe, rechazar; si no existe, crear el nodo; si no había árbol, convertirlo en `root`; si ya había árbol, usar el último nodo visitado y la comparación para enlazar; finalmente, actualizar `n`.

### **<mark>28. Traza de inserciones</mark>**

Partimos de un árbol vacío y ejecutamos `add(40)`, `add(20)`, `add(60)`, `add(10)`, `add(30)`, `add(50)`, `add(70)`.

`add(40)` encuentra el árbol vacío (`findLast(40) = null`), y 40 se convierte en raíz, con `n = 1`. `add(20)` compara `20 < 40` y encuentra `left == null`, dejando 20 como hijo izquierdo de 40 (`20.parent = 40`, `n = 2`). `add(60)` compara `60 > 40` y encuentra `right == null`, dejando 60 como hijo derecho de 40 (`n = 3`). `add(10)` sigue el camino `40 -> 20 -> left null` y queda como hijo izquierdo de 20. `add(30)` sigue `40 -> 20 -> right null` y queda como hijo derecho de 20. `add(50)` sigue `40 -> 60 -> left null` y queda como hijo izquierdo de 60. `add(70)` sigue `40 -> 60 -> right null` y queda como hijo derecho de 60.

El resultado final es:

```text
       40
     /    \
   20      60
  /  \    /  \
10   30  50   70
```

con `n = 7`.

### **<mark>29. Intentar insertar un duplicado</mark>**

Ahora ejecutamos `add(30)`. El camino es `40 -> 20 -> 30`, y como `30 == 30`, `findLast(30)` retorna el nodo existente; entonces `p != null` y `p.x == x`, así que la operación retorna `false`. El árbol sigue siendo el mismo de siete nodos y `n = 7`; no aparece un segundo 30.

### **<mark>30. Mantenimiento de n</mark>**

La variable `private int n;` almacena el número de nodos que pertenecen al árbol, así que `size() -> n` puede responderse en **O(1)**. `n` aumenta únicamente cuando un nuevo nodo se incorpora realmente, en una inserción aceptada; si un duplicado se rechaza, `n` no cambia.

Un invariante importante es que **<mark>n debe ser exactamente el número de nodos alcanzables desde root siguiendo left y right</mark>**. Un valor de `n` correcto no puede compensar enlaces incorrectos; la representación completa debe ser coherente.

### **<mark>31. Invariantes de representación del BST</mark>**

Podemos reunir varias propiedades. **<mark>Invariantes de Representación Estructural:</mark>**

- Árbol vacío: **<mark>n == 0 <=> root == null</mark>**.
- Raíz no vacía: **<mark>root != null y root.parent == null</mark>**.
- Coherencia bidireccional: **<mark>u.left.parent == u</mark>** y **<mark>u.right.parent == u</mark>**. Como tamaño, el número de nodos alcanzables desde `root` mediante `left` y `right` debe ser exactamente `n`. Como orden BST, para todo nodo `u`, todo valor del subárbol izquierdo es menor que `u.x` y todo valor del subárbol derecho es mayor. Finalmente, como claves distintas, no existen dos nodos diferentes con el mismo `x`. Una operación es correcta cuando produce el resultado esperado y además conserva estas propiedades.

### **<mark>32. Recorrer un árbol</mark>**

En una secuencia ya existe un orden lineal natural, por ejemplo `10 -> 20 -> 30`, y podemos seguir `next`. En un árbol no existe un único "siguiente" definido por la representación; debemos decidir un **orden de recorrido**. Un recorrido especifica en qué momento visitamos el nodo actual, el subárbol izquierdo y el subárbol derecho. Durante esta semana solamente necesitamos desarrollar en profundidad el recorrido **inorder**, porque su relación con el invariante del BST es directa.

### **<mark>33. Recorrido inorder</mark>**

El recorrido inorder utiliza el orden subárbol izquierdo, nodo, subárbol derecho (`left, node, right`). Para el árbol completo de siete nodos, el resultado es `10 20 30 40 50 60 70`.

### **<mark>34. Implementación recursiva de inorder</mark>**

El archivo de esta semana contiene:

```java
void inorder() {
    inorder(root);
    System.out.println();
}

private void inorder(Node u) {
    **<mark>if (u == null) return;</mark>** // Caso base

    **<mark>inorder(u.left);</mark>**
    **<mark>System.out.print(u.x + " ");</mark>**
    **<mark>inorder(u.right);</mark>**
}
```

La **<mark>versión pública inicia desde root</mark>**; la **<mark>versión recursiva procesa un subárbol (patrón wrapper / helper)</mark>**.

### **<mark>35. Por qué la recursión encaja naturalmente con árboles</mark>**

Un árbol binario se describe recursivamente: **<mark>cada subárbol hijo vuelve a ser un árbol binario completo</mark>**. Por eso la recursión encaja de forma natural. Por eso una operación sobre todo el árbol puede expresarse como resolver el subárbol izquierdo, resolver el nodo y resolver el subárbol derecho. El caso base es `u == null`, porque no existe ningún nodo que procesar.

### **<mark>36. Traza de inorder sobre un subárbol</mark>**

Considera el subárbol `20` con hijo izquierdo 10 y derecho 30. Al ejecutar `inorder(20)`, primero se llama `inorder(10)`; dentro de 10, `inorder(null)` retorna, se imprime 10, y `inorder(null)` retorna de nuevo. Regresamos a 20 y se imprime 20. Luego se llama `inorder(30)`, que imprime 30. El resultado es `10 20 30`.

### **<mark>37. Por qué inorder produce claves ordenadas en un BST</mark>**

Esta propiedad no surge por casualidad. Para cualquier nodo `u`, el invariante garantiza que todas las claves del subárbol izquierdo son menores que `u.x`. El recorrido inorder imprime primero ese subárbol, después imprime `u.x`, y luego el invariante garantiza que todas las claves del subárbol derecho son mayores que `u.x`, que el recorrido imprime al final. Por tanto, **<mark>el orden es menores, nodo, mayores: en un BST inorder produce SIEMPRE una secuencia ordenada creciente</mark>**.

### **<mark>38. Inorder ordenado no verifica toda la representación</mark>**

Supongamos `40.left == 20` y `40.right == 60`, pero accidentalmente `20.parent == 60`. El recorrido inorder utiliza `left` y `right`, no utiliza `parent`, así que podría seguir produciendo `20 40 60` aunque el campo `parent` sea incorrecto. La lección es que **<mark>un recorrido inorder correcto NO demuestra que el campo parent sea correcto</mark>**. Esta idea continúa el enfoque de las semanas anteriores.

### **<mark>39. Profundidad y altura</mark>**

Para analizar el costo necesitamos describir qué tan largos pueden ser los caminos del árbol. La **profundidad** de un nodo mide qué tan lejos está de la raíz, usando la raíz como nivel inicial; por ejemplo, en `40` con hijo izquierdo `20` (que tiene hijo izquierdo 10) y derecho `60`, 40 tiene profundidad 0, sus hijos profundidad 1, y 10 profundidad 2.

Para mantener coherencia con las implementaciones Java posteriores del curso, utilizaremos durante esta secuencia la convención de que un **<mark>convención del curso: árbol vacío h = 0, hoja h = 1</mark>**. La **altura h** es el número de nodos del camino más largo desde la raíz hasta una hoja. El árbol completo de siete nodos tiene `h = 3`, porque un camino más largo contiene tres nodos, como `40 -> 20 -> 10` o `40 -> 60 -> 70`.

Algunos libros cuentan aristas en lugar de nodos; con esa convención el mismo árbol tendría altura 2. La diferencia es una constante, y para el análisis asintótico **O(h)** no cambia. Lo importante es usar una convención de manera consistente.

### **<mark>40. El costo de la búsqueda depende de h</mark>**

`findLast(x)` sigue un solo camino; no recorre primero todo el subárbol izquierdo y después el derecho. En cada nodo decide exactamente una continuación, izquierda o derecha. En el peor caso puede recorrer un camino desde la raíz hasta una hoja, de longitud proporcional a `h`. Por tanto, **<mark>findLast(x) -> O(h)</mark>**. Como `contains(x)` llama a `findLast(x)` y luego realiza trabajo constante, **<mark>contains(x) -> O(h)</mark>**.

### **<mark>41. El costo de add también depende de h</mark>**

La inserción tiene dos fases. La localización (`findLast(x)`) puede recorrer hasta `h` nodos, con costo **<mark>localización: O(h)</mark>**. Una vez conocido `p`, la modificación (insertar) cambia una cantidad constante de referencias (`u.parent`, `p.left` o `p.right`, `n`), con costo **<mark>modificación: O(1)</mark>**. Por tanto, `O(h) + O(1)` se simplifica a **<mark>add(x) -> O(h)</mark>**; **<mark>la localización domina el costo</mark>**.

### **<mark>42. Un BST no garantiza O(log n) (¡Trampa de Examen!)</mark>**

Esta es una de las ideas más importantes de la semana. Es frecuente escuchar "BST, búsqueda O(log n)", pero **<mark>esa afirmación es falsa e incompleta</mark>**. La afirmación correcta para un BST no balanceado es **<mark>búsqueda e inserción en O(h)</mark>**. La relación entre `h` y `n` depende de la forma del árbol.

### **<mark>43. Árbol de poca altura</mark>**

Insertando `40, 20, 60, 10, 30, 50, 70` en ese orden obtenemos el árbol balanceado de siete nodos, con `n = 7` y `h = 3`. En un árbol cuya altura crece aproximadamente como `log n`, las operaciones basadas en un camino tienen costo **<mark>O(log n) en el mejor caso</mark>**.

### **<mark>44. Árbol degenerado</mark>**

Ahora insertamos exactamente las mismas claves, pero ya ordenadas, `10, 20, 30, 40, 50, 60, 70`. El resultado es una **<mark>cadena lineal degenerada</mark>**, `10 -> 20 -> 30 -> 40 -> 50 -> 60 -> 70`, cada uno como hijo derecho del anterior, con `n = 7` y `h = 7`. Buscar 70 ahora visita los siete nodos. El BST se comporta estructuralmente como una lista enlazada. En general, si `h` es proporcional a `n`, entonces **<mark>findLast, contains y add cuestan O(n)</mark>**. **<mark>Un BST simple NO impide esta degeneración</mark>**.

### **<mark>45. Mismas claves, diferente forma</mark>**

Este contraste es fundamental. Con las mismas siete claves podemos obtener un árbol balanceado de altura 3 o una cadena de altura 7. **<mark>Historia de inserciones -> Forma del árbol -> Altura h -> Longitud del camino -> Costo asintótico</mark>**.

### **<mark>46. Relación entre representación, forma y costo</mark>**

Esta semana vuelve a aparecer el principio central del curso. En la Semana 1, el arreglo ofrecía acceso por índice en **O(1)**, pero la modificación interna podía exigir desplazamientos. En la Semana 2, la lista enlazada podía tener localización por índice en **O(n)**, pero modificación local en **O(1)**. En la Semana 3, restringimos las operaciones de `Stack` y `Queue` para elegir representaciones adecuadas a esa restricción. En la Semana 4, el BST impone un orden jerárquico para guiar la búsqueda, pero incluso dentro del mismo BST, la forma del árbol afecta el costo. La representación no es solamente el conjunto de campos de una clase; también importa cómo esos campos quedan conectados después de una historia de operaciones.

### **<mark>47. Qué significa que el BST sea una estructura ordenada</mark>**

Un BST no almacena necesariamente las claves físicamente en orden lineal; la raíz puede ser 40 aunque el menor valor sea 10. El orden está distribuido en la jerarquía, con los menores a la izquierda y los mayores a la derecha. Ese orden permite buscar, insertar y recorrer en orden. El recorrido inorder convierte ese orden jerárquico en una secuencia creciente.

### **<mark>48. Correctitud de la búsqueda</mark>**

Podemos justificar la búsqueda mediante el invariante. Supongamos que estamos en un nodo `w`. Si `x < w.x`, por el invariante todo valor del subárbol derecho es mayor que `w.x`, así que `x` no puede encontrarse allí, y continuar por `w.left` no descarta ninguna posición válida para `x`. Si `x > w.x`, por el invariante todo valor del subárbol izquierdo es menor que `w.x`, así que continuamos por `w.right`. Si `x == w.x`, la clave fue encontrada. Esta argumentación muestra que el algoritmo y el invariante no pueden estudiarse por separado.

### **<mark>49. Correctitud de la inserción</mark>**

Supongamos que `x` no existe y `p = findLast(x)`. La búsqueda terminó al intentar seguir una referencia `null` desde `p`. Si `x < p.x`, el lugar libre compatible con el camino es `p.left`; si `x > p.x`, el lugar libre compatible es `p.right`. El nuevo nodo se coloca exactamente en la posición a la que la búsqueda de `x` habría intentado continuar, y por eso se preserva el orden.

### **<mark>50. Casos que debe distinguir add</mark>**

Aunque no estamos estudiando eliminación todavía, la inserción ya exige distinguir algunos estados: árbol vacío (`p == null`, el nuevo nodo se convierte en `root`), clave existente (`p.x == x`, no se inserta), clave menor que `p.x` (`p.left = u; u.parent = p;`) y clave mayor que `p.x` (`p.right = u; u.parent = p;`). No es necesario introducir más casos estructurales esta semana.

### **<mark>51. Qué errores pueden romper el BST durante add</mark>**

**<mark>Catálogo de cinco errores típicos que Lara evalúa en los exámenes:</mark>** 1. **<mark>Elegir el lado incorrecto (rompe el invariante de orden BST)</mark>**. 2. **<mark>No mantener parent (el árbol parece ordenado en inorder pero la estructura interna queda rota)</mark>**. 3. **<mark>Aumentar n antes de detectar duplicados (desincroniza size() con los nodos reales)</mark>**. 4. **<mark>Sobrescribir root cuando el árbol no estaba vacío (pierde el árbol anterior en memoria)</mark>**. 5. **<mark>Permitir duplicados (viola la definición de conjunto ordenado con claves únicas)</mark>**.

### **<mark>52. Una batería mínima de razonamiento</mark>**

Para comprobar una implementación no basta usar una única secuencia. Conviene probar al menos árbol vacío, primera inserción, inserción a la izquierda, inserción a la derecha, búsqueda de la raíz, búsqueda de una clave interna, búsqueda ausente, duplicado, inorder, `size`, una historia que produzca poca altura y una historia que produzca un árbol degenerado. Estas pruebas obligan a verificar diferentes partes de la representación.

### **<mark>53. Ejemplo integrado</mark>**

Construimos el árbol insertando `40, 20, 60, 10, 30, 50, 70`, obteniendo el árbol balanceado de siete nodos con `root.x = 40` y `n = 7`. Las relaciones de `parent` son `20.parent = 40`, `60.parent = 40`, `10.parent = 20`, `30.parent = 20`, `50.parent = 60` y `70.parent = 60`. Las búsquedas dan `contains(30) -> true` y `contains(55) -> false`. La inserción duplicada `add(30)` retorna `false` y `n` sigue siendo 7. El recorrido inorder produce `10 20 30 40 50 60 70`. En cuanto a complejidad, `findLast`, `contains` y `add` cuestan **O(h)**, y para este árbol `h = 3`.

### **<mark>54. El archivo Java de la semana</mark>**

La representación central es:

```java
static class BinarySearchTree {
    static class Node {
        int x;
        Node left;
        Node right;
        Node parent;

        Node(int x) {
            this.x = x;
        }
    }

    private Node root;
    private int n;
}
```

`Node.x` es la clave almacenada; `Node.left` es la raíz del subárbol izquierdo; `Node.right` es la raíz del subárbol derecho; `Node.parent` es el padre del nodo; `root` es la raíz del BST completo; `n` es el número de nodos. Los métodos principales de esta semana son `findLast`, `contains`, `add`, `inorder` y `size`. No necesitamos agregar más operaciones para alcanzar el objetivo conceptual de la Semana 4.

### **<mark>55. Cómo leer findLast y add juntos</mark>**

No conviene estudiar `findLast` y `add` como dos algoritmos aislados. **<mark>findLast(x) reutiliza una sola búsqueda</mark>**: si existe alimenta a **<mark>contains</mark>**; si no existe determina la posición exacta de **<mark>add</mark>**. El diseño reutiliza una sola lógica de búsqueda.

### **<mark>56. Cómo leer inorder junto al invariante</mark>**

Tampoco conviene memorizar el orden `left, node, right` sin entender por qué. **<mark>El invariante BST (izquierda < nodo < derecha) es lo que hace que recorrer en inorder produzca una salida estrictamente creciente</mark>**. El recorrido obtiene su significado gracias al invariante.

### **<mark>57. Cómo leer O(h) junto a la representación</mark>**

La complejidad tampoco debe memorizarse como una tabla suelta. `findLast -> O(h)` porque el método sigue un único camino, y la longitud máxima de ese camino está limitada por `h`. `contains` reutiliza `findLast`, y `add` reutiliza `findLast` y después modifica unas pocas referencias. Por tanto, **<mark>findLast, contains y add cuestan todos O(h)</mark>**, y **<mark>la forma del árbol determina si h es logarítmica o lineal</mark>**.

### **<mark>58. Qué no significa O(h)</mark>**

`O(h)` no afirma que siempre visitamos exactamente `h` nodos; una búsqueda puede terminar antes. Por ejemplo, `contains(40)` en un árbol con raíz 40 termina en la raíz misma. El costo de peor caso se expresa mediante la longitud máxima que podría tener el camino relevante, no mediante la longitud real de cada búsqueda particular.

### **<mark>59. Qué significa razonablemente balanceado</mark>**

No estudiaremos todavía un algoritmo de balanceo. Usaremos la expresión **razonablemente balanceado** solamente de manera descriptiva, para distinguir árboles cuya altura es relativamente pequeña respecto de `n` de árboles que se aproximan a una cadena. El árbol de siete nodos con ramas de longitudes parecidas está razonablemente balanceado; una cadena que crece siempre hacia el mismo lado está claramente degenerada. No introducimos todavía rotaciones, factor de balance ni AVL; esos mecanismos corresponden a una etapa posterior.

### **<mark>60. Una pregunta que queda abierta</mark>**

Sabemos insertar una nueva clave, por ejemplo 55, cuya búsqueda `40 -> 60 -> 50 -> right null` nos dice dónde insertarla. Pero ahora imagina que queremos retirar 60. No resolveremos todavía esa operación, solo observa el problema: 60 conecta dos subárboles, el de 50 y el de 70. Cualquier solución futura tendrá que preservar la alcanzabilidad, `parent`, `root` si fuera necesario, `n` y el orden BST. La Semana 5 partirá de esas obligaciones.

### **<mark>61. Síntesis total</mark>**

La Semana 4 introduce la primera estructura jerárquica central del curso. Un BST combina un árbol binario, claves comparables y un invariante de orden. La representación utiliza `root`, `n`, y en cada nodo `x`, `left`, `right` y `parent`. El invariante establece que el subárbol izquierdo es menor que el nodo, que a su vez es menor que el subárbol derecho.

Gracias a ese invariante, una búsqueda no explora todo el árbol; en cada nodo decide ir a la izquierda si la clave es menor, a la derecha si es mayor, o detenerse si es igual. `findLast(x)` reutiliza ese camino y conserva el último nodo visitado, lo que permite construir `contains(x)` y también determinar la posición de `add(x)`. La inserción debe preservar `root`, `parent`, `n`, el orden BST y la unicidad de las claves.

El recorrido inorder visita izquierda, nodo, derecha, y produce las claves en orden creciente porque utiliza directamente la propiedad de orden del BST. Finalmente, `findLast`, `contains` y `add` cuestan todos **O(h)**. La altura depende de la forma del árbol, y la forma depende de cómo las inserciones construyeron la representación. Por eso la idea final de la semana es la cadena representación, forma, altura, camino, costo.

La siguiente semana conservará este mismo BST y planteará una operación más delicada, eliminar. Antes de estudiarla, la búsqueda, el invariante, `parent`, `root`, `n` e inorder deben estar completamente comprendidos.
