/**
 * CC232 - Algoritmos y Estructuras de Datos (UNI)
 * Examen Parcial - Problema 3: Índice de activos: eliminación segura en un BST
 *
 * Incisos del examen:
 * a) BST inicial con claves 64, 32, 88, 20, 49, 76, 96, 40, 57, 70, 82, 73.
 * Relaciones parent de 70, 73, 76, inorder y n inicial.
 * b) Ejecución de remove(64) con sucesor inorder (nodo 70) y reconexión con
 * splice.
 * c) remove(20) y remove(21) distinguiendo boolean y tamaños.
 * d) Live coding de void splice(Node u) sin n--.
 * e) Costos de findLast, contains, add y remove en términos de h.
 */
public class Problema3_BST {

    public static class BinarySearchTree {
        public static class Node {
            public int x;
            public Node left;
            public Node right;
            public Node parent;

            public Node(int val) {
                this.x = val;
            }
        }

        public Node root;
        public int n;

        public BinarySearchTree() {
            root = null;
            n = 0;
        }

        public Node findLast(int x) {
            Node w = root, prev = null;

            while (w != null) {
                prev = w;
                if (x < w.x)
                    w = w.left;
                else if (x > w.x)
                    w = w.right;
                else
                    return w;
            }

            return prev;
        }

        public boolean add(int x) {
            Node p = findLast(x);
            Node u = new Node(x);
            if (p == null) {
                root = u;
            } else {
                if (x < p.x)
                    p.left = u;
                else if (x > p.x)
                    p.right = u;
                else
                    return false; // duplicado no permitido
                u.parent = p;
            }
            n++;
            return true;
        }

        public Node find(int x) {
            Node w = root;
            while (w != null) {
                if (x < w.x)
                    w = w.left;
                else if (x > w.x)
                    w = w.right;
                else
                    return w;
            }
            return null;
        }

        // =========================================================================
        // Inciso d: Live coding de splice(Node u)
        // Desconecta un nodo 'u' que tiene a lo más un hijo (left o right).
        // Presupone que u tiene como máximo un hijo y NO decrementa n.
        // =========================================================================
        public void splice(Node u) {
            Node s = (u.left != null) ? u.left : u.right;
            Node p = u.parent;

            if (s != null) {
                s.parent = p;
            }

            if (p == null) {
                root = s;
            } else if (p.left == u) {
                p.left = s;
            } else {
                p.right = s;
            }

            u.left = null;
            u.right = null;
            u.parent = null;
        }

        public boolean remove(int x) {
            Node u = find(x);
            if (u == null)
                return false;

            if (u.left == null || u.right == null) {
                splice(u);
            } else {
                // Nodo con dos hijos: buscar sucesor in-order (mínimo del subárbol derecho)
                Node w = u.right;
                while (w.left != null)
                    w = w.left;
                u.x = w.x; // copia la clave del sucesor
                splice(w); // desconecta físicamente el nodo sucesor
            }
            n--;
            return true;
        }

        public void printInOrder(String label) {
            System.out.print(label + ": [");
            boolean[] first = { true };
            inorderTraverse(root, first);
            System.out.println("] (n=" + n + ", raiz=" + (root != null ? root.x : "null") + ")");
        }

        private void inorderTraverse(Node u, boolean[] first) {
            if (u == null)
                return;
            inorderTraverse(u.left, first);
            if (!first[0])
                System.out.print(", ");
            System.out.print(u.x);
            first[0] = false;
            inorderTraverse(u.right, first);
        }
    }

    public static void main(String[] args) {
        BinarySearchTree bst = new BinarySearchTree();

        // Claves exactas del examen: 64, 32, 88, 20, 49, 76, 96, 40, 57, 70, 82, 73
        int[] keys = { 64, 32, 88, 20, 49, 76, 96, 40, 57, 70, 82, 73 };
        for (int k : keys)
            bst.add(k);
        bst.printInOrder("Inorder inicial");

        boolean r64 = bst.remove(64);
        System.out.println("remove(64) -> " + r64 + " | Nueva raiz = " + bst.root.x + " (n=" + bst.n + ")");

        boolean r20 = bst.remove(20);
        boolean r21 = bst.remove(21);
        System.out.println("remove(20) -> " + r20 + " | remove(21) -> " + r21 + " (clave ausente)");

        bst.printInOrder("Inorder final  ");
    }
}
