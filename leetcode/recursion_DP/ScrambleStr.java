import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// Given a string s1, we may represent it as a binary tree by partitioning it to
// two non-empty substrings recursively. To scramble the string, we may choose
// any non-leaf node and swap its two children.
// Given two strings s1 and s2 of the same length, determine if s2 is a
// scrambled string of s1.
public class ScrambleStr {
    // Recursion
    // beats 67.44%(6 ms)
    public boolean isScramble(String s1, String s2) {
        if (s1.equals(s2)) return true;

        int len = s1.length();
        for (int i = 1; i < len; i++) {
            if (isPerm(s1, s2, 0, 0, i)) {
                if (isScramble(s1.substring(0, i), s2.substring(0, i))
                    && isScramble(s1.substring(i), s2.substring(i))) {
                    return true;
                }
            }
            if (isPerm(s1, s2, 0, len - i, i)) {
                if (isScramble(s1.substring(0, i), s2.substring(len - i, len))
                    && isScramble(s1.substring(i), s2.substring(0, len - i))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isPerm(String s1, String s2, int i1, int i2, int len) {
        if (len == 1) return s1.charAt(i1) == s2.charAt(i2);

        char[] arr1 = s1.toCharArray();
        char[] arr2 = s2.toCharArray();
        Arrays.sort(arr1, i1, i1 + len);
        Arrays.sort(arr2, i2, i2 + len);
        for (int i = i1, j = i2; i < i1 + len; i++, j++) {
            if (arr1[i] != arr2[j]) return false;
        }
        return true;
    }

    private boolean isPerm2(String s1, String s2, int i1, int i2, int len) {
        if (len == 1) return s1.charAt(i1) == s2.charAt(i2);

        Map<Character, Integer> map = new HashMap<>();
        charMap(map, s1, i1, i1 + len, 1);
        charMap(map, s2, i2, i2 + len, -1);
        return map.isEmpty();
    }

    private void charMap(Map<Character, Integer> map, String s,
                         int start, int end, int increase) {
        for (int i = start; i < end; i++) {
            char c = s.charAt(i);
            if (!map.containsKey(c)) {
                map.put(c, increase);
            } else {
                int count = map.get(c) + increase;
                if (count == 0) {
                    map.remove(c);
                } else {
                    map.put(c, count);
                }
            }
        }
    }

    // Solution of Choice
    // Dynamic Programming(bottom-up)
    // beats 28.87%(20 ms)
    public boolean isScramble2(String s1, String s2) {
        int len = s1.length();
        // s1[i..i+l-1] is a scramble of s2[j..j+l-1]
        boolean[][][] dp = new boolean[len][len][len + 1];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                dp[i][j][1] = (s1.charAt(i) == s2.charAt(j));
            }
        }
        for (int l = 2; l <= len; l++) {
            for (int i = 0; i <= len - l; i++) {
                for (int j = 0; j <= len - l; j++) {
                    for (int k = 1; k < l; k++) {
                        dp[i][j][l] |= (dp[i][j][k] && dp[i + k][j + k][l - k])
                                       || (dp[i][j + l - k][k] && dp[i + k][j][l - k]);
                    }
                }
            }
        }
        return dp[0][0][len];
    }

    // Solution of Choice
    // Dynamic Programming(top-down) + Pruning
    // beats 56.35%(7 ms)
    public boolean isScramble3(String s1, String s2) {
        int len = s1.length();
        return isScramble3(s1.toCharArray(), s2.toCharArray(), 0, 0,
                           len, new Boolean[len][len][len + 1], true);
    }

    private boolean isScramble3(char[] s1, char[] s2, int i1, int i2, int len,
                                Boolean[][][] memo, boolean check) {

        if (len == 1) return s1[i1] == s2[i2];

        Boolean cached = memo[i1][i2][len];
        if (cached != null) return cached;

        if (check) {
            int[] count = new int[26]; // or 256
            for (int j = 0; j < len; j++) {
                count[s1[i1 + j] - 'a']++;
                count[s2[i2 + j] - 'a']--;
            }
            for (int c : count) {
                if (c != 0) return memo[i1][i2][len] = false;
            }
        }

        boolean res = false;
        for (int j = 1; j < len; j++) {
            if (isScramble3(s1, s2, i1, i2, j, memo, true)
                && isScramble3(s1, s2, i1 + j, i2 + j, len - j, memo, false)
                || isScramble3(s1, s2, i1, i2 + len - j, j, memo, true)
                && isScramble3(s1, s2, i1 + j, i2, len - j, memo, false)) {
                res = true;
                break;
            }
        }
        return memo[i1][i2][len] = res;
    }

    // Recursion + Pruning
    // beats 91.45%(4 ms)
    public boolean isScramble4(String s1, String s2) {
        return isScramble4(s1, s2, true);
    }

    private boolean isScramble4(String s1, String s2, boolean check) {
        if (s1.equals(s2)) return true;

        int len = s1.length();
        if (check) {
            // beats 67.44%(6 ms) if use character sort
            // char[] arr1 = s1.toCharArray();
            // char[] arr2 = s2.toCharArray();
            // Arrays.sort(arr1);
            // Arrays.sort(arr2);
            // if (!Arrays.equals(arr1, arr2)) return false;
            int[] count = new int[26]; // or 256
            for (int i = 0; i < len; i++) {
                count[s1.charAt(i) - 'a']++;
                count[s2.charAt(i) - 'a']--;
            }
            for (int c : count) {
                if (c != 0) return false;
            }
        }

        for (int i = 1; i < len; i++) {
            String s11 = s1.substring(0, i);
            String s12 = s1.substring(i);
            if (isScramble4(s11, s2.substring(0, i), true)
                && isScramble4(s12, s2.substring(i), false)
                || isScramble4(s11, s2.substring(len - i, len), true)
                && isScramble4(s12, s2.substring(0, len - i), false)) {
                return true;
            }
        }
        return false;
    }

    void test(String s1, String s2, boolean expected) {
        assertEquals(expected, isScramble(s1, s2));
        assertEquals(expected, isScramble2(s1, s2));
        assertEquals(expected, isScramble3(s1, s2));
        assertEquals(expected, isScramble4(s1, s2));
    }

    @Test
    public void test1() {
        test("xreat", "rgtae", false);
        test("ab", "ab", true);
        test("ab", "ba", true);
        test("abc", "bac", true);
        test("great", "rgtae", true);
        test("abcd", "dcba", true);
        test("abcde", "dceba", true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ScrambleStr");
    }
}
