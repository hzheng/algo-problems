import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/largest-divisible-subset/
//
// Given a set of distinct positive integers, find the largest subset such that
// every pair (Si, Sj) of elements in this subset satisfies: Si % Sj = 0 or
// Sj % Si = 0.
public class LargestDivisibleSubset {
    // Dynamic Programming
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 68.64%(36 ms)
    public List<Integer> largestDivisibleSubset(int[] nums) {
        int n = nums.length;
        if (n == 0) return Collections.emptyList();

        Arrays.sort(nums);
        int[][] factors = new int[n][];
        int max = 0;
        int maxIndex = 0;
        for (int i = 0; i < n; i++) {
            int num = nums[i];
            factors[i] = new int[2];
            for (int j = i - 1; j >= 0; j--) {
                if (num % nums[j] == 0 && factors[j][0] > factors[i][0]) {
                    factors[i][0] = factors[j][0];
                    factors[i][1] = j;
                }
            }
            if (++factors[i][0] > max) {
                maxIndex = i;
                max = factors[i][0];
            }
        }
        List<Integer> res = new LinkedList<>();
        for (int i = factors[maxIndex][0] - 1; i >= 0; i--) {
            int[] maxCount = factors[maxIndex];
            res.add(0, nums[maxIndex]);
            maxIndex = maxCount[1];
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
    }

    @Test
    public void test1() {
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
