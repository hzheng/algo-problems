import java.util.*;
import java.util.stream.Stream;

import java.util.function.Function;

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
                int subset = set & ~(1 << j);
                if (subset == set) continue;

                int cur = dp[subset][j] + overlap(A, j, prev, overlaps);
                if (cur > max) {
                    max = cur;
                    maxIndex = j;
                }
            }
            stack.push(maxIndex);
            set &= ~(1 << maxIndex);
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
            int mask = subset & ~(1 << i);
            if (mask != subset) {
                res = Math.max(res, maxOverlap(A, mask, i, dp, overlaps)
                                    + overlap(A, i, last, overlaps));
            }
        }
        return dp[subset][last] = res;
    }

    private int overlap(String s1, String s2) {
        for (int len = Math.min(s1.length(), s2.length()), i = len; i > 0; i--) {
            if (s1.endsWith(s2.substring(0, i))) return i;
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
                if (((set >> i) & 1) != 0) continue;

                for (int j = 0; j < n; j++) {
                    int subset = set & ~(1 << j);
                    if (subset == set) continue;

                    dp[set][i] = Math.max(dp[set][i], dp[subset][j] + overlap(A, j, i, overlaps));
                }
            }
        }
        return buildString(A, dp, overlaps);
    }

    // https://leetcode.com/problems/find-the-shortest-superstring/solution/
    // Dynamic Programming(Bottom-Up)
    // time complexity: O(N ^ 2 * (2 ^ N + W), space complexity: O(N * 2 ^ N)
    // beats %(37 ms for 72 tests)
    public String shortestSuperstring4(String[] A) {
        int n = A.length;
        int[][] overlaps = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) continue;

                int len = Math.min(A[i].length(), A[j].length());
                for (int k = len; k >= 0; k--) {
                    if (A[i].endsWith(A[j].substring(0, k))) {
                        overlaps[i][j] = k;
                        break;
                    }
                }
            }
        }
        int[][] dp = new int[1 << n][n];
        int[][] parent = new int[1 << n][n];
        for (int set = 0; set < (1 << n); set++) {
            Arrays.fill(parent[set], -1);
            for (int i = 0; i < n; i++) {
                if (((set >> i) & 1) == 0) continue;

                int subset = set ^ (1 << i);
                if (subset == 0) continue;

                for (int j = 0; j < n; j++) {
                    if (((subset >> j) & 1) == 0) continue;

                    int val = dp[subset][j] + overlaps[j][i];
                    if (val > dp[set][i]) {
                        dp[set][i] = val;
                        parent[set][i] = j;
                    }
                }
            }
        }
        int[] perm = new int[n];
        boolean[] visited = new boolean[n];
        int p = 0; // the last element of perm
        for (int i = 0; i < n; i++) {
            if (dp[(1 << n) - 1][i] > dp[(1 << n) - 1][p]) {
                p = i;
            }
        }
        int index = 0;
        for (int mask = (1 << n) - 1, p2; p != -1; mask ^= 1 << p, p = p2) {
            perm[index++] = p;
            visited[p] = true;
            p2 = parent[mask][p];
        }
        for (int i = 0; i < index / 2; i++) { // reverse
            int tmp = perm[i];
            perm[i] = perm[index - 1 - i];
            perm[index - 1 - i] = tmp;
        }
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                perm[index++] = i;
            }
        }
        StringBuilder buf = new StringBuilder(A[perm[0]]);
        for (int i = 1; i < n; i++) {
            int overlap = overlaps[perm[i - 1]][perm[i]];
            buf.append(A[perm[i]].substring(overlap));
        }
        return buf.toString();
    }

    void test(String[] A, String... expected) {
        ShortestSuperstring s = new ShortestSuperstring();
        test(A, s::shortestSuperstring, expected);
        test(A, s::shortestSuperstring2, expected);
        test(A, s::shortestSuperstring3, expected);
        test(A, s::shortestSuperstring4, expected);
    }

    void test(String[] A, Function<String[], String> fun, String... expected) {
        String res = fun.apply(A);
        assertTrue(Stream.of(expected).anyMatch(x -> x.equals(res)));
    }

    @Test
    public void test() {
        test(new String[] {"alex", "loves", "leetcode"}, "alexlovesleetcode", "leetcodealexloves",
             "leetcodelovesalex");
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
