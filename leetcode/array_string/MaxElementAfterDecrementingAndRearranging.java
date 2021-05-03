import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1846: https://leetcode.com/problems/maximum-element-after-decreasing-and-rearranging/
//
// You are given an array of positive integers arr. Perform some operations (possibly none) on arr
// so that it satisfies these conditions:
// The value of the first element in arr must be 1.
// The absolute difference between any 2 adjacent elements must be less than or equal to 1. In other
// words, abs(arr[i] - arr[i - 1]) <= 1 for each i where 1 <= i < arr.length (0-indexed). abs(x) is
// the absolute value of x.
// There are 2 types of operations that you can perform any number of times:
// Decrease the value of any element of arr to a smaller positive integer.
// Rearrange the elements of arr to be in any order.
// Return the maximum possible value of an element in arr after performing the operations to satisfy
// the conditions.
//
// Constraints:
// 1 <= arr.length <= 10^5
// 1 <= arr[i] <= 10^9
public class MaxElementAfterDecrementingAndRearranging {
    // Sort
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 3 ms(99.90%), 55.5 MB(77.72%) for 49 tests
    public int maximumElementAfterDecrementingAndRearranging(int[] arr) {
        Arrays.sort(arr);
        int res = 0;
        for (int a : arr) {
            res = Math.min(res + 1, a);
        }
        return res;
    }

    private void test(int[] arr, int expected) {
        assertEquals(expected, maximumElementAfterDecrementingAndRearranging(arr));
    }

    @Test public void test1() {
        test(new int[] {2, 2, 1, 2, 1}, 2);
        test(new int[] {100, 1, 1000}, 3);
        test(new int[] {1, 2, 3, 4, 5}, 5);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
