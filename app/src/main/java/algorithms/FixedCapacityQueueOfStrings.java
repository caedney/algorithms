package algorithms;

import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class FixedCapacityQueueOfStrings {
    private String[] q;
    private int n;
    private int first;
    private int last;

    public FixedCapacityQueueOfStrings(int cap) {
        q = new String[cap];
        first = 0;
        last = 0;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public void enqueue(String string) {
        if (n == q.length)
            throw new IndexOutOfBoundsException("Queue overflow");

        q[last++] = string; // add item

        if (last == q.length)
            last = 0; // wrap around

        n++;
    }

    public String dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("Queue underflow");

        String string = q[0];

        string = q[first];
        q[first] = null; // to avoid loitering
        n--;
        first++;

        if (first == q.length)
            first = 0; // wrap-around

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
