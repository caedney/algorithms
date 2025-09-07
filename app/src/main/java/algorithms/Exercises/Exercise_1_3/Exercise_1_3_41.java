package algorithms.Exercises.Exercise_1_3;

import java.util.Iterator;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.41
 * 
 * <p>
 * <i>Copy a queue</i>. Create a new constructor so
 * {@snippet :
 * Queue<Item> r = new Queue<Item>(q);
 * }
 * makes {@code r} a reference to a new and independent copy of the queue
 * {@code q}. You should be able to push and pop from either {@code q} or
 * {@code r} without influencing the other. <i>Hint</i>: Delete all of the
 * elements from {@code q} and add these elements to both {@code q} and
 * {@code r}.
 * </p>
 */
public class Exercise_1_3_41 {
    public static class Queue<Item> implements Iterable<Item> {
        private Node<Item> front;
        private Node<Item> back;
        private int size;

        public Queue() {
            front = null;
            back = null;
            size = 0;
        }

        public Queue(Queue<Item> queue) {
            this(); // initialize empty queue

            Queue<Item> temp = new Queue<>();

            while (!queue.isEmpty()) {
                Item item = queue.dequeue(); // remove from queue
                this.enqueue(item); // copy into this
                temp.enqueue(item); // keep backup
            }

            while (!temp.isEmpty())
                queue.enqueue(temp.dequeue()); // restore queue
        }

        private static class Node<Item> {
            private Item item;
            private Node<Item> next;

            Node(Item item, Node<Item> next) {
                this.item = item;
                this.next = next;
            }
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public int size() {
            return size;
        }

        public void enqueue(Item item) {
            Node<Item> newNode = new Node<Item>(item, null);

            if (isEmpty()) {
                front = newNode;
                back = newNode;
            } else {
                back.next = newNode;
                back = newNode;
            }

            size++;
        }

        public Item dequeue() {
            if (isEmpty())
                throw new RuntimeException("Queue underflow");

            Item item = front.item;
            front = front.next;
            size--;

            if (isEmpty())
                back = null;

            return item;
        }

        public Iterator<Item> iterator() {
            return new QueueIterator(front);
        }

        private class QueueIterator implements Iterator<Item> {
            private Node<Item> current;

            public QueueIterator(Node<Item> front) {
                current = front;
            }

            public boolean hasNext() {
                return current != null;
            }

            public Item next() {
                Item item = current.item;
                current = current.next;
                return item;
            }
        }
    }

    public static void main(String[] args) {
        Queue<Integer> q = new Queue<>();
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);

        Queue<Integer> r = new Queue<>(q);
        q.dequeue();
        r.enqueue(4);

        StdOut.println(q.size()); // 2
        StdOut.println(r.size()); // 4
    }
}
