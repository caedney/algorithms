package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import edu.princeton.cs.algs4.StdOut;

public class Steque<Item> implements Iterable<Item> {
    private Node<Item> first;
    private Node<Item> last;
    private int size;

    public Steque() {
        this.first = null;
        this.last = null;
        this.size = 0;
    }

    private static class Node<Item> {
        Item item;
        Node<Item> next;

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
            throw new NoSuchElementException("Steque underflow");

        return first.item;
    }

    public void push(Item item) {
        Node<Item> newNode = new Node<Item>(item, first);
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
        Node<Item> newNode = new Node<Item>(item, null);

        if (isEmpty()) {
            first = newNode;
            last = newNode;
        } else {
            last.next = newNode;
            last = newNode;
        }

        size++;
    }

    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");

        for (Item value : this)
            joiner.add(String.valueOf(value));

        return joiner.toString();
    }

    public Iterator<Item> iterator() {
        return new StequeIterator();
    }

    private class StequeIterator implements Iterator<Item> {
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
        Steque<String> steque = new Steque<>();
        steque.push("C");
        steque.push("D");
        steque.push("E");
        steque.push("F");
        steque.pop();
        steque.pop();
        steque.enqueue("B");
        steque.enqueue("A");

        StdOut.println(steque.toString()); // [D, C, B, A]
    }
}
