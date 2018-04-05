import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC646: https://leetcode.com/problems/maximum-length-of-pair-chain/
//
// You are given n pairs of numbers. In every pair, the first number is always
// smaller than the second number. Define a pair (c, d) can follow another pair
// (a, b) if and only if b < c. Chain of pairs can be formed in this fashion.
// Given a set of pairs, find the length longest chain which can be formed. You
// needn't use up all the given pairs. You can select pairs in any order.
// Note: The number of given pairs will be in the range [1, 1000].
public class FindLongestChain {
    // Heap + Greedy
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 86.85%(40 ms for 202 tests)
    public int findLongestChain(int[][] pairs) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return a[1] - b[1];
            }
        });
        for (int[] pair : pairs) {
            pq.offer(pair);
        }
        int res = 0;
        for (int prev = Integer.MIN_VALUE; !pq.isEmpty(); ) {
            int[] cur = pq.poll();
            if (cur[0] > prev) {
                res++;
                prev = cur[1];
            }
        }
        return res;
    }

    // Sort + Greedy
    // time complexity: O(N * log(N)), space complexity: O(log(N))
    // beats 91.41%(36 ms for 202 tests)
    public int findLongestChain2(int[][] pairs) {
        // Arrays.sort(pairs, (a, b) -> a[1] - b[1]);
        Arrays.sort (pairs, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return a[1] - b[1];
            }
        });
        int res = 0;
        int prev = Integer.MIN_VALUE;
        for (int[] pair : pairs) {
            if (pair[0] > prev) {
                res++;
                prev = pair[1];
            }
        }
        return res;
    }

    // Sort + Greedy
    // time complexity: O(N * log(N)), space complexity: O(log(N))
    // beats 95.03%(34 ms for 202 tests)
    public int findLongestChain2_2(int[][] pairs) {
        // Arrays.sort(pairs, (a, b) -> a[1] - b[1]);
        Arrays.sort (pairs, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return a[1] - b[1];
            }
        });
        int res = 0;
        for (int i = 0, n = pairs.length; i < n; i++, res++) {
            int prev = pairs[i][1];
            for (; i + 1 < n && pairs[i + 1][0] <= prev; i++) {}
        }
        return res;
    }

    // Sort + Dynamic Programming
    // time complexity: O(N ^ 2, space complexity: O(N)
    // beats 81.88%(71 ms for 202 tests)
    public int findLongestChain3(int[][] pairs) {
        // Arrays.sort(pairs, (a, b) -> a[1] - b[1]);
        Arrays.sort (pairs, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return a[0] - b[0];
            }
        });
        int n = pairs.length;
        int[] dp = new int[n]; // length of the longest chain ending at pairs[i]
        Arrays.fill(dp, 1);
        for (int j = 1; j < n; j++) {
            for (int i = 0; i < j; i++) {
                if (pairs[i][1] < pairs[j][0]) {
                    dp[j] = Math.max(dp[j], dp[i] + 1);
                }
            }
        }
        return dp[n - 1]; // dp is decreasing
    }

    void test(int[][] pairs, int expected) {
        assertEquals(expected, findLongestChain(pairs));
        assertEquals(expected, findLongestChain2(pairs.clone()));
        assertEquals(expected, findLongestChain2_2(pairs.clone()));
        assertEquals(expected, findLongestChain3(pairs.clone()));
    }

    @Test
    public void test() {
        test(new int[][] {{1, 2}, {2, 5}, {2, 4}}, 1);
        test(new int[][] {{1, 2}, {2, 5}, {3, 4}}, 2);
        test(new int[][] {{1, 2}, {2, 3}, {3, 4}}, 2);
        test(new int[][] {{5, 6}, {1, 2}, {2, 3}, {3, 4}}, 3);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
