package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class ArrayRingBuffer<Item> implements RingBuffer<Item>, Iterable<Item> {
    private Item[] buffer;
    private int front;
    private int back;
    private int size;

    public ArrayRingBuffer(int capacity) {
        if (capacity < 1)
            throw new IllegalArgumentException("Capacity must be > 0");

        buffer = (Item[]) new Object[capacity];
        front = 0;
        back = 0;
        size = 0;
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

    public Iterator<Item> iterator() {
        return new ArrayRingBufferIterator();
    }

    private class ArrayRingBufferIterator implements Iterator<Item> {
        private int i = 0;

        public boolean hasNext() {
            return i < size;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            Item item = buffer[(i + front) % buffer.length];
            i++;

            return item;
        }
    }
}
