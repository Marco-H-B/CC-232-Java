
/**
 * CC232 - Algoritmos y Estructuras de Datos (UNI)
 * Examen Parcial - Problema 2: Despacho de tareas con FIFO, rotación y registro LIFO
 *
 * Incisos del examen:
 * a) Traza de los 7 eventos con j, n, estados antes/después de resize y tras T10.
 * b) Live coding de void resize()
 * c) Análisis del error b[k] = a[k] con j = 0
 * d) Live coding de void push(String x) para LinkedStack y análisis amortizado
 */
import java.util.NoSuchElementException;

public class Problema2_Despacho {

    public static class LinkedStack {
        public static class Node {
            public String x;
            public Node next;

            public Node(String x, Node next) {
                this.x = x;
                this.next = next;
            }
        }

        public Node head;
        public int n;

        public LinkedStack() {
            head = null;
            n = 0;
        }

        // =========
        // Inciso d
        // =========
        public void push(String x) {
            // TODO: [Paso 1] Crear nuevo nodo que apunte al head actual como su next y
            // actualizar head al nuevo nodo
            head = new Node(x, head);

            // TODO: [Paso 2] Incrementar contador n++
            n++;
        }

        public String pop() {
            if (n == 0)
                throw new NoSuchElementException("pila vacía");
            String x = head.x;
            head = head.next;
            n--;
            return x;
        }

        public void printStack(String label) {
            System.out.print(label + ": [");
            Node curr = head;
            while (curr != null) {
                System.out.print(curr.x);
                if (curr.next != null)
                    System.out.print(", ");
                curr = curr.next;
            }
            System.out.println("] (n=" + n + ")");
        }
    }

    public static class ArrayQueue {
        public String[] a;
        public int j; // índice del frente
        public int n; // cantidad de elementos válidos

        public ArrayQueue(int capacity) {
            a = new String[capacity];
            j = 0;
            n = 0;
        }

        public void resize() {
            // TODO: [Paso 1] Nuevo arreglo b con capacidad Math.max(1, 2 * n)}
            String[] b = new String[Math.max(1, 2 * n)];

            // TODO: [Paso 2] Copiar los n elementos desde el frente j:
            // a[(j+k) % a.length]
            for (int k = 0; k < n; k++) {
                b[k] = a[(j + k) % a.length];
            }

            // TODO: [Paso 3] Reemplazar referencia y reiniciar j
            a = b;
            j = 0;
        }

        public void add(String x) {
            if (n + 1 > a.length)
                resize();

            a[(j + n) % a.length] = x;
            n++;
        }

        public String remove() {
            if (n == 0)
                throw new NoSuchElementException("array vacía");

            String x = a[j];
            a[j] = null;

            j = (j + 1) % a.length;
            n--;

            return x;
        }

        public void printState(String label) {
            System.out.print(label + ": FIFO [");
            for (int i = 0; i < n; i++) {
                System.out.print(a[(j + i) % a.length]);
                if (i < n - 1)
                    System.out.print(", ");
            }
            System.out.println("] (cap=" + a.length + ", j=" + j + ", n=" + n + ")");
        }
    }

    public static void main(String[] args) {
        ArrayQueue ready = new ArrayQueue(8);
        LinkedStack finished = new LinkedStack();

        // Estado inicial exacto del examen: a.length=8, j=5, n=5
        ready.j = 5;
        ready.n = 5;
        ready.a[5] = "T1";
        ready.a[6] = "T2";
        ready.a[7] = "T3";
        ready.a[0] = "T4";
        ready.a[1] = "T5";
        ready.printState("Estado inicial ");

        // Eventos 1 al 5
        finished.push(ready.remove()); // T1 sale a finished
        ready.add(ready.remove()); // T2 rota al final
        ready.add("T6");
        ready.add("T7");
        ready.add("T8");

        // Evento 6: llega T9 (antes de resize)
        ready.add("T9");
        ready.printState("Antes de resize");

        // Evento 7: llega T10 (desencadena resize)
        ready.add("T10");
        ready.printState("Tras resize    ");

        finished.printStack("Pila finished  ");
    }
}
