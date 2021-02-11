import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1748: https://leetcode.com/problems/sum-of-unique-elements/
//
// You are given an integer array nums. The unique elements of an array are the elements that appear
// exactly once in the array.
// Return the sum of all the unique elements of nums.
//
// Constraints:
// 1 <= nums.length <= 100
// 1 <= nums[i] <= 100
public class SumOfUnique {
    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 1 ms(78.60%), 36.9 MB(58.58%) for 73 tests
    public int sumOfUnique(int[] nums) {
        Map<Integer, Integer> count = new HashMap<>();
        for (int num : nums) {
            count.put(num, count.getOrDefault(num, 0) + 1);
        }
        int res = 0;
        for (int num : count.keySet()) {
            if (count.get(num) == 1) {
                res += num;
            }
        }
        return res;
    }

    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 36.6 MB(79.72%) for 73 tests
    public int sumOfUnique2(int[] nums) {
        int[] count = new int[101];
        for (int num : nums) {
            count[num]++;
        }
        int res = 0;
        for (int i = count.length - 1; i > 0; i--) {
            if (count[i] == 1) {
                res += i;
            }
        }
        return res;
    }

    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 36.3 MB(92.09%) for 73 tests
    public int sumOfUnique3(int[] nums) {
        int[] count = new int[101];
        int res = 0;
        for (int num : nums) {
            int cnt = ++count[num];
            res += (cnt == 1) ? num : (cnt == 2 ? -num : 0);
        }
        return res;
    }

    private void test(int[] nums, int expected) {
        assertEquals(expected, sumOfUnique(nums));
        assertEquals(expected, sumOfUnique2(nums));
        assertEquals(expected, sumOfUnique3(nums));
    }

    @Test public void test() {
        test(new int[] {1, 2, 3, 2}, 4);
        test(new int[] {1, 1, 1, 1, 1}, 0);
        test(new int[] {1, 2, 3, 4, 5}, 15);
        test(new int[] {5, 1, 2, 3, 7, 3, 3, 7, 4, 7, 5, 1}, 6);
        test(new int[] {2, 4, 5, 1, 2, 3, 7, 3, 3, 7, 4, 7, 5, 1}, 0);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
