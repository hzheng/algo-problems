import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1681: https://leetcode.com/problems/minimum-incompatibility/
//
// You are given an integer array nums and an integer k. You are asked to distribute this array into
// k subsets of equal size such that there are no two equal elements in the same subset. A subset's
// incompatibility is the difference between the maximum and minimum elements in that array.
// Return the minimum possible sum of incompatibilities of the k subsets after distributing the
// array optimally, or return -1 if it is not possible.
// A subset is a group integers that appear in the array with no particular order.
//
// Constraints:
// 1 <= k <= nums.length <= 16
// nums.length is divisible by k
// 1 <= nums[i] <= nums.length
public class MinimumIncompatibility {
    // Sort + DFS + Recursion + Backtracking
    // time complexity: O(N), space complexity: O(N)
    // 375 ms(49.72%), 36.7 MB(94.43%) for 105 tests
    public int minimumIncompatibility(int[] nums, int k) {
        Arrays.sort(nums);
        int[] res = new int[] {Integer.MAX_VALUE};
        dfs(nums, 0, new int[k], new int[k][nums.length / k], res);
        return res[0] == Integer.MAX_VALUE ? -1 : res[0];
    }

    private void dfs(int[] nums, int cur, int[] indices, int[][] group, int[] res) {
        if (cur == nums.length) {
            int incompatibility = 0;
            for (int[] a : group) {
                int min = a[0];
                int max = a[0];
                for (int x : a) {
                    min = Math.min(min, x);
                    max = Math.max(max, x);
                }
                incompatibility += max - min;
            }
            res[0] = Math.min(res[0], incompatibility);
            return;
        }

        int num = nums[cur];
        for (int i = 0; i < indices.length; i++) {
            int index = indices[i];
            if (index >= group[i].length || index > 0 && group[i][index - 1] == num) { continue; }

            group[i][index] = num;
            indices[i]++;
            dfs(nums, cur + 1, indices, group, res);
            indices[i]--;
            if (index == 0) { break; }
        }
    }

    // Sort + DFS + Recursion + Backtracking
    // time complexity: O(N), space complexity: O(N)
    // 7 ms(97.77%), 36.2 MB(97.77%) for 105 tests
    public int minimumIncompatibility2(int[] nums, int k) {
        Arrays.sort(nums);
        int[] res = new int[] {Integer.MAX_VALUE};
        dfs(nums, 0, 0, new int[k], new int[k][nums.length / k], res);
        return res[0] == Integer.MAX_VALUE ? -1 : res[0];
    }

    private void dfs(int[] nums, int cur, int incompatibility, int[] indices, int[][] group,
                     int[] res) {
        if (incompatibility >= res[0]) { return; }

        if (cur == nums.length) {
            res[0] = incompatibility;
            return;
        }

        int num = nums[cur];
        for (int i = 0; i < indices.length; i++) {
            int index = indices[i];
            if (index >= group[i].length || index > 0 && group[i][index - 1] == num) { continue; }

            group[i][index] = num;
            indices[i]++;
            int increase = (index > 0) ? num - group[i][index - 1] : 0;
            dfs(nums, cur + 1, incompatibility + increase, indices, group, res);
            indices[i]--;
            if (index == 0) { break; }
        }
    }

    // 2-D Dynamic Programming(Bottom-Up) + Bit Manipulation
    // time complexity: O(N^2*2^N), space complexity: O(N*2^N)
    // 190 ms(59.56%), 52.1 MB(21.52%) for 105 tests
    public int minimumIncompatibility3(int[] nums, int k) {
        Arrays.sort(nums);
        int n = nums.length;
        if (k == n) { return 0; }

        int[][] dp = new int[1 << n][n]; // i: chosen numbers j: last taken number
        final int MAX = Integer.MAX_VALUE / 2;
        for (int[] a : dp) {
            Arrays.fill(a, MAX);
        }
        for (int i = 0; i < n; i++) {
            dp[1 << i][i] = 0;
        }
        for (int mask = 0, size = n / k; mask < dp.length; mask++) {
            boolean newGroup = (Integer.bitCount(mask) % size == 1);
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) == 0) { continue; }

                for (int j = i + 1; j < n; j++) {
                    if ((mask & (1 << j)) == 0) { continue; }

                    if (newGroup) {
                        dp[mask][i] = Math.min(dp[mask][i], dp[mask ^ (1 << i)][j]);
                        // omit the following line if we select j between 0 and n (and needn't sort)
                        dp[mask][j] = Math.min(dp[mask][j], dp[mask ^ (1 << j)][i]);
                    } else if (nums[i] < nums[j]) {
                        dp[mask][j] =
                                Math.min(dp[mask][j], dp[mask ^ (1 << j)][i] + nums[j] - nums[i]);
                    }
                }
            }
        }
        int res = MAX;
        for (int a : dp[dp.length - 1]) {
            res = Math.min(res, a);
        }
        return res == MAX ? -1 : res;
    }

    // 1-D Dynamic Programming(Bottom-Up) + Bit Manipulation
    // time complexity: O(N*2^N), space complexity: O(2^N)
    // 17 ms(92.95%), 39.5 MB(56.96%) for 105 tests
    public int minimumIncompatibility4(int[] nums, int k) {
        Arrays.sort(nums);
        int n = nums.length;
        final int ALL = (1 << n) - 1;
        int[] subsets = new int[ALL + 1];
        final int MAX = Integer.MAX_VALUE / 3;
        Arrays.fill(subsets, MAX);
        outer:
        for (int mask = ALL, m = n / k; mask >= 0; mask--) {
            if (Integer.bitCount(mask) != m) { continue; }

            int min = MAX;
            int max = 0;
            for (int i = 0, prev = -1; i < n; i++) {
                if ((mask << ~i) < 0) {
                    if (prev == nums[i]) { continue outer; }

                    prev = nums[i];
                    min = Math.min(min, nums[i]);
                    max = Math.max(max, nums[i]);
                }
            }
            subsets[mask] = max - min;
        }
        int[] dp = new int[ALL + 1];
        Arrays.fill(dp, MAX);
        dp[0] = 0;
        for (int chosen = 0; chosen < ALL; chosen++) {
            if (dp[chosen] >= MAX) { continue; }

            int free = ~chosen & ALL;
            int last = ~chosen & -~chosen;
            free ^= last;
            for (int i = free; i >= 0; i--) {
                i &= free;
                int next = i | last;
                dp[chosen | next] = Math.min(dp[chosen | next], dp[chosen] + subsets[next]);
            }
        }
        return (dp[ALL] >= MAX) ? -1 : dp[ALL];
    }

    private void test(int[] nums, int k, int expected) {
        assertEquals(expected, minimumIncompatibility(nums.clone(), k));
        assertEquals(expected, minimumIncompatibility2(nums.clone(), k));
        assertEquals(expected, minimumIncompatibility3(nums.clone(), k));
        assertEquals(expected, minimumIncompatibility4(nums.clone(), k));
    }

    @Test public void test() {
        test(new int[] {1, 2, 1, 4}, 2, 4);
        test(new int[] {6, 3, 8, 1, 3, 1, 2, 2}, 4, 6);
        test(new int[] {5, 3, 3, 6, 3, 3}, 3, -1);
        test(new int[] {1, 1, 8, 3, 2, 1, 8, 8}, 4, 20);
        test(new int[] {7, 3, 3, 9, 4, 4, 9, 9, 3, 8, 5}, 11, 0);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
