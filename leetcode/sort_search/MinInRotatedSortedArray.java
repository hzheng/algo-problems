import org.junit.Test;
import static org.junit.Assert.*;

// LC153: https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/
//
// Suppose a sorted array is rotated at some pivot unknown to you beforehand.
// Find the minimum element.
// You may assume no duplicate exists in the array.
public class MinInRotatedSortedArray {
    // Binary Search
    // beats 3.09%(1 ms)
    public int findMin(int[] nums) {
        int right = nums.length - 1;
        int first = nums[0];
        if (nums[right] >= first) return first;

        int left = 0;
        while (right > left) {
            int pivot = (left + right) >>> 1;
            if (nums[pivot] >= first) {
                left = pivot + 1;
            } else {
                right = pivot;
            }
        }
        return Math.min(nums[right], nums[left]);
    }

    // Solution of Choice
    // Binary Search
    // beats 3.09%(1 ms)
    public int findMin2(int[] nums) {
        int left = 0;
        for (int right = nums.length - 1; right > left; ) {
            int mid = (left + right) >>> 1;
            if (nums[mid] > nums[right]) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return nums[left];
    }

    void test(int expected, int ... nums) {
        assertEquals(expected, findMin(nums));
        assertEquals(expected, findMin2(nums));
    }

    @Test
    public void test1() {
        test(1, 1, 2, 3);
        test(1, 3, 1, 2);
        test(1, 2, 3, 1);
        test(0, 3, 4, 5, 6, 7, 0, 1, 2);
        test(1, 4, 5, 6, 7, 8, 1, 2, 3);
        test(1, 2, 3, 5, 1);
        test(1, 3, 4, 1, 2);
        test(1, 4, 5, 1, 3);
        test(3, 3, 4, 5, 6, 7, 8, 9);
        test(4, 10, 4, 5, 6, 7, 8, 9);
        test(0, 4, 5, 6, 7, 0, 1, 2);
        test(0, 3, 4, 5, 6, 7, 0, 1, 2);
        test(0, 4, 5, 6, 7, 8, 0, 1, 2, 3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MinInRotatedSortedArray");
    }
}
