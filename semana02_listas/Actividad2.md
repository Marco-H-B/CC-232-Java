# CC232 - Actividad 2 - Semana 2

> **Curso:** CC232 - Algoritmos y Estructuras de Datos  
> **Actividad:** Listas enlazadas  
> **Duración:** 50 minutos | **Modalidad:** Individual | **Carácter:** Formativa, sin calificación  
> **Objetivo:** Razonar sobre representación, invariantes, localización y costo antes de implementar  
> **Entrega:** Hojas de la actividad  

---

**Nombre:** **Marco Huamani** | **Código:** **20232741D**

**Indicaciones:** Responde primero en papel. Razona a partir de los estados y diagramas dados y justifica brevemente cuando se solicite. No uses Internet ni asistentes de inteligencia artificial. Si la sesión es virtual, usa la computadora únicamente para conectarte a la clase y visualizar las indicaciones del docente.

---

## 1. De un arreglo a una representación enlazada (6 min)

En la Semana 1 se representó una secuencia mediante un arreglo. Considera:
```text
a = [10, 20, 30, 40, _, _, _, _]
n = 4
```

**a. Si se inserta 5 al inicio, ¿qué valores deben cambiar de posición? Dibuja el estado final.**
- Deben desplazarse hacia la derecha todos los elementos existentes: `40`, `30`, `20` y `10` (en ese orden de derecha a izquierda para no sobrescribir datos).
- **Estado final:**
  ```text
  a = [5, 10, 20, 30, 40, _, _, _]
  n = 5
  ```

**b. Supón ahora que cada valor puede almacenarse en un objeto separado. Dibuja una posible forma de conectar 10, 20, 30 y 40 sin exigir posiciones contiguas.**
- Cada nodo almacena su dato y una referencia al siguiente:
  ```text
  [10 | *] ---> [20 | *] ---> [30 | *] ---> [40 | null]
  ```

**c. ¿Qué posible ventaja tendría esa representación para insertar un elemento al inicio?**
- No requiere desplazar ningún elemento existente. Solo se crea el nuevo nodo y se redirigen punteros/referencias en tiempo constante $O(1)$.

---

## 2. Nodo, head, tail y tamaño (6 min)

Considera la siguiente lista simplemente enlazada:
```text
head                tail
 |                   |
 v                   v
[10 | *] -> [20 | *] -> [30 | null]
n = 3
```

**a. ¿Qué información almacena cada nodo además del valor? ¿Qué representan head y tail?**
- Cada nodo almacena una referencia (`next`) al siguiente nodo en la secuencia (o `null` si es el último).
- `head`: Referencia al primer nodo lógico de la lista.
- `tail`: Referencia al último nodo lógico de la lista.

**b. Completa las propiedades que deberían cumplirse:**
- Si `n == 0`: `head = null`, `tail = null`
- Si `n > 0`: `head != null`, `tail.next = null`

**c. Si `n == 1`, ¿qué relación debe existir entre head y tail?**
- `head == tail` (ambas variables son alias que referencian al mismo y único nodo existente).

---

## 3. Insertar y retirar desde la cabecera (6 min)

Se tiene:
```text
head
 |
 v
[20 | *] -> [30 | null]
             ^
             |
            tail
```

**a. Se desea ejecutar `push(10)`. Dibuja el nuevo nodo y señala únicamente qué referencias deben cambiar.**
- Se crea el nodo `u = [10 | *]`.
- Cambios de referencias:
  1. `u.next = head` (enlaza al nodo 20).
  2. `head = u` (la cabecera ahora apunta a 10).
- Diagrama resultante:
  ```text
  head                               tail
   |                                  |
   v                                  v
  [10 | *] ---> [20 | *] ---> [30 | null]
  ```

**b. Después se ejecuta `pop()`. ¿Qué valor debería retornar y cuál debería ser el nuevo head?**
- Retorna el valor `10`.
- El nuevo `head` debe ser el nodo con valor `20` (`head = head.next`).

**c. ¿Por qué tail requiere atención especial cuando se elimina el único nodo de la lista?**
- Porque al hacer `head = head.next` cuando $n = 1$, `head` pasa a ser `null`, pero `tail` seguiría apuntando al nodo eliminado si no se actualiza explícitamente (`if (n == 0) tail = null`). Esto dejaría un estado inconsistente que violaría el invariante de lista vacía.

---

## 4. Una limitación de la lista simplemente enlazada (4 min)

```text
head
 |
 v
[10 | *] -> [20 | *] -> [30 | *] -> [40 | null]
                                     ^
                                     |
                                    tail
```

**Se desea eliminar 40 y hacer que tail apunte a 30. ¿Puede obtenerse directamente el nodo anterior a tail? Explica qué tendría que hacerse y qué información adicional podría evitar ese recorrido.**
- **No se puede obtener directamente:** La lista simplemente enlazada es unidireccional y `tail` no tiene referencia hacia atrás.
- **Qué tendría que hacerse:** Recorrer desde `head` paso a paso siguiendo `.next` hasta encontrar el nodo cuyo `.next == tail` (costo $O(n)$).
- **Información adicional que lo evitaría:** Una referencia al predecesor (`prev`) en cada nodo, es decir, convertir la estructura en una lista doblemente enlazada (DLList).

---

## 5. Lista doblemente enlazada y nodo centinela (6 min)

Ahora cada nodo conoce a su sucesor y a su predecesor. Se introduce además un nodo `dummy` que no contiene un elemento lógico:
```text
dummy <-> [10] <-> [20] <-> [30] <-> dummy
          primer nodo       último nodo
```

**a. ¿Qué ventaja aporta una referencia `prev` además de `next`?**
- Permite retroceder en la secuencia y eliminar/insertar antes de un nodo conocido en tiempo constante $O(1)$ sin necesidad de recorrer desde el inicio para hallar al predecesor.

**b. En una lista vacía completa:**
- `dummy.next = dummy`
- `dummy.prev = dummy`

**c. Completa dos invariantes de frontera:**
- `dummy.next.prev == dummy`
- `dummy.prev.next == dummy`

**d. Explica por qué un nodo centinela puede reducir casos especiales al insertar o eliminar en los extremos.**
- Porque elimina la necesidad de comprobar condicionales de casos borde (`head == null`, lista vacía, insertar al inicio o al final). Con el centinela, todo nodo real **siempre** tiene un predecesor y un sucesor estructurales válidos (que en los extremos es `dummy`), unificando la lógica de punteros.

---

## 6. Localizar un nodo por índice (7 min)

En una lista doblemente enlazada de tamaño $n = 8$, se puede comenzar desde el primer nodo o desde el último. Completa:

| Índice $i$ | Extremo conveniente | Enlaces recorridos |
|:---:|:---:|:---:|
| 0 | Inicio (`dummy.next`) | 0 |
| 1 | Inicio (`dummy.next`) | 1 |
| 3 | Inicio (`dummy.next`) | 3 |
| 4 | Final (`dummy.prev`) | 3 |
| 6 | Final (`dummy.prev`) | 1 |
| 7 | Final (`dummy.prev`) | 0 |

**a. Explica por qué `get(i)` ya no puede ser acceso directo $O(1)$ como en un arreglo.**
- Porque los nodos están dispersos en memoria y no en posiciones contiguas indexables por aritmética de punteros. Para llegar al $i$-ésimo nodo es obligatorio atravesar la cadena de referencias enlace por enlace.

**b. ¿Qué idea expresa la complejidad $O(1 + \min(i, n - i))$?**
- Expresa que la búsqueda se optimiza partiendo desde el extremo más cercano al índice solicitado: si $i < n/2$ se avanza desde el inicio ($i$ pasos), y si $i \ge n/2$ se retrocede desde el final ($n - 1 - i$ pasos). En el peor caso (centro) recorre a lo más $n/2$ pasos, pero sigue siendo proporcional a la distancia al borde más próximo.

---

## 7. Modificación local y comparación de costos (7 min)

Se conoce una referencia al nodo $w$ y se quiere insertar un nuevo nodo $u$ inmediatamente antes:
```text
A <-> W   ====>   A <-> U <-> W
```

**a. Completa las cuatro referencias necesarias:**
```java
u.prev = w.prev;
u.next = w;
u.next.prev = u; // equivalente a: w.prev = u;
u.prev.next = u;
```

**b. Ahora se tiene `A <-> W <-> B`. Escribe las dos asignaciones que permiten que A y B se enlacen directamente y W quede desconectado.**
```java
w.prev.next = w.next;
w.next.prev = w.prev;
```

**c. Si el nodo ya está localizado, ¿insertar o eliminar alrededor de él requiere recorrer toda la lista? Justifica.**
- No. Requiere únicamente reasignar un número finito y constante de punteros locales (4 para insertar, 2 para desconectar). Por tanto, la mutación en sí cuesta $O(1)$.

---

## 8. Síntesis: cambiar la representación cambia el costo (8 min)

Completa la tabla usando $O(1)$, $O(n)$ u $O(1 + \min(i, n - i))$, según corresponda. Para la última fila, supón que se conoce la referencia al nodo $w$; en SLList no se conoce su predecesor.

| Operación | Arreglo dinámico | SLList | DLList |
|---|:---:|:---:|:---:|
| **Acceder por índice** | $O(1)$ | $O(n)$ | $O(1 + \min(i, n - i))$ |
| **Insertar al inicio** | $O(n)$ | $O(1)$ | $O(1)$ |
| **Insertar al final** | $O(1)$ amortizado | $O(1)$ | $O(1)$ |
| **Eliminar al inicio** | $O(n)$ | $O(1)$ | $O(1)$ |
| **Eliminar nodo $w$ ya localizado** | $O(n)$ | $O(n)$ | $O(1)$ |

**Explica en una o dos frases por qué ninguna de las tres representaciones es mejor para todas las operaciones:**
- Ninguna estructura domina absolutamente porque existe una compensación directa: los arreglos favorecen el acceso aleatorio directo por índice a costa de desplazamientos en inserción/borrado interno, mientras que las listas favorecen la modificación local inmediata a costa de perder el acceso directo y requerir recorridos para localizar.
