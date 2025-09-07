package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class ResizingArrayQueue<Item> implements Iterable<Item> {
    private static final int INIT_CAPACITY = 1;
    private Item[] array;
    private int front;
    private int back;
    private int size;

    public ResizingArrayQueue() {
        array = (Item[]) new Object[INIT_CAPACITY];
        front = 0;
        back = 0;
        size = 0;
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
            newArray[i] = array[(front + i) % array.length];

        array = newArray;
        front = 0;
        back = size;
    }

    public void enqueue(Item item) {
        if (size == array.length)
            resize(2 * array.length);

        array[back++] = item;

        if (back == array.length)
            back = 0; // wrap-around

        size++;
    }

    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("Queue underflow");

        Item item = array[front];
        array[front] = null; // to avoid loitering
        size--;
        front++;

        if (front == array.length)
            front = 0; // wrap-around

        if (size > 0 && size == array.length / 4)
            resize(array.length / 2);

        return item;
    }

    public Iterator<Item> iterator() {
        return new ResizingArrayQueueIterator();
    }

    private class ResizingArrayQueueIterator implements Iterator<Item> {
        private int i = 0;

        public boolean hasNext() {
            return i < size;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            Item item = array[(i + front) % array.length];
            i++;

            return item;
        }
    }
}
