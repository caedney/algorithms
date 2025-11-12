package algorithms.Exercises.Exercise_1_4;

/******************************************************************************
 * Exercise 1.4.12
 * 
 * <p>
 * Write a program that, given two sorted arrays of 𝑁 {@code int} values,
 * prints all elements that appear in both arrays, in sorted order. The running
 * time of your program should be proportional to 𝑁 in the worst case.
 * </p>
 *
 * <pre>
 * ./gradlew run -PmainClass=Exercises.Exercise_1_4.Exercise_1_4_12
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_12 {
    public static void printIntersection(int[] a, int[] b) {
        int i = 0;
        int j = 0;

        while (i < a.length && j < b.length) {
            if (a[i] < b[j])
                i++;
            else if (a[i] > b[j])
                j++;
            else {
                System.out.println(a[i]);
                i++;
                j++;
            }
        }
    }

    public static void main(String[] args) {
        int[] a = { 1, 3, 5, 7 };
        int[] b = { 3, 4, 7, 9 };
        printIntersection(a, b); // 3, 7
    }
}
