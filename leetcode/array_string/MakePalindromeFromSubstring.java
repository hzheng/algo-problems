import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1177: https://leetcode.com/problems/can-make-palindrome-from-substring/
//
// Given a string s, we make queries on substrings of s. For each query
// queries[i] = [left, right, k], we may rearrange the substring s[left], ..., s[right], and then
// choose up to k of them to replace with any lowercase English letter.
// If the substring is possible to be a palindrome string after the operations above, the result of
// the query is true. Otherwise, the result is false.
// Return an array answer[], where answer[i] is the result of the i-th query queries[i].
// Note that: Each letter is counted individually for replacement so if for example
// s[left..right] = "aaa", and k = 2, we can only replace two of the letters. Also, note that the
// initial string s is never modified by any query.
//
// Constraints:
// 1 <= s.length, queries.length <= 10^5
// 0 <= queries[i][0] <= queries[i][1] < s.length
// 0 <= queries[i][2] <= s.length
// s only contains lowercase English letters.
public class MakePalindromeFromSubstring {
    // time complexity: O(N+Q), space complexity: O(N+Q)
    // 93 ms(8.08%), 110.1 MB(34.04%) for 31 tests
    public List<Boolean> canMakePaliQueries(String s, int[][] queries) {
        char[] cs = s.toCharArray();
        int n = cs.length;
        int[][] count = new int[26][n + 1];
        for (int i = 0; i < n; i++) {
            for (int c = 0, index = cs[i] - 'a'; c < 26; c++) {
                count[c][i + 1] = count[c][i] + ((c == index) ? 1 : 0);
            }
        }
        List<Boolean> res = new ArrayList<>();
        for (int[] query : queries) {
            res.add(canMakePali(count, query[0], query[1], query[2]));
        }
        return res;
    }

    private boolean canMakePali(int[][] count, int left, int right, int k) {
        int odds = 0;
        for (int[] cnt : count) {
            odds += (cnt[right + 1] - cnt[left]) % 2;
        }
        return odds / 2 <= k;
    }

    // time complexity: O(N+Q), space complexity: O(N+Q)
    // 61 ms(51.49%), 110 MB(36.60%) for 31 tests
    public List<Boolean> canMakePaliQueries2(String s, int[][] queries) {
        List<Boolean> res = new ArrayList<>();
        int n = s.length();
        int[][] count = new int[n + 1][26];
        for (int i = 0; i < n; i++) {
            count[i + 1] = count[i].clone();
            count[i + 1][s.charAt(i) - 'a']++;
        }
        for (int[] q : queries) {
            int odds = 0;
            for (int i = count[0].length - 1; i >= 0; i--) {
                odds += (count[q[1] + 1][i] - count[q[0]][i]) % 2;
            }
            res.add(odds / 2 <= q[2]);
        }
        return res;
    }

    // time complexity: O(N+Q), space complexity: O(N+Q)
    // 61 ms(51.49%), 105 MB(58.30%) for 31 tests
    public List<Boolean> canMakePaliQueries3(String s, int[][] queries) {
        int n = s.length();
        boolean[][] odds = new boolean[n + 1][26];
        for (int i = 0; i < n; i++) {
            odds[i + 1] = odds[i].clone();
            odds[i + 1][s.charAt(i) - 'a'] ^= true;
        }
        List<Boolean> res = new ArrayList<>();
        for (int[] q : queries) {
            int sum = 0;
            for (int i = odds[0].length - 1; i >= 0; i--) {
                sum += (odds[q[1] + 1][i] ^ odds[q[0]][i]) ? 1 : 0;
            }
            res.add(sum / 2 <= q[2]);
        }
        return res;
    }

    // Bit Manipulation
    // time complexity: O(N+Q), space complexity: O(N+Q)
    // 7 ms(99.57%), 96.6 MB(94.47%) for 31 tests
    public List<Boolean> canMakePaliQueries4(String s, int[][] queries) {
        int n = s.length();
        int[] odds = new int[n + 1];
        for (int i = 0; i < n; i++) {
            odds[i + 1] = odds[i] ^ 1 << (s.charAt(i) - 'a');
        }
        List<Boolean> res = new ArrayList<>();
        for (int[] q : queries) {
            res.add(Integer.bitCount(odds[q[1] + 1] ^ odds[q[0]]) / 2 <= q[2]);
        }
        return res;
    }

    private void test(String s, int[][] queries, Boolean[] expected) {
        List<Boolean> expectedList = Arrays.asList(expected);
        assertEquals(expectedList, canMakePaliQueries(s, queries));
        assertEquals(expectedList, canMakePaliQueries2(s, queries));
        assertEquals(expectedList, canMakePaliQueries3(s, queries));
        assertEquals(expectedList, canMakePaliQueries4(s, queries));
    }

    @Test public void test() {
        test("abcda", new int[][] {{3, 3, 0}, {1, 2, 0}, {0, 3, 1}, {0, 3, 2}, {0, 4, 1}},
             new Boolean[] {true, false, false, true, true});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
