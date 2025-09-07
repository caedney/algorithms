package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class ResizingArrayDeque<Item> implements Iterable<Item> {
    private static final int INIT_CAPACITY = 1;
    private Item[] array;
    private int first;
    private int last;
    private int size;

    public ResizingArrayDeque() {
        array = (Item[]) new Object[INIT_CAPACITY];
        size = 0;
        first = 0;
        last = 0;
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
            newArray[i] = array[(first + i) % array.length];

        array = newArray;
        first = 0;
        last = size;
    }

    public void pushLeft(Item item) {
        if (size == array.length)
            resize(2 * array.length);

        first = (first - 1 + array.length) % array.length; // move head left (wrap around)
        array[first] = item;

        size++;
    }

    public void pushRight(Item item) {
        if (size == array.length)
            resize(2 * array.length);

        array[last] = item; // insert at tail
        last = (last + 1) % array.length; // advance tail (wrap around)

        size++;
    }

    public Item popLeft() {
        if (isEmpty())
            throw new NoSuchElementException("Deque underflow");

        Item item = array[first];
        array[first] = null; // avoid loitering
        first = (first + 1) % array.length;
        size--;

        if (size > 0 && size == array.length / 4)
            resize(array.length / 2);

        return item;
    }

    public Item popRight() {
        if (isEmpty())
            throw new NoSuchElementException("Deque underflow");

        last = (last - 1 + array.length) % array.length; // move tail back
        Item item = array[last]; // grab the last element
        array[last] = null; // avoid loitering
        size--;

        if (size > 0 && size == array.length / 4) {
            resize(array.length / 2);
        }

        return item;
    }

    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<Item> {
        private int i = 0;

        public boolean hasNext() {
            return i < array.length;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            return array[i++];
        }
    }
}
