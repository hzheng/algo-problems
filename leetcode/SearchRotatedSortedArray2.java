import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// Follow up for "Search in Rotated Sorted Array":
// Search a sorted array which is rotated at some unknown pivot beforehand.
// What if duplicates are allowed?
//Would this affect the run-time complexity? How and why?
public class SearchRotatedSortedArray2 {
    // beats 23.71%
    public boolean search(int[] nums, int target) {
        return search(nums, 0, nums.length - 1, target) >= 0;
    }

    private int search(int nums[], int left, int right, int target) {
        int mid = (left + right) / 2;
        if (target == nums[mid]) return mid;

        if (right < left) return -1;

        if (nums[left] < nums[mid]) {
            if (target >= nums[left] && target < nums[mid]) {
                return search(nums, left, mid - 1, target);
            } else {
                return search(nums, mid + 1, right, target);
            }
        } else if (nums[left] > nums[mid]) {
            if (target > nums[mid] && target <= nums[right]) {
                return search(nums, mid + 1, right, target);
            } else {
                return search(nums, left, mid - 1, target);
            }
        }
        // nums[left] == nums[mid])
        // target could be anywhere: e.g. if all but target are equals
        int midVal = nums[mid];
        int i = left + 1;
        for (; i < mid; i++) {
            if (nums[i] != midVal) break;
        }
        if (i >= mid) { // all left part are equal
            return search(nums, mid + 1, right, target);
        } else {
            return search(nums, i, mid - 1, target);
        }
    }

    // beats 23.71%
    public boolean search2(int[] nums, int target) {
        for (int left = 0, right = nums.length - 1; left <= right; ) {
            int mid = (left + right) / 2;
            int midVal = nums[mid];
            if (target == midVal) return true;

            if (nums[left] < midVal) {
                if (target >= nums[left] && target < midVal) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else if (nums[left] > midVal) {
                if (target > midVal && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            } else {
                int i = left + 1;
                for (; i < mid; i++) {
                    if (nums[i] != midVal) break;
                }
                if (i >= mid) { // all left part are equal
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        return false;
    }

    void test(boolean expected, int target, int ... nums) {
        assertEquals(expected, search(nums, target));
        assertEquals(expected, search2(nums, target));
    }

    @Test
    public void test1() {
        test(true, 1, 1, 3);
        test(true, 3, 1, 3);
        test(true, 3, 1, 3, 1, 1, 1, 1);
        test(false, 3, 4, 5, 6, 7, 8, 9);
        test(false, 3, 4, 5, 6, 7, 0, 1, 2);
        test(true, 7, 4, 5, 6, 7, 8, 0, 1, 2, 3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SearchRotatedSortedArray2");
    }
}
