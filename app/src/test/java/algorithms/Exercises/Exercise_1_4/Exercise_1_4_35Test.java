package algorithms.Exercises.Exercise_1_4;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import algorithms.Exercises.Exercise_1_4.Exercise_1_4_35.CountingLinkedStack;
import algorithms.Exercises.Exercise_1_4.Exercise_1_4_35.CountingLinkedStackOfInts;
import algorithms.Exercises.Exercise_1_4.Exercise_1_4_35.CountingResizingArrayStack;
import algorithms.Exercises.Exercise_1_4.Exercise_1_4_35.CountingResizingArrayStackOfInts;

/******************************************************************************
 * Exercise_1_4_35Test
 *
 * <pre>
 * ./gradlew test --tests "Exercise_1_4_35Test"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_35Test {
    private static final int LARGEST = 1 << 16;

    @Test
    public void everyStackStartsWithNothingCounted() {
        // The resizing arrays are born with one slot; that array belongs to
        // the constructor, not to a push, so it is not an object created.
        CountingLinkedStackOfInts listOfInts = new CountingLinkedStackOfInts();
        CountingLinkedStack<Integer> listOfIntegers = new CountingLinkedStack<>();
        CountingResizingArrayStackOfInts arrayOfInts = new CountingResizingArrayStackOfInts();
        CountingResizingArrayStack<Integer> arrayOfIntegers = new CountingResizingArrayStack<>();

        assertEquals(0, listOfInts.dataReferences + listOfInts.objectsCreated + listOfInts.size());
        assertEquals(0, listOfIntegers.dataReferences + listOfIntegers.objectsCreated + listOfIntegers.size());
        assertEquals(0, arrayOfInts.dataReferences + arrayOfInts.objectsCreated + arrayOfInts.size());
        assertEquals(0, arrayOfIntegers.dataReferences + arrayOfIntegers.objectsCreated + arrayOfIntegers.size());
    }

    @Test
    public void linkedListPushOfAnIntIsTwoReferencesAndOneObject() {
        // The node's item and next are written; first and size are the
        // stack's own bookkeeping and are not counted.
        CountingLinkedStackOfInts stack = new CountingLinkedStackOfInts();

        for (int n = 1; n <= 100; n++) {
            stack.push(n);
            assertEquals(2L * n, stack.dataReferences, "references after push " + n);
            assertEquals(n, stack.objectsCreated, "objects after push " + n);
            assertEquals(n, stack.size());
        }
    }

    @Test
    public void genericLinkedListCountsOnlyItsOwnWork() {
        // Handed a box that already exists, the generic stack costs the same
        // as the int stack: the box is the client's.
        CountingLinkedStack<Integer> stack = new CountingLinkedStack<>();
        Integer box = Integer.valueOf(1000);

        for (int n = 1; n <= 100; n++) {
            stack.push(box);
            assertEquals(2L * n, stack.dataReferences, "references after push " + n);
            assertEquals(n, stack.objectsCreated, "objects after push " + n);
        }
    }

    @Test
    public void linkedListOfIntsIsExactlyTwoNAndN() {
        for (int n = 0; n <= 1025; n++)
            assertArrayEquals(new long[] { 2L * n, n }, Exercise_1_4_35.linkedListOfInts(n), "N = " + n);

        assertArrayEquals(new long[] { 2L * LARGEST, LARGEST }, Exercise_1_4_35.linkedListOfInts(LARGEST));
    }

    @Test
    public void linkedListOfIntegersIsExactlyThreeNAndTwoN() {
        for (int n = 0; n <= 1025; n++)
            assertArrayEquals(new long[] { 3L * n, 2L * n }, Exercise_1_4_35.linkedListOfIntegers(n), "N = " + n);

        assertArrayEquals(new long[] { 3L * LARGEST, 2L * LARGEST }, Exercise_1_4_35.linkedListOfIntegers(LARGEST));
    }

    @Test
    public void boxingAValueAboveTheCacheMakesANewObjectEveryTime() {
        // The premise of the Integer rows. Values from -128 to 127 are shared,
        // so a workload that pushed them would create no boxes at all.
        assertNotSame(Integer.valueOf(1000), Integer.valueOf(1000));
        assertSame(Integer.valueOf(5), Integer.valueOf(5));
    }

    @Test
    public void resizingArrayDoublesOnThePushThatFindsItFull() {
        // A new array on pushes 2, 3, 5, 9 and 17, not on the pushes that
        // fill the old one.
        CountingResizingArrayStackOfInts stack = new CountingResizingArrayStackOfInts();
        long[] expectedArrays = { 0, 1, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 5 };

        for (int i = 0; i < expectedArrays.length; i++) {
            stack.push(i);
            assertEquals(expectedArrays[i], stack.objectsCreated, "arrays after push " + (i + 1));
        }
    }

    @Test
    public void sixteenPushesCopyFifteenItemsAndSeventeenCopyThirtyOne() {
        // The write-up's two cases: 16 + 2 * 15 and 17 + 2 * 31.
        assertArrayEquals(new long[] { 46, 4 }, Exercise_1_4_35.resizingArrayOfInts(16));
        assertArrayEquals(new long[] { 79, 5 }, Exercise_1_4_35.resizingArrayOfInts(17));
    }

    @Test
    public void resizingArrayOfIntsMatchesTheCopyCountForEveryN() {
        for (int n = 1; n <= 2049; n++) {
            long[] expected = { n + 2 * itemsCopied(n), doublings(n) };
            assertArrayEquals(expected, Exercise_1_4_35.resizingArrayOfInts(n), "N = " + n);
        }
    }

    @Test
    public void resizingArrayOfIntsIsThreeNAtAPowerOfTwoAndFiveNOnePast() {
        for (int n = 2; n <= LARGEST; n *= 2) {
            assertEquals(3L * n - 2, Exercise_1_4_35.resizingArrayOfInts(n)[0], "N = " + n);
            assertEquals(5L * (n + 1) - 6, Exercise_1_4_35.resizingArrayOfInts(n + 1)[0], "N = " + (n + 1));
        }
    }

    @Test
    public void resizingArrayOfIntsStaysBetweenThreeNAndFiveN() {
        for (int n = 2; n <= 4097; n++) {
            long references = Exercise_1_4_35.resizingArrayOfInts(n)[0];
            assertTrue(references >= 3L * n - 2 && references <= 5L * n - 6, "N = " + n + ": " + references);
        }
    }

    @Test
    public void resizingArrayCreatesCeilingLgNArrays() {
        for (int n = 2; n <= LARGEST; n *= 2) {
            int lg = Integer.numberOfTrailingZeros(n);
            assertEquals(lg, Exercise_1_4_35.resizingArrayOfInts(n)[1], "N = " + n);
            assertEquals(lg + 1, Exercise_1_4_35.resizingArrayOfInts(n + 1)[1], "N = " + (n + 1));
        }
    }

    @Test
    public void genericResizingArrayCountsOnlyItsOwnWork() {
        CountingResizingArrayStack<Integer> stack = new CountingResizingArrayStack<>();
        Integer box = Integer.valueOf(1000);

        for (int n = 1; n <= 1025; n++) {
            stack.push(box);
            assertEquals(n + 2 * itemsCopied(n), stack.dataReferences, "references after push " + n);
            assertEquals(doublings(n), stack.objectsCreated, "objects after push " + n);
            assertEquals(n, stack.size());
        }
    }

    @Test
    public void boxingAddsNReferencesAndNObjectsToTheResizingArray() {
        // The same N the linked list gains, which is why the worst case is
        // about 6N under this convention and not the table's 5N.
        for (int n = 1; n <= 2049; n++) {
            long[] ints = Exercise_1_4_35.resizingArrayOfInts(n);
            long[] integers = Exercise_1_4_35.resizingArrayOfIntegers(n);

            assertEquals(ints[0] + n, integers[0], "references, N = " + n);
            assertEquals(ints[1] + n, integers[1], "objects, N = " + n);
        }

        assertArrayEquals(new long[] { 96, 22 }, Exercise_1_4_35.resizingArrayOfIntegers(17));
    }

    @Test
    public void resizingArrayOfIntegersIsSixNOnePastAPowerOfTwo() {
        for (int n = 2; n <= LARGEST; n *= 2) {
            assertEquals(4L * n - 2, Exercise_1_4_35.resizingArrayOfIntegers(n)[0], "N = " + n);
            assertEquals(6L * (n + 1) - 6, Exercise_1_4_35.resizingArrayOfIntegers(n + 1)[0], "N = " + (n + 1));
        }
    }

    @Test
    public void boxingCostsBothImplementationsTheSame() {
        for (int n = 1; n <= 1025; n++) {
            long listReferences = Exercise_1_4_35.linkedListOfIntegers(n)[0] - Exercise_1_4_35.linkedListOfInts(n)[0];
            long arrayReferences = Exercise_1_4_35.resizingArrayOfIntegers(n)[0]
                    - Exercise_1_4_35.resizingArrayOfInts(n)[0];
            long listObjects = Exercise_1_4_35.linkedListOfIntegers(n)[1] - Exercise_1_4_35.linkedListOfInts(n)[1];
            long arrayObjects = Exercise_1_4_35.resizingArrayOfIntegers(n)[1]
                    - Exercise_1_4_35.resizingArrayOfInts(n)[1];

            assertEquals(listReferences, arrayReferences, "references, N = " + n);
            assertEquals(listObjects, arrayObjects, "objects, N = " + n);
        }
    }

    /**
     * The number of items copied by 𝑁 pushes from an empty one-slot array,
     * worked out by walking the capacity rather than by the stack's own code:
     * a push that finds the array full copies everything in it.
     */
    private static long itemsCopied(int n) {
        long copied = 0;
        int capacity = 1;

        for (int size = 0; size < n; size++) {
            if (size == capacity) {
                copied += size;
                capacity *= 2;
            }
        }

        return copied;
    }

    /** The number of times the array doubles during 𝑁 pushes from one slot. */
    private static long doublings(int n) {
        long count = 0;

        for (int capacity = 1; capacity < n; capacity *= 2)
            count++;

        return count;
    }
}
