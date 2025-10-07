package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import edu.princeton.cs.algs4.StdOut;

@SuppressWarnings("unchecked")
public class ResizingArrayDeque<Item> implements Iterable<Item> {
    private static final int INIT_CAPACITY = 1;
    private Item[] array;
    private int left;
    private int right;
    private int size;

    public ResizingArrayDeque() {
        this.array = (Item[]) new Object[INIT_CAPACITY];
        this.left = 0;
        this.right = 0;
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
            newArray[i] = array[(left + i) % array.length];

        array = newArray;
        left = 0;
        right = size;
    }

    public void pushLeft(Item item) {
        if (size == array.length)
            resize(2 * array.length);

        left = (left - 1 + array.length) % array.length; // move head left (wrap around)
        array[left] = item;

        size++;
    }

    public void pushRight(Item item) {
        if (size == array.length)
            resize(2 * array.length);

        array[right] = item; // insert at tail
        right = (right + 1) % array.length; // advance tail (wrap around)

        size++;
    }

    public Item popLeft() {
        if (isEmpty())
            throw new NoSuchElementException("Deque underflow");

        Item item = array[left];
        array[left] = null; // avoid loitering
        left = (left + 1) % array.length;
        size--;

        if (size > 0 && size == array.length / 4)
            resize(array.length / 2);

        return item;
    }

    public Item popRight() {
        if (isEmpty())
            throw new NoSuchElementException("Deque underflow");

        right = (right - 1 + array.length) % array.length; // move tail back
        Item item = array[right]; // grab the right element
        array[right] = null; // avoid loitering
        size--;

        if (size > 0 && size == array.length / 4) {
            resize(array.length / 2);
        }

        return item;
    }

    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");

        for (Item value : this)
            joiner.add(String.valueOf(value));

        return joiner.toString();
    }

    public Iterator<Item> iterator() {
        return new ResizingArrayDequeIterator();
    }

    private class ResizingArrayDequeIterator implements Iterator<Item> {
        private int index = 0;

        public ResizingArrayDequeIterator() {
            this.index = 0;
        }

        public boolean hasNext() {
            return index < array.length;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            return array[index++];
        }
    }

    public static void main(String[] args) {
        ResizingArrayDeque<String> d = new ResizingArrayDeque<>();
        d.pushLeft("A");
        d.pushLeft("B");
        d.pushLeft("C");
        d.pushLeft("D");
        d.pushLeft("E");
        d.pushLeft("F");
        d.popLeft();
        d.popLeft();
        StdOut.println(d.toString()); // [D, C, B, A]

        ResizingArrayDeque<String> e = new ResizingArrayDeque<>();
        e.pushRight("A");
        e.pushRight("B");
        e.pushRight("C");
        e.pushRight("D");
        e.pushRight("E");
        e.pushRight("F");
        e.popRight();
        e.popRight();
        StdOut.println(e.toString()); // [A, B, C, D]
    }
}
