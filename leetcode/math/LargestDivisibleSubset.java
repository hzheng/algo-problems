import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC368: https://leetcode.com/problems/largest-divisible-subset/
//
// Given a set of distinct positive integers, find the largest subset such that
// every pair (Si, Sj) of elements in this subset satisfies: Si % Sj = 0 or
// Sj % Si = 0.
public class LargestDivisibleSubset {
    // Dynamic Programming
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 74.85%(33 ms for 36 tests)
    public List<Integer> largestDivisibleSubset(int[] nums) {
        int n = nums.length;
        int[][] dp = new int[n][];
        int max = 0;
        int maxIndex = 0;
        Arrays.sort(nums);
        for (int i = 0; i < n; i++) {
            int num = nums[i];
            dp[i] = new int[2];
            for (int j = i - 1; j >= 0; j--) {
                if (num % nums[j] == 0 && dp[j][0] > dp[i][0]) {
                    dp[i][0] = dp[j][0];
                    dp[i][1] = j;
                }
            }
            if (++dp[i][0] > max) {
                maxIndex = i;
                max = dp[i][0];
            }
        }
        List<Integer> res = new LinkedList<>();
        for (int i = maxIndex, j = max; j > 0; i = dp[i][1], j--) {
            res.add(0, nums[i]);
        }
        return res;
    }

    // Solution of Choice
    // beats 66.25%(34 ms for 36 tests)
    public List<Integer> largestDivisibleSubset2(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        int[] prev = new int[n];
        int max = 0;
        int maxIndex = 0;
        Arrays.sort(nums);
        for (int i = 0; i < n; i++) {
            int num = nums[i];
            for (int j = i - 1; j >= 0; j--) {
                if (num % nums[j] == 0 && dp[j] > dp[i]) {
                    dp[i] = dp[j];
                    prev[i] = j;
                }
            }
            if (++dp[i] > max) {
                maxIndex = i;
                max = dp[i];
            }
        }
        List<Integer> res = new LinkedList<>();
        for (int i = maxIndex, j = max; j > 0; i = prev[i], j--) {
            res.add(0, nums[i]);
        }
        return res;
    }

    void test(Function<int[], List<Integer> > largestDivisibleSubset,
              int[] nums, Integer ... expected) {
        assertArrayEquals(expected, largestDivisibleSubset.apply(nums)
                          .toArray(new Integer[0]));
    }

    void test(int[] nums, Integer ... expected) {
        LargestDivisibleSubset l = new LargestDivisibleSubset();
        test(l::largestDivisibleSubset, nums, expected);
        test(l::largestDivisibleSubset2, nums, expected);
    }

    @Test
    public void test1() {
        test(new int[] {});
        test(new int[] {1, 2, 3}, 1, 2);
        test(new int[] {1, 2, 4, 8}, 1, 2, 4, 8);
        test(new int[] {1, 2, 4, 8, 12, 32}, 1, 2, 4, 8, 32);
        test(new int[] {1, 2, 3, 12, 6, 4, 36, 8, 12, 32}, 1, 3, 6, 12, 12, 36);
        test(new int[] {1, 2, 4, 8, 9, 72}, 1, 2, 4, 8, 72);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LargestDivisibleSubset");
    }
}
