package algorithms;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

@SuppressWarnings("unchecked")
public class RandomQueue<Item> implements Iterable<Item> {
    private static final int INIT_CAPACITY = 1;
    private Item[] array;
    private int size;
    private Random random;

    public RandomQueue() {
        array = (Item[]) new Object[INIT_CAPACITY];
        size = 0;
        random = new Random();
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

    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException("Queue underflow");

        int x = random.nextInt(size);
        return array[x];
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

    public Iterator<Item> iterator() {
        return new RandomQueueIterator();
    }

    private class RandomQueueIterator implements Iterator<Item> {
        private int index;
        private final Item[] copy;

        public RandomQueueIterator() {
            index = size;
            copy = (Item[]) new Object[size];

            for (int i = 0; i < size; i++)
                copy[i] = array[index];

            for (int i = 0; i < size; i++) {
                int x = random.nextInt(i + 1);
                Item temp = copy[i];
                copy[i] = copy[x];
                copy[x] = temp;
            }
        }

        public boolean hasNext() {
            return index > 0;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            return copy[--index];
        }
    }
}
