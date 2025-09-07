package algorithms;

import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

@SuppressWarnings("unchecked")
public class FixedCapacityQueue<Item> {
    private Item[] q;
    private int n;
    private int first;
    private int last;

    public FixedCapacityQueue(int cap) {
        q = (Item[]) new Object[cap];
        first = 0;
        last = 0;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public void enqueue(Item item) {
        if (n == q.length)
            throw new ArrayIndexOutOfBoundsException("Queue overflow");

        q[last++] = item; // add item

        if (last == q.length)
            last = 0; // wrap around

        n++;
    }

    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("Queue underflow");

        Item string = q[0];

        string = q[first];
        q[first] = null; // to avoid loitering
        n--;
        first++;

        if (first == q.length)
            first = 0; // wrap-around

        return string;
    }

    public static void main(String[] args) {
        FixedCapacityQueue<String> queue = new FixedCapacityQueue<>(5);

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
