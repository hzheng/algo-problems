import java.util.*;

import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/remove-invalid-parentheses/
//
// Remove the minimum number of invalid parentheses in order to make the input
// string valid. Return all possible results.
public class RemoveInvalidParentheses {
    // beats 66.26%(10 ms)
    public List<String> removeInvalidParentheses(String s) {
        List<String> leftRes = new ArrayList<>();
        int end1 = removeRightParentheses(s, leftRes);
        if (end1 >= s.length()) return leftRes;

        List<String> rightRes = new ArrayList<>();
        removeRightParentheses(reverse(s.substring(end1)), rightRes);

        List<String> res = new LinkedList<>();
        for (String s1 : leftRes) {
            for (String s2 : rightRes) {
                res.add(s1 + reverse(s2));
            }
        }
        return res;
    }

    private int removeRightParentheses(String s, List<String> res) {
        int len = s.length();
        int diff = 0;
        int leftParens = 0;
        int rightParens = 0;
        SortedMap<Integer, Integer> map = new TreeMap<>();
        int rightParensToRemoved = 0;
        int invalidRightParenEnd = 0;
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c == '(') {
                leftParens++;
                diff--;
            } else {
                if (c == ')') {
                    rightParens++;
                    map.put(i, ++diff);
                }
            }
            if (rightParens >= leftParens) {
                for (++i; i < len; i++) {
                    c = s.charAt(i);
                    if (c == '(') break;

                    if (c == ')') {
                        rightParens++;
                        map.put(i, ++diff);
                    }
                }
                invalidRightParenEnd = i--;
                rightParensToRemoved = diff;
                leftParens = 0;
                rightParens = 0;
            }
        }
        if (rightParensToRemoved > 0) { // remove invalid right parentheses
            int firstRightParen = map.firstKey();
            StringBuilder sb = new StringBuilder(s.substring(0, firstRightParen));
            removeRightParentheses(s, firstRightParen, invalidRightParenEnd - 1, 0,
                                   rightParensToRemoved, map, sb, res);
        } else {
            res.add(s.substring(0, invalidRightParenEnd));
        }
        return invalidRightParenEnd;
    }

    private void removeRightParentheses(String s, int start, int end,
                                        int removed, int toRemoved,
                                        SortedMap<Integer, Integer> map,
                                        StringBuilder sb, List<String> res) {
        if (toRemoved == 0) {
            if (start <= end) {
                sb.append(s.substring(start, end + 1));
            }
            res.add(sb.toString());
            return;
        }
        if (start > end) return;

        int parenEnd = start;
        while (parenEnd <= end && s.charAt(parenEnd) == ')') {
            parenEnd++;
        }
        int next = end + 1;
        if (parenEnd < end) {
            SortedMap<Integer, Integer> subMap = map.tailMap(parenEnd + 1);
            next = subMap.firstKey();
        }
        int minParens = Math.max(0, map.get(parenEnd - 1) - removed);
        int maxParens = Math.min(parenEnd - start, toRemoved);
        for (int delete = minParens; delete <= maxParens; delete++) {
            int len = sb.length();
            sb.append(s.substring(start, parenEnd - delete));
            if (parenEnd < next) {
                sb.append(s.substring(parenEnd, next));
            }
            removeRightParentheses(s, next, end, removed + delete,
                                   toRemoved - delete, map, sb, res);
            sb.setLength(len);
        }
    }

    private String reverse(String s) {
        s = new StringBuilder(s).reverse().toString();
        return s.replace('(', (char)0).replace(')', '(').replace((char)0, ')');
    }

    void test(Function<String, List<String> > remove,
              String s, String ... expected) {
        List<String> res = remove.apply(s);
        Collections.sort(res);
        Arrays.sort(expected);
        // System.out.println(res + "<->" + Arrays.toString(expected));
        assertArrayEquals(expected, res.toArray(new String[0]));
    }

    void test(String s, String ... expected) {
        RemoveInvalidParentheses r = new RemoveInvalidParentheses();
        test(r::removeInvalidParentheses, s, expected);
    }

    @Test
    public void test1() {
        test(")", "");
        test("(", "");
        test(")(", "");
        test("())", "()");
        test("f)", "f");
        test("(f", "f");
        test(")(f", "f");
        test("f(", "f");
        test("())()", "()()");
        test("((a)()", "(a)()", "((a))");
        test("(a)(()", "(a)()");
        test("())(a)", "()(a)");
        test("()())()((a)((", "(())()(a)","()()()(a)");
        test("()())", "()()", "(())");
        test("(((k()((", "k()", "(k)");
        test("())())", "(())", "()()");
        test("()())(()((a)((", "(())()(a)","(())(()a)","()()()(a)","()()(()a)"); ///???
        test("()())(()((a)((", "(())()(a)", "(())(()a)", "()()()(a)", "()()(()a)");
        test("a", "a");
        test("((a)((", "(a)");
        test("a))))", "a");
        test("(a)())()", "(a)()()", "(a())()");
        test("))(a)())(()(", "(a)()()", "(a())()");
        test("(a)())(()", "(a)()()", "(a())()");
        test("()())()(((a)))))",
             "((()(((a)))))","(()((((a)))))","(()()(((a))))","(())((((a))))","(())()(((a)))","()(((((a)))))","()(()(((a))))","()()((((a))))","()()()(((a)))");
        test("()())()(((a)))))(((",
             "((()(((a)))))","(()((((a)))))","(()()(((a))))","(())((((a))))","(())()(((a)))","()(((((a)))))","()(()(((a))))","()()((((a))))","()()()(((a)))");
        test("()())()", "()()()", "(())()");
        test("", "");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RemoveInvalidParentheses");
    }
}
