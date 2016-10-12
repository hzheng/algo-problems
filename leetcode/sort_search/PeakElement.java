import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/find-peak-element/
//
// A peak element is an element that is greater than its neighbors.
// The array may contain multiple peaks, in that case return the index to any
// one of the peaks is fine.
// You may imagine that num[-1] = num[n] = -∞.
// Your solution should be in logarithmic complexity.
public class PeakElement {
    // time complexity: O(N), space complexity: O(1)
    // beats 2.34%(1 ms for 58 tests)
    public int findPeakElement(int[] nums) {
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] < nums[i - 1]) return i - 1;
        }
        return nums.length - 1;
    }

    // Binary Search
    // time complexity: O(log(N)), space complexity: O(1)
    // beats 34.74%(0 ms for 58 tests)
    public int findPeakElement2(int[] nums) {
        int low = 0;
        int high = nums.length - 1;
        while (low < high) {
            int mid = (low + high) >>> 1;
            if (nums[mid] > nums[mid + 1]) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    void test(int expected, int ... nums) {
        assertEquals(expected, findPeakElement(nums));
        assertEquals(expected, findPeakElement2(nums));
    }

    @Test
    public void test1() {
        test(0, 3);
        test(2, 1, 2, 3, 1);
        test(4, 1, 2, 3, 4, 5, 4, 1);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PeakElement");
    }
}
