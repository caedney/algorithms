package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import edu.princeton.cs.algs4.StdOut;

public class LinkedStack<Item> implements Iterable<Item> {
    private Node<Item> head;
    private int size;

    public LinkedStack() {
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

    public void push(Item item) {
        Node<Item> oldHead = head;
        head = new Node<Item>(item, oldHead);

        size++;
    }

    public Item pop() {
        if (isEmpty())
            throw new NoSuchElementException("Stack underflow");

        Item item = head.item;
        head = head.next;
        size--;

        return item;
    }

    public Item peek() {
        if (isEmpty())
            throw new NoSuchElementException("Stack underflow");

        return head.item;
    }

    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");

        for (Item value : this)
            joiner.add(String.valueOf(value));

        return joiner.toString();
    }

    public Iterator<Item> iterator() {
        return new LinkedStackIterator(head);
    }

    private class LinkedStackIterator implements Iterator<Item> {
        private Node<Item> current;

        public LinkedStackIterator(Node<Item> current) {
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
        Stack<String> stack = new Stack<>();
        stack.push("A");
        stack.push("B");
        stack.push("C");
        stack.push("D");
        stack.push("E");
        stack.push("F");
        stack.pop();
        stack.pop();

        StdOut.println(stack.toString()); // [D, C, B, A]
    }
}
