package algorithms;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

@SuppressWarnings("unchecked")
public class FixedCapacityStack<Item> {
    private Item[] array;
    private int size;

    public FixedCapacityStack(int capacity) {
        this.array = (Item[]) new Object[capacity];
        this.size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void resize(int max) {
        Item[] temp = (Item[]) new Object[max];

        for (int i = 0; i < size; i++)
            temp[i] = array[i];

        array = temp;
    }

    public void push(Item item) {
        if (size == array.length)
            resize(2 * array.length);

        array[size++] = item;
    }

    public Item pop() {
        Item item = array[--size];

        array[size] = null;

        if (size > 0 && size == array.length / 4)
            resize(array.length / 2);

        return item;
    }

    public static void main(String[] args) {
        FixedCapacityStack<String> stack = new FixedCapacityStack<String>(100);

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) {
                stack.push(item);
            } else if (!stack.isEmpty())
                StdOut.print(stack.pop() + " ");
        }

        StdOut.println("(" + stack.size() + " left on stack)");
    }
}
