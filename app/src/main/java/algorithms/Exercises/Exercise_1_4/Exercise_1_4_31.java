package algorithms.Exercises.Exercise_1_4;

import java.util.NoSuchElementException;

import algorithms.Stack;

/******************************************************************************
 * Exercise 1.4.31
 * 
 * <p>
 * <i>Deque with three stacks</i>. Implement a deque with three stacks so that
 * each deque operation takes a constant amortized number of stack operations.
 * </p>
 * 
 * <pre>
 * ./gradlew run -PmainClass=Exercises.Exercise_1_4.Exercise_1_4_31
 * ./gradlew test --tests "Exercise_1_4_31Test"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_31 {
    public static class DequeWithThreeStacks<Item> {
        private Stack<Item> left;
        private Stack<Item> scratch;
        private Stack<Item> right;

        public DequeWithThreeStacks() {
            this(new Stack<>(), new Stack<>(), new Stack<>());
        }

        DequeWithThreeStacks(Stack<Item> left, Stack<Item> scratch, Stack<Item> right) {
            this.left = left;
            this.scratch = scratch;
            this.right = right;
        }

        private void rebalance(Stack<Item> from, Stack<Item> to) {
            if (from.size() == 1)
                pour(from, to);
            else {
                pour(from, scratch);
                split(from, to);
            }
        }

        private void pour(Stack<Item> from, Stack<Item> to) {
            while (!from.isEmpty()) {
                to.push(from.pop());
            }
        }

        private void split(Stack<Item> from, Stack<Item> to) {
            int movedTop = scratch.size() / 2;
            int movedBottom = scratch.size() - movedTop;

            for (int i = 0; i < movedTop; i++) {
                from.push(scratch.pop());
            }

            for (int i = 0; i < movedTop; i++) {
                to.push(from.pop());
            }

            for (int i = 0; i < movedBottom; i++) {
                from.push(scratch.pop());
            }
        }

        public void pushLeft(Item item) {
            left.push(item);
        }

        public Item popLeft() {
            if (isEmpty())
                throw new NoSuchElementException("Deque underflow");

            if (left.isEmpty()) {
                rebalance(right, left);
            }

            return left.pop();
        }

        public void pushRight(Item item) {
            right.push(item);
        }

        public Item popRight() {
            if (isEmpty())
                throw new NoSuchElementException("Deque underflow");

            if (right.isEmpty()) {
                rebalance(left, right);
            }

            return right.pop();
        }

        public boolean isEmpty() {
            return left.isEmpty() && right.isEmpty();
        }

        public int size() {
            return left.size() + right.size();
        }

        public String toString() {
            return left.toString() + " " + right.toString();
        }
    }

    public static void main(String[] args) {
        DequeWithThreeStacks<Integer> deque = new DequeWithThreeStacks<>();
        deque.pushLeft(1);
        deque.pushLeft(2);
        deque.pushLeft(3);
        deque.pushLeft(4);
        deque.pushLeft(5);
        deque.pushLeft(6);
        System.out.println(deque); // 6 5 4 3 2 1
        System.out.println(deque.popLeft()); // 6
        System.out.println(deque.popRight()); // 1
    }
}
