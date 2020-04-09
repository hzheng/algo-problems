import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1365: https://leetcode.com/problems/how-many-numbers-are-smaller-than-the-current-number/
//
// Given the array nums, for each nums[i] find out how many numbers in the array are smaller than it. That is, for each
// nums[i] you have to count the number of valid j's such that j != i and nums[j] < nums[i].
// Return the answer in an array.
// Constraints:
// 2 <= nums.length <= 500
// 0 <= nums[i] <= 100
public class SmallerNumbersThanCurrent {
    // Bucket
    // time complexity: O(N), space complexity: O(N)
    // 1 ms(99.68%), 37.9 MB(100%) for 103 tests
    public int[] smallerNumbersThanCurrent(int[] nums) {
        int n = nums.length;
        int[] count = new int[101];
        for (int num : nums) {
            count[num]++;
        }
        for (int i = 1; i < count.length; i++) {
            count[i] += count[i - 1];
        }
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            if (nums[i] > 0) {
                res[i] = count[nums[i] - 1];
            }
        }
        return res;
    }

    // Sort + Hash Table
    // time complexity: O(N * log(N)), space complexity: O(N)
    // 3 ms(80.21%), 37.9 MB(100%) for 103 tests
    public int[] smallerNumbersThanCurrent2(int[] nums) {
        Map<Integer, Integer> count = new HashMap<>();
        int[] res = nums.clone();
        Arrays.sort(res);
        for (int i = 0; i < nums.length; i++) {
            count.putIfAbsent(res[i], i);
        }
        for (int i = 0; i < nums.length; i++) {
            res[i] = count.get(nums[i]);
        }
        return res;
    }

    // Sort + Binary Search
    // time complexity: O(N * log(N)), space complexity: O(N)
    // 2 ms(82.65%), 39.6 MB(100%) for 103 tests
    public int[] smallerNumbersThanCurrent3(int[] nums) {
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        int[] res = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            res[i] = binarySearch(sorted, nums[i]);
        }
        return res;
    }

    private int binarySearch(int[] nums, int target) {
        int low = 0;
        for (int high = nums.length - 1; low < high; ) {
            int mid = (low + high) >>> 1;
            if (nums[mid] < target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    private void test(int[] nums, int[] expected) {
        assertArrayEquals(expected, smallerNumbersThanCurrent(nums));
        assertArrayEquals(expected, smallerNumbersThanCurrent2(nums));
        assertArrayEquals(expected, smallerNumbersThanCurrent3(nums));
    }

    @Test
    public void test() {
        test(new int[]{8, 1, 2, 2, 3}, new int[]{4, 0, 1, 1, 3});
        test(new int[]{6, 5, 4, 8}, new int[]{2, 1, 0, 3});
        test(new int[]{7, 7, 7, 7}, new int[]{0, 0, 0, 0});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
