import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC740: https://leetcode.com/problems/delete-and-earn/
//
// Given an array nums of integers, you can perform operations on the array.
// In each operation, you pick any nums[i] and delete it to earn nums[i] points.
// After, you must delete every element equal to nums[i] - 1 or nums[i] + 1.
// You start with 0 points. Return the maximum number of points you can earn by
// applying such operations.
// Note:
// The length of nums is at most 20000.
// Each element nums[i] is an integer in the range [1, 10000].
public class DeleteAndEarn {
    private static final int MAX = 10000;

    // Recursion + Dynamic Programming(Top-Down) + Hash Table
    // beats %(12 ms for 46 tests)
    public int deleteAndEarn(int[] nums) {
        int[] counts = new int[MAX + 1];
        for (int num : nums) {
            counts[num]++;
        }
        int res = 0;
        Map<Integer, Integer> dp = new HashMap<>();
        for (int i = 1; i <= MAX; i++) {
            int count = counts[i];
            if (count == 0 || counts[i + 1] == 0) {
                res += count * i;
                continue;
            }
            int j = i + 1;
            for (; j <= MAX; j++) {
                if (counts[j] == 0) break;
            }
            res += max(counts, i, j - 1, dp);
            i = j;
        }
        return res;
    }

    private int max(int[] counts, int start, int end,
                    Map<Integer, Integer> dp) {
        if (end == start) return counts[start] * start;

        if (end - start == 1) {
            return Math.max(counts[start] * start,
                            counts[start + 1] * (start + 1));
        }

        int k = start * MAX + end;
        Integer cached = dp.get(k);
        if (cached != null) return cached;

        int res1 = counts[start] * start + max(counts, start + 2, end, dp);
        int res2 = max(counts, start + 1, end, dp);
        int res = Math.max(res1, res2);
        dp.put(k, res);
        return res;
    }

    // Dynamic Programming(Bottom-Up)
    // beats %(13 ms for 46 tests)
    public int deleteAndEarn2(int[] nums) {
        int[] counts = new int[MAX + 1];
        for (int num : nums) {
            counts[num]++;
        }
        int[] dp = new int[MAX + 1];
        dp[1] = counts[1];
        for (int i = 2; i < dp.length; i++) {
            dp[i] = Math.max(counts[i] * i + dp[i - 2], dp[i - 1]);
        }
        return dp[dp.length - 1];
    }

    // Dynamic Programming(Bottom-Up)
    // beats %(12 ms for 46 tests)
    public int deleteAndEarn3(int[] nums) {
        int[] points = new int[MAX + 1];
        for (int num : nums) {
            points[num] += num;
        }
        int[] dp = new int[MAX + 3];
        for(int i = MAX; i >= 0; i--) {
            dp[i] = Math.max(points[i] + dp[i + 2], dp[i + 1]);
        }
        return dp[0];
    }

    // Dynamic Programming(Bottom-Up)
    // beats %(10 ms for 46 tests)
    public int deleteAndEarn4(int[] nums) {
        int[] counts = new int[MAX + 1];
        for (int num : nums) {
            counts[num]++;
        }
        int cur = 0;
        for (int i = 1, prev = 0; i < counts.length; i++) {
            int tmp = Math.max(counts[i] * i + prev, cur);
            prev = cur;
            cur = tmp;
        }
        return cur;
    }

    // Dynamic Programming(Bottom-Up)
    // beats %(13 ms for 46 tests)
    public int deleteAndEarn5(int[] nums) {
        int[] points = new int[MAX + 1];
        for (int num : nums) {
            points[num] += num;
        }
        int cur = 0;
        int prev = 0;
        for (int point : points) {
            int tmp = Math.max(point + prev, cur);
            prev = cur;
            cur = tmp;
        }
        return cur;
    }

    void test(int[] nums, int expected) {
        assertEquals(expected, deleteAndEarn(nums));
        assertEquals(expected, deleteAndEarn2(nums));
        assertEquals(expected, deleteAndEarn3(nums));
        assertEquals(expected, deleteAndEarn4(nums));
        assertEquals(expected, deleteAndEarn5(nums));
    }

    @Test
    public void test() {
        test(new int[] {3, 4, 2}, 6);
        test(new int[] {2, 2, 3, 3, 3, 4}, 9);
        test(new int[] {10, 8, 4, 2, 1, 3, 4, 8, 2, 9, 10, 4, 8, 5, 9, 1, 5, 1,
                        6, 8, 1, 1, 6, 7, 8, 9, 1, 7, 6, 8, 4, 5, 4, 1, 5, 9, 8,
                        6, 10, 6, 4, 3, 8, 4, 10, 8, 8, 10, 6, 4, 4, 4, 9, 6, 9,
                        10, 7, 1, 5, 3, 4, 4, 8, 1, 1, 2, 1, 4, 1, 1, 4, 9, 4,
                        7, 1, 5, 1, 10, 3, 5, 10, 3, 10, 2, 1, 10, 4, 1, 1, 4,
                        1, 2, 10, 9, 7, 10, 1, 2, 7, 5}, 338);
        test(new int[] {94, 27, 27, 27, 34, 82, 97, 93, 62, 10, 78, 25, 23, 41,
                        53, 16, 81, 93, 52, 53, 74, 78, 18, 27, 66, 62, 40, 50,
                        8, 20, 31, 77, 26, 82, 28, 60, 98, 94, 26, 30, 23, 49,
                        54, 80, 69, 28, 25, 32, 78, 7, 1, 73, 2, 31, 99, 78, 50,
                        95, 28, 53, 60, 78, 71, 52, 25, 85, 21, 16, 20, 78, 96,
                        96, 65, 1, 19, 18, 24, 18, 55, 69, 88, 76, 14, 23, 58,
                        17, 83, 43, 63, 9, 41, 6, 71, 7, 2, 20, 21, 63, 18, 36,
                        53, 95, 36, 11, 32, 64, 52, 48, 52, 11, 50, 48, 35, 49,
                        24, 89, 72, 33, 60, 57, 46, 3, 24, 90, 20, 95, 87, 8,
                        93, 1, 47, 2, 66, 45, 57, 75, 18, 76, 96, 67, 65, 92,
                        92, 41, 57, 60, 98, 98, 10, 64, 23, 86, 100, 20, 21, 93,
                        49, 54, 77, 77, 34, 98, 94, 4, 9, 75, 67, 4, 31, 82, 87,
                        26, 70, 26, 59, 86, 100, 22, 15, 61, 57, 73, 54, 54, 76,
                        82, 56, 63, 49, 46, 53, 71, 32, 1, 64, 48, 20, 71, 2,
                        60, 83, 80, 97, 30, 2, 57, 31, 82, 21, 63, 52, 46, 71,
                        55, 58, 94, 16, 9, 62, 67, 74, 79, 87, 31, 53, 27, 80,
                        11, 33, 52, 73, 2, 88, 80, 9, 38, 37, 3, 79, 24, 89, 75,
                        10, 97, 24, 63, 24, 47, 80, 56, 75, 23, 32, 58, 72, 80,
                        95, 28, 57, 37, 17, 48, 14, 85, 58, 61, 58, 1, 37, 14,
                        34, 76, 11, 63, 67, 7, 9, 8, 74, 38, 97, 56, 25, 67, 9,
                        34, 62, 58, 72, 77, 15, 15, 90, 36, 60, 39, 95, 61, 28,
                        44, 43, 56, 22, 12, 81, 13, 10, 91, 84, 46, 39, 35, 39,
                        65, 82, 41, 51, 19, 76, 99, 75, 88, 43, 89, 21, 83, 6,
                        35, 21, 47, 4, 21, 51, 76, 63, 43, 71, 39, 43, 16, 36,
                        78, 35, 68, 75, 81, 91, 97, 7, 82, 44, 73, 56, 39, 76,
                        21, 76, 87, 98, 6, 38, 96, 84, 96, 77, 84, 83, 28, 52,
                        100, 6, 52, 78, 7, 91, 96, 97, 62, 32, 26, 7, 80, 71,
                        25, 58, 23, 54, 74, 81, 4, 84, 35, 83, 58, 64, 42, 38,
                        30, 88, 87, 52, 95, 23, 31, 31, 55, 7, 20, 18, 84, 40,
                        14, 93, 40, 45, 69, 84, 30, 66, 6, 88, 41, 88, 98, 80,
                        69, 64, 1, 100, 48, 2, 89, 6, 21, 45, 73, 77, 31, 20,
                        70, 89, 30, 53, 33, 59, 8, 82, 63, 17, 10, 46, 49, 86,
                        9, 14, 68, 6, 15, 55, 36, 71, 64, 80, 59, 40, 60, 46,
                        24, 49, 45, 78, 38, 92, 43, 99, 78, 5, 83, 57, 76, 34,
                        11, 93, 71, 71, 54, 54, 29, 29, 74, 83, 72, 1, 6, 56,
                        22, 85, 35, 48, 29}, 14251);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
