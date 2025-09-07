package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class LinkedList<Item> implements Iterable<Item> {
    private int n;
    private Node first;
    private Node last;

    class Node {
        Item item;
        Node next;
    }

    public LinkedList() {
        first = null;
        last = null;
        n = 0;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return n;
    }

    public void enqueue(Item item) {
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;

        if (isEmpty())
            first = last;
        else
            oldlast.next = last;

        n++;
    }

    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("Queue underflow");

        Item item = first.item;
        first = first.next;
        n--;

        if (isEmpty())
            last = null; // to avoid loitering

        return item;
    }

    public void delete(int k) {
        if (k <= 0 || k > size())
            throw new ArrayIndexOutOfBoundsException("No item found");

        if (isEmpty())
            throw new IllegalStateException("List is empty, cannot access item");

        if (k == 1) {
            first = first.next;
        } else {
            Node current = first;
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
        while (first != null && first.item.equals(key)) {
            first = first.next;
        }

        // Traverse and remove matching nodes
        Node current = first;
        while (current != null && current.next != null) {
            if (current.next.item.equals(key)) {
                current.next = current.next.next; // skip node
            } else {
                current = current.next;
            }
        }
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
        LinkedList<String> list = new LinkedList<String>();

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-"))
                list.enqueue(item);
            else if (!list.isEmpty())
                StdOut.print(list.dequeue() + " ");
        }

        StdOut.println("(" + list.size() + " left on list)");
    }
}
