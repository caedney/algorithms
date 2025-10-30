package algorithms.Exercises.Exercise_1_3;

import algorithms.LinkedList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.21
 * 
 * <p>
 * Write a method {@code find()} that takes a linked list and a string
 * {@code key} as arguments and returns {@code true} if some node in the list
 * has {@code key} as its item field, {@code false} otherwise.
 * </p>
 */
public class Exercise_1_3_21 {
    public static void main(String[] args) {
        In in = new In("src/main/resources/data/tobe.txt");
        LinkedList<String> list = new LinkedList<String>();

        while (!in.isEmpty()) {
            String item = in.readString();
            list.append(item);
        }

        boolean found = list.find(list, "question");
        StdOut.println("the key 'question' is in the text: " + found);
    }
}
