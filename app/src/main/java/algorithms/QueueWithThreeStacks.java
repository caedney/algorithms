package algorithms;

import edu.princeton.cs.algs4.StdOut;

public class QueueWithThreeStacks<T> {
    private Stack<T> in = new Stack<>();
    private Stack<T> out = new Stack<>();
    private Stack<T> buffer = new Stack<>();
    private boolean transferring = false;

    public void enqueue(T item) {
        in.push(item);
        stepTransfer(); // move work forward incrementally
    }

    public T dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }

        if (out.isEmpty()) {
            stepTransfer(); // progress transfer
        }

        return out.pop();
    }

    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }

        if (out.isEmpty()) {
            stepTransfer();
        }

        return out.peek();
    }

    public boolean isEmpty() {
        return in.isEmpty() && out.isEmpty() && buffer.isEmpty();
    }

    // Each call does a constant amount of transfer work
    private void stepTransfer() {
        if (!transferring && out.isEmpty()) {
            // Start new transfer cycle
            transferring = true;
            while (!in.isEmpty()) {
                buffer.push(in.pop());
            }
        }

        if (transferring) {
            if (!buffer.isEmpty()) {
                out.push(buffer.pop()); // move 1 element per step
            }
            if (buffer.isEmpty()) {
                transferring = false; // done transferring
            }
        }
    }

    public static void main(String[] args) {
        QueueWithThreeStacks<Integer> queue = new QueueWithThreeStacks<>();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        StdOut.println(queue.dequeue()); // 1
        queue.enqueue(4);
        StdOut.println(queue.dequeue()); // 2
        StdOut.println(queue.dequeue()); // 3
        StdOut.println(queue.dequeue()); // 4
    }
}
