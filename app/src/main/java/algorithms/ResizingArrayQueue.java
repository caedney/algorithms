package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import edu.princeton.cs.algs4.StdOut;

@SuppressWarnings("unchecked")
public class ResizingArrayQueue<Item> implements Iterable<Item> {
    private static final int INIT_CAPACITY = 1;
    private Item[] array;
    private int head;
    private int tail;
    private int size;

    public ResizingArrayQueue() {
        this.array = (Item[]) new Object[INIT_CAPACITY];
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

    public int capacity() {
        return array.length;
    }

    private void resize(int capacity) {
        Item[] newArray = (Item[]) new Object[capacity];

        for (int i = 0; i < size; i++)
            newArray[i] = array[(head + i) % array.length];

        array = newArray;
        head = 0;
        tail = size;
    }

    public void enqueue(Item item) {
        if (size == array.length)
            resize(2 * array.length);

        array[tail++] = item;

        if (tail == array.length)
            tail = 0; // wrap-around

        size++;
    }

    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("Queue underflow");

        Item item = array[head];
        array[head] = null; // to avoid loitering
        size--;
        head++;

        if (head == array.length)
            head = 0; // wrap-around

        if (size > 0 && size == array.length / 4)
            resize(array.length / 2);

        return item;
    }

    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");

        for (Item value : this)
            joiner.add(String.valueOf(value));

        return joiner.toString();
    }

    public Iterator<Item> iterator() {
        return new ResizingArrayQueueIterator();
    }

    private class ResizingArrayQueueIterator implements Iterator<Item> {
        private int index;

        public ResizingArrayQueueIterator() {
            this.index = 0;
        }

        public boolean hasNext() {
            return index < size;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            Item item = array[(index + head) % array.length];
            index++;

            return item;
        }
    }

    public static void main(String[] args) {
        ResizingArrayQueue<String> queue = new ResizingArrayQueue<>();
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        queue.enqueue("D");
        queue.enqueue("E");
        queue.enqueue("F");
        queue.dequeue();
        queue.dequeue();
        StdOut.println(queue.toString()); // [C, D, E, F]
    }
}
