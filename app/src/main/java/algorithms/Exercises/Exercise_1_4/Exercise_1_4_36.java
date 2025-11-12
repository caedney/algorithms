package algorithms.Exercises.Exercise_1_4;

/**
 * Exercise 1.4.36
 * 
 * <p>
 * <i>Space usage for pushdown stacks</i>. Justify the entries in the table
 * below, which shows typical space usage for various pushdown stack
 * implementations. Use a static nested class for linked-list nodes to avoid the
 * non-static nested class overhead.
 * </p>
 * 
 * <table>
 * <caption>Space usage in pushdown stacks (various implementations)</caption>
 * <thead>
 * <td>data structure</td>
 * <td>item type</td>
 * <td>space usage for <i>N</i> int values (bytes)</td> </thead> <tbody>
 * <tr>
 * <td rowspan=2><i>linked List</i></td>
 * <td><code>int</code></td>
 * <td>~32<i>N</i></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><code>Integer</code></td>
 * <td>~64<i>N</i></td>
 * </tr>
 * <tr>
 * <td rowspan=2><i>resisizing array</i></td>
 * <td><code>int</code></td>
 * <td>between ~4<i>N</i> and ~16<i>N</i></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><code>Integer</code></td>
 * <td>between ~32<i>N</i> and ~56<i>N</i></td>
 * </tr>
 * </tbody>
 * </table>
 */
public class Exercise_1_4_36 {
    public static void main(String[] args) {
    }
}
