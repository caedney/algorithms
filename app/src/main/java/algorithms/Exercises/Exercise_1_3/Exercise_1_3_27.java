package algorithms.Exercises.Exercise_1_3;

import algorithms.LinkedList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.27
 * 
 * <p>
 * Write a method {@code max()} that takes a reference to the first node in a
 * linked list as argument and returns the value of the maximum key in the list.
 * Assume that all keys are positive integers, and return {@code 0} if the list
 * is empty.
 * </p>
 */
public class Exercise_1_3_27 {
    public static void main(String[] args) {
        In in = new In("src/main/resources/data/keys.txt");
        LinkedList<Integer> list = new LinkedList<Integer>();

        while (!in.isEmpty()) {
            int item = in.readInt();
            list.append(item);
        }

        int max = list.max();
        StdOut.println(max); // 12343
    }
}
