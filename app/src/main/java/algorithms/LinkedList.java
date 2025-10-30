package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import edu.princeton.cs.algs4.StdOut;

public class LinkedList<Item> implements Iterable<Item> {
    private Node<Item> head;
    private int size;

    public LinkedList() {
        this.head = null;
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

    public void append(Item item) {
        Node<Item> node = new Node<Item>(item, null);

        if (isEmpty()) {
            head = node;
        } else {
            Node<Item> currentNode = head;

            while (currentNode.next != null)
                currentNode = currentNode.next;

            currentNode.next = node;
        }

        size++;
    }

    public void removeFirstNode() {
        if (isEmpty())
            throw new NoSuchElementException("List is empty, cannot access item");

        if (size == 1)
            head = null;
        else {
            head = head.next;
        }

        size--;
    }

    public void removeLastNode() {
        if (isEmpty())
            throw new NoSuchElementException("List is empty, cannot access item");

        if (size == 1)
            head = null;
        else {
            Node<Item> current = head;

            while (current.next.next != null)
                current = current.next;

            current.next = null;
        }

        size--;
    }

    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");

        for (Item value : this)
            joiner.add(String.valueOf(value));

        return joiner.toString();
    }

    public Iterator<Item> iterator() {
        return new LinkedListIterator(head);
    }

    private class LinkedListIterator implements Iterator<Item> {
        private Node<Item> current;

        public LinkedListIterator(Node<Item> current) {
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
        LinkedList<String> list = new LinkedList<>();
        list.append("A");
        list.append("B");
        list.append("C");
        list.append("D");
        list.append("E");
        list.append("F");
        list.removeFirstNode();
        list.removeFirstNode();
        list.removeLastNode();
        StdOut.println(list); // [C, D, E]
    }
}
