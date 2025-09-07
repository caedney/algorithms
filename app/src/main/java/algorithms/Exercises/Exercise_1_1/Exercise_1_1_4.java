package algorithms.Exercises.Exercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.4
 * 
 * <p>
 * What (if anything) is wrong with each of the following statements?
 * </p>
 * 
 * <ol type="a">
 * <li>if (a > b) then c = 0;</li>
 * <li>if a > b { c = 0; }</li>
 * <li>if (a > b) c = 0;</li>
 * <li>if (a > b) c = 0 else b = 0;</li>
 * </ol>
 */
public class Exercise_1_1_4 {
    public static void main() {
        StdOut.println("Exercise 1.1.4");
        StdOut.println("1 - there is no then operator");
        StdOut.println("2 - parens must be used");
        StdOut.println("3 - is valid");
        StdOut.println("4 - no semi colon after assignment c = 0");
    }
}
