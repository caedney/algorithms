package algorithms;

import java.util.StringJoiner;

import edu.princeton.cs.algs4.StdOut;

public class Josephus {
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int m = Integer.parseInt(args[1]);

        Queue<Integer> queue = new Queue<Integer>();

        for (int i = 0; i < n; i++)
            queue.enqueue(i);

        while (!queue.isEmpty()) {
            StringJoiner joiner = new StringJoiner(", ", "[", "]");
            for (Integer q : queue)
                joiner.add(String.valueOf(q));
            StdOut.print(joiner.toString() + " → ");

            StdOut.print("enqueued: ");
            for (int i = 0; i < m - 1; i++) {
                int item = queue.dequeue();
                StdOut.print(item + ", ");
                queue.enqueue(item);
            }

            StdOut.print("dequeued: " + queue.dequeue());
            StdOut.println();
        }
    }
}
