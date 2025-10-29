package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.StringJoiner;

import edu.princeton.cs.algs4.StdOut;

@SuppressWarnings("unchecked")
public class RandomBag<Item> implements Iterable<Item> {
    private Node<Item> head;
    private int size;
    private Random random;

    public RandomBag() {
        this.head = null;
        this.size = 0;
        this.random = new Random();
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

    public void add(Item item) {
        Node<Item> oldHead = head;
        head = new Node<Item>(item, oldHead);
        size++;
    }

    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");

        for (Item value : this)
            joiner.add(String.valueOf(value));

        return joiner.toString();
    }

    public Iterator<Item> iterator() {
        return new RandomBagIterator();
    }

    private class RandomBagIterator implements Iterator<Item> {
        private Item[] array;
        private int index;

        public RandomBagIterator() {
            this.array = (Item[]) new Object[size];
            this.index = 0;

            Node<Item> current = head;
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

    public static void main(String[] args) {
        RandomBag<String> randomBag = new RandomBag<String>();
        randomBag.add("A");
        randomBag.add("B");
        randomBag.add("C");
        randomBag.add("D");
        randomBag.add("E");
        randomBag.add("F");

        StdOut.println(randomBag.toString());
    }
}
