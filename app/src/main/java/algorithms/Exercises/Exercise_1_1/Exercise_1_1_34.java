package algorithms.Exercises.Exercise_1_1;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.1.34
 * 
 * <p>
 * <i>Filtering</i>. Which of the following <i>require</i> saving all the values
 * from standard input (in an array, say), and which could be implemented as a
 * filter using only a fixed number of variables and arrays of fixed size (not
 * dependent on 𝑁)? For each, the input comes from standard input and consists
 * of 𝑁 real numbers between 0 and 1.
 * </p>
 * 
 * <ul>
 * <li>Print the maximum and minimum numbers</li>
 * <li>Print the median of the numbers</li>
 * <li>Print the kth smallest value, for k less than 100</li>
 * <li>Print the sum of the squares of the numbers</li>
 * <li>Print the average of the N numbers</li>
 * <li>Print the percentage of numbers greater than the average</li>
 * <li>Print the N numbers in increasing order</li>
 * <li>Print the N numbers in random order</li>
 * </ul>
 */
public class Exercise_1_1_34 {
    public static void main(String[] args) {
        // 1. Print the maximum and minimum numbers
        // You only need two variables: max_so_far and min_so_far.
        // As you read each number, update them if necessary.
        StdOut.println("1. ✅ Can be done without storing all numbers");

        // 2. Print the median of the numbers
        // To find the median exactly, you need the middle value(s) after sorting.
        // Sorting or selection requires all numbers.
        StdOut.println("2. ❌ Requires saving all numbers");

        // 3. Print the kth smallest value, for k < 100
        // If 𝑘 is a small fixed number, we can maintain a fixed-size array of the k
        // smallest numbers seen so far.
        // Update it as we read numbers.
        StdOut.println("3. ✅ Can be done without storing all numbers, because k < 100 is constant");

        // 4. Print the sum of the squares of the numbers
        // Maintain a single variable sum_squares.
        // For each number x, do sum_squares += x*x.
        StdOut.println("4. ✅ Can be done without storing all numbers");

        // 5. Print the average of the N numbers
        // Maintain sum and count.
        // Average = sum / count after reading all numbers.
        StdOut.println("5. ✅ Can be done without storing all numbers");

        // 6. Print the percentage of numbers greater than the average
        // Problem: you don’t know the average until you've read all numbers.
        // To check which numbers are greater than average, you need all numbers stored.
        StdOut.println("6. ❌ Requires saving all numbers");

        // 7. Print the N numbers in increasing order
        // You must sort the numbers.
        // Sorting requires all numbers.
        StdOut.println("7. ❌ Requires saving all numbers");

        // 8. Print the N numbers in random order
        // To randomize, you typically need all numbers in memory to shuffle them (e.g.,
        // Fisher–Yates).
        StdOut.println("8. ❌ Requires saving all numbers");
    }
}