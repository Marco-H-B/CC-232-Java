/**
 * CC232 - Algoritmos y Estructuras de Datos (UNI)
 * Examen Parcial - Problema 1: Edición transaccional de una bitácora en memoria
 *
 * Implementación de lista doblemente enlazada circular con centinela dummy.
 * Método a implementar en vivo: void removeCurrent()
 */
public class Problema1_Bitacora {

    public static class DLList {
        public static class Node {
            public int x;
            public Node prev;
            public Node next;

            public Node(int x) {
                this.x = x;
            }
        }

        public Node dummy;
        public Node current;
        public int n;

        public DLList() {
            dummy = new Node(-1);
            dummy.next = dummy;
            dummy.prev = dummy;
            current = dummy;
            n = 0;
        }

        // Inserta después de current preservando current en el mismo nodo
        public void insertAfterCurrent(int x) {
            Node u = new Node(x);
            u.prev = current;
            u.next = current.next;
            u.prev.next = u;
            u.next.prev = u;
            n++;
        }

        public void advanceCurrent(int steps) {
            for (int i = 0; i < steps; i++) {
                if (current.next != dummy) {
                    current = current.next;
                }
            }
        }

        public int get(int i) {
            if (i < 0 || i >= n)
                throw new IndexOutOfBoundsException();
            Node p;
            if (i < n / 2) {
                p = dummy.next;
                for (int j = 0; j < i; j++)
                    p = p.next;
            } else {
                p = dummy.prev;
                for (int j = n - 1; j > i; j--)
                    p = p.prev;
            }
            return p.x;
        }

        // =========================================================================
        // Inciso c: removeCurrent()
        // =========================================================================
        public void removeCurrent() {
            if (n <= 0 || current == dummy) {
                return;
            }

            Node removed = current;

            // TODO: Actualizar posición de current
            if (n == 1) {
                current = dummy;
            } else if (removed.next != dummy) {
                current = removed.next;
            } else {
                current = removed.prev;
            }

            // TODO: Desconectar nodo actual y decrementar n}
            removed.prev.next = removed.next;
            removed.next.prev = removed.prev;

            n--;
        }

        public void printState(String label) {
            System.out.print(label + ": [");
            Node p = dummy.next;
            while (p != dummy) {
                if (p == current)
                    System.out.print(p.x + "*");
                else
                    System.out.print(p.x);
                if (p.next != dummy)
                    System.out.print(", ");
                p = p.next;
            }
            System.out.println("] (n=" + n + ", current=" + (current != dummy ? current.x : "dummy") + ")");
        }
    }

    public static void main(String[] args) {
        DLList list = new DLList();

        // Cargar estado inicial: [12, 24, 36, 48, 60, 72, 84], current en 36
        int[] inicial = { 12, 24, 36, 48, 60, 72, 84 };
        for (int v : inicial) {
            list.insertAfterCurrent(v);
            list.current = list.current.next;
        }
        list.current = list.dummy.next.next.next; // current en 36
        list.printState("Estado inicial");

        // Secuencia de operaciones
        list.insertAfterCurrent(42);
        list.advanceCurrent(2);
        list.removeCurrent();
        list.insertAfterCurrent(66);

        list.printState("Estado final  ");
        System.out.println("get(5) = " + list.get(5));
        boolean inv = (list.dummy.next.prev == list.dummy && list.dummy.prev.next == list.dummy);
        System.out.println("Invariante circular: " + (inv ? "OK" : "ERROR"));
    }
}
