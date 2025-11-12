package algorithms.Exercises.Exercise_1_4;

/******************************************************************************
 * Exercise 1.4.20
 * 
 * <p>
 * <i>Bitonic search</i>. An array is <i>bitonic</i> if it is comprised of an
 * increasing sequence of integers followed immediately by a decreasing sequence
 * of integers. Write a program that, given a bitonic array of 𝑁 distinct
 * <code>int</code> values, determines whether a given integer is in the array.
 * Your program should use ~3lg 𝑁 compares in the worst case.
 * </p>
 * 
 * <pre>
 * ./gradlew run -PmainClass=Exercises.Exercise_1_4.Exercise_1_4_20
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_20 {
    public static int findPeak(int[] a) {
        int lo = 0;
        int hi = a.length - 1;

        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;

            if (a[mid] < a[mid + 1]) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }

        return lo;
    }

    public static int ascendingSearch(int[] a, int target, int lo, int hi) {
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;

            if (target > a[mid]) {
                lo = mid + 1;
            } else if (target < a[mid]) {
                hi = mid - 1;
            } else {
                return mid;
            }
        }

        return -1;
    }

    public static int descendingSearch(int[] a, int target, int lo, int hi) {
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;

            if (target < a[mid]) {
                lo = mid + 1;
            } else if (target > a[mid]) {
                hi = mid - 1;
            } else {
                return mid;
            }
        }

        return -1;
    }

    public static int findTarget(int[] a, int target) {
        if (a.length == 0) {
            return -1;
        }

        int peak = findPeak(a);
        int asc = ascendingSearch(a, target, 0, peak);

        if (asc > -1) {
            return asc;
        }

        return descendingSearch(a, target, peak + 1, a.length - 1);
    }

    public static void main(String[] args) {
        int[] a = { 2, 7, 11, 25, 19, 12, 5, 1 };
        int target = findTarget(a, 19);
        System.out.println("Target was found at index: " + target);
    }
}
