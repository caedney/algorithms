package algorithms.Exercises.Exercise_1_3;

import java.io.ByteArrayInputStream;
import java.util.StringJoiner;

import algorithms.ResizingArrayStack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Exercise 1.3.8
 * 
 * <p>
 * Give the contents and size of the array for {@code DoublingStackOfStrings}
 * with the input
 * </p>
 * <p>
 * {@code it was - the best - of times - - - it was - the - -}
 * </p>
 */
public class Exercise_1_3_8 {
    public static void main(String[] args) {
        ResizingArrayStack<String> arrayStack = new ResizingArrayStack<String>();

        String input = "it was - the best - of times - - - it was - the - -";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();

            if (!item.equals("-")) {
                arrayStack.push(item);
            } else if (!arrayStack.isEmpty())
                arrayStack.pop();
        }

        StringJoiner joiner = new StringJoiner(", ", "[", "]");
        for (String value : arrayStack) {
            joiner.add(value);
        }

        StdOut.println(joiner.toString());
        StdOut.println("capacity: " + arrayStack.capacity());
    }
}
