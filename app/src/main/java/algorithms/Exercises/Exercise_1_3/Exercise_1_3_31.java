package algorithms.Exercises.Exercise_1_3;

import algorithms.DoublyLinkedList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.31
 * 
 * <p>
 * Implement a nested class {@code DoubleNode} for building doubly-linked lists,
 * where each node contains a reference to the item preceding it and the item
 * following it in the list ({@code null} if there is no such item). Then
 * implement static methods for the following tasks: insert at the beginning,
 * insert at the end, remove from the beginning, remove from the end, insert
 * before a given node, insert after a given node, and remove a given node.
 * </p>
 */
public class Exercise_1_3_31 {
    public static void main(String[] args) {
        In in = new In("src/main/resources/data/keys.txt");
        DoublyLinkedList<Integer> list = new DoublyLinkedList<Integer>();

        while (!in.isEmpty()) {
            int item = in.readInt();
            list.append(item);
        }

        StdOut.println("first item: " + list.peekHead());
        StdOut.println("last item: " + list.peekTail());
        StdOut.println("removed first item: " + list.removeHead());
        StdOut.println("removed last item: " + list.removeTail());
        StdOut.println("removed item: " + list.remove(9));

        list.insertBefore(1, 5768);
        list.insertAfter(1, 3546);

        StdOut.println(list);
    }
}
