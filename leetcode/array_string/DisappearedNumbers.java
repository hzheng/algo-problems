import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC448: https://leetcode.com/problems/find-all-numbers-disappeared-in-an-array/
//
// Given an array of integers where 1 ≤ a[i] ≤ n (n = size of array), some
// elements appear twice and others appear once.
// Find all the elements of [1, n] inclusive that do not appear in this array.
// Could you do it without extra space and in O(n) runtime? You may assume the
// returned list does not count as extra space.
public class DisappearedNumbers {
    // beats 0.22%(16 ms for 34 tests)
    public List<Integer> findDisappearedNumbers(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            if (num != nums[num - 1]) {
                swap(nums, i--, num - 1);
            }
        }
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != i + 1) {
                res.add(i + 1);
            }
        }
        return res;
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    // beats 0.22%(17 ms for 34 tests)
    public List<Integer> findDisappearedNumbers2(int[] nums) {
        for (int num : nums) {
            int index = Math.abs(num) - 1;
            if (nums[index] > 0) {
                nums[index] = -nums[index];
            }
        }
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > 0) {
                res.add(i + 1);
            }
        }
        return res;
    }

    // beats 0.22%(16 ms for 34 tests)
    public List<Integer> findDisappearedNumbers3(int[] nums) {
        int n = nums.length;
        for (int num : nums) {
            int index = (num - 1 + n) % n;
            if (nums[index] > 0) {
                nums[index] -= n;
            }
        }
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (nums[i] > 0) {
                res.add(i + 1);
            }
        }
        return res;
    }

    void test(int[] nums, Integer... expected) {
        assertArrayEquals(expected, findDisappearedNumbers(nums.clone()).toArray(new Integer[0]));
        assertArrayEquals(expected, findDisappearedNumbers2(nums.clone()).toArray(new Integer[0]));
        assertArrayEquals(expected, findDisappearedNumbers3(nums.clone()).toArray(new Integer[0]));
    }

    @Test
    public void test1() {
        test(new int[] {4, 3, 2, 7, 8, 2, 3, 1}, 5, 6);
        test(new int[] {4, 3, 2, 7, 7, 2, 3, 1}, 5, 6, 8);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("DisappearedNumbers");
    }
}
