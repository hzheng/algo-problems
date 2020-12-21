import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1695: https://leetcode.com/problems/maximum-erasure-value/
//
// You are given an array of positive integers nums and want to erase a subarray containing unique
// elements. The score you get by erasing the subarray is equal to the sum of its elements.
// Return the maximum score you can get by erasing exactly one subarray.
// An array b is called to be a subarray of a if it forms a contiguous subsequence of a, that is, if
// it is equal to a[l],a[l+1],...,a[r] for some (l,r).
//
// Constraints:
// 1 <= nums.length <= 10^5
// 1 <= nums[i] <= 10^4
public class MaximumErasureValue {
    // Sliding Window + Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 30 ms(86.14%), 49.7 MB(96.83%) for 62 tests
    public int maximumUniqueSubarray(int[] nums) {
        int n = nums.length;
        int res = 0;
        Map<Integer, Integer> map = new HashMap<>();
        int[] sum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            sum[i + 1] = sum[i] + nums[i];
        }
        for (int start = -1, end = 0; end < n; end++) {
            Integer old = map.put(nums[end], end);
            if (old != null) {
                for (int j = start + 1; j < old; j++) {
                    map.remove(nums[j]);
                }
                start = old;
            }
            res = Math.max(res, sum[end + 1] - sum[start + 1]);
        }
        return res;
    }

    // Solution of Choice
    // Sliding Window + Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 28 ms(86.38%), 56.9 MB(64.92%) for 62 tests
    public int maximumUniqueSubarray2(int[] nums) {
        int n = nums.length;
        int res = 0;
        Map<Integer, Integer> map = new HashMap<>();
        int[] sum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            sum[i + 1] = sum[i] + nums[i];
        }
        for (int start = -1, end = 0; end < n; end++) {
            Integer old = map.put(nums[end], end);
            if (old != null && start < old) {
                start = old;
            }
            res = Math.max(res, sum[end + 1] - sum[start + 1]);
        }
        return res;
    }

    // Sliding Window + Set
    // time complexity: O(N), space complexity: O(MAX)
    // 11 ms(90.82%), 48.2 MB(99.37%) for 62 tests
    public int maximumUniqueSubarray3(int[] nums) {
        final int max = Arrays.stream(nums).max().getAsInt();
        boolean[] appear = new boolean[max + 1];
        int n = nums.length;
        int res = 0;
        for (int start = 0, end = 0, sum = 0; end < n; end++) {
            for (; appear[nums[end]]; start++) {
                sum -= nums[start];
                appear[nums[start]] = false;
            }
            appear[nums[end]] = true;
            res = Math.max(res, sum += nums[end]);
        }
        return res;
    }

    // Solution of Choice
    // Sliding Window + Set
    // time complexity: O(N), space complexity: O(MAX)
    // 51 ms(33.33%), 51.9 MB(66.67%) for 62 tests
    public int maximumUniqueSubarray4(int[] nums) {
        Set<Integer> set = new HashSet<>();
        int n = nums.length;
        int res = 0;
        for (int start = 0, end = 0, sum = 0; end < n; ) {
            if (set.contains(nums[end])) {
                sum -= nums[start];
                set.remove(nums[start++]);
            } else {
                res = Math.max(res, sum += nums[end]);
                set.add(nums[end++]);
            }
        }
        return res;
    }

    private void test(int[] nums, int expected) {
        assertEquals(expected, maximumUniqueSubarray(nums));
        assertEquals(expected, maximumUniqueSubarray2(nums));
        assertEquals(expected, maximumUniqueSubarray3(nums));
        assertEquals(expected, maximumUniqueSubarray4(nums));
    }

    @Test public void test() {
        test(new int[] {4, 2, 4, 5, 6}, 17);
        test(new int[] {5, 2, 1, 2, 5, 2, 1, 2, 5}, 8);
        test(new int[] {187, 470, 25, 436, 538, 809, 441, 167, 477, 110, 275, 133, 666, 345, 411,
                        459, 490, 266, 987, 965, 429, 166, 809, 340, 467, 318, 125, 165, 809, 610,
                        31, 585, 970, 306, 42, 189, 169, 743, 78, 810, 70, 382, 367, 490, 787, 670,
                        476, 278, 775, 673, 299, 19, 893, 817, 971, 458, 409, 886, 434}, 16911);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
