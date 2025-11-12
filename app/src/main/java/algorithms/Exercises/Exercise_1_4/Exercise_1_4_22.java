package algorithms.Exercises.Exercise_1_4;

/******************************************************************************
 * Exercise 1.4.22
 * 
 * <p>
 * <i>Binary search with only addition and subtraction</i>. [Mihai Patrascu]
 * Write a program that, given an array of 𝑁 distinct <code>int</code> values
 * in ascending order, determines whether a given integer is in the array. You
 * may use only additions and subtractions and a constant amount of extra
 * memory. The running time of your program should be proportional to log 𝑁 in
 * the worst case.
 * </p>
 * 
 * <p>
 * <i>Answer</i>: Instead of searching based on powers of two (binary search),
 * use Fibonacci numbers (which also grow exponentially). Maintain the current
 * search range to be the interval [𝑖, 𝑖 + 𝐹ₖ] and keep 𝐹ₖ and 𝐹ₖ₋₁ in two
 * variables. At each step compute 𝐹ₖ₋₂ via subtraction, check element 𝑖 +
 * 𝐹ₖ₋₂, and update the current range to either [𝑖, 𝑖 + 𝐹ₖ₋₂] or [𝑖 +
 * 𝐹ₖ₋₂, 𝑖 + 𝐹ₖ₋₂ + 𝐹ₖ₋₁].
 * </p>
 * 
 * <pre>
 * ./gradlew run -PmainClass=Exercises.Exercise_1_4.Exercise_1_4_22
 * ./gradlew test --tests "Exercise_1_4_22Test"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_22 {
    public static int findTarget(int[] a, int target) {
        int n = a.length;
        int fib1 = 1;
        int fib2 = 1;

        while (fib1 < n) {
            fib1 = fib1 + fib2;
            fib2 = fib1 - fib2;
        }

        int i = 0;

        while (i < i + fib1) {
            int probe = i + fib1 - fib2;
            System.out.println("range: [" + i + ", " + (i + fib1) + ")");
            System.out.println("pair: (" + fib1 + ", " + fib2 + ")");
            System.out.println("probe: " + probe);

            if (probe >= n || target < a[probe]) {
                fib1 = fib1 - fib2;
                fib2 = fib2 - fib1 < 1 ? 1 : fib2 - fib1;
            } else if (target > a[probe]) {
                fib2 = fib1 - fib2 < 1 ? 1 : fib1 - fib2;
                fib1 = fib1 - fib2;
                i = probe;
            } else {
                return probe;
            }
        }

        return -1;
    }

    public static void main(String[] args) {
        int[] a = { 2, 4, 7, 10 };
        int target = findTarget(a, 11);
        System.out.println(target == -1 ? "Target was not found" : "Target was found at index: " + target);
    }
}
