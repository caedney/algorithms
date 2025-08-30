package algorithms.Excercises.Excercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Excercise 1.1.10
 * 
 * <p>
 * What is wrong with the following code fragment?
 * </p>
 * 
 * {@snippet :
 * int[] a;
 * for (int i = 0; i < 10; i++)
 *     a[i] = i * i;
 * }
 */
public class Excercise_1_1_10 {
    public static void main() {
        StdOut.println("Excercise 1.1.10");
        StdOut.println("Compile-time error. It does not allocate memory for a[] with new");
    }
}
