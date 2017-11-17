import org.junit.Test;
import static org.junit.Assert.*;

// LC724: https://leetcode.com/problems/find-pivot-index/
//
// Given an array of integers nums, write a method that returns the "pivot"
// index of this array. We define the pivot index as the index where the sum of
// the numbers to the left of the index is equal to the sum of the numbers to
// the right of the index. If no such index exists, we should return -1. If 
// there are multiple pivot indexes, you should return the left-most pivot index.
public class PivotIndex {
    // time complexity: O(N), space complexity: O(N)
    // beats 64.43%(36 ms for 741 tests)
    public int pivotIndex(int[] nums) {
        int n = nums.length;
        int[] right = new int[n + 1];
        for (int i = n - 1; i >= 0; i--) {
            right[i] = right[i + 1] + nums[i];
        }
        int left = 0;
        for (int i = 0; i < n; i++) {
            left += nums[i];
            if (left == right[i]) return i;
        }
        return -1;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats 64.43%(36 ms for 741 tests)
    public int pivotIndex2(int[] nums) {
        int sum = 0;
        int leftSum = 0;
        for (int num : nums) {
            sum += num;
        }
        for (int i = 0; i < nums.length; i++) {
            if (leftSum == sum - leftSum - nums[i]) return i;
            leftSum += nums[i];
        }
        return -1;
    }

    void test(int[] nums, int expected) {
        assertEquals(expected, pivotIndex(nums));
        assertEquals(expected, pivotIndex2(nums));
    }

    @Test
    public void test() {
        test(new int[]{1, 7, 3, 6, 5, 6}, 3);
        test(new int[]{1, 2, 3}, -1);
    }

    public static void main(String[] args) {
        String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
