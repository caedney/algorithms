package algorithms;

public class CircularQueue<Item> {
    private Node back;
    private int size;

    public CircularQueue() {
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

    public Item peek() {
        if (isEmpty()) {
            throw new RuntimeException("Queue underflow");
        }

        return back.next.item;
    }

    public void enqueue(Item item) {
        Node newNode = new Node(item, null);

        if (isEmpty()) {
            newNode.next = newNode; // point to self
            back = newNode; // update back
        } else {
            newNode.next = back.next; // point to front
            back.next = newNode; // point front to new node
            back = newNode; // update back
        }

        size++;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Queue underflow");
        }

        Node first = back.next;
        Item item = first.item;

        if (size == 1) {
            back = null;
        } else {
            back.next = first.next; // bypass first
        }

        size--;

        return item;
    }
}
