package algorithms.Exercises.Exercise_1_4;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import algorithms.Exercises.Exercise_1_4.Exercise_1_4_28.StackWithAQueue;
import algorithms.Queue;

/******************************************************************************
 * Exercise_1_4_28Test
 *
 * <pre>
 * ./gradlew test --tests "Exercise_1_4_28Test"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_28Test {
    @Test
    public void followsTheTraceExample() {
        // push 1, 2, 3, 4: each push rotates the older elements behind the new
        // one, so the queue reads 4 3 2 1 and pops come out newest first.
        StackWithAQueue<Integer> stack = new StackWithAQueue<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);

        assertEquals("4 3 2 1 ", stack.toString());
        assertEquals(4, stack.pop());
        assertEquals(3, stack.pop());
        assertEquals(2, stack.pop());
        assertEquals(1, stack.pop());
        assertTrue(stack.isEmpty());
    }

    @Test
    public void startsEmpty() {
        StackWithAQueue<String> stack = new StackWithAQueue<>();

        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
    }

    @Test
    public void throwsOnPopOfAnEmptyStack() {
        StackWithAQueue<String> stack = new StackWithAQueue<>();

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, stack::pop);
        assertEquals("Stack underflow", thrown.getMessage());
    }

    @Test
    public void throwsOnPeekOfAnEmptyStack() {
        StackWithAQueue<String> stack = new StackWithAQueue<>();

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, stack::peek);
        assertEquals("Stack underflow", thrown.getMessage());
    }

    @Test
    public void throwsOnceDrained() {
        StackWithAQueue<String> stack = new StackWithAQueue<>();
        stack.push("a");
        stack.pop();

        assertTrue(stack.isEmpty());
        assertThrows(NoSuchElementException.class, stack::pop);
    }

    @Test
    public void returnsASingleElement() {
        StackWithAQueue<String> stack = new StackWithAQueue<>();
        stack.push("only");

        assertEquals(1, stack.size());
        assertEquals("only", stack.peek());
        assertEquals("only", stack.pop());
        assertTrue(stack.isEmpty());
    }

    @Test
    public void peekDoesNotRemove() {
        StackWithAQueue<Integer> stack = new StackWithAQueue<>();
        stack.push(1);
        stack.push(2);

        assertEquals(2, stack.peek());
        assertEquals(2, stack.peek());
        assertEquals(2, stack.size());
        assertEquals(2, stack.pop());
        assertEquals(1, stack.peek());
    }

    @Test
    public void interleavesPushesAndPops() {
        // A push after some pops must land on top of what is left, not
        // underneath it.
        StackWithAQueue<Integer> stack = new StackWithAQueue<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.pop());
        stack.push(4);

        assertEquals(3, stack.size());
        assertFalse(stack.isEmpty());
        assertEquals(4, stack.pop());
        assertEquals(2, stack.pop());
        assertEquals(1, stack.pop());
    }

    @Test
    public void handlesALargeStack() {
        int n = 5_000; // quadratic: 5,000 pushes cost about 25 million queue operations
        StackWithAQueue<Integer> stack = new StackWithAQueue<>();

        for (int i = 0; i < n; i++)
            stack.push(i);

        assertEquals(n, stack.size());

        for (int i = n - 1; i >= 0; i--)
            assertEquals(i, stack.pop());

        assertTrue(stack.isEmpty());
    }

    @Test
    public void agreesWithArrayDequeOnRandomInterleavings() {
        Random random = new Random(28L); // fixed seed -> reproducible failures

        for (int trial = 0; trial < 2000; trial++) {
            StackWithAQueue<Integer> stack = new StackWithAQueue<>();
            ArrayDeque<Integer> reference = new ArrayDeque<>();
            int steps = 1 + random.nextInt(500);
            double pushBias = random.nextDouble(); // some runs pile up, some churn
            int next = 0;

            for (int step = 0; step < steps; step++) {
                if (reference.isEmpty() || random.nextDouble() < pushBias) {
                    stack.push(next);
                    reference.push(next);
                    next++;
                } else if (random.nextBoolean()) {
                    assertEquals(reference.peek(), stack.peek(), "peek at trial " + trial + " step " + step);
                } else {
                    Integer expected = reference.pop();
                    assertEquals(expected, stack.pop(), "trial " + trial + " step " + step);
                }

                assertEquals(reference.size(), stack.size(), "size at trial " + trial + " step " + step);
                assertEquals(reference.isEmpty(), stack.isEmpty(), "isEmpty at trial " + trial + " step " + step);
            }
        }
    }

    @Test
    public void pushingCostsTwoQueueOperationsPerElementAlreadyPresent() {
        // The write-up's trace: push(4) onto 3 2 1 is one enqueue plus three
        // rotations of two queue operations each.
        CountingQueue<Integer> queue = new CountingQueue<>();
        StackWithAQueue<Integer> stack = new StackWithAQueue<>(queue);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(1 + 3 + 5, queue.operations);

        stack.push(4);
        assertEquals(1 + 3 + 5 + 7, queue.operations);
    }

    @Test
    public void popAndPeekCostOneQueueOperation() {
        CountingQueue<Integer> queue = new CountingQueue<>();
        StackWithAQueue<Integer> stack = new StackWithAQueue<>(queue);
        for (int i = 0; i < 100; i++)
            stack.push(i);

        long before = queue.operations;
        stack.peek();
        assertEquals(1, queue.operations - before);
        stack.pop();
        assertEquals(2, queue.operations - before);
    }

    @Test
    public void nPushesCostNSquaredQueueOperations() {
        // Each push onto n elements costs 2n + 1 queue operations; summed over
        // n = 0 .. N-1 that is exactly N squared.
        int n = 1_000;
        CountingQueue<Integer> queue = new CountingQueue<>();
        StackWithAQueue<Integer> stack = new StackWithAQueue<>(queue);

        for (int i = 0; i < n; i++)
            stack.push(i);

        assertEquals((long) n * n, queue.operations);
    }

    /**
     * A Queue that counts its own enqueues, dequeues and peeks, so a test can
     * see how many queue operations a sequence of stack operations really cost.
     */
    private static class CountingQueue<Item> extends Queue<Item> {
        long operations;

        @Override
        public void enqueue(Item item) {
            operations++;
            super.enqueue(item);
        }

        @Override
        public Item dequeue() {
            operations++;
            return super.dequeue();
        }

        @Override
        public Item peek() {
            operations++;
            return super.peek();
        }
    }
}
