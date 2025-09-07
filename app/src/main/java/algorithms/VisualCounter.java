package algorithms;

import edu.princeton.cs.algs4.StdDraw;

public class VisualCounter {
    private int count;
    private int maxOperations;
    private int operationTally;
    private int maxAbs;

    public VisualCounter(int N, int max) {
        this.count = 0;
        this.operationTally = 0;
        this.maxOperations = N;
        this.maxAbs = max;

        StdDraw.setXscale(0, N);
        StdDraw.setYscale(-max, max);
        StdDraw.setPenRadius(0.005);
    }

    public void increment() {
        if (operationTally < maxOperations && count < maxAbs) {
            count++;
            operationTally++;
            draw();
        }
    }

    public void decrement() {
        if (operationTally < maxOperations && count > -maxAbs) {
            count--;
            operationTally++;
            draw();
        }
    }

    private void draw() {
        StdDraw.point(operationTally, count);
    }

    public int tally() {
        return count;
    }
}
