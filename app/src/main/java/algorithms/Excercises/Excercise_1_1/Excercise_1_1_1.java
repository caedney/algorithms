package algorithms.Excercises.Excercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Excercise 1.1.1
 * 
 * <p>
 * Give the value of each of the following expressions:
 * </p>
 * 
 * <ol type="a">
 * <li>(0 + 15) / 2</li>
 * <li>2.0e-6 * 100000000.1</li>
 * <li>true && false || true && false</li>
 * </ol>
 * 
 */
public class Excercise_1_1_1 {
    public static void main() {
        StdOut.println("Excercise 1.1.1");
        StdOut.printf("(0 + 15) / 2 = %d\n", (0 + 15) / 2);
        StdOut.println("2.0e-6 * 100000000.1 = " + (2.0e-6 * 100000000.1));
        StdOut.printf("true && false || true && false = %b\n", true && false || true && false);
    }
}
