package algorithms;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/******************************************************************************
 * FixedCapacityStack
 * 
 * <p>
 * A generic stack backed by an array whose length is fixed when the stack is
 * created. Pushing onto a full stack is not checked: the client is responsible
 * for not exceeding the capacity.
 * </p>
 * 
 * <pre>
 * ./gradlew run -PmainClass=FixedCapacityStack < "app/src/data/algs4/tobe.txt"
 * </pre>
 ******************************************************************************/
public class FixedCapacityStack<Item> {
    private Item[] array;
    private int size;

    @SuppressWarnings("unchecked")
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

    public void push(Item item) {
        array[size++] = item;
    }

    public Item pop() {
        return array[--size];
    }

    public static void main(String[] args) {
        FixedCapacityStack<String> stack = new FixedCapacityStack<String>(100);

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
