package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import edu.princeton.cs.algs4.StdOut;

public class CircularQueue<Item> implements Iterable<Item> {
    private Node<Item> tail;
    private int size;

    public CircularQueue() {
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

        return tail.next.item;
    }

    public void enqueue(Item item) {
        Node<Item> newNode = new Node<Item>(item, null);

        if (isEmpty()) {
            newNode.next = newNode; // point to self
            tail = newNode; // update tail
        } else {
            newNode.next = tail.next; // point to head
            tail.next = newNode; // point head to new node
            tail = newNode; // update tail
        }

        size++;
    }

    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("Queue underflow");

        Node<Item> head = tail.next;

        if (size == 1) {
            tail = null; // empty list
        } else {
            tail.next = head.next; // bypass head
        }

        head.next = null; // avoid loitering
        size--;

        return head.item;
    }

    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");

        for (Item value : this)
            joiner.add(String.valueOf(value));

        return joiner.toString();
    }

    public Iterator<Item> iterator() {
        return new CircularQueueIterator(tail.next);
    }

    private class CircularQueueIterator implements Iterator<Item> {
        private Node<Item> current;
        private int index;

        public CircularQueueIterator(Node<Item> current) {
            this.current = current;
            this.index = 0;
        }

        public boolean hasNext() {
            return index < size;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            Item item = current.item;
            current = current.next;
            index++;

            return item;
        }
    }

    public static void main(String[] args) {
        CircularQueue<String> queue = new CircularQueue<>();
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
