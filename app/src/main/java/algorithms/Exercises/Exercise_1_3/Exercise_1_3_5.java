package algorithms.Exercises.Exercise_1_3;

import algorithms.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.5
 * 
 * <p>
 * What does the following code fragment print when {@code N} is {@code 50}?
 * Give a high-level description of what it does when presented with a positive
 * integer {@code N}.
 * </p>
 * 
 * {@snippet :
 * Stack<Integer> stack = new Stack<Integer>();
 * while (N > 0) {
 *     stack.push(N % 2);
 *     N = N / 2;
 * }
 * 
 * for (int d : stack)
 *     StdOut.print(d);
 * 
 * StdOut.println();
 * }
 * 
 * <i>Answer</i>: Prints the binary representation of {@code N} ({@code 110010}
 * when {@code N} is {@code 50})
 */
public class Exercise_1_3_5 {
    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<Integer>();
        int N = 50;

        while (N > 0) {
            stack.push(N % 2);
            StdOut.println("N " + N + " " + N % 2);
            N = N / 2;
        }
        // N 50 0
        // N 25 1
        // N 12 0
        // N 6 0
        // N 3 1
        // N 1 1

        for (int d : stack)
            StdOut.print(d);

        StdOut.println(); // 110010
    }
}
