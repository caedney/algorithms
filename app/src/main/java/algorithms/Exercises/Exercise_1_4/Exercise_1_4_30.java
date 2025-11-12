package algorithms.Exercises.Exercise_1_4;

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

        public void pushLeft(Item item) {
            left.push(item);
        }

        public void popLeft() {
            left.pop();
        }

        public void pushRight(Item item) {
            right.enqueue(item);
        }

        public void popRight() {
            right.pop();
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
    }
}
