import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1787: https://leetcode.com/problems/make-the-xor-of-all-segments-equal-to-zero/
//
// You are given an array nums and an integer k The XOR of a segment [left, right] where
// left <= right is the XOR of all the elements with indices between left and right, inclusive:
// nums[left] XOR nums[left+1] XOR ... XOR nums[right].
// Return the minimum number of elements to change in the array such that the XOR of all segments of
// size k is equal to zero.
//
// Constraints:
// 1 <= k <= nums.length <= 2000
// 0 <= nums[i] < 2^10
public class MakeXOREqualToZero {
    // 2D-Dynamic Programming
    // time complexity: O(N*MAX), space complexity: O(K*MAX)
    // 117 ms(100.00%), 65.3 MB(100.00%) for 81 tests
    public int minChanges(int[] nums, int k) {
        int n = nums.length;
        int max = Arrays.stream(nums).max().getAsInt() + 1;
        int[][] freq = new int[k][max];
        for (int i = 0; i < n; i++) {
            freq[i % k][nums[i]]++;
        }
        int[][] dp = new int[k + 1][max]; // min changes needed in first i elements s.t. xor = j
        for (int i = 0; i <= k; i++) {
            Arrays.fill(dp[i], n);
        }
        dp[0][0] = 0;
        for (int i = 0, prevMin = 0; i < k; i++) {
            int min = n;
            for (int j = 0, count = (n - i + k - 1) / k; j < max; j++) {
                // change nums[i] to one of value from nums
                for (int p = i; p < n; p += k) {
                    int a = nums[p];
                    if ((j ^ a) >= max) { continue; }

                    dp[i + 1][j ^ a] = Math.min(dp[i + 1][j ^ a], dp[i][j] + count - freq[i][a]);
                }
                // change nums[i] to other value
                dp[i + 1][j] = Math.min(dp[i + 1][j], count + prevMin);
                min = Math.min(min, dp[i + 1][j]);
            }
            prevMin = min;
        }
        return dp[k][0];
    }

    // 1D-Dynamic Programming
    // time complexity: O(N*MAX), space complexity: O(MAX)
    // 130 ms(100.00%), 39.3 MB(100.00%) for 81 tests
    public int minChanges2(int[] nums, int k) {
        int max = Arrays.stream(nums).max().getAsInt() + 1;
        int[] dp = new int[max];
        int n = nums.length;
        Arrays.fill(dp, n);
        dp[0] = 0;
        for (int i = 0; i < k; i++) {
            int[] freq = new int[max];
            for (int j = i; j < n; j += k) {
                freq[nums[j]]++;
            }
            int count = (n - i + k - 1) / k;
            int[] ndp = new int[max];
            int min = Arrays.stream(dp).min().getAsInt();
            Arrays.fill(ndp, min + count);
            for (int j = 0; j < max; j++) {
                for (int p = i; p < n; p += k) {
                    int a = nums[p];
                    if ((j ^ a) < max) {
                        ndp[j ^ a] = Math.min(ndp[j ^ a], dp[j] + count - freq[a]);
                    }
                }
            }
            dp = ndp;
        }
        return dp[0];
    }

    private void test(int[] nums, int k, int expected) {
        assertEquals(expected, minChanges(nums, k));
        assertEquals(expected, minChanges2(nums, k));
    }

    @Test public void test() {
        test(new int[] {3, 4, 5, 2, 1, 7, 3, 4, 7}, 3, 3);
        test(new int[] {1, 2, 4, 1, 2, 5, 1, 2, 6}, 3, 3);
        test(new int[] {1, 2, 0, 3, 0}, 1, 3);
        test(new int[] {16, 5, 15, 6, 1, 25, 0, 13, 12, 12, 7, 16, 4, 25, 3}, 3, 12);
        test(new int[] {4, 11, 31, 2, 16, 12, 0, 27, 11, 26, 8, 23, 2, 2, 22, 5, 9}, 3, 13);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
