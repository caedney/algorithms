package algorithms.Exercises.Exercise_1_4;

/******************************************************************************
 * Exercise 1.4.17
 * 
 * <p>
 * <i>Farthest pair (in one dimension)</i>. Write a program that, given an array
 * <code>a[]</code> of 𝑁 <code>double</code> values, finds a <i>farthest
 * pair</i>: two values whose difference is no smaller than the difference of
 * any other pair (in absolute value). The running time of your program should
 * be linear in the worst case.
 * </p>
 * 
 * <pre>
 * ./gradlew run -PmainClass=Exercises.Exercise_1_4.Exercise_1_4_17
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_17 {
    public record FarthestPair(double min, double max) {
        public double distance() {
            return max - min;
        }
    }

    public static FarthestPair farthestPair(double[] a) {
        int n = a.length;

        if (n < 2)
            throw new IllegalArgumentException("farthest pair needs at least two values");

        double min = a[0], max = a[0];

        for (int i = 1; i < n; i++) {
            if (a[i] < min)
                min = a[i];
            else if (a[i] > max)
                max = a[i];
        }

        return new FarthestPair(min, max);
    }

    public static void main(String[] args) {
        double[] a = { 2.7, -1.1, 9.4, 3.0, 6.8 };
        FarthestPair result = farthestPair(a);
        System.out.println(result.toString());
        System.out.println("Distance: " + result.distance());
    }
}
