import org.junit.Test;

import static org.junit.Assert.*;

// LC1829: https://leetcode.com/problems/maximum-xor-for-each-query/
//
// You are given a sorted array nums of n non-negative integers and an integer maximumBit. You want
// to perform the following query n times:
// Find a non-negative integer k < 2^maximumBit such that nums[0] XOR nums[1] XOR ... XOR
// nums[nums.length-1] XOR k is maximized. k is the answer to the ith query.
// Remove the last element from the current array nums.
// Return an array answer, where answer[i] is the answer to the ith query.
//
// Constraints:
// nums.length == n
// 1 <= n <= 10^5
// 1 <= maximumBit <= 20
// 0 <= nums[i] < 2^maximumBit
// nums is sorted in ascending order.
public class MaximumXOR {
    // time complexity: O(N), space complexity: O(N)
    // 26 ms(25.55%), 55.9 MB(81.54%) for 86 tests
    public int[] getMaximumXor(int[] nums, int maximumBit) {
        int n = nums.length;
        int xor = 0;
        for (int num : nums) {
            xor ^= num;
        }
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            int ans = 0;
            for (int b = 0, mask = 1; b < maximumBit; b++, mask <<= 1) {
                if ((xor & mask) == 0) {
                    ans |= mask;
                }
            }
            res[i] = ans;
            xor ^= nums[n - i - 1];
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(N)
    // 2 ms(99.83%), 56.5 MB(58.01%) for 86 tests
    public int[] getMaximumXor2(int[] nums, int maximumBit) {
        int n = nums.length;
        int maxXor = (1 << maximumBit) - 1;
        int[] res = new int[n];
        for (int i = 0, xor = 0; i < n; i++) {
            xor ^= nums[i];
            res[n - i - 1] = maxXor ^ xor;
        }
        return res;
    }

    private void test(int[] nums, int maximumBit, int[] expected) {
        assertArrayEquals(expected, getMaximumXor(nums, maximumBit));
        assertArrayEquals(expected, getMaximumXor2(nums, maximumBit));
    }

    @Test public void test() {
        test(new int[] {0, 1, 1, 3}, 2, new int[] {0, 3, 2, 3});
        test(new int[] {2, 3, 4, 7}, 3, new int[] {5, 2, 6, 5});
        test(new int[] {0, 1, 2, 2, 5, 7}, 3, new int[] {4, 3, 6, 4, 6, 7});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
