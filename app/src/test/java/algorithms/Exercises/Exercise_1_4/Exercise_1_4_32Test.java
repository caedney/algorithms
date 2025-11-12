package algorithms.Exercises.Exercise_1_4;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import algorithms.Exercises.Exercise_1_4.Exercise_1_4_32.CountingResizingArrayStack;

/******************************************************************************
 * Exercise_1_4_32Test
 *
 * <pre>
 * ./gradlew test --tests "Exercise_1_4_32Test"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_32Test {
    private static final int QUARTER = 4;
    private static final int HALF = 2;

    @Test
    public void startsEmptyWithOneSlot() {
        CountingResizingArrayStack<Integer> stack = new CountingResizingArrayStack<>(QUARTER);

        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
        assertEquals(1, stack.capacity());
        assertEquals(0, stack.accesses);
    }

    @Test
    public void doublesOnThePushThatFindsTheArrayFull() {
        // The array doubles on pushes 2, 3, 5 and 9, not on the pushes that
        // fill it: the second push finds one slot full and makes two.
        CountingResizingArrayStack<Integer> stack = new CountingResizingArrayStack<>(QUARTER);
        int[] expectedCapacity = { 1, 2, 4, 4, 8, 8, 8, 8, 16 };

        for (int i = 0; i < expectedCapacity.length; i++) {
            stack.push(i);
            assertEquals(expectedCapacity[i], stack.capacity(), "capacity after push " + (i + 1));
        }
    }

    @Test
    public void halvesOnThePopThatLeavesTheArrayOneQuarterFull() {
        CountingResizingArrayStack<Integer> stack = pushed(16, QUARTER);
        assertEquals(16, stack.capacity());

        for (int i = 0; i < 12; i++)
            stack.pop();
        assertEquals(8, stack.capacity()); // 4 items in 16 slots halved

        stack.pop();
        stack.pop();
        assertEquals(4, stack.capacity()); // 2 items in 8 slots halved

        stack.pop();
        assertEquals(2, stack.capacity()); // 1 item in 4 slots halved

        stack.pop();
        assertEquals(2, stack.capacity()); // empty: no halving to nothing
    }

    @Test
    public void aResizeLeavesTheArrayHalfFull() {
        // Every resize in either direction creates a half-full array. A
        // doubling happens inside push before the write, so by the time push
        // returns the new array holds one more than half.
        CountingResizingArrayStack<Integer> stack = new CountingResizingArrayStack<>(QUARTER);

        for (int i = 0; i < 1024; i++) {
            int before = stack.capacity();
            stack.push(i);
            if (stack.capacity() != before)
                assertEquals(stack.capacity() / 2 + 1, stack.size(), "after doubling to " + stack.capacity());
        }

        while (stack.size() > 1) {
            int before = stack.capacity();
            stack.pop();
            if (stack.capacity() != before)
                assertEquals(stack.capacity() / 2, stack.size(), "after halving to " + stack.capacity());
        }
    }

    @Test
    public void sixteenPushesCostFortySix() {
        // Copies of 1, 2, 4 and 8 items, 30 accesses, plus 16 writes.
        assertEquals(46, pushed(16, QUARTER).accesses);
    }

    @Test
    public void seventeenPushesCostSeventyNine() {
        // One past a power of two: the seventeenth push copies 16 more.
        assertEquals(79, pushed(17, QUARTER).accesses);
    }

    @Test
    public void theRoundTripCostsNinetyTwo() {
        // Sixteen up, sixteen down: halvings copy 4, 2 and 1 items, 14
        // accesses, plus 2 per pop; the way down costs the same as the way up.
        CountingResizingArrayStack<Integer> stack = pushed(16, QUARTER);

        for (int i = 0; i < 16; i++)
            stack.pop();

        assertEquals(92, stack.accesses);
        assertTrue(stack.isEmpty());
    }

    @Test
    public void onePastAPowerOfTwoCostsFiveMMinusSix() {
        for (int m = 2; m <= 1 << 16; m *= 2)
            assertEquals(5L * (m + 1) - 6, pushed(m + 1, QUARTER).accesses, "M = " + (m + 1));
    }

    @Test
    public void pushesOnlyApproachFivePerOperationFromBelow() {
        double previous = 0;

        for (int m = 16; m <= 1 << 16; m *= 2) {
            double ratio = (double) pushed(m + 1, QUARTER).accesses / (m + 1);

            assertTrue(ratio < 5, "M = " + (m + 1) + " ratio " + ratio);
            assertTrue(ratio > previous, "M = " + (m + 1) + " ratio " + ratio + " fell from " + previous);
            previous = ratio;
        }
    }

    @Test
    public void randomInterleavingsStayUnderFivePerOperation() {
        Random random = new Random(32L); // fixed seed -> reproducible failures

        for (int run = 0; run < 2000; run++) {
            CountingResizingArrayStack<Integer> stack = new CountingResizingArrayStack<>(QUARTER);
            int operations = 1 + random.nextInt(2000);
            double pushBias = random.nextDouble();

            for (int i = 0; i < operations; i++) {
                if (stack.isEmpty() || random.nextDouble() < pushBias)
                    stack.push(i);
                else
                    stack.pop();
            }

            assertTrue(stack.accesses <= 5L * operations + 1,
                    "run " + run + ": " + stack.accesses + " accesses for " + operations + " operations");
        }
    }

    @Test
    public void agreesWithArrayDequeOnRandomInterleavings() {
        Random random = new Random(32L); // fixed seed -> reproducible failures

        for (int run = 0; run < 2000; run++) {
            CountingResizingArrayStack<Integer> stack = new CountingResizingArrayStack<>(QUARTER);
            ArrayDeque<Integer> oracle = new ArrayDeque<>();
            int operations = 1 + random.nextInt(500);

            for (int i = 0; i < operations; i++) {
                if (oracle.isEmpty() || random.nextBoolean()) {
                    stack.push(i);
                    oracle.push(i);
                } else {
                    assertEquals(oracle.pop(), stack.pop(), "run " + run + " step " + i);
                }

                assertEquals(oracle.size(), stack.size(), "run " + run + " step " + i);
                assertEquals(oracle.isEmpty(), stack.isEmpty(), "run " + run + " step " + i);
            }
        }
    }

    @Test
    public void sloshNeverResizesAtOneQuarter() {
        // Alternating pop and push just past a doubling: at one-quarter the
        // array stays at 32 slots and every operation is one or two accesses.
        CountingResizingArrayStack<Integer> stack = pushed(17, QUARTER);
        long before = stack.accesses;

        for (int i = 0; i < 1000; i++) {
            stack.pop();
            stack.push(i);
        }

        assertEquals(32, stack.capacity());
        assertEquals(before + 3L * 1000, stack.accesses);
    }

    @Test
    public void sloshResizesOnEveryOperationAtOneHalf() {
        // The same alternation with halving at one-half full: the pop halves
        // the array to 16 slots holding 16, the push doubles it back, and each
        // copies 16 items, 32 accesses, plus its own.
        CountingResizingArrayStack<Integer> stack = pushed(17, HALF);
        long before = stack.accesses;

        for (int i = 0; i < 1000; i++) {
            stack.pop();
            assertEquals(16, stack.capacity(), "after pop " + i);
            stack.push(i);
            assertEquals(32, stack.capacity(), "after push " + i);
        }

        assertEquals(before + (32L + 2 + 32 + 1) * 1000, stack.accesses);
    }

    @Test
    public void sloshAtOneHalfGrowsWithTheArray() {
        // Accesses per operation on the alternation scale with the array size
        // rather than settling at a constant: doubling the climb roughly
        // doubles the ratio.
        double previous = 0;

        for (int climb = 17; climb <= (1 << 12) + 1; climb = (climb - 1) * 2 + 1) {
            CountingResizingArrayStack<Integer> stack = pushed(climb, HALF);
            long before = stack.accesses;

            for (int i = 0; i < 100; i++) {
                stack.pop();
                stack.push(i);
            }

            double perOperation = (stack.accesses - before) / 200.0;
            assertTrue(perOperation > 1.9 * previous, "climb " + climb + ": " + perOperation + " after " + previous);
            previous = perOperation;
        }
    }

    /** Pushes 𝑛 items from empty under the given shrink policy. */
    private static CountingResizingArrayStack<Integer> pushed(int n, int shrinkDivisor) {
        CountingResizingArrayStack<Integer> stack = new CountingResizingArrayStack<>(shrinkDivisor);

        for (int i = 0; i < n; i++)
            stack.push(i);

        return stack;
    }
}
