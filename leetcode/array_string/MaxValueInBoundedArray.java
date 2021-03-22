import org.junit.Test;

import static org.junit.Assert.*;

// LC1802: https://leetcode.com/problems/maximum-value-at-a-given-index-in-a-bounded-array/
//
// You are given three positive integers n, index and maxSum. You want to construct an array nums
// (0-indexed) that satisfies the following conditions:
// nums.length == n
// nums[i] is a positive integer where 0 <= i < n.
// abs(nums[i] - nums[i+1]) <= 1 where 0 <= i < n-1.
// The sum of all the elements of nums does not exceed maxSum.
// nums[index] is maximized.
// Return nums[index] of the constructed array.
// Note that abs(x) equals x if x >= 0, and -x otherwise.
//
// Constraints:
// 1 <= n <= maxSum <= 109
// 0 <= index < n
public class MaxValueInBoundedArray {
    // Binary Search
    // time complexity: O(log(MAX)), space complexity: O(1)
    // 1 ms(%), 35.8 MB(%) for 370 tests
    public int maxValue(int n, int index, int maxSum) {
        int low = maxSum / n;
        for (int high = maxSum; low < high; ) {
            int mid = (low + high + 1) >>> 1;
            long sum = mid + countSum(mid, index) + countSum(mid, n - 1 - index);
            if (sum <= maxSum) {
                low = mid;
            } else {
                high = mid - 1;
            }
        }
        return low;
    }

    private long countSum(int x, int index) {
        long count = Math.min(index, x - 1);
        long sum = ((x - 1) - count + 1 + x - 1) * count / 2;
        if (count < index) {
            sum += (index - count);
        }
        return sum;
    }

    // TODO: O(1) by math computing

    private void test(int n, int index, int maxSum, int expected) {
        assertEquals(expected, maxValue(n, index, maxSum));
    }

    @Test public void test() {
        test(4, 2, 6, 2);
        test(6, 1, 10, 3);
        test(3, 0, 815094800, 271698267);
        test(6, 2, 931384943, 155230825);
        test(7, 0, 930041194, 132863030);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
