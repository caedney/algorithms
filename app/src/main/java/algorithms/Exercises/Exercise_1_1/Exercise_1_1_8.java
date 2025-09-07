package algorithms.Exercises.Exercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.8
 * 
 * <p>
 * What do each of the following print?
 * </p>
 * 
 * <ol type="a">
 * <li>System.out.println("b");</li>
 * <li>System.out.println("b" + "c");</li>
 * <li>System.out.println((char) ("a" + 4));</li>
 * </ol>
 * 
 * <p>
 * Explain each outcome.
 * </p>
 */
public class Exercise_1_1_8 {
    public static void main() {
        StdOut.println("Exercise 1.1.8");
        StdOut.println("a - b");
        StdOut.println("b - bc");
        StdOut.println("c - Compile-time error");
    }
}
