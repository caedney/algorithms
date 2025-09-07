package algorithms;

import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.StdOut;

public class InfixToPostfix {
    public static List<Character> convert(String infix) {
        // Shunting Yard Algorithm (Dijkstra)
        List<Character> output = new ArrayList<>();
        Stack<Character> operators = new Stack<Character>();

        for (int i = 0; i < infix.length(); i++) {
            char c = infix.charAt(i);

            if (c == ' ' || c == '(')
                continue;

            if (c == '+')
                operators.push(c);
            else if (c == '*')
                operators.push(c);
            else if (c == ')')
                output.add(operators.pop());
            else
                output.add(c);
        }

        return output;
    }

    public static void main(String[] args) {
        String infix = "( 2 + ( ( 3 + 4 ) * ( 5 * 6 ) ) )";
        List<Character> postfix = InfixToPostfix.convert(infix);
        StdOut.println("Infix:   " + infix); // ( 2 + ( ( 3 + 4 ) * ( 5 * 6 ) ) )

        StdOut.print("Postfix: ");
        for (int i = 0; i < postfix.size(); i++)
            StdOut.print(postfix.get(i) + " "); // 2 3 4 + 5 6 * * +
        StdOut.println();

        infix = "( ( ( 5 + ( 7 * ( 1 + 1 ) ) ) * 3 ) + ( 2 * ( 1 + 1 ) ) )";
        postfix = InfixToPostfix.convert(infix);
        StdOut.println("Infix:   " + infix); // ( ( ( 5 + ( 7 * ( 1 + 1 ) ) ) * 3 ) + ( 2 * ( 1 + 1 ) ) )

        StdOut.print("Postfix: ");
        for (int i = 0; i < postfix.size(); i++)
            StdOut.print(postfix.get(i) + " "); // 5 7 1 1 + * + 3 * 2 1 1 + * +
        StdOut.println();
    }
}
