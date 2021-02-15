import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1751: https://leetcode.com/problems/maximum-number-of-events-that-can-be-attended-ii/
//
// You are given an array of events where events[i] = [startDayi, endDayi, valuei]. The ith event
// starts at startDayi and ends at endDayi, and if you attend this event, you will receive a value
// of valuei. You are also given an integer k which represents the maximum number of events you can
// attend. You can only attend one event at a time. If you choose to attend an event, you must
// attend the entire event. Note that the end day is inclusive: that is, you cannot attend two
// events where one of them starts and the other ends on the same day.
// Return the maximum sum of values that you can receive by attending events.
//
// Constraints:
// 1 <= k <= events.length
// 1 <= k * events.length <= 10^6
// 1 <= startDayi <= endDayi <= 10^9
// 1 <= valuei <= 10^6
public class MaxEventsAttendedII {
    // Bottom-up Dynamic Programming + Binary Search
    // time complexity: O(N*(log(N)+K)), space complexity: O(N*K)
    // 43 ms(76.93%), 82.1 MB(75.88%) for 66 tests
    public int maxValue(int[][] events, int k) {
        int n = events.length;
        Arrays.sort(events, Comparator.comparingInt(a -> a[1]));
        int[][] dp = new int[n + 1][k + 1];
        for (int i = 0; i < n; i++) {
            int prev = binarySearch(events, events[i][0]);
            for (int j = 0; j < k; j++) {
                int v = Math.max(dp[i + 1][j], dp[i][j + 1]);
                dp[i + 1][j + 1] = Math.max(v, dp[prev][j] + events[i][2]);
            }
        }
        return dp[n][k];
    }

    private int binarySearch(int[][] a, int key) {
        int low = -1;
        for (int high = a.length; low < high - 1; ) {
            int mid = (low + high) >>> 1;
            if (a[mid][1] < key) {
                low = mid;
            } else {
                high = mid;
            }
        }
        return low + 1;
    }

    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(N^2)), space complexity: O(N*K)
    // 42 ms(77.50%), 607.7 MB(5.06%) for 66 tests
    public int maxValue2(int[][] events, int k) {
        Arrays.sort(events, Comparator.comparingInt(a -> a[0]));
        int n = events.length;
        return dfs(events, n, 0, k, new Integer[n + 1][k + 1]);
    }

    private int dfs(int[][] events, int n, int cur, int k, Integer[][] dp) {
        if (cur >= n || k == 0) { return 0; }
        if (dp[cur][k] != null) { return dp[cur][k]; }

        int i = cur + 1;
        for (int end = events[cur][1]; i < n && events[i][0] <= end; i++) {}
        return dp[cur][k] = Math.max(dfs(events, n, cur + 1, k, dp),
                                     events[cur][2] + dfs(events, n, i, k - 1, dp));
    }

    // Recursion + Dynamic Programming(Top-Down) + Binary Search
    // time complexity: O(N^2)), space complexity: O(N*K)
    // 48 ms(68.66%), 607.7 MB(5.06%) for 66 tests
    public int maxValue3(int[][] events, int k) {
        Arrays.sort(events, Comparator.comparingInt(a -> a[0]));
        int n = events.length;
        return dfs3(events, n, 0, k, new Integer[n + 1][k + 1]);
    }

    private int dfs3(int[][] events, int n, int cur, int k, Integer[][] dp) {
        if (cur >= n || k == 0) { return 0; }
        if (dp[cur][k] != null) { return dp[cur][k]; }

        int prev = search(events, events[cur][1]);
        return dp[cur][k] = Math.max(dfs3(events, n, cur + 1, k, dp),
                                     events[cur][2] + dfs3(events, n, prev, k - 1, dp));
    }

    private int search(int[][] a, int key) {
        int low = 0;
        for (int high = a.length; low < high - 1; ) {
            int mid = (low + high) >>> 1;
            if (a[mid][0] <= key) {
                low = mid;
            } else {
                high = mid;
            }
        }
        return low + 1;
    }

    private void test(int[][] events, int k, int expected) {
        assertEquals(expected, maxValue(events, k));
        assertEquals(expected, maxValue2(events, k));
        assertEquals(expected, maxValue3(events, k));
    }

    @Test public void test() {
        test(new int[][] {{1, 1, 1}, {2, 2, 2}, {3, 3, 3}, {4, 4, 4}}, 3, 9);
        test(new int[][] {{1, 2, 4}, {3, 4, 3}, {2, 3, 1}}, 2, 7);
        test(new int[][] {{1, 2, 4}, {3, 4, 3}, {2, 3, 10}}, 2, 10);
        test(new int[][] {{1, 3, 4}, {2, 4, 1}, {1, 1, 4}, {3, 5, 1}, {2, 5, 5}}, 3, 9);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
