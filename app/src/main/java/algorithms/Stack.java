package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;

/******************************************************************************
 * Stack
 * 
 * <p>
 * A stack is a last-in-first-out (LIFO) data structure that allows for the
 * insertion and removal of elements at the top of the stack.
 * </p>
 * 
 * <pre>
 * ./gradlew run -PmainClass=Stack
 * </pre>
 ******************************************************************************/
public class Stack<Item> implements Iterable<Item> {
    private Node<Item> head;
    private int size;

    public Stack() {
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

    public Item peek() {
        if (isEmpty())
            throw new NoSuchElementException("Stack underflow");

        return head.item;
    }

    public void push(Item item) {
        Node<Item> oldHead = head;
        head = new Node<Item>(item, oldHead);

        size++;
    }

    public Item pop() {
        if (isEmpty())
            throw new NoSuchElementException("Stack underflow");

        Item item = head.item; // save item to return
        head = head.next; // delete head node
        size--;

        return item; // return the saved item
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
        return new StackIterator(head);
    }

    private class StackIterator implements Iterator<Item> {
        private Node<Item> current;

        public StackIterator(Node<Item> current) {
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
        stack.push("F");
        stack.push("E");
        stack.push("D");
        stack.push("C");
        stack.push("B");
        stack.push("A");
        System.out.println(stack); // A B C D E F
    }
}
