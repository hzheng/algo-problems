import java.util.*;
import java.util.stream.IntStream;

import org.junit.Test;

import static org.junit.Assert.*;

// LC962: https://leetcode.com/problems/maximum-width-ramp/
//
// Given an array A of integers, a ramp is a tuple (i, j) for which i < j and A[i] <= A[j]. The
// width of it is j - i. Find the maximum width of a ramp in A. Return 0 if one doesn't exist.
//
// Note:
// 2 <= A.length <= 50000
// 0 <= A[i] <= 50000
public class MaxWidthRamp {
    // Binary Search
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 7 ms(79.18%), 47.4 MB(23.65%) for 98 tests
    public int maxWidthRamp(int[] A) {
        int n = A.length;
        int[] left = new int[n + 1];
        left[0] = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            left[i + 1] = Math.min(left[i], A[i]);
        }
        int res = 0;
        for (int i = n - 1; i >= 0; i--) {
            int a = A[i];
            int pos = search(left, i, a) - 1;
            if (pos >= 0 && A[pos] <= a) {
                int width = i - pos;
                res = Math.max(res, width);
            }
        }
        return res;
    }

    private int search(int[] a, int end, int target) {
        int low = 0;
        for (int high = end; low < high; ) {
            int mid = (low + high) >>> 1;
            int midVal = a[mid];
            if (midVal > target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    // Sort
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 38 ms(29.31%), 48.7 MB(5.40%) for 98 tests
    public int maxWidthRamp2(int[] A) {
        int n = A.length;
        Integer[] B = IntStream.range(0, n).boxed().toArray(Integer[]::new);
        Arrays.sort(B, Comparator.comparingInt(i -> A[i]));
        int res = 0;
        int minIndex = n;
        for (int i : B) {
            res = Math.max(res, i - minIndex);
            minIndex = Math.min(minIndex, i);
        }
        return res;
    }

    // Binary Search
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 10 ms(56.04%), 46 MB(89.46%) for 98 tests
    public int maxWidthRamp3(int[] A) {
        int res = 0;
        List<Integer> candidates = new ArrayList<>();
        for (int i = A.length - 1; i >= 0; i--) {
            int low = 0;
            for (int high = candidates.size(); low < high; ) {
                int mid = (low + high) >>> 1;
                if (A[candidates.get(mid)] < A[i]) {
                    low = mid + 1;
                } else {
                    high = mid;
                }
            }
            if (low < candidates.size()) {
                res = Math.max(res, candidates.get(low) - i);
            } else {
                candidates.add(i);
            }
        }
        return res;
    }

    // Binary Search
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 8 ms(67.10%), 45.7 MB(95.12%) for 98 tests
    public int maxWidthRamp4(int[] A) {
        int res = 0;
        List<Integer> candidates = new ArrayList<>();
        for (int i = 0, n = A.length; i < n; i++) {
            if (candidates.isEmpty() || A[i] < A[candidates.get(candidates.size() - 1)]) {
                candidates.add(i);
            } else {
                int low = 0;
                for (int high = candidates.size() - 1; low < high; ) {
                    int mid = (low + high) >>> 1;
                    if (A[candidates.get(mid)] > A[i]) {
                        low = mid + 1;
                    } else {
                        high = mid;
                    }
                }
                res = Math.max(res, i - candidates.get(low));
            }
        }
        return res;
    }

    // Stack (monoqueue)
    // time complexity: O(N), space complexity: O(N)
    // 6 ms(85.35%), 47 MB(32.65%) for 98 tests
    public int maxWidthRamp5(int[] A) {
        Stack<Integer> candidates = new Stack<>();
        for (int i = 0, n = A.length; i < n; i++) {
            if (candidates.isEmpty() || A[i] < A[candidates.peek()]) {
                candidates.push(i);
            }
        }
        int res = 0;
        for (int i = A.length - 1; i > res; i--) {
            while (!candidates.isEmpty() && A[i] >= A[candidates.peek()]) {
                res = Math.max(res, i - candidates.pop());
            }
        }
        return res;
    }

    // Dynamic Programming + Two Pointers
    // time complexity: O(N), space complexity: O(N)
    // 2 ms(100.00%), 47.4 MB(21.08%) for 98 tests
    public int maxWidthRamp6(int[] A) {
        int n = A.length;
        int[] right = new int[n + 1];
        for (int i = n - 1; i >= 0; i--) {
            right[i] = Math.max(right[i + 1], A[i]);
        }
        int res = 0;
        for (int i = 0, j = 1; j < n; j++) {
            for (; i < j && A[i] > right[j]; i++) {}
            res = Math.max(res, j - i);
        }
        return res;
    }

    private void test(int[] A, int expected) {
        assertEquals(expected, maxWidthRamp(A));
        assertEquals(expected, maxWidthRamp2(A));
        assertEquals(expected, maxWidthRamp3(A));
        assertEquals(expected, maxWidthRamp4(A));
        assertEquals(expected, maxWidthRamp5(A));
        assertEquals(expected, maxWidthRamp6(A));
    }

    @Test public void test() {
        test(new int[] {6, 0, 8, 2, 1, 5}, 4);
        test(new int[] {9, 8, 1, 0, 1, 9, 4, 0, 4, 1}, 7);
        test(new int[] {6, 5, 5, 0, 8, 2, 1, 5}, 6);
        test(new int[] {7, 6, 5, 5, 0, 8, 2, 1, 5, 3, 6, 5, 6}, 11);
        test(new int[] {1, 0}, 0);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
