package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;

/******************************************************************************
 * Deque
 * 
 * <p>
 * A deque is a double-ended queue that allows for the insertion and removal of
 * elements at both the front and the back of the queue.
 * </p>
 * 
 * <pre>
 * ./gradlew run -PmainClass=Deque
 * </pre>
 ******************************************************************************/
public class Deque<Item> implements Iterable<Item> {
    private Node<Item> left;
    private Node<Item> right;
    private int size;

    public Deque() {
        this.left = null;
        this.right = null;
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

    public Item peekLeft() {
        if (isEmpty())
            throw new NoSuchElementException("Deque underflow");

        return left.item;
    }

    public Item peekRight() {
        if (isEmpty())
            throw new NoSuchElementException("Deque underflow");

        return right.item;
    }

    public void pushLeft(Item item) {
        if (isEmpty()) {
            left = new Node<Item>(item, null, null);
            right = left;
        } else {
            Node<Item> oldLeft = left;
            left = new Node<Item>(item, null, oldLeft);
            oldLeft.prev = left;
        }

        size++;
    }

    public void pushRight(Item item) {
        if (isEmpty()) {
            right = new Node<Item>(item, null, null);
            left = right;
        } else {
            Node<Item> oldRight = right;
            right = new Node<Item>(item, oldRight, null);
            oldRight.next = right;
        }

        size++;
    }

    public Item popLeft() {
        return removeNode(left);
    }

    public Item popRight() {
        return removeNode(right);
    }

    private Item removeNode(Node<Item> node) {
        if (isEmpty())
            throw new NoSuchElementException("Deque underflow");

        if (size == 1) {
            left = null;
            right = null;
        } else if (node == left) {
            left = node.next;
            left.prev = null;
        } else if (node == right) {
            right = node.prev;
            right.next = null;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        size--;

        return node.item;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();

        for (Item item : this) {
            s.append(item);
            s.append(' ');
        }

        return s.toString();
    }

    public Iterator<Item> iterator() {
        return new DequeIterator(left);
    }

    private class DequeIterator implements Iterator<Item> {
        private Node<Item> current;

        public DequeIterator(Node<Item> current) {
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
        Deque<String> deque = new Deque<>();
        deque.pushLeft("C");
        deque.pushLeft("B");
        deque.pushLeft("A");
        deque.pushRight("D");
        deque.pushRight("E");
        deque.pushRight("F");
        System.out.println(deque); // A B C D E F
    }
}
