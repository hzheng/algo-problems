import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC462: https://leetcode.com/problems/minimum-moves-to-equal-array-elements-ii/
//
// Given a non-empty integer array, find the minimum number of moves required to
// make all array elements equal, where a move is incrementing a selected
// element by 1 or decrementing a selected element by 1.
// You may assume the array's length is at most 10,000.
public class MinMoves2 {
    // Sort
    // time complexity: O(N * log(N)), space complexity: O(1)
    // beats N/A(9 ms for 29 tests)
    public int minMoves2(int[] nums) {
        Arrays.sort(nums);
        int mid = nums[nums.length >> 1];
        int moves = 0;
        for (int num : nums) {
            moves += Math.abs(num - mid);
        }
        return moves;
    }

    // Sort + Two Pointers
    // time complexity: O(N * log(N)), space complexity: O(1)
    // beats N/A(7 ms for 29 tests)
    public int minMoves2_2(int[] nums) {
        Arrays.sort(nums);
        int count = 0;
        for (int i = 0, j = nums.length - 1; i < j; i++, j--) {
            count += nums[j] - nums[i];
        }
        return count;
    }

    // TODO: quickSelect for O(N) time complexity

    void test(int[] nums, int expected) {
        assertEquals(expected, minMoves2(nums));
        assertEquals(expected, minMoves2_2(nums));
    }

    @Test
    public void test() {
        test(new int[] {1, 1, 2}, 1);
        test(new int[] {1, 2, 3}, 2);
        test(new int[] {1}, 0);
        test(new int[] {1, 2, 2, 3, 3, 3}, 4);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MinMoves2");
    }
}
