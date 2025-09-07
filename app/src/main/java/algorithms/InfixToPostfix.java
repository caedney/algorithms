package algorithms;

public class InfixToPostfix {
    public static String convert(String infix) {
        StringBuilder result = new StringBuilder();
        Stack<Character> stack = new Stack<Character>();

        for (int i = 0; i < infix.length(); i++) {
            char c = infix.charAt(i);

            if (c == ' ' || c == '(')
                continue;

            if (c == '+')
                stack.push(c);
            else if (c == '*')
                stack.push(c);
            else if (c == ')')
                result.append(stack.pop()).append(" ");
            else
                result.append(c).append(" ");
        }

        return result.toString().trim();
    }
}
