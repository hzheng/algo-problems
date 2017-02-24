import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC259: https://leetcode.com/problems/3sum-smaller/
//
// Given an array of n integers nums and a target, find the number of index
// triplets i, j, k with 0 <= i < j < k < n that satisfy the condition
// nums[i] + nums[j] + nums[k] < target.
public class ThreeSumSmaller {
    // Sort + Two Pointers
    // beats 90.75%(5 ms for 313 tests)
    public int threeSumSmaller(int[] nums, int target) {
        Arrays.sort(nums);
        int n = nums.length;
        int count = 0;
        for (int i = 0; i < n - 2; i++) {
            int max = target - nums[i];
            for (int j = i + 1, k = n - 1; j < k; ) {
                if (nums[j] + nums[k] < max) {
                    count += k - j;
                    j++;
                } else { // Binary Search?
                    k--;
                }
            }
        }
        return count;
    }

    // Sort + Binary Search
    // beats 12.53%(13 ms for 313 tests)
    public int threeSumSmaller2(int[] nums, int target) {
        Arrays.sort(nums);
        int count = 0;
        for (int i = 0; i < nums.length - 2; i++) {
            count += twoSumSmaller(nums, i + 1, target - nums[i]);
        }
        return count;
    }

    private int twoSumSmaller(int[] nums, int startIndex, int target) {
        int count = 0;
        for (int i = startIndex; i < nums.length - 1; i++) {
            count += binarySearch(nums, i, target - nums[i]) - i;
        }
        return count;
    }

    private int binarySearch(int[] nums, int startIndex, int target) {
        int low = startIndex;
        for (int high = nums.length - 1; low < high; ) {
            int mid = low + (high - low + 1) / 2;
            if (nums[mid] < target) {
                low = mid;
            } else {
                high = mid - 1;
            }
        }
        return low;
    }

    void test(int[] nums, int target, int expected) {
        assertEquals(expected, threeSumSmaller(nums, target));
        assertEquals(expected, threeSumSmaller2(nums, target));
    }

    @Test
    public void test() {
        test(new int[] {-2, 0, 1, 3}, 2, 2);
        // test(new int[] {1, 0, -1}, 0, 0);
        // test(new int[] {1, 1, -2}, 1, 1);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ThreeSumSmaller");
    }
}
