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

    // DFS
    // beats 9.82%(230 ms)
    public List<String> removeInvalidParentheses2(String s) {
        List<String> res = new ArrayList<>();
        removeInvalidParentheses2(s, "", 0, 0, new int[] {0}, res);
        if (res.size() == 0) {
            res.add("");
        }
        return res;
    }

    private void removeInvalidParentheses2(String src, String dest,
                                           int parenDiff, int leftParens,
                                           int[] maxPairs, List<String> res) {
        if (src.length() == 0) {
            if (parenDiff == 0 && dest.length() != 0) {
                maxPairs[0] = Math.max(maxPairs[0], leftParens);
                if (leftParens == maxPairs[0] && !res.contains(dest)) {
                    res.add(dest);
                }
            }
            return;
        }

        String stripped = src.substring(1);
        switch (src.charAt(0)) {
        case '(':
            removeInvalidParentheses2(stripped, dest + '(', parenDiff + 1,
                                      leftParens + 1, maxPairs, res);
            removeInvalidParentheses2(stripped, dest, parenDiff,
                                      leftParens, maxPairs, res);
            break;
        case ')':
            if (parenDiff > 0) {
                removeInvalidParentheses2(stripped, dest + ')', parenDiff - 1,
                                          leftParens, maxPairs, res);
            }
            removeInvalidParentheses2(stripped, dest, parenDiff,
                                      leftParens, maxPairs, res);
            break;
        default:
            removeInvalidParentheses2(stripped, dest + src.charAt(0), parenDiff,
                                      leftParens, maxPairs, res);
        }
    }

    // https://discuss.leetcode.com/topic/34875/easy-short-concise-and-fast-java-dfs-3-ms-solution/2
    // beats 79.88%(3 ms)
    public List<String> removeInvalidParentheses3(String s) {
        List<String> res = new ArrayList<>();
        removeParens(s, res, 0, 0, true);
        return res;
    }

    private void removeParens(String s, List<String> res,
                              int lastI, int lastJ, boolean leftToRight) {
        char paren1 = leftToRight ? '(' : ')';
        char paren2 = leftToRight ? ')' : '(';
        int len = s.length();
        for (int parenDiff = 0, i = lastI; i < len; ++i) {
            if (s.charAt(i) == paren1) {
                parenDiff++;
            } else if (s.charAt(i) == paren2) {
                parenDiff--;
            }
            if (parenDiff >= 0) continue;

            for (int j = lastJ; j <= i; ++j) {
                if (s.charAt(j) == paren2
                    && (j == lastJ || s.charAt(j - 1) != paren2)) {
                    removeParens(s.substring(0, j) + s.substring(j + 1, len),
                                 res, i, j, leftToRight);
                }
            }
            return;
        }

        String reversed = new StringBuilder(s).reverse().toString();
        if (leftToRight) { // finished left to right
            removeParens(reversed, res, 0, 0, false);
        } else { // finished right to left
            res.add(reversed);
        }
    }

    // BFS(non-recursion)
    // https://discuss.leetcode.com/topic/28827/share-my-java-bfs-solution
    // beats 41.59%(103 ms)
    public List<String> removeInvalidParentheses4(String s) {
        List<String> res = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();

        queue.add(s);
        visited.add(s);
        boolean found = false;
        while (!queue.isEmpty()) {
            s = queue.poll();
            if (isValid(s)) {
                res.add(s);
                found = true;
            }
            if (found) continue;

            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) != '(' && s.charAt(i) != ')') continue;

                String candidate = s.substring(0, i) + s.substring(i + 1);
                if (!visited.contains(candidate)) {
                    queue.add(candidate);
                    visited.add(candidate);
                }
            }
        }
        return res;
    }

    private boolean isValid(String s) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') count++;
            if (c == ')' && count-- == 0) return false;
        }
        return count == 0;
    }

    void test(Function<String, List<String> > remove, String name,
              String s, String ... expected) {
        long t1 = System.nanoTime();
        List<String> res = remove.apply(s);
        if (s.length() > 15) {
            System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        }
        Collections.sort(res);
        Arrays.sort(expected);
        // System.out.println(res + "<->" + Arrays.toString(expected));
        assertArrayEquals(expected, res.toArray(new String[0]));
    }

    void test(String s, String ... expected) {
        RemoveInvalidParentheses r = new RemoveInvalidParentheses();
        test(r::removeInvalidParentheses, "removeInvalidParentheses", s, expected);
        test(r::removeInvalidParentheses2, "removeInvalidParentheses2", s, expected);
        test(r::removeInvalidParentheses3, "removeInvalidParentheses3", s, expected);
        test(r::removeInvalidParentheses4, "removeInvalidParentheses4", s, expected);
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
