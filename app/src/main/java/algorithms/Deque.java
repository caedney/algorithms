package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    private Node<Item> left;
    private Node<Item> right;
    private int size;

    public Deque() {
        this.left = null;
        this.right = null;
        this.size = 0;
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

        return left.item;
    }

    public Item peekRight() {
        if (isEmpty())
            throw new NoSuchElementException("Deque underflow");

        return right.item;
    }

    public void pushLeft(Item item) {
        if (isEmpty()) {
            left = new Node<Item>(item, null, null);
            right = left;
        } else {
            Node<Item> oldLeft = left;
            left = new Node<Item>(item, null, oldLeft);
            oldLeft.prev = left;
        }

        size++;
    }

    public void pushRight(Item item) {
        if (isEmpty()) {
            right = new Node<Item>(item, null, null);
            left = right;
        } else {
            Node<Item> oldRight = right;
            right = new Node<Item>(item, oldRight, null);
            oldRight.next = right;
        }

        size++;
    }

    public Item popLeft() {
        return removeNode(left);
    }

    public Item popRight() {
        return removeNode(right);
    }

    private Item removeNode(Node<Item> node) {
        if (isEmpty())
            throw new NoSuchElementException("Deque underflow");

        if (size == 1) {
            left = null;
            right = null;
        } else if (node == left) {
            left = node.next;
            left.prev = null;
        } else if (node == right) {
            right = node.prev;
            right.next = null;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
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
        return new DequeIterator(left);
    }

    private class DequeIterator implements Iterator<Item> {
        private Node<Item> current;

        public DequeIterator(Node<Item> current) {
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
        Deque<String> d = new Deque<>();
        d.pushLeft("A");
        d.pushLeft("B");
        d.pushLeft("C");
        d.pushLeft("D");
        d.pushLeft("E");
        d.pushLeft("F");
        d.popLeft();
        d.popLeft();

        StdOut.println(d.toString()); // [D, C, B, A]

        Deque<String> e = new Deque<>();
        e.pushRight("A");
        e.pushRight("B");
        e.pushRight("C");
        e.pushRight("D");
        e.pushRight("E");
        e.pushRight("F");
        e.popRight();
        e.popRight();

        StdOut.println(e.toString());// [A, B, C, D]
    }
}
