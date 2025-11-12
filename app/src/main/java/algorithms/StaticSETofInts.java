package algorithms;

import java.util.Arrays;

public class StaticSETofInts {
    private int[] a;

    public String toString() {
        return Arrays.toString(a);
    }

    public StaticSETofInts(int[] keys) {
        a = new int[keys.length];

        for (int i = 0; i < keys.length; i++)
            a[i] = keys[i]; // defensive copy

        Arrays.sort(a);
    }

    public boolean contains(int key) {
        return rank(key) != -1;
    }

    public int rank(int key) {
        int lo = 0;
        int hi = a.length - 1;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;

            if (key < a[mid])
                hi = mid - 1;
            else if (key > a[mid])
                lo = mid + 1;
            else
                return mid;
        }

        return -1;
    }

    /**
     * Number of occurrences of key, in ~2 lg N compares worst case: (index of first
     * element > key) minus (index of first element >= key).
     */
    public int howMany(int key) {
        return upperBound(key) - lowerBound(key);
    }

    // first index i such that a[i] >= key (or a.length if none)
    private int lowerBound(int key) {
        int lo = 0;
        int hi = a.length;

        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;

            if (a[mid] < key)
                lo = mid + 1;
            else
                hi = mid;
        }

        return lo;
    }

    // first index i such that a[i] > key (or a.length if none)
    private int upperBound(int key) {
        int lo = 0;
        int hi = a.length;

        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;

            if (a[mid] <= key)
                lo = mid + 1;
            else
                hi = mid;
        }

        return lo;
    }
}