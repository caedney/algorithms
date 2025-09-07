package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class DoublyLinkedList<Item> implements Iterable<Item> {
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

    public DoublyLinkedList() {
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

    public Item peekFirst() {
        if (isEmpty())
            throw new NoSuchElementException("Queue underflow");

        return first.item;
    }

    public Item peekLast() {
        if (isEmpty())
            throw new NoSuchElementException("Queue underflow");

        return last.item;
    }

    public void append(Item item) {
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

    public void prepend(Item item) {
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

    public void insertBefore(int index, Item item) {
        Node node = getNode(index);

        if (node == first) {
            Node oldFirst = first;
            first = new Node(item, null, oldFirst);
            oldFirst.prev = first;
        } else {
            Node newNode = new Node(item, node.prev, node);
            node.prev.next = newNode;
            node.prev = newNode;
        }

        size++;
    }

    public void insertAfter(int index, Item item) {
        Node node = getNode(index);

        if (node == last) {
            Node oldLast = last;
            last = new Node(item, oldLast, null);
            oldLast.next = last;
        } else {
            Node newNode = new Node(item, node, node.next);
            node.next.prev = newNode;
            node.next = newNode;
        }

        size++;
    }

    public Item remove(int index) {
        Node node = getNode(index);
        return removeNode(node);
    }

    public Item removeFirst() {
        return removeNode(first);
    }

    public Item removeLast() {
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

    private Node getNode(int index) {
        if (index < 0 || index > size - 1)
            throw new ArrayIndexOutOfBoundsException("No available item found");

        Node current = first;

        for (int i = 0; current != null && i < index; i++) {
            current = current.next;
        }

        return current;
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
        In in = new In("src/main/resources/data/keys.txt");
        DoublyLinkedList<Integer> list = new DoublyLinkedList<Integer>();

        while (!in.isEmpty()) {
            int item = in.readInt();
            list.append(item);
        }

        StdOut.println(list.peekFirst());
        StdOut.println(list.peekLast());

        for (int value : list) {
            StdOut.print(value + " ");
        }
    }
}
