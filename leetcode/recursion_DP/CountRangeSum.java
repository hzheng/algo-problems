import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC327: https://leetcode.com/problems/count-of-range-sum/
//
// Given an integer array nums, return the number of range sums that lie in
// [lower, upper] inclusive.
// Range sum S(i, j) is defined as the sum of the elements in nums between
// indices i and j (i <= j), inclusive.
// Note:
// A naive algorithm of O(n ^ 2) is trivial. You MUST do better than that.
public class CountRangeSum {
    // Recursion + Divide and Conquer + Sort + Binary Search
    // time complexity: O(N * log(N) ^ 2), space complexity: O(N)
    // beats 35.13%(41 ms for 61 tests)
    public int countRangeSum(int[] nums, int lower, int upper) {
        int n = nums.length;
        if (n == 0) return 0;

        long[] sums = new long[n + 1];
        for (int i = 0; i < n; i++) {
            sums[i + 1] = sums[i] + nums[i];
        }
        return countRangeSum(nums, lower, upper, 0, n, sums, new double[n / 2]);
    }

    private int countRangeSum(int[] nums, int lower, int upper,
                              int start, int end, long[] sums, double[] buffer) {
        int size = (end - start) >>> 1;
        if (size == 0) {
            return nums[start] >= lower && nums[start] <= upper ? 1 : 0;
        }
        int mid = start + size;
        int count = countRangeSum(nums, lower, upper, start, mid, sums, buffer)
                    + countRangeSum(nums, lower, upper, mid, end, sums, buffer);
        for (int i = 0; i < size; i++) {
            buffer[i] = sums[start + i];
        }
        Arrays.sort(buffer, 0, size);
        for (int i = mid + 1; i <= end; i++) {
            int min = -Arrays.binarySearch(buffer, 0, size, sums[i] - upper - 0.5);
            if (min <= size) {
                count += -Arrays.binarySearch(
                    buffer, min - 1, size, sums[i] - lower + 0.5) - min;
            }
        }
        return count;
    }

    // Solution of Choice
    // Recursion + Divide & Conquer + Merge Sort
    // https://discuss.leetcode.com/topic/33738/share-my-solution/
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 62.49%(16 ms for 61 tests)
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
            for (; left < end && sums[left] - sums[i] < lower; left++) {}
            for (; right < end && sums[right] - sums[i] <= upper; right++) {}
            count += right - left;
            for (; k < end && sums[k] < sums[i]; buffer[j++] = sums[k++]) {}
            buffer[j] = sums[i];
        }
        System.arraycopy(buffer, 0, sums, start, k - start);
        return count;
    }

    // Sort + Binary Search + Binary Indexed Tree
    // https://discuss.leetcode.com/topic/33749/an-o-n-log-n-solution-via-fenwick-tree
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 35.13%(41 ms for 61 tests)
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

    // Sort + Binary Search + Binary Indexed Tree
    // https://maskray.me/leetcode/count-of-range-sum.cc
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 46.48%(25 ms for 61 tests)
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
        add(bit, bound(sums, 0, true));
        for (int num : nums) {
            prefixSum += num;
            int maxPos = bound(sums, prefixSum - lower, false);
            int minPos = bound(sums, prefixSum - upper, true);
            count += sum2(bit, maxPos) - sum2(bit, minPos);
            add(bit, bound(sums, prefixSum, true));
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

    private int bound(long[] nums, long x, boolean lower) {
        int pos = Arrays.binarySearch(nums, x);
        return (pos >= 0) ? (lower ? pos : pos + 1) : -pos - 1;
    }

    // Sort + Segment Tree + Recursion
    // beats 39.67%(36 ms for 61 tests)
    public int countRangeSum5(int[] nums, int lower, int upper) {
        Set<Long> valSet = new HashSet<>();
        long sum = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += (long)nums[i];
            valSet.add(sum);
        }
        Long[] vals = valSet.toArray(new Long[0]);
        Arrays.sort(vals);
        SegmentTreeNode root = SegmentTreeNode.build(vals, 0, vals.length - 1);
        int res = 0;
        for (int i = nums.length - 1; i >= 0; i--) {
            root.update(sum);
            sum -= (long)nums[i];
            res += root.getCount((long)lower + sum, (long)upper + sum);
        }
        return res;
    }

    private static class SegmentTreeNode {
        SegmentTreeNode left, right;
        int count;
        long min, max;

        SegmentTreeNode(long min, long max) {
            this.min = min;
            this.max = max;
        }

        static SegmentTreeNode build(Long[] vals, int low, int high) {
            if (low > high) return null;

            SegmentTreeNode node = new SegmentTreeNode(vals[low], vals[high]);
            if (low == high) return node;

            int mid = (low + high) >>> 1;
            node.left = build(vals, low, mid);
            node.right = build(vals, mid + 1, high);
            return node;
        }

        void update(long val) {
            if (val < min || val > max) return;

            count++;
            if (left != null) {
                left.update(val);
            }
            if (right != null) {
                right.update(val);
            }
        }

        int getCount(long min, long max) {
            if (min > this.max || max < this.min) return 0;
            if (min <= this.min && max >= this.max) return count;

            return (left == null ? 0 : left.getCount(min, max))
                   + (right == null ? 0 : right.getCount(min, max));
        }
    }

    // TODO: BST

    void test(int[] nums, int lower, int upper, int expected) {
        assertEquals(expected, countRangeSum(nums, lower, upper));
        assertEquals(expected, countRangeSum2(nums, lower, upper));
        assertEquals(expected, countRangeSum3(nums, lower, upper));
        assertEquals(expected, countRangeSum4(nums, lower, upper));
        assertEquals(expected, countRangeSum5(nums, lower, upper));
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
