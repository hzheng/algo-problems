import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC697: https://leetcode.com/problems/degree-of-an-array/
//
// Given a non-empty array of non-negative integers nums, the degree of this 
// array is defined as the maximum frequency of any one of its elements.
// Your task is to find the smallest possible length of a (contiguous) subarray 
// of nums, that has the same degree as nums.
public class DegreeOfArray {
    // time complexity: O(N), space complexity: O(N)
    // beats 96.08%(27 ms for 89 tests)
    public int findShortestSubArray(int[] nums) {
        Map<Integer, int[]> map = new HashMap<>();
        int degree = 0;
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            int[] count = map.get(num);
            if (count == null) {
                count = new int[]{0, i, i};
                map.put(num, count);
            } else {
                count[2] = i;
            }
            degree = Math.max(degree, ++count[0]);
        }
        int res = nums.length;
        for (int[] values : map.values()) {
            if (values[0] == degree) {
                res = Math.min(res, values[2] - values[1] + 1);
            }
        }
        return res;
    }

    void test(int[] nums, int expected) {
        assertEquals(expected, findShortestSubArray(nums));
    }

    @Test
    public void test() {
        test(new int[] {1, 2, 2, 3, 1}, 2);
        test(new int[] {1, 2, 2, 3, 1, 4, 2}, 6);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
