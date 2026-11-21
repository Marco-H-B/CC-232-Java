# CC232 - Algoritmos y Estructuras de Datos
# Solucionario Canónico: Práctica Calificada 3 --- Semanas 5 a 7 (Ciclo 2026-2)
**Universidad Nacional de Ingeniería --- Facultad de Ciencias**  
**Cátedra:** Prof. César Lara Ávila  
**Estudiante:** Marco Antonio Huamani Bonifacio (Código `20232741D`)  
**Modalidad:** Solucionario oficial razonado con evidencia exhaustiva

---

## Parámetros de la Evaluación y Regla General

| Parámetro | Detalle de Cátedra |
| :--- | :--- |
| **Duración oficial** | 150 minutos |
| **Puntaje total** | 20,0 puntos (4,0 puntos por problema) |
| **Alcance conceptual** | Semana 5 (BST con `parent` y `splice`), Semana 6 (AVL e inserción/rotaciones), Semana 7 (BinaryHeap y `heapify`) |
| **Criterio rector** | Coherencia entre ADT, invariantes de representación, localización vs modificación, y análisis asintótico formal. |

> **Regla General de Calificación Aplicada:** Todo resultado numérico o asintótico carente de desarrollo explícito obtiene 0 puntos. Toda afirmación algorítmica exige su trazado o pseudocódigo riguroso correspondiente. En todo análisis de complejidad temporal se desglosa el costo de localización del costo de modificación física en función de la altura $h$ y el tamaño $n$.

---

## Problema 1. BST: Invariante de `parent`, eliminación con `splice` y sucesor --- 4,0 puntos

### Contexto y Representación de Cátedra
Cada nodo $u$ mantiene claves enteras $u.x$, punteros a subárboles $u.left$, $u.right$ y referencia a su ascendente inmediato $u.parent$.
La desvinculación física de un nodo con a lo sumo un hijo la ejecuta la operación interna $O(1)$:
```java
private void splice(Node u) {
    Node s = (u.left != null) ? u.left : u.right;
    Node p = u.parent;
    if (u == root) {
        root = s;
        if (s != null) s.parent = null;
    } else {
        if (p.left == u) p.left = s;
        else p.right = s;
        if (s != null) s.parent = p;
    }
}
```

---

### Inciso a) Traza gráfica de las cuatro operaciones de eliminación (1,5 puntos)

#### Estado Inicial ($n = 12$)
```text
                 50
               /    \
             25      75
            /  \    /  \
          15    35 65   85
         /  \  / \   \    \
        10  20 30 40  70   90
```

#### Paso 1: `remove(10)`
- **Localización:** `findLast(10)` desciende por $50 \to 25 \to 15 \to 10$. Nodo localizado en profundidad 3.
- **Caso de eliminación:** Nodo hoja ($10.left == null$ y $10.right == null$).
- **Operación física:** Invocación directa a `splice(Node(10))`.
  - $s = null$.
  - $p = Node(15)$.
  - Como $p.left == Node(10)$, se ejecuta $p.left = null$.
  - $s == null$, por lo que no hay reasignación de `parent`.
  - `n` se decrementa a $11$.
- **Sustitución de clave lógica:** Ninguna (caso de desvinculación directa).

**Árbol tras `remove(10)` ($n = 11$):**
```text
                 50
               /    \
             25      75
            /  \    /  \
          15    35 65   85
            \  / \   \    \
            20 30 40  70   90
```

---

#### Paso 2: `remove(65)`
- **Localización:** `findLast(65)` desciende por $50 \to 75 \to 65$.
- **Caso de eliminación:** Nodo con exactamente un hijo ($65.left == null$, $65.right == Node(70)$).
- **Operación física:** Invocación directa a `splice(Node(65))`.
  - $s = Node(70)$.
  - $p = Node(75)$.
  - Como $p.left == Node(65)$, se actualiza $p.left = s$ ($75.left = Node(70)$).
  - Como $s \ne null$, se actualiza $s.parent = p$ ($70.parent = Node(75)$).
  - El nodo 65 queda aislado y es reclamado por el Garbage Collector.
  - `n` se decrementa a $10$.
- **Sustitución de clave lógica:** Ninguna (puenteo directo de punteros).

**Árbol tras `remove(65)` ($n = 10$):**
```text
                 50
               /    \
             25      75
            /  \    /  \
          15    35 70   85
            \  / \        \
            20 30 40       90
```

---

#### Paso 3: `remove(25)`
- **Localización:** `findLast(25)` desciende por $50 \to 25$.
- **Caso de eliminación:** Nodo con dos hijos no nulos ($25.left == Node(15)$, $25.right == Node(35)$).
- **Búsqueda del sucesor inorder ($w$):** Se toma el hijo derecho $w = 25.right = Node(35)$ y se desciende a la izquierda todo lo posible (`while (w.left != null) w = w.left`).
  - Como $35.left == Node(30)$ y $30.left == null$, el sucesor inorder es $w = Node(30)$.
- **Sustitución de clave lógica:** Se copia la clave del sucesor en el nodo original:
  $$\text{Node}(25).x = w.x = 30$$
- **Operación física:** Invocación a `splice(w)` sobre el nodo físico 30.
  - En el nodo 30: $30.left == null$ y $30.right == null$ (es hoja).
  - $s = null$.
  - $p = 30.parent = Node(35)$.
  - Como $p.left == Node(30)$, se actualiza $p.left = null$ ($35.left = null$).
  - `n` se decrementa a $9$.

**Árbol tras `remove(25)` ($n = 9$):**
```text
                 50
               /    \
             30      75
            /  \    /  \
          15    35 70   85
            \     \       \
            20     40      90
```

---

#### Paso 4: `remove(50)`
- **Localización:** Clave ubicada en la raíz ($u == root$).
- **Caso de eliminación:** Nodo con dos hijos no nulos ($50.left == Node(30)$, $50.right == Node(75)$).
- **Búsqueda del sucesor inorder ($w$):** Se toma $w = 50.right = Node(75)$ y se desciende a la izquierda:
  - $75.left == Node(70)$.
  - $70.left == null$.
  - El sucesor inorder es $w = Node(70)$.
- **Sustitución de clave lógica:** Se copia la clave en la raíz:
  $$root.x = w.x = 70$$
- **Operación física:** Invocación a `splice(w)` sobre el nodo físico 70.
  - En el nodo 70: $70.left == null$ y $70.right == null$.
  - $s = null$.
  - $p = 70.parent = Node(75)$.
  - Como $p.left == Node(70)$, se ejecuta $p.left = null$ ($75.left = null$).
  - `n` se decrementa a $8$.

**Árbol final resultante ($n = 8$):**
```text
                 70
               /    \
             30      75
            /  \       \
          15    35      85
            \     \       \
            20     40      90
```

---

### Inciso b) Modificación de punteros en `remove(25)` y propiedades del sucesor (0,75 puntos)

#### 1. Secuencia de mutación física en memoria para `remove(25)`
1. **Localización del sucesor:** Variable temporal $w$ apunta a `Node(30)`.
2. **Mutación de clave lógica:** `u.x = 30` (el objeto en memoria ubicado en la posición original de 25 pasa a almacenar 30; no se crea un nuevo nodo).
3. **Ejecución de `splice(w)`:**
   - Lectura: $s = (w.left \ne null) \text{ ? } w.left : w.right \implies s = null$.
   - Lectura: $p = w.parent \implies p = Node(35)$.
   - Rama condicional: como $u \ne root$, entra al bloque `else`.
   - Modificación del puntero del padre: $p.left == w \implies 35.left = null$.
   - Verificación de descendiente: $s == null$, no se ejecuta $s.parent = p$.
4. **Actualización de invariante de tamaño:** `n--` decrementa el total a 9.

#### 2. Demostración formal de por qué $w.left == null$ pero $w.right$ puede ser no nulo
- **Por qué $w.left$ siempre es `null`:** El sucesor inorder de un nodo $u$ con dos hijos es el elemento mínimo del subárbol derecho de $u$ ($\min(\text{subtree}(u.right))$). Por el invariante fundamental de un BST, para cualquier nodo $k$, todos los elementos de su subárbol izquierdo son estrictamente menores que $k$. Si el sucesor candidato $w$ tuviera un hijo izquierdo ($w.left \ne null$), cualquier clave en dicho subárbol izquierdo satisfaría $k < w.x \le \text{claves del subárbol derecho}$, lo cual contradice la minimalidad de $w$. Por contradicción, $w.left$ es obligatoriamente `null`.
- **Por qué $w.right$ puede ser no nulo:** El sucesor solo garantiza que no existen elementos menores que él en su propio subárbol, pero pueden existir elementos mayores que él contenidos dentro de su subárbol derecho (ejemplo: si el nodo 30 tuviera como hijo derecho al nodo 32, $32 > 30$).
- **Respuesta de `splice(w)` ante un hijo derecho:** Si $w.right \ne null$, la expresión condicional asigna $s = w.right$. Luego, en `p.left = s` se engancha dicho subárbol derecho al padre de $w$ (nodo 35), y la sentencia `s.parent = p` reasigna el puntero de vuelta hacia 35, preservando el orden BST y la consistencia bidireccional en $O(1)$.

---

### Inciso c) Análisis de `splice` defectuoso y posterior `remove(70)` (0,75 puntos)

#### 1. Corrección del recorrido inorder posterior
El recorrido inorder estándar se guía exclusivamente por referencias descendentes (`u.left` y `u.right`), partiendo desde `root`:
```java
void inorder(Node u) {
    if (u == null) return;
    inorder(u.left);
    System.out.print(u.x + " ");
    inorder(u.right);
}
```
Dado que el puntero descendente del padre $75.left = Node(70)$ fue correctamente asignado en el `splice` defectuoso, el recorrido inorder descendente **sigue siendo matemáticamente correcto** y emite la secuencia ordenada. El defecto es un estado corrupto de punteros ascendentes (*dangling parent reference*).

#### 2. Identificación del puntero roto
La referencia rota es **`Node(70).parent`**. En lugar de apuntar a su nuevo padre biológico `Node(75)`, conserva su puntero hacia el nodo desvinculado `Node(65)`:
$$70.parent \to \text{Node(65) [nodo huérfano / recolectado]}$$

#### 3. Consecuencias al invocar `remove(70)`
Cuando el sistema ejecuta posteriormente `remove(70)`:
1. `findLast(70)` desciende desde la raíz y encuentra exitosamente a `Node(70)`.
2. Como 70 es una hoja ($70.left == null$ y $70.right == null$), `remove` invoca `splice(Node(70))`.
3. Dentro de `splice(70)`:
   - $s = null$.
   - Se lee el padre: $p = 70.parent \implies p = Node(65)$.
   - Se evalúa la condición de reconexión:
     - `p.left == u` $\implies 65.left == Node(70)$ (falso).
     - `p.right == u` $\implies 65.right == Node(70)$ (verdadero, pues 65 tenía a 70 como hijo derecho antes de ser desconectado).
     - Se ejecuta: $p.right = s \implies 65.right = null$.
4. **Falla estructural catastrófica:**
   - La modificación ocurrió en el nodo muerto 65.
   - El padre real en el árbol (`Node(75)`) **jamás fue notificado ni modificado**: su puntero $75.left$ sigue apuntando tercamente a `Node(70)`.
   - El nodo 70 **nunca fue eliminado del árbol**. Si se ejecuta un nuevo `inorder()`, 70 seguirá apareciendo a pesar de que `n` decreció y `remove(70)` retornó `true`. Se corrompe el invariante de consistencia lógica frente al tamaño físico.

---

### Inciso d) Desglose de costos y refutación del $O(\log n)$ automático (1,0 punto)

#### 1. Separación rigurosa de costos
Toda eliminación en un BST con punteros al padre se descompone en dos fases disjuntas:

| Fase de la Operación | Acciones Concretas | Costo Temporal |
| :--- | :--- | :---: |
| **Localización (`findLast` + sucesor)** | Descenso por comparaciones desde la raíz hasta el nodo $u$. Si $u$ tiene dos hijos, descenso adicional por enlaces `left` en el subárbol derecho para hallar el sucesor $w$. Ambos caminos están acotados por la altura $h$. | $O(h)$ |
| **Modificación física (`splice` + copia)** | Copia escalar $u.x = w.x$, reasignación de a lo sumo 3 punteros de referencia (`p.left`/`p.right`, `root`, `s.parent`) y decremento de variable primitiva `n--`. | $O(1)$ |

$$\text{Costo Total}(\text{remove}) = \text{Localización} + \text{Modificación} = O(h) + O(1) = O(h)$$

#### 2. Por qué es un error conceptual grave escribir $O(\log n)$
El árbol BST sin mecanismos de balanceo no impone ninguna restricción sobre la diferencia de alturas entre subárboles hermanos. La altura $h$ es una variable aleatoria que depende estrictamente del orden cronológico de inserción:
- En el mejor caso (árbol binario completo): $h = \lfloor \log_2 n \rfloor \implies O(\log n)$.
- En el peor caso (claves insertadas en orden monótono): el árbol degenera en una lista enlazada donde $h = n - 1$. Por ende, afirmar $O(\log n)$ como cota general es falso; la cota formal para peor caso es $\Theta(n)$.

#### 3. Árbol degenerado donde `remove` cuesta $\Theta(n)$
Insertando la secuencia $10, 20, 30, \dots, n \cdot 10$:
```text
10
  \
   20
     \
      30
        \
         ...
            \
             n*10
```
Para ejecutar `remove(n * 10)`:
- `findLast` recorre una cadena lineal de $n$ nodos $\implies n - 1$ comparaciones descendentes.
- Costo de localización: $\Theta(n)$.
- Costo total: $\Theta(n) + O(1) = \Theta(n)$.

---

## Problema 2. AVL: Los cuatro casos de rotación y orden de actualización --- 4,0 puntos

### Contexto y Mecánica de Rotación
- Invariante de altura: $h(\text{hoja}) = 1$, $h(null) = 0$.
- Invariante de actualización:
  $$\text{updateHeight}(u) \implies u.height = 1 + \max(height(u.left), height(u.right))$$
- Factor de Balance:
  $$FE(u) = height(u.left) - height(u.right)$$
- Invariante de estructura AVL: $|FE(u)| \le 1$ para todo nodo $u$.

---

### Inciso a) Traza paso a paso de la inserción de claves (1,5 puntos)

Secuencia: `45, 20, 10, 30, 25, 60, 80, 70`

#### 1. Inserción de 45, 20, 10
- Inserta 45: raíz, $h=1, FE=0$.
- Inserta 20: hijo izquierdo de 45. $h(20)=1, h(45)=2, FE(45)=+1$.
- Inserta 10: hijo izquierdo de 20.
  - Al desapilar:
    - $h(10) = 1, FE(10) = 0$.
    - $h(20) = 2, FE(20) = 1 - 0 = +1$.
    - Nodo 45: $FE(45) = height(20) - height(null) = 2 - 0 = +2$ $\implies$ **Desbalance**.
  - **Diagnóstico:** Nodo crítico $u = 45$ ($FE = +2$). Su hijo izquierdo $20$ tiene $FE(20) = +1$ (mismo signo, positivo).
  - **Caso:** **LL (Left-Left)**.
  - **Solución:** `rotateRight(45)`.
    - Punteros: $x = 20$, $y = 45$, $middle = 20.right = null$.
    - $20.right = 45$, $45.left = null$.
    - Actualizaciones: $updateHeight(45) \to 1$, $updateHeight(20) \to 2$.
    - Nueva raíz local: $20$.

**Subárbol tras rebalanceo:**
```text
      20 (h=2, FE=0)
     /  \
   10    45
```

---

#### 2. Inserción de 30
- Desciende por $20 \to 45 \to 30$ ($30 < 45 \implies 45.left = Node(30)$).
- Alturas y balances en el retorno:
  - $h(30) = 1, FE(30) = 0$.
  - $h(45) = 2, FE(45) = 1 - 0 = +1$.
  - $h(20) = 1 + \max(1, 2) = 3, FE(20) = 1 - 2 = -1$.
- Todo el árbol satisface $|FE| \le 1$. No hay rotación.

**Estado:**
```text
         20 (h=3)
        /  \
      10    45
           /
          30
```

---

#### 3. Inserción de 25
- Desciende por $20 \to 45 \to 30 \to 25$ ($25 < 30 \implies 30.left = Node(25)$).
- Alturas y balances en el retorno:
  - $h(25) = 1, FE(25) = 0$.
  - $h(30) = 2, FE(30) = 1 - 0 = +1$.
  - Nodo 45: $FE(45) = height(30) - height(null) = 2 - 0 = +2$ $\implies$ **Desbalance**.
  - **Diagnóstico:** Nodo crítico más profundo detectado es $u = 45$ ($FE = +2$). Su hijo izquierdo $30$ tiene $FE(30) = +1$.
  - **Caso:** **LL en el subárbol derecho**.
  - **Solución:** `rotateRight(45)`.
    - Punteros: $x = 30$, $y = 45$, $middle = 30.right = null$.
    - $30.right = 45$, $45.left = null$.
    - $20.right$ pasa a ser 30.
    - $updateHeight(45) \to 1$, $updateHeight(30) \to 2$.
    - Nodo 20: $height(20.left) = 1$, $height(20.right) = 2 \implies h(20) = 3, FE(20) = 1 - 2 = -1$.

**Estado tras rebalanceo:**
```text
         20 (h=3)
        /  \
      10    30 (h=2)
           /  \
          25   45
```

---

#### 4. Inserción de 60, 80
- Inserta 60: $20 \to 30 \to 45 \to 60$. $45.right = Node(60)$. Alturas: $h(60)=1, h(45)=2, h(30)=3, h(20)=4$.
  - $FE(45) = 0 - 1 = -1$.
  - $FE(30) = 1 - 2 = -1$.
  - $FE(20) = 1 - 3 = -2$ $\implies$ **Desbalance en la raíz 20**.
  - Nodo 20 tiene $FE = -2$. Su hijo derecho 30 tiene $FE = -1$ (signos iguales, negativos).
  - **Caso:** **RR (Right-Right)**.
  - **Solución:** `rotateLeft(20)`.
    - $y = 30, x = 20, middle = 30.left = Node(25)$.
    - $30.left = 20$, $20.right = 25$.
    - $updateHeight(20) \to 1 + \max(h(10), h(25)) = 1 + \max(1, 1) = 2$.
    - $updateHeight(30) \to 1 + \max(h(20), h(45)) = 1 + \max(2, 2) = 3$.
    - Nueva raíz global: $30$.

**Estado tras `rotateLeft(20)`:**
```text
            30 (h=3, FE=0)
           /  \
         20    45 (h=2, FE=-1)
        /  \     \
      10    25    60
```
- Inserta 80: $30 \to 45 \to 60 \to 80$. $60.right = Node(80)$.
  - $h(80) = 1, FE(80) = 0$.
  - $h(60) = 2, FE(60) = -1$.
  - Nodo 45: $height(null) - height(60) = 0 - 2 = -2$ $\implies$ **Desbalance en 45**.
  - Hijo derecho 60 tiene $FE(60) = -1$.
  - **Caso:** **RR**.
  - **Solución:** `rotateLeft(45)`.
    - $60.left = 45$, $45.right = null$.
    - $30.right = Node(60)$.
    - $updateHeight(45) \to 1$, $updateHeight(60) \to 2$.
    - $h(30) = 1 + \max(2, 2) = 3, FE(30) = 0$.

**Estado:**
```text
            30 (h=3)
           /  \
         20    60 (h=2)
        /  \   / \
      10   25 45  80
```

---

#### 5. Inserción de 70
- Desciende por $30 \to 60 \to 80 \to 70$ ($70 < 80 \implies 80.left = Node(70)$).
- Alturas y balances en el retorno:
  - $h(70) = 1, FE(70) = 0$.
  - $h(80) = 2, FE(80) = 1 - 0 = +1$.
  - Nodo 60: $height(45) - height(80) = 1 - 2 = -1$.
  - Nodo 30: $height(20) - height(60) = 2 - 3 = -1$.
- **Verificación en el subárbol 80:** El nodo 80 no está desbalanceado ($|FE| = 1$). El nodo 60 tiene $FE = 1 - 2 = -1$. El nodo 30 tiene $FE = 2 - 3 = -1$.
- **Árbol AVL Completo Final ($n = 8$):**

```text
                 30
               /    \
             20      60
            /  \    /  \
          10    25 45   80
                       /
                      70
```

---

### Inciso b) Análisis crítico de `rotateRight` y orden de `updateHeight` (0,75 puntos)

#### 1. Justificación formal del orden causal
El método `rotateRight(Node y)` transmuta las referencias topológicas de modo que el nodo $y$ (antigua raíz local) pasa a ser el **hijo derecho** del nodo $x$ (nueva raíz local):
$$x.right = y$$
Por la definición matemática inductiva de altura:
$$height(u) = 1 + \max(height(u.left), height(u.right))$$
El valor correcto de $height(x)$ **depende funcionalmente del valor final de $height(y)$**. Por el principio de causalidad en computación secuencial, una variable que actúa como argumento en una función de agregación debe poseer su estado final antes de ser evaluada:
1. Se debe calcular primero `updateHeight(y)` computando los subárboles $y.left$ (antiguo $middle$) e $y.right$.
2. Con la altura de $y$ ya corregida y fijada en memoria, se ejecuta `updateHeight(x)`, que lee el nuevo $y.height$ a través del puntero $x.right$.

#### 2. Demostración con subárbol concreto y valor numérico erróneo
Consideremos la rotación sobre $y = 45$, con $x = 20$, donde $10$ es hijo de $20$ ($h(10) = 1$) y $y.right = null$:
```text
       y=45 (h_antigua = 2)
      /
    x=20 (h_antigua = 1)
    /
  10 (h = 1)
```
Si un programador invierte el orden:
```java
updateHeight(x); // ERROR: ejecutado en Paso 1
updateHeight(y); // ejecutado en Paso 2
```
1. Al ejecutarse `updateHeight(x)` en el Paso 1:
   - $x.left = 10 \implies height(x.left) = 1$.
   - $x.right = y \implies$ lee $y.height$. **¡Pero $y.height$ aún retiene su valor obsoleto anterior a la rotación ($y.height = 2$)!**
   - Se asigna erróneamente:
     $$x.height = 1 + \max(1, 2) = 3$$
2. En el Paso 2 se ejecuta `updateHeight(y)`:
   - $y.left = null \implies 0$.
   - $y.right = null \implies 0$.
   - $y.height = 1 + \max(0, 0) = 1$ (se corrige tarde para $y$).
3. **Resultado:** El nodo $x$ queda congelado con $x.height = 3$, cuando su altura topológica real es:
   $$x.height_{\text{real}} = 1 + \max(height(10), height(y)) = 1 + \max(1, 1) = 2$$
   Este desfase de $+1$ propaga factores de balance adulterados hacia los ancestros superiores, disparando rotaciones espurias o enmascarando desbalances reales futuros.

---

### Inciso c) Refutación del algoritmo de rotación ciega en caso LR (0,75 puntos)

#### 1. Refutación conceptual
La afirmación *“si $FE = +2$ la anomalía siempre está a la izquierda, por lo que basta una única `rotateRight`”* es falsa porque ignora la **topología de zigzag**.
Una rotación simple a la derecha pivota sobre el enlace $u-u.left$ asumiendo que el camino más pesado desciende de forma estrictamente exterior ($LL$: izquierda-izquierda). Si el desbalance fue originado en el subárbol interior ($LR$: izquierda-derecha), la masa del árbol está concentrada en el centro. Una rotación simple a la derecha traslada ese bloque central pesado al subárbol derecho de la nueva raíz sin reducir la profundidad máxima de las hojas, preservando o empeorando la violación de balance.

#### 2. Demostración mediante el contraejemplo con clave 25
Subárbol antes de insertar 25:
```text
      45 (h=2)
     /
   30 (h=1)
```
Se inserta 25 $\implies 30.left = Node(25)$.
Supongamos ahora el caso canónico de inserción interior tipo $LR$:
```text
      45 (h=3, FE=+2)
     /
   20 (h=2, FE=-1)
     \
      30 (h=1, FE=0)
```
Si se aplica la regla ingenua de ejecutar `rotateRight(45)` directo:
- $x = 20, y = 45$.
- $middle = x.right = Node(30)$.
- Se enlaza: $x.right = y \implies 20.right = 45$.
- Se enlaza: $y.left = middle \implies 45.left = 30$.

**Árbol obtenido tras la rotación errónea:**
```text
         20
           \
            45
           /
          30
```
- **Cálculo de factores de balance sobre el resultado:**
  - $h(30) = 1, FE(30) = 0$.
  - Nodo 45: $height(45.left) = 1, height(45.right) = 0 \implies h(45) = 2, FE(45) = 1 - 0 = +1$.
  - Nodo 20: $height(20.left) = 0, height(20.right) = h(45) = 2 \implies \mathbf{FE(20) = 0 - 2 = -2}$.
- **Conclusión:** El árbol resultante **no es un AVL válido**. La violación de balance sigue existiendo con la misma magnitud ($|FE| = 2$), desplazada de signo ($+2 \to -2$). Esto demuestra formalmente por qué el caso $LR$ exige una rotación previa a la izquierda sobre el hijo (`rotateLeft(u.left)`) para transformar el zigzag interior en una línea recta exterior ($LL$) antes de aplicar `rotateRight(u)`.

---

### Inciso d) Tabla de nodos del árbol final (0,5 puntos)

Para el árbol final obtenido tras insertar las 8 claves:

| Clave (`x`) | Subárbol Izquierdo (`left`) | Subárbol Derecho (`right`) | Altura (`height`) | Factor de Balance (`FE`) | Estado Local |
| :---: | :---: | :---: | :---: | :---: | :---: |
| **10** | `null` | `null` | 1 | $0 - 0 = 0$ | Hoja equilibrada |
| **25** | `null` | `null` | 1 | $0 - 0 = 0$ | Hoja equilibrada |
| **20** | `Node(10)` | `Node(25)` | 2 | $1 - 1 = 0$ | Subárbol perfectamente balanceado |
| **45** | `null` | `null` | 1 | $0 - 0 = 0$ | Hoja equilibrada |
| **70** | `null` | `null` | 1 | $0 - 0 = 0$ | Hoja equilibrada |
| **80** | `Node(70)` | `null` | 2 | $1 - 0 = +1$ | Invariante válido ($|FE| \le 1$) |
| **60** | `Node(45)` | `Node(80)` | 3 | $1 - 2 = -1$ | Invariante válido ($|FE| \le 1$) |
| **30** | `Node(20)` | `Node(60)` | 4 | $2 - 3 = -1$ | Raíz del AVL equilibrada |

---

### Inciso e) Árboles de Fibonacci y cota de altura para $n = 8$ (0,5 puntos)

#### 1. Cálculo de $N(h)$ hasta $h = 6$
Relación de recurrencia para la mínima cantidad de nodos de un AVL de altura $h$:
$$N(1) = 1, \quad N(2) = 2, \quad N(h) = 1 + N(h-1) + N(h-2) \quad (h \ge 3)$$

- $N(1) = 1$
- $N(2) = 2$
- $N(3) = 1 + N(2) + N(1) = 1 + 2 + 1 = 4$
- $N(4) = 1 + N(3) + N(2) = 1 + 4 + 2 = 7$
- $N(5) = 1 + N(4) + N(3) = 1 + 7 + 4 = 12$
- $N(6) = 1 + N(5) + N(4) = 1 + 12 + 7 = 20$

#### 2. Deducción de la altura máxima para $n = 8$
Para que un AVL alcance una altura $h$, la cantidad de nodos debe cumplir:
$$n \ge N(h)$$
Evaluando nuestros valores:
- Para alcanzar $h = 4$: se requieren al menos $N(4) = 7$ nodos. Como $8 \ge 7$, es posible un AVL de altura 4 con 8 nodos.
- Para alcanzar $h = 5$: se requieren al menos $N(5) = 12$ nodos. Como $8 < 12$, **es matemáticamente imposible construir un AVL válido con 8 nodos que tenga altura 5**.
- Por consiguiente:
  $$h_{\max}(\text{AVL con } 8 \text{ nodos}) = 4$$

#### 3. Comparación con BST sin balanceo
En un BST sin balanceo, las claves pueden insertarse en orden estrictamente ascendente, degenerando en una cadena lineal sin ramificaciones laterales:
$$h_{\max}(\text{BST con } 8 \text{ nodos}) = 8 \text{ niveles (o } 7 \text{ aristas)}$$
El mecanismo de balanceo automático del AVL reduce la altura máxima en un **$50\%$**, garantizando la cota logarítmica estricta $h \le 1.44 \log_2(n+2)$.

---

## Problema 3. AVL: Eliminación en cascada y operaciones en pseudocódigo --- 4,0 puntos

### Árbol Base ($n = 11, h = 4$)
```text
                 50 (h=4)
               /    \
             30      80 (h=3)
            /  \    /  \
          20    40 70   90 (h=2)
         /     /  / \     \
        10    35 60  75    95 (h=1)
```

---

### Inciso a) Eliminación de la clave 10 y rebalanceo en cascada (1,25 puntos)

#### 1. Desvinculación de 10 y retroceso recursivo
- `remove(10)` localiza el nodo 10 en el subárbol izquierdo de 20.
- Como 10 es hoja, su desvinculación retorna `null`, fijando $20.left = null$.
- Comienza el desapilado recursivo evaluando alturas y balances:
  1. **En nodo 20:**
     - $height(20.left) = 0$, $height(20.right) = 0$.
     - $updateHeight(20) \implies 20.height = 1 + \max(0, 0) = 1$.
     - $FE(20) = 0 - 0 = 0$. Válido.
  2. **En nodo 30:**
     - $height(30.left) = height(20) = 1$.
     - $height(30.right) = height(40) = 2$ (el nodo 40 tiene como hijo izquierdo a 35).
     - $updateHeight(30) \implies 30.height = 1 + \max(1, 2) = 3$.
     - $FE(30) = height(20) - height(40) = 1 - 2 = -1$. Válido ($|FE| \le 1$).

#### 2. Detección de desbalance en la raíz 50
  3. **En la raíz 50:**
     - Subárbol izquierdo (enraizado en 30): su altura se redujo a **$3$**.
     - Subárbol derecho (enraizado en 80):
       - $h(70) = 2$ (hijos 60 y 75).
       - $h(90) = 2$ (hijo 95).
       - $h(80) = 1 + \max(2, 2) = 3$.
     - Cálculo de balance en 50:
       $$FE(50) = height(30) - height(80) = 3 - 3 = 0$$
- **Conclusión de propagación:** La eliminación del nodo 10 redujo la altura del nodo 20 ($2 \to 1$), pero al llegar al nodo 30, la presencia del subárbol hermano derecho 40 ($h=2$) amortiguó la contracción, haciendo que $h(30)$ se mantuviera en 3 ($1 + \max(1, 2) = 3$).
- Por lo tanto, $FE(50)$ no se vio alterado y se mantiene en balance perfecto ($FE = 0$). No se requirieron rotaciones en esta llamada particular.

#### 3. Justificación de $O(\log n)$ rotaciones en el peor caso de eliminación vs $O(1)$ en inserción
- **En Inserción:** La inserción de una clave añade exactamente un nodo en una hoja, incrementando la altura del subárbol en $+1$. Cuando una rotación simple o doble rebalancea el nodo crítico más profundo, la altura del subárbol rebalanceado **retorna exactamente a la altura que tenía antes de la inserción**. En consecuencia, ningún ancestro superior ve alterada su altura y el proceso de balanceo termina inmediatamente ($O(1)$ rotaciones).
- **En Eliminación:** La desvinculación reduce la altura de un subárbol en $-1$. La rotación que rebalancea el nodo desbalanceado puede restaurar el balance local, pero la nueva raíz local resultante puede tener una altura **$1$ unidad menor que antes de la eliminación**. Esta pérdida de altura se transmite hacia arriba como un nuevo desbalance en el padre, pudiendo propagarse de manera inductiva a lo largo de todo el camino ascendente hasta la raíz. En el peor caso, se requiere ejecutar hasta $\lfloor h/2 \rfloor = O(\log n)$ rotaciones independientes.

---

### Inciso b) Pseudocódigo riguroso de `deleteMax(Node u)` (1,25 puntos)

#### 1. Estrategia Algorítmica y Casos Borde
El método recursivo explota el invariante BST: el máximo elemento siempre se encuentra siguiendo la rama derecha hasta encontrar un nodo que carezca de hijo derecho ($u.right == null$).
- **Caso base ($u.right == null$):** El nodo $u$ es el máximo local. Puede ser una hoja o tener un único hijo izquierdo $u.left$. Se retorna $u.left$ como nueva raíz del subárbol (puenteando a $u$ y permitiendo su recolección).
- **Paso inductivo ($u.right \ne null$):** Se invoca recursivamente `deleteMax(u.right)`. En el retorno, se reasigna el puntero $u.right = \text{resultado}$, se actualiza la altura de $u$ con `updateHeight(u)` y se invoca `rebalance(u)` para neutralizar cualquier desbalance generado por la contracción del subárbol derecho.
- **Precondición:** $u \ne null$ (el árbol o subárbol no está vacío).
- **Postcondición:** Retorna la raíz del subárbol habiendo eliminado físicamente su clave máxima, satisfaciendo estrictamente el invariante AVL ($|FE| \le 1$ y alturas actualizadas).

#### 2. Pseudocódigo Riguroso
```java
// Precondicion: u != null
// Postcondicion: elimina la clave maxima del subarbol u, rebalancea y retorna la nueva raiz local
Node deleteMax(Node u) {
    // Caso base: se alcanzo el nodo maximo del subarbol
    if (u.right == null) {
        return u.left; // Desvincula u en O(1), preservando su subarbol izquierdo si existe
    }

    // Paso recursivo: descender hacia la derecha
    u.right = deleteMax(u.right);

    // Fase de desapilado: restaurar invariantes ascendentes
    updateHeight(u);

    // Rebalancear localmente ante posible caso LL o LR provocado por la caida de u.right
    return rebalance(u);
}
```

---

### Inciso c) Costo temporal y eliminación de punteros `parent` (0,75 puntos)

#### 1. Demostración asintótica del peor caso
- En cada nivel de recursión, `deleteMax` ejecuta una comprobación condicional $O(1)$ y desciende una unidad en el árbol hacia la derecha.
- La profundidad de la recursión está acotada estrictamente por la altura $h$ del árbol AVL.
- En la fase de retorno recursivo, en cada nivel se ejecutan operaciones elementales:
  - `updateHeight(u)`: llamada a `Math.max` de alturas $\implies O(1)$.
  - `rebalance(u)`: cálculo de $FE$ y a lo sumo una rotación simple o doble $\implies O(1)$.
- Por tanto, la ecuación de recurrencia temporal es:
  $$T(h) = T(h - 1) + O(1) \implies T(h) = O(h)$$
- Dado que el invariante AVL garantiza formalmente $h \le 1.44 \log_2(n + 2) = O(\log n)$ para cualquier topología válida:
  $$\text{Costo}(\text{deleteMax}) = O(\log n) \text{ en el peor caso}$$

#### 2. Por qué no se requieren punteros `parent`
El diseño recursivo delega la memoria topológica de los ancestros al **Call Stack (pila de ejecución de la JVM)**. Cuando la función desciende, cada llamada apila en su *stack frame* la referencia a la dirección en el heap del nodo actual. En el momento en que el caso base retorna, la ejecución se reanuda en el *stack frame* del padre biológico exacto, permitiéndole reasignar `u.right = resultado` y rebalancearse sin necesidad de consumir 8 bytes adicionales por nodo para un puntero `parent` en el heap.

---

### Inciso d) Operación `countInRange` con poda inteligente (0,75 puntos)

#### 1. Estrategia de Poda y Justificación
Un recorrido ciego (como `inorder` o `preorder`) inspecciona recursivamente los $n$ nodos, costando $\Theta(n)$ independientemente del rango solicitado.
La poda inteligente compara la clave del nodo actual $u.x$ contra los límites del intervalo cerrado $[low, high]$:
- Si $u.x < low$: por la propiedad BST, todos los nodos del subárbol izquierdo satisfacen $v.x < u.x < low$, por lo que **se poda completamente el subárbol izquierdo** (cero llamadas recursivas sobre $u.left$).
- Si $u.x > high$: análogamente, todos los nodos del subárbol derecho satisfacen $w.x > u.x > high$, por lo que **se poda completamente el subárbol derecho**.
- Si $low \le u.x \le high$: la clave actual aporta $+1$ al conteo y se deben explorar ambas ramas.

#### 2. Pseudocódigo Riguroso
```java
// Precondicion: low <= high
// Postcondicion: retorna la cantidad exacta de claves en [low, high] sin visitar ramas invalidas
int countInRange(Node u, int low, int high) {
    if (u == null) {
        return 0;
    }

    // Poda 1: todo el subarbol izquierdo esta fuera de rango por cota inferior
    if (u.x < low) {
        return countInRange(u.right, low, high);
    }

    // Poda 2: todo el subarbol derecho esta fuera de rango por cota superior
    if (u.x > high) {
        return countInRange(u.left, low, high);
    }

    // Caso intermedio: u.x pertenece al intervalo; se contabiliza y se expande a ambos lados
    return 1 + countInRange(u.left, low, high) + countInRange(u.right, low, high);
}
```

#### 3. Complejidad en el Mejor Caso
En el mejor caso, el intervalo $[low, high]$ cae completamente a la izquierda del mínimo o a la derecha del máximo del árbol, o define un rango puntual que no intersecta con los datos. En ese escenario, el algoritmo desciende por un único camino unidireccional hasta llegar a `null`, realizando una única llamada recursiva por nivel. Como la altura de un AVL está acotada por $O(\log n)$, el costo en el mejor caso es **$O(\log n)$**, superando ampliamente la cota rígida $\Theta(n)$ del recorrido ciego.

---

## Problema 4. BinaryHeap: `heapify` $O(n)$, operaciones y forma --- 4,0 puntos

### Contexto y Representación
- Arreglo `Integer[] a`, contador `n = 10`, `a.length = 16`.
- Fórmulas de navegación base 0:
  $$\text{left}(i) = 2i + 1, \quad \text{right}(i) = 2i + 2, \quad \text{parent}(i) = \lfloor (i - 1)/2 \rfloor$$
- Invariante de Min-Heap: $a[\text{parent}(i)] \le a[i]$ para todo $0 < i < n$.

Arreglo inicial:
```text
índice:   0   1   2   3   4   5   6   7   8   9
a:      [22, 14, 35, 10,  8, 28, 40,  5, 12,  6]
```

---

### Inciso a) Construcción de Floyd con `heapify()` (1,5 puntos)

#### 1. Índice de inicio y omisión de hojas
El algoritmo inicia en:
$$i = \lfloor n/2 \rfloor - 1 = \lfloor 10/2 \rfloor - 1 = 5 - 1 = 4$$
- **Por qué se omiten los índices $> 4$:** Los índices desde $n/2 = 5$ hasta $n - 1 = 9$ corresponden a los nodos hoja del montículo. Un subárbol de 1 solo elemento no tiene descendientes; por definición satisface trivialmente la propiedad de orden min-heap. Aplicar `trickleDown` sobre una hoja generaría una comprobación fallida inmediata ($left(i) \ge n$). Procesar únicamente los nodos internos que poseen al menos un hijo ahorra trabajo computacional innecesario y fundamenta la cota lineal.

#### 2. Trazado paso a paso de `trickleDown(i)`

##### Paso $i = 4$:
- Elemento: $a[4] = 8$.
- Hijos: $left(4) = 9 \implies a[9] = 6$; $right(4) = 10 \ge n$ (no existe hijo derecho).
- Comparación: $\min(a[4], a[9]) = \min(8, 6) = 6$ en índice 9.
- Como $a[9] < a[4]$ ($6 < 8$), se ejecuta $\text{swap}(4, 9)$.
- Estado tras $i = 4$:
  `[22, 14, 35, 10, 6, 28, 40, 5, 12, 8]` (1 intercambio).

##### Paso $i = 3$:
- Elemento: $a[3] = 10$.
- Hijos: $left(3) = 7 \implies a[7] = 5$; $right(3) = 8 \implies a[8] = 12$.
- Menor de los hijos: $\min(a[7], a[8]) = \min(5, 12) = 5$ en índice 7.
- Comparación con el padre: $a[7] < a[3]$ ($5 < 10$), se ejecuta $\text{swap}(3, 7)$.
- Nueva posición de 10 es $i = 7$. Como $left(7) = 15 \ge n$, es hoja y termina.
- Estado tras $i = 3$:
  `[22, 14, 35, 5, 6, 28, 40, 10, 12, 8]` (1 intercambio).

##### Paso $i = 2$:
- Elemento: $a[2] = 35$.
- Hijos: $left(2) = 5 \implies a[5] = 28$; $right(2) = 6 \implies a[6] = 40$.
- Menor de los hijos: $\min(a[5], a[6]) = \min(28, 40) = 28$ en índice 5.
- Comparación con el padre: $a[5] < a[2]$ ($28 < 35$), se ejecuta $\text{swap}(2, 5)$.
- Nueva posición de 35 es $i = 5$. Como $left(5) = 11 \ge n$, es hoja y termina.
- Estado tras $i = 2$:
  `[22, 14, 28, 5, 6, 35, 40, 10, 12, 8]` (1 intercambio).

##### Paso $i = 1$:
- Elemento: $a[1] = 14$.
- Hijos: $left(1) = 3 \implies a[3] = 5$; $right(1) = 4 \implies a[4] = 6$.
- Menor de los hijos: $\min(a[3], a[4]) = \min(5, 6) = 5$ en índice 3.
- Comparación: $5 < 14$, se ejecuta $\text{swap}(1, 3)$.
- El valor 14 pasa al índice $i = 3$:
  - Hijos de 3: $left(3) = 7 \implies a[7] = 10$; $right(3) = 8 \implies a[8] = 12$.
  - Menor de los hijos: $\min(10, 12) = 10$ en índice 7.
  - Comparación: $a[7] < a[3]$ ($10 < 14$), se ejecuta $\text{swap}(3, 7)$.
  - 14 pasa al índice 7 (hoja, termina).
- Estado tras $i = 1$:
  `[22, 5, 28, 10, 6, 35, 40, 14, 12, 8]` (2 intercambios).

##### Paso $i = 0$:
- Elemento: $a[0] = 22$.
- Hijos: $left(0) = 1 \implies a[1] = 5$; $right(0) = 2 \implies a[2] = 28$.
- Menor de los hijos: $a[1] = 5$.
- Comparación: $5 < 22$, se ejecuta $\text{swap}(0, 1)$.
- El valor 22 pasa al índice $i = 1$:
  - Hijos de 1: $left(1) = 3 \implies a[3] = 10$; $right(1) = 4 \implies a[4] = 6$.
  - Menor de los hijos: $a[4] = 6$.
  - Comparación: $6 < 22$, se ejecuta $\text{swap}(1, 4)$.
- El valor 22 pasa al índice $i = 4$:
  - Hijos de 4: $left(4) = 9 \implies a[9] = 8$; $right(4) = 10 \ge n$ (no existe).
  - Menor de los hijos: $a[9] = 8$.
  - Comparación: $8 < 22$, se ejecuta $\text{swap}(4, 9)$.
  - 22 pasa al índice 9 (hoja, termina).
- Estado tras $i = 0$:
  `[5, 6, 28, 10, 8, 35, 40, 14, 12, 22]` (3 intercambios).

#### 3. Árbol casi completo final y balance de intercambios
- **Total exacto de intercambios (`swap`):** $1 + 1 + 1 + 2 + 3 = \mathbf{8 \text{ intercambios}}$.
- **Arreglo físico final:**
  `a = [5, 6, 28, 10, 8, 35, 40, 14, 12, 22]`, con $n = 10$.

```text
                 5
               /   \
             6      28
            / \    /  \
          10   8  35   40
         /  \  /
        14 12 22
```

---

### Inciso b) Operaciones de extracción e inserción (1,0 punto)

#### 1. Primera operación: `remove()`
- Guarda el mínimo: $min = a[0] = 5$.
- Copia el último elemento en la raíz y libera celda:
  $$a[0] = a[n - 1] = a[9] = 22; \quad a[9] = null; \quad n = 9$$
- Invocación a `trickleDown(0)`:
  - En $i = 0$ ($val = 22$): hijos $a[1]=6, a[2]=28 \implies \min = 6$. Como $6 < 22 \implies \text{swap}(0, 1)$.
  - En $i = 1$ ($val = 22$): hijos $a[3]=10, a[4]=8 \implies \min = 8$. Como $8 < 22 \implies \text{swap}(1, 4)$.
  - En $i = 4$ ($val = 22$): hijos $left(4) = 9 \ge n$ (no hay hijos). Termina.
- **Retorna:** `5`.
- **Estado:** `a = [6, 8, 28, 10, 22, 35, 40, 14, 12, _]`, $n = 9$.

#### 2. Segunda operación: `remove()`
- Guarda el mínimo: $min = a[0] = 6$.
- Copia el último elemento en la raíz:
  $$a[0] = a[n - 1] = a[8] = 12; \quad a[8] = null; \quad n = 8$$
- Invocación a `trickleDown(0)`:
  - En $i = 0$ ($val = 12$): hijos $a[1]=8, a[2]=28 \implies \min = 8$. Como $8 < 12 \implies \text{swap}(0, 1)$.
  - En $i = 1$ ($val = 12$): hijos $a[3]=10, a[4]=22 \implies \min = 10$. Como $10 < 12 \implies \text{swap}(1, 3)$.
  - En $i = 3$ ($val = 12$): hijos $left(3) = 7 \implies a[7] = 14$; $right(3) = 8 \ge n$. Como $12 < 14$, el elemento ya es menor que sus hijos válidos. Termina.
- **Retorna:** `6`.
- **Estado:** `a = [8, 10, 28, 12, 22, 35, 40, 14, _, _]`, $n = 8$.

#### 3. Tercera operación: `add(4)`
- Inserción en la primera posición disponible:
  $$a[n] = a[8] = 4; \quad n = 9$$
- Invocación a `bubbleUp(8)`:
  - En $i = 8$ ($val = 4$): padre $p = parent(8) = \lfloor (8 - 1)/2 \rfloor = 3 \implies a[3] = 12$.
    - Como $a[8] < a[3]$ ($4 < 12$), se ejecuta $\text{swap}(8, 3)$.
  - En $i = 3$ ($val = 4$): padre $p = parent(3) = \lfloor (3 - 1)/2 \rfloor = 1 \implies a[1] = 10$.
    - Como $a[3] < a[1]$ ($4 < 10$), se ejecuta $\text{swap}(3, 1)$.
  - En $i = 1$ ($val = 4$): padre $p = parent(1) = 0 \implies a[0] = 8$.
    - Como $a[1] < a[0]$ ($4 < 8$), se ejecuta $\text{swap}(1, 0)$.
  - En $i = 0$: se alcanzó la raíz ($i == 0$). Termina el ascenso.
- **Estado final:**
  `a = [4, 8, 28, 10, 22, 35, 40, 14, 12, _]`, con $n = 9$.

---

### Inciso c) Demostración de $O(n)$ de `heapify()` vs $O(n \log n)$ de inserciones sucesivas (0,75 puntos)

#### 1. La Sumatoria por Niveles en `heapify()`
En un árbol binario casi completo de altura $H = \lfloor \log_2 n \rfloor$, la cantidad de nodos que se encuentran a una altura $k$ del fondo (donde $k = 0$ son las hojas y $k = H$ es la raíz) está acotada por:
$$\text{Nodos a altura } k \le \left\lceil \frac{n}{2^{k+1}} \right\rceil$$
Dado que el costo en el peor caso de `trickleDown` sobre un nodo a altura $k$ es proporcional a la distancia máxima que puede descender ($k$ niveles, $O(k)$ pasos):
$$T(n) = \sum_{k=0}^{\lfloor \log_2 n \rfloor} \left\lceil \frac{n}{2^{k+1}} \right\rceil c \cdot k \le c \cdot n \sum_{k=0}^{\infty} \frac{k}{2^{k+1}} = \frac{c \cdot n}{2} \sum_{k=0}^{\infty} \frac{k}{2^k}$$

#### 2. Convergencia de la Serie de Potencias
Consideremos la serie geométrica convergente para $|x| < 1$:
$$\sum_{k=0}^{\infty} x^k = \frac{1}{1 - x}$$
Derivando término a término respecto a $x$:
$$\sum_{k=1}^{\infty} k x^{k-1} = \frac{1}{(1 - x)^2}$$
Multiplicando por $x$:
$$\sum_{k=0}^{\infty} k x^k = \frac{x}{(1 - x)^2}$$
Evaluando en $x = 1/2$:
$$\sum_{k=0}^{\infty} \frac{k}{2^k} = \frac{1/2}{(1 - 1/2)^2} = \frac{1/2}{1/4} = 2$$
Sustituyendo este resultado exacto en nuestra cota de costo:
$$T(n) \le \frac{c \cdot n}{2} \cdot 2 = c \cdot n \implies \mathbf{T(n) = O(n)}$$

#### 3. Por qué $n$ llamadas a `add(x)` cuestan $\Omega(n \log n)$ en el peor caso
Al construir el montículo insertando elementos secuencialmente con `add(x)`:
- Los elementos se insertan en las hojas y ascienden mediante `bubbleUp`.
- La mitad de los nodos del montículo ($n/2$ nodos) se ubican en el último nivel (profundidad $\approx \log_2 n$).
- Cada uno de estos $n/2$ nodos puede verse obligado a ascender todo el camino hasta la raíz ($\log_2 n$ comparaciones):
  $$T_{\text{add}}(n) \ge \sum_{i=n/2}^{n} \log_2 i \ge \frac{n}{2} \log_2\left(\frac{n}{2}\right) = \Omega(n \log n)$$
- **Conclusión de cátedra:** `heapify()` es asintóticamente superior porque la gran mayoría de los nodos (las hojas) no pagan ningún costo ($k=0$), y el costo de descenso más costoso ($k = \log_2 n$) se aplica a un único elemento (la raíz). En contraste, las inserciones sucesivas obligan a la mayoría de los nodos a pagar el costo máximo de ascenso.

---

### Inciso d) Refutación de la optimización de hueco en `remove()` (0,75 puntos)

#### 1. Invariante destruido: Propiedad de Forma
La propuesta sugiere dejar la raíz vacía y promover sucesivamente el menor de los hijos hacia arriba ($\text{corrimiento en cascada}$):
$$a[0] = \min(a[1], a[2])$$
repitiendo esto en el subárbol del hijo promovido hasta dejar un hueco en alguna hoja.
- **Invariante destruido:** Se destruye de forma irreversible el **invariante de forma del montículo (Árbol binario casi completo)**.
- El contrato formal exige que los $n$ elementos ocupen estrictamente el **prefijo contiguo** $a[0..n-1]$ sin discontinuidades físicas intermedias.
- Al descender eligiendo siempre el menor hijo, la trayectoria puede dejar un hueco en una hoja interior izquierda o central, mientras que las posiciones físicas $a[n-1]$ continúan ocupadas. Esto introduce un **agujero físico** ($\text{celda } null$) en medio del arreglo activo.

#### 2. Corrupción de las fórmulas de indexación $2i+1$ y $2i+2$
Las fórmulas aritméticas:
$$left(i) = 2i + 1, \quad right(i) = 2i + 2$$
se derivan de una biyección estricta entre la posición secuencial por niveles de un árbol completo y los índices de memoria contigua.
- Si existe una celda vacía en el índice $j < n$, dicha celda vacía cuenta aritméticamente en el cálculo de los hijos de nodos posteriores.
- Para cualquier nodo ubicado en un índice $k > j/2$, su hijo izquierdo $2k + 1$ se verá desplazado una posición hacia la derecha respecto a su ubicación topológica real.
- El árbol deja de corresponder a un árbol binario casi completo y las relaciones padre-hijo colapsan: operaciones futuras de inserción insertarían sobre elementos activos o perderían subárboles completos al buscar hijos en índices desfasados.

---

## Problema 5. Cuando un flujo ordenado de Heap degrada un BST y por qué AVL lo salva --- 4,0 puntos

### Inciso a) Degradación topológica en BST (1,25 puntos)

#### 1. Orden numérico de salida desde el Min-Heap
Por el invariante fundamental de un Min-Heap, la operación `remove()` extrae en cada invocación el elemento con la clave mínima absoluta de los elementos restantes en la estructura. Al ejecutar $n$ llamadas sucesivas a `remove()`, la secuencia de elementos emerge en **orden estrictamente no decreciente (orden ascendente ordenado)**:
$$x_1 \le x_2 \le x_3 \le \dots \le x_n$$

#### 2. Topología del BST resultante
Al insertar cada clave $x_k$ mediante `add(x)` en un BST inicialmente vacío:
- La primera clave $x_1$ se convierte en la raíz.
- Como la segunda clave $x_2 > x_1$, se inserta como hijo derecho de $x_1$.
- Como $x_3 > x_2 > x_1$, `findLast(x_3)` desciende por $x_1 \to x_2$ e inserta $x_3$ como hijo derecho de $x_2$.
- Por inducción matemática, para todo $k > 1$, el nodo $x_k$ se enlaza como $x_{k-1}.right$, dejando todos los punteros $x_{k-1}.left = null$.
- **Forma geométrica:** El árbol degenera en una **cadena lineal unilateral derecha (árbol degenerado o *skewed tree*)**, perdiendo toda cualidad de árbol bidimensional y transformándose topológicamente en una lista simplemente enlazada.

#### 3. Altura exacta $h(n)$
Midiendo la altura por cantidad de niveles (o profundidad de la hoja):
$$h(n) = n \text{ niveles} \quad (\text{o } h(n) = n - 1 \text{ aristas})$$

#### 4. Complejidad asintótica degenerada
- **`contains(x)`:** Para buscar una clave ausente mayor que todos los nodos o ubicar la clave máxima $x_n$, el algoritmo debe recorrer secuencialmente los $n$ nodos mediante un bucle `w = w.right`. Costo: $\mathbf{\Theta(n)}$.
- **`remove(x)`:** La localización toma $\Theta(n)$. La modificación física con `splice` toma $O(1)$. Costo total: $\mathbf{\Theta(n)}$.
- **Por qué es el peor caso absoluto:** Porque anula la ganancia de la división y conquista: no descarta la mitad del espacio de búsqueda en cada paso. La tasa de reducción por paso es de solo 1 elemento en lugar de dividirse entre 2.

---

### Inciso b) Inmunidad y rebalanceo sistemático en AVL (1,0 punto)

#### 1. Tipo de desbalance detectado
Dado que cada nueva clave insertada es estrictamente mayor que todas las anteriores, siempre se inserta como la hoja más profunda del extremo derecho del árbol.
- En cada ciclo en que la altura del subárbol derecho supere en 2 a la del subárbol izquierdo, el nodo ancestro detectará:
  $$FE(u) = height(u.left) - height(u.right) = h - (h + 2) = -2$$
- Dado que la clave fue añadida en el extremo exterior derecho, el hijo derecho $u.right$ siempre satisface $FE(u.right) = -1$ (o 0).
- **Diagnóstico sistemático:** Se detectará repetitivamente el **Caso RR (Right-Right)**.

#### 2. Rotación elemental correctora
El caso $RR$ se neutraliza mediante una única **rotación simple a la izquierda**:
$$\text{rotateLeft}(u)$$
Esta rotación eleva al hijo derecho $y = u.right$ a nueva raíz local, desciende a $u$ como hijo izquierdo de $y$, y actualiza las alturas en $O(1)$.

#### 3. Cota superior de altura y neutralización
Por el teorema de balanceo AVL, la altura máxima posible de un árbol AVL con $n$ claves está acotada estrictamente por:
$$h < 1.4405 \log_2(n + 2) - 0.328 = O(\log n)$$
- **Mecanismo de neutralización:** Cada vez que el flujo creciente intenta elongar el camino derecho a longitud 3, la rotación `rotateLeft` dobla la cadena hacia la izquierda, transfiriendo nodos hacia subárboles izquierdos vacíos y manteniendo el árbol perfectamente ramificado y balanceado. La búsqueda conserva su garantía logarítmica $\mathbf{O(\log n)}$ en el peor caso absoluto.

---

### Inciso c) La falacia de la búsqueda en Heap vs AVL (0,75 puntos)

#### 1. Refutación formal de la afirmación
La afirmación es **completamente falsa**. Que dos estructuras compartan una altura topológica de $O(\log n)$ no implica que ambas soporten búsqueda de clave arbitraria en $O(\log n)$. El costo de la búsqueda depende de la naturaleza del **orden de invariante**, no de la altura.

#### 2. Orden Parcial Vertical vs Orden Total Horizontal

| Característica | BinaryHeap (Min-Heap) | Árbol AVL (BST balanceado) |
| :--- | :--- | :--- |
| **Tipo de Orden** | **Orden Parcial Vertical:** La restricción solo existe a lo largo de caminos raíz-hoja ($parent(i) \le i$). | **Orden Total Horizontal:** La restricción separa rígidamente todo el plano ($left < u.x < right$). |
| **Relación entre hermanos** | **Ninguna:** El hermano izquierdo puede ser mayor, menor o igual que el hermano derecho. | **Total:** Todo elemento en el subárbol izquierdo es estrictamente menor que cualquier elemento en el subárbol derecho. |
| **Poder de poda en búsqueda** | **Nulo:** Si la clave buscada $x > root$, la clave podría residir indistintamente en el hijo izquierdo, en el derecho o en ninguno. No es posible descartar ningún subárbol. | **Total:** Si $x < u.x$, se descarta con certeza el 100% del subárbol derecho; si $x > u.x$, se descarta el 100% del subárbol izquierdo. |

#### 3. Costo real de `contains(x)` en un BinaryHeap
Dado que no se puede podar ningún subárbol basándose en una comparación $x > a[parent]$, en el peor caso (por ejemplo, buscar una clave que no existe y que es mayor que la raíz) el algoritmo no puede tomar decisiones de desvío y se ve obligado a explorar exhaustivamente todos y cada uno de los $n$ nodos del arreglo:
$$\text{Costo}(\text{contains en BinaryHeap}) = \mathbf{\Theta(n)}$$

---

### Inciso d) Matriz de decisión de ingeniería y escenarios (1,0 punto)

#### 1. Tabla de Complejidades en el Peor Caso

| Operación de ingeniería | BST sin balanceo | Árbol AVL | BinaryHeap (Min-Heap) |
| :--- | :---: | :---: | :---: |
| **Construcción a partir de $n$ elementos desordenados** | $O(n^2)$ | $O(n \log n)$ | $\mathbf{O(n)}$ (Floyd `heapify`) |
| **Extracción del elemento mínimo (`removeMin`)** | $O(n)$ | $O(\log n)$ | $\mathbf{O(\log n)}$ (o $O(1)$ consulta `peek`) |
| **Búsqueda de una clave arbitraria (`contains(x)`)** | $O(n)$ | $\mathbf{O(\log n)}$ | $O(n)$ (búsqueda lineal completa) |
| **Emisión de todas las claves ordenadas (menor a mayor)** | $O(n^2)$ peor / $O(n)$ si equilibrado | $\mathbf{O(n)}$ (inorder estándar) | $O(n \log n)$ ($n$ extracciones `remove`) |

#### 2. Fundamentación de Elección de Estructura por Escenario

##### Escenario Alpha (95% inserciones y extracciones de mínimo, 5% reportes batch):
- **Estructura elegida:** **`BinaryHeap (Min-Heap)`**.
- **Fundamentación técnica:**
  1. El perfil de carga está dominado casi exclusivamente ($95\%$) por la semántica natural de una cola de prioridad (`add` y `removeMin`).
  2. El BinaryHeap presenta factores constantes drásticamente menores que el AVL: opera sobre un arreglo contiguo contiguo en memoria cache ($O(1)$ localidad espacial de referencia), sin la penalización de memoria de punteros (`left`, `right`) ni la sobrecarga de actualizar alturas y ejecutar rotaciones en cada operación.
  3. Para el $5\%$ de reportes batch ordenados, un vaciado de montículo en $O(n \log n)$ (HeapSort) es perfectamente admisible frente a la ganancia masiva de latencia y throughput en el $95\%$ de las operaciones críticas.

##### Escenario Beta (70% búsquedas puntuales `contains` y 30% consultas de rango `countInRange`):
- **Estructura elegida:** **`Árbol AVL`**.
- **Fundamentación técnica:**
  1. En este escenario, el BinaryHeap queda completamente descalificado: su operación `contains` cuesta $O(n)$ y no posee orden horizontal para resolver consultas de rango eficientemente. Con $n = 10^6$, un heap requeriría $10^6$ operaciones por consulta frente a $\approx 20$ comparaciones del AVL.
  2. El BST sin balanceo tampoco es admisible debido al riesgo de degeneración a $O(n)$ ante patrones de inserción sesgados.
  3. El árbol AVL garantiza rigurosamente $O(\log n)$ en el peor caso para las búsquedas puntuales ($70\%$) y permite que las consultas de rango ($30\%$) ejecuten poda logarítmica mediante `countInRange`, reduciendo el tiempo de respuesta a $O(\log n + k)$, donde $k$ es el número de elementos reportados.

---

## Rúbrica y Criterios Oficiales de Calificación (20,0 puntos)

```text
========================================================================================
PROBLEMA 1 (4,0 pts) - BST, SPLICE Y SUCESOR
- Inciso a (1,50 pts): 0,35 pt por traza correcta de remove(10), remove(65), remove(25)
                       y 0,45 pt por remove(50) con dibujos limpios de estados.
- Inciso b (0,75 pts): 0,35 pt por secuencia de punteros; 0,40 pt por demostración de w.left=null.
- Inciso c (0,75 pts): 0,25 pt por análisis de inorder; 0,50 pt por identificar 70.parent
                       y describir la falla catastrófica de remove(70).
- Inciso d (1,00 pt):  0,50 pt por separar findLast de splice; 0,50 pt por refutación y árbol degenerado.
----------------------------------------------------------------------------------------
PROBLEMA 2 (4,0 pts) - AVL, ROTACIONES Y ALTURAS
- Inciso a (1,50 pts): 0,25 pt por cada inserción con desbalance correctamente clasificada (LL, RR)
                       y 0,50 pt por el árbol final completo de 8 claves.
- Inciso b (0,75 pts): 0,40 pt por principio de causalidad; 0,35 pt por trazado de valor erróneo.
- Inciso c (0,75 pts): 0,35 pt por contraejemplo con clave 25; 0,40 pt por cálculo de FE=-2.
- Inciso d (0,50 pts): Tabla completa con x, left, right, height y balanceFactor sin errores.
- Inciso e (0,50 pts): 0,25 pt por valores N(h); 0,25 pt por deducción h_max=4 vs h_max=8.
----------------------------------------------------------------------------------------
PROBLEMA 3 (4,0 pts) - AVL ELIMINACIÓN Y PSEUDOCÓDIGO
- Inciso a (1,25 pts): 0,50 pt por traza de remove(10); 0,75 pt por justificación de O(log n) vs O(1).
- Inciso b (1,25 pts): 0,50 pt por explicación previa y contratos; 0,75 pt por pseudocódigo riguroso.
- Inciso c (0,75 pts): 0,40 pt por demostración O(log n); 0,35 pt por justificación de Call Stack.
- Inciso d (0,75 pts): 0,35 pt por pseudocódigo de poda; 0,40 pt por análisis de mejor caso O(log n).
----------------------------------------------------------------------------------------
PROBLEMA 4 (4,0 pts) - BINARY HEAP Y HEAPIFY
- Inciso a (1,50 pts): 0,40 pt por inicio en n/2-1; 0,70 pt por traza de pasos i=4..0; 0,40 pt por árbol y swaps.
- Inciso b (1,00 pt):  0,35 pt por primer remove; 0,35 pt por segundo remove; 0,30 pt por add(4).
- Inciso c (0,75 pts): 0,40 pt por sumatoria por niveles; 0,35 pt por serie geométrica y comparación.
- Inciso d (0,75 pts): 0,40 pt por invariante de forma; 0,35 pt por demostración de fórmulas 2i+1/2i+2.
----------------------------------------------------------------------------------------
PROBLEMA 5 (4,0 pts) - INTEGRADOR Y MATRIZ DE DECISIÓN
- Inciso a (1,25 pts): 0,25 pt orden; 0,35 pt topología skew; 0,25 pt altura; 0,40 pt Theta(n).
- Inciso b (1,00 pt):  0,35 pt caso RR; 0,30 pt rotateLeft; 0,35 pt cota superior estricta.
- Inciso c (0,75 pts): 0,40 pt orden vertical vs horizontal; 0,35 pt demostración Theta(n).
- Inciso d (1,00 pt):  0,50 pt por matriz de decisión completa; 0,50 pt por escenarios Alpha y Beta.
========================================================================================
TOTAL: 20,0 PUNTOS
```
