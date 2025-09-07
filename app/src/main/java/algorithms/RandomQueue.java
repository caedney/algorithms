package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.StringJoiner;

import edu.princeton.cs.algs4.StdOut;

@SuppressWarnings("unchecked")
public class RandomQueue<Item> implements Iterable<Item> {
    private static final int INIT_CAPACITY = 1;
    private Item[] array;
    private int size;
    private Random random;

    public RandomQueue() {
        this.array = (Item[]) new Object[INIT_CAPACITY];
        this.size = 0;
        this.random = new Random();
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return array.length;
    }

    private void resize(int capacity) {
        Item[] newArray = (Item[]) new Object[capacity];

        for (int i = 0; i < size; i++)
            newArray[i] = array[i];

        array = newArray;
    }

    public void enqueue(Item item) {
        if (size == array.length)
            resize(2 * array.length);

        array[size++] = item;
    }

    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException("Queue underflow");

        int x = random.nextInt(size);
        Item item = array[x];
        array[x] = array[size - 1];
        array[size - 1] = null;
        size--;

        if (size > 0 && size == array.length / 4)
            resize(array.length / 2);

        return item;
    }

    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException("Queue underflow");

        int x = random.nextInt(size);

        return array[x];
    }

    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");

        for (Item value : this)
            joiner.add(String.valueOf(value));

        return joiner.toString();
    }

    public Iterator<Item> iterator() {
        return new RandomQueueIterator();
    }

    private class RandomQueueIterator implements Iterator<Item> {
        private int index;

        public RandomQueueIterator() {
            this.index = size;
        }

        public boolean hasNext() {
            return index > 0;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            return array[--index];
        }
    }

    public static void main(String[] args) {
        RandomQueue<Card> deck = new RandomQueue<>();

        for (Card card : Card.standardDeck())
            deck.enqueue(card);

        for (Card card : deck)
            StdOut.print(card + " ");

        StdOut.println();
    }
}
