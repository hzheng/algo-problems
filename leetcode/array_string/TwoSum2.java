import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC167: https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/
//
// Given an array of integers that is already sorted in ascending order,
// find two numbers such that they add up to a specific target number.
// You may assume that each input would have exactly one solution.
public class TwoSum2 {
    // Solution of Choice
    // Two Pointers
    // beats 24.59%(2 ms for 15 tests)
    public int[] twoSum(int[] numbers, int target) {
        for (int i = 0, j = numbers.length - 1; i < j; ) {
            int sum = numbers[i] + numbers[j];
            if (sum < target) {
                i++;
            } else if (sum > target) {
                j--;
            } else return new int[] {i + 1, j + 1};
        }
        return null;
    }

    // Binary Search
    // beats 21.28%(3 ms for 15 tests)
    public int[] twoSum2(int[] numbers, int target) {
        int n = numbers.length;
        for (int i = 0; i < n; i++) {
            int j = Arrays.binarySearch(numbers, i + 1, n, target - numbers[i]);
            if (j >= 0) return new int[] {i + 1, j + 1};
        }
        return null;
    }

    void test(int[] nums, int target, int ... expected) {
        assertArrayEquals(expected, twoSum(nums, target));
        assertArrayEquals(expected, twoSum2(nums, target));
    }

    @Test
    public void test1() {
        test(new int[] {2, 7, 11, 15}, 9, 1, 2);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("TwoSum2");
    }
}
