/*
 * CC-232 - Semana 4, lunes: búsqueda e inserción en un BST.
 * Adaptación didáctica de BinarySearchTree de Pat Morin.
 */
public class Semana04_BST0 {
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

        int size() {
            return n;
        }

        // Función privada que sigue el camino de búsqueda y retorna el último nodo
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

        boolean contains(int x) {
            Node p = findLast(x);
            return p != null && p.x == x;
        }

        // Función publica que permite agregar un nuevo nodo al árbol.
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

        void inorder() {
            inorder(root);
            System.out.println();
        }

        private void inorder(Node u) {
            if (u == null) {
                return;
            }

            inorder(u.left);
            System.out.print(u.x + " ");
            inorder(u.right);
        }
    }

    public static void main(String[] args) {
        BinarySearchTree tree = new BinarySearchTree();
        try {
            for (int x : new int[] { 40, 20, 60, 10, 30, 50, 70 })
                tree.add(x);
            tree.inorder();
            System.out.println("Esperado: 10 20 30 40 50 60 70");
            System.out.println("Resultado de contains(30): " + tree.contains(30) + " (esperado: true)");
            System.out.println("Tamaño: " + tree.size() + " (esperado: 7)");
        } catch (UnsupportedOperationException e) {
            System.out.println(e.getMessage());
        }
    }
}
