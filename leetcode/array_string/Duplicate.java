import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC217: https://leetcode.com/problems/contains-duplicate/
//
// Given an array of integers, find if the array contains any duplicates. Your
// function should return true if any value appears at least twice in the array,
// and it should return false if every element is distinct.
public class Duplicate {
    // Solution of Choice
    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // beats 80.34%(8 ms for 17 tests)
    public boolean containsDuplicate(int[] nums) {
        Set<Integer> set = new HashSet<>(nums.length);
        for (int num : nums) {
            if (!set.add(num)) return true;
        }
        return false;
    }

    // Sort (con: modified input)
    // time complexity: O(N * long(N)), space complexity: O(1)
    // beats 80.04%(6 ms)
    public boolean containsDuplicate2(int[] nums) {
        Arrays.sort(nums);
        for (int i = nums.length - 1; i > 0; i--) {
            if (nums[i] == nums[i - 1]) return true;
        }
        return false;
    }

    void test(boolean expected, int... nums) {
        assertEquals(expected, containsDuplicate(nums));
        assertEquals(expected, containsDuplicate2(nums.clone()));
    }

    @Test
    public void test1() {
        test(false, 1);
        test(true, 1, 1);
        test(false, 1, 2, 3, 4, 5);
        test(true, 1, 2, 3, 4, 5, 1);
        test(false, 1, 2, 3, 4, 5, 0);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Duplicate");
    }
}
