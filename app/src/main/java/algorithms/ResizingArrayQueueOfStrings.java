package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import edu.princeton.cs.algs4.StdOut;

public class ResizingArrayQueueOfStrings implements Iterable<String> {
    private static final int INIT_CAPACITY = 1;
    private String[] array;
    private int head;
    private int tail;
    private int size;

    public ResizingArrayQueueOfStrings() {
        this.array = new String[INIT_CAPACITY];
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
        String[] newArray = new String[capacity];

        for (int i = 0; i < size; i++)
            newArray[i] = array[(head + i) % array.length];

        array = newArray;
        head = 0;
        tail = size;
    }

    public void enqueue(String item) {
        if (size == array.length)
            resize(2 * array.length);

        array[tail++] = item;

        if (tail == array.length)
            tail = 0; // wrap-around

        size++;
    }

    public String dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("Queue underflow");

        String item = array[head];
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

        for (String value : this)
            joiner.add(String.valueOf(value));

        return joiner.toString();
    }

    public Iterator<String> iterator() {
        return new ResizingArrayQueueOfStringsIterator();
    }

    private class ResizingArrayQueueOfStringsIterator implements Iterator<String> {
        private int index;

        public ResizingArrayQueueOfStringsIterator() {
            this.index = 0;
        }

        public boolean hasNext() {
            return index < size;
        }

        public String next() {
            if (!hasNext())
                throw new NoSuchElementException();

            String item = array[(index + head) % array.length];
            index++;

            return item;
        }
    }

    public static void main(String[] args) {
        ResizingArrayQueueOfStrings queue = new ResizingArrayQueueOfStrings();
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        queue.enqueue("D");
        queue.enqueue("E");
        queue.enqueue("F");
        queue.dequeue();
        queue.dequeue();
        StdOut.println(queue); // [C, D, E, F]
    }
}
