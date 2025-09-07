package algorithms;

import java.util.Stack;

public class Buffer {
    private Stack<Character> left;
    private Stack<Character> right;

    // Create an empty buffer
    public Buffer() {
        left = new Stack<>();
        right = new Stack<>();
    }

    // Insert character c at the cursor
    public void insert(char c) {
        left.push(c);
    }

    // Delete and return the character at the cursor (right of cursor)
    public char delete() {
        if (right.isEmpty())
            throw new RuntimeException("Cursor is at the end, nothing to delete");

        return right.pop();
    }

    // Move cursor k positions to the left
    public void left(int k) {
        for (int i = 0; i < k && !left.isEmpty(); i++) {
            right.push(left.pop());
        }
    }

    // Move cursor k positions to the right
    public void right(int k) {
        for (int i = 0; i < k && !right.isEmpty(); i++) {
            left.push(right.pop());
        }
    }

    // Number of characters in the buffer
    public int size() {
        return left.size() + right.size();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (char c : left)
            stringBuilder.append(c);

        stringBuilder.append('|'); // cursor position

        for (int i = right.size() - 1; i >= 0; i--)
            stringBuilder.append(right.get(i));

        return stringBuilder.toString();
    }
}