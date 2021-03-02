import java.util.*;
import java.util.stream.IntStream;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1775: https://leetcode.com/problems/equal-sum-arrays-with-minimum-number-of-operations/
//
// You are given two arrays of integers nums1 and nums2, possibly of different lengths. The values
// in the arrays are between 1 and 6, inclusive.
// In one operation, you can change any integer's value in any of the arrays to any value between 1
// and 6, inclusive.
// Return the minimum number of operations required to make the sum of values in nums1 equal to the
// sum of values in nums2. Return -1 if it is not possible to make the sum of the two arrays equal.
//
// Constraints:
// 1 <= nums1.length, nums2.length <= 10^5
// 1 <= nums1[i], nums2[i] <= 6
public class EqualSumArraysWithMinOperation {
    // Greedy + Heap
    // time complexity: O((N1+N2)*log(N1+N2)), space complexity: O(log(N1+N2))
    // 55 ms(33.33%), 55.6 MB(16.67%) for 62 tests
    public int minOperations(int[] nums1, int[] nums2) {
        int n1 = nums1.length;
        int n2 = nums2.length;
        if (n1 * 6 < n2 || n2 * 6 < n1) { return -1; }

        int sum1 = Arrays.stream(nums1).sum();
        int sum2 = Arrays.stream(nums2).sum();
        if (sum1 == sum2) { return 0; }

        if (sum1 > sum2) {
            int[] tmp = nums1;
            nums1 = nums2;
            nums2 = tmp;
        }
        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> (b - a));
        for (int a : nums1) {
            if (a != 6) {
                pq.offer(6 - a);
            }
        }
        for (int a : nums2) {
            if (a != 1) {
                pq.offer(a - 1);
            }
        }
        int res = 0;
        for (int diff = Math.abs(sum2 - sum1); diff > 0; res++) {
            diff -= pq.poll();
        }
        return res;
    }

    // Sort + Two Pointers
    // time complexity: O(N1*log(N1)+N2*log(N2)), space complexity: O(log(N1+N2))
    // 15 ms(33.33%), 48.7 MB(83.33%) for 62 tests
    public int minOperations2(int[] nums1, int[] nums2) {
        int n1 = nums1.length;
        int n2 = nums2.length;
        if (n1 * 6 < n2 || n2 * 6 < n1) { return -1; }

        int sum1 = Arrays.stream(nums1).sum();
        int sum2 = Arrays.stream(nums2).sum();
        if (sum1 > sum2) { return minOperations2(nums2, nums1); }

        Arrays.sort(nums1);
        Arrays.sort(nums2);
        int res = 0;
        for (int i = 0, j = n2 - 1, diff = sum2 - sum1; diff > 0; res++) {
            if (j < 0 || i < n1 && 6 - nums1[i] > nums2[j] - 1) {
                diff -= 6 - nums1[i++];
            } else {
                diff -= nums2[j--] - 1;
            }
        }
        return res;
    }

    // Count Sort
    // time complexity: O(N1+N2), space complexity: O(1)
    // 14 ms(33.33%), 48.7 MB(83.33%) for 62 tests
    public int minOperations3(int[] nums1, int[] nums2) {
        int n1 = nums1.length;
        int n2 = nums2.length;
        if (n1 * 6 < n2 || n2 * 6 < n1) { return -1; }

        int sum1 = Arrays.stream(nums1).sum();
        int sum2 = Arrays.stream(nums2).sum();
        if (sum1 > sum2) { return minOperations3(nums2, nums1); }

        int[] count = new int[6];
        IntStream.of(nums1).forEach(i -> count[6 - i]++);
        IntStream.of(nums2).forEach(i -> count[i - 1]++);
        int res = 0;
        for (int i = 5, diff = sum2 - sum1; diff > 0; ) {
            if (count[i]-- == 0) {
                i--;
            } else {
                diff -= i;
                res++;
            }
        }
        return res;
    }

    // Count Sort
    // time complexity: O(N1+N2), space complexity: O(1)
    // 14 ms(33.33%), 48.7 MB(83.33%) for 62 tests
    public int minOperations4(int[] nums1, int[] nums2) {
        int n1 = nums1.length;
        int n2 = nums2.length;
        if (n1 * 6 < n2 || n2 * 6 < n1) { return -1; }

        int sum1 = Arrays.stream(nums1).sum();
        int sum2 = Arrays.stream(nums2).sum();
        if (sum1 > sum2) { return minOperations4(nums2, nums1); }

        int[] count = new int[6];
        IntStream.of(nums1).forEach(i -> count[6 - i]++);
        IntStream.of(nums2).forEach(i -> count[i - 1]++);
        int res = 0;
        for (int i = 5, diff = sum2 - sum1; diff > 0; i--) {
            int times = Math.min(count[i], (int)((diff + i - 0.5) / i));
            diff -= times * i;
            res += times;
        }
        return res;
    }

    private void test(int[] nums1, int[] nums2, int expected) {
        assertEquals(expected, minOperations(nums1, nums2));
        assertEquals(expected, minOperations2(nums1, nums2));
        assertEquals(expected, minOperations3(nums1, nums2));
        assertEquals(expected, minOperations4(nums1, nums2));
    }

    @Test public void test() {
        test(new int[] {1, 2, 3, 4, 5, 6}, new int[] {1, 1, 2, 2, 2, 2}, 3);
        test(new int[] {1, 1, 1, 1, 1, 1, 1}, new int[] {6}, -1);
        test(new int[] {6, 6}, new int[] {1}, 3);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
