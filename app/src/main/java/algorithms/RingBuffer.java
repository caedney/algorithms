package algorithms;

public interface RingBuffer<Item> {
    boolean isEmpty();

    boolean isFull();

    int size();

    int capacity();

    Item peek();

    void enqueue(Item item);

    Item dequeue();
}
