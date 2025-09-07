package algorithms.Exercises.Exercise_1_3;

import java.io.ByteArrayInputStream;

import algorithms.Steque;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.32
 * 
 * <p>
 * <i>Steque</i>. A stack-ended queue or <i>steque</i> is a data type that
 * supports <i>push</i>, <i>pop</i>, and <i>enqueue</i>. Articulate an API for
 * this ADT. Develop a linked-list-based implementation.
 * </p>
 */
public class Exercise_1_3_32 {
    public static void main(String[] args) {
        String input = "1 2 3 4 5";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Steque<Integer> steque = new Steque<Integer>();

        while (!StdIn.isEmpty())
            steque.push(StdIn.readInt());

        steque.enqueue(6);
        steque.push(7);

        for (int value : steque)
            StdOut.print(value + " ");

        StdOut.println();
        StdOut.println("removed top item: " + steque.pop());
        StdOut.println("removed top item: " + steque.pop());
        StdOut.println("removed top item: " + steque.pop());
        StdOut.println("removed top item: " + steque.pop());
        StdOut.println("removed top item: " + steque.pop());
        StdOut.println("removed top item: " + steque.pop());
        StdOut.println("removed top item: " + steque.pop());
    }
}
