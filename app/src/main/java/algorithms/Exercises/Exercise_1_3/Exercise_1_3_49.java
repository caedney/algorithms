package algorithms.Exercises.Exercise_1_3;

import algorithms.QueueWithThreeStacks;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.49
 * 
 * <p>
 * <i>Queue with three stacks</i>. Implement a queue with three stacks so that
 * each queue operation takes a constant (worst-case) number of stack
 * operations. <i>Warning</i>: high degree of difficulty.
 * </p>
 * 
 * <p>
 * <b>Key Idea</b>
 * </p>
 * <ul>
 * <li><b>Stack</b> {@code in} → where new elements are enqueued.</li>
 * <li><b>Stack</b> {@code out} → where elements are dequeued from.</li>
 * <li><b>Stack</b> {@code buffer} → to incrementally help transfer elements
 * from {@code in} to {@code out}.</li>
 * </ul>
 */
public class Exercise_1_3_49 {
    public static void main(String[] args) {
        QueueWithThreeStacks<Integer> queue = new QueueWithThreeStacks<>();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        StdOut.println(queue.dequeue()); // 1
        queue.enqueue(4);
        StdOut.println(queue.dequeue()); // 2
        StdOut.println(queue.dequeue()); // 3
        StdOut.println(queue.dequeue()); // 4
    }
}
