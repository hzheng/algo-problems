import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1674: https://leetcode.com/problems/minimum-moves-to-make-array-complementary/
//
// You are given an integer array nums of even length n and an integer limit. In one move, you can
// replace any integer from nums with another integer between 1 and limit, inclusive.
// The array nums is complementary if for all indices i (0-indexed), nums[i] + nums[n - 1 - i]
// equals the same number. Return the minimum number of moves required to make nums complementary.
//
// Constraints:
// n == nums.length
// 2 <= n <= 10^5
// 1 <= nums[i] <= limit <= 10^5
// n is even.
public class MinMoveToMakeComplementary {
    // Difference Array
    // time complexity: O(N+L), space complexity: O(L)
    // 5 ms(100.00%), 48.7 MB(92.31%) for 110 tests
    public int minMoves(int[] nums, int limit) {
        int[] count = new int[2 * limit + 2];
        int n = nums.length;
        for (int i = n / 2 - 1; i >= 0; i--) {
            int a = nums[i];
            int b = nums[n - 1 - i];
            count[Math.min(a, b) + 1]--; //  count 1
            count[a + b]--; // count 0
            count[a + b + 1]++; // count 1
            count[Math.max(a, b) + limit + 1]++; // count 2
        }
        int res = n;
        for (int i = 1, cur = n; i < count.length; i++) {
            cur += count[i];
            res = Math.min(cur, res);
        }
        return res;
    }

    // SortedMap
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 236 ms(12.64%), 56.4 MB(42.31%) for 110 tests
    public int minMoves2(int[] nums, int limit) {
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();
        SortedMap<Integer, Integer> count = new TreeMap<>();
        int n = nums.length;
        for (int i = n / 2 - 1; i >= 0; i--) {
            int min = Math.min(nums[i], nums[n - 1 - i]);
            int max = Math.max(nums[i], nums[n - 1 - i]);
            left.add(min);
            right.add(max);
            int sum = min + max;
            count.put(sum, count.getOrDefault(sum, 0) + 1);
        }
        Collections.sort(left);
        Collections.sort(right);
        int res = 0;
        int decreaseEnds = 0;
        int increaseEnds = 0;
        for (int i : count.keySet()) {
            for (; decreaseEnds < n / 2 && left.get(decreaseEnds) < i; decreaseEnds++) {}
            for (; increaseEnds < n / 2 && right.get(increaseEnds) + limit < i; increaseEnds++) {}
            res = Math.max(res, decreaseEnds - increaseEnds + count.get(i));
        }
        return n - res;
    }

    private void test(int[] nums, int limit, int expected) {
        assertEquals(expected, minMoves(nums, limit));
        assertEquals(expected, minMoves2(nums, limit));
    }

    @Test public void test() {
        test(new int[] {1, 2, 4, 3}, 4, 1);
        test(new int[] {1, 2, 2, 1}, 2, 2);
        test(new int[] {1, 2, 1, 2}, 2, 0);
        test(new int[] {37, 2, 9, 49, 58, 57, 48, 17}, 58, 3);
        test(new int[] {87, 9, 72, 83, 17, 69, 6, 82, 19, 32, 46, 2, 54, 18}, 88, 5);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
