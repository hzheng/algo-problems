import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.*;

// LC1785: https://leetcode.com/problems/minimum-elements-to-add-to-form-a-given-sum/
//
// You are given an integer array nums and two integers limit and goal. The array nums has an
// interesting property that abs(nums[i]) <= limit.
// Return the minimum number of elements you need to add to make the sum of the array equal to goal.
// The array must maintain its property that abs(nums[i]) <= limit.
//
// Constraints:
// 1 <= nums.length <= 10^5
// 1 <= limit <= 10^6
// -limit <= nums[i] <= limit
// -10^9 <= goal <= 10^9
public class MinElements {
    // Greedy
    // time complexity: O(N), space complexity: O(1)
    // 1 ms(100.00%), 52.7 MB(100.00%) for 77 tests
    public int minElements(int[] nums, int limit, int goal) {
        long sum = 0;
        for (int a : nums) {
            sum += a;
        }
        return (int)((Math.abs(goal - sum) + limit - 1) / limit);
    }

    // Greedy
    // time complexity: O(N), space complexity: O(1)
    // 7 ms(100.00%), 53.2 MB(100.00%) for 77 tests
    public int minElements2(int[] nums, int limit, int goal) {
        long diff = goal - IntStream.of(nums).mapToLong(i -> i).sum();
        return (int)((Math.abs(diff) + limit - 1) / limit);
    }

    private void test(int[] nums, int limit, int goal, int expected) {
        assertEquals(expected, minElements(nums, limit, goal));
        assertEquals(expected, minElements2(nums, limit, goal));
    }

    @Test public void test() {
        test(new int[] {1, -1, 1}, 3, -4, 2);
        test(new int[] {1, -10, 9, 1}, 100, 0, 1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
