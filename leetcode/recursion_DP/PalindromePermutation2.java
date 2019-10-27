import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC267: https://leetcode.com/problems/palindrome-permutation-ii/
//
// Given a string s, return all the palindromic permutations of it. Return an
// empty list if no palindromic permutation could be form.
public class PalindromePermutation2 {
    // DFS/Backtracking + Recursion
    // beats 99.15%(1 ms for 29 tests)
    public List<String> generatePalindromes(String s) {
        int[] count = new int[256];
        for (char c : s.toCharArray()) {
            count[c]++;
        }
        char oddChar = 0;
        for (int i = 0; i < count.length; i++) {
            if (count[i] % 2 == 1) {
                if (oddChar != 0) return Collections.emptyList();

                oddChar = (char)i;
            }
        }
        int len = s.length();
        char[] word = new char[len / 2];
        for (int i = 0, k = 0; i < count.length; i++) {
            for (int j = count[i] / 2; j > 0; j--) {
                word[k++] = (char)i;
            }
        }
        char[] buf = new char[len];
        buf[len / 2] = oddChar;
        List<String> res = new ArrayList<>();
        dfs(word, 0, new boolean[len / 2], buf, res);
        return res;
    }

    private void dfs(char[] s, int cur, boolean[] visited, char[] buf, List<String> res) {
        if (cur >= s.length) {
            for (int i = buf.length - 1, j = 0; i > j; i--, j++) {
                buf[i] = buf[j];
            }
            res.add(new String(buf));
            return;
        }
        char last = 0; // avoid duplicate(notice that s is sorted)
        for (int i = 0; i < s.length; i++) {
            if (visited[i] || s[i] == last) continue;

            visited[i] = true;
            last = buf[cur] = s[i];
            dfs(s, cur + 1, visited, buf, res);
            visited[i] = false;
        }
    }

    // DFS/Backtracking + Recursion
    // beats 68.44%(5 ms for 29 tests)
    public List<String> generatePalindromes2(String s) {
        int[] count = new int[256];
        for (char c : s.toCharArray()) {
            count[c]++;
        }
        char oddChar = 0;
        for (int i = 0; i < count.length; i++) {
            if (count[i] % 2 == 1) {
                if (oddChar != 0) return Collections.emptyList();

                oddChar = (char)i;
            }
        }
        List<String> res = new ArrayList<>();
        for (int i = 0; i < count.length; i++) {
            count[i] /= 2;
        }
        int len = s.length();
        char[] buf = new char[len];
        buf[len / 2] = oddChar;
        dfs2(count, 0, buf, res);
        return res;
    }

    private void dfs2(int[] count, int cur, char[] buf, List<String> res) {
        if (cur >= buf.length / 2) {
            for (int i = buf.length - 1, j = 0; i > j; i--, j++) {
                buf[i] = buf[j];
            }
            res.add(new String(buf));
            return;
        }
        for (int i = 0; i < count.length; i++) {
            if (count[i] == 0) continue;

            count[i]--;
            buf[cur] = (char)i;
            dfs2(count, cur + 1, buf, res);
            count[i]++;
        }
    }

    void test(String s, String[] expected) {
        assertArrayEquals(expected, generatePalindromes(s).toArray(new String[0]));
        assertArrayEquals(expected, generatePalindromes2(s).toArray(new String[0]));
    }

    @Test
    public void test() {
        test("abc", new String[]{});
        test("aabb", new String[]{"abba", "baab"});
        test("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
             new String[]{"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
