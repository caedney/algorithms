package algorithms;

public class EvaluatePostfix {
    public static Integer evaluate(String postfix) {
        Stack<Integer> stack = new Stack<Integer>();

        for (int i = 0; i < postfix.length(); i++) {
            char c = postfix.charAt(i);

            if (c == ' ')
                continue;

            if (c == '+')
                stack.push(stack.pop() + stack.pop());
            else if (c == '*')
                stack.push(stack.pop() * stack.pop());
            else
                stack.push(Integer.parseInt(String.valueOf(c)));
        }

        return stack.pop();
    }
}
