package algorithms.Exercises.Exercise_1_3;

import algorithms.CircularQueue;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.29
 * 
 * <p>
 * Write a {@code Queue} implementation that uses a <i>circular</i> linked list,
 * which is the same as a linked list except that no links are <i>null</i> and
 * the value of {@code last.next} is {@code first} whenever the list is not
 * empty. Keep only one {@code Node} instance variable ({@code last}).
 * </p>
 */
public class Exercise_1_3_29 {
    public static void main(String[] args) {
        CircularQueue<Integer> queue = new CircularQueue<Integer>();

        queue.enqueue(1); // 1 → 1
        queue.enqueue(2); // 2 → 1 → 2
        queue.enqueue(3); // 3 → 2 → 1 → 3

        StdOut.println(queue.peek()); // 1
        StdOut.println(queue.dequeue()); // 1
        StdOut.println(queue.peek()); // 2
        StdOut.println(queue.dequeue()); // 2
        StdOut.println(queue.peek()); // 3
        StdOut.println(queue.dequeue()); // 3
        StdOut.println(queue.isEmpty()); // true
    }
}
