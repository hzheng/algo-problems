import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC494: https://leetcode.com/problems/target-sum/
//
// You are given a list of non-negative integers, a1, a2, ..., an, and a target,
// S. Now you have 2 symbols + and -. For each integer, you should choose one
// from + and - as its new symbol.
// Find out how many ways to assign symbols to make sum of integers equal to target S.
public class TargetSum {
    // Time Limit Exceeded
    // Bit Manipulation
    public int findTargetSumWays0(int[] nums, int S) {
        int n = nums.length;
        if (n == 0) return 0;
        int count = 0;
        for (int flag = (1 << (n - 1)) - 1; flag >= 0; flag--) {
            if (compute(nums, flag, S)) {
                count++;
            }
        }
        return S == 0 ? count * 2 : count;
    }

    private boolean compute(int[] nums, int flag, int S) {
        int sum = 0;
        for (int i = 0, mask = 1; i < nums.length; mask <<= 1, i++) {
            sum += nums[i] * ((flag & mask) == 0 ? 1 : -1);
        }
        return sum == S || sum == -S;
    }

    // Bit Manipulation + Hash Table
    // beats 74.91%(55 ms for 139 tests)
    public int findTargetSumWays(int[] nums, int S) {
        int n = nums.length;
        int m = n / 2;
        Map<Integer, Integer> map = new HashMap<>();
        for (int flag = (1 << m) - 1; flag >= 0; flag--) {
            int sum = compute(nums, 0, m, flag);
            map.put(sum, map.getOrDefault(sum, 0) + 1);
        }
        int count = 0;
        for (int flag = (1 << (n - m)) - 1; flag >= 0; flag--) {
            int sum = compute(nums, m, n, flag);
            count += map.getOrDefault(S - sum, 0);
        }
        return count;
    }

    private int compute(int[] nums, int start, int end, int flag) {
        int sum = 0;
        for (int i = start, mask = 1; i < end; mask <<= 1, i++) {
            sum += nums[i] * ((flag & mask) == 0 ? 1 : -1);
        }
        return sum;
    }

    // beats 40.09%(642 ms for 139 tests)
    // DFS + Recursion
    public int findTargetSumWays2(int[] nums, int S) {
        int[] count = {0};
        findTarget(nums, 0, S, count);
        return count[0];
    }

    private void findTarget(int[] nums, int items, int remainder, int[] count) {
        if (items == nums.length) {
            if (remainder == 0) {
                count[0]++;
            }
            return;
        }
        findTarget(nums, items + 1, remainder - nums[items], count);
        findTarget(nums, items + 1, remainder + nums[items], count);
    }

    // Recursion + Dynamic Programming(Top-Down)
    // beats 51.90%(279 ms for 139 tests)
    public int findTargetSumWays3(int[] nums, int S) {
        return findTarget(nums, 0, 0, S, new HashMap<>());
    }

    private int findTarget(int[] nums, int index, int sum, int target, Map<Long, Integer> map) {
        if (index == nums.length) return sum == target ? 1 : 0;

        long key = ((long)index << 32) + sum;
        Integer cached = map.get(key);
        if (cached != null) return cached;

        int res = findTarget(nums, index + 1, sum - nums[index], target, map);
        res += findTarget(nums, index + 1, sum + nums[index], target, map);
        map.put(key, res);
        return res;
    }

    // Dynamic Programming
    // https://discuss.leetcode.com/topic/76264/short-java-dp-solution-with-explanation
    // beats 88.16%(24 ms for 139 tests)
    public int findTargetSumWays4(int[] nums, int S) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        if (S > sum || S < -sum) return 0;

        int size = 2 * sum + 1;
        int[] dp = new int[size];
        dp[sum] = 1;
        for (int num : nums) {
            int[] next = new int[size];
            for (int i = 0; i < size; i++) {
                if (dp[i] != 0) {
                    next[i + num] += dp[i];
                    next[i - num] += dp[i];
                }
            }
            dp = next;
        }
        return dp[sum + S];
    }

    // Dynamic Programming
    // beats 97.37%(16 ms for 139 tests)
    public int findTargetSumWays5(int[] nums, int S) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        if (S > sum || S < -sum || ((S + sum) & 1) != 0) return 0;

        // Find a positive subset of nums such that sum(P) = (S + sum(nums)) / 2
        int total = (S + sum) >>> 1;
        int[] dp = new int[total + 1];
        dp[0] = 1;
        for (int num : nums) {
            for (int i = total; i >= num; i--) {
                dp[i] += dp[i - num];
            }
        }
        return dp[total];
    }

    void test(int[] nums, int S, int expected) {
        assertEquals(expected, findTargetSumWays(nums, S));
        assertEquals(expected, findTargetSumWays2(nums, S));
        assertEquals(expected, findTargetSumWays3(nums, S));
        assertEquals(expected, findTargetSumWays4(nums, S));
        assertEquals(expected, findTargetSumWays5(nums, S));
        assertEquals(expected, findTargetSumWays0(nums, S));
    }

    @Test
    public void test() {
        test(new int[] {1}, 1, 1);
        test(new int[] {0}, 0, 2);
        test(new int[] {1, 1, 1, 1, 1}, 3, 5);
        test(new int[] {16, 40, 9, 17, 49, 32, 30, 10, 38, 36, 31, 22, 3, 36, 32,
                        2, 26, 17, 30, 47}, 49, 5828);
        test(new int[] {25, 14, 16, 44, 9, 22, 15, 27, 23, 10, 41, 25, 14, 35,
                        28, 47, 39, 26, 11, 38}, 43, 6182);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("TargetSum");
    }
}
