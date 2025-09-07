package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Queue<Item> implements Iterable<Item> {
    private Node<Item> front;
    private Node<Item> back;
    private int size;

    public Queue() {
        front = null;
        back = null;
        size = 0;
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

        return front.item;
    }

    public void enqueue(Item item) {
        Node<Item> oldBack = back;
        back = new Node<Item>(item, null);

        if (isEmpty())
            front = back;
        else
            oldBack.next = back;

        size++;
    }

    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("Queue underflow");

        Item item = front.item;
        front = front.next;
        size--;

        if (isEmpty())
            back = null; // avoid loitering

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
        return new QueueIterator(front);
    }

    private class QueueIterator implements Iterator<Item> {
        private Node<Item> current;

        public QueueIterator(Node<Item> first) {
            current = first;
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
        Queue<String> queue = new Queue<String>();

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-"))
                queue.enqueue(item);
            else if (!queue.isEmpty())
                StdOut.print(queue.dequeue() + " ");
        }

        StdOut.println("(" + queue.size() + " left on queue)");
    }
}
