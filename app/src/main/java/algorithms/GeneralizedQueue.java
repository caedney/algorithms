package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class GeneralizedQueue<Item> implements Iterable<Item> {
    private Node front;
    private Node back;
    private int size;

    public GeneralizedQueue() {
        front = null;
        back = null;
        size = 0;
    }

    private class Node {
        private Item item;
        private Node next;

        Node(Item item, Node next) {
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

    public void insert(Item item) {
        Node newNode = new Node(item, null);

        if (isEmpty()) {
            front = newNode;
            back = newNode;
        } else {
            back.next = newNode;
            back = newNode;
        }

        size++;
    }

    public Item delete(int k) {
        Node node = getNode(k - 1);
        return removeNode(node);
    }

    private Node getNode(int index) {
        if (index < 0 || index > size - 1)
            throw new ArrayIndexOutOfBoundsException("No available item found");

        Node current = front;

        for (int i = 0; current != null && i < index; i++) {
            current = current.next;
        }

        return current;
    }

    private Item removeNode(Node node) {
        if (isEmpty())
            throw new IllegalStateException("List is empty, cannot access item");

        if (size == 1) {
            front = null;
            back = null;
        } else {
            Node current = front;

            while (current != null && current.next != node) {
                current = current.next;
            }

            current.next = node.next;
        }

        size--;

        return node.item;
    }

    public Iterator<Item> iterator() {
        return new GeneralizedQueueIterator(front);
    }

    private class GeneralizedQueueIterator implements Iterator<Item> {
        private Node current;

        public GeneralizedQueueIterator(Node front) {
            current = front;
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
}
