package algorithms.Excercises.Excercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Excercise 1.1.16
 * 
 * <p>
 * Give the value of {@code exR1(6)}:
 * </p>
 * 
 * {@snippet :
 * public static String exR1(int n) {
 *     if (n <= 0)
 *         return "";
 * 
 *     return exR1(n - 3) + n + exR1(n - 2) + n;
 * }
 * }
 */
public class Excercise_1_1_16 {
    public static String exR1(int n) {
        if (n <= 0)
            return "";

        return exR1(n - 3) + n + exR1(n - 2) + n;
    }

    public static void main() {
        StdOut.println("Excercise 1.1.16");

        String result = exR1(6);
        // (("")3(("")1("")1)3)6((("")1("")1)4(("")2("")2)4)6
        // 311361142246
        StdOut.println(result);
    }
}
