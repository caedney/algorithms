package algorithms.Exercises.Exercise_1_3;

/**
 * Exercise 1.3.30
 * 
 * <p>
 * Write a function that takes the first {@code Node} in a linked list as
 * argument and (destructively) reverses the list, returning the first
 * {@code Node} in the result.
 * </p>
 * 
 * <p>
 * <i>Iterative solution</i>: To accomplish this task, we maintain references to
 * three consecutive nodes in the linked list, {@code reverse}, {@code first},
 * and {@code second}. At each iteration, we extract the node {@code first} from
 * the original linked list and insert it at the beginning of the reversed list.
 * We maintain the invariant that {@code first} is the first node of what's left
 * of the original list, {@code second} is the second node of what's left of the
 * original list, and {@code reverse} is the first node of the resulting
 * reversed list.
 * </p>
 * 
 * {@snippet :
 * public Node reverse(Node x) {
 *     Node first = x;
 *     Node reverse = null;
 *     while (first != null) {
 *         Node second = first.next;
 *         first.next = reverse;
 *         reverse = first;
 *         first = second;
 *     }
 *     return reverse;
 * }
 * }
 * 
 * <p>
 * When writing code involving linked lists, we must always be careful to
 * properly handle the exceptional cases (when the linked list is empty, when
 * the list has only one or two nodes) and the boundary cases (dealing with the
 * first or last items). This is usually much trickier than handling the normal
 * cases.
 * </p>
 * 
 * <p>
 * <i>Recursive solution</i>: Assuming the linked list has 𝑁 nodes, we
 * recursively reverse the last 𝑁 - 1 nodes, and then carefully append the
 * first node to the end.
 * </p>
 * 
 * {@snippet :
 * public Node reverse(Node x) {
 *     if (first == null)
 *         return null;
 *     if (first.next == null)
 *         return first;
 *     Node second = first.next;
 *     second.next = first;
 *     first.next = null;
 *     return rest;
 * }
 * }
 */
public class Exercise_1_3_30 {
    public static void main(String[] args) {
    }
}
