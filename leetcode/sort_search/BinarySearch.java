import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC704: https://leetcode.com/problems/binary-search/
//
// Given a sorted (in ascending order) integer array nums of n elements and a
// target value, write a function to search target in nums. If target exists,
// then return its index, otherwise return -1.
public class BinarySearch {
    // beats 83.52%(3 ms for 46 tests)
    public int search(int[] nums, int target) {
        int low = 0;
        for (int high = nums.length - 1; low <= high; ) {
            int mid = (low + high) >>> 1;
            if (nums[mid] < target) {
                low = mid + 1;
            } else if (nums[mid] > target) {
                high = mid - 1;
            } else return mid;
        }
        return -1;
    }

    // beats 83.52%(3 ms for 46 tests)
    public int search2(int[] nums, int target) {
        int res = Arrays.binarySearch(nums, target);
        return res >= 0 ? res : -1;
    }

    void test(int[] nums, int target, int expected) {
        assertEquals(expected, search(nums, target));
        assertEquals(expected, search2(nums, target));
    }

    @Test
    public void test() {
        test(new int[] { -1, 0, 3, 5, 9, 12 }, 9, 4);
        test(new int[] { -1, 0, 3, 5, 9, 12 }, 2, -1);
        test(new int[] { 5 }, 5, 0);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
