package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class DoublyLinkedList<Item> implements Iterable<Item> {
    private Node<Item> head;
    private Node<Item> tail;
    private int size;

    public DoublyLinkedList() {
        this.head = null;
        this.tail = null;
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

    public Item peekHead() {
        if (isEmpty())
            throw new NoSuchElementException("Queue underflow");

        return head.item;
    }

    public Item peekTail() {
        if (isEmpty())
            throw new NoSuchElementException("Queue underflow");

        return tail.item;
    }

    public void append(Item item) {
        if (isEmpty()) {
            tail = new Node<Item>(item, null, null);
            head = tail;
        } else {
            Node<Item> oldTail = tail;
            tail = new Node<Item>(item, oldTail, null);
            oldTail.next = tail;
        }

        size++;
    }

    public void prepend(Item item) {
        if (isEmpty()) {
            head = new Node<Item>(item, null, null);
            tail = head;
        } else {
            Node<Item> oldHead = head;
            head = new Node<Item>(item, null, oldHead);
            oldHead.prev = head;
        }

        size++;
    }

    public void insertBefore(int index, Item item) {
        Node<Item> node = getNode(index);

        if (node == head) {
            Node<Item> oldHead = head;
            head = new Node<Item>(item, null, oldHead);
            oldHead.prev = head;
        } else {
            Node<Item> newNode = new Node<Item>(item, node.prev, node);
            node.prev.next = newNode;
            node.prev = newNode;
        }

        size++;
    }

    public void insertAfter(int index, Item item) {
        Node<Item> node = getNode(index);

        if (node == tail) {
            Node<Item> oldTail = tail;
            tail = new Node<Item>(item, oldTail, null);
            oldTail.next = tail;
        } else {
            Node<Item> newNode = new Node<Item>(item, node, node.next);
            node.next.prev = newNode;
            node.next = newNode;
        }

        size++;
    }

    public Item remove(int index) {
        Node<Item> node = getNode(index);
        return removeNode(node);
    }

    public Item removeHead() {
        return removeNode(head);
    }

    public Item removeTail() {
        return removeNode(tail);
    }

    private Item removeNode(Node<Item> node) {
        if (isEmpty())
            throw new IllegalStateException("List is empty, cannot access item");

        if (size == 1) {
            head = null;
            tail = null;
        } else if (node == head) {
            head = node.next;
            head.prev = null;
        } else if (node == tail) {
            tail = node.prev;
            tail.next = null;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        size--;

        return node.item;
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

    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");

        for (Item value : this)
            joiner.add(String.valueOf(value));

        return joiner.toString();
    }

    public Iterator<Item> iterator() {
        return new DoublyLinkedListIterator(head);
    }

    private class DoublyLinkedListIterator implements Iterator<Item> {
        private Node<Item> current;

        public DoublyLinkedListIterator(Node<Item> current) {
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
        In in = new In("src/main/resources/data/keys.txt");
        DoublyLinkedList<Integer> list = new DoublyLinkedList<Integer>();

        while (!in.isEmpty()) {
            int item = in.readInt();
            list.append(item);
        }

        StdOut.println(list.peekHead());
        StdOut.println(list.peekTail());
        StdOut.println(list.toString());
    }
}
