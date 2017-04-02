import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC548: https://leetcode.com/problems/split-array-with-equal-sum/
//
// Given an array with n integers, you need to find if there are triplets (i, j, k)
// which satisfies following conditions:
// 0 < i, i + 1 < j, j + 1 < k < n - 1
// Sum of subarrays (0, i - 1), (i + 1, j - 1), (j + 1, k - 1) and (k + 1, n - 1)
// should be equal.
public class SplitArrayWithEqualSum {
    // time complexity: O(N ^ 3), space complexity: O(N)
    // beats N/A(37 ms for 120 tests)
    public boolean splitArray(int[] nums) {
        int n = nums.length;
        int[] sums = new int[n + 1];
        int index = 0;
        for (int num : nums) {
            index++;
            sums[index] = sums[index - 1] + num;
        }
        for (int i = 1; i < n - 5; i++) {
            for (int k = n - 2; k > i + 3; k--) {
                int sum = sums[i];
                if (sum != sums[n] - sums[k + 1]) continue;

                for (int j = i + 1; j < k; j++) {
                    if ((sums[j] - sums[i + 1] == sum)
                        && (sums[k] - sums[j + 1] == sum)) return true;
                }
            }
        }
        return false;
    }

    // Set
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats N/A(146 ms for 120 tests)
    public boolean splitArray2(int[] nums) {
        int n = nums.length;
        int[] sums = new int[n + 1];
        int index = 0;
        for (int num : nums) {
            index++;
            sums[index] = sums[index - 1] + num;
        }
        for (int j = 3; j < n - 3; j++) {
            Set<Integer> set = new HashSet<>();
            for (int i = 1; i < j - 1; i++) {
                if (sums[i] == sums[j] - sums[i + 1]) {
                    set.add(sums[i]);
                }
            }
            for (int k = j + 2; k < n - 1; k++) {
                int sum = sums[n] - sums[k + 1];
                if (sum == sums[k] - sums[j + 1] && set.contains(sum)) return true;
            }
        }
        return false;
    }

    void test(int[] nums, boolean expected) {
        assertEquals(expected, splitArray(nums));
        assertEquals(expected, splitArray2(nums));
    }

    @Test
    public void test() {
        test(new int[] {1, 2, 1, 2, 1, 2, 1}, true);
        test(new int[] {1, 2, 1, 3, 0, 0, 2, 2, 1, 3, 3}, true);
    }

    public static void main(String[] args) {
        String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
