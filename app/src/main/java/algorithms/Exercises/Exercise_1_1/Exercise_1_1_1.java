package algorithms.Exercises.Exercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.1
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
 */
public class Exercise_1_1_1 {
    public static void main(String[] args) {
        StdOut.println((0 + 15) / 2); // 7
        StdOut.println(2.0e-6 * 100000000.1); // 200.0000002
        StdOut.println(true && false || true && false); // false
    }
}
