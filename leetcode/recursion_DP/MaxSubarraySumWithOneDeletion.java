import org.junit.Test;

import static org.junit.Assert.*;

// LC1186: https://leetcode.com/problems/maximum-subarray-sum-with-one-deletion/
//
// Given an array of integers, return the maximum sum for a non-empty subarray (contiguous elements)
// with at most one element deletion. In other words, you want to choose a subarray and optionally
// delete one element from it so that there is still at least one element left and the sum of the
// remaining elements is maximum possible.
// Note that the subarray needs to be non-empty after deleting one element.
//
// Constraints:
// 1 <= arr.length <= 10^5
// -10^4 <= arr[i] <= 10^4
public class MaxSubarraySumWithOneDeletion {
    // time complexity: O(N^2), space complexity: O(1)
    // 0 ms(100.00%), 39 MB(26.79%) for 37 tests
    public int maximumSum(int[] arr) {
        int res = Integer.MIN_VALUE;
        for (int i = -1; i < arr.length; i++) {
            res = Math.max(res, maxSum(arr, i));
        }
        return res;
    }

    private int maxSum(int[] arr, int excluded) {
        int res = Integer.MIN_VALUE;
        int sum = 0;
        for (int i = 0, n = arr.length; i < n; i++) {
            if (i == excluded) { continue; }

            sum += arr[i];
            res = Math.max(res, sum);
            sum = Math.max(sum, 0);
        }
        return res;
    }

    // 1D-Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // 2 ms(99.09%), 46.9 MB(71.72%) for 36 tests
    public int maximumSum2(int[] arr) {
        int n = arr.length;
        int[] left = new int[n + 1];
        for (int i = 0; i < n; i++) {
            left[i + 1] = Math.max(arr[i], arr[i] + left[i]);
        }
        int[] right = new int[n + 1];
        for (int i = n - 1; i >= 0; i--) {
            right[i] = Math.max(arr[i], arr[i] + right[i + 1]);
        }
        int res = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            res = Math.max(res, Math.max(left[i + 1], right[i]));
            res = Math.max(res, left[i + 1] + right[i + 1]);
            if (i > 0) {
                res = Math.max(res, left[i - 1] + right[i]);
            }
        }
        return res;
    }

    // 1D-Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // 2 ms(99.09%), 46.6 MB(98.19%) for 36 tests
    public int maximumSum3(int[] arr) {
        int n = arr.length;
        int[] left = new int[n + 1];
        for (int i = 0; i < n; i++) {
            left[i + 1] = Math.max(arr[i], arr[i] + left[i]);
        }
        int res = Integer.MIN_VALUE;
        for (int i = n - 1, right2 = 0, right1; i >= 0; i--, right2 = right1) {
            right1 = Math.max(arr[i], arr[i] + right2);
            res = Math.max(res, Math.max(left[i + 1], right1));
            res = Math.max(res, left[i + 1] + right2);
            if (i > 0) {
                res = Math.max(res, left[i - 1] + right1);
            }
        }
        return res;
    }

    // 0D-Dynamic Programming
    // time complexity: O(N), space complexity: O(1)
    // 3 ms(89.82%), 46.8 MB(88.46%) for 36 tests
    public int maximumSum4(int[] arr) {
        int res = arr[0];
        for (int i = 1, n = arr.length, delete0 = arr[0], delete1 = 0; i < n; i++) {
            delete1 = Math.max(delete1 + arr[i], delete0);
            delete0 = arr[i] + Math.max(delete0, 0);
            res = Math.max(res, Math.max(delete0, delete1));
        }
        return res;
    }

    private void test(int[] arr, int expected) {
        assertEquals(expected, maximumSum(arr));
        assertEquals(expected, maximumSum2(arr));
        assertEquals(expected, maximumSum3(arr));
        assertEquals(expected, maximumSum4(arr));
    }

    @Test public void test() {
        test(new int[] {1, -2, 0, 3}, 4);
        test(new int[] {1, -2, -2, 3}, 3);
        test(new int[] {-1, -1, -1, -1}, -1);
        test(new int[] {2, 1, -2, -5, -2}, 3);
        test(new int[] {-50}, -50);
        test(new int[] {4512, 3631, -1278, -711, -1724, 4995, -2026, 2429, -1912, -1817, 1146,
                        -1263, 1948, -485, 1294, -2843, -2592, -1073, 2658, -3447, -2652, -1309,
                        982, 3629, -1960, 2567, -4998, 1745, 3212, 3995, 3886, 2469, 1616, -401,
                        -2446, -1527, 3172, 915, 838, -387, 4440, 1079, 1754, 3089, -4932, -2469,
                        -2258, 3717, 1328, 2319, -302, 2601, -1244, -685, 294, -2739, 1944, -4809,
                        -2678, 2417, -3620, 2011, -4978, -2539, 128, 2559, -3710, 185, 1204, -3789,
                        -2426, -1523, 4744, 119, 234, 1281, 524, -1349, -4466, 565, -3984, 3711,
                        -382, -1127, -1708, -253, 294, -3203, 3320, -505, -1366, -4655, 204, 4507,
                        3390, -1131, -4912, 1071, -670, 1321, -940, 1651, 3318, -2547, -56, -4537,
                        -4214, -3055, 220, -4757, -4082, -4715, 4784, 2079, 4242, 3369, -620, -1117,
                        2262, 3229, 1928, 4071, -4022, 2529, -1044, 2987, -1602, -971, -2912, 1183,
                        1874, -4919, -12, 35, -2938, 3629, -2584, 2279, 1994, -1855, -2196, 3291,
                        -4402, -4571, 736, 4015, -1214, 745, 3684, -3085, 3126, -4739, -1912, 4167,
                        1178, 1521, -2756, 3867, 3470, -1179, 1776, 703, 1698, 2345, 3332, -13,
                        4726, 1405, 4504, 4583, 90, -1519, -3124, -2981, 878, -1739, -384, -2001,
                        -2990, 953, -2028, -3197, 4950, -3795, -488, 2551, 2026, 1169, -2756, -4453,
                        -498, -4171, -1008, 1327, -1102, -887, 2308, -237, -3082, 2253, 3448, 4503,
                        -2934, 1073, 3516, -997, -4994, 4421, -560, -1521, -3726, 2233, 3332, -1469,
                        1462, 4909, -2904, -2713, -1846, -2127, -2308, -1573, -3250, -1355, -2096,
                        3551, -4478, -615, 212, 2995, -3802, -2563, -1256, -4313, 10, 2129, 1468,
                        395, -34, 1893, -944, 3679, -2062, 2457, 724, -2189, -346, 2013, 4424,
                        -3641, -3846, -1407, -2132, 43, -1602, -1611, 3904, -241, 2530, 57, 436,
                        2739, -1434, -1699, 920, -3993, -4790, -4495, 3980, -3861, 3774, 805, -2400,
                        4559, 2530, 4121, 3300, 915, -4410, -2739, -2504, -4073, -2022, -3951,
                        -1972, 2507, -4082, 293, 4704, -4401, 322, 3849, -4412, 915, 3574, 3325,
                        -2040, 647, -486, 2257, 4764, 3355, -2066, 2245, -864, -801, 1826, -407,
                        -1820, 1971, -2042, -2259, 3832, 2711, -3814, -1951, 2188, 619, -290, -1819,
                        64, 559, -334, -4983, -748, 4291, -4522, -819, -408, -3449, -3990, 1393,
                        2284, 1355, -1874, 261, 4704, 3135, 3186, 1209, 3812, 2739, -4746, -3779,
                        2296, 4135, 3050, 993, 2060, 4459, 4990, 213, 3008, 4607, -3230, -4807,
                        3764, -4621, -1946, -1251, -4488, -1193, 2938, -1071, -2452, 3963, -626,
                        -4443, -1440, -3921, -4142, 537, 116, 2291, -4410, 2013, -3368, 167, -1030,
                        3921, -948}, 51692);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
