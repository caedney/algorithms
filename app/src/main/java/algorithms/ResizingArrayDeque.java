package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import edu.princeton.cs.algs4.StdOut;

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
        return new ResizingArrayDequeIterator();
    }

    private class ResizingArrayDequeIterator implements Iterator<Item> {
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

    public static void main(String[] args) {
        Deque<String> d = new Deque<>();
        d.pushLeft("A");
        d.pushLeft("B");
        d.pushLeft("C");
        d.pushLeft("D");
        d.pushLeft("E");
        d.pushLeft("F");
        d.popLeft();
        d.popLeft();

        StringJoiner joiner = new StringJoiner(", ", "[", "]");
        for (String value : d)
            joiner.add(String.valueOf(value));

        StdOut.println(joiner.toString()); // [D, C, B, A]

        Deque<String> e = new Deque<>();
        e.pushRight("A");
        e.pushRight("B");
        e.pushRight("C");
        e.pushRight("D");
        e.pushRight("E");
        e.pushRight("F");
        e.popRight();
        e.popRight();

        joiner = new StringJoiner(", ", "[", "]");
        for (String value : e)
            joiner.add(String.valueOf(value));

        StdOut.println(joiner.toString()); // [A, B, C, D]
    }
}
