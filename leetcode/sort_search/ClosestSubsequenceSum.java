import java.util.*;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

// LC1755: https://leetcode.com/problems/closest-subsequence-sum/
//
// You are given an integer array nums and an integer goal. You want to choose a subsequence of nums
// such that the sum of its elements is the closest possible to goal. That is, if the sum of the
// subsequence's elements is sum, then you want to minimize the absolute difference abs(sum - goal).
// Return the minimum possible value of abs(sum - goal).
// Note that a subsequence of an array is an array formed by removing some elements (possibly all or
// none) of the original array.
//
// Constraints:
// 1 <= nums.length <= 40
// -10^7 <= nums[i] <= 10^7
// -10^9 <= goal <= 10^9
public class ClosestSubsequenceSum {
    // Bit Manipulation + Sort + Binary Search + Meet in the Middle
    // time complexity: O(N*2^(N/2)), space complexity: O(2^(N/2))
    // 273 ms(87.08%), 43.1 MB(99.52%) for 69 tests
    public int minAbsDifference(int[] nums, int goal) {
        int n = nums.length;
        int m = n / 2;
        int res = Integer.MAX_VALUE;
        int[] sums = new int[1 << m];
        for (int mask = sums.length - 1; mask > 0; mask--) {
            int sum = 0;
            for (int i = 0; i < m; i++) {
                if ((mask >> i & 1) != 0) {
                    sum += nums[i];
                }
            }
            sums[mask] = sum;
        }
        Arrays.sort(sums);
        for (int p = n - m, mask = (1 << p) - 1; mask >= 0; mask--) {
            int sum = 0;
            for (int i = 0; i < p; i++) {
                if ((mask >> i & 1) != 0) {
                    sum += nums[i + m];
                }
            }
            int index = Arrays.binarySearch(sums, goal - sum);
            if (index >= 0) { return 0; }

            index = -index - 1;
            if (index < sums.length) {
                res = Math.min(res, Math.abs(sums[index] + sum - goal));
            }
            if (index > 0) {
                res = Math.min(res, Math.abs(sums[index - 1] + sum - goal));
            }
        }
        return res;
    }

    // Bit Manipulation + Sort + Binary Search + Meet in the Middle
    // time complexity: O(N*2^(N/2)), space complexity: O(2^(N/2))
    // 113 ms(95.69%), 47.2 MB(89.47%) for 69 tests
    public int minAbsDifference2(int[] nums, int goal) {
        int n = nums.length;
        int m = n / 2;
        int[] leftSum = new int[1 << m];
        for (int i = 1, total = leftSum.length; i < total; i++) {
            leftSum[i] += leftSum[i & (i - 1)] + nums[Integer.numberOfTrailingZeros(i)];
        }
        Arrays.sort(leftSum);
        int[] rightSum = new int[1 << (n - m)];
        for (int i = 1, total = rightSum.length; i < total; i++) {
            rightSum[i] += rightSum[i & i - 1] + nums[m + Integer.numberOfTrailingZeros(i)];
        }
        int res = Integer.MAX_VALUE;
        for (int sum : rightSum) {
            int index = Arrays.binarySearch(leftSum, goal - sum);
            if (index >= 0) { return 0; }

            index = -index - 1;
            if (index < leftSum.length) {
                res = Math.min(res, Math.abs(leftSum[index] + sum - goal));
            }
            if (index > 0) {
                res = Math.min(res, Math.abs(leftSum[index - 1] + sum - goal));
            }
        }
        return res;
    }

    // Bit Manipulation + SortedSet + Meet in the Middle
    // time complexity: O(N*2^(N/2)), space complexity: O(2^(N/2))
    // 767 ms(58.55%), 115.6 MB(52.19%) for 69 tests
    public int minAbsDifference3(int[] nums, int goal) {
        NavigableSet<Integer> sums = new TreeSet<>();
        int n = nums.length;
        int m = n / 2;
        sums.add(0);
        for (int mask = (1 << m) - 1; mask > 0; mask--) {
            int sum = 0;
            for (int i = 0; i < m; i++) {
                sum += nums[i] * ((mask >> i) & 1);
            }
            sums.add(sum);
        }
        int res = Math.abs(goal);
        for (int mask = (1 << (n - m)) - 1; mask >= 0; mask--) {
            int sum = 0;
            for (int i = 0; i < n - m; i++) {
                sum += nums[i + m] * ((mask >> i) & 1);
            }
            Integer floor = sums.floor(goal - sum);
            if (floor != null) {
                res = Math.min(res, Math.abs(sum + floor - goal));
            }
            Integer ceiling = sums.ceiling(goal - sum);
            if (ceiling != null) {
                res = Math.min(res, Math.abs(sum + ceiling - goal));
            }
        }
        return res;
    }

    // Recursion + Sort + Meet in the Middle
    // time complexity: O(N*2^(N/2)), space complexity: O(2^(N/2))
    // 767 ms(58.55%), 115.6 MB(52.19%) for 69 tests
    public int minAbsDifference4(int[] nums, int goal) {
        int n = nums.length;
        List<Integer> leftSum = new ArrayList<>();
        List<Integer> rightSum = new ArrayList<>();
        generateSums(nums, 0, n / 2, 0, leftSum);
        generateSums(nums, n / 2, n, 0, rightSum);
        Collections.sort(leftSum);
        int res = Integer.MAX_VALUE;
        for (int sum : rightSum) {
            int index = Collections.binarySearch(leftSum, goal - sum);
            if (index >= 0) { return 0; }

            index = -index - 1;
            if (index < leftSum.size()) {
                res = Math.min(res, Math.abs(leftSum.get(index) + sum - goal));
            }
            if (index > 0) {
                res = Math.min(res, Math.abs(leftSum.get(index - 1) + sum - goal));
            }
        }
        return res;
    }

    private void generateSums(int[] nums, int cur, int end, int sum, List<Integer> sums) {
        if (cur == end) {
            sums.add(sum);
            return;
        }
        generateSums(nums, cur + 1, end, sum + nums[cur], sums);
        generateSums(nums, cur + 1, end, sum, sums);
    }

    private void test(int[] nums, int goal, int expected) {
        assertEquals(expected, minAbsDifference(nums, goal));
        assertEquals(expected, minAbsDifference2(nums, goal));
        assertEquals(expected, minAbsDifference3(nums, goal));
        assertEquals(expected, minAbsDifference4(nums, goal));
    }

    @Test public void test() {
        test(new int[] {5, -7, 3, 5}, 6, 0);
        test(new int[] {7, -9, 15, -2}, -5, 1);
        test(new int[] {1, 2, 3}, -7, 7);
        test(new int[] {1, 2, 3}, -7, 7);
        test(new int[] {5, -7, 3, 4}, -2, 0);
        test(new int[] {3, 15, -17, 9, 28, 34, -4, 31, -6, 23, 4, 52}, 14, 0);
        test(new int[] {3, 15, -17, 9, 28, -61, 34, 14, 31, -6, 23, 4, 12}, 165, 1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
