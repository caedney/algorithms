package algorithms.Exercises.Exercise_1_4;

import java.util.NoSuchElementException;

import algorithms.Stack;

/******************************************************************************
 * Exercise 1.4.27
 * 
 * <p>
 * <i>Queue with two stacks</i>. Implement a queue with two stacks so that each
 * queue operation takes a constant amortized number of stack operations.
 * <i>Hint</i>: If you push elements onto a stack and then pop them all, they
 * appear in reverse order. If you repeat this process, they're now back in
 * order.
 * </p>
 * 
 * <pre>
 * ./gradlew run -PmainClass=Exercises.Exercise_1_4.Exercise_1_4_27
 * ./gradlew test --tests "Exercise_1_4_27Test"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_27 {
    static class QueueWithTwoStacks<Item> {
        private Stack<Item> in;
        private Stack<Item> out;

        public QueueWithTwoStacks() {
            this(new Stack<>(), new Stack<>());
        }

        QueueWithTwoStacks(Stack<Item> in, Stack<Item> out) {
            this.in = in;
            this.out = out;
        }

        public void enqueue(Item item) {
            in.push(item);
        }

        public Item dequeue() {
            if (isEmpty())
                throw new NoSuchElementException("Queue underflow");

            if (out.isEmpty()) {
                while (!in.isEmpty()) {
                    out.push(in.pop());
                }
            }

            return out.pop();
        }

        public boolean isEmpty() {
            return in.isEmpty() && out.isEmpty();
        }

        public int size() {
            return in.size() + out.size();
        }

        public String toString() {
            return in.toString() + " " + out.toString();
        }
    }

    public static void main(String[] args) {
        QueueWithTwoStacks<Integer> queue = new QueueWithTwoStacks<>();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        System.out.println(queue.dequeue()); // 1
        queue.enqueue(4);
        System.out.println(queue.dequeue()); // 2
        System.out.println(queue.dequeue()); // 3
        System.out.println(queue.dequeue()); // 4
    }
}
