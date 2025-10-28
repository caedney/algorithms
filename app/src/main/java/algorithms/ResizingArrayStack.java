package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

@SuppressWarnings("unchecked")
public class ResizingArrayStack<Item> implements Iterable<Item> {
    private static final int INIT_CAPACITY = 1;
    private Item[] array;
    private int size;

    public ResizingArrayStack() {
        this.array = (Item[]) new Object[INIT_CAPACITY];
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

    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");

        for (Item value : this)
            joiner.add(String.valueOf(value));

        return joiner.toString();
    }

    public Iterator<Item> iterator() {
        return new ResizingArrayStackIterator();
    }

    private class ResizingArrayStackIterator implements Iterator<Item> {
        private int index;

        public ResizingArrayStackIterator() {
            this.index = size - 1;
        }

        public boolean hasNext() {
            return index >= 0;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            return array[index--];
        }
    }

    public static void main(String[] args) {
        In in = new In("src/data/algs4/tobe.txt");
        ResizingArrayStack<String> stack = new ResizingArrayStack<String>();

        while (!in.isEmpty()) {
            String item = in.readString();
            if (!item.equals("-")) {
                stack.push(item);
            } else if (!stack.isEmpty())
                stack.pop();
        }

        StdOut.println("(" + stack.size() + " left on stack)");
        StdOut.println(stack.toString());
    }
}
