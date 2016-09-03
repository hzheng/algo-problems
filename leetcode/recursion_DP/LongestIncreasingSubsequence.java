import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.function.Function;

// LC300: https://leetcode.com/problems/longest-increasing-subsequence/
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

        // Let A[i,j] be the smallest possible tail out of all increasing
        // subsequences of length j using elements nums[0] to nums[i].
        // seq is a longest increasing subsequences of them:
        // A[i,1]<A[i,2]<...<A[i,j], for any A[i,k](1<=k<=j) we could not find
        // a smaller alternative.
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
                    high = mid; // not mid - 1!
                }
            }
            seq.set(low, num);
        }
        return seq.size();
    }

    // beats 89.51%(1 ms)
    public int lengthOfLIS3(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;

        int[] seq = new int[n];
        seq[0] = nums[0];
        int length = 1;
        for (int i = 1; i < n; i++) {
            int num = nums[i];
            if (num > seq[length - 1]) {
                seq[length++] = num;
                continue;
            }

            int low = 0;
            int high = length - 1;
            while (low < high) {
                int mid = low + (high - low) / 2;
                if (seq[mid] < num) {
                    low = mid + 1;
                } else {
                    high = mid;
                }
            }
            seq[low] = num;
        }
        return length;
    }

    // Solution of Choice
    // beats 69.02%(3 ms)
    public int lengthOfLIS4(int[] nums) {
        int[] seq = new int[nums.length];
        int len = 0;
        for (int num : nums) {
            int index = Arrays.binarySearch(seq, 0, len, num);
            if (index < 0) {
                index = -(index + 1);
            }
            seq[index] = num;
            if (index == len) {
                len++;
            }
        }
        return len;
    }

    void test(Function<int[], Integer> length, int expected, int... nums) {
        assertEquals(expected, (int)length.apply(nums));
    }

    void test(int expected, int... nums) {
        LongestIncreasingSubsequence l = new LongestIncreasingSubsequence();
        test(l::lengthOfLIS, expected, nums);
        test(l::lengthOfLIS2, expected, nums);
        test(l::lengthOfLIS3, expected, nums);
        test(l::lengthOfLIS4, expected, nums);
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
