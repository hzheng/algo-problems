import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC020: https://leetcode.com/problems/valid-parentheses/
// 
// Given a string containing just the characters '(', ')', '{', '}', '[' and ']',
// determine if the input string is valid.
public class ValidParentheses {
    // beats 2.53%(8 ms) // old: beats 54.59%(1 ms)
    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            switch (c) {
            case '{': case '[': case '(':
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

    // Solution of Choice
    // beats 4.74%(7 ms)
    public boolean isValid2(String s) {
        Stack<Character> stack = new Stack<>();
        char[] pairs = new char[128];
        pairs[')'] = '(';
        pairs[']'] = '[';
        pairs['}'] = '{';
        stack.push(' '); // dummy
        for (char c : s.toCharArray()) {
            switch (c) {
            case '{': case '[': case '(':
                stack.push(c);
                break;
            default:
                if (stack.pop() != pairs[c]) return false;
                break;
            }
        }
        return stack.size() == 1;
    }

    // beats 2.53%(8 ms)
    public boolean isValid3(String s) {
        Stack<Character> stack = new Stack<>();
        stack.push(' '); // dummy
        for (char c : s.toCharArray()) {
            switch (c) {
            case '{': case '[': case '(':
                stack.push(c);
                break;
            default:
                int diff = c - stack.pop();
                if (diff != 1 && diff != 2) return false;
            }
        }
        return stack.size() == 1;
    }

    // beats 13.08%(5 ms)
    public boolean isValid4(String s) {
        char[] stack = new char[s.length() + 2];
        int top = 1;
        for (char c : s.toCharArray()) {
            switch (c) {
            case '{': case '[': case '(':
                stack[++top] = c;
                break;
            default:
                int diff = c - stack[top--];
                if (diff != 1 && diff != 2) return false;
            }
        }
        return top == 1;
    }

    // beats 13.08%(5 ms)
    public boolean isValid5(String s) {
        char[] chars = s.toCharArray(); // reuse as a stack
        int top = 0;
        for (char c : chars) {
            if (top == 0 || (c - chars[top - 1] + 1) / 2 != 1) {
                chars[top++] = c;
            } else {
                top--;
            }
        }
        return top == 0;
    }

    void test(String s, boolean expected) {
        assertEquals(expected, isValid(s));
        assertEquals(expected, isValid2(s));
        assertEquals(expected, isValid3(s));
        assertEquals(expected, isValid4(s));
        assertEquals(expected, isValid5(s));
    }

    @Test
    public void test1() {
        test("[", false);
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
