package algorithms;

public class ArrayUtils {
    public static <T> T[] reverseArray(T[] array) {
        int N = array.length;

        for (int i = 0; i < N / 2; i++) {
            T temp = array[i];
            array[i] = array[N - 1 - i];
            array[N - i - 1] = temp;
        }

        return array;
    }

    public static <T extends Comparable<T>> T getMaxValue(T[] array) {
        T max = array[0];

        for (int i = 1; i < array.length; i++) {
            if (array[i].compareTo(max) > 0) {
                max = array[i];
            }
        }

        return max;
    }

    public static <T extends Number> double getAverageValue(T[] array) {
        int N = array.length;
        double sum = 0.0;

        for (int i = 0; i < N; i++) {
            sum += array[i].doubleValue();
        }

        return sum / N;
    }

    // Returns an array of length M counting occurrences of each integer
    public static int[] histogram(int[] a, int M) {
        int[] counts = new int[M];

        for (int value : a) {
            if (value >= 0 && value < M) { // ensure the value is in the expected range
                counts[value]++;
            } else {
                throw new IllegalArgumentException("Array contains value out of range 0 to " + (M - 1));
            }
        }

        return counts;
    }
}
