import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

// LC852: https://leetcode.com/problems/peak-index-in-a-mountain-array/
//
// Let's call an array A a mountain if the following properties hold:
// A.length >= 3
// There exists some 0 < i < A.length - 1 such that A[0] < A[1] < ... A[i-1] <
// A[i] > A[i+1] > ... > A[A.length - 1]
// Given an array that is definitely a mountain, return such i.
public class PeakIndexInMountainArray {
    // time complexity: O(N), space complexity: O(1)
    // beats 35.46%(3 ms for 32 tests)
    public int peakIndexInMountainArray(int[] A) {
        for (int i = 0; ; i++) {
            if (A[i + 1] < A[i])
                return i;
        }
    }

    // Binary Search
    // time complexity: O(log(N)), space complexity: O(1)
    // beats 99.80%(2 ms for 32 tests)
    public int peakIndexInMountainArray2(int[] A) {
        int low = 0;
        for (int high = A.length - 1; low < high; ) {
            int mid = (low + high) >>> 1;
            if (A[mid] < A[mid + 1]) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    // Golden-section search
    // https://en.wikipedia.org/wiki/Golden-section_search
    // time complexity: O(log(N)), space complexity: O(1)
    // beats 100.00%(0 ms for 32 tests)
    // 0 ms(100%), 38.5 MB(99.93%) for 32 tests
    public int peakIndexInMountainArray3(int[] A) {
        int left = 0;
        int right = A.length - 1;
        int x1 = gold1(left, right);
        int x2 = gold2(left, right);
        while (x1 < x2) {
            if (A[x1] < A[x2]) {
                left = x1;
                x1 = x2;
                x2 = gold1(x1, right);
            } else {
                right = x2;
                x2 = x1;
                x1 = gold2(left, x2);
            }
        }
        int max = A[left];
        for (int i = left + 1; i <= right; i++) {
            max = Math.max(max, A[i]);
        }
        for (int i = left; ; i++) {
            if (A[i] == max) return i;
        }
    }

    private int gold1(int left, int right) {
        return left + (int)Math.round((right - left) * 0.382);
    }

    private int gold2(int left, int right) {
        return left + (int)Math.round((right - left) * 0.618);
    }

    void test(int[] A, int expected) {
        assertEquals(expected, peakIndexInMountainArray(A));
        assertEquals(expected, peakIndexInMountainArray2(A));
        assertEquals(expected, peakIndexInMountainArray3(A));
    }

    @Test
    public void test() {
        test(new int[]{0, 1, 0}, 1);
        test(new int[]{0, 2, 1, 0}, 1);
        test(new int[]{1, 2, 3, 4, 1, 0}, 3);
    }

    public static void main(String[] args) {
        String clazz =
                new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
