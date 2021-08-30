import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC698: https://leetcode.com/problems/partition-to-k-equal-sum-subsets/description/
//
// Given an array of integers nums and a positive integer k, find whether it's
// possible to divide this array into k subsets whose sums are all equal.
// Note:
// 1 <= k <= len(nums) <= 16.
// 0 < nums[i] < 10000.
public class PartitionKSubsets {
    // Recursion + DFS + Bit Manipulation
    // beats 16.34%(130 ms for 147 tests)
    public boolean canPartitionKSubsets(int[] nums, int k) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        if ((sum % k) != 0) { return false; }

        int target = sum / k;
        List<Integer> subsets = new ArrayList<>();
        int n = nums.length;
        for (int i = (1 << n) - 1; i > 0; i--) {
            int t = 0;
            for (int j = 0; j < n; j++) {
                if (((i >> j) & 1) > 0) {
                    t += nums[j];
                }
            }
            if (t == target) {
                subsets.add(i);
            }
        }
        return (subsets.size() >= k) && partition(subsets, 0, k, (1 << n) - 1);
    }

    private boolean partition(List<Integer> set, int start, int left, int mask) {
        if (start >= set.size()) { return left == 0; }

        int cur = set.get(start);
        if ((cur | mask) == mask) {
            if (partition(set, start + 1, left - 1, mask & ~cur)) { return true; }
        }
        return partition(set, start + 1, left, mask);
    }

    // https://leetcode.com/articles/partition-to-k-equal-sum-subsets/ #1
    // Recursion + DFS + Backtracking
    // time complexity: O(k! * k ^ (N − k)), space complexity: O(N)
    // beats 23.44%(85 ms for 147 tests)
    public boolean canPartitionKSubsets2(int[] nums, int k) {
        int sum = Arrays.stream(nums).sum();
        if ((sum % k) != 0) { return false; }

        int target = sum / k;
        Arrays.sort(nums);
        int last = nums.length - 1;
        if (nums[last] > target) { return false; }

        for (; last >= 0 && nums[last] == target; last--, k--) {}
        return partition(nums, new int[k], last, target);
    }

    private boolean partition(int[] nums, int[] groups, int last, int target) {
        if (last < 0) { return true; }

        int num = nums[last];
        for (int i = 0; i < groups.length; i++) {
            if (groups[i] + num <= target) {
                groups[i] += num;
                if (partition(nums, groups, last - 1, target)) { return true; }

                groups[i] -= num;
            }
            if (groups[i] == 0) {
                break; // push 0 values of each group to the end
            }
        }
        return false;
    }

    // https://leetcode.com/articles/partition-to-k-equal-sum-subsets/ #2_1
    // Recursion + Dynamic Programming(Top-Down) + Bit Manipulation
    // time complexity: O(N * 2 ^ N)), space complexity: O(2 ^ N)
    // beats 23.44%(85 ms for 147 tests)
    public boolean canPartitionKSubsets3(int[] nums, int k) {
        int sum = Arrays.stream(nums).sum();
        if ((sum % k) != 0) { return false; }

        Boolean[] dp = new Boolean[1 << nums.length];
        dp[(1 << nums.length) - 1] = true;
        return partition(nums, 0, sum, sum / k, dp);
    }

    private boolean partition(int[] nums, int mask, int left, int target, Boolean[] dp) {
        if (dp[mask] == null) {
            dp[mask] = false;
            int t = (left - 1) % target + 1;
            for (int i = 0; i < nums.length; i++) {
                if ((((mask >> i) & 1) == 0) && nums[i] <= t) {
                    if (partition(nums, mask | (1 << i), left - nums[i], target, dp)) {
                        dp[mask] = true;
                        break;
                    }
                }
            }
        }
        return dp[mask];
    }

    // https://leetcode.com/articles/partition-to-k-equal-sum-subsets/ #2_2
    // Dynamic Programming(Bottom-Up) + Bit Manipulation
    // time complexity: O(N * 2 ^ N)), space complexity: O(2 ^ N)
    // beats 13.33%(153 ms for 147 tests)
    public boolean canPartitionKSubsets4(int[] nums, int k) {
        Arrays.sort(nums);
        int sum = Arrays.stream(nums).sum();
        int target = sum / k;
        int n = nums.length;
        if ((sum % k) != 0 || nums[n - 1] > target) { return false; }

        boolean[] dp = new boolean[1 << n];
        dp[0] = true;
        int[] total = new int[1 << n];
        for (int cur = 0; cur < (1 << n); cur++) {
            if (!dp[cur]) { continue; }

            for (int i = 0; i < n; i++) {
                int next = cur | (1 << i);
                if (cur == next || dp[next]) { continue; }

                if (nums[i] > target - (total[cur] % target)) { break; }

                dp[next] = true;
                total[next] = total[cur] + nums[i];
            }
        }
        return dp[(1 << n) - 1];
    }

    // Recursion + DFS + Backtracking
    // beats 21.72%(95 ms for 147 tests)
    public boolean canPartitionKSubsets5(int[] nums, int k) {
        int sum = Arrays.stream(nums).sum();
        if ((sum % k) != 0) { return false; }

        return partition(nums, k, sum / k, 0, 0, new boolean[nums.length]);
    }

    public boolean partition(int[] nums, int groups, int target, int start, int sum,
                             boolean[] visited) {
        if (groups == 1) { return true; }

        if (sum > target) { return false; }

        if (sum == target) {
            return partition(nums, groups - 1, target, 0, 0, visited);
        }

        for (int i = start; i < nums.length; i++) {
            if (visited[i]) { continue; }

            visited[i] = true;
            if (partition(nums, groups, target, i + 1, sum + nums[i], visited)) {
                return true;
            }
            visited[i] = false;
        }
        return false;
    }

    // Recursion + DFS + Backtracking
    // 2 ms(53.53%), 36.7 MB(39.32%) for 142 tests
    public boolean canPartitionKSubsets6(int[] nums, int k) {
        int sum = Arrays.stream(nums).sum();
        if ((sum % k) != 0) { return false; }

        Arrays.sort(nums); // save time by trying bigger numbers first
        int n = nums.length;
        return partition6(nums, k, sum / k, n - 1, 0, new boolean[n]);
    }

    private boolean partition6(int[] nums, int groups, int target, int start, int sum,
                               boolean[] visited) {
        if (groups == 1) { return true; }

        if (sum == target && partition6(nums, groups - 1, target, nums.length - 1, 0, visited)) {
            return true;
        }

        for (int i = start; i >= 0; i--) {
            if (visited[i] || sum + nums[i] > target) { continue; }

            visited[i] = true;
            if (partition6(nums, groups, target, i - 1, sum + nums[i], visited)) {
                return true;
            }
            visited[i] = false;
        }
        return false;
    }

    void test(int[] nums, int k, boolean expected) {
        assertEquals(expected, canPartitionKSubsets(nums, k));
        assertEquals(expected, canPartitionKSubsets2(nums, k));
        assertEquals(expected, canPartitionKSubsets3(nums, k));
        assertEquals(expected, canPartitionKSubsets4(nums, k));
        assertEquals(expected, canPartitionKSubsets5(nums, k));
        assertEquals(expected, canPartitionKSubsets6(nums, k));
    }

    @Test public void test() {
        test(new int[] {4, 3, 2, 3, 5, 2, 1}, 4, true);
        test(new int[] {2, 2, 2, 2, 3, 4, 5}, 4, false);
        test(new int[] {5, 2, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 3}, 15, true);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
