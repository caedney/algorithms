package algorithms.Exercises.Exercise_1_3;

import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.25
 * 
 * <p>
 * Write a method {@code insertAfter()} that takes two linked-list {@code Node}
 * arguments and inserts the second after the first on its list (and does
 * nothing if either argument is null).
 * </p>
 */
public class Exercise_1_3_25 {
    public static class Node {
        String item;
        Node next;
    }

    public static void insertAfter(Node first, Node second) {
        if (first != null || second != null) {
            first.next = second;
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
        insertAfter(first, third);
        printList(first); // to or not
    }
}
