package algorithms.Exercises.Exercise_1_4;

/**
 * Exercise 1.4.35
 * 
 * <p>
 * <i>Time costs for pushdown stacks</i>. Justify the entries in the table
 * below, which shows typical time costs for various pushdown stack
 * implementations, using a cost model that counts both <i>data references</i>
 * (references to data pushed onto the stack, either an array reference or a
 * reference to an object's instance variable) and <i>objects created</i>.
 * </p>
 * 
 * <table>
 * <caption>Time costs for pushdown stacks (various implementations)</caption>
 * <thead>
 * <td>data structure</td>
 * <td>item type</td>
 * <td>date references</td>
 * <td>objects created</td> </thead> <tbody>
 * <tr>
 * <td rowspan=2><i>linked List</i></td>
 * <td><code>int</code></td>
 * <td>2<i>N</i></td>
 * <td><i>N</i></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><code>Integer</code></td>
 * <td>3<i>N</i></td>
 * <td>2<i>N</i></td>
 * </tr>
 * <tr>
 * <td rowspan=2><i>resisizing array</i></td>
 * <td><code>int</code></td>
 * <td>~5<i>N</i></td>
 * <td>lg <i>N</i></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td><code>Integer</code></td>
 * <td>~5<i>N</i></td>
 * <td>~<i>N</i></td>
 * </tr>
 * </tbody>
 * </table>
 */
public class Exercise_1_4_35 {
    public static void main(String[] args) {
    }
}
