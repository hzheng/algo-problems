import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC150: https://leetcode.com/problems/evaluate-reverse-polish-notation/
//
// Evaluate the value of an arithmetic expression in Reverse Polish Notation.
public class EvalRPN {
    // Solution of Choice
    // beats 50.28%(16 ms)
    public int evalRPN(String[] tokens) {
        Stack<Integer> stack = new Stack<>();
        for (String token : tokens) {
            switch (token) {
            case "+":
                stack.push(stack.pop() + stack.pop());
                break;
            case "*":
                stack.push(stack.pop() * stack.pop());
                break;
            case "-":
                int second = stack.pop();
                int first = stack.pop();
                stack.push(first - second);
                break;
            case "/":
                second = stack.pop();
                first = stack.pop();
                stack.push(first / second);
                break;
            default:
                stack.push(Integer.parseInt(token));
                break;
            }
        }
        return stack.pop();
    }

    void test(int expected, String ... tokens) {
        assertEquals(expected, evalRPN(tokens));
    }

    @Test
    public void test1() {
        test(1, "2", "1", "-");
        test(2, "2");
        test(9, "2", "1", "+", "3", "*");
        test(6, "4", "13", "5", "/", "+");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("EvalRPN");
    }
}
