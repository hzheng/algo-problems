import org.junit.Test;

import static org.junit.Assert.*;

// LC1793: https://leetcode.com/problems/maximum-score-of-a-good-subarray/
//
// You are given an array of integers nums (0-indexed) and an integer k.
// The score of a subarray (i, j) is defined as min(nums[i], nums[i+1], ..., nums[j]) * (j - i + 1).
// A good subarray is a subarray where i <= k <= j.
// Return the maximum possible score of a good subarray.
//
// Constraints:
// 1 <= nums.length <= 10^5
// 1 <= nums[i] <= 2 * 10^4
// 0 <= k < nums.length
public class MaxScoreOfGoodSubarray {
    // Monotonic Stack
    // time complexity: O(N), space complexity: O(N)
    // 9 ms(100.00%), 52.9 MB(100.00%) for 72 tests
    public int maximumScore(int[] nums, int k) {
        int n = nums.length;
        int[] stack = new int[n + 1];
        stack[0] = -1;
        int res = 0;
        for (int i = 0, top = 0; i <= n; ) {
            int height = (i == n) ? 0 : nums[i];
            if (top == 0 || height > nums[stack[top]]) {
                stack[++top] = i++;
            } else {
                int last = stack[top--];
                int dist = i - stack[top] - 1;
                if (i > k && i - dist <= k) {
                    res = Math.max(res, nums[last] * dist);
                }
            }
        }
        return res;
    }

    // Two Pointers
    // time complexity: O(N), space complexity: O(1)
    // 6 ms(100.00%), 48.3 MB(100.00%) for 72 tests
    public int maximumScore2(int[] nums, int k) {
        int res = nums[k];
        for (int i = k, j = k, min = nums[k], n = nums.length; i > 0 || j < n - 1; ) {
            if (i == 0) {
                j++;
            } else if (j == n - 1) {
                i--;
            } else if (nums[i - 1] < nums[j + 1]) {
                j++;
            } else {
                i--;
            }
            min = Math.min(min, Math.min(nums[i], nums[j]));
            res = Math.max(res, min * (j - i + 1));
        }
        return res;
    }

    // Two Pointers + Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // 5 ms(100.00%), 53.5 MB(100.00%) for 72 tests
    public int maximumScore3(int[] nums, int k) {
        int n = nums.length;
        int res = nums[k];
        int[] min = new int[n];
        min[k] = nums[k];
        for (int i = k + 1; i < n; i++) {
            min[i] = Math.min(min[i - 1], nums[i]);
        }
        for (int i = k - 1; i >= 0; --i) {
            min[i] = Math.min(min[i + 1], nums[i]);
        }
        for (int i = 0, j = n - 1; i < j; ) {
            res = Math.max(res, (j - i + 1) * Math.min(min[i], min[j]));
            if (j == k || min[i] < min[j]) {
                i++;
            } else {
                j--;
            }
        }
        return res;
    }

    // Dynamic Programming (+ Binary Search)
    // time complexity: O(N+nums[k]), space complexity: O(N)
    // 5 ms(100.00%), 53.9 MB(100.00%) for 72 tests
    public int maximumScore4(int[] nums, int k) {
        int n = nums.length;
        int[] min = new int[n];
        min[k] = nums[k];
        for (int i = k + 1; i < n; i++) {
            min[i] = Math.min(min[i - 1], nums[i]);
        }
        for (int i = k - 1; i >= 0; --i) {
            min[i] = Math.min(min[i + 1], nums[i]);
        }
        int res = 0;
        for (int v = min[k], i = k, j = k; v >= 1; v--) {
            for (; i > 0 && min[i - 1] >= v; i--) {} // could be improved by Binary Search
            for (; j < n - 1 && min[j + 1] >= v; j++) {} // could be improved by Binary Search
            res = Math.max(res, (j - i + 1) * v);
        }
        return res;
    }

    private void test(int[] nums, int k, int expected) {
        assertEquals(expected, maximumScore(nums, k));
        assertEquals(expected, maximumScore2(nums, k));
        assertEquals(expected, maximumScore3(nums, k));
        assertEquals(expected, maximumScore4(nums, k));
    }

    @Test public void test() {
        test(new int[] {1, 4, 3, 7, 4, 5}, 3, 15);
        test(new int[] {5, 5, 4, 5, 4, 1, 1, 1}, 0, 20);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
