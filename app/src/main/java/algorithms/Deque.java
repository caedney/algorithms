package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int size;
    private Node first;
    private Node last;

    class Node {
        Item item;
        Node prev;
        Node next;

        Node(Item item, Node prev, Node next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    public Deque() {
        size = 0;
        first = null;
        last = null;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public Item peek() {
        if (isEmpty())
            throw new NoSuchElementException("Deque underflow");

        return first.item;
    }

    public void pushLeft(Item item) {
        if (isEmpty()) {
            first = new Node(item, null, null);
            last = first;
        } else {
            Node oldFirst = first;
            first = new Node(item, null, oldFirst);
            oldFirst.prev = first;
        }

        size++;
    }

    public void pushRight(Item item) {
        if (isEmpty()) {
            last = new Node(item, null, null);
            first = last;
        } else {
            Node oldLast = last;
            last = new Node(item, oldLast, null);
            oldLast.next = last;
        }

        size++;
    }

    public Item popLeft() {
        return removeNode(first);
    }

    public Item popRight() {
        return removeNode(last);
    }

    private Item removeNode(Node node) {
        if (isEmpty())
            throw new IllegalStateException("List is empty, cannot access item");

        if (size == 1) {
            first = null;
            last = null;
        } else if (node == first) {
            first = node.next;
            first.prev = null;
        } else if (node == last) {
            last = node.prev;
            last.next = null;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        size--;

        return node.item;
    }

    public Iterator<Item> iterator() {
        return new LinkedIterator();
    }

    private class LinkedIterator implements Iterator<Item> {
        private Node current = first;

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
