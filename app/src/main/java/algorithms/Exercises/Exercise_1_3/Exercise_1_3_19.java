package algorithms.Exercises.Exercise_1_3;

import algorithms.LinkedList;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.19
 * 
 * <p>
 * Give a code fragment that removes the last node in a linked list whose first
 * node is {@code first}.
 * </p>
 */
public class Exercise_1_3_19 {
    public static void main(String[] args) {
        LinkedList<String> queue = new LinkedList<>();
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        queue.enqueue("D");
        queue.enqueue("E");
        queue.enqueue("F");
        queue.removeLastNode();

        StdOut.println(queue.toString()); // [A, B, C, D, E]
    }
}
