import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC385: https://leetcode.com/problems/mini-parser/
//
// Given a nested list of integers represented as a string, implement a parser
// to deserialize it. Each element is either an integer, or a list -- whose
// elements may also be integers or other lists.
// Note: You may assume that the string is well-formed:
// String is non-empty.
// String does not contain white spaces.
// String contains only digits 0-9, [, - ,, ].

class NestedInteger {
    Integer integer;
    List<NestedInteger> list;

    // Constructor initializes an empty nested list.
    public NestedInteger() {
    }

    // Constructor initializes a single integer.
    public NestedInteger(int value) {
        setInteger(value);
    }

    // @return true if this NestedInteger holds a single integer, rather than a nested list.
    public boolean isInteger() {
        return integer != null;
    }

    // @return the single integer that this NestedInteger holds, if it holds a single integer
    // Return null if this NestedInteger holds a nested list
    public Integer getInteger() {
        return integer;
    }

    // Set this NestedInteger to hold a single integer.
    public void setInteger(int value) {
        integer = value;
        list = null;
    }

    // Set this NestedInteger to hold a nested list and adds a nested integer to it.
    public void add(NestedInteger ni) {
        if (list == null) {
            list = new ArrayList<>();
            integer = null;
        }
        list.add(ni);
    }

    // @return the nested list that this NestedInteger holds, if it holds a nested list
    // Return null if this NestedInteger holds a single integer
    public List<NestedInteger> getList() {
        return list;
    }
}

public class MiniParser {
    // Stack
    // beats 53.90%(22 ms)
    public NestedInteger deserialize(String s) {
        if (s.charAt(0) != '[') {
            return new NestedInteger(Integer.parseInt(s));
        }

        Stack<NestedInteger> stack = new Stack<>();
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            switch (chars[i]) {
            case '[':
                stack.push(new NestedInteger());
                break;
            case ']':
                NestedInteger ni = stack.pop();
                if (stack.isEmpty()) return ni;

                stack.peek().add(ni);
                break;
            case ',':
                break;
            default: // (c == '-' || c >= '0' && c <= '9')
                // StringBuilder sb = new StringBuilder();
                // sb.append(chars[i]);
                // while (Character.isDigit(chars[++i])) {
                //     sb.append(chars[i]);
                // }
                // stack.peek().add(new NestedInteger(Integer.parseInt(sb.toString())));
                int sign = 1;
                int num = 0;
                if (chars[i] == '-') {
                    sign = -1;
                } else {
                    num = chars[i] - '0';
                }
                while (Character.isDigit(chars[++i])) {
                    num *= 10;
                    num += chars[i] - '0';
                }
                stack.peek().add(new NestedInteger(num * sign));
                i--;
            }
        }
        return null;
    }

    // TODO: recursion

    // TODO: formal grammar
    // https://discuss.leetcode.com/topic/55929/my-solution-using-formal-grammar-long-but-elegant-in-my-opinion

    void test(String s) {
        NestedInteger ni = deserialize(s);
        // System.out.println(ni);
    }

    @Test
    public void test1() {
        test("324");
        test("[123,[456,[789]]]");
        test("[123,[456,[789,-12,[23,45]]]]");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MiniParser");
    }
}
