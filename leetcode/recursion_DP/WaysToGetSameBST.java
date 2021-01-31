import java.util.*;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

// LC1569: https://leetcode.com/problems/number-of-ways-to-reorder-array-to-get-same-bst/
//
// Given an array nums that represents a permutation of integers from 1 to n. We are going to
// construct a binary search tree (BST) by inserting the elements of nums in order into an initially
// empty BST. Find the number of different ways to reorder nums so that the constructed BST is
// identical to that formed from the original array nums. For example, given nums = [2,1,3], we will
// have 2 as the root, 1 as a left child, and 3 as a right child. The array [2,3,1] also yields the
// same BST but [3,2,1] yields a different BST.
// Return the number of ways to reorder nums such that the BST formed is identical to the original
// BST formed from nums.
// Since the answer may be very large, return it modulo 10^9 + 7.
//
// Constraints:
// 1 <= nums.length <= 1000
// 1 <= nums[i] <= nums.length
// All integers in nums are distinct.
public class WaysToGetSameBST {
    private static final int MOD = 1_000_000_007;

    // DFS + Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(), space complexity: O()
    // 543 ms(6.98%), 107 MB(8.14%) for 161 tests
    public int numOfWays(int[] nums) {
        return (int)dfs(nums, 0, 0, nums.length + 1) - 1;
    }

    private long dfs(int[] nums, int root, int min, int max) {
        int pivot = nums[root];
        int left = 0;
        int right = 0;
        int smaller = 0;
        int bigger = 0;
        for (int i = root + 1, n = nums.length; i < n; i++) {
            if (nums[i] <= min || nums[i] >= max) { continue; }
            if (nums[i] < pivot) {
                if (left == 0) {
                    left = i;
                }
                smaller++;
            } else {
                if (right == 0) {
                    right = i;
                }
                bigger++;
            }
        }
        long res;
        if (left == 0 && right == 0) {
            res = 1;
        } else if (left == 0) {
            res = dfs(nums, right, min, max);
        } else if (right == 0) {
            res = dfs(nums, left, min, max);
        } else {
            res = dfs(nums, left, min, pivot) * dfs(nums, right, pivot, max) % MOD * combinate(
                    bigger + smaller, smaller, combinations) % MOD;
        }
        return res;
    }

    private static final Map<Long, Long> combinations = new HashMap<>();

    private long combinate(int n, int m, Map<Long, Long> combinations) {
        if (m == 0 || n <= m) { return 1; }

        long key = (((long)n) << Integer.SIZE) | m;
        long res = combinations.getOrDefault(key, 0L);
        if (res > 0) { return res; }

        res = (combinate(n - 1, m - 1, combinations) + combinate(n - 1, m, combinations)) % MOD;
        combinations.put(key, res);
        return res;
    }

    // Recursion + Divide and Conquer + Dynamic Programming(Bottom-Up)
    // time complexity: O(N^2), space complexity: O(N^2)
    // 146 ms(50.00%), 63 MB(19.77%) for 161 tests
    public int numOfWays2(int[] nums) {
        long[][] C = new long[nums.length][nums.length];
        for (int n = 0; n < C.length; n++) {
            C[n][0] = 1;
            for (int r = Math.min(n, C[0].length - 1); r > 0; r--) {
                C[n][r] = (C[n - 1][r - 1] + C[n - 1][r]) % MOD;
            }
        }
        return (int)dfs(Arrays.stream(nums).boxed().collect(Collectors.toList()), C) - 1;
    }

    private long dfs(List<Integer> nums, long[][] C) {
        if (nums.size() < 3) { return 1; }

        int pivot = nums.get(0);
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();
        for (int num : nums) {
            if (num < pivot) {
                left.add(num);
            } else if (num > pivot) {
                right.add(num);
            }
        }
        return dfs(left, C) * dfs(right, C) % MOD * C[nums.size() - 1][left.size()] % MOD;
    }

    // Solution of Choice
    // Recursion + Divide and Conquer + Dynamic Programming(Bottom-Up)
    // time complexity: O(N^2), space complexity: O(N^2)
    // 90 ms(75.58%), 51.2 MB(62.79%) for 161 tests
    public int numOfWays3(int[] nums) {
        return (int)dfs(Arrays.stream(nums).boxed().collect(Collectors.toList())) - 1;
    }

    private long dfs(List<Integer> nums) {
        if (nums.size() < 3) { return 1; }

        int pivot = nums.get(0);
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();
        for (int num : nums) {
            if (num < pivot) {
                left.add(num);
            } else if (num > pivot) {
                right.add(num);
            }
        }
        return dfs(left) * dfs(right) % MOD * combinate(nums.size() - 1, left.size()) % MOD;
    }

    private long combinate(int n, int r) {
        long res = 1;
        for (int i = n; i >= n - r + 1; i--) {
            res = (res * i) % MOD;
        }
        for (int i = 1; i <= r; i++) {
            res = (res * inverse(i, MOD)) % MOD;
        }
        return res;
    }

    // https://en.wikipedia.org/wiki/Modular_multiplicative_inverse#Multiple_inverses
    private long inverse(long x, int mod) {
        long res = 1; // a^(-1) = a^(m-2) % (mod m)
        for (int n = mod - 2; n > 0; n >>= 1) {
            if ((n & 1) != 0) {
                res = (res * x) % mod;
            }
            x = (x * x) % mod;
        }
        return res;
    }

    private void test(int[] nums, int expected) {
        assertEquals(expected, numOfWays(nums));
        assertEquals(expected, numOfWays2(nums));
        assertEquals(expected, numOfWays3(nums));
    }

    @Test public void test() {
        test(new int[] {3, 4, 5, 1, 2}, 5);
        test(new int[] {2, 1, 3}, 1);
        test(new int[] {6, 1, 3, 2, 7, 4, 5}, 17);
        test(new int[] {1, 2, 3}, 0);
        test(new int[] {6, 9, 11, 15, 1, 12, 3, 2, 7, 8, 14, 4, 5, 13, 10}, 840839);
        test(new int[] {3, 1, 2, 5, 4, 6}, 19);
        test(new int[] {9, 4, 2, 1, 3, 6, 5, 7, 8, 14, 11, 10, 12, 13, 16, 15, 17, 18}, 216212978);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
