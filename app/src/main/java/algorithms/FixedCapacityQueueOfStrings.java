package algorithms;

import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class FixedCapacityQueueOfStrings {
    private String[] array;
    private int head;
    private int tail;
    private int size;

    public FixedCapacityQueueOfStrings(int capacity) {
        this.array = new String[capacity];
        this.head = 0;
        this.tail = 0;
        this.size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(String string) {
        if (size == array.length)
            throw new IndexOutOfBoundsException("Queue overflow");

        array[tail++] = string;

        if (tail == array.length)
            tail = 0; // wrap around

        size++;
    }

    public String dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("Queue underflow");

        String string = array[0];

        string = array[head];
        array[head] = null; // to avoid loitering
        size--;
        head++;

        if (head == array.length)
            head = 0; // wrap-around

        return string;
    }

    public static void main(String[] args) {
        FixedCapacityQueueOfStrings queue = new FixedCapacityQueueOfStrings(5);

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) {
                queue.enqueue(item);
            } else if (!queue.isEmpty())
                StdOut.print(queue.dequeue() + " ");
        }

        StdOut.println("(" + queue.size() + " left on queue)");
    }
}
