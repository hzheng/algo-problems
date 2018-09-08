import java.util.*;

import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.*;

// LC784: https://leetcode.com/problems/letter-case-permutation/
//
// Given a string S, we can transform every letter individually to be lowercase
// or uppercase to create another string.  Return a list of all possible strings
// we could create.
public class LetterCasePermutation {
    // Bit Manipulation + Set
    // beats 10.81%(21 ms for 64 tests)
    public List<String> letterCasePermutation(String S) {
        Set<String> set = new HashSet<>();
        int n = S.length();
        for (int mask = (1 << n) - 1; mask >= 0; mask--) {
            char[] cs = S.toCharArray();
            for (int i = 0; i < n; i++) {
                cs[i] = ((mask >> i) & 1) != 0
                        ? Character.toLowerCase(cs[i])
                        : Character.toUpperCase(cs[i]);
            }
            set.add(String.valueOf(cs));
        }
        return new ArrayList<>(set);
    }

    // Solution of Choice
    // DFS + Recursion
    // beats 86.83%(7 ms for 64 tests)
    public List<String> letterCasePermutation2(String S) {
        List<String> res = new ArrayList<>();
        dfs(S.toCharArray(), 0, res);
        return res;
    }

    private void dfs(char[] cs, int start, List<String> res) {
        if (start == cs.length) {
            res.add(String.valueOf(cs));
            return;
        }
        dfs(cs, start + 1, res);
        if (Character.isLetter(cs[start])) {
            cs[start] ^= 32;
            dfs(cs, start + 1, res);
        }
    }

    // Solution of Choice
    // BFS + Queue
    // beats 75.01%(8 ms for 64 tests)
    public List<String> letterCasePermutation3(String S) {
        LinkedList<String> queue = new LinkedList<>();
        queue.offer(S);
        for (int i = S.length() - 1; i >= 0; i--) {
            if (S.charAt(i) <= '9') continue;

            for (int j = queue.size(); j > 0; j--) {
                String cur = queue.poll();
                queue.offer(cur);
                char[] cs = cur.toCharArray();
                cs[i] ^= 32;
                queue.offer(String.valueOf(cs));
            }
        }
        return queue;
    }

    // Solution of Choice
    // Iteration
    // beats 53.19%(10 ms for 64 tests)
    public List<String> letterCasePermutation4(String S) {
        List<String> res = new ArrayList<>(Arrays.asList(S));
        for (int i = 0, n = S.length(); i < n; i++) {
            for (int j = 0, m = res.size(); S.charAt(i) > '9' && j < m; j++) {
                char[] cs = res.get(j).toCharArray();
                cs[i] ^= 32; // toggle case
                res.add(String.valueOf(cs));
            }
        }
        return res;
    }

    void test(String S, String[] expected, Function<String, List<String> > f) {
        Arrays.sort(expected);
        List<String> exp = Arrays.asList(expected);
        List<String> res = f.apply(S);
        Collections.sort(res);
        assertEquals(exp, res);
    }

    void test(String S, String[] expected) {
        LetterCasePermutation l = new LetterCasePermutation();
        test(S, expected, l::letterCasePermutation);
        test(S, expected, l::letterCasePermutation2);
        test(S, expected, l::letterCasePermutation3);
        test(S, expected, l::letterCasePermutation4);
    }

    @Test
    public void test() {
        test("a1b2", new String[] { "a1b2", "a1B2", "A1b2", "A1B2" });
        test("Da1b2C$", new String[] { "DA1B2C$", "DA1B2c$", "DA1b2C$",
                                       "DA1b2c$", "Da1B2C$", "Da1B2c$",
                                       "Da1b2C$",
                                       "Da1b2c$", "dA1B2C$", "dA1B2c$",
                                       "dA1b2C$", "dA1b2c$", "da1B2C$",
                                       "da1B2c$", "da1b2C$",
                                       "da1b2c$" });
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
