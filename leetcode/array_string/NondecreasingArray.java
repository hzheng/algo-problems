import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC665: https://leetcode.com/problems/non-decreasing-array/
//
// Check if an array could become non-decreasing by modifying at most 1 element.
public class NondecreasingArray {
    // time complexity: O(N), space complexity: O(1)
    // beats 11.03%(17 ms for 325 tests)
    public boolean checkPossibility(int[] nums) {
        boolean modified = false;
        for (int i = 1, n = nums.length; i < n; i++) {
            if (nums[i] >= nums[i - 1]) continue;

            if (modified || i > 1 && i < n - 1 && nums[i] < nums[i - 2]
                            && nums[i + 1] < nums[i - 1]) {
                return false;
            }
            modified = true;
        }
        return true;
    }

    void test(int[] nums, boolean expected) {
        assertEquals(expected, checkPossibility(nums));
    }

    @Test
    public void test() {
        test(new int[] { 4, 2, 3 }, true);
        test(new int[] { 4, 2, 1 }, false);
        test(new int[] { -1, 4, 2, 3 }, true);
        test(new int[] { 3, 4, 2, 5 }, true);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
