import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC154: https://leetcode.com/problems/find-minimum-in-rotated-sorted-array-ii/
//
// Suppose a sorted array is rotated at some pivot unknown to you beforehand.
// Find the minimum element. The array may contain duplicates.
public class MinInRotatedSortedArray2 {
    // Solution of Choice
    // beats 6.02%(1 ms)
    public int findMin(int[] nums) {
        int left = 0;
        for (int right = nums.length - 1; right > left; ) {
            int mid = (left + right) >>> 1;
            int rightVal = nums[right];
            if (nums[mid] > rightVal) {
                left = mid + 1;
            } else if (nums[mid] < rightVal) {
                right = mid;
            } else {
                right--;
            }
        }
        return nums[left];
    }

    void test(int expected, int ... nums) {
        assertEquals(expected, findMin(nums));
    }

    @Test
    public void test1() {
        test(0, 0, 1, 1, 1);
        test(1, 1, 2, 3);
        test(1, 3, 1, 2);
        test(1, 1, 1, 1, 1);
        test(0, 1, 1, 0, 1);
        test(1, 1, 1, 2, 1);
        test(1, 2, 3, 1);
        test(0, 2, 4, 5, 6, 7, 0, 1, 2);
        test(1, 4, 5, 6, 7, 1, 1, 2, 3);
        test(1, 2, 3, 5, 1);
        test(1, 3, 4, 1, 2);
        test(1, 4, 4, 1, 3);
        test(3, 3, 4, 6, 6, 7, 8, 9);
        test(4, 9, 4, 5, 6, 7, 8, 9);
        test(0, 2, 5, 6, 7, 0, 1, 2);
        test(0, 3, 4, 5, 5, 0, 0, 1, 2);
        test(0, 4, 4, 5, 6, 7, 8, 0, 1, 2, 3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MinInRotatedSortedArray2");
    }
}
