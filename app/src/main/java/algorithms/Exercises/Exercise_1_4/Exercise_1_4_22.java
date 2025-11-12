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
 * search range to be the interval [𝑖, 𝑖 + 𝐹ₖ] and keep 𝐹ₖ and 𝐹ₖ₋₁, in two
 * variables. At each step compute 𝐹ₖ₋₂ via subtraction, check element 𝑖 +
 * 𝐹ₖ₋₂, and update the current range to either [𝑖, 𝑖 + 𝐹ₖ₋₂] or [𝑖 +
 * 𝐹ₖ₋₂, 𝑖 + 𝐹ₖ₋₂ + 𝐹ₖ₋₁].
 * </p>
 * 
 * <pre>
 * ./gradlew run -PmainClass=Exercises.Exercise_1_4.Exercise_1_4_22
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_22 {
    public static void main(String[] args) {
    }
}
