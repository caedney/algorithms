package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    private Node<Item> first;
    private Node<Item> last;
    private int size;

    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    private static class Node<Item> {
        Item item;
        Node<Item> prev;
        Node<Item> next;

        Node(Item item, Node<Item> prev, Node<Item> next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public Item peekLeft() {
        if (isEmpty())
            throw new NoSuchElementException("Deque underflow");

        return first.item;
    }

    public Item peekRight() {
        if (isEmpty())
            throw new NoSuchElementException("Deque underflow");

        return last.item;
    }

    public void pushLeft(Item item) {
        if (isEmpty()) {
            first = new Node<Item>(item, null, null);
            last = first;
        } else {
            Node<Item> oldFirst = first;
            first = new Node<Item>(item, null, oldFirst);
            oldFirst.prev = first;
        }

        size++;
    }

    public void pushRight(Item item) {
        if (isEmpty()) {
            last = new Node<Item>(item, null, null);
            first = last;
        } else {
            Node<Item> oldLast = last;
            last = new Node<Item>(item, oldLast, null);
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

    private Item removeNode(Node<Item> node) {
        if (isEmpty())
            throw new NoSuchElementException("Deque underflow");

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
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node<Item> current = first;

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
        Deque<String> d = new Deque<>();
        d.pushLeft("A");
        d.pushLeft("B");
        d.pushLeft("C");
        d.pushLeft("D");
        d.pushLeft("E");
        d.pushLeft("F");
        d.popLeft();
        d.popLeft();

        StringJoiner joiner = new StringJoiner(", ", "[", "]");
        for (String value : d)
            joiner.add(String.valueOf(value));

        StdOut.println(joiner.toString()); // [D, C, B, A]

        Deque<String> e = new Deque<>();
        e.pushRight("A");
        e.pushRight("B");
        e.pushRight("C");
        e.pushRight("D");
        e.pushRight("E");
        e.pushRight("F");
        e.popRight();
        e.popRight();

        joiner = new StringJoiner(", ", "[", "]");
        for (String value : e)
            joiner.add(String.valueOf(value));

        StdOut.println(joiner.toString()); // [A, B, C, D]
    }
}
