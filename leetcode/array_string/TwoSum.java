import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC001: https://leetcode.com/problems/two-sum/
//
// Given an array of integers, return indices of the two numbers such that they
// add up to a specific target.
// You may assume that each input would have exactly one solution.
public class TwoSum {
    // beats 58.79%
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> complements = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int cur = nums[i];
            if (complements.containsKey(cur)) {
                return new int[] { complements.get(cur), i };
            }
            complements.put(target - cur, i);
        }
        return null;
    }

    // Solution of Choice
    // https://leetcode.com/articles/two-sum/
    public int[] twoSum2(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[] { map.get(complement), i };
            }
            map.put(nums[i], i);
        }
        throw new IllegalArgumentException("No two sum solution");
    }

    void test(int[] nums, int target, int ... expected) {
        assertArrayEquals(expected, twoSum(nums, target));
        assertArrayEquals(expected, twoSum2(nums, target));
    }

    @Test
    public void test1() {
        test(new int[] {2, 7, 11, 15}, 9, 0, 1);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("TwoSum");
    }
}
