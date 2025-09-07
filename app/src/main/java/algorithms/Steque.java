package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Steque<Item> implements Iterable<Item> {
    private int size;
    private Node first;
    private Node last;

    class Node {
        Item item;
        Node next;

        Node(Item item, Node next) {
            this.item = item;
            this.next = next;
        }
    }

    public Steque() {
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
            throw new NoSuchElementException("Steque underflow");

        return first.item;
    }

    public void push(Item item) {
        Node newNode = new Node(item, first);
        first = newNode;

        if (last == null)
            last = first;

        size++;
    }

    public Item pop() {
        if (isEmpty())
            throw new NoSuchElementException("Steque underflow");

        Item item = first.item;
        first = first.next;

        if (first == null)
            last = null;

        size--;

        return item;
    }

    public void enqueue(Item item) {
        Node newNode = new Node(item, null);

        if (isEmpty()) {
            first = newNode;
            last = newNode;
        } else {
            last.next = newNode;
            last = newNode;
        }

        size++;
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

    public static void main(String[] args) {
        Steque<String> list = new Steque<String>();

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-"))
                list.enqueue(item);
        }

        StdOut.println("(" + list.size() + " left on list)");
    }
}
