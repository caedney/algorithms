package algorithms;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class VisualAccumulator {
    private double total;
    private int size;

    public VisualAccumulator(int trials, double max) {
        StdDraw.setXscale(0, trials);
        StdDraw.setYscale(0, max);
        StdDraw.setPenRadius(.005);
    }

    public void addDataValue(double val) {
        size++;
        total += val;
        StdDraw.setPenColor(StdDraw.DARK_GRAY);
        StdDraw.point(size, val);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.point(size, total / size);
    }

    public double mean() {
        return total / size;
    }

    public String toString() {
        return "Mean (" + size + " values): " + String.format("%7.5f", mean());
    }

    public static void main(String[] args) {
        int T = Integer.parseInt(args[0]);
        VisualAccumulator a = new VisualAccumulator(T, 1.0);

        for (int t = 0; t < T; t++)
            a.addDataValue(StdRandom.uniformDouble());

        StdOut.println(a);
    }
}