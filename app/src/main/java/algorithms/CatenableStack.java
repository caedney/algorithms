package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import edu.princeton.cs.algs4.StdOut;

public class CatenableStack<Item> implements Iterable<Item> {
    private Node<Item> tail;
    private int size;

    public CatenableStack() {
        tail = null;
        size = 0;
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

    public Item peek() {
        if (isEmpty())
            throw new RuntimeException("Stack underflow");

        return tail.next.item;
    }

    public void push(Item item) {
        Node<Item> newNode = new Node<Item>(item, null);

        if (isEmpty()) {
            newNode.next = newNode; // point to self
            tail = newNode; // update tail
        } else {
            newNode.next = tail.next; // point to head
            tail.next = newNode; // point head to new node
        }

        size++;
    }

    public Item pop() {
        if (isEmpty())
            throw new RuntimeException("Stack underflow");

        Node<Item> head = tail.next;

        if (size == 1) {
            tail = null; // empty list
        } else {
            tail.next = head.next; // bypass head
        }

        size--;

        return head.item;
    }

    public void catenate(CatenableStack<Item> that) {
        if (that.isEmpty())
            return;

        if (this.isEmpty()) {
            this.tail = that.tail;
            this.size = that.size;
            return;
        }

        Node<Item> thisHead = this.tail.next; // head of this
        Node<Item> thatHead = that.tail.next; // head of that

        this.tail.next = thatHead; // last node of 'this' points to head of 'that' for circular link
        that.tail.next = thisHead; // last node of 'that' points to head of 'this'
        this.size += that.size;

        that.tail = null;
        that.size = 0;
    }

    public Iterator<Item> iterator() {
        return new CatenableStackIterator(tail.next);
    }

    private class CatenableStackIterator implements Iterator<Item> {
        private Node<Item> current;
        private int index;

        public CatenableStackIterator(Node<Item> tail) {
            current = tail;
            index = 0;
        }

        public boolean hasNext() {
            return index < size;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            Item item = current.item;
            current = current.next;
            index++;

            return item;
        }
    }

    public static void main(String[] args) {
        CatenableStack<String> stack = new CatenableStack<>();
        stack.push("A");
        stack.push("B");
        stack.push("C");
        stack.push("D");
        stack.push("E");
        stack.push("F");
        stack.pop();
        stack.pop();

        StringJoiner joiner = new StringJoiner(", ", "[", "]");
        for (String value : stack)
            joiner.add(String.valueOf(value));

        StdOut.println(joiner.toString()); // [D, C, B, A]
    }
}
