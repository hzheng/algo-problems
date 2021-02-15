import org.junit.Test;

import static org.junit.Assert.*;

// LC1144: https://leetcode.com/problems/decrease-elements-to-make-array-zigzag/
//
// Given an array nums of integers, a move consists of choosing any element and decreasing it by 1.
// An array A is a zigzag array if either:
// Every even-indexed element is greater than adjacent elements,
// ie. A[0] > A[1] < A[2] > A[3] < A[4] > ...
// OR, every odd-indexed element is greater than adjacent elements,
// ie. A[0] < A[1] > A[2] < A[3] > A[4] < ...
// Return the minimum number of moves to transform the given array nums into a zigzag array.
//
// Constraints:
// 1 <= nums.length <= 1000
// 1 <= nums[i] <= 1000
public class MovesToMakeZigzag {
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 36.6 MB(47.11%) for 31 tests
    public int movesToMakeZigzag(int[] nums) {
        return Math.min(moves(nums, 0), moves(nums, 1));
    }

    private int moves(int[] nums, int even) {
        int n = nums.length;
        int moves = 0;
        for (int i = 1, prev = nums[0]; i < n; i++) {
            int diff = Math.abs(prev - nums[i]) + 1;
            if ((i % 2 != even) && (nums[i] >= prev)) {
                moves += diff;
                prev = nums[i] - diff;
            } else {
                if ((i % 2 == even) && (nums[i] <= prev)) {
                    moves += diff;
                }
                prev = nums[i];
            }
        }
        return moves;
    }

    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 36.6 MB(47.11%) for 31 tests
    public int movesToMakeZigzag2(int[] nums) {
        int[] res = new int[2];
        for (int i = 0, n = nums.length; i < n; i++) {
            int left = i > 0 ? nums[i - 1] : Integer.MAX_VALUE;
            int right = i + 1 < n ? nums[i + 1] : Integer.MAX_VALUE;
            res[i % 2] += Math.max(0, nums[i] - Math.min(left, right) + 1);
        }
        return Math.min(res[0], res[1]);
    }

    private void test(int[] nums, int expected) {
        assertEquals(expected, movesToMakeZigzag(nums));
        assertEquals(expected, movesToMakeZigzag2(nums));
    }

    @Test public void test() {
        test(new int[] {1, 2, 3}, 2);
        test(new int[] {9, 6, 1, 6, 2}, 4);
        test(new int[] {7, 4, 8, 9, 7, 7, 5}, 6);
        test(new int[] {2, 7, 10, 9, 8, 9}, 4);
        test(new int[] {10, 4, 4, 10, 10, 6, 2, 3}, 13);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
