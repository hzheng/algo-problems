import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC228: https://leetcode.com/problems/summary-ranges/
//
// Given a sorted integer array without duplicates, return the summary of its ranges.
public class SummaryRanges {
    // beats 3.93%(1 ms)
    public List<String> summaryRanges(int[] nums) {
        int n = nums.length;
        List<String> res = new ArrayList<>();
        if (n == 0) return res;

        int start = nums[0];
        for (int i = 1; i < n; i++) {
            int last = nums[i - 1];
            if (nums[i] > last + 1) {
                if (last > start) {
                    res.add(Integer.toString(start) + "->" + last);
                } else {
                    res.add(Integer.toString(start));
                }
                start = nums[i];
            }
        }
        if (nums[n - 1] > start) {
            res.add(Integer.toString(start) + "->" + nums[n - 1]);
        } else {
            res.add(Integer.toString(start));
        }
        return res;
    }

    // beats 3.93%(1 ms)
    public List<String> summaryRanges2(int[] nums) {
        int n = nums.length;
        List<String> res = new ArrayList<>();
        if (n == 0) return res;

        int start = nums[0];
        int cur = start;
        for (int i = 1; i < n; i++) {
            if (nums[i] > ++cur) {
                if (cur > start + 1) {
                    res.add(Integer.toString(start) + "->" + (cur - 1));
                } else {
                    res.add(Integer.toString(start));
                }
                cur = start = nums[i];
            }
        }
        if (cur > start) {
            res.add(Integer.toString(start) + "->" + cur);
        } else {
            res.add(Integer.toString(start));
        }
        return res;
    }

    // beats 6.15%(1 ms for 27 tests)
    public List<String> summaryRanges3(int[] nums) {
        List<String> res = new ArrayList<>();
        for (int i = 0, n = nums.length; i < n; i++) {
            int start = nums[i];
            for (; i + 1 < n && (nums[i + 1] == nums[i] + 1); i++) {}
            res.add(start + ((start == nums[i]) ? "" : "->" + nums[i]));
        }
        return res;
    }

    void test(int[] nums, String ... expected) {
        assertArrayEquals(expected, summaryRanges(nums).toArray(new String[0]));
        assertArrayEquals(expected, summaryRanges2(nums).toArray(new String[0]));
        assertArrayEquals(expected, summaryRanges3(nums).toArray(new String[0]));
    }

    @Test
    public void test1() {
        test(new int[] {0}, "0");
        test(new int[] {1,3}, "1", "3");
        test(new int[] {0, 1, 2, 4, 5, 7}, "0->2", "4->5", "7");
        test(new int[] {0, 1, 2, 3, 4, 5, 7, 8, 10}, "0->5", "7->8", "10");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SummaryRanges");
    }
}
