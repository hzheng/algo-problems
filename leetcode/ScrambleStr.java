import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// Given a string s1, we may represent it as a binary tree by partitioning it to
// two non-empty substrings recursively. To scramble the string, we may choose
// any non-leaf node and swap its two children.
// Given two strings s1 and s2 of the same length, determine if s2 is a
// scrambled string of s1.
public class ScrambleStr {
    // beats 71.43%(34.88% if use 'isPerm2')
    public boolean isScramble(String s1, String s2) {
        if (s1.equals(s2)) return true;

        int len = s1.length();
        if (len <= 1) return false;

        // if (!isPerm(s1, s2, 0, 0, len)) return false;
        // if (len < 3) return true;

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

    // beats 18.74%
    public boolean isScramble2(String s1, String s2) {
        if (s1.equals(s2)) return true;

        int len = s1.length();
        if (len <= 1) return false;

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

    // beats 66.42%
    public boolean isScramble3(String s1, String s2) {
        if (s1.equals(s2)) return true;

        int len = s1.length();
        if (len <= 1) return false;

        char[] arr1 = s1.toCharArray();
        char[] arr2 = s2.toCharArray();
        Arrays.sort(arr1);
        Arrays.sort(arr2);
        if (!Arrays.equals(arr1, arr2)) return false;

        for (int i = 1; i < len; i++) {
            String s11 = s1.substring(0, i);
            String s12 = s1.substring(i);
            String s21 = s2.substring(0, i);
            String s22 = s2.substring(i);

            if (isScramble(s11, s21) && isScramble(s12, s22)) return true;

            String s23 = s2.substring(0, len - i);
            String s24 = s2.substring(len - i, len);
            if (isScramble(s11, s24) && isScramble(s12, s23)) return true;
        }

        return false;
    }

    void test(String s1, String s2, boolean expected) {
        assertEquals(expected, isScramble(s1, s2));
        assertEquals(expected, isScramble2(s1, s2));
        assertEquals(expected, isScramble3(s1, s2));
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
