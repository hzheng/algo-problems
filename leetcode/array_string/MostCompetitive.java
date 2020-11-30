import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1673: https://leetcode.com/problems/find-the-most-competitive-subsequence/
//
// Given an integer array nums and a positive integer k, return the most competitive subsequence of
// nums of size k. An array's subsequence is a resulting sequence obtained by erasing some (possibly
// zero) elements from the array. We define that a subsequence a is more competitive than a
// subsequence b (of the same length) if in the first position where a and b differ, subsequence a
// has a number less than the corresponding number in b. For example, [1,3,4] is more competitive
// than [1,3,5] because the first position they differ is at the final number, and 4 is less than 5.
//
// Constraints:
// 1 <= nums.length <= 10^5
// 0 <= nums[i] <= 10^9
// 1 <= k <= nums.length
public class MostCompetitive {
    // Deque (monoqueue)
    // time complexity: O(N), space complexity: O(N)
    // 19 ms(72.73%), 57.3 MB(36.36%) for 85 tests
    public int[] mostCompetitive(int[] nums, int k) {
        int toRemove = nums.length - k;
        Deque<Integer> deque = new LinkedList<>();
        for (int num : nums) {
            for (; toRemove > 0 && !deque.isEmpty() && num < deque.peekLast(); toRemove--) {
                deque.pollLast();
            }
            deque.offerLast(num);
        }
        int[] res = new int[k];
        for (int i = 0; i < k; i++) {
            res[i] = deque.pollFirst();
        }
        return res;
    }

    // Stack (monoqueue)
    // time complexity: O(N), space complexity: O(N)
    // 33 ms(54.55%), 51.5 MB(81.82%) for 85 tests
    public int[] mostCompetitive2(int[] nums, int k) {
        Stack<Integer> stack = new Stack<>();
        for (int i = 0, n = nums.length ; i < n; i++) {
            while (!stack.isEmpty() && stack.peek() > nums[i] && stack.size() + n - i > k) {
                stack.pop();
            }
            if (stack.size() < k) {
                stack.push(nums[i]);
            }
        }
        return stack.stream().mapToInt(i -> i).toArray();
    }

    // Array/Stack (monoqueue)
    // time complexity: O(N), space complexity: O(N)
    // 6 ms(100.00%), 50.3 MB(100.00%) for 85 tests
    public int[] mostCompetitive3(int[] nums, int k) {
        int[] stack = new int[k];
        int n = nums.length;
        for (int i = 0, j = 0; i < n; i++) {
            for (; j > 0 && stack[j - 1] > nums[i] && j - 1 + n - i >= k; j--) {}
            if (j < k) {
                stack[j++] = nums[i];
            }
        }
        return stack;
    }

    // TODO: SegmentTree

    private void test(int[] nums, int k, int[] expected) {
        assertArrayEquals(expected, mostCompetitive(nums, k));
        assertArrayEquals(expected, mostCompetitive2(nums, k));
        assertArrayEquals(expected, mostCompetitive3(nums, k));
    }

    @Test public void test() {
        test(new int[] {3, 5, 2, 6}, 2, new int[] {2, 6});
        test(new int[] {2, 4, 3, 3, 5, 4, 9, 6}, 4, new int[] {2, 3, 3, 4});
        test(new int[] {71, 18, 52, 29, 55, 73, 24, 42, 66, 8, 80, 2}, 3, new int[] {8, 80, 2});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
