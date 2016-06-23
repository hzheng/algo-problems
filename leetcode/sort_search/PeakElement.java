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
    // beats 2.08%
    public int findPeakElement(int[] nums) {
        int n = nums.length;
        if (n < 2) return n - 1;

        if (nums[0] > nums[1]) return 0;

        if (nums[n - 1] > nums[n - 2]) return n - 1;

        for (int i = 1; i < n - 1; i++) {
            if (nums[i] > nums[i - 1] && nums[i] > nums[i + 1]) return i;
        }
        return -1;
    }

    // beats 33.78%
    public int findPeakElement2(int[] nums) {
        int n = nums.length;
        if (n < 2) return n - 1;

        int low = 0;
        int high = n - 1;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (mid > 0 && nums[mid - 1] > nums[mid]) {
                high = mid - 1;
                continue;
            }

            if (mid + 1 < n && nums[mid + 1] < nums[mid]) return mid;

            low = mid + 1;
        }
        return low;
    }

    void test(int expected, int ... nums) {
        assertEquals(expected, findPeakElement(nums));
        assertEquals(expected, findPeakElement2(nums));
    }

    @Test
    public void test1() {
        test(2, 1, 2, 3, 1);
        test(4, 1, 2, 3, 4, 5, 4, 1);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PeakElement");
    }
}
