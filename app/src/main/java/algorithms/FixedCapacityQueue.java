package algorithms;

import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

@SuppressWarnings("unchecked")
public class FixedCapacityQueue<Item> {
    private Item[] array;
    private int head;
    private int tail;
    private int size;

    public FixedCapacityQueue(int capacity) {
        this.array = (Item[]) new Object[capacity];
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

    public void enqueue(Item item) {
        if (size == array.length)
            throw new ArrayIndexOutOfBoundsException("Queue overflow");

        array[tail++] = item;

        if (tail == array.length)
            tail = 0; // wrap around

        size++;
    }

    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("Queue underflow");

        Item string = array[0];

        string = array[head];
        array[head] = null; // to avoid loitering
        size--;
        head++;

        if (head == array.length)
            head = 0; // wrap-around

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
