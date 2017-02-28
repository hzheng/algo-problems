import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC523: https://leetcode.com/problems/continuous-subarray-sum/
//
// Given a list of non-negative numbers and a target integer k, write a function
// to check if the array has a continuous subarray of size at least 2 that sums
// up to the multiple of k.
public class ContinuousSubarraySum {
    // Dynamic Programming
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 27.88%(79 ms for 75 tests)
    public boolean checkSubarraySum(int[] nums, int k) {
        int n = nums.length;
        int[] dp = new int[n + 1];
        for (int i = 0; i < n; i++) {
            dp[i + 1] = dp[i] + nums[i];
        }
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 2; j <= n; j++) {
                if ((dp[j] == dp[i]) || k != 0 && (dp[j] - dp[i]) % k == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 26.74%(80 ms for 75 tests)
    public boolean checkSubarraySum2(int[] nums, int k) {
        int n = nums.length;
        for (int i = 0; i < n - 1; i++) {
            int sum = nums[i];
            for (int j = i + 1; j < n; j++) {
                sum += nums[j];
                if (k == 0 && sum == 0 || k != 0 && sum % k == 0) return true;
            }
        }
        return false;
    }

    // Set
    // time complexity: O(N), space complexity: O(N)
    // beats 97.41%(12 ms for 75 tests)
    public boolean checkSubarraySum3(int[] nums, int k) {
        int n = nums.length;
        if (k == 0) {
            for (int i = 1; i < n; i++) {
                if (nums[i] == 0 && nums[i - 1] == 0) return true;
            }
            return false;
        }

        Set<Integer> indices = new HashSet<>();
        for (int i = 0, sum = 0; i < n; i++) {
            sum += nums[i];
            if ((sum %= k) == 0 && i > 0 || !indices.add(sum)) return true;
        }
        return false;
    }

    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // beats 78.28%(18 ms for 75 tests)
    public boolean checkSubarraySum4(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, -1);
        for (int i = 0, sum = 0, n = nums.length; i < n; i++) {
            sum += nums[i];
            if (k != 0) {
                sum %= k;
            }
            Integer prev = map.putIfAbsent(sum, i);
            if (prev != null && (i - prev) > 1) return true;
        }
        return false;
    }

    void test(int[] nums, int k, boolean expected) {
        assertEquals(expected, checkSubarraySum(nums, k));
        assertEquals(expected, checkSubarraySum2(nums, k));
        assertEquals(expected, checkSubarraySum3(nums, k));
        assertEquals(expected, checkSubarraySum4(nums, k));
    }

    @Test
    public void test() {
        test(new int[] {1, 3}, 2, true);
        test(new int[] {0, 0}, 0, true);
        test(new int[] {0, 1, 0}, 0, false);
        test(new int[] {23, 2, 6, 4, 7}, 17, true);
        test(new int[] {1, 1}, 2, true);
        test(new int[] {1000000000}, 1000000000, false);
        test(new int[] {6}, 6, false);
        test(new int[] {23, 2, 4, 6, 7}, 6, true);
        test(new int[] {}, 1, false);
        test(new int[] {23, 2, 4, 6, 7}, 0, false);
        test(new int[] {23, 2, 6, 4, 7}, 6, true);
        test(new int[] {23, 2, 6, 4, 7}, 29, false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ContinuousSubarraySum");
    }
}
