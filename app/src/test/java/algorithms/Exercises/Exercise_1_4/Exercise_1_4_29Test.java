package algorithms.Exercises.Exercise_1_4;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import algorithms.Exercises.Exercise_1_4.Exercise_1_4_29.StequeWithTwoStacks;
import algorithms.Stack;

/******************************************************************************
 * Exercise_1_4_29Test
 *
 * <pre>
 * ./gradlew test --tests "Exercise_1_4_29Test"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_29Test {
    @Test
    public void followsTheTraceExample() {
        // enqueue 1, 2; push 3; pop twice; enqueue 4; push 5; pop three times.
        // The 4 arrives while the front-stack still holds 2 and must wait its
        // turn at the bottom.
        StequeWithTwoStacks<Integer> steque = new StequeWithTwoStacks<>();
        steque.enqueue(1);
        steque.enqueue(2);
        steque.push(3);

        assertEquals(3, steque.pop());
        assertEquals(1, steque.pop());
        steque.enqueue(4);
        steque.push(5);
        assertEquals(5, steque.pop());
        assertEquals(2, steque.pop());
        assertEquals(4, steque.pop());
        assertTrue(steque.isEmpty());
    }

    @Test
    public void startsEmpty() {
        StequeWithTwoStacks<String> steque = new StequeWithTwoStacks<>();

        assertTrue(steque.isEmpty());
        assertEquals(0, steque.size());
    }

    @Test
    public void throwsOnPopOfAnEmptySteque() {
        StequeWithTwoStacks<String> steque = new StequeWithTwoStacks<>();

        assertThrows(NoSuchElementException.class, steque::pop);
    }

    @Test
    public void throwsOnceDrained() {
        StequeWithTwoStacks<String> steque = new StequeWithTwoStacks<>();
        steque.enqueue("a");
        steque.pop();

        assertTrue(steque.isEmpty());
        assertThrows(NoSuchElementException.class, steque::pop);
    }

    @Test
    public void returnsASinglePushedElement() {
        StequeWithTwoStacks<String> steque = new StequeWithTwoStacks<>();
        steque.push("only");

        assertEquals(1, steque.size());
        assertEquals("only", steque.pop());
        assertTrue(steque.isEmpty());
    }

    @Test
    public void returnsASingleEnqueuedElement() {
        StequeWithTwoStacks<String> steque = new StequeWithTwoStacks<>();
        steque.enqueue("only");

        assertEquals(1, steque.size());
        assertEquals("only", steque.pop());
        assertTrue(steque.isEmpty());
    }

    @Test
    public void pushesToTheTopAndEnqueuesToTheBottom() {
        // Pushed elements come out newest first; an enqueued element waits
        // beneath all of them.
        StequeWithTwoStacks<Integer> steque = new StequeWithTwoStacks<>();
        steque.push(1);
        steque.push(2);
        steque.enqueue(3);

        assertEquals(2, steque.pop());
        assertEquals(1, steque.pop());
        assertEquals(3, steque.pop());
        assertTrue(steque.isEmpty());
    }

    @Test
    public void pushAfterAPourLandsOnTop() {
        // After a pour the front-stack holds poured elements; a push must still
        // go above them, not beneath.
        StequeWithTwoStacks<Integer> steque = new StequeWithTwoStacks<>();
        steque.enqueue(1);
        steque.enqueue(2);
        assertEquals(1, steque.pop());
        steque.push(9);

        assertEquals(9, steque.pop());
        assertEquals(2, steque.pop());
        assertTrue(steque.isEmpty());
    }

    @Test
    public void countsElementsAcrossBothStacks() {
        // After the first pop the remaining element lives on the front-stack
        // and the new one on the back-stack; size must add the two.
        StequeWithTwoStacks<Integer> steque = new StequeWithTwoStacks<>();
        steque.enqueue(1);
        steque.enqueue(2);
        steque.pop();
        steque.enqueue(3);
        steque.push(4);

        assertEquals(3, steque.size());
        assertFalse(steque.isEmpty());
    }

    @Test
    public void preservesOrderWhenFullyDrainedBetweenBatches() {
        // Two full cycles of fill-then-drain: the second pour must start from
        // an empty front-stack, not on top of leftovers.
        StequeWithTwoStacks<Integer> steque = new StequeWithTwoStacks<>();

        for (int batch = 0; batch < 2; batch++) {
            for (int i = 0; i < 5; i++)
                steque.enqueue(batch * 10 + i);
            for (int i = 0; i < 5; i++)
                assertEquals(batch * 10 + i, steque.pop());
        }

        assertTrue(steque.isEmpty());
    }

    @Test
    public void handlesALargeSteque() {
        int n = 100_000;
        StequeWithTwoStacks<Integer> steque = new StequeWithTwoStacks<>();

        for (int i = 0; i < n; i++)
            steque.enqueue(i);

        assertEquals(n, steque.size());

        for (int i = 0; i < n; i++)
            assertEquals(i, steque.pop());

        assertTrue(steque.isEmpty());
    }

    @Test
    public void agreesWithArrayDequeOnRandomInterleavings() {
        Random random = new Random(29L); // fixed seed -> reproducible failures

        for (int trial = 0; trial < 2000; trial++) {
            StequeWithTwoStacks<Integer> steque = new StequeWithTwoStacks<>();
            ArrayDeque<Integer> reference = new ArrayDeque<>();
            int steps = 1 + random.nextInt(500);
            double addBias = random.nextDouble(); // some runs pile up, some churn
            double enqueueShare = random.nextDouble(); // of the adds, how many enqueue
            int next = 0;

            for (int step = 0; step < steps; step++) {
                if (reference.isEmpty() || random.nextDouble() < addBias) {
                    if (random.nextDouble() < enqueueShare) {
                        steque.enqueue(next);
                        reference.addLast(next);
                    } else {
                        steque.push(next);
                        reference.addFirst(next);
                    }
                    next++;
                } else {
                    Integer expected = reference.removeFirst();
                    assertEquals(expected, steque.pop(), "trial " + trial + " step " + step);
                }

                assertEquals(reference.size(), steque.size(), "size at trial " + trial + " step " + step);
                assertEquals(reference.isEmpty(), steque.isEmpty(), "isEmpty at trial " + trial + " step " + step);
            }
        }
    }

    @Test
    public void aPushedElementCostsTwoStackOperations() {
        CountingStack<Integer> front = new CountingStack<>();
        CountingStack<Integer> back = new CountingStack<>();
        StequeWithTwoStacks<Integer> steque = new StequeWithTwoStacks<>(front, back);
        steque.push(1);
        steque.pop();

        assertEquals(2, front.operations + back.operations);
    }

    @Test
    public void anEnqueuedElementCostsFourStackOperations() {
        CountingStack<Integer> front = new CountingStack<>();
        CountingStack<Integer> back = new CountingStack<>();
        StequeWithTwoStacks<Integer> steque = new StequeWithTwoStacks<>(front, back);
        steque.enqueue(1);
        steque.pop();

        assertEquals(4, front.operations + back.operations);
    }

    @Test
    public void pouringCostsTwoStackOperationsPerElement() {
        // The write-up's trace: the first pop is served from the front-stack
        // for 1 operation; the second finds it empty and pours two elements
        // (2 pops + 2 pushes) before its own pop.
        CountingStack<Integer> front = new CountingStack<>();
        CountingStack<Integer> back = new CountingStack<>();
        StequeWithTwoStacks<Integer> steque = new StequeWithTwoStacks<>(front, back);
        steque.enqueue(1);
        steque.enqueue(2);
        steque.push(3);
        assertEquals(3, front.operations + back.operations);

        steque.pop();
        assertEquals(3 + 1, front.operations + back.operations);

        steque.pop();
        assertEquals(3 + 1 + 5, front.operations + back.operations);
    }

    @Test
    public void aSinglePopCanCostLinearTime() {
        // Amortized O(1) is not worst-case O(1): after n enqueues and no pops,
        // the next pop pays 2n + 1 stack operations at once.
        int n = 10_000;
        CountingStack<Integer> front = new CountingStack<>();
        CountingStack<Integer> back = new CountingStack<>();
        StequeWithTwoStacks<Integer> steque = new StequeWithTwoStacks<>(front, back);

        for (int i = 0; i < n; i++)
            steque.enqueue(i);

        long before = front.operations + back.operations;
        steque.pop();

        assertEquals(2L * n + 1, front.operations + back.operations - before);
    }

    @Test
    public void frequentPoursAreCheap() {
        // Alternating enqueue and pop pours on every pop, but each pour moves a
        // single element: exactly 4 stack operations per pair, 2 per steque
        // operation.
        int pairs = 1_000;
        CountingStack<Integer> front = new CountingStack<>();
        CountingStack<Integer> back = new CountingStack<>();
        StequeWithTwoStacks<Integer> steque = new StequeWithTwoStacks<>(front, back);

        for (int i = 0; i < pairs; i++) {
            steque.enqueue(i);
            steque.pop();
        }

        assertEquals(4L * pairs, front.operations + back.operations);
    }

    @Test
    public void neverExceedsFourStackOperationsPerStequeOperation() {
        // The amortized bound: over any sequence of M steque operations the two
        // stacks together perform at most 4M pushes and pops, whatever the
        // interleaving of push, enqueue and pop.
        Random random = new Random(29L); // fixed seed -> reproducible failures

        for (int trial = 0; trial < 2000; trial++) {
            CountingStack<Integer> front = new CountingStack<>();
            CountingStack<Integer> back = new CountingStack<>();
            StequeWithTwoStacks<Integer> steque = new StequeWithTwoStacks<>(front, back);
            int steps = 1 + random.nextInt(500);
            double addBias = random.nextDouble();
            double enqueueShare = random.nextDouble();
            long stequeOperations = 0;

            for (int step = 0; step < steps; step++) {
                if (steque.isEmpty() || random.nextDouble() < addBias) {
                    if (random.nextDouble() < enqueueShare)
                        steque.enqueue(step);
                    else
                        steque.push(step);
                } else {
                    steque.pop();
                }

                stequeOperations++;
            }

            long stackOperations = front.operations + back.operations;
            assertTrue(stackOperations <= 4 * stequeOperations,
                    "trial " + trial + ": " + stackOperations + " stack operations for " + stequeOperations
                            + " steque operations");
        }
    }

    /**
     * A Stack that counts its own pushes and pops, so a test can see how many
     * stack operations a sequence of steque operations really cost.
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
