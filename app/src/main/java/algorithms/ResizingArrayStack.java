package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

@SuppressWarnings("unchecked")
public class ResizingArrayStack<Item> implements Iterable<Item> {
    private static final int INIT_CAPACITY = 1;
    private Item[] array;
    private int size;

    public ResizingArrayStack() {
        array = (Item[]) new Object[INIT_CAPACITY];
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

    private void resize(int max) {
        Item[] newArray = (Item[]) new Object[max];

        for (int i = 0; i < size; i++)
            newArray[i] = array[i];

        array = newArray;
    }

    public void push(Item item) {
        if (size == array.length)
            resize(2 * array.length);

        array[size++] = item;
    }

    public Item pop() {
        Item item = array[--size];

        array[size] = null; // avoid loitering

        if (size > 0 && size == array.length / 4)
            resize(array.length / 2);

        return item;
    }

    public Iterator<Item> iterator() {
        return new ReverseArrayIterator();
    }

    private class ReverseArrayIterator implements Iterator<Item> {
        private int i = size - 1;

        public boolean hasNext() {
            return i >= 0;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            return array[i--];
        }
    }

    public static void main(String[] args) {
        ResizingArrayStack<String> collection = new ResizingArrayStack<String>();

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) {
                collection.push(item);
            } else if (!collection.isEmpty())
                StdOut.print(collection.pop() + " ");
        }

        StdOut.println("(" + collection.size() + " left on stack)");

        // print remaining items
        for (String s : collection) {
            StdOut.println(s);
        }

        // foreach statment is shorthand for the following while statement
        Iterator<String> i = collection.iterator();

        while (i.hasNext()) {
            String s = i.next();
            StdOut.println(s);
        }
    }
}
