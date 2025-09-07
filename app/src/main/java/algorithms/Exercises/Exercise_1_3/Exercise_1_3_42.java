package algorithms.Exercises.Exercise_1_3;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.42
 * 
 * <p>
 * <i>Copy a stack</i>. Create a new constructor for the linked-list
 * implementation of {@code Stack} so that
 * </p>
 * {@code Stack<Item> t = new Stack<Item>(s);}
 * <p>
 * makes {@code t} a reference to a new and independent copy of the stack
 * {@code s}.
 * </p>
 */
public class Exercise_1_3_42 {
    public static class Stack<Item> implements Iterable<Item> {
        private Node<Item> head;
        private int size;

        public Stack() {
            head = null;
            size = 0;
        }

        public Stack(Stack<Item> stack) {
            this(); // initialize empty stack

            Stack<Item> temp = new Stack<>();

            while (!stack.isEmpty()) {
                Item item = stack.pop(); // remove from stack
                this.push(item); // copy into this
                temp.push(item); // keep backup
            }

            while (!temp.isEmpty())
                stack.push(temp.pop()); // restore stack
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

        public void push(Item item) {
            Node<Item> oldHead = head;
            head = new Node<Item>(item, oldHead);

            size++;
        }

        public Item pop() {
            if (isEmpty())
                throw new NoSuchElementException("Stack underflow");

            Item item = head.item; // save item to return
            head = head.next; // delete head node
            size--;

            return item; // return the saved item
        }

        public Item peek() {
            if (isEmpty())
                throw new NoSuchElementException("Stack underflow");

            return head.item;
        }

        public String toString() {
            StringBuilder s = new StringBuilder();

            for (Item item : this) {
                s.append(item);
                s.append(' ');
            }

            return s.toString();
        }

        public Iterator<Item> iterator() {
            return new StackIterator(head);
        }

        private class StackIterator implements Iterator<Item> {
            private Node<Item> current;

            public StackIterator(Node<Item> head) {
                current = head;
            }

            public boolean hasNext() {
                return current != null;
            }

            public Item next() {
                if (!hasNext())
                    throw new NoSuchElementException();

                Item item = current.item;
                current = current.next;

                return item;
            }
        }
    }

    public static void main(String[] args) {
        Stack<Integer> s = new Stack<>();
        s.push(1);
        s.push(2);
        s.push(3);

        Stack<Integer> t = new Stack<>(s);
        s.pop();
        t.push(4);

        StdOut.println(s.size()); // 2
        StdOut.println(t.size()); // 4
    }
}
