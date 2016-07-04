import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/contains-duplicate-iii/
//
// Given an array of integers, find out whether there are two distinct indices
// i and j in the array such that the difference between nums[i] and nums[j]
// is at most t and the difference between i and j is at most k.
public class Duplicate3 {
    // beats 32.20%(51 ms)
    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
        if (t < 0) return false;

        SortedMap<Long, Integer> map = new TreeMap<>();
        for (int i = 0; i < nums.length; i++) {
            long num = nums[i];
            for (long almostDup : map.subMap(num - t, num + t + 1).keySet()) {
                if (i - map.get(almostDup) <= k) return true;
            }
            map.put(num, i);
            // if (i >= k) {  // unnecessary, beat rate will drop to 23.42%
            //     map.remove((long)nums[i - k]);
            // }
        }
        return false;
    }

    // beats 41.36%(48 ms)
    public boolean containsNearbyAlmostDuplicate2(int[] nums, int k, int t) {
        if (t < 0) return false;

        NavigableSet<Long> set = new TreeSet<>();
        for (int i = 0; i < nums.length; i++) {
            long num = nums[i];
            Long floor = set.floor(num);
            if (floor != null && num - floor <= t) return true;

            Long ceiling = set.ceiling(num);
            if (ceiling != null && ceiling - num <= t) return true;

            set.add(num);
            if (i >= k) { // necessary for set
                set.remove((long)nums[i - k]);
            }
        }
        return false;
    }

    // beats 44.85%(47 ms)
    public boolean containsNearbyAlmostDuplicate3(int[] nums, int k, int t) {
        if (t < 0) return false;

        SortedSet<Long> set = new TreeSet<>();
        for (int i = 0; i < nums.length; i++) {
            long num = nums[i];
            if (!set.subSet(num - t, num + t + 1).isEmpty()) return true;

            set.add(num);
            if (i >= k) {
                set.remove((long)nums[i - k]);
            }
        }
        return false;
    }

    void test(boolean expected, int k, int t, int... nums) {
        assertEquals(expected, containsNearbyAlmostDuplicate(nums, k, t));
        assertEquals(expected, containsNearbyAlmostDuplicate2(nums, k, t));
        assertEquals(expected, containsNearbyAlmostDuplicate3(nums, k, t));
    }

    @Test
    public void test1() {
        test(false, 1, -1, -1, -1);
        test(true, 1, 2147483647, 0, 2147483647);
        test(false, 1, 0, 1);
        test(true, 1, 0, 1, 1);
        test(false, 1, 0, 1, 3, 1, 5);
        test(true, 2, 0, 1, 3, 1, 5);
        test(false, 2, 0, 1, 2, 3, 1, 5);
        test(true, 3, 0, 1, 2, 3, 1, 5);
        test(false, 3, 0, 1, 0, 2, 3, 1, 5);
        test(true, 3, 1, 1, 0, 2, 3, 1, 5);
        test(false, 3, 1, 2, 10, 18, 6, 12, 9, 4, 15);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Duplicate3");
    }
}
