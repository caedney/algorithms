package algorithms.Exercises.Exercise_1_3;

import algorithms.InfixToPostfix;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.10
 * 
 * <p>
 * Write a filter {@code InfixToPostfix} that converts an arithmetic expression
 * from infix to postfix.
 * </p>
 */
public class Exercise_1_3_10 {
    public static void main(String[] args) {
        String infix = "( 2 + ( ( 3 + 4 ) * ( 5 * 6 ) ) )";
        String postfix = InfixToPostfix.convert(infix);
        StdOut.println("Infix:   " + infix);
        StdOut.println("Postfix: " + postfix);

        infix = "( ( ( 5 + ( 7 * ( 1 + 1 ) ) ) * 3 ) + ( 2 * ( 1 + 1 ) ) )";
        postfix = InfixToPostfix.convert(infix);
        StdOut.println("Infix:   " + infix);
        StdOut.println("Postfix: " + postfix);
    }
}
