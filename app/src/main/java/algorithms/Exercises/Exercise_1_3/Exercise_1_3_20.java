package algorithms.Exercises.Exercise_1_3;

import algorithms.LinkedList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.20
 * 
 * <p>
 * Write a method {@code delete()} that takes an {@code int} argument {@code k}
 * and deletes the {@code k}th element in a linked list, if it exists.
 * </p>
 */
public class Exercise_1_3_20 {
    public static void main(String[] args) {
        In in = new In("src/main/resources/data/tobe.txt");
        LinkedList<String> list = new LinkedList<String>();

        while (!in.isEmpty()) {
            String item = in.readString();
            list.append(item);
            StdOut.print(item + " ");
        }

        StdOut.println(); // to be or not to be that is the question
        list.delete(10);

        for (String string : list)
            StdOut.print(string + " ");

        StdOut.println(); // to be or not to be that is the
    }
}
