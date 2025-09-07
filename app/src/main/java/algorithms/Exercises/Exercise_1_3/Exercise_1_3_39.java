package algorithms.Exercises.Exercise_1_3;

import algorithms.ArrayRingBuffer;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.39
 * 
 * <p>
 * <i>Ring buffer</i>. A ring buffer, or circular queue, is a FIFO data
 * structure of a fixed size 𝑁. It is useful for transferring data between
 * asynchronous processes or for storing log files. When the buffer is empty,
 * the consumer waits until data is deposited; when the buffer is full, the
 * producer waits to deposit data. Develop an API for a {@code RingBuffer} and
 * an implementation that uses an array representation (with circular
 * wrap-around).
 * </p>
 */
public class Exercise_1_3_39 {
    public static void main(String[] args) {
        ArrayRingBuffer<Integer> buffer = new ArrayRingBuffer<>(5);

        buffer.enqueue(10);
        buffer.enqueue(20);
        buffer.enqueue(30);
        buffer.enqueue(40);
        buffer.enqueue(50);

        StdOut.println("First element: " + buffer.peek());
        StdOut.println("Size: " + buffer.size());
        StdOut.println("Is full? " + buffer.isFull());

        buffer.enqueue(60);
        buffer.enqueue(70);
        buffer.enqueue(80);
        buffer.enqueue(90);
        buffer.enqueue(100);
        buffer.enqueue(110);
        buffer.enqueue(120);

        StdOut.println("Removed: " + buffer.dequeue());

        for (int value : buffer)
            StdOut.print(value + " ");

        StdOut.println();
    }
}
