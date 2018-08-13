import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC457: https://leetcode.com/problems/circular-array-loop/
//
// You are given an array of positive and negative integers. If a number n at an
// index is positive, then move forward n steps. Conversely, move backward -n
// steps. Assume the first element of the array is forward next to the last
// element, and the last element is backward next to the first element.
// Determine if there is a loop in this array. A loop starts and ends at a
// particular index with more than 1 element along the loop. The loop must be
// "forward" or "backward'.
// Note: The given array is guaranteed to contain no element "0".
// Can you do it in O(n) time complexity and O(1) space complexity?
public class CircularArrayLoop {
    // Slow/Fast Pointer
    // beats 100.00%(0 ms for 10 tests)
    public boolean circularArrayLoop(int[] nums) {
        outer : for (int i = nums.length - 1; i >= 0; i--) {
            for (int slow = i, fast = next(i, nums), dir = nums[i]; 
                 nums[fast] * dir > 0 && nums[next(fast, nums)] * dir > 0; ) {
                if (slow == fast) {
                    if (slow == next(slow, nums)) continue outer; // 1 element
                    return true;
                }
                slow = next(slow, nums);
                fast = next(next(fast, nums), nums);
            }
        }
        return false;
    }

    private int next(int x, int[] nums) {
        int n = nums.length;
        int res = (x + nums[x]) % n;
        return res >= 0 ? res : res + n;
    }

    void test(int[] nums, boolean expected) {
        assertEquals(expected, circularArrayLoop(nums));
    }

    @Test
    public void test() {
        test(new int[] { 2, -1, 1, 2, 2 }, true);
        test(new int[] { -1, 2 }, false);
        test(new int[] { -2, 1, -1, -2, -2 }, false);
        test(new int[] { 2, 0, 2, 1, 3 }, true);
        test(new int[] { 3, 1, 2}, true);
        test(new int[] { -1, -2, -3, -4, -5 }, false);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
