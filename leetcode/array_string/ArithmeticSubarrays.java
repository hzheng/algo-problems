import java.util.*;

import common.Utils;
import org.junit.Test;

import static org.junit.Assert.*;

// LC1630: https://leetcode.com/problems/arithmetic-subarrays/
//
// A sequence of numbers is called arithmetic if it consists of at least two elements, and the
// difference between every two consecutive elements is the same. More formally, a sequence s is
// arithmetic if and only if s[i+1] - s[i] == s[1] - s[0] for all valid i.
// You are given an array of n integers, nums, and two arrays of m integers each, l and r,
// representing the m range queries, where the ith query is the range [l[i], r[i]]. All the arrays
// are 0-indexed. Return a list of boolean elements answer, where answer[i] is true if the subarray
// nums[l[i]], nums[l[i]+1], ... , nums[r[i]] can be rearranged to form an arithmetic sequence, and
// false otherwise.
// Constraints:
// n == nums.length
// m == l.length
// m == r.length
// 2 <= n <= 500
// 1 <= m <= 500
// 0 <= l[i] < r[i] < n
// -10^5 <= nums[i] <= 10^5
public class ArithmeticSubarrays {
    // Sort
    // time complexity: O(N*log(N)*M), space complexity: O(N)
    // 14 ms(100%), 39.3 MB(16.67%) for 101 tests
    public List<Boolean> checkArithmeticSubarrays(int[] nums, int[] l, int[] r) {
        List<Boolean> res = new ArrayList<>();
        for (int i = 0; i < l.length; i++) {
            res.add(isArithmetic(nums, l[i], r[i]));
        }
        return res;
    }

    private boolean isArithmetic(int[] nums, int l, int r) {
        int[] num = Arrays.copyOfRange(nums, l, r + 1);
        Arrays.sort(num);
        for (int d = num[1] - num[0], i = 2; i < num.length; i++) {
            if (num[i] - num[i - 1] != d) { return false; }
        }
        return true;
    }

    // time complexity: O(N*M), space complexity: O(N)
    // 5 ms(100%), 39.3 MB(16.67%) for 101 tests
    public List<Boolean> checkArithmeticSubarrays2(int[] nums, int[] l, int[] r) {
        List<Boolean> res = new ArrayList<>();
        for (int i = 0; i < l.length; i++) {
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;
            for (int j = l[i]; j <= r[i]; j++) {
                min = Math.min(min, nums[j]);
                max = Math.max(max, nums[j]);
            }
            int m = r[i] - l[i] + 1;
            if (max == min) {
                res.add(true);
            } else if ((max - min) % (m - 1) != 0) {
                res.add(false);
            } else {
                Boolean[] diffs = new Boolean[m];
                int d = (max - min) / (m - 1);
                for (int j = l[i]; j <= r[i]; j++) {
                    if ((nums[j] - min) % d != 0) { break; }

                    diffs[(nums[j] - min) / d] = true;
                }
                res.add(!Arrays.asList(diffs).contains(null));
            }
        }
        return res;
    }

    private void test(int[] nums, int[] l, int[] r, Boolean[] expected) {
        List<Boolean> expectedList = Arrays.asList(expected);
        assertEquals(expectedList, checkArithmeticSubarrays(nums, l, r));
        assertEquals(expectedList, checkArithmeticSubarrays2(nums, l, r));
    }

    @Test public void test() {
        test(new int[] {4, 6, 5, 9, 3, 7}, new int[] {0, 0, 2}, new int[] {2, 3, 5},
             new Boolean[] {true, false, true});
        test(new int[] {-12, -9, -3, -12, -6, 15, 20, -25, -20, -15, -10},
             new int[] {0, 1, 6, 4, 8, 7}, new int[] {4, 4, 9, 7, 9, 10},
             new Boolean[] {false, true, false, false, true, true});
        test(new int[] {-3, -6, -8, -4, -2, -8, -6, 0, 0, 0, 0},
             new int[] {5, 4, 3, 2, 4, 7, 6, 1, 7}, new int[] {6, 5, 6, 3, 7, 10, 7, 4, 10},
             new Boolean[] {true, true, true, true, false, true, true, true, true});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
