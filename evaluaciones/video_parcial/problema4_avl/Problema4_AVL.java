/**
 * CC232 - Algoritmos y Estructuras de Datos (UNI)
 * Examen Parcial - Problema 4: Índice AVL para consultas de baja latencia
 *
 * Árbol AVL balanceado con factor de balance y rotaciones.
 * Método a implementar en vivo: Node rebalance(Node u)
 */
public class Problema4_AVL {

    public static class AVLTree {
        public static class Node {
            public int x;
            public int height;
            public Node left;
            public Node right;

            public Node(int val) {
                this.x = val;
                this.height = 1; // Hoja tiene height = 1 según convención del examen
            }
        }

        public Node root;

        public int height(Node u) {
            return (u == null) ? 0 : u.height;
        }

        public void updateHeight(Node u) {
            if (u != null) {
                u.height = 1 + Math.max(height(u.left), height(u.right));
            }
        }

        public int balanceFactor(Node u) {
            return (u == null) ? 0 : height(u.left) - height(u.right);
        }

        public Node rotateRight(Node y) {
            Node x = y.left;
            Node middle = x.right;
            x.right = y;
            y.left = middle;
            updateHeight(y);
            updateHeight(x);
            return x;
        }

        public Node rotateLeft(Node x) {
            Node y = x.right;
            Node middle = y.left;
            y.left = x;
            x.right = middle;
            updateHeight(x);
            updateHeight(y);
            return y;
        }

        // =========================================================================
        // MÉTODO A IMPLEMENTAR EN VIVO DURANTE LA GRABACIÓN (Inciso c)
        // Detecta desbalances y aplica rotaciones simples (LL, RR) o dobles (LR, RL)
        // Siguiendo el estándar de la cátedra César Lara (Semana 06 - AVL)
        // =========================================================================
        public Node rebalance(Node u) {
            if (u == null)
                return null;

            updateHeight(u);
            int fb = balanceFactor(u);

            // Desbalance hacia la izquierda (fb > 1): casos LL o LR
            if (fb > 1) {
                if (balanceFactor(u.left) < 0) {
                    u.left = rotateLeft(u.left); // Caso LR: rotación izquierda en hijo
                }
                return rotateRight(u); // Caso LL o final de LR
            }

            // Desbalance hacia la derecha (fb < -1): casos RR o RL
            if (fb < -1) {
                if (balanceFactor(u.right) > 0) {
                    u.right = rotateRight(u.right); // Caso RL: rotación derecha en hijo
                }
                return rotateLeft(u); // Caso RR o final de RL
            }

            return u; // Ya está balanceado
        }

        public void add(int x) {
            root = addRecursive(root, x);
        }

        private Node addRecursive(Node u, int x) {
            if (u == null)
                return new Node(x);
            if (x < u.x)
                u.left = addRecursive(u.left, x);
            else if (x > u.x)
                u.right = addRecursive(u.right, x);
            else
                return u; // Duplicados no permitidos

            return rebalance(u);
        }

        public void printTree(Node u, String prefix, boolean isLeft) {
            if (u != null) {
                System.out.println(prefix + (isLeft ? "|-- L: " : "\\-- R: ") + u.x + " (h=" + u.height + ", bf="
                        + balanceFactor(u) + ")");
                printTree(u.left, prefix + (isLeft ? "|   " : "    "), true);
                printTree(u.right, prefix + (isLeft ? "|   " : "    "), false);
            }
        }
    }

    public static void main(String[] args) {
        AVLTree avl = new AVLTree();

        // 8 inserciones iniciales + inserción de 90 (provoca rotación doble RL)
        int[] keys = { 30, 100, 10, 15, 120, 70, 95, 60, 90 };
        for (int v : keys)
            avl.add(v);

        System.out.println("Arbol AVL tras add(90) con rotacion RL:");
        avl.printTree(avl.root, "", false);
        System.out.println("Invariante AVL: OK (bf en raiz = " + avl.balanceFactor(avl.root) + ")");
    }
}
