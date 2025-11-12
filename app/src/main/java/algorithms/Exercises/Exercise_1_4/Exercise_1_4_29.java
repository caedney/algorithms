package algorithms.Exercises.Exercise_1_4;

import java.util.NoSuchElementException;

import algorithms.Stack;

/******************************************************************************
 * Exercise 1.4.29
 * 
 * <p>
 * <i>Steque with two stacks</i>. Implement a steque with two stacks so that
 * each steque operation (see EXERCISE 1.3.32) takes a constant amortized number
 * of stack operations.
 * </p>
 * 
 * <pre>
 * ./gradlew run -PmainClass=Exercises.Exercise_1_4.Exercise_1_4_29
 * ./gradlew test --tests "Exercise_1_4_29Test"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_29 {
    static class StequeWithTwoStacks<Item> {
        private Stack<Item> front;
        private Stack<Item> back;

        public StequeWithTwoStacks() {
            this(new Stack<>(), new Stack<>());
        }

        StequeWithTwoStacks(Stack<Item> front, Stack<Item> back) {
            this.front = front;
            this.back = back;
        }

        public void push(Item item) {
            front.push(item);
        }

        public Item pop() {
            if (isEmpty())
                throw new NoSuchElementException("Steque underflow");

            if (front.isEmpty()) {
                while (!back.isEmpty()) {
                    front.push(back.pop());
                }
            }

            return front.pop();
        }

        public void enqueue(Item item) {
            back.push(item);
        }

        public boolean isEmpty() {
            return front.isEmpty() && back.isEmpty();
        }

        public int size() {
            return front.size() + back.size();
        }

        public String toString() {
            return front.toString() + " " + back.toString();
        }
    }

    public static void main(String[] args) {
        StequeWithTwoStacks<Integer> steque = new StequeWithTwoStacks<>();
        steque.enqueue(1);
        steque.enqueue(2);
        steque.push(3);
        System.out.println(steque.pop()); // 3
        System.out.println(steque.pop()); // 1
        steque.enqueue(4);
        steque.push(5);
        System.out.println(steque.pop()); // 5
        System.out.println(steque.pop()); // 2
        System.out.println(steque.pop()); // 4
    }
}
