import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC053: https://leetcode.com/problems/maximum-subarray/
// Cracking the Coding Interview(5ed) Problem 17.8
//
// Given an array of integers, find the contiguous sequence with the largest sum.
public class MaxSum {
    // beats 22.56%(2 ms)
    public static int maxSubArray(int[] nums) {
        int maxSingle = Integer.MIN_VALUE; // in case of all negatives
        int sum = 0;
        int maxSum = 0;
        for (int num : nums) {
            if ((maxSingle < 0) && (num > maxSingle)) {
                maxSingle = num;
            }
            sum += num;
            if (maxSum < sum) {
                maxSum = sum;
            } else if (sum < 0) { // start a new segment
                sum = 0;
            }
        }
        return maxSingle < 0 ? maxSingle : maxSum;
    }

    // beats 22.56%(2 ms)
    public static int maxSubArray2(int[] nums) {
        int maxSum = 0;
        int sum = 0;
        for (int num : nums) {
            sum += num;
            if (maxSum < sum) {
                maxSum = sum;
            } else if (sum < 0) {
                sum = 0;
            }
        }
        if (maxSum == 0) {
            maxSum = Integer.MIN_VALUE;
            for (int num : nums) {
                maxSum = Math.max(num, maxSum);
            }
        }
        return maxSum;
    }

    // beats 22.56%(2 ms)
    public static int maxSubArray3(int[] nums) {
        int n = nums.length;
        int maxSum = nums[n - 1];
        int sum = nums[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            sum = Math.max(sum, 0);
            maxSum = Math.max(maxSum, sum += nums[i]);
        }
        return maxSum;
    }

    // Dynamic Programming
    // beats 15.70%(3 ms)
    public static int maxSubArray4(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n + 1]; // maximum subarray ending with A[i]
        int max = Integer.MIN_VALUE;
        for (int i = 1; i <= n; i++) {
            dp[i] = nums[i - 1] + Math.max(dp[i - 1], 0);
            max = Math.max(max, dp[i]);
        }
        return max;
    }

    // Leetcode More practice:
    // If you have figured out the O(n) solution, try coding another solution
    // using the divide and conquer approach, which is more subtle.
    // time complexity: O(N * log(N))
    // beats 15.70%(3 ms)
    public static int maxSubArray5(int[] nums) {
        return maxSubArray5(nums, 0, nums.length - 1);
    }

    private static int maxSubArray5(int[] nums, int start, int end) {
        if (start > end) return Integer.MIN_VALUE;

        int mid = (start + end) >>> 1;
        int maxSum1 = 0;
        for (int i = mid - 1, sum = 0; i >= start; i--) {
            maxSum1 = Math.max(maxSum1, sum += nums[i]);
        }
        int maxSum2 = 0;
        for (int i = mid + 1, sum = 0; i <= end; i++) {
            maxSum2 = Math.max(maxSum2, sum += nums[i]);
        }
        return Math.max(maxSum1 + maxSum2 + nums[mid],
                        Math.max(maxSubArray5(nums, start, mid - 1),
                                 maxSubArray5(nums, mid + 1, end)));
    }

    // Solution of Choice
    // 0-D Dynamic Programming(Bottom-Up) (Kadane's algorithm)
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 39.5 MB(12.40%) for 203 tests
    public static int maxSubArray6(int[] nums) {
        int res = Integer.MIN_VALUE;
        int runningSum = 0;
        for (int a : nums) {
            runningSum = a + Math.max(runningSum, 0);
            res = Math.max(res, runningSum);
        }
        return res;
    }

    private void test(Function<int[], Integer> maxSum, String name,
                      int expected, int ... a) {
        long t1 = System.nanoTime();
        int n = maxSum.apply(a);
        if (a.length > 100) {
            System.out.format("%s: %.3f ms\n", name,
                              (System.nanoTime() - t1) * 1e-6);
        }
        assertEquals(expected, n);
    }

    private void test(int expected, int ... a) {
        test(MaxSum::maxSubArray, "maxSubArray", expected, a);
        test(MaxSum::maxSubArray2, "maxSubArray2", expected, a);
        test(MaxSum::maxSubArray3, "maxSubArray3", expected, a);
        test(MaxSum::maxSubArray4, "maxSubArray4", expected, a);
        test(MaxSum::maxSubArray5, "maxSubArray5", expected, a);
        test(MaxSum::maxSubArray6, "maxSubArray6", expected, a);
    }

    @Test
    public void test1() {
        test(0, -2, -4, 0);
        test(12, -2, -4, 5, 7);
        test(6, -2, 1, -3, 4, -1, 2, 1, -5, 4);
        test(1, -2, 1);
        test(-1, -2, -1);
        test(-1, -1, -2);
        test(14, 6, -2, -2, 3, -2, -2, 4, 5, -4, -4, 5, 7, -9);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
