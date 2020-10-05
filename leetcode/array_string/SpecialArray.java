import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1608: https://leetcode.com/problems/special-array-with-x-elements-greater-than-or-equal-x/
//
// You are given an array nums of non-negative integers. nums is considered special if there exists
// a number x such that there are exactly x numbers in nums that are greater than or equal to x.
// Notice that x does not have to be an element in nums.
// Return x if the array is special, otherwise, return -1. It can be proven that if nums is special,
// the value for x is unique.
// Constraints:
// 1 <= nums.length <= 100
// 0 <= nums[i] <= 1000
public class SpecialArray {
    // time complexity: O(N*MAX), space complexity: O(MAX)
    // 1 ms(100%), 36.8 MB(78.20%) for 95 tests
    public int specialArray(int[] nums) {
        final int max = 1000;
        int[] count = new int[max + 1];
        for (int num : nums) {
            for (int i = 0; i <= num; i++) {
                count[i]++;
            }
        }
        for (int i = 0; i <= max; i++) {
            if (count[i] == i) { return i; }
        }
        return -1;
    }

    // Sort
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 0 ms(100%), 36.8 MB(78.20%) for 95 tests
    public int specialArray2(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            int count = n - i;
            if ((count <= nums[i]) && ((i == 0) || (count > nums[i - 1]))) {
                return count;
            }
        }
        return -1;
    }

    // Sort
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 6 ms(6.19%), 36.8 MB(78.20%) for 95 tests
    public int specialArray3(int[] nums) {
        nums = Arrays.stream(nums).boxed().sorted(Collections.reverseOrder())
                     .mapToInt(Integer::intValue).toArray();
        int i = 0;
        for (int n = nums.length; i < n && nums[i] >= i; i++) {}
        return (nums[i - 1] < i) ? -1 : i;
    }

    // Sort + Binary Search
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 6 ms(6.19%), 36.8 MB(78.20%) for 95 tests
    public int specialArray4(int[] nums) {
        nums = Arrays.stream(nums).boxed().sorted(Collections.reverseOrder())
                     .mapToInt(Integer::intValue).toArray();
        int low = 0;
        for (int high = nums.length; low < high; ) {
            int mid = (low + high) >>> 1;
            if (mid < nums[mid]) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return (low < nums.length && low == nums[low]) ? -1 : low;
    }

    // Count Sort
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100%), 37.1 MB(71.68%) for 95 tests
    public int specialArray5(int[] nums) {
        int n = nums.length;
        int[] count = new int[n + 2];
        for (int num : nums) {
            count[Math.min(n, num)]++;
        }
        for (int i = n; i > 0; i--) {
            count[i] += count[i + 1];
            if (count[i] == i) { return i; }
        }
        return -1;
    }

    // Count Sort
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100%), 36.8 MB(79.68%) for 95 tests
    public int specialArray6(int[] nums) {
        int n = nums.length;
        int[] count = new int[n + 1];
        for (int num : nums) {
            count[Math.min(n, num)]++;
        }
        for (int i = 0, biggerCount = n; i <= n; biggerCount -= count[i++]) {
            if (biggerCount == i) { return i; }
        }
        return -1;
    }

    void test(int[] nums, int expected) {
        assertEquals(expected, specialArray(nums));
        assertEquals(expected, specialArray2(nums));
        assertEquals(expected, specialArray3(nums));
        assertEquals(expected, specialArray4(nums));
        assertEquals(expected, specialArray5(nums));
        assertEquals(expected, specialArray6(nums));
    }

    @Test public void test() {
        test(new int[] {3, 5}, 2);
        test(new int[] {0, 0}, -1);
        test(new int[] {0, 4, 3, 0, 4}, 3);
        test(new int[] {3, 6, 7, 7, 0}, -1);
        test(new int[] {3, 9, 8, 10, 5, 3, 1, 7, 2, 6, 7, 7, 0}, -1);
        test(new int[] {3, 9, 8, 10, 5, 2, 7, 3, 1, 7, 2, 6, 7, 7, 0}, 7);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
