package algorithms.Exercises.Exercise_1_3;

import java.io.ByteArrayInputStream;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.9
 * 
 * <p>
 * Write a program that takes from standard input an expression without left
 * parentheses and prints the equivalent infix expression with the parentheses
 * inserted. For example, given the input:
 * </p>
 * 
 * <p>
 * {@code 1 + 2 ) * 3 - 4 ) * 5 - 6 ) ) ) }
 * </p>
 * 
 * <p>
 * your program should print
 * </p>
 * 
 * <p>
 * {@code ( ( 1 + 2 ) * ( ( 3 - 4 ) * ( 5 - 6 ) ) }
 * </p>
 */
public class Exercise_1_3_9 {
    public static void main(String[] args) {
        String input = "1 + 2 ) * 3 - 4 ) * 5 - 6 ) ) )";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Stack<String> ops = new Stack<String>();
        Stack<String> vals = new Stack<String>();

        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();

            if (s.equals("+"))
                ops.push(s);
            else if (s.equals("-"))
                ops.push(s);
            else if (s.equals("*"))
                ops.push(s);
            else if (s.equals(")")) {
                String right = vals.pop();
                String left = vals.pop();
                String op = ops.pop();
                String expr = "( " + left + " " + op + " " + right + " )";
                vals.push(expr);
            } else {
                vals.push(s);
            }
        }

        StdOut.println(vals.pop()); // ( ( 1 + 2 ) * ( ( 3 - 4 ) * ( 5 - 6 ) ) )
    }
}
