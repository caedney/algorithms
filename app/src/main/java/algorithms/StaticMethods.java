package algorithms;

import java.util.Arrays;

public class StaticMethods {
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

    public static int getIntAbsoluteNumber(int x) {
        if (x < 0)
            return -x;
        else
            return x;
    }

    public static double getDoubleAbsoluteNumber(double x) {
        if (x < 0.0)
            return -x;
        else
            return x;
    }

    public static boolean isPrimeNumber(int N) {
        if (N < 2)
            return false;

        for (int i = 2; i * i <= N; i++) {
            if (N % i == 0)
                return false;
        }

        return true;
    }

    public static double getSquareRoot(double c) {
        if (c < 0)
            return Double.NaN;

        double err = 1e-15;
        double t = c;

        while (Math.abs(t - c / t) > err * t) {
            t = (c / t + t) / 2.0;
        }

        return t;
    }

    public static void main(String[] args) {
        Integer[] array = { 1, 1, 2, 3, 5, 8, 13 };

        Integer[] reversedArray = reverseArray(array);
        System.out.println("Reversed array: " + Arrays.toString(reversedArray));

        Integer maxNumber = getMaxValue(array);
        System.out.println("Max number: " + maxNumber);

        double averageNumber = getAverageValue(array);
        System.out.println("Average number: " + averageNumber);

        int intAbsoluteNumber = getIntAbsoluteNumber(-7);
        System.out.println("Absolute int number: " + intAbsoluteNumber);

        double doubleAbsoluteNumber = getDoubleAbsoluteNumber(-7.0);
        System.out.println("Absolute int number: " + doubleAbsoluteNumber);

        boolean primeNumber = isPrimeNumber(7);
        System.out.println("Is prime number: " + primeNumber);

        double squareRoot = getSquareRoot(70);
        System.out.println("Square root is: " + squareRoot);
    }
}