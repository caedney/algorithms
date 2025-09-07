package algorithms.Exercises.Exercise_1_3;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.24
 * 
 * <p>
 * Write a method {@code removeAfter()} that takes a linked-list {@code Node} as
 * argument and removes the node following the given one (and does nothing if
 * the argument or the next field in the argument node is null).
 * </p>
 */
public class Exercise_1_3_24 {
    public static class Node {
        String item;
        Node next;
    }

    public static void removeAfter(Node node) {
        if (node != null && node.next != null) {
            node.next = node.next.next;
        }
    }

    public static void printList(Node node) {
        while (node != null) {
            StdOut.print(node.item + " ");
            node = node.next;
        }

        StdOut.println();
    }

    public static void main(String[] args) {
        Node first = new Node();
        first.item = "to";

        Node second = new Node();
        second.item = "be";
        first.next = second;

        Node third = new Node();
        third.item = "or";
        second.next = third;

        Node fourth = new Node();
        fourth.item = "not";
        third.next = fourth;

        printList(first); // to be or not
        removeAfter(second);
        printList(first); // to be not
    }
}
