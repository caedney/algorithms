package algorithms.Exercises.Exercise_1_3;

import java.io.ByteArrayInputStream;

import algorithms.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.7
 * 
 * <p>
 * Add a method {@code peek()} to {@code Stack} that returns the most recently
 * inserted item on the stack (without popping it).
 * </p>
 */
public class Exercise_1_3_7 {
    public static void main(String[] args) {
        Stack<String> stack = new Stack<String>();

        String input = "it was the best of times";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();

            if (!item.equals("-"))
                stack.push(item);
        }

        StdOut.println(stack.peek()); // times;
    }
}
