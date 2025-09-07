package algorithms;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;

public class StackFailFast<Item> implements Iterable<Item> {
    private Node<Item> head;
    private int size;
    private int modificationCounter;

    public StackFailFast() {
        head = null;
        size = 0;
        modificationCounter = 0;
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

        modificationCounter++;
        size++;
    }

    public Item pop() {
        if (isEmpty())
            throw new NoSuchElementException("Stack underflow");

        Item item = head.item; // save item to return
        head = head.next; // delete head node

        modificationCounter++;
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
        private int count;

        public StackIterator(Node<Item> head) {
            current = head;
            count = modificationCounter;
        }

        public void checkComodification() {
            if (count != modificationCounter) {
                throw new ConcurrentModificationException();
            }
        }

        public boolean hasNext() {
            checkComodification();

            return current != null;
        }

        public Item next() {
            checkComodification();

            if (!hasNext())
                throw new NoSuchElementException();

            Item item = current.item;
            current = current.next;

            return item;
        }
    }

    public static void main(String[] args) {
        StackFailFast<String> stack = new StackFailFast<>();
        stack.push("A");
        stack.push("B");
        stack.push("C");

        StdOut.println("Iterating over stack...");
        var iterator = stack.iterator();
        StdOut.println(iterator.next()); // C
        StdOut.println(iterator.next()); // B

        // Modify stack during iteration
        StdOut.println("Pushing new element...");
        stack.push("D");

        try {
            // Next call should fail
            StdOut.println(iterator.next());
        } catch (ConcurrentModificationException e) {
            StdOut.println("Caught expected ConcurrentModificationException!");
        }
    }
}
