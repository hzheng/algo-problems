import org.junit.Test;
import static org.junit.Assert.*;

// LC643: https://leetcode.com/problems/maximum-average-subarray-i/
//
// Given an array consisting of n integers, find the contiguous subarray of
// given length k that has the maximum average value. And you need to output the
// maximum average value.
public class FindMaxAverage {
    // beats 62.94%(20 ms for 123 tests)
    public double findMaxAverage(int[] nums, int k) {
        int sum = 0;
        for (int i = 0; i < k; i++) {
            sum += nums[i];
        }
        int res = sum;
        for (int i = k; i < nums.length; i++) {
            sum += nums[i] - nums[i - k];
            res = Math.max(res, sum);
        }
        return res / (double)k;
    }

    void test(int[] nums, int k, double expected) {
        assertEquals(expected, findMaxAverage(nums, k), 1e-6);
    }

    @Test
    public void test() {
        test(new int[] {1, 12, -5, -6, 50, 3}, 4, 12.75);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
