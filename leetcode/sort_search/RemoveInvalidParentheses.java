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
        int end1 = removeParentheses(s, leftRes, true);
        if (end1 >= s.length()) return leftRes;

        List<String> rightRes = new ArrayList<>();
        StringBuilder sb = new StringBuilder(s.substring(end1)).reverse();
        removeParentheses(sb.toString(), rightRes, false);

        List<String> res = new LinkedList<>();
        for (String left : leftRes) {
            for (String right : rightRes) {
                res.add(left + new StringBuilder(right).reverse());
            }
        }
        return res;
    }

    private int removeParentheses(String s, List<String> res, boolean isRight) {
        int len = s.length();
        int diff = 0;
        char paren1 = isRight ? '(' : ')';
        char paren2 = isRight ? ')' : '(';
        int parens1 = 0;
        int parens2 = 0;
        SortedMap<Integer, Integer> map = new TreeMap<>();
        int parensToRemoved = 0;
        int surplusParenEnd = 0;
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c == paren1) {
                parens1++;
                diff--;
            } else if (c == paren2) {
                parens2++;
                map.put(i, ++diff);
            }
            if (parens2 >= parens1) {
                for (++i; i < len; i++) {
                    c = s.charAt(i);
                    if (c == paren1) break;

                    if (c == paren2) {
                        parens2++;
                        map.put(i, ++diff);
                    }
                }
                surplusParenEnd = i--;
                parensToRemoved = diff;
                parens1 = 0;
                parens2 = 0;
            }
        }
        if (parensToRemoved > 0) { // remove invalid parentheses
            int firstParen = map.firstKey();
            StringBuilder sb = new StringBuilder(s.substring(0, firstParen));
            removeParentheses(s, firstParen, surplusParenEnd - 1, 0,
                              parensToRemoved, map, sb, res, isRight);
        } else {
            res.add(s.substring(0, surplusParenEnd));
        }
        return surplusParenEnd;
    }

    private void removeParentheses(String s, int start, int end, int removed,
                                   int toRemoved, SortedMap<Integer, Integer> map,
                                   StringBuilder sb, List<String> res,
                                   boolean isRight) {
        if (toRemoved == 0) {
            if (start <= end) {
                sb.append(s.substring(start, end + 1));
            }
            res.add(sb.toString());
            return;
        }
        if (start > end) return;

        int parenEnd = start;
        char paren = isRight ? ')' : '(';
        while (parenEnd <= end && s.charAt(parenEnd) == paren) {
            parenEnd++;
        }
        int next = end + 1;
        if (parenEnd < end) {
            next = map.tailMap(parenEnd + 1).firstKey();
        }
        int minParens = Math.max(0, map.get(parenEnd - 1) - removed);
        int maxParens = Math.min(parenEnd - start, toRemoved);
        for (int delete = minParens; delete <= maxParens; delete++) {
            int len = sb.length();
            sb.append(s.substring(start, parenEnd - delete));
            if (parenEnd < next) {
                sb.append(s.substring(parenEnd, next));
            }
            removeParentheses(s, next, end, removed + delete,
                              toRemoved - delete, map, sb, res, isRight);
            sb.setLength(len);
        }
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
