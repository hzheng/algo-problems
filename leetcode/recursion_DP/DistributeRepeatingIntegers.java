import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1655: https://leetcode.com/problems/distribute-repeating-integers/
//
// You are given an array of n integers, nums, where there are at most 50 unique values in the
// array. You are also given an array of m customer order quantities, quantity, where quantity[i] is
// the amount of integers the ith customer ordered. Determine if it is possible to distribute nums
// such that:
// The ith customer gets exactly quantity[i] integers,
// The integers the ith customer gets are all equal, and
// Every customer is satisfied.
// Return true if it is possible to distribute nums according to the above conditions.
//
// Constraints:
// n == nums.length
// 1 <= n <= 10^5
// 1 <= nums[i] <= 1000
// m == quantity.length
// 1 <= m <= 10
// 1 <= quantity[i] <= 10^5
// There are at most 50 unique values in nums.
public class DistributeRepeatingIntegers {
    // Recursion + DFS + Backtracking + Sort
    // 22 ms(96.43%), 49.2 MB(57.86%) for 100 tests
    public boolean canDistribute(int[] nums, int[] quantity) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        int[] count = new int[map.size()];
        int i = 0;
        for (int k : map.keySet()) {
            count[i++] = map.get(k);
        }
        Arrays.sort(quantity);
        return canDistribute(quantity.length - 1, count, quantity);
    }

    private boolean canDistribute(int cur, int[] count, int[] quantity) {
        if (cur < 0) { return true; }

        Set<Integer> used = new HashSet<>(); // checking this optimized from 97 ms to 22 ms
        for (int i = 0, m = count.length, q = quantity[cur]; i < m; i++) {
            if (count[i] < q || !used.add(count[i])) { continue; }

            count[i] -= q;
            if (canDistribute(cur - 1, count, quantity)) { return true; }
            count[i] += q;
        }
        return false;
    }

    // Recursion + DFS + Dynamic Programming(Top-Down) + Bit Manipulation
    // time complexity: O(N*2^M), space complexity: O(N*2^M)
    // 60 ms(62.86%), 53.8 MB(20.71%) for 100 tests
    public boolean canDistribute2(int[] nums, int[] quantity) {
        int m = quantity.length;
        int choices = 1 << m;
        int[] total = new int[choices];
        for (int mask = choices - 1; mask >= 0; mask--) {
            int sum = 0;
            for (int i = 0; i < m; i++) {
                if (((mask >> i) & 1) != 0) {
                    sum += quantity[i];
                }
            }
            total[mask] = sum;
        }
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        int n = map.size();
        int[] count = new int[n];
        int i = 0;
        for (int k : map.keySet()) {
            count[i++] = map.get(k);
        }
        return dfs(count, total, n - 1, choices - 1, new Boolean[n][choices]);
    }

    private boolean dfs(int[] count, int[] total, int cur, int mask, Boolean[][] dp) {
        if (mask == 0) {return true; }
        if (cur < 0) {return false; }
        if (dp[cur][mask] != null) { return dp[cur][mask]; }

        boolean res = dfs(count, total, cur - 1, mask, dp);
        for (int submask = mask; !res && submask > 0; submask = (submask - 1) & mask) {
            if (total[submask] <= count[cur]) {
                res = dfs(count, total, cur - 1, mask ^ submask, dp);
            }
        }
        return dp[cur][mask] = res;
    }

    // 2D-Dynamic Programming(Bottom-Up) + Bit Manipulation
    // time complexity: O(N*2^M), space complexity: O(N*2^M)
    // 60 ms(62.86%), 47.6 MB(97.14%) for 100 tests
    public boolean canDistribute3(int[] nums, int[] quantity) {
        int m = quantity.length;
        int fullMask = (1 << m) - 1;
        int[] total = new int[fullMask + 1];
        for (int mask = fullMask; mask >= 0; mask--) {
            int sum = 0;
            for (int i = 0; i < m; i++) {
                if (((mask >> i) & 1) != 0) {
                    sum += quantity[i];
                }
            }
            total[mask] = sum;
        }
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        int n = map.size();
        int[] count = new int[n];
        int i = 0;
        for (int k : map.keySet()) {
            count[i++] = map.get(k);
        }
        boolean[][] dp = new boolean[n + 1][fullMask + 1];
        dp[0][0] = true;
        for (i = 0; i < n; i++) {
            for (int choice = 0; choice <= fullMask; choice++) {
                if (!dp[i][choice]) { continue; }

                dp[i + 1][choice] = true;
                int mask = fullMask ^ choice;
                for (int submask = mask; submask > 0; submask = (submask - 1) & mask) {
                    dp[i + 1][choice | submask] |= (total[submask] <= count[i]);
                }
            }
        }
        return dp[n][fullMask];
    }

    private void test(int[] nums, int[] quantity, boolean expected) {
        assertEquals(expected, canDistribute(nums, quantity));
        assertEquals(expected, canDistribute2(nums, quantity));
        assertEquals(expected, canDistribute3(nums, quantity));
    }

    @Test public void test() {
        test(new int[] {1, 2, 3, 4}, new int[] {2}, false);
        test(new int[] {1, 2, 3, 3}, new int[] {2}, true);
        test(new int[] {1, 1, 2, 2}, new int[] {2, 2}, true);
        test(new int[] {1, 1, 2, 3}, new int[] {2, 2}, false);
        test(new int[] {1, 1, 1, 1, 1}, new int[] {2, 3}, true);
        test(new int[] {1, 1, 1, 1, 2, 2, 2}, new int[] {3, 2, 2}, true);
        test(new int[] {1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12,
                        12, 13, 13, 14, 14, 15, 15, 16, 16, 17, 17, 18, 18, 19, 19, 20, 20, 21, 21,
                        22, 22, 23, 23, 24, 24, 25, 25, 26, 26, 27, 27, 28, 28, 29, 29, 30, 30, 31,
                        31, 32, 32, 33, 33, 34, 34, 35, 35, 36, 36, 37, 37, 38, 38, 39, 39, 40, 40,
                        41, 41, 42, 42, 43, 43, 44, 44, 45, 45, 46, 46, 47, 47, 48, 48, 49, 49, 50,
                        50}, new int[] {2, 2, 2, 2, 2, 2, 2, 2, 2, 3}, false);
        test(new int[] {128, 812, 834, 231, 658, 812, 905, 128, 124, 403, 812, 796, 995, 510, 128,
                        128, 750, 128, 128, 128, 665, 995, 812, 728, 665, 128, 252, 273, 252, 830,
                        665, 128, 273, 128, 128, 665, 687, 785, 665, 419, 834, 633, 338, 665, 128,
                        128, 128, 812, 97, 720, 252, 319, 620, 710, 853, 128, 128, 252, 812, 661,
                        273, 231, 812, 128, 410, 828, 128, 84, 458, 252, 252, 853, 698, 128, 252},
             new int[] {15, 7}, true);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
