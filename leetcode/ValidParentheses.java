import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// Given a string containing just the characters '(', ')', '{', '}', '[' and ']',
// determine if the input string is valid.
public class ValidParentheses {
    // beats 54.59%
    public boolean isValid(String s) {
        if (s == null || s.isEmpty()) return false;

        Stack<Character> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            switch (c) {
            case '{':
            case '[':
            case '(':
                stack.push(c);
                break;

            case '}':
                if (stack.isEmpty() || stack.pop() != '{') return false;
                break;
            case ']':
                if (stack.isEmpty() || stack.pop() != '[') return false;
                break;
            case ')':
                if (stack.isEmpty() || stack.pop() != '(') return false;
                break;
            default:
                return false;
            }
        }
        return stack.isEmpty();
    }

    void test(String s, boolean expected) {
        assertEquals(expected, isValid(s));
    }

    @Test
    public void test1() {
        test("{", false);
        test("{}", true);
        test("()", true);
        test("[]", true);
        test("{}[]", true);
        test("{[}]", false);
        test("[{()}]", true);
        test("[]{()}]", false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ValidParentheses");
    }
}
