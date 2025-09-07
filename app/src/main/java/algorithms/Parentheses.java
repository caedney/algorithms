package algorithms;

import edu.princeton.cs.algs4.StdOut;

public class Parentheses {
    private static final char LEFT_PAREN = '(';
    private static final char RIGHT_PAREN = ')';
    private static final char LEFT_BRACE = '{';
    private static final char RIGHT_BRACE = '}';
    private static final char LEFT_BRACKET = '[';
    private static final char RIGHT_BRACKET = ']';
    private static final char LEFT_ANGLE = '<';
    private static final char RIGHT_ANGLE = '>';

    public static boolean isBalanced(String str) {
        Stack<Character> stack = new Stack<Character>();

        for (char chr : str.toCharArray()) {
            if (chr == LEFT_PAREN)
                stack.push(LEFT_PAREN);
            if (chr == LEFT_BRACE)
                stack.push(LEFT_BRACE);
            if (chr == LEFT_BRACKET)
                stack.push(LEFT_BRACKET);
            if (chr == LEFT_ANGLE)
                stack.push(LEFT_ANGLE);

            if (chr == RIGHT_PAREN) {
                if (stack.isEmpty())
                    return false;
                if (stack.pop() != LEFT_PAREN)
                    return false;
            }

            else if (chr == RIGHT_BRACE) {
                if (stack.isEmpty())
                    return false;
                if (stack.pop() != LEFT_BRACE)
                    return false;
            }

            else if (chr == RIGHT_BRACKET) {
                if (stack.isEmpty())
                    return false;
                if (stack.pop() != LEFT_BRACKET)
                    return false;
            }

            else if (chr == RIGHT_ANGLE) {
                if (stack.isEmpty())
                    return false;
                if (stack.pop() != LEFT_ANGLE)
                    return false;
            }
        }

        return stack.isEmpty();
    }

    public static void main(String[] args) {
        StdOut.println(isBalanced("[()]{}{[()()]()}")); // true
        StdOut.println(isBalanced("[(])")); // false
    }
}
