package algorithms;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/******************************************************************************
 * FixedCapacityStackOfStrings
 * 
 * <p>
 * A stack of strings backed by an array whose length is fixed when the stack is
 * created. Pushing onto a full stack is not checked: the client is responsible
 * for not exceeding the capacity.
 * </p>
 * 
 * <pre>
 * ./gradlew run -PmainClass=FixedCapacityStackOfStrings < "app/src/data/algs4/tobe.txt"
 * </pre>
 ******************************************************************************/
public class FixedCapacityStackOfStrings {
    private String[] array;
    private int size;

    public FixedCapacityStackOfStrings(int capacity) {
        this.array = new String[capacity];
        this.size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == array.length;
    }

    public int size() {
        return size;
    }

    public void push(String item) {
        array[size++] = item;
    }

    public String pop() {
        return array[--size];
    }

    public static void main(String[] args) {
        FixedCapacityStackOfStrings stack = new FixedCapacityStackOfStrings(100);

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
