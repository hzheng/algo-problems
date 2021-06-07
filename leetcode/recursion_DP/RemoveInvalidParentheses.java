import java.util.*;

import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC301: https://leetcode.com/problems/remove-invalid-parentheses/
//
// Remove the minimum number of invalid parentheses in order to make the input
// string valid. Return all possible results.
public class RemoveInvalidParentheses {
    // Recursion + Backtracking + SortedMap
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

    // DFS + Recursion
    // beats 9.41%(221 ms for 125 tests)
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

    // Solution of Choice
    // DFS + Recursion
    // https://discuss.leetcode.com/topic/34875/easy-short-concise-and-fast-java-dfs-3-ms-solution/
    // 3 ms(76.42%), 39.8 MB(30.88%) for 125 tests
    public List<String> removeInvalidParentheses3(String s) {
        List<String> res = new ArrayList<>();
        removeParens(s, res, 0, 0, true);
        return res;
    }

    private void removeParens(String s, List<String> res,
                              int lastI, int lastJ, boolean leftToRight) {
        char paren1 = leftToRight ? '(' : ')';
        char paren2 = leftToRight ? ')' : '(';
        for (int bal = 0, i = lastI, len = s.length(); i < len; i++) {
            if (s.charAt(i) == paren1) {
                bal++;
            } else if (s.charAt(i) == paren2) {
                bal--;
            }
            if (bal >= 0) { continue; }

            for (int j = lastJ; j <= i; j++) {
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

    // BFS + Queue
    // 37 ms(46.79%), 51.4 MB(5.01%) for 125 tests
    public List<String> removeInvalidParentheses4(String s) {
        List<String> res = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.offer(s);
        visited.add(s);
        for (boolean found = false; !queue.isEmpty(); ) {
            String cur = queue.poll();
            int balance = parenBalance(cur);
            if (balance == 0) {
                res.add(cur);
                found = true;
            }
            if (found)  { continue; }

            for (int i = 0, n = cur.length(); i < n; i++) {
                if (cur.charAt(i) != '(' && cur.charAt(i) != ')'
                    || cur.charAt(i) == '(' && balance < 0) {
                    continue;
                }

                String candidate = cur.substring(0, i) + cur.substring(i + 1);
                if (visited.add(candidate)) {
                    queue.offer(candidate);
                }
            }
        }
        return res;
    }

    private int parenBalance(String s) {
        int count = 0;
        for (int i = 0, n = s.length(); i < n; i++) {
            char c = s.charAt(i);
            if (c == '(') {
                count++;
            } else if (c == ')' && count-- == 0) return -1;
        }
        return count;
    }

    // DFS/Backtracking + Recursion + Set
    // https://discuss.leetcode.com/topic/30743/easiest-9ms-java-solution
    // 2 ms(85.21%), 39.1 MB(64.02%) for 125 tests
    public List<String> removeInvalidParentheses5(String s) {
        int leftRemoves = 0;
        int rightRemoves = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                leftRemoves++;
            } else if (s.charAt(i) == ')') {
                if (leftRemoves != 0) {
                    leftRemoves--;
                } else {
                    rightRemoves++;
                }
            }
        }
        Set<String> res = new HashSet<>();
        dfs(s, 0, res, new StringBuilder(), leftRemoves, rightRemoves, 0);
        return new ArrayList<>(res);
    }

    public void dfs(String s, int i, Set<String> res, StringBuilder sb,
                   int leftRemoves, int rightRemoves, int open) {
        if (leftRemoves < 0 || rightRemoves < 0 || open < 0) { return; }

        if (i == s.length()) {
            if (leftRemoves == 0 && rightRemoves == 0) {
                res.add(sb.toString());
            }
            return;
        }

        char c = s.charAt(i);
        int len = sb.length();
        if (c == '(') {
            dfs(s, i + 1, res, sb, leftRemoves - 1, rightRemoves, open);
            dfs(s, i + 1, res, sb.append(c), leftRemoves, rightRemoves, open + 1);
        } else if (c == ')') {
            dfs(s, i + 1, res, sb, leftRemoves, rightRemoves - 1, open);
            dfs(s, i + 1, res, sb.append(c), leftRemoves, rightRemoves, open - 1);
        } else {
            dfs(s, i + 1, res, sb.append(c), leftRemoves, rightRemoves, open);
        }
        sb.setLength(len);
    }

    // DFS/Backtracking + Recursion + Set
    // https://discuss.leetcode.com/topic/30743/easiest-9ms-java-solution
    // 31 ms(47.93%), 39.1 MB(69.21%) for 125 tests
    public List<String> removeInvalidParentheses6(String s) {
        int n = s.length();
        int bal = 0;
        int len = n;
        for (int i = 0; i < n; i++) {
            switch (s.charAt(i)) {
            case '(':
                bal++;
                break;
            case ')':
                if (--bal < 0) {
                    len--;
                    bal = 0;
                }
                break;
            }
        }
        Set<String> res = new HashSet<>();
        remove(s, 0, 0, len - bal, new StringBuilder(), res);
        return new ArrayList<>(res);
    }

    private void remove(String s, int cur, int bal, int remain, StringBuilder sb, Set<String> res) {
        if (remain == 0) {
            if (bal == 0) {
                res.add(sb.toString());
            }
            return;
        }
        if (bal < 0 || cur >= s.length()) { return; }

        char c = s.charAt(cur);
        if (c == '(' || c == ')') {
            remove(s, cur + 1, bal, remain, sb, res);
            bal += (c == '(') ? 1 : -1;
        }
        int len = sb.length();
        remove(s, cur + 1, bal, remain - 1, sb.append(c), res);
        sb.setLength(len);
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
        test(r::removeInvalidParentheses5, "removeInvalidParentheses5", s, expected);
        test(r::removeInvalidParentheses6, "removeInvalidParentheses6", s, expected);
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
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
