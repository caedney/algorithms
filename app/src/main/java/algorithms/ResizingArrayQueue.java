package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

@SuppressWarnings("unchecked")
public class ResizingArrayQueue<Item> implements Iterable<Item> {
    private static final int INIT_CAPACITY = 1; // initial capacity
    private Item[] q; // stack items
    private int n; // number of elements on queue
    private int first; // index of first element of queue
    private int last; // index of next available slot

    /**
     * Initializes an empty queue.
     */
    public ResizingArrayQueue() {
        q = (Item[]) new Object[INIT_CAPACITY];
        n = 0;
        first = 0;
        last = 0;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public int capacity() {
        return q.length;
    }

    // move stack to new array of size max
    private void resize(int capacity) {
        assert capacity >= n;

        Item[] copy = (Item[]) new Object[capacity];

        for (int i = 0; i < n; i++)
            copy[i] = q[(first + i) % q.length];

        q = copy;
        first = 0;
        last = n;
    }

    /**
     * Adds the item to this queue.
     * 
     * @param item the item to add
     */
    public void enqueue(Item item) {
        if (n == q.length)
            resize(2 * q.length); // double size of array if necessary

        q[last++] = item; // add item

        if (last == q.length)
            last = 0; // wrap-around

        n++;
    }

    /**
     * Removes and returns the item on this queue that was least recently added.
     * 
     * @return the item on this queue that was least recently added
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("Queue underflow");

        Item item = q[first];
        q[first] = null; // to avoid loitering
        n--;
        first++;

        if (first == q.length)
            first = 0; // wrap-around

        if (n > 0 && n == q.length / 4)
            resize(q.length / 2); // shrink size of array if necessary

        return item;
    }

    public Iterator<Item> iterator() {
        return new ReverseArrayIterator();
    }

    // support LIFO iteration
    private class ReverseArrayIterator implements Iterator<Item> {
        private int i = n;

        public boolean hasNext() {
            return i > 0;
        }

        public Item next() {
            return q[--i];
        }

        public void remove() {
        }
    }

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
