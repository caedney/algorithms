package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import edu.princeton.cs.algs4.StdOut;

@SuppressWarnings("unchecked")
public class ArrayRingBuffer<Item> implements RingBuffer<Item>, Iterable<Item> {
    private Item[] buffer;
    private int front;
    private int back;
    private int size;

    public ArrayRingBuffer(int capacity) {
        if (capacity < 1)
            throw new IllegalArgumentException("Capacity must be > 0");

        this.buffer = (Item[]) new Object[capacity];
        this.front = 0;
        this.back = 0;
        this.size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == buffer.length;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return buffer.length;
    }

    public Item peek() {
        if (isEmpty())
            throw new NoSuchElementException("Ring buffer is empty");

        return buffer[front];
    }

    public void enqueue(Item item) {
        buffer[back] = item;
        back = (back + 1) % buffer.length;

        if (isFull())
            front = (front + 1) % buffer.length;
        else
            size++;

    }

    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("Ring buffer underflow");

        Item item = buffer[front];
        buffer[front] = null; // avoid loitering
        front = (front + 1) % buffer.length;

        size--;

        return item;
    }

    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");

        for (Item value : this)
            joiner.add(String.valueOf(value));

        return joiner.toString();
    }

    public Iterator<Item> iterator() {
        return new ArrayRingBufferIterator();
    }

    private class ArrayRingBufferIterator implements Iterator<Item> {
        private int index;

        public ArrayRingBufferIterator() {
            this.index = 0;
        }

        public boolean hasNext() {
            return index < size;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            Item item = buffer[(index + front) % buffer.length];
            index++;

            return item;
        }
    }

    public static void main(String[] args) {
        ArrayRingBuffer<Integer> buffer = new ArrayRingBuffer<>(5);
        buffer.enqueue(10);
        buffer.enqueue(20);
        buffer.enqueue(30);
        buffer.enqueue(40);
        buffer.enqueue(50);
        StdOut.println(buffer.toString()); // [10, 20, 30, 40, 50]

        buffer.enqueue(60);
        buffer.enqueue(70);
        buffer.enqueue(80);
        StdOut.println(buffer.toString()); // [40, 50, 60, 70, 80]
    }
}
