package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import edu.princeton.cs.algs4.StdOut;

public class GeneralizedQueue<Item> implements Iterable<Item> {
    private Node<Item> head;
    private Node<Item> tail;
    private int size;

    public GeneralizedQueue() {
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

    public void insert(Item item) {
        Node<Item> newNode = new Node<Item>(item, null);

        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }

        size++;
    }

    public Item delete(int k) {
        Node<Item> node = getNode(k - 1);
        return removeNode(node);
    }

    private Node<Item> getNode(int index) {
        if (index < 0 || index > size - 1)
            throw new ArrayIndexOutOfBoundsException("No available item found");

        Node<Item> current = head;

        for (int i = 0; current != null && i < index; i++) {
            current = current.next;
        }

        return current;
    }

    private Item removeNode(Node<Item> node) {
        if (isEmpty())
            throw new IllegalStateException("List is empty, cannot access item");

        if (size == 1) {
            head = null;
            tail = null;
        } else {
            Node<Item> current = head;

            while (current != null && current.next != node) {
                current = current.next;
            }

            current.next = node.next;
        }

        size--;

        return node.item;
    }

    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");

        for (Item value : this)
            joiner.add(String.valueOf(value));

        return joiner.toString();
    }

    public Iterator<Item> iterator() {
        return new GeneralizedQueueIterator(head);
    }

    private class GeneralizedQueueIterator implements Iterator<Item> {
        private Node<Item> current;

        public GeneralizedQueueIterator(Node<Item> current) {
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
        GeneralizedQueue<String> queue = new GeneralizedQueue<>();
        queue.insert("A");
        queue.insert("B");
        queue.insert("C");
        queue.insert("D");
        queue.insert("E");
        queue.insert("F");
        StdOut.println("Deleted item: " + queue.delete(2)); // B
        StdOut.println(queue.toString()); // [A, C, D, E, F]
    }
}
