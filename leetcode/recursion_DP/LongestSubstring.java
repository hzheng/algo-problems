import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC395: https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/
//
// Find the length of the longest substring T of a given string (consists of
// lowercase letters only) such that every character in T appears no less than
// k times.
public class LongestSubstring {
    // Dynamic Programming
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 0.67%(251 ms for 28 tests)
    public int longestSubstring(String s, int k) {
        int len = s.length();
        if (k <= 1) return len;

        if (k > len) return 0;

        int[][] dp = new int[len + 1][26];
        // dp[0] = new int[26];
        // for (int i = 1; i <= len; i++) {
        //     int[] count = dp[i - 1].clone();
        //     count[s.charAt(i - 1) - 'a']++;
        //     dp[i] = count;
        // }
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < 26; j++) {
                dp[i + 1][j] = dp[i][j];
            }
            dp[i + 1][s.charAt(i) - 'a']++;
        }
        int maxLen = 0;
        for (int i = 0; i < len; i++) {
middle:
            for (int j = i + k - 1; j < len; j++) {
                for (int n = 25; n >= 0; n--) {
                    int count = dp[j + 1][n] - dp[i][n];
                    if (count > 0 && count < k) continue middle;
                }
                maxLen = Math.max(maxLen, j - i + 1);
            }
        }
        return maxLen;
    }

    // Dynamic Programming
    // time complexity: O(N ^ 2), space complexity: O(N)
    // Time Limit Exceeded
    public int longestSubstring_2(String s, int k) {
        int len = s.length();
        if (k <= 1) return len;

        if (k > len) return 0;

        int[][] dp = new int[len + 1][26];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < 26; j++) {
                dp[i + 1][j] = dp[i][j];
            }
            dp[i + 1][s.charAt(i) - 'a']++;
        }
        int maxLen = 0;
outer:
        for (int i = 0; i < len; i++) {
middle:
            for (int j = len - 1; j >= i + k - 1; j--) {
                for (int n = 25; n >= 0; n--) {
                    int count = dp[j + 1][n] - dp[i][n];
                    if (count > 0 && count < k) continue middle;
                }
                maxLen = Math.max(maxLen, j - i + 1);
                continue outer;
            }
        }
        return maxLen;
    }

    // Dynamic Programming
    // time complexity: O(N ^ 3), space complexity: O(N)
    // Time Limit Exceeded
    public int longestSubstring2(String s, int k) {
        int len = s.length();
        int[][] dp = new int[26][len + 1];
        int max = 0;
        for (int i = 0; i < len; i++) {
            int index = s.charAt(i) - 'a';
            for (int j = 0; j < 26; j++) {
                dp[j][i + 1] = dp[j][i] + ((j == index) ? 1 : 0);
            }
        }
outer:
        for (int i = 0; i < len; i++) {
middle:
            for (int j = len - 1; j >= i; j--) {
                for (int m = i; m <= j; m++) {
                    int index = s.charAt(m) - 'a';
                    if (dp[index][j + 1] - dp[index][i] < k) continue middle;
                }
                max = Math.max(max, j - i + 1);
                continue outer;
            }
        }
        return max;
    }

    // Divide & Conquer/Recursion
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 13.56%(99 ms for 28 tests)
    public int longestSubstring3(String s, int k) {
        return longestSubstring3(s.toCharArray(), k, 0, s.length());
    }

    private int longestSubstring3(char[] chars, int k, int start, int end) {
        int len = end - start;
        if (k > len) return 0;

        int[] counts = new int[26];
        for (int i = start; i < end; i++) {
            counts[chars[i] - 'a']++;
        }
        for (int i = start; i < end; i++) {
            if (counts[chars[i] - 'a'] < k) {
                return Math.max(longestSubstring3(chars, k, start, i),
                                longestSubstring3(chars, k, i + 1, end));
            }
        }
        return len;
    }

    // Solution of Choice
    // Divide & Conquer/Recursion
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 86.33%(3 ms for 28 tests)
    public int longestSubstring4(String s, int k) {
        return longestSubstring4(s.toCharArray(), k, 0, s.length());
    }

    private int longestSubstring4(char[] chars, int k, int start, int end) {
        int len = end - start;
        if (k > len) return 0;

        int[] counts = new int[26];
        for (int i = start; i < end; i++) {
            counts[chars[i] - 'a']++;
        }
        int max = 0;
        int split = start;
        for (int i = start; i < end; i++) {
            if (counts[chars[i] - 'a'] < k) {
                max = Math.max(max, longestSubstring4(chars, k, split, i));
                split = i + 1;
            }
        }
        return (split == start)
               ? len : Math.max(max, longestSubstring4(chars, k, split, end));
    }

    // Divide & Conquer/Recursion + Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // beats 43.67%(9 ms for 28 tests)
    public int longestSubstring5(String s, int k) {
        int len = s.length();
        if (k <= 1) return len;

        if (k > len) return 0;

        int[][] dp = new int[len + 1][26];
        for (int i = 0; i < len; i++) {
            int index = s.charAt(i) - 'a';
            for (int j = 0; j < 26; j++) {
                dp[i + 1][j] = dp[i][j] + ((j == index) ? 1 : 0);
            }
        }
        return longestSubstring(s.toCharArray(), k, 0, len, dp);
    }

    private int longestSubstring(char[] chars, int k, int start, int end, int[][] dp) {
        int len = end - start;
        if (k > len) return 0;

        int max = 0;
        int split = start;
        int[] from = dp[start];
        int[] to = dp[end];
        for (int i = start; i < end; i++) {
            int index = chars[i] - 'a';
            if (to[index] - from[index] < k) {
                max = Math.max(max, longestSubstring(chars, k, split, i, dp));
                split = i + 1;
            }
        }
        return (split == start)
               ? len : Math.max(max, longestSubstring(chars, k, split, end, dp));
    }

    // Divide & Conquer/Recursion
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 11.44%(103 ms for 28 tests)
    public int longestSubstring6(String s, int k) {
        return longestSubstring6(s.toCharArray(), k, 0, s.length());
    }

    private int longestSubstring6(char[] chars, int k, int start, int end) {
        if (k > end - start) return 0;

        int[] counts = new int[26];
        for (int i = start; i < end; i++) {
            counts[chars[i] - 'a']++;
        }
        for (int i = 0; i < 26; i++) {
            if (counts[i] >= k || counts[i] == 0) continue;
            for (int j = start; j < end; j++) {
                if (chars[j] == 'a' + i) {
                    return Math.max(longestSubstring6(chars, k, start, j),
                                    longestSubstring6(chars, k, j + 1, end));
                }
            }
        }
        return end - start;
    }

    // Bit Manipulation
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 13.56%(99 ms for 28 tests)
    public int longestSubstring7(String s, int k) {
        int max = 0;
        int len = s.length();
        for (int i = 0, maxLast = 0; i + k <= len; i = maxLast + 1) {
            int[] count = new int[26];
            maxLast = i;
            for (int j = i, mask = 0; j < len; j++) {
                int index = s.charAt(j) - 'a';
                count[index]++;
                if (count[index] < k) {
                    mask |= (1 << index);
                } else {
                    mask &= ~(1 << index);
                }
                if (mask == 0) {
                    max = Math.max(max, j - i + 1);
                    maxLast = j;
                }
            }
        }
        return max;
    }

    // Two Pointers
    // time complexity: O(N), space complexity: O(N)
    // beats 41.11%(10 ms for 28 tests)
    public int longestSubstring8(String s, int k) {
        char[] cs = s.toCharArray();
        int max = 0;
        for (int uniqueCount = 1; uniqueCount <= 26; uniqueCount++) {
            int[] counts = new int[26];
            for (int i = 0, j = 0, unique = 0, noLessThanK = 0; j < cs.length; ) {
                if (unique <= uniqueCount) {
                    int index = cs[j++] - 'a';
                    if (counts[index]++ == 0) {
                        unique++;
                    }
                    if (counts[index] == k) {
                        noLessThanK++;
                    }
                } else {
                    int index = cs[i++] - 'a';
                    if (counts[index]-- == k) {
                        noLessThanK--;
                    }
                    if (counts[index] == 0) {
                        unique--;
                    }
                }
                if (unique == uniqueCount && unique == noLessThanK) {
                    max = Math.max(j - i, max);
                }
            }
        }
        return max;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<String, Integer, Integer> longestSubstring, String name,
              String s, int k, int expected) {
        long t1 = System.nanoTime();
        assertEquals(expected, (int)longestSubstring.apply(s, k));
        if (s.length() > 30) {
            System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        }
    }

    void test(String s, int k, int expected) {
        LongestSubstring l = new LongestSubstring();
        test(l::longestSubstring, "longestSubstring", s, k, expected);
        test(l::longestSubstring_2, "longestSubstring_2", s, k, expected);
        test(l::longestSubstring2, "longestSubstring2", s, k, expected);
        test(l::longestSubstring3, "longestSubstring3", s, k, expected);
        test(l::longestSubstring4, "longestSubstring4", s, k, expected);
        test(l::longestSubstring5, "longestSubstring5", s, k, expected);
        test(l::longestSubstring6, "longestSubstring6", s, k, expected);
        test(l::longestSubstring7, "longestSubstring7", s, k, expected);
        test(l::longestSubstring8, "longestSubstring8", s, k, expected);
    }

    @Test
    public void test1() {
        test("a", 1, 1);
        test("weitong", 2, 0);
        test("aaabb", 3, 3);
        test("ababacb", 3, 0);
        test("bbaaacbd", 3, 3);
        test("aaabb", 4, 0);
        test("ababbc", 2, 5);
        test("ababb", 2, 5);
        test("aaabbccddaaaccabbbcaa", 3, 12);
        test("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
             10, 1674);
        test("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "baaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
             10, 929);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LongestSubstring");
    }
}
