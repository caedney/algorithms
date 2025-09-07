package algorithms.Exercises.Exercise_1_3;

import java.io.ByteArrayInputStream;

import algorithms.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.2
 * 
 * <p>
 * Give the output printed by {@code java Stack} for the input
 * {@code it was - the best - of times - - - it was - the - -}
 * </p>
 */
public class Exercise_1_3_2 {
    public static void main(String[] args) {
        Stack<String> stack = new Stack<String>();

        String input = "it was - the best - of times - - - it was - the - -";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();

            if (!item.equals("-"))
                stack.push(item);
            else if (!stack.isEmpty())
                StdOut.print(stack.pop() + " "); // was best times of the was the it
        }

        StdOut.println();
    }
}
