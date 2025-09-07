package algorithms.Exercises.Exercise_1_3;

import algorithms.LinkedList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.26
 * 
 * <p>
 * Write a method {@code remove()} that takes a linked list and a string key as
 * arguments and removes all of the nodes in the list that have {@code key} as
 * its item field.
 * </p>
 */
public class Exercise_1_3_26 {
    public static void main(String[] args) {
        In in = new In("src/main/resources/data/tobe.txt");
        LinkedList<String> list = new LinkedList<String>();

        while (!in.isEmpty()) {
            String item = in.readString();
            list.append(item);
            StdOut.print(item + " ");
        }

        StdOut.println(); // to be or not to be that is the question
        list.remove("be");

        for (String string : list)
            StdOut.print(string + " ");

        StdOut.println(); // to or not to that is the question
    }
}
