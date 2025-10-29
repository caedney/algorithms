package algorithms.Exercises.Exercise_1_3;

import algorithms.ResizingArrayQueueOfStrings;
import edu.princeton.cs.algs4.StdOut;

/**
 * Complete this task
 */

/**
 * Exercise 1.3.14
 * 
 * <p>
 * Develop a class {@code ResizingArrayQueueOfStrings} that implements the queue
 * abstraction with a fixed-size array, and then extend your implementation to
 * use array resizing to remove the size restriction.
 * </p>
 */
public class Exercise_1_3_14 {
    public static void main(String[] args) {
        ResizingArrayQueueOfStrings queue = new ResizingArrayQueueOfStrings();
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        queue.enqueue("D");
        queue.enqueue("E");
        queue.enqueue("F");
        queue.dequeue();
        queue.dequeue();
        StdOut.println(queue); // [C, D, E, F]
    }
}
