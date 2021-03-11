import org.junit.Test;

import static org.junit.Assert.*;

// LC1781: https://leetcode.com/problems/sum-of-beauty-of-all-substrings/
//
// The beauty of a string is the difference in frequencies between the most frequent and least
// frequent characters. For example, the beauty of "abaacc" is 3 - 1 = 2.
// Given a string s, return the sum of beauty of all of its substrings.
//
// Constraints:
// 1 <= s.length <= 500
// s consists of only lowercase English letters.
public class BeautySum {
    // time complexity: O(N^2), space complexity: O(N)
    // 94 ms(80.00%), 39.1 MB(60.00%) for 57 tests
    public int beautySum(String s) {
        char[] cs = s.toCharArray();
        int n = cs.length;
        int[][] freqs = new int[26][n + 1];
        for (int i = 0; i < n; i++) {
            for (char c = 'a'; c <= 'z'; c++) {
                int[] freq = freqs[c - 'a'];
                freq[i + 1] = freq[i] + ((c == cs[i]) ? 1 : 0);
            }
        }
        int res = 0;
        for (int len = 1; len <= n; len++) {
            for (int start = 0; start + len - 1 < n; start++) {
                int max = 0;
                int min = n;
                for (char c = 'a'; c <= 'z'; c++) {
                    int[] freq = freqs[c - 'a'];
                    int v = freq[start + len] - freq[start];
                    if (v != 0) {
                        max = Math.max(max, v);
                        min = Math.min(min, v);
                    }
                }
                res += max - min;
            }
        }
        return res;
    }

    // time complexity: O(N^2), space complexity: O(1)
    // 64 ms(100.00%), 38.6 MB(60.00%) for 57 tests
    public int beautySum2(String s) {
        int res = 0;
        for (int i = 0, n = s.length(); i < n; i++) {
            int[] freq = new int[26];
            for (int j = i; j < n; j++) {
                freq[s.charAt(j) - 'a']++;
                res += beauty(freq);
            }
        }
        return res;
    }

    private int beauty(int[] freq) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = freq.length - 1; i >= 0; i--) {
            if (freq[i] > 0) {
                min = Math.min(min, freq[i]);
                max = Math.max(max, freq[i]);
            }
        }
        return max - min;
    }

    private void test(String s, int expected) {
        assertEquals(expected, beautySum(s));
        assertEquals(expected, beautySum2(s));
    }

    @Test public void test() {
        test("aabcb", 5);
        test("aabcbaa", 17);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
