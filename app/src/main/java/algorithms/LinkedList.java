package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import edu.princeton.cs.algs4.StdOut;

public class LinkedList<Item> implements Iterable<Item> {
    private Node<Item> head;
    private Node<Item> tail;
    private int size;

    public LinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
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
        Node<Item> oldTail = tail;
        tail = new Node<Item>(item, null);

        if (isEmpty())
            head = tail;
        else
            oldTail.next = tail;

        size++;
    }

    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("Queue underflow");

        Item item = head.item;
        head = head.next;
        size--;

        if (isEmpty())
            tail = null; // to avoid loitering

        return item;
    }

    public void delete(int k) {
        if (k <= 0 || k > size())
            throw new ArrayIndexOutOfBoundsException("No item found");

        if (isEmpty())
            throw new NoSuchElementException("Queue underflow");

        if (k == 1) {
            head = head.next;
        } else {
            Node<Item> current = head;
            for (int i = 1; current != null && i < k - 1; i++) {
                current = current.next;
            }

            if (current != null) {
                current.next = current.next.next;
            }
        }
    }

    public void remove(String key) {
        // Remove nodes from the beginning if they match
        while (head != null && head.item.equals(key)) {
            head = head.next;
        }

        // Traverse and remove matching nodes
        Node<Item> current = head;
        while (current != null && current.next != null) {
            if (current.next.item.equals(key)) {
                current.next = current.next.next; // skip node
            } else {
                current = current.next;
            }
        }
    }

    public void removeLastNode() {
        if (isEmpty())
            throw new NoSuchElementException("Queue underflow");

        if (size == 1)
            head = null;
        else {
            Node<Item> current = head;

            while (current.next.next != null) // Traverse to the second-to-last node
                current = current.next;

            current.next = null; // Remove last node
        }
    }

    public int max() {
        if (!(head.item instanceof Integer)) {
            return 0;
        }

        return maxRecursive(head);
    }

    private int maxRecursive(Node<Item> node) {
        if (node == null) {
            return 0;
        }

        int currentVal = (Integer) node.item;
        int restMax = maxRecursive(node.next);

        return Math.max(currentVal, restMax);
    }

    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");

        for (Item value : this)
            joiner.add(String.valueOf(value));

        return joiner.toString();
    }

    public Iterator<Item> iterator() {
        return new LinkedListIterator(head);
    }

    private class LinkedListIterator implements Iterator<Item> {
        private Node<Item> current;

        public LinkedListIterator(Node<Item> current) {
            this.current = current;
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

    public static void main(String[] args) {
        LinkedList<String> queue = new LinkedList<>();
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        queue.enqueue("D");
        queue.enqueue("E");
        queue.enqueue("F");
        queue.dequeue();
        queue.dequeue();

        StdOut.println(queue.toString()); // [C, D, E, F]
    }
}
