import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC045: https://leetcode.com/problems/jump-game-ii/
//
// Given an array of non-negative integers, you are initially positioned at the
// first index of the array. Each element in the array represents your maximum
// jump length at that position.
// Your goal is to reach the last index in the minimum number of jumps.
// Note: You can assume that you can always reach the last index.
public class JumpGame2 {
    // Greedy
    // beats 29.35%(10 ms)
    public int jump(int[] nums) {
        int n = nums.length;
        if (n < 2) return 0;

        int jumps = 1;
        int max = nums[0];
        for (int i = 0; i < n && max < n - 1; jumps++) {
            int nextIndex = n; // exit signal
            for (int j = nums[i]; j >= 1; j--) {
                if ((i + j) < n && nums[i + j] + i + j > max) {
                    nextIndex = i + j;
                    max = nums[nextIndex] + nextIndex;
                }
            }
            i = nextIndex;
        }
        return jumps;
    }

    // Greedy
    // beats 29.35%(10 ms)
    public int jump2(int[] nums) {
        int jumps = 0;
        for (int i = 0, max = 0, n = nums.length; max < n - 1; jumps++) {
            for (int oldMax = max; i <= oldMax; i++) {
                max = Math.max(max, i + nums[i]);
            }
            // for (int j = max; j >= i; j--) { // TLE
            //     max = Math.max(max, j + nums[j]);
            // }
        }
        return jumps;
    }

    // Dynamic Programming
    // http://www.jiuzhang.com/solutions/jump-game-ii/
    // Time Limit Exceeded
    public int jump3(int[] nums) {
        int n = nums.length;
        int[] jumps = new int[n];
        jumps[0] = 0;
        for (int i = 1; i < n; i++) {
            jumps[i] = -1;
            for (int j = 0; j < i; j++) {
                if (jumps[j] >= 0 && j + nums[j] >= i) {
                    jumps[i] = jumps[j] + 1;
                    break;
                }
            }
        }
        return jumps[n - 1];
    }

    // Dynamic Programming
    // Time Limit Exceeded
    public int jump4(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        for (int i = 0; i < n; i++) {
            int num = nums[i];
            for (int j = Math.min(i + num, n - 1); j > i; j--) {
                dp[j] = Math.min(dp[j], dp[i] + 1);
            }
        }
        return dp[n - 1];
    }

    // Solution of Choice
    // Greedy
    // time complexity: O(N), space complexity: O(1)
    // beats 29.35%(10 ms)
    public int jump5(int[] nums) {
        int jumps = 0;
        for (int i = 0, max = 0, lastMax = 0; i < nums.length - 1; i++) {
            max = Math.max(max, i + nums[i]);
            if (i == lastMax) {
                jumps++;
                lastMax = max;
                // if (max >= nums.length - 1) return jumps;
            }
        }
        return jumps;
    }

    // BFS
    // https://discuss.leetcode.com/topic/18815/10-lines-c-16ms-python-bfs-solutions-with-explanations
    // beats 17.25%(11 ms)
    public int jump6(int[] nums) {
        int n = nums.length;
        if (n < 2) return 0;

        int jumps = 1;
        for (int start = 0, end = 0; end < n - 1; jumps++) {
            int max = end + 1;
            for (int i = start; i <= end; i++) {
                max = Math.max(max, i + nums[i]);
                if (max >= n - 1) return jumps;
            }
            start = end + 1;
            end = max;
        }
        return jumps;
    }

    void test(int expected, int ... nums) {
        assertEquals(expected, jump(nums));
        assertEquals(expected, jump2(nums));
        assertEquals(expected, jump3(nums));
        assertEquals(expected, jump4(nums));
        assertEquals(expected, jump5(nums));
        assertEquals(expected, jump6(nums));
    }

    @Test
    public void test1() {
        test(3, 1, 1, 1, 1);
        test(0, 0);
        test(1, 1, 2);
        test(2, 1, 1, 1);
        test(2, 2, 3, 1, 1, 4);
        // test(-1, 0, 1);
        // test(-1, 2, 3, 1, 1, 0, 0, 4);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
