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
    public static void main(String[] args) {
        StdOut.println("if (a > b) then c = 0;       ❌ then keyword is not part of Java syntax");
        StdOut.println("if a > b { c = 0; }          ❌ condition must be inside parentheses");
        StdOut.println("if (a > b) c = 0;            ✅");
        StdOut.println("if (a > b) c = 0 else b = 0; ❌ each statement must be clearly delimited with a semicolon");
    }
}
