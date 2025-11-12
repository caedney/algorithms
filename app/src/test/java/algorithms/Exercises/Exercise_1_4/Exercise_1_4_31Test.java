package algorithms.Exercises.Exercise_1_4;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import algorithms.Exercises.Exercise_1_4.Exercise_1_4_31.DequeWithThreeStacks;
import algorithms.Stack;

/******************************************************************************
 * Exercise_1_4_31Test
 *
 * <pre>
 * ./gradlew test --tests "Exercise_1_4_31Test"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_31Test {
    @Test
    public void followsTheEasyCase() {
        // While both outward stacks are non-empty every deque operation is a
        // single primitive on one of them.
        DequeWithThreeStacks<Integer> deque = new DequeWithThreeStacks<>();
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
        DequeWithThreeStacks<String> deque = new DequeWithThreeStacks<>();

        assertTrue(deque.isEmpty());
        assertEquals(0, deque.size());
    }

    @Test
    public void throwsOnPopLeftOfAnEmptyDeque() {
        DequeWithThreeStacks<String> deque = new DequeWithThreeStacks<>();

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, deque::popLeft);
        assertEquals("Deque underflow", thrown.getMessage());
    }

    @Test
    public void throwsOnPopRightOfAnEmptyDeque() {
        DequeWithThreeStacks<String> deque = new DequeWithThreeStacks<>();

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, deque::popRight);
        assertEquals("Deque underflow", thrown.getMessage());
    }

    @Test
    public void throwsOnceDrained() {
        DequeWithThreeStacks<String> deque = new DequeWithThreeStacks<>();
        deque.pushLeft("a");
        deque.popRight();

        assertTrue(deque.isEmpty());
        assertThrows(NoSuchElementException.class, deque::popLeft);
        assertThrows(NoSuchElementException.class, deque::popRight);
    }

    @Test
    public void popsASingleElementFromEitherEnd() {
        // A rebalance of one element is a single pour across. Without the
        // guard the split would compute a top half of zero elements, pour
        // the lone element straight back where it came from, and the pop
        // on the other side would throw.
        DequeWithThreeStacks<String> left = new DequeWithThreeStacks<>();
        left.pushRight("only");
        assertEquals("only", left.popLeft());
        assertTrue(left.isEmpty());

        DequeWithThreeStacks<String> right = new DequeWithThreeStacks<>();
        right.pushLeft("only");
        assertEquals("only", right.popRight());
        assertTrue(right.isEmpty());
    }

    @Test
    public void popLeftReachesTheBottomOfTheRightStack() {
        // Everything pushed on the right sits in the right stack; the
        // leftmost element is at its bottom and needs the rebalance to reach
        // it, facing the right way.
        DequeWithThreeStacks<Integer> deque = new DequeWithThreeStacks<>();
        for (int i = 1; i <= 6; i++)
            deque.pushRight(i);

        for (int i = 1; i <= 6; i++)
            assertEquals(i, deque.popLeft());
        assertTrue(deque.isEmpty());
    }

    @Test
    public void popRightReachesTheBottomOfTheLeftStack() {
        DequeWithThreeStacks<Integer> deque = new DequeWithThreeStacks<>();
        for (int i = 6; i >= 1; i--)
            deque.pushLeft(i);

        for (int i = 6; i >= 1; i--)
            assertEquals(i, deque.popRight());
        assertTrue(deque.isEmpty());
    }

    @Test
    public void rebalanceLeavesBothHalvesFacingTheRightWay() {
        // The write-up's trace, continued: after the rebalance the left stack
        // holds 3 2 1 and the right stack 4 5 6 (bottom to top), so the next
        // pops from either end return the deque's ends.
        DequeWithThreeStacks<Integer> deque = new DequeWithThreeStacks<>();
        for (int i = 1; i <= 6; i++)
            deque.pushRight(i);

        assertEquals(1, deque.popLeft());
        assertEquals(6, deque.popRight());
        assertEquals(2, deque.popLeft());
        assertEquals(5, deque.popRight());
        assertEquals(3, deque.popLeft());
        assertEquals(4, deque.popRight());
        assertTrue(deque.isEmpty());
    }

    @Test
    public void alternatingEndsKeepsTheOrder() {
        // The sequence that defeats a pour-everything policy: it must still
        // return the right values, and the ratio test below bounds its cost.
        DequeWithThreeStacks<Integer> deque = new DequeWithThreeStacks<>();
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
        DequeWithThreeStacks<Integer> deque = new DequeWithThreeStacks<>();
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
        DequeWithThreeStacks<Integer> deque = new DequeWithThreeStacks<>();

        for (int i = 0; i < n; i++)
            deque.pushLeft(i);

        assertEquals(n, deque.size());

        for (int i = 0; i < n; i++)
            assertEquals(i, deque.popRight());

        assertTrue(deque.isEmpty());
    }

    @Test
    public void agreesWithArrayDequeOnRandomInterleavings() {
        Random random = new Random(31L); // fixed seed -> reproducible failures

        for (int trial = 0; trial < 2000; trial++) {
            CountingStack<Integer> scratch = new CountingStack<>();
            DequeWithThreeStacks<Integer> deque = new DequeWithThreeStacks<>(new CountingStack<>(), scratch, new CountingStack<>());
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
                // The representation invariant: scratch is only ever used
                // inside a rebalance.
                assertTrue(scratch.isEmpty(), "scratch not empty at trial " + trial + " step " + step);
            }
        }
    }

    @Test
    public void easyCaseOperationsCostOnePrimitiveEach() {
        CountingStack<Integer> left = new CountingStack<>();
        CountingStack<Integer> scratch = new CountingStack<>();
        CountingStack<Integer> right = new CountingStack<>();
        DequeWithThreeStacks<Integer> deque = new DequeWithThreeStacks<>(left, scratch, right);
        deque.pushLeft(1);
        deque.pushLeft(2);
        deque.pushRight(3);
        deque.pushRight(4);
        deque.popLeft();
        deque.popRight();

        assertEquals(6, left.operations + scratch.operations + right.operations);
        assertEquals(0, scratch.operations);
    }

    @Test
    public void rebalancingSixElementsCostsThirtyPrimitives() {
        // The write-up's popLeft trace: six elements through the first pour,
        // three through a second, three through a third (pop, push each),
        // then the pop itself.
        CountingStack<Integer> left = new CountingStack<>();
        CountingStack<Integer> scratch = new CountingStack<>();
        CountingStack<Integer> right = new CountingStack<>();
        DequeWithThreeStacks<Integer> deque = new DequeWithThreeStacks<>(left, scratch, right);
        for (int i = 1; i <= 6; i++)
            deque.pushRight(i);

        long before = left.operations + scratch.operations + right.operations;
        assertEquals(1, deque.popLeft());
        assertEquals(2 * 6 + 2 * 3 + 2 * 3 + 2 * 3 + 1, left.operations + scratch.operations + right.operations - before);
    }

    @Test
    public void rebalancingIsSymmetric() {
        // The mirror image: six elements on the left, popRight pays the same.
        CountingStack<Integer> left = new CountingStack<>();
        CountingStack<Integer> scratch = new CountingStack<>();
        CountingStack<Integer> right = new CountingStack<>();
        DequeWithThreeStacks<Integer> deque = new DequeWithThreeStacks<>(left, scratch, right);
        for (int i = 6; i >= 1; i--)
            deque.pushLeft(i);

        long before = left.operations + scratch.operations + right.operations;
        assertEquals(6, deque.popRight());
        assertEquals(5 * 6 + 1, left.operations + scratch.operations + right.operations - before);
    }

    @Test
    public void rebalancingOneElementCostsTwoPrimitives() {
        // The guard: a lone element is poured straight across rather than
        // sent through the split.
        CountingStack<Integer> left = new CountingStack<>();
        CountingStack<Integer> scratch = new CountingStack<>();
        CountingStack<Integer> right = new CountingStack<>();
        DequeWithThreeStacks<Integer> deque = new DequeWithThreeStacks<>(left, scratch, right);
        deque.pushRight(1);

        long before = left.operations + scratch.operations + right.operations;
        assertEquals(1, deque.popLeft());
        assertEquals(2 + 1, left.operations + scratch.operations + right.operations - before);
        assertEquals(0, scratch.operations);
    }

    @Test
    public void followsTheWorstSequenceTrace() {
        // Eight pushes on the left, then pops from the right. Each rebalance
        // handles half of what the last one left: 8, then 4, then 2, then 1.
        CountingStack<Integer> left = new CountingStack<>();
        CountingStack<Integer> scratch = new CountingStack<>();
        CountingStack<Integer> right = new CountingStack<>();
        DequeWithThreeStacks<Integer> deque = new DequeWithThreeStacks<>(left, scratch, right);
        for (int i = 1; i <= 8; i++)
            deque.pushLeft(i);
        assertEquals(8, left.operations + scratch.operations + right.operations);

        long[] expected = { 41, 1, 1, 1, 21, 1, 11, 3 };
        for (long cost : expected) {
            long before = left.operations + scratch.operations + right.operations;
            deque.popRight();
            assertEquals(cost, left.operations + scratch.operations + right.operations - before);
        }

        assertEquals(88, left.operations + scratch.operations + right.operations);
        assertTrue(deque.isEmpty());
    }

    @Test
    public void worstSequenceStaysUnderSixPrimitivesPerOperation() {
        // n pushes on one end then n pops from the other: the rebalances sum
        // to just under 10n, so the ratio approaches 6 from below as n grows.
        for (int n : new int[] { 8, 64, 1_024, 65_536 }) {
            CountingStack<Integer> left = new CountingStack<>();
            CountingStack<Integer> scratch = new CountingStack<>();
            CountingStack<Integer> right = new CountingStack<>();
            DequeWithThreeStacks<Integer> deque = new DequeWithThreeStacks<>(left, scratch, right);

            for (int i = 0; i < n; i++)
                deque.pushLeft(i);
            for (int i = 0; i < n; i++)
                deque.popRight();

            long primitives = left.operations + scratch.operations + right.operations;
            assertTrue(primitives < 6L * 2 * n, "n = " + n + ": " + primitives + " primitives for " + 2 * n + " operations");
            assertTrue(primitives > 5L * 2 * n, "n = " + n + ": " + primitives + " primitives for " + 2 * n + " operations");
        }
    }

    @Test
    public void randomInterleavingsStayUnderSixPrimitivesPerOperation() {
        Random random = new Random(31L); // fixed seed -> reproducible failures

        for (int trial = 0; trial < 2000; trial++) {
            CountingStack<Integer> left = new CountingStack<>();
            CountingStack<Integer> scratch = new CountingStack<>();
            CountingStack<Integer> right = new CountingStack<>();
            DequeWithThreeStacks<Integer> deque = new DequeWithThreeStacks<>(left, scratch, right);
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

            long primitives = left.operations + scratch.operations + right.operations;
            assertTrue(primitives <= 6 * dequeOperations, "trial " + trial + ": " + primitives + " primitives for " + dequeOperations + " operations");
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
}
