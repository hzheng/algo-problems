import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1619: https://leetcode.com/problems/mean-of-array-after-removing-some-elements/
//
// Given an integer array arr, return the mean of the remaining integers after removing the smallest
// 5% and the largest 5% of the elements.
// Answers within 10^-5 of the actual answer will be considered accepted.
//
// Constraints:
// 20 <= arr.length <= 1000
// arr.length is a multiple of 20.
// 0 <= arr[i] <= 10^5
public class TrimMean {
    // Sort
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 2 ms(97.80%), 39.1 MB(39.43%) for 50 tests
    public double trimMean(int[] arr) {
        Arrays.sort(arr);
        int n = arr.length;
        int sum = 0;
        for (int i = n / 20, max = n - i; i < max; i++) {
            sum += arr[i];
        }
        return sum / (n - n / 10.0);
    }

    // Heap
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 11 ms(6.10%), 38.6 MB(94.81%) for 50 tests
    public double trimMean2(int[] arr) {
        PriorityQueue<Integer> highQ = new PriorityQueue<>(Comparator.comparingInt(a -> a));
        PriorityQueue<Integer> lowQ = new PriorityQueue<>(Comparator.comparingInt(a -> -a));
        int n = arr.length;
        int limit = n / 20;
        int sum = 0;
        for (int a : arr) {
            highQ.offer(a);
            if (highQ.size() > limit) {
                lowQ.offer(highQ.poll());
                if (lowQ.size() > limit) {
                    sum += lowQ.poll();
                }
            }
        }
        return sum / (n - 2.0 * limit);
    }

    // TODO: QuickSelect for O(N)

    private void test(int[] arr, double expected) {
        assertEquals(expected, trimMean(arr), 1E-5);
        assertEquals(expected, trimMean2(arr), 1E-5);
    }

    @Test public void test() {
        test(new int[] {1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3}, 2.0);
        test(new int[] {6, 2, 7, 5, 1, 2, 0, 3, 10, 2, 5, 0, 5, 5, 0, 8, 7, 6, 8, 0}, 4.0);
        test(new int[] {6, 0, 7, 0, 7, 5, 7, 8, 3, 4, 0, 7, 8, 1, 6, 8, 1, 1, 2, 4, 8, 1, 9, 5, 4,
                        3, 8, 5, 10, 8, 6, 6, 1, 0, 6, 10, 8, 2, 3, 4}, 4.77778);
        test(new int[] {9, 7, 8, 7, 7, 8, 4, 4, 6, 8, 8, 7, 6, 8, 8, 9, 2, 6, 0, 0, 1, 10, 8, 6, 3,
                        3, 5, 1, 10, 9, 0, 7, 10, 0, 10, 4, 1, 10, 6, 9, 3, 6, 0, 0, 2, 7, 0, 6, 7,
                        2, 9, 7, 7, 3, 0, 1, 6, 1, 10, 3}, 5.27778);
        test(new int[] {4, 8, 4, 10, 0, 7, 1, 3, 7, 8, 8, 3, 4, 1, 6, 2, 1, 1, 8, 0, 9, 8, 0, 3, 9,
                        10, 3, 10, 1, 10, 7, 3, 2, 1, 4, 9, 10, 7, 6, 4, 0, 8, 5, 1, 2, 1, 6, 2, 5,
                        0, 7, 10, 9, 10, 3, 7, 10, 5, 8, 5, 7, 6, 7, 6, 10, 9, 5, 10, 5, 5, 7, 2,
                        10, 7, 7, 8, 2, 0, 1, 1}, 5.29167);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
