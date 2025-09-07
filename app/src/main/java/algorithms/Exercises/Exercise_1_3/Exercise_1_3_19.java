package algorithms.Exercises.Exercise_1_3;

/**
 * Exercise 1.3.19
 * 
 * <p>
 * Give a code fragment that removes the last node in a linked list whose first
 * node is {@code first}.
 * </p>
 */
public class Exercise_1_3_19 {
    public class LinkedList<Item> {
        private Node first;

        private class Node {
            Node next;
        }

        public void removeLastNode() {
            if (first != null) {
                if (first.next == null) // only one node
                    first = null; // empty list
                else {
                    Node current = first;

                    while (current.next.next != null) // Traverse to the second-to-last node
                        current = current.next;

                    current.next = null; // Remove last node
                }
            }
        }

    }

    public static void main(String[] args) {
    }
}
