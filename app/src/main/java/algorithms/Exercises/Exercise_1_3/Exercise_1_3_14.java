package algorithms.Exercises.Exercise_1_3;

import algorithms.ResizingArrayQueue;
import edu.princeton.cs.algs4.StdIn;
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
        ResizingArrayQueue<String> collection = new ResizingArrayQueue<>();

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) {
                collection.enqueue(item);
            } else if (!collection.isEmpty())
                StdOut.print(collection.dequeue() + " ");
        }

        StdOut.println("(" + collection.size() + " left on queue)");
    }
}
