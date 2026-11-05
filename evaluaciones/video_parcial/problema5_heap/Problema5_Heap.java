
/**
 * CC232 - Algoritmos y Estructuras de Datos (UNI)
 * Examen Parcial - Problema 5: Centro de incidentes (BinaryHeap min-heap)
 *
 * Min-Heap basado en arreglo contiguo con trickleDown iterativo.
 * Método a implementar en vivo: void trickleDown(int i)
 */
import java.util.Arrays;
import java.util.NoSuchElementException;

public class Problema5_Heap {

    public static class BinaryHeap {
        public int[] a;
        public int n;

        public BinaryHeap(int[] values) {
            this.n = values.length;
            this.a = Arrays.copyOf(values, n);
            heapify();
        }

        public int left(int i) {
            return 2 * i + 1;
        }

        public int right(int i) {
            return 2 * i + 2;
        }

        public int parent(int i) {
            return (i - 1) / 2;
        }

        public void swap(int i, int j) {
            int temp = a[i];
            a[i] = a[j];
            a[j] = temp;
        }

        public void heapify() {
            // Construcción bottom-up O(n): desde el último padre n/2 - 1 hasta 0
            for (int i = n / 2 - 1; i >= 0; i--) {
                trickleDown(i);
            }
        }

        public void resize() {
            a = Arrays.copyOf(a, Math.max(1, 2 * n));
        }

        public void add(int x) {
            if (n + 1 > a.length)
                resize();
            a[n] = x;
            n++;
            bubbleUp(n - 1);
        }

        public void bubbleUp(int i) {
            while (i > 0 && a[i] < a[parent(i)]) {
                int p = parent(i);
                swap(i, p);
                i = p;
            }
        }

        public int remove() {
            if (n == 0)
                throw new NoSuchElementException("el heap está vacío");
            int minVal = a[0];
            a[0] = a[--n]; // Coloca el último en la raíz
            if (n > 0)
                trickleDown(0);
            return minVal;
        }

        // ===========
        // Inciso c
        // ===========
        public void trickleDown(int i) {
            while (true) {
                int l = left(i);
                int r = right(i);
                int smallest = i;

                if (l < n && a[l] < a[smallest]) {
                    smallest = l;
                }

                if (r < n && a[r] < a[smallest]) {
                    smallest = r;
                }

                if (smallest == i)
                    break;

                swap(i, smallest);
                i = smallest;
            }
        }

        public void printHeap(String label) {
            System.out.print(label + ": [");
            for (int i = 0; i < n; i++) {
                System.out.print(a[i]);
                if (i < n - 1)
                    System.out.print(", ");
            }
            System.out.println("] (n=" + n + ", cap=" + a.length + ")");
        }
    }

    public static void main(String[] args) {
        int[] values = { 17, 9, 24, 5, 12, 20, 28, 3, 7, 15 };
        BinaryHeap heap = new BinaryHeap(values);
        heap.printHeap("Tras heapify()");

        heap.add(4);
        heap.printHeap("Tras add(4)   ");

        int r1 = heap.remove();
        heap.printHeap("remove() -> " + r1 + " ");

        int r2 = heap.remove();
        heap.printHeap("remove() -> " + r2 + " ");
    }
}
