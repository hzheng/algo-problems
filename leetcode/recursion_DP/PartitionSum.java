import org.junit.Test;
import static org.junit.Assert.*;

// LC416: https://leetcode.com/problems/partition-equal-subset-sum/
//
// Given a non-empty array containing only positive integers, find if the array
// can be partitioned into two subsets such that the sum of elements in both
// subsets is equal.
public class PartitionSum {
    // Recursion
    // beats N/A(61 ms for 89 tests)
    public boolean canPartition(int[] nums) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        if (sum % 2 != 0) return false;

        return canPartition(nums, 0, nums.length, sum / 2);
    }

    private boolean canPartition(int[] nums, int start, int end, int target) {
        if (target < 0) return false;
        if (start >= end) return target == 0;

        for (int i = start; i < end; i++) {
            if (canPartition(nums, i + 1, end, target - nums[i])) return true;
        }
        return false;
    }

    // Dynamic Programming
    // beats N/A(24 ms for 89 tests)
    public boolean canPartition2(int[] nums) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        if (sum % 2 != 0) return false;

        sum /= 2;
        boolean[] dp = new boolean[sum + 1];
        dp[0] = true;
        for (int num : nums) {
            for (int i = num; i <= sum; i++) {
                dp[i] |= dp[i - num];
            }
        }
        return dp[sum];
    }

    void test(int[] nums, boolean expected) {
        assertEquals(expected, canPartition(nums));
        assertEquals(expected, canPartition2(nums));
    }

    @Test
    public void test() {
        test(new int[]{100}, false);
        test(new int[]{1, 3, 5, 5, 11, 8, 5}, true);
        test(new int[]{2, 3, 5, 5, 11, 8, 5}, false);
        test(new int[]{1, 5, 11, 5}, true);
        test(new int[]{1, 2, 3, 5}, false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PartitionSum");
    }
}
