package algorithms.Exercises.Exercise_1_3;

import java.io.FileInputStream;
import java.io.IOException;

import algorithms.FixedCapacityStackOfStrings;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.1
 * 
 * <p>
 * Add a method {@code isFull()} to {@code FixedCapacityStackOfStrings}.
 * </p>
 */
public class Exercise_1_3_1 {
    public static void main(String[] args) throws IOException {
        System.setIn(new FileInputStream("src/data/algs4/tobe.txt"));
        FixedCapacityStackOfStrings stack = new FixedCapacityStackOfStrings(8);

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();

            if (!item.equals("-"))
                stack.push(item);
        }

        StdOut.println("(" + stack.size() + " left on stack)");
        StdOut.println("stack is " + (stack.isFull() ? "full" : "not full"));
    }
}
