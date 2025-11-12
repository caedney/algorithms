package algorithms.Exercises.Exercise_1_4;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import algorithms.Exercises.Exercise_1_4.Exercise_1_4_27.QueueWithTwoStacks;
import algorithms.Stack;

/******************************************************************************
 * Exercise_1_4_27Test
 *
 * <pre>
 * ./gradlew test --tests "Exercise_1_4_27Test"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_27Test {
    @Test
    public void followsTheTraceExample() {
        // enqueue 1, 2, 3; dequeue; enqueue 4; dequeue three times. The 4
        // arrives while the out-stack still holds 2 and 3 and must wait its turn.
        QueueWithTwoStacks<Integer> queue = new QueueWithTwoStacks<>();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        assertEquals(1, queue.dequeue());
        queue.enqueue(4);
        assertEquals(2, queue.dequeue());
        assertEquals(3, queue.dequeue());
        assertEquals(4, queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void startsEmpty() {
        QueueWithTwoStacks<String> queue = new QueueWithTwoStacks<>();

        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }

    @Test
    public void throwsOnDequeueOfAnEmptyQueue() {
        QueueWithTwoStacks<String> queue = new QueueWithTwoStacks<>();

        assertThrows(NoSuchElementException.class, queue::dequeue);
    }

    @Test
    public void throwsOnceDrained() {
        QueueWithTwoStacks<String> queue = new QueueWithTwoStacks<>();
        queue.enqueue("a");
        queue.dequeue();

        assertTrue(queue.isEmpty());
        assertThrows(NoSuchElementException.class, queue::dequeue);
    }

    @Test
    public void returnsASingleElement() {
        QueueWithTwoStacks<String> queue = new QueueWithTwoStacks<>();
        queue.enqueue("only");

        assertEquals(1, queue.size());
        assertEquals("only", queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void countsElementsAcrossBothStacks() {
        // After the first dequeue the remaining elements live on the out-stack
        // and the new one on the in-stack; size must add the two.
        QueueWithTwoStacks<Integer> queue = new QueueWithTwoStacks<>();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.dequeue();
        queue.enqueue(4);

        assertEquals(3, queue.size());
        assertFalse(queue.isEmpty());
    }

    @Test
    public void preservesOrderWhenFullyDrainedBetweenBatches() {
        // Two full cycles of fill-then-drain: the second pour must start from
        // an empty out-stack, not on top of leftovers.
        QueueWithTwoStacks<Integer> queue = new QueueWithTwoStacks<>();

        for (int batch = 0; batch < 2; batch++) {
            for (int i = 0; i < 5; i++)
                queue.enqueue(batch * 10 + i);
            for (int i = 0; i < 5; i++)
                assertEquals(batch * 10 + i, queue.dequeue());
        }

        assertTrue(queue.isEmpty());
    }

    @Test
    public void handlesALargeQueue() {
        int n = 100_000;
        QueueWithTwoStacks<Integer> queue = new QueueWithTwoStacks<>();

        for (int i = 0; i < n; i++)
            queue.enqueue(i);

        assertEquals(n, queue.size());

        for (int i = 0; i < n; i++)
            assertEquals(i, queue.dequeue());

        assertTrue(queue.isEmpty());
    }

    @Test
    public void agreesWithArrayDequeOnRandomInterleavings() {
        Random random = new Random(27L); // fixed seed -> reproducible failures

        for (int trial = 0; trial < 2000; trial++) {
            QueueWithTwoStacks<Integer> queue = new QueueWithTwoStacks<>();
            ArrayDeque<Integer> reference = new ArrayDeque<>();
            int steps = 1 + random.nextInt(500);
            double enqueueBias = random.nextDouble(); // some runs pile up, some churn
            int next = 0;

            for (int step = 0; step < steps; step++) {
                if (reference.isEmpty() || random.nextDouble() < enqueueBias) {
                    queue.enqueue(next);
                    reference.addLast(next);
                    next++;
                } else {
                    Integer expected = reference.pollFirst();
                    assertEquals(expected, queue.dequeue(), "trial " + trial + " step " + step);
                }

                assertEquals(reference.size(), queue.size(), "size at trial " + trial + " step " + step);
                assertEquals(reference.isEmpty(), queue.isEmpty(), "isEmpty at trial " + trial + " step " + step);
            }
        }
    }

    @Test
    public void pouringCostsTwoStackOperationsPerElement() {
        // The write-up's trace: the first dequeue finds the out-stack empty and
        // pours three elements (3 pops + 3 pushes) before its own pop.
        CountingStack<Integer> in = new CountingStack<>();
        CountingStack<Integer> out = new CountingStack<>();
        QueueWithTwoStacks<Integer> queue = new QueueWithTwoStacks<>(in, out);
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        assertEquals(3, in.operations + out.operations);

        queue.dequeue();
        assertEquals(3 + 7, in.operations + out.operations);
    }

    @Test
    public void aSingleDequeueCanCostLinearTime() {
        // Amortized O(1) is not worst-case O(1): after n enqueues and no
        // dequeues, the next dequeue pays 2n + 1 stack operations at once.
        int n = 10_000;
        CountingStack<Integer> in = new CountingStack<>();
        CountingStack<Integer> out = new CountingStack<>();
        QueueWithTwoStacks<Integer> queue = new QueueWithTwoStacks<>(in, out);

        for (int i = 0; i < n; i++)
            queue.enqueue(i);

        long before = in.operations + out.operations;
        queue.dequeue();

        assertEquals(2L * n + 1, in.operations + out.operations - before);
    }

    @Test
    public void neverExceedsFourStackOperationsPerQueueOperation() {
        // The amortized bound: over any sequence of M queue operations the two
        // stacks together perform at most 4M pushes and pops, whatever the
        // interleaving.
        Random random = new Random(27L); // fixed seed -> reproducible failures

        for (int trial = 0; trial < 2000; trial++) {
            CountingStack<Integer> in = new CountingStack<>();
            CountingStack<Integer> out = new CountingStack<>();
            QueueWithTwoStacks<Integer> queue = new QueueWithTwoStacks<>(in, out);
            int steps = 1 + random.nextInt(500);
            double enqueueBias = random.nextDouble();
            long queueOperations = 0;

            for (int step = 0; step < steps; step++) {
                if (queue.isEmpty() || random.nextDouble() < enqueueBias)
                    queue.enqueue(step);
                else
                    queue.dequeue();

                queueOperations++;
            }

            long stackOperations = in.operations + out.operations;
            assertTrue(stackOperations <= 4 * queueOperations,
                    "trial " + trial + ": " + stackOperations + " stack operations for " + queueOperations
                            + " queue operations");
        }
    }

    /**
     * A Stack that counts its own pushes and pops, so a test can see how many
     * stack operations a sequence of queue operations really cost.
     */
    private static class CountingStack<Item> extends Stack<Item> {
        long operations;

        @Override
        public void push(Item item) {
            operations++;
            super.push(item);
        }

        @Override
        public Item pop() {
            operations++;
            return super.pop();
        }
    }
}
