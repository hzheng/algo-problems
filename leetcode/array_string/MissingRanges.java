import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC163: https://leetcode.com/problems/missing-ranges/
//
// Given a sorted integer array where the range of elements are in the inclusive
// range [lower, upper], return its missing ranges.
public class MissingRanges {
    // beats 20.63%(1 ms for 40 tests)
    public List<String> findMissingRanges(int[] nums, int lower, int upper) {
        List<String> res = new ArrayList<>();
        long next = lower;
        for (int i = 0, n = nums.length; i <= n; i++) {
            long num = (i < n) ? nums[i] : (long)upper + 1;
            if (next + 1 <= num) {
                res.add(next + ((num == next + 1) ? "" : "->" + (num - 1)));
            }
            next = num + 1;
        }
        return res;
    }

    // beats 20.63%(1 ms for 40 tests)
    public List<String> findMissingRanges2(int[] nums, int lower, int upper) {
        List<String> res = new ArrayList<String>();
        for (int i = 0, n = nums.length; i <= n; i++) {
            long low = (i == 0) ? lower : (long)nums[i - 1] + 1;
            long high = (i == n) ? upper : (long)nums[i] - 1;
            if (low == high) {
                res.add(String.valueOf(low));
            } else if (low < high) {
                res.add(low + "->" + high);
            }
        }
        return res;
    }

    void test(int[] nums, int lower, int upper, String[] expected) {
        assertArrayEquals(expected, findMissingRanges(nums, lower, upper).toArray(new String[0]));
        assertArrayEquals(expected, findMissingRanges2(nums, lower, upper).toArray(new String[0]));
    }

    @Test
    public void test() {
        test(new int[] {}, 1, 1, new String[] {"1"});
        test(new int[] {}, 1, 2, new String[] {"1->2"});
        test(new int[] {1, 1, 1}, 1, 1, new String[] {});
        test(new int[] {0, 1, 3, 50, 75}, 0, 99,
             new String[] {"2", "4->49", "51->74", "76->99"});
        test(new int[] {0, 1, 3, 50, 75}, 0, 76,
             new String[] {"2", "4->49", "51->74", "76"});
    }

    @Test
    public void test2() {
        test(new int[] {2147483647}, 0, 2147483647, new String[] {"0->2147483646"});
        test(new int[] {}, -2147483648, 2147483647, new String[] {"-2147483648->2147483647"});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MissingRanges");
    }
}
