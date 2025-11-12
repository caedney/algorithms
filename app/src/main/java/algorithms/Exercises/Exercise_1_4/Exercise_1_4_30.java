package algorithms.Exercises.Exercise_1_4;

import java.util.NoSuchElementException;

import algorithms.Stack;
import algorithms.Steque;

/******************************************************************************
 * Exercise 1.4.30
 * 
 * <p>
 * <i>Deque with a stack and a steque</i>. Implement a deque with a stack and a
 * steque (see EXERCISE 1.3.32) so that each deque operation takes a constant
 * amortized number of stack and steque operations.
 * </p>
 * 
 * <pre>
 * ./gradlew run -PmainClass=Exercises.Exercise_1_4.Exercise_1_4_30
 * ./gradlew test --tests "Exercise_1_4_30Test"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_30 {
    public static class DequeWithAStackAndASteque<Item> {
        private Stack<Item> left;
        private Steque<Item> right;

        public DequeWithAStackAndASteque() {
            this(new Stack<>(), new Steque<>());
        }

        DequeWithAStackAndASteque(Stack<Item> left, Steque<Item> right) {
            this.left = left;
            this.right = right;
        }

        private void pour() {
            while (!left.isEmpty()) {
                right.push(left.pop());
            }
        }

        private void split() {
            int rotatedHalf = right.size() / 2;
            int movedHalf = right.size() - rotatedHalf;

            for (int i = 0; i < rotatedHalf; i++) {
                right.enqueue(right.pop());
            }

            for (int i = 0; i < movedHalf; i++) {
                left.push(right.pop());
            }
        }

        public void pushLeft(Item item) {
            left.push(item);
        }

        public Item popLeft() {
            if (isEmpty())
                throw new NoSuchElementException("Deque underflow");

            if (left.isEmpty()) {
                split();
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
                pour();

                if (right.size() > 1)
                    split();
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
        DequeWithAStackAndASteque<Integer> deque = new DequeWithAStackAndASteque<>();
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
