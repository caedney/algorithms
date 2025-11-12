package algorithms.Exercises.Exercise_1_4;

import java.util.Random;

/******************************************************************************
 * Exercise 1.4.32
 *
 * <p>
 * <i>Amortized analysis</i>. Prove that, starting from an empty stack, the
 * number of array accesses used by any sequence of 𝑀 operations in the
 * resizing array implementation of <code>Stack</code> is proportional to 𝑀.
 * </p>
 *
 * <p>
 * The proof is in the notes; this client measures it. A resizing array stack
 * with an access counter runs three workloads as 𝑀 doubles — pushes only,
 * stopping one past a power of two (the worst sequence), random pushes and
 * pops, and a push/pop alternation at the resize boundary — and prints the
 * accesses per operation for the book's policy (halve at one-quarter full)
 * beside a variant that halves at one-half full. The first should flatten
 * towards 5 and stay there on every workload; the second should grow without
 * bound on the alternation.
 * </p>
 *
 * <pre>
 * ./gradlew run -PmainClass=Exercises.Exercise_1_4.Exercise_1_4_32
 * ./gradlew run -PmainClass=Exercises.Exercise_1_4.Exercise_1_4_32 --args="16 65536"
 * ./gradlew test --tests "Exercise_1_4_32Test"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_32 {
    /**
     * The book's resizing array stack, counting every read or write of the backing
     * array. The shrink divisor is the fraction full at which a pop halves the
     * array: 4 for the book's policy, 2 for the variant that thrashes.
     */
    static class CountingResizingArrayStack<Item> {
        private static final int INIT_CAPACITY = 1;
        private final int shrinkDivisor;
        private Item[] array;
        private int size;
        long accesses;

        @SuppressWarnings("unchecked")
        CountingResizingArrayStack(int shrinkDivisor) {
            this.shrinkDivisor = shrinkDivisor;
            this.array = (Item[]) new Object[INIT_CAPACITY];
            this.size = 0;
        }

        boolean isEmpty() {
            return size == 0;
        }

        int size() {
            return size;
        }

        int capacity() {
            return array.length;
        }

        @SuppressWarnings("unchecked")
        private void resize(int max) {
            Item[] newArray = (Item[]) new Object[max];

            for (int i = 0; i < size; i++) {
                newArray[i] = array[i];
                accesses += 2; // one read, one write
            }

            array = newArray;
        }

        void push(Item item) {
            if (size == array.length)
                resize(2 * array.length);

            array[size++] = item;
            accesses++;
        }

        @SuppressWarnings("null")
        Item pop() {
            Item item = array[--size];
            accesses++;

            array[size] = null; // avoid loitering
            accesses++;

            if (size > 0 && size == array.length / shrinkDivisor)
                resize(array.length / 2);

            return item;
        }
    }

    /**
     * Pushes 𝑀 + 1 items from empty — one past a power of two, so the last push
     * doubles the array — and returns the accesses per operation.
     */
    private static double pushesOnly(int m, int shrinkDivisor) {
        CountingResizingArrayStack<Integer> stack = new CountingResizingArrayStack<>(shrinkDivisor);
        int operations = m + 1;

        for (int i = 0; i < operations; i++)
            stack.push(i);

        return (double) stack.accesses / operations;
    }

    /**
     * Performs 𝑀 operations from empty, each a push or (when the stack is
     * non-empty) a pop with equal probability, and returns the accesses per
     * operation.
     */
    private static double random(int m, int shrinkDivisor) {
        CountingResizingArrayStack<Integer> stack = new CountingResizingArrayStack<>(shrinkDivisor);
        Random random = new Random(32L); // fixed seed -> reproducible rows

        for (int i = 0; i < m; i++) {
            if (stack.isEmpty() || random.nextBoolean())
                stack.push(i);
            else
                stack.pop();
        }

        return (double) stack.accesses / m;
    }

    /**
     * Pushes 𝑀 / 2 + 1 items from empty, one past a power of two so that the array
     * has just doubled, then alternates pop and push 𝑀 / 2 times at that boundary.
     * Returns the accesses per operation over the whole sequence. Halving at
     * one-half full resizes on every one of the alternating operations; halving at
     * one-quarter full never resizes again.
     */
    private static double slosh(int m, int shrinkDivisor) {
        CountingResizingArrayStack<Integer> stack = new CountingResizingArrayStack<>(shrinkDivisor);
        int climb = m / 2 + 1;

        for (int i = 0; i < climb; i++)
            stack.push(i);

        for (int i = 0; i < m / 2; i++) {
            if (i % 2 == 0)
                stack.pop();
            else
                stack.push(i);
        }

        return (double) stack.accesses / (climb + m / 2);
    }

    public static void main(String[] args) {
        int smallest = args.length > 0 ? Integer.parseInt(args[0]) : 16;
        int largest = args.length > 1 ? Integer.parseInt(args[1]) : 1 << 16;

        System.out.printf("%12s %14s %14s %14s %14s %14s %14s%n", "M", "pushes 1/4", "pushes 1/2", "random 1/4",
                "random 1/2", "slosh 1/4", "slosh 1/2");

        for (int m = smallest; m <= largest; m *= 2) {
            System.out.printf("%12d %14.3f %14.3f %14.3f %14.3f %14.3f %14.3f%n", m, pushesOnly(m, 4), pushesOnly(m, 2),
                    random(m, 4), random(m, 2), slosh(m, 4), slosh(m, 2));
        }
    }
}
