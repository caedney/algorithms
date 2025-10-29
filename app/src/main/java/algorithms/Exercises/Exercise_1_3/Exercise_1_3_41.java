package algorithms.Exercises.Exercise_1_3;

import algorithms.Queue;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.41
 * 
 * <p>
 * <i>Copy a queue</i>. Create a new constructor so
 * {@snippet :
 * Queue<Item> r = new Queue<Item>(q);
 * }
 * makes {@code r} a reference to a new and independent copy of the queue
 * {@code q}. You should be able to push and pop from either {@code q} or
 * {@code r} without influencing the other. <i>Hint</i>: Delete all of the
 * elements from {@code q} and add these elements to both {@code q} and
 * {@code r}.
 * </p>
 */
public class Exercise_1_3_41 {
    public static void main(String[] args) {
        Queue<Integer> q = new Queue<>();
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);

        Queue<Integer> r = new Queue<>(q);
        q.dequeue();
        r.enqueue(4);

        StdOut.println(q.size()); // 2
        StdOut.println(r.size()); // 4
    }
}
