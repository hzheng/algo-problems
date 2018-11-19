import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC943: https://leetcode.com/problems/find-the-shortest-superstring/
//
// Given an array A of strings, find any smallest string that contains each
// string in A as a substring.
// We may assume that no string in A is substring of another string in A.
// Note:
// 1 <= A.length <= 12
// 1 <= A[i].length <= 20
public class ShortestSuperstring {
    // Greedy (passed the test, but should be wrong for an NP-hard problem)
    // beats %(7 ms for 72 tests)
    public String shortestSuperstring(String[] A) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < A.length; i++) {
            list.add(A[i]);
        }
        for (int len = list.size(); len > 1; len--) {
            int maxOverlap = -1;
            int index1 = 0;
            int index2 = 0;
            for (int i = 0; i < len; i++) {
                for (int j = i + 1; j < len; j++) {
                    int overlap1 = overlap(list.get(i), list.get(j));
                    int overlap2 = overlap(list.get(j), list.get(i));
                    if (maxOverlap < overlap1) {
                        maxOverlap = overlap1;
                        index1 = i;
                        index2 = j;
                    }
                    if (maxOverlap < overlap2) {
                        maxOverlap = overlap2;
                        index1 = j;
                        index2 = i;
                    }
                }
            }
            list.add(list.get(index1) + list.get(index2).substring(maxOverlap));
            list.remove(index1);
            list.remove(index2 > index1 ? index2 - 1 : index2);
        }
        return list.get(0);
    }

    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(N ^ 2 * (2 ^ N + W), space complexity: O(N * 2 ^ N)
    // beats %(27 ms for 72 tests)
    public String shortestSuperstring2(String[] A) {
        int n = A.length;
        int[][] dp = new int[1 << n][n + 1];
        int[][] overlaps = new int[n][n];
        for (int[] a : dp) {
            Arrays.fill(a, -1);
        }
        for (int[] a : overlaps) {
            Arrays.fill(a, -1);
        }
        maxOverlap(A, ((1 << n) - 1), n, dp, overlaps);
        return buildString(A, dp, overlaps);
    }

    private String buildString(String[] A, int[][] dp, int[][] overlaps) {
        Stack<Integer> stack = new Stack<>();
        int n = A.length;
        for (int i = 0, set = (1 << n) - 1, prev = n, maxIndex; i < n; i++, prev = maxIndex) {
            int max = -1;
            maxIndex = -1;
            for (int j = 0; j < n; j++) {
                int subset = set & ~(1 << (n - 1 - j));
                if (subset == set) continue;

                int cur = dp[subset][j] + overlap(A, j, prev, overlaps);
                if (cur > max) {
                    max = cur;
                    maxIndex = j;
                }
            }
            stack.push(maxIndex);
            set &= ~(1 << (n - 1 - maxIndex));
        }
        String res = A[stack.pop()];
        while (!stack.isEmpty()) {
            String next = A[stack.pop()];
            res += next.substring(overlap(res, next));
        }
        return res;
    }

    private int maxOverlap(String[] A, int subset, int last, int[][] dp, int[][] overlaps) {
        if (dp[subset][last] >= 0) return dp[subset][last];

        int res = 0;
        for (int i = 0, n = A.length; i < n; i++) {
            int mask = subset & ~(1 << (n - 1 - i));
            if (mask != subset) {
                res = Math.max(res, maxOverlap(A, mask, i, dp, overlaps)
                                    + overlap(A, i, last, overlaps));
            }
        }
        return dp[subset][last] = res;
    }

    private int overlap(String str1, String str2) {
        int len1 = str1.length();
        int len2 = str2.length();
        for (int len = Math.min(len1, len2), i = len; i > 0; i--) {
            if (str1.endsWith(str2.substring(0, i))) return i;
        }
        return 0;
    }

    private int overlap(String[] A, int i, int j, int[][] overlaps) {
        if (i == A.length || j == A.length) return 0;

        if (overlaps[i][j] < 0) {
            overlaps[i][j] = overlap(A[i], A[j]);
        }
        return overlaps[i][j];
    }

    // Dynamic Programming(Bottom-Up)
    // time complexity: O(N ^ 2 * (2 ^ N + W), space complexity: O(N * 2 ^ N)
    // beats %(43 ms for 72 tests)
    public String shortestSuperstring3(String[] A) {
        int n = A.length;
        int[][] dp = new int[1 << n][n];
        int[][] overlaps = new int[n][n];
        for (int[] a : overlaps) {
            Arrays.fill(a, -1);
        }
        for (int set = 0; set < dp.length; set++) {
            for (int i = 0; i < n; i++) {
                if (((set >> (n - 1 - i)) & 1) != 0) continue;

                for (int j = 0; j < n; j++) {
                    int subset = set & ~(1 << (n - 1 - j));
                    if (subset == set) continue;

                    dp[set][i] = Math.max(dp[set][i], dp[subset][j] + overlap(A, j, i, overlaps));
                }
            }
        }
        return buildString(A, dp, overlaps);
    }

    void test(String[] A, String expected) {
        assertEquals(expected, shortestSuperstring(A));
        assertEquals(expected, shortestSuperstring2(A));
        assertEquals(expected, shortestSuperstring3(A));
    }

    @Test
    public void test() {
        // test(new String[] {"alex", "loves", "leetcode"}, "alexlovesleetcode");
        test(new String[] {"ift", "efd", "dnete", "tef", "fdn"}, "iftefdnete");
        test(new String[] {"catg", "ctaagt", "gcta", "ttca", "atgcatc"}, "gctaagttcatgcatc");
        test(new String[] {"wmiy", "yarn", "rnnwc", "arnnw", "wcj"}, "wmiyarnnwcj");
        test(new String[] {"chakgmeinq", "lhdbntkf", "mhkelhye", "hdbntkfch", "kfchakgme",
                           "wymhkelh", "kgmeinqw"},
             "lhdbntkfchakgmeinqwymhkelhye");
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
