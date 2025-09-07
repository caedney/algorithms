package algorithms;

public class CircularQueue<Item> {
    private Node last; // beginning and end of queue
    private int size; // number of elements on queue

    private class Node {
        private Item item;
        private Node next;
    }

    public CircularQueue() {
        last = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public Item peek() {
        if (isEmpty()) {
            throw new RuntimeException("Queue underflow");
        }

        return last.next.item;
    }

    public void enqueue(Item item) {
        Node newNode = new Node();
        newNode.item = item;

        if (isEmpty()) {
            newNode.next = newNode; // points to itself
            last = newNode;
        } else {
            newNode.next = last.next; // new node points to first
            last.next = newNode; // old last points to new node
            last = newNode; // update last
        }

        size++;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Queue underflow");
        }

        Node first = last.next;
        Item item = first.item;

        if (first == last) { // only one element
            last = null;
        } else {
            last.next = first.next; // bypass first
        }

        size--;

        return item;
    }
}
