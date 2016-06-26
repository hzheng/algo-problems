import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.function.Function;

// https://leetcode.com/problems/longest-increasing-subsequence/
//
// Given an unsorted array of integers, find the length of longest increasing
// subsequence.
public class LongestIncreasingSubsequence {
    // beats 52.71%(29 ms)
    // time complexity: O(N ^ 2), space complexity: O(N)
    public int lengthOfLIS(int[] nums) {
        int n = nums.length;
        int[] lens = new int[n];
        for (int i = 0; i < n; i++) {
            int max = 0;
            int num = nums[i];
            for (int j = 0; j < i; j++) {
                if (nums[j] < num) {
                    max = Math.max(max, lens[j]);
                }
            }
            lens[i] = max + 1;
        }
        int max = 0;
        for (int len : lens) {
            max = Math.max(max, len);
        }
        return max;
    }

    // http://www.algorithmist.com/index.php/Longest_Increasing_Subsequence
    // time complexity: O(N ^ log(N)), space complexity: O(N)
    // beats 62.94%(4 ms)
    public int lengthOfLIS2(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;

        // seq[i] is an longest increasing subsequence with smallest possible
        // tail from nums[0] to nums[i]
        List<Integer> seq = new ArrayList<>();
        seq.add(nums[0]);
        for (int i = 1; i < n; i++) {
            int maxLen = seq.size();
            int num = nums[i];
            if (num > seq.get(maxLen - 1)) {
                seq.add(num);
                continue;
            }

            int low = 0;
            int high = maxLen - 1;
            while (low < high) {
                int mid = low + (high - low) / 2;
                if (seq.get(mid) < num) {
                    low = mid + 1;
                } else {
                    high = mid;
                }
            }
            seq.set(low, num);
        }
        return seq.size();
    }

    void test(Function<int[], Integer> length, int expected, int... nums) {
        assertEquals(expected, (int)length.apply(nums));
    }

    void test(int expected, int... nums) {
        LongestIncreasingSubsequence l = new LongestIncreasingSubsequence();
        test(l::lengthOfLIS, expected, nums);
        test(l::lengthOfLIS2, expected, nums);
    }

    @Test
    public void test1() {
        test(6, 3, 5, 6, 2, 5, 4, 19, 5, 6, 7, 12);
        test(3, 1, 2, 5, 1, 2);
        test(1, 1);
        test(1, 1, 1);
        test(2, 1, 2);
        test(3, 1, 2, 0, 3);
        test(6, 1, 3, 6, 7, 9, 4, 10, 5, 6);
        test(4, 10, 9, 2, 5, 3, 7, 101, 18);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LongestIncreasingSubsequence");
    }
}
