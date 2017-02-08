import java.util.*;

import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.NestedInteger;
import common.NestedIntegerImpl;

// LC385: https://leetcode.com/problems/mini-parser/
//
// Given a nested list of integers represented as a string, implement a parser
// to deserialize it. Each element is either an integer, or a list -- whose
// elements may also be integers or other lists.
// Note: You may assume that the string is well-formed:
// String is non-empty.
// String does not contain white spaces.
// String contains only digits 0-9, [, - ,, ].
public class MiniParser {
    // Solution of Choice
    // Stack
    // beats 70.27%(21 ms for 57 tests)
    public NestedInteger deserialize(String s) {
        if (s.charAt(0) != '[') {
            return new NestedIntegerImpl(Integer.parseInt(s));
        }
        Stack<NestedInteger> stack = new Stack<>();
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            switch (chars[i]) {
            case '[':
                stack.push(new NestedIntegerImpl());
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
                stack.peek().add(new NestedIntegerImpl(num * sign));
                i--;
            }
        }
        return null;
    }

    // Recursion
    // beats 99.49%(10 ms for 57 tests)
    public NestedInteger deserialize2(String s) {
        return parse(s.toCharArray(), new int[1]);
    }

    private NestedInteger parse(char[] s, int[] index) {
        int i = index[0];
        if (s[i] == '[') {
            NestedInteger ni = new NestedIntegerImpl();
            for (index[0]++; s[index[0]] != ']'; ) {
                ni.add(parse(s, index));
                if (s[index[0]] == ',') {
                    index[0]++;
                }
            }
            index[0]++;
            return ni;
        }
        int sign = 1;
        int num = 0;
        if (s[i] == '-') {
            sign = -1;
        } else {
            num = s[i] - '0';
        }
        while (++i < s.length && Character.isDigit(s[i])) {
            num *= 10;
            num += s[i] - '0';
        }
        index[0] = i;
        return new NestedIntegerImpl(num * sign);
    }

    // TODO: formal grammar
    // https://discuss.leetcode.com/topic/55929/my-solution-using-formal-grammar-long-but-elegant-in-my-opinion

    void test(Function<String, NestedInteger> deserialize, String s) {
        NestedInteger ni = deserialize.apply(s);
        assertEquals(s, ni.toString());
    }

    void test(String s) {
        MiniParser m = new MiniParser();
        test(m::deserialize, s);
        test(m::deserialize2, s);
    }

    @Test
    public void test1() {
        test("[-1]");
        test("324");
        test("[123,[456,[789]]]");
        test("[123,[456,[789,-12,[23,45]]]]");
        test("[123,456,[788,799,833],[[]],10,[]]");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MiniParser");
    }
}
