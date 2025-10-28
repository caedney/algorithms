package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/******************************************************************************
 * ResizingArrayStack
 * 
 * <p>
 * A stack backed by an array that grows and shrinks with its contents: the
 * array doubles when a push finds it full and halves when a pop leaves it
 * one-quarter full, so the stack never uses more than four times the space it
 * needs and never resizes twice in quick succession.
 * </p>
 * 
 * <pre>
 * ./gradlew run -PmainClass=ResizingArrayStack < "app/src/data/algs4/tobe.txt"
 * </pre>
 ******************************************************************************/
public class ResizingArrayStack<Item> implements Iterable<Item> {
    private static final int INIT_CAPACITY = 1;
    private Item[] array;
    private int size;

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("null")
    public Item pop() {
        Item item = array[--size];

        array[size] = null; // avoid loitering

        if (size > 0 && size == array.length / 4)
            resize(array.length / 2);

        return item;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();

        for (Item item : this) {
            s.append(item);
            s.append(' ');
        }

        return s.toString();
    }

    public Iterator<Item> iterator() {
        return new ResizingArrayStackIterator();
    }

    /**
     * Iterates over the items in LIFO order, top of the stack first, by walking the
     * array backwards from the last item pushed. The stack is left unchanged.
     */
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
        ResizingArrayStack<String> stack = new ResizingArrayStack<String>();

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();

            if (!item.equals("-")) {
                stack.push(item);
            } else if (!stack.isEmpty()) {
                StdOut.print(stack.pop() + " ");
            }
        }

        StdOut.println("(" + stack.size() + " left on stack)");
    }
}
