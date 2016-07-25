import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/count-of-range-sum/
//
// Given an integer array nums, return the number of range sums that lie in
// [lower, upper] inclusive.
// Range sum S(i, j) is defined as the sum of the elements in nums between
// indices i and j (i <= j), inclusive.
// Note:
// A naive algorithm of O(n ^ 2) is trivial. You MUST do better than that.
public class CountRangeSum {
    // time complexity: O(N * log(N) ^ 2), space complexity: O(N)
    // beats 34.27%(42 ms)
    public int countRangeSum(int[] nums, int lower, int upper) {
        int n = nums.length;

        long[] dp = new long[n + 1];
        for (int i = 0; i < n; i++) {
            dp[i + 1] = dp[i] + nums[i];
        }
        return countRangeSum(nums, lower, upper, 0, n, dp, new long[n / 2]);
    }

    private int countRangeSum(int[] nums, int lower, int upper,
                              int start, int end, long[] dp, long[] buffer) {
        int size = end - start;
        if (size <= 0) return 0;

        if (size == 1) {
            return nums[start] >= lower && nums[start] <= upper ? 1 : 0;
        }

        size /= 2;
        int mid = start + size;
        int count = countRangeSum(nums, lower, upper, start, mid, dp, buffer)
                    + countRangeSum(nums, lower, upper, mid, end, dp, buffer);

        System.arraycopy(dp, start, buffer, 0, size);
        Arrays.sort(buffer, 0, size);
        for (int i = mid; i < end; i++) {
            int minPos = Arrays.binarySearch(buffer, 0, size, dp[i + 1] - upper);
            if (minPos >= 0) {
                for (int j = minPos - 1; j >= 0; j--, minPos--) {
                    if (buffer[j] != buffer[j + 1]) break; // may repeat
                }
            } else {
                minPos = -minPos - 1;
                if (minPos == size) continue;
            }

            int maxPos = Arrays.binarySearch(buffer, 0, size, dp[i + 1] - lower);
            if (maxPos >= 0) {
                for (int j = maxPos + 1; j < size; j++, maxPos++) {
                    if (buffer[j] != buffer[j - 1]) break; // may repeat
                }
                count += maxPos - minPos + 1;
            } else {
                maxPos = -maxPos - 1;
                if (maxPos > 0) {
                    count += maxPos - minPos;
                }
            }
        }
        return count;
    }

    void test(int[] nums, int lower, int upper, int expected) {
        assertEquals(expected, countRangeSum(nums, lower, upper));
    }

    @Test
    public void test1() {
        test(new int[] {0, -1, 1, 2, -3, -3}, -3, 1, 13);
        test(new int[] {0, -3, 2, -2, -2}, -3, 1, 11);
        test(new int[] {-3, 2, -2, -2}, -3, 1, 7);
        test(new int[] {0, 0, -3, 2, -2, -2}, -3, 1, 16);
        test(new int[] {10, -3, -2}, -5, 5, 4);
        test(new int[] {2, 6, 10, -3, -2}, -5, 5, 5);
        test(new int[] {5, 2, 6, 10, -3, -2}, -5, 5, 6);
        test(new int[] {-8, 5, 2, 6, 10, -3, -2}, -5, 5, 9);
        test(new int[] {-1, -8, 5, 2, 6, 10, -3, -2}, -5, 5, 13);
        test(new int[] {5, -1, -8, 5, 2, 6, 10, -3, -2}, -5, 5, 18);
        test(new int[] {-2, 5, -1}, -2, 2, 3);
        test(new int[] {-2, 5, -1, -8, 5, 2, 6, 10, -3}, -5, 5, 20);
        test(new int[] {-2, 5, -1, -8, 5, 2, 6, 10, -3, -2, 8}, -5, 5, 24);
        test(new int[] {1, 22, -23, 20, -22, -11, 7, -10}, -23, -16, 5);

        test(new int[] {-2147483647, 0, -2147483647, 2147483647}, -564, 3864, 3);

        test(new int[] {0,  -29, -16, 0, 12, -28, 7, 1, 22, -23, 20, -22, -11,
                        7, -10},  -23,  -16,  15);

        test(new int[] {0, -29, -16, 0, 12, -28, 7, 1, 22, -23, 20, -22, -11, 7,
                        -10, -5, 27, 27, 0, 19, -9, 28, -2, 6, 23, -9, -9, 1,
                        8, -15}, -23,  -16,  27);

        test(new int[] {6, 21, -27, 17, -20, 3, 1, -2, 10, 2, 23, 15, -3, 1, 9,
                        19, -9, -24, -30, -26, -13, 23, 2, -10, 20, 0, 27, 24,
                        -28, 26, 0, -29, -16, 0, 12, -28, 7, 1, 22, -23, 20,
                        -22, -11, 7, -10, -5, 27, 27, 0, 19, -9, 28, -2, 6, 23,
                        -9, -9, 1, 8, -15},
             -23, -16, 97);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CountRangeSum");
    }
}
