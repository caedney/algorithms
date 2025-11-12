package algorithms.Exercises.Exercise_1_4;

import java.util.NoSuchElementException;

import algorithms.Queue;

/******************************************************************************
 * Exercise 1.4.28
 * 
 * <p>
 * <i>Stack with a queue</i>. Implement a stack with a single queue so that each
 * stack operation takes a linear number of queue operations. <i>Hint</i>: To
 * delete an item, get all of the elements on the queue one at a time, and put
 * them at the end, except for the last one which you should delete and return.
 * (This solution is admittedly very inefficient.)
 * </p>
 * 
 * <pre>
 * ./gradlew run -PmainClass=Exercises.Exercise_1_4.Exercise_1_4_28
 * ./gradlew test --tests "Exercise_1_4_28Test"
 * </pre>
 ******************************************************************************/
public class Exercise_1_4_28 {
    static class StackWithAQueue<Item> {
        private Queue<Item> queue;

        public StackWithAQueue() {
            this(new Queue<>());
        }

        StackWithAQueue(Queue<Item> queue) {
            this.queue = queue;
        }

        public void push(Item item) {
            queue.enqueue(item);

            for (int i = 0; i < queue.size() - 1; i++) {
                queue.enqueue(queue.dequeue());
            }
        }

        public Item pop() {
            if (isEmpty())
                throw new NoSuchElementException("Stack underflow");

            return queue.dequeue();
        }

        public Item peek() {
            if (isEmpty())
                throw new NoSuchElementException("Stack underflow");

            return queue.peek();
        }

        public boolean isEmpty() {
            return queue.isEmpty();
        }

        public int size() {
            return queue.size();
        }

        public String toString() {
            return queue.toString();
        }
    }

    public static void main(String[] args) {
        StackWithAQueue<Integer> stack = new StackWithAQueue<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        System.out.println(stack.pop()); // 4
        System.out.println(stack.pop()); // 3
        System.out.println(stack.pop()); // 2
        System.out.println(stack.pop()); // 1
    }
}
