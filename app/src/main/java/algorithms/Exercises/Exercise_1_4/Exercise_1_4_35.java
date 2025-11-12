package algorithms.Exercises.Exercise_1_4;

/******************************************************************************
 * Exercise 1.4.35
 * 
 * <p>
 * <i>Time costs for pushdown stacks</i>. Justify the entries in the table
 * below, which shows typical time costs for various pushdown stack
 * implementations, using a cost model that counts both <i>data references</i>
 * (references to data pushed onto the stack, either an array reference or a
 * reference to an object's instance variable) and <i>objects created</i>.
 * </p>
 * 
 * <table>
 * <thead>
 * <tr>
 * <th>data structure</th>
 * <th>item type</th>
 * <th>data references</th>
 * <th>objects created</th>
 * </tr>
 * </thead> <tbody>
 * <tr>
 * <td><i>linked list</i></td>
 * <td><code>int</code></td>
 * <td>2𝑁</td>
 * <td>𝑁</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><code>Integer</code></td>
 * <td>3𝑁</td>
 * <td>2𝑁</td>
 * </tr>
 * <tr>
 * <td><i>resizing array</i></td>
 * <td><code>int</code></td>
 * <td>~5𝑁</td>
 * <td>lg 𝑁</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><code>Integer</code></td>
 * <td>~5𝑁</td>
 * <td>~𝑁</td>
 * </tr>
 * </tbody>
 * </table>
 * 
 * <p>
 * The justification is in the notes; this client measures it. Four stacks, each
 * the book's code with a counter for each cost, take 𝑁 pushes at every power
 * of two and at one past it, and the client prints the data references per push
 * and the objects created. The linked-list figures are the same on every row.
 * The resizing array's references swing between about 3 and about 5 per push
 * for <code>int</code>, one more for <code>Integer</code> because the write of
 * the value into its box is counted, as it is for the linked list.
 * </p>
 * 
 * <pre>
 * ./gradlew run -PmainClass=Exercises.Exercise_1_4.Exercise_1_4_35
 * ./gradlew run -PmainClass=Exercises.Exercise_1_4.Exercise_1_4_35 --args="16 65536"
 * ./gradlew test --tests "Exercise_1_4_35Test"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_35 {
    /**
     * The book's linked-list stack written for {@code int} items, counting the
     * exercise's two costs. A data reference is a read or write of an instance
     * variable of a node; the stack's own {@code first} and {@code size} are
     * bookkeeping and are not counted.
     */
    static class CountingLinkedStackOfInts {
        private Node first;
        private int size;
        long dataReferences;
        long objectsCreated;

        @SuppressWarnings("unused")
        private class Node {
            int item;
            Node next;
        }

        void push(int item) {
            Node oldFirst = first;

            first = new Node();
            objectsCreated++; // the node

            first.item = item;
            dataReferences++;

            first.next = oldFirst;
            dataReferences++;

            size++;
        }

        int size() {
            return size;
        }
    }

    /**
     * The book's generic linked-list stack with the same two counters. The stack
     * counts only what it does itself; the cost of boxing an {@code int} is added
     * by the client that boxes it.
     */
    static class CountingLinkedStack<Item> {
        private Node first;
        private int size;
        long dataReferences;
        long objectsCreated;

        @SuppressWarnings({ "unused", "null" })
        private class Node {
            Item item;
            Node next;
        }

        void push(Item item) {
            Node oldFirst = first;

            first = new Node();
            objectsCreated++; // the node

            first.item = item;
            dataReferences++;

            first.next = oldFirst;
            dataReferences++;

            size++;
        }

        int size() {
            return size;
        }
    }

    /**
     * The book's resizing array stack written for {@code int} items. A data
     * reference is a read or write of an array slot. The one-slot array made by the
     * constructor is not counted: the costs are those of the pushes.
     */
    static class CountingResizingArrayStackOfInts {
        private int[] array = new int[1];
        private int size;
        long dataReferences;
        long objectsCreated;

        private void resize(int max) {
            int[] newArray = new int[max];
            objectsCreated++; // the array

            for (int i = 0; i < size; i++) {
                newArray[i] = array[i];
                dataReferences += 2; // one read, one write
            }

            array = newArray;
        }

        void push(int item) {
            if (size == array.length)
                resize(2 * array.length);

            array[size++] = item;
            dataReferences++;
        }

        int size() {
            return size;
        }
    }

    /**
     * The book's generic resizing array stack with the same two counters. As with
     * the linked list, the cost of boxing is added by the client that boxes.
     */
    static class CountingResizingArrayStack<Item> {
        @SuppressWarnings("unchecked")
        private Item[] array = (Item[]) new Object[1];
        private int size;
        long dataReferences;
        long objectsCreated;

        @SuppressWarnings("unchecked")
        private void resize(int max) {
            Item[] newArray = (Item[]) new Object[max];
            objectsCreated++; // the array

            for (int i = 0; i < size; i++) {
                newArray[i] = array[i];
                dataReferences += 2; // one read, one write
            }

            array = newArray;
        }

        void push(Item item) {
            if (size == array.length)
                resize(2 * array.length);

            array[size++] = item;
            dataReferences++;
        }

        int size() {
            return size;
        }
    }

    /**
     * The first value pushed. {@code Integer.valueOf} hands out shared objects for
     * -128 to 127, so every value pushed is kept above that range and each box is a
     * new object.
     */
    private static final int FIRST_VALUE = 1000;

    /** Pushes 𝑁 {@code int}s onto a linked-list stack of {@code int}. */
    static long[] linkedListOfInts(int n) {
        CountingLinkedStackOfInts stack = new CountingLinkedStackOfInts();

        for (int i = 0; i < n; i++)
            stack.push(FIRST_VALUE + i);

        return new long[] { stack.dataReferences, stack.objectsCreated };
    }

    /**
     * Pushes 𝑁 {@code int}s onto a linked-list stack of {@code Integer}. Each push
     * boxes its value first: one more object, and one more data reference for the
     * write of the value into the box.
     */
    static long[] linkedListOfIntegers(int n) {
        CountingLinkedStack<Integer> stack = new CountingLinkedStack<>();

        for (int i = 0; i < n; i++) {
            Integer box = Integer.valueOf(FIRST_VALUE + i);
            stack.objectsCreated++; // the box
            stack.dataReferences++; // the value written into the box

            stack.push(box);
        }

        return new long[] { stack.dataReferences, stack.objectsCreated };
    }

    /** Pushes 𝑁 {@code int}s onto a resizing array stack of {@code int}. */
    static long[] resizingArrayOfInts(int n) {
        CountingResizingArrayStackOfInts stack = new CountingResizingArrayStackOfInts();

        for (int i = 0; i < n; i++)
            stack.push(FIRST_VALUE + i);

        return new long[] { stack.dataReferences, stack.objectsCreated };
    }

    /**
     * Pushes 𝑁 {@code int}s onto a resizing array stack of {@code Integer}, boxing
     * each one first and counting the box as above.
     */
    static long[] resizingArrayOfIntegers(int n) {
        CountingResizingArrayStack<Integer> stack = new CountingResizingArrayStack<>();

        for (int i = 0; i < n; i++) {
            Integer box = Integer.valueOf(FIRST_VALUE + i);
            stack.objectsCreated++; // the box
            stack.dataReferences++; // the value written into the box

            stack.push(box);
        }

        return new long[] { stack.dataReferences, stack.objectsCreated };
    }

    private static void printRow(int n) {
        long[] listInt = linkedListOfInts(n);
        long[] listInteger = linkedListOfIntegers(n);
        long[] arrayInt = resizingArrayOfInts(n);
        long[] arrayInteger = resizingArrayOfIntegers(n);

        System.out.printf("%10d %10.3f %10d %10.3f %10d %10.3f %10d %10.3f %10d%n", n, (double) listInt[0] / n,
                listInt[1], (double) listInteger[0] / n, listInteger[1], (double) arrayInt[0] / n, arrayInt[1],
                (double) arrayInteger[0] / n, arrayInteger[1]);
    }

    public static void main(String[] args) {
        int smallest = args.length > 0 ? Integer.parseInt(args[0]) : 16;
        int largest = args.length > 1 ? Integer.parseInt(args[1]) : 1 << 16;

        System.out.printf("%10s %21s %21s %21s %21s%n", "", "linked list int", "linked list Integer",
                "resizing array int", "resizing array Integer");
        System.out.printf("%10s %10s %10s %10s %10s %10s %10s %10s %10s%n", "N", "refs/N", "objects", "refs/N",
                "objects", "refs/N", "objects", "refs/N", "objects");

        for (int n = smallest; n <= largest; n *= 2) {
            printRow(n); // a power of two: the array has not yet doubled past it
            printRow(n + 1); // one past it: the last push has just doubled the array
        }
    }
}
