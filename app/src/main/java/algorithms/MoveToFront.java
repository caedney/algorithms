package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class MoveToFront implements Iterable<Character> {
    private Node front;

    public MoveToFront() {
        front = null;
    }

    private class Node {
        char item;
        Node next;

        Node(char item, Node next) {
            this.item = item;
            this.next = next;
        }
    }

    public boolean isEmpty() {
        return front == null;
    }

    private Node delete(Node x, char item) {
        if (x == null)
            return null;

        if (x.item == item)
            return x.next;

        x.next = delete(x.next, item);

        return x;
    }

    private int find(char item) {
        int i = 1;

        for (Node x = front; x != null; x = x.next) {
            if (x.item == item)
                return i;
            i++;
        }

        return 0;
    }

    public int add(char item) {
        int location = find(item);
        front = delete(front, item);
        front = new Node(item, front);

        return location;
    }

    public Iterator<Character> iterator() {
        return new GeneralizedQueueIterator(front);
    }

    private class GeneralizedQueueIterator implements Iterator<Character> {
        private Node current;

        public GeneralizedQueueIterator(Node front) {
            current = front;
        }

        public boolean hasNext() {
            return current != null;
        }

        public Character next() {
            if (!hasNext())
                throw new NoSuchElementException();

            Character item = current.item;
            current = current.next;

            return item;
        }
    }
}
