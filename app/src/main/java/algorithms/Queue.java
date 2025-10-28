package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import edu.princeton.cs.algs4.StdOut;

public class Queue<Item> implements Iterable<Item> {
    private Node<Item> head;
    private Node<Item> tail;
    private int size;

    public Queue() {
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

    public Item peek() {
        if (isEmpty())
            throw new NoSuchElementException("Queue underflow");

        return head.item;
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
            tail = null; // avoid loitering

        return item;
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
        return new QueueIterator(head);
    }

    private class QueueIterator implements Iterator<Item> {
        private Node<Item> current;

        public QueueIterator(Node<Item> current) {
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
        Queue<String> queue = new Queue<>();
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        queue.enqueue("D");
        queue.enqueue("E");
        queue.enqueue("F");
        queue.dequeue();
        queue.dequeue();

        StringJoiner joiner = new StringJoiner(", ", "[", "]");
        for (String value : queue)
            joiner.add(String.valueOf(value));

        StdOut.println(joiner.toString()); // [C, D, E, F]
    }
}
