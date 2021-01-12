import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1723: https://leetcode.com/problems/find-minimum-time-to-finish-all-jobs/
//
// You are given an integer array jobs, where jobs[i] is the amount of time it takes to complete the
// ith job. There are k workers that you can assign jobs to. Each job should be assigned to exactly
// one worker. The working time of a worker is the sum of the time it takes to complete all jobs
// assigned to them. Your goal is to devise an optimal assignment such that the maximum working time
// of any worker is minimized.
// Return the minimum possible maximum working time of any assignment.
//
// Constraints:
// 1 <= k <= jobs.length <= 12
// 1 <= jobs[i] <= 10^7
public class MinimumTimeRequired {
    // DFS + Recursion + Backtracking
    // 105 ms(43.15%), 78.6 MB(6.46%) for 56 tests
    public int minimumTimeRequired(int[] jobs, int k) {
        int[] res = new int[] {Integer.MAX_VALUE};
        dfs(res, jobs, new int[k], 0);
        return res[0];
    }

    private void dfs(int[] res, int[] jobs, int[] assigned, int cur) {
        if (cur == jobs.length) {
            res[0] = Math.min(res[0], Arrays.stream(assigned).max().getAsInt());
            return;
        }
        for (int i = 0; i < assigned.length; i++) {
            if (assigned[i] + jobs[cur] < res[0]) {
                assigned[i] += jobs[cur];
                dfs(res, jobs, assigned, cur + 1);
                assigned[i] -= jobs[cur];
            }
            if (assigned[i] == 0) { break; } // omitting this will TLE
        }
    }

    // DFS + Recursion + Backtracking + Binary Search
    // 4 ms(86.30%), 36.5 MB(85.01%) for 56 tests
    public int minimumTimeRequired2(int[] jobs, int k) {
        //        Integer[] sortedJobs = Arrays.stream(jobs).boxed().toArray(Integer[]::new);
        //        Arrays.sort(sortedJobs, Comparator.reverseOrder()); // optimization
        int low = Arrays.stream(jobs).max().getAsInt();
        for (int high = Arrays.stream(jobs).sum(); low < high; ) {
            int mid = (low + high) >>> 1;
            if (dfs(jobs, new int[k], 0, mid)) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    private boolean dfs(int[] jobs, int[] assigned, int cur, int max) {
        if (cur == jobs.length) { return true; }

        for (int i = 0; i < assigned.length; i++) {
            if (assigned[i] + jobs[cur] <= max) {
                assigned[i] += jobs[cur];
                if (dfs(jobs, assigned, cur + 1, max)) { return true; }

                assigned[i] -= jobs[cur];
            }
            if (assigned[i] == 0) { break; } // omitting this will TLE
        }
        return false;
    }

    // DFS + Recursion + Backtracking
    // 7 ms(75.19%), 39.1 MB(33.07%) for 56 tests
    public int minimumTimeRequired3(int[] jobs, int k) {
        return dfs(new int[] {Integer.MAX_VALUE}, jobs, 0, new int[k], 0);
    }

    private int dfs(int[] res, int[] jobs, int cur, int[] assigned, int curMax) {
        if (curMax >= res[0]) { return res[0]; }

        if (cur == jobs.length) { return res[0] = curMax; }

        Set<Integer> visited = new HashSet<>();
        for (int i = 0; i < assigned.length; i++) {
            if (!visited.add(assigned[i])) { continue; }

            assigned[i] += jobs[cur];
            dfs(res, jobs, cur + 1, assigned, Math.max(curMax, assigned[i]));
            assigned[i] -= jobs[cur];
        }
        return res[0];
    }

    // 2-D Dynamic Programming(Bottom-Up)
    // time complexity: O(N*2^N+K*3^N), space complexity: O(K*2^N)
    // 105 ms(43.15%), 38.6 MB(51.94%) for 56 tests
    public int minimumTimeRequired4(int[] jobs, int k) {
        int n = jobs.length;
        int m = 1 << n;
        int[] sum = new int[m];
        for (int mask = 0; mask < m; mask++) {
            for (int i = 0; i < n; i++) {
                if (((1 << i) & mask) != 0) {
                    sum[mask] += jobs[i];
                }
            }
        }
        int[][] dp = new int[k + 1][m];
        Arrays.fill(dp[0], Integer.MAX_VALUE);
        dp[0][0] = 0;
        for (int i = 1; i <= k; i++) {
            for (int mask = 1; mask < m; mask++) {
                dp[i][mask] = dp[i - 1][mask];
                for (int submask = mask; submask != 0; submask = (submask - 1) & mask) {
                    dp[i][mask] = Math.min(dp[i][mask],
                                           Math.max(sum[submask], dp[i - 1][mask - submask]));
                }
            }
        }
        return dp[k][m - 1];
    }

    // Solution of Choice
    // 1-D Dynamic Programming(Bottom-Up)
    // time complexity: O(K*3^N), space complexity: O(2^N)
    // 113 ms(42.64%), 37.1 MB(79.84%) for 56 tests
    public int minimumTimeRequired5(int[] jobs, int k) {
        int m = 1 << jobs.length;
        int[] dp = new int[m];
        int[] sum = new int[m];
        for (int mask = 1; mask < m; mask++) {
            dp[mask] = Integer.MAX_VALUE;
            sum[mask] += sum[mask & (mask - 1)] + jobs[Integer.numberOfTrailingZeros(mask)];
        }
        for (int i = k; i > 0; i--) {
            for (int mask = m - 1; mask >= 0; mask--) {
                for (int submask = mask; submask != 0; submask = (submask - 1) & mask) {
                    dp[mask] = Math.min(dp[mask], Math.max(dp[mask ^ submask], sum[submask]));
                }
            }
        }
        return dp[m - 1];
    }

    // 1-D Dynamic Programming(Bottom-Up) + Binary Search
    // time complexity: O(log(SUM)*3^N), space complexity: O(2^N)
    // 209 ms(35.14%), 37.1 MB(79.84%) for 56 tests
    public int minimumTimeRequired6(int[] jobs, int k) {
        int m = 1 << jobs.length;
        int[] sum = new int[m];
        for (int mask = 1; mask < m; mask++) {
            sum[mask] += sum[mask & (mask - 1)] + jobs[Integer.numberOfTrailingZeros(mask)];
        }
        int low = Arrays.stream(jobs).max().getAsInt();
        int[] dp = new int[m];
        for (int high = Arrays.stream(jobs).sum(); low < high; ) {
            int mid = (low + high) >>> 1;
            if (ok(mid, k, sum, dp)) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    private boolean ok(int max, int k, int[] sum, int[] dp) {
        Arrays.fill(dp, Integer.MAX_VALUE / 2);
        dp[0] = 0;
        for (int mask = 1; mask < dp.length; mask++) {
            for (int submask = mask; submask > 0; submask = (submask - 1) & mask) {
                if (sum[submask] <= max) {
                    dp[mask] = Math.min(dp[mask], dp[mask ^ submask] + 1);
                }
            }
        }
        return dp[dp.length - 1] <= k;
    }

    private void test(int[] jobs, int k, int expected) {
        assertEquals(expected, minimumTimeRequired(jobs, k));
        assertEquals(expected, minimumTimeRequired2(jobs, k));
        assertEquals(expected, minimumTimeRequired3(jobs, k));
        assertEquals(expected, minimumTimeRequired4(jobs, k));
        assertEquals(expected, minimumTimeRequired5(jobs, k));
        assertEquals(expected, minimumTimeRequired6(jobs, k));
    }

    @Test public void test() {
        test(new int[] {3, 2, 3}, 3, 3);
        test(new int[] {1, 2, 4, 7, 8}, 2, 11);
        test(new int[] {11, 2, 20, 18, 2, 1, 7, 11, 7, 10}, 9, 20);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
