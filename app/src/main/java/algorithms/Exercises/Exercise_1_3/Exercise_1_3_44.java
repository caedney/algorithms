package algorithms.Exercises.Exercise_1_3;

import algorithms.Buffer;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.44
 * 
 * <p>
 * <i>Text editor buffer</i>. Develop a data type for a buffer in a text editor
 * that implements the following API:
 * </p>
 * 
 * {@code public class Buffer}
 * <table>
 * <tbody>
 * </tr>
 * <tr>
 * <td></td>
 * <td>{@code Buffer()}</td>
 * <td><i>create an empty buffer</i></td>
 * </tr>
 * <tr>
 * <td>{@code void}</td>
 * <td>{@code insert(char c)}</td>
 * <td><i>insert c at the cursor position</i></td>
 * </tr>
 * <tr>
 * <td>{@code char}</td>
 * <td>{@code delete()}</td>
 * <td><i>delete and return the character at the cursor</i></td>
 * </tr>
 * <tr>
 * <td>{@code void}</td>
 * <td>{@code left(int k)}</td>
 * <td><i>move the cursor k positions to the left</i></td>
 * </tr>
 * <tr>
 * <td>{@code void}</td>
 * <td>{@code right(int k)}</td>
 * <td><i>move the cursor k positions to the right</i></td>
 * </tr>
 * <tr>
 * <td>{@code int}</td>
 * <td>{@code size()}</td>
 * <td><i>number of characters in the buffer</i></td>
 * </tr>
 * </tbody>
 * </table>
 * <p>
 * <i>Hint</i>: Use two stacks.
 * <p>
 */
public class Exercise_1_3_44 {
    public static void main(String[] args) {
        Buffer buffer = new Buffer();
        buffer.insert('a');
        buffer.insert('b');
        buffer.insert('c');
        StdOut.println(buffer); // abc|

        buffer.left(2);
        StdOut.println(buffer); // a|bc

        buffer.insert('x');
        StdOut.println(buffer); // ax|bc

        buffer.right(1);
        StdOut.println(buffer); // axb|c
        StdOut.println(buffer.delete()); // deletes 'c'
        StdOut.println(buffer); // axb|
        StdOut.println("Size: " + buffer.size()); // 3
    }
}
