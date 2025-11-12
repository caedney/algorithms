package algorithms.Exercises.Exercise_1_4;

import java.io.FileNotFoundException;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/******************************************************************************
 * Exercise 1.4.10
 * 
 * <p>
 * Modify binary search so that it always returns the element with the smallest
 * index that matches the search element (and still guarantees logarithmic
 * running time).
 * </p>
 *
 * <pre>
 * ./gradlew run -PmainClass=Exercises.Exercise_1_4.Exercise_1_4_10 --args="src/data/algs4/tinyW.txt" < "app/src/data/algs4/tinyT.txt"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_10 {
    public static int rank(int key, int[] array) {
        int lo = 0;
        int hi = array.length - 1;
        int result = -1;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;

            if (key < array[mid])
                hi = mid - 1;
            else if (key > array[mid])
                lo = mid + 1;
            else {
                result = mid; // found one, but maybe not the first...
                hi = mid - 1; // ...so keep looking to the left
            }
        }

        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        int[] whitelist = new In(args[0]).readAllInts();
        Arrays.sort(whitelist);

        while (!StdIn.isEmpty()) {
            int key = StdIn.readInt();

            if (rank(key, whitelist) < 0)
                StdOut.println(key);
        }
    }
}