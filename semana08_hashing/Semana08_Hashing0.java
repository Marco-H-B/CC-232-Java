/*
 * CC-232 - Semana 8, lunes: hashing multiplicativo.
 * Adaptación didáctica de ChainedHashTable de Pat Morin.
 */
public class Semana08_Hashing0 {
    static final int W = 32;
    static final int Z = 0x9E3779B9; // entero impar fijo para reproducibilidad

    static int hash(int hashCode, int d) {
        return (Z * hashCode) >>> (W - d);
    }

    static int dimensionFor(int n) {
        int d = 1;
        int cap = 2;

        while (cap <= n) {
            cap *= 2;
            d++;
        }

        return d;
    }

    public static void main(String[] args) {
        try {
            int d = 3;
            for (String key : new String[] { "AVL", "heap", "tabla hash", "BTree" }) {
                int index = hash(key.hashCode(), d);
                System.out.println(key + " -> " + index + " (rango esperado: 0..7)");
            }
            System.out.println("dimensionFor(8)=" + dimensionFor(8) + " (esperado: 4)");
        } catch (UnsupportedOperationException e) {
            System.out.println(e.getMessage());
        }
    }
}
