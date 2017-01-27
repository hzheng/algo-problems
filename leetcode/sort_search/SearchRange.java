import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC034: https://leetcode.com/problems/search-for-a-range/
//
// Given a sorted array, find the range of a given target.
public class SearchRange {
    // beats 65.47%(0 ms)
    public int[] searchRange(int[] nums, int target) {
        int[] range = {-1, -1};
        int left = 0;
        int right = nums.length - 1;
        int found = -1;
        while (left <= right) {
            int mid = (left + right) >>> 1;
            if (nums[mid] < target) {
                left = mid + 1;
            } else if (nums[mid] > target) {
                right = mid - 1;
            } else {
                found = mid;
                break;
            }
        }
        if (found < 0) return range;

        for (int r = found; left <= r; ) {
            int mid = (left + r) >>> 1;
            if (nums[mid] == target) {
                r = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        range[0] = left;
        for (left = found; left <= right; ) {
            int mid = (left + right) >>> 1;
            if (nums[mid] == target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        range[1] = right;
        return range;
    }

    // Solution of Choice
    // beats 65.47%(0 ms)
    // two loops(one fewer that the last one, but may search more)
    public int[] searchRange2(int[] nums, int target) {
        int left = 0;
        for (int right = nums.length - 1; left <= right; ) {
            int mid = (left + right) >>> 1;
            if (nums[mid] >= target) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        if (left == nums.length || nums[left] != target) return new int[]{-1, -1};

        int right = nums.length - 1;
        for (int l = left; l <= right; ) {
            int mid = (l + right) >>> 1;
            if (nums[mid] == target) {
                l = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return new int[]{left, right};
    }

    void test(int[] expected, int target, int ... nums) {
        assertArrayEquals(expected, searchRange(nums, target));
        assertArrayEquals(expected, searchRange2(nums, target));
    }

    @Test
    public void test1() {
        test(new int[]{-1, -1}, 3, 2, 2);
        test(new int[]{1, 1}, 2, 1, 2, 3);
        test(new int[]{0, 0}, 8, 8);
        test(new int[]{0, 1}, 8, 8, 8, 9);
        test(new int[]{3, 4}, 8, 5, 7, 7, 8, 8);
        test(new int[]{3, 4}, 8, 5, 7, 7, 8, 8, 10);
        test(new int[]{3, 3}, 8, 5, 7, 7, 8, 9, 10);
        test(new int[]{-1, -1}, 6, 5, 7, 7, 8, 9, 10);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SearchRange");
    }
}
