package algorithms;

import java.util.Iterator;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

@SuppressWarnings("unchecked")
public class ResizingArrayStack<Item> implements Iterable<Item> {
    private Item[] a = (Item[]) new Object[1]; // stack items
    private int N = 0; // number of items

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    // move stack to new array of size max
    private void resize(int max) {
        Item[] temp = (Item[]) new Object[max];

        for (int i = 0; i < N; i++)
            temp[i] = a[i];

        a = temp;
    }

    // add item to top of stack
    public void push(Item item) {
        if (N == a.length)
            resize(2 * a.length);
        a[N++] = item;
    }

    // remove item from top of stack
    public Item pop() {
        Item item = a[--N];

        a[N] = null; // avoid loitering

        if (N > 0 && N == a.length / 4)
            resize(a.length / 2);

        return item;
    }

    public Iterator<Item> iterator() {
        return new ReverseArrayIterator();
    }

    // support LIFO iteration
    private class ReverseArrayIterator implements Iterator<Item> {
        private int i = N;

        public boolean hasNext() {
            return i > 0;
        }

        public Item next() {
            return a[--i];
        }

        public void remove() {
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
