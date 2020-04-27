import java.util.ArrayDeque;
import java.util.Deque;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1425: https://leetcode.com/problems/constrained-subset-sum/
//
// Given an integer array nums and an integer k, return the maximum sum of a non-empty subset of that array such that
// for every two consecutive integers in the subset, nums[i] and nums[j], where i < j, the condition j - i <= k is
// satisfied.
// A subset of an array is obtained by deleting some number of elements (can be zero) from the array, leaving the
// remaining elements in their original order.
// Constraints:
// 1 <= k <= nums.length <= 10^5
// -10^4 <= nums[i] <= 10^4
public class ConstrainedSubsetSum {
    // Deque (monoqueue) + Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // 24 ms(100%), 62.7 MB(100%) for 15 tests
    public int constrainedSubsetSum(int[] nums, int k) {
        int n = nums.length;
        // dp[i]: max sum if nums[i] is chosen
        // dp[i] = max(0, dp[i - k], dp[i - k + 1], .., dp[i -1]) + nums[i]
        int[] dp = new int[n];
        Deque<Integer> maxQ = new ArrayDeque<>();
        int res = nums[0];
        for (int i = 0; i < n; i++) {
            dp[i] = (maxQ.isEmpty() ? 0 : maxQ.peekFirst()) + nums[i];
            res = Math.max(res, dp[i]);
            while (!maxQ.isEmpty() && maxQ.peekLast() < dp[i]) {
                maxQ.pollLast();
            }
            if (dp[i] > 0) {
                maxQ.offerLast(dp[i]);
            }
            if (i >= k && !maxQ.isEmpty() && maxQ.peekFirst() == dp[i - k]) {
                maxQ.pollFirst();
            }
        }
        return res;
    }

    // Deque (monoqueue) + Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // 5 ms(100%), 47.7 MB(100%) for 15 tests
    public int constrainedSubsetSum2(int[] nums, int k) {
        int n = nums.length;
        int[] dp = new int[n];
        int[] maxQ = new int[n];
        int res = Integer.MIN_VALUE;
        for (int i = 0, head = 0, tail = 0; i < n; i++) {
            dp[i] = ((head == tail) ? 0 : dp[maxQ[head]]) + nums[i];
            res = Math.max(res, dp[i]);
            for (; head != tail && dp[maxQ[tail - 1]] < dp[i]; tail--) {}
            if (dp[i] > 0) {
                maxQ[tail++] = i;
            }
            if (head != tail && maxQ[head] == i - k) {
                head++;
            }
        }
        return res;
    }

    // TODO: Segment tree

    private void test(int[] nums, int k, int expected) {
        assertEquals(expected, constrainedSubsetSum(nums, k));
        assertEquals(expected, constrainedSubsetSum2(nums, k));
    }

    @Test public void test() {
        test(new int[] {10, 2, -10, 5, 20}, 2, 37);
        test(new int[] {-1, -2, -3}, 1, -1);
        test(new int[] {10, -2, -10, -5, 20}, 2, 23);
        test(new int[] {10, -2, -10, -6, -5, 20, 12, -30, -2, -40, 5, -8, -9, -10, 12, 78, -98, -19,
                        -20, 3}, 3, 121);
        test(new int[] {-9535, 3750, -5771, -5049, 4296, -3354, -4854, -4228, -2936, -8216, -7398,
                        900, 9775, -3260, -3208, 2316, 797, -9602, -9566, -908, -1350, 3419, 4891,
                        615, 2562, 8838, -7073, -976, 9422, -4489, -4011, -4906, -8461, -3262, 2270,
                        4235, -7985, 4337, -2984, 5924, 483, 9966, 9681, 9421, -7548, -1943, -5300,
                        -4207, -880, -7057, 4576, 2958, 8956, -5415, -5761, -6003, 8447, -2773,
                        -4681, 6315, -2191, -3980, 5160, -7540, 8408, 3935, -7584, -8273, -6700,
                        1650, 3, -1090, 2387, -8874, 7563, -3906, -6929, 3861, -7765, 7671, -8500,
                        2118, -7760, -1594, -4520, 3323, 5338, 3630, 7832, -2551, 6236, -334, 1845,
                        6238, -28, 277, 3806, 455, 4197, -2216, 534, -717, 5744, 9711, -7217, -4416,
                        -4566, 9564, -6229, 778, 5712, -9396, 6049, -954, -7351, -3895, -3072, 241,
                        -9766, 5150, 9256, 3428, -1707, -8562, -5748, 2426, 6822, -4729, 4529,
                        -8679, -7715, 6742, 5561, -7019, -3474, -5849, 5445, 2426, -9305, 4848,
                        -3171, 220, 6818, 887, -3966, -4852, 9897, 5570, 8270, -7907, 3727, -8328,
                        668, -4414, -6717, 3115, -6727, -4651, -4372, -4226, 190, 2429, 722, 3339,
                        -1492, 8443, 5030, -2414, 556, -2123, -8057, -5819, -5072, 2493, 112, -3981,
                        2671, 8866, -7191, -1307, -8094, -4561, 8633, -4167, 2334, 4826, 9060,
                        -1660, -3232, -9080, -4287, -5042, 9324, -186, -5351, -3051, -2914, 4298,
                        6491, -2605, 1194, 481, 3062, 3665, -2968, -5847, -4189, 5376, -2862, 4588,
                        -342, -1851, 727, 6790, 5232, -2132, -5634, -2707, 6231, 7173, -4188, -6316,
                        -2998, -5588, -1803, -6121, 6978, 1731, 2380, 1552, 297, -6072, 6423, 5925,
                        5626, 1593, 7958, -3835, -8292, 1825, 3173, 5327, -5226, 4963, 1915, -6373,
                        -6235, 9362, -703, 6456, 5326, 773, -9149, -8555, -9955, -7952, -8567,
                        -5432, 7784, 6432, 4249, -5466, 9813, 1732, -7524, 8426, -6389, 2496, -9885,
                        2308, -8355, -931, 817, 7622, -3100, -173, 4875, -3565, 2707, 9584, -1656,
                        167, -4968, -8162, -6408, -4882, 8380, 4740, -2167, -5112, 334, -8852,
                        -1879, -1541, -6591, 9385, -3163, 592, 1892, -906, -9427, -7616, -1489,
                        -4901, -808, 8964, 7035, -6063, 6813, 1662, -8230, -9144, -3204, 90, -3128,
                        -6482, 3595, 8643, 9253, 3966, 9847, -3350, -5244, 187, -6864, -7524, 4352,
                        -3436, 4883, -5963, -8107, -4296, -7137, 1075, 8829, 8617, -36, -8321, 2126,
                        -385, 1573, 2353, 1057, 9908, 1580, -8663, -4887, 6377, 5112, -877, -8446,
                        4636, -6400, 5002, -8550, -8583, 9747, 5066, 3010, -2857, -9716, -6870,
                        5369, 2546, -8556, -6565, 8427, 9393, 8741, -6075, -2084, -8293, 8034,
                        -3866, -2799, -1979, -4001, 4816, 2608, 7615, 8683, 9941, 5245, -2784, 3730,
                        -3550, -3713, 4204, 204, -1878, -7823, -3812, 1811, 2511, -1787, -9718,
                        -7595, 1813, -8978, 4651, -8129, -1681, -8845, 5596, 8222, -4097, 467, 6620,
                        865, 839, 7407, -9202, -4987, -9390, 7734, -3028, -2326, 525, 5264, 7861,
                        -5325, -5427, 4236, 952, -8094, 9529, 5711, 162, 2282, -4121, -3382, -9407,
                        1261, 9963, 7969, 7698, -2111, -68, -2159, -8041, 4140, 2461, 5292, 3660,
                        -6615, -449, 9871, 2202, -3776, -6293, -9931, -8843, 7458, -2238, -7572,
                        2054, -7115, 82, -9220, -4254, 1369, 1340, -2660, -4271, -5636, 37, 7050,
                        -5925, 6224, -2122, -2340, -3478, -3813, 4757, 9019, 4180, 5805, 8435, 2679,
                        -6875, 7223, 2137, -6044, -4565, 9380, 7367, -82, -1116, 1293, 6526, 2112,
                        3920, -2980, -6291, 4141, 6888, -5412, -5189, 4311, -8408}, 325, 1137751);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
