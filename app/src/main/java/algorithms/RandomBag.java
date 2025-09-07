package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

@SuppressWarnings("unchecked")
public class RandomBag<Item> implements Iterable<Item> {
    private Node first;
    private int size;
    private Random random;

    public RandomBag() {
        first = null;
        size = 0;
        random = new Random();
    }

    private class Node {
        Item item;
        Node next;

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

    public void add(Item item) {
        Node oldFirst = first;
        first = new Node(item, oldFirst);
        size++;
    }

    public Iterator<Item> iterator() {
        return new RandomBagIterator();
    }

    private class RandomBagIterator implements Iterator<Item> {
        private Item[] array = (Item[]) new Object[size];
        private int index = 0;

        public RandomBagIterator() {
            Node current = first;
            int i = 0;

            while (current != null) {
                array[i++] = current.item;
                current = current.next;
            }

            // Fisher–Yates shuffle
            for (int j = array.length - 1; j > 0; j--) {
                int x = random.nextInt(j + 1);
                Item temp = array[j];
                array[j] = array[x];
                array[x] = temp;
            }
        }

        public boolean hasNext() {
            return index < array.length;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            return array[index++];
        }
    }
}
