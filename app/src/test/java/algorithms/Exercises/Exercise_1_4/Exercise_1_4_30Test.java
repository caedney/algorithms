package algorithms.Exercises.Exercise_1_4;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import algorithms.Exercises.Exercise_1_4.Exercise_1_4_30.DequeWithAStackAndASteque;
import algorithms.Stack;
import algorithms.Steque;

/******************************************************************************
 * Exercise_1_4_30Test
 *
 * <pre>
 * ./gradlew test --tests "Exercise_1_4_30Test"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_30Test {
    @Test
    public void followsTheEasyCaseTrace() {
        // The write-up's first trace: while both sides are non-empty every
        // deque operation is a single primitive on one of them.
        DequeWithAStackAndASteque<Integer> deque = new DequeWithAStackAndASteque<>();
        deque.pushLeft(1);
        deque.pushLeft(2);
        deque.pushRight(3);
        deque.pushRight(4);

        assertEquals(4, deque.size());
        assertEquals(2, deque.popLeft());
        assertEquals(4, deque.popRight());
        assertEquals(1, deque.popLeft());
        assertEquals(3, deque.popRight());
        assertTrue(deque.isEmpty());
    }

    @Test
    public void startsEmpty() {
        DequeWithAStackAndASteque<String> deque = new DequeWithAStackAndASteque<>();

        assertTrue(deque.isEmpty());
        assertEquals(0, deque.size());
    }

    @Test
    public void throwsOnPopLeftOfAnEmptyDeque() {
        DequeWithAStackAndASteque<String> deque = new DequeWithAStackAndASteque<>();

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, deque::popLeft);
        assertEquals("Deque underflow", thrown.getMessage());
    }

    @Test
    public void throwsOnPopRightOfAnEmptyDeque() {
        DequeWithAStackAndASteque<String> deque = new DequeWithAStackAndASteque<>();

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, deque::popRight);
        assertEquals("Deque underflow", thrown.getMessage());
    }

    @Test
    public void throwsOnceDrained() {
        DequeWithAStackAndASteque<String> deque = new DequeWithAStackAndASteque<>();
        deque.pushLeft("a");
        deque.popRight();

        assertTrue(deque.isEmpty());
        assertThrows(NoSuchElementException.class, deque::popLeft);
        assertThrows(NoSuchElementException.class, deque::popRight);
    }

    @Test
    public void popsASingleElementFromEitherEnd() {
        // A split of one element moves it rather than nothing (a draft with a
        // size > 1 guard inside split left the stack empty and popLeft threw).
        DequeWithAStackAndASteque<String> left = new DequeWithAStackAndASteque<>();
        left.pushRight("only");
        assertEquals("only", left.popLeft());
        assertTrue(left.isEmpty());

        // After a pour a lone element is already on the steque, so popRight
        // must not split it back across (a draft without the guard in
        // popRight did, and the steque was empty when it came to pop).
        DequeWithAStackAndASteque<String> right = new DequeWithAStackAndASteque<>();
        right.pushLeft("only");
        assertEquals("only", right.popRight());
        assertTrue(right.isEmpty());
    }

    @Test
    public void popLeftReachesTheBottomOfTheSteque() {
        // Everything pushed on the right sits in the steque; the leftmost
        // element is at its bottom and needs the split to reach it.
        DequeWithAStackAndASteque<Integer> deque = new DequeWithAStackAndASteque<>();
        for (int i = 1; i <= 6; i++)
            deque.pushRight(i);

        for (int i = 1; i <= 6; i++)
            assertEquals(i, deque.popLeft());
        assertTrue(deque.isEmpty());
    }

    @Test
    public void popRightReachesTheBottomOfTheStack() {
        DequeWithAStackAndASteque<Integer> deque = new DequeWithAStackAndASteque<>();
        for (int i = 6; i >= 1; i--)
            deque.pushLeft(i);

        for (int i = 6; i >= 1; i--)
            assertEquals(i, deque.popRight());
        assertTrue(deque.isEmpty());
    }

    @Test
    public void alternatingEndsKeepsTheOrder() {
        // The sequence that defeats a pour-everything policy: it must still
        // return the right values, and the ratio test below bounds its cost.
        DequeWithAStackAndASteque<Integer> deque = new DequeWithAStackAndASteque<>();
        for (int i = 1; i <= 100; i++)
            deque.pushRight(i);

        int left = 1, right = 100;
        while (!deque.isEmpty()) {
            assertEquals(left++, deque.popLeft());
            if (!deque.isEmpty())
                assertEquals(right--, deque.popRight());
        }
    }

    @Test
    public void interleavesPushesAndPops() {
        DequeWithAStackAndASteque<Integer> deque = new DequeWithAStackAndASteque<>();
        deque.pushLeft(2);
        deque.pushLeft(1);
        deque.pushRight(3);
        assertEquals(3, deque.popRight());
        deque.pushRight(4);
        assertEquals(1, deque.popLeft());
        deque.pushLeft(0);

        assertEquals(3, deque.size());
        assertFalse(deque.isEmpty());
        assertEquals(0, deque.popLeft());
        assertEquals(4, deque.popRight());
        assertEquals(2, deque.popRight());
        assertTrue(deque.isEmpty());
    }

    @Test
    public void handlesALargeDeque() {
        int n = 100_000;
        DequeWithAStackAndASteque<Integer> deque = new DequeWithAStackAndASteque<>();

        for (int i = 0; i < n; i++)
            deque.pushLeft(i);

        assertEquals(n, deque.size());

        for (int i = 0; i < n; i++)
            assertEquals(i, deque.popRight());

        assertTrue(deque.isEmpty());
    }

    @Test
    public void agreesWithArrayDequeOnRandomInterleavings() {
        Random random = new Random(30L); // fixed seed -> reproducible failures

        for (int trial = 0; trial < 2000; trial++) {
            DequeWithAStackAndASteque<Integer> deque = new DequeWithAStackAndASteque<>();
            ArrayDeque<Integer> reference = new ArrayDeque<>();
            int steps = 1 + random.nextInt(500);
            double pushBias = random.nextDouble(); // some runs pile up, some churn
            int next = 0;

            for (int step = 0; step < steps; step++) {
                int op = random.nextInt(4);
                if (reference.isEmpty() || random.nextDouble() < pushBias)
                    op = random.nextInt(2);

                switch (op) {
                    case 0 -> {
                        deque.pushLeft(next);
                        reference.addFirst(next);
                        next++;
                    }
                    case 1 -> {
                        deque.pushRight(next);
                        reference.addLast(next);
                        next++;
                    }
                    case 2 -> assertEquals(reference.pollFirst(), deque.popLeft(), "popLeft at trial " + trial + " step " + step);
                    default -> assertEquals(reference.pollLast(), deque.popRight(), "popRight at trial " + trial + " step " + step);
                }

                assertEquals(reference.size(), deque.size(), "size at trial " + trial + " step " + step);
                assertEquals(reference.isEmpty(), deque.isEmpty(), "isEmpty at trial " + trial + " step " + step);
            }
        }
    }

    @Test
    public void easyCaseOperationsCostOnePrimitiveEach() {
        CountingStack<Integer> stack = new CountingStack<>();
        CountingSteque<Integer> steque = new CountingSteque<>();
        DequeWithAStackAndASteque<Integer> deque = new DequeWithAStackAndASteque<>(stack, steque);
        deque.pushLeft(1);
        deque.pushLeft(2);
        deque.pushRight(3);
        deque.pushRight(4);
        deque.popLeft();
        deque.popRight();

        assertEquals(6, stack.operations + steque.operations);
    }

    @Test
    public void splittingSixElementsCostsTwelvePrimitives() {
        // The write-up's popLeft trace: three rotations (pop, enqueue) and
        // three moves (pop, push), then the pop itself.
        CountingStack<Integer> stack = new CountingStack<>();
        CountingSteque<Integer> steque = new CountingSteque<>();
        DequeWithAStackAndASteque<Integer> deque = new DequeWithAStackAndASteque<>(stack, steque);
        for (int i = 1; i <= 6; i++)
            deque.pushRight(i);

        long before = stack.operations + steque.operations;
        assertEquals(1, deque.popLeft());
        assertEquals(2 * 3 + 2 * 3 + 1, stack.operations + steque.operations - before);
    }

    @Test
    public void pouringAndSplittingSixElementsCostsTwentyFourPrimitives() {
        // The write-up's popRight trace: six pours (pop, push), then the same
        // split, then the pop itself.
        CountingStack<Integer> stack = new CountingStack<>();
        CountingSteque<Integer> steque = new CountingSteque<>();
        DequeWithAStackAndASteque<Integer> deque = new DequeWithAStackAndASteque<>(stack, steque);
        for (int i = 6; i >= 1; i--)
            deque.pushLeft(i);

        long before = stack.operations + steque.operations;
        assertEquals(6, deque.popRight());
        assertEquals(2 * 6 + 2 * 3 + 2 * 3 + 1, stack.operations + steque.operations - before);
    }

    @Test
    public void followsTheWorstSequenceTrace() {
        // The write-up's n = 8 table: eight pushes on the left, then pops from
        // the right. Each rebalance handles half of what the last one left.
        CountingStack<Integer> stack = new CountingStack<>();
        CountingSteque<Integer> steque = new CountingSteque<>();
        DequeWithAStackAndASteque<Integer> deque = new DequeWithAStackAndASteque<>(stack, steque);
        for (int i = 1; i <= 8; i++)
            deque.pushLeft(i);
        assertEquals(8, stack.operations + steque.operations);

        long[] expected = { 33, 1, 1, 1, 17, 1, 9, 3 };
        for (long cost : expected) {
            long before = stack.operations + steque.operations;
            deque.popRight();
            assertEquals(cost, stack.operations + steque.operations - before);
        }

        assertEquals(74, stack.operations + steque.operations);
        assertTrue(deque.isEmpty());
    }

    @Test
    public void worstSequenceStaysUnderFivePrimitivesPerOperation() {
        // n pushes on one end then n pops from the other: the rebalances sum
        // to just under 8n, so the ratio approaches 5 from below as n grows.
        for (int n : new int[] { 8, 64, 1_024, 65_536 }) {
            CountingStack<Integer> stack = new CountingStack<>();
            CountingSteque<Integer> steque = new CountingSteque<>();
            DequeWithAStackAndASteque<Integer> deque = new DequeWithAStackAndASteque<>(stack, steque);

            for (int i = 0; i < n; i++)
                deque.pushLeft(i);
            for (int i = 0; i < n; i++)
                deque.popRight();

            long primitives = stack.operations + steque.operations;
            assertTrue(primitives < 5L * 2 * n, "n = " + n + ": " + primitives + " primitives for " + 2 * n + " operations");
            assertTrue(primitives > 4L * 2 * n, "n = " + n + ": " + primitives + " primitives for " + 2 * n + " operations");
        }
    }

    @Test
    public void randomInterleavingsStayUnderFivePrimitivesPerOperation() {
        Random random = new Random(30L); // fixed seed -> reproducible failures

        for (int trial = 0; trial < 2000; trial++) {
            CountingStack<Integer> stack = new CountingStack<>();
            CountingSteque<Integer> steque = new CountingSteque<>();
            DequeWithAStackAndASteque<Integer> deque = new DequeWithAStackAndASteque<>(stack, steque);
            int steps = 1 + random.nextInt(500);
            double pushBias = random.nextDouble();
            long dequeOperations = 0;

            for (int step = 0; step < steps; step++) {
                int op = random.nextInt(4);
                if (deque.isEmpty() || random.nextDouble() < pushBias)
                    op = random.nextInt(2);

                switch (op) {
                    case 0 -> deque.pushLeft(step);
                    case 1 -> deque.pushRight(step);
                    case 2 -> deque.popLeft();
                    default -> deque.popRight();
                }
                dequeOperations++;
            }

            long primitives = stack.operations + steque.operations;
            assertTrue(primitives <= 5 * dequeOperations, "trial " + trial + ": " + primitives + " primitives for " + dequeOperations + " operations");
        }
    }

    /**
     * A Stack that counts its own pushes and pops, so a test can see how many
     * primitives a sequence of deque operations really cost.
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

    /**
     * A Steque that counts its own pushes, pops and enqueues.
     */
    private static class CountingSteque<Item> extends Steque<Item> {
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

        @Override
        public void enqueue(Item item) {
            operations++;
            super.enqueue(item);
        }
    }
}
