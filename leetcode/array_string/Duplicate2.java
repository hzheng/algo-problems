import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC219: https://leetcode.com/problems/contains-duplicate-ii/
//
// Given an array of integers and an integer k, find out whether there are two
// distinct indices i and j in the array such that nums[i] = nums[j] and the
// difference between i and j is at most k.
public class Duplicate2 {
    // Solution of Choice
    // Hash Table
    // beats 80.27%(9 ms for 20 tests)
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            Integer last = map.put(nums[i], i);
            if (last != null && i - last <= k) return true;
        }
        return false;
    }

    // Hash Table
    // beats 80.27%(9 ms for 20 tests)
    public boolean containsNearbyDuplicate2(int[] nums, int k) {
        Set<Integer> set = new HashSet<>();
        for(int i = 0; i < nums.length; i++) {
            if (i > k) {
                set.remove(nums[i - k - 1]);
            }
            if (!set.add(nums[i])) return true;
        }
        return false;
    }

    void test(boolean expected, int k, int ... nums) {
        assertEquals(expected, containsNearbyDuplicate(nums, k));
        assertEquals(expected, containsNearbyDuplicate2(nums, k));
    }

    @Test
    public void test1() {
        test(false, 1, 1);
        test(true, 1, 1, 1);
        test(false, 1, 1, 3, 1, 5);
        test(true, 2, 1, 3, 1, 5);
        test(false, 2, 1, 2, 3, 1, 5);
        test(true, 3, 1, 2, 3, 1, 5);
        test(false, 3, 1, 0, 2, 3, 1, 5);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Duplicate2");
    }
}
