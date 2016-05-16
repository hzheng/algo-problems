import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// Given a sorted array and a target, return the index if the target is found.
// If not, return the index where it would be if it were inserted in order.
public class SearchInsertion {
    // beats 19.33%
    public int searchInsert(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (nums[mid] == target) return mid;
            if (nums[mid] > target) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    void test(int[] expected, int[] targets, int ... nums) {
        for (int i = 0; i < targets.length; i++) {
            assertEquals(expected[i], searchInsert(nums, targets[i]));
        }
    }

    @Test
    public void test1() {
        test(new int[] {2, 1, 4, 0}, new int[] {5, 2, 7, 0}, 1, 3, 5, 6);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SearchInsertion");
    }
}
