import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC561: https://leetcode.com/problems/array-partition-i/
//
// Given an array of 2n integers, your task is to group these integers into n pairs
// of integer, say (a1, b1), (a2, b2), ..., (an, bn) which makes sum of min(ai, bi)
// for all i from 1 to n as large as possible.
// Note:
// n is a positive integer, which is in the range of [1, 10000].
// All the integers in the array will be in the range of [-10000, 10000].
public class ArrayPartition {
    // Sort
    // time complexity: O(N * log(N)), space complexity: O(1)
    // beats 26.04%(41 ms for 81 tests)
    public int arrayPairSum(int[] nums) {
        Arrays.sort(nums);
        int sum = 0;
        for (int i = 0; i < nums.length; i += 2) {
            sum += nums[i];
        }
        return sum;
    }

    // Counting Sort
    // time complexity: O(N), space complexity: O(N)
    // beats 97.09%(27 ms for 81 tests)
    public int arrayPairSum2(int[] nums) {
        final int LIMIT = 10000;
        int[] count = new int[20001];
        for (int num : nums) {
            count[num + LIMIT]++;
        }
        int sum = 0;
        for (int i = -LIMIT, even = 0; i <= LIMIT; i++) {
            sum += (count[i + LIMIT] + 1 - even) / 2 * i;
            even = (count[i + LIMIT] + even) % 2;
        }
        return sum;
    }

    void test(int[] nums, int expected) {
        assertEquals(expected, arrayPairSum(nums.clone()));
        assertEquals(expected, arrayPairSum2(nums.clone()));
    }

    @Test
    public void test() {
        test(new int[] {1, 4, 3, 2}, 4);
        test(new int[] {1, 4, 8, -10, -9, 13, 2, 10}, 5);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
