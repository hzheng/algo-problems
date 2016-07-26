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
    // Divide and Conquer + Binary Search
    // time complexity: O(N * log(N) ^ 2), space complexity: O(N)
    // beats 41.33%(37 ms)
    public int countRangeSum(int[] nums, int lower, int upper) {
        int n = nums.length;
        if (n == 0) return 0;

        long[] sums = new long[n + 1];
        for (int i = 0; i < n; i++) {
            sums[i + 1] = sums[i] + nums[i];
        }
        return countRangeSum(nums, lower, upper, 0, n, sums, new long[n / 2]);
    }

    private int countRangeSum(int[] nums, int lower, int upper,
                              int start, int end, long[] sums, long[] buffer) {
        int size = (end - start) / 2;
        if (size == 0) {
            return nums[start] >= lower && nums[start] <= upper ? 1 : 0;
        }

        int mid = start + size;
        int count = countRangeSum(nums, lower, upper, start, mid, sums, buffer)
                    + countRangeSum(nums, lower, upper, mid, end, sums, buffer);

        System.arraycopy(sums, start, buffer, 0, size);
        Arrays.sort(buffer, 0, size);
        for (int i = mid; i < end; i++) {
            int minPos = Arrays.binarySearch(buffer, 0, size, sums[i + 1] - upper);
            if (minPos < 0) {
                minPos = -minPos - 1;
                if (minPos == size) continue;
            } else { // in case of duplicates
                for (int j = minPos - 1; j >= 0 && buffer[j] == buffer[j + 1];
                     j--, minPos--) {}
            }

            int maxPos = Arrays.binarySearch(buffer, minPos, size, sums[i + 1] - lower);
            if (maxPos < 0) {
                maxPos = -maxPos - 1;
                if (maxPos <= 0) continue;
            } else { // in case of duplicates
                for (int j = ++maxPos; j < size && buffer[j] == buffer[j - 1];
                     j++, maxPos++) {}
            }
            count += maxPos - minPos;
        }
        return count;
    }

    // Merge Sort processing
    // https://discuss.leetcode.com/topic/33738/share-my-solution/2
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 60.48%(16 ms)
    public int countRangeSum2(int[] nums, int lower, int upper) {
        int n = nums.length;
        long[] sums = new long[n + 1];
        for (int i = 0; i < n; ++i) {
            sums[i + 1] = sums[i] + nums[i];
        }
        return countInMergeSort(sums, 0, n + 1, lower, upper, new long[n + 1]);
    }

    private int countInMergeSort(long[] sums, int start, int end,
                                 int lower, int upper, long[] buffer) {
        int size = end - start;
        if (size <= 1) return 0;

        int mid = start + size / 2;
        int count = countInMergeSort(sums, start, mid, lower, upper, buffer)
                    + countInMergeSort(sums, mid, end, lower, upper, buffer);
        int k = mid;
        for (int i = start, j = 0, left = mid, right = mid; i < mid; i++, j++) {
            while (left < end && sums[left] - sums[i] < lower) left++;
            while (right < end && sums[right] - sums[i] <= upper) right++;
            while (k < end && sums[k] < sums[i]) buffer[j++] = sums[k++];
            buffer[j] = sums[i];
            count += right - left;
        }
        System.arraycopy(buffer, 0, sums, start, k - start);
        return count;
    }

    // Binary Indexed Tree
    // https://discuss.leetcode.com/topic/33749/an-o-n-log-n-solution-via-fenwick-tree
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 35.65%(41 ms)
    public int countRangeSum3(int[] nums, int lower, int upper) {
        int n = nums.length;
        if (n == 0) return 0;

        long[] sums = new long[n + 1];
        long[] bounds = new long[n * 3 + 2];
        bounds[1] = Long.MIN_VALUE; // make sure no number gets a 0-index.
        for (int i = 0, j = 2; i < n; i++) {
            sums[i + 1] = sums[i] + nums[i];
            bounds[j++] = sums[i + 1];
            bounds[j++] = lower + sums[i] - 1;
            bounds[j++] = upper + sums[i];
        }
        Arrays.sort(bounds);

        int[] bit = new int[bounds.length];
        for (int i = 0; i <= n; i++) {
            add(bit, Arrays.binarySearch(bounds, sums[i]), 1);
        }
        int count = 0;
        for (int i = 0; i < n; i++) {
            add(bit, Arrays.binarySearch(bounds, sums[i]), -1);
            count += sum(bit, Arrays.binarySearch(bounds, upper + sums[i]));
            count -= sum(bit, Arrays.binarySearch(bounds, lower + sums[i] - 1));
        }
        return count;
    }

    private void add(int[] bit, int i, int delta) {
        for (; i < bit.length; i += (i & -i)) {
            bit[i] += delta;
        }
    }

    private int sum(int[] bit, int i) {
        int sum = 0;
        for (; i > 0; i -= (i & -i)) {
            sum += bit[i];
        }
        return sum;
    }

    // https://maskray.me/leetcode/count-of-range-sum.cc
    // beats 47.18%(31 ms)
    // time complexity: O(N * log(N)), space complexity: O(N)
    public int countRangeSum4(int[] nums, int lower, int upper) {
        int n = nums.length;
        if (n == 0) return 0;

        int[] bit = new int[n + 1];
        long[] sums = new long[n + 1];
        for (int i = 0; i < n; i++) {
            sums[i + 1] = sums[i] + nums[i];
        }
        Arrays.sort(sums, 0, n + 1);
        long prefixSum = 0;
        int count = 0;
        add(bit, bound(sums, 0, n + 1, 0, true));
        for (int num : nums) {
            prefixSum += num;
            int maxPos = bound(sums, 0, n + 1, prefixSum - lower, false);
            int minPos = bound(sums, 0, n + 1, prefixSum - upper, true);
            count += sum2(bit, maxPos) - sum2(bit, minPos);
            add(bit, bound(sums, 0, n + 1, prefixSum, true));
        }
        return count;
    }

    private void add(int[] bit, int i) {
        for (; i < bit.length; i |= (i + 1)) { // fill the lowest 0 bit
            bit[i]++;
        }
    }

    private int sum2(int[] bit, int i) {
        int sum = 0;
        for (; i > 0; i &= (i - 1)) { // zero the lowest 1 bit
            sum += bit[i - 1];
        }
        return sum;
    }

    private int bound(long[] nums, int start, int end, long x, boolean lower) {
        int pos = Arrays.binarySearch(nums, start, end, x);
        return (pos >= 0) ? (lower ? pos : pos + 1) : -pos - 1;
    }

    // TODO: Segment Tree
    // TODO: BST
    // TODO: Order Statistic Tree

    void test(int[] nums, int lower, int upper, int expected) {
        assertEquals(expected, countRangeSum(nums, lower, upper));
        assertEquals(expected, countRangeSum2(nums, lower, upper));
        assertEquals(expected, countRangeSum3(nums, lower, upper));
        assertEquals(expected, countRangeSum4(nums, lower, upper));
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
