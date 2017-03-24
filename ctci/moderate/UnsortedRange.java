import java.util.Arrays;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 17.6:
 * Given an array of integers, write a method to find indices m and n such that
 * if you sorted elements m through n,the entire array would be sorted.
 * Minimize n - m(that is, find the smallest such sequence).
 */
public class UnsortedRange {
    static class Range {
        int start;
        int end;

        Range(int from, int to) {
            start = from;
            end = to;
        }

        @Override
        public boolean equals(Object that) {
            if (!(that instanceof Range)) return false;
            Range other = (Range)that;
            return (start == other.start) && (end == other.end);
        }

        @Override
        public String toString() {
            return "(" + start + "," + end + ")";
        }
    }

    // easy but less efficient
    // time complexity: O(N * log(N)), space complexity: O(N)
    public static Range getRangeEasy(int[] a) {
        int[] b = Arrays.copyOf(a, a.length);
        Arrays.sort(b);
        int left = 0;
        for (; left < a.length; left++) {
            if (a[left] != b[left]) break;
        }
        if (left == a.length) return null; // sorted

        int right = a.length - 1;
        for (; right >= 0; right--) {
            if (a[right] != b[right]) break;
        }
        return new Range(left, right);
    }

    // time complexity: O(N), space complexity: O(1)
    public static Range getRange(int[] a) {
        int minMid = getMinMid(a);
        if (minMid == a.length - 1) return null; // already sorted

        int maxMid = getMaxMid(a);

        Range minMax = getMinMax(a, minMid, maxMid);
        int left = getLeft(a, minMid, minMax.start);
        int right = getRight(a, maxMid, minMax.end);
        return new Range(left, right);
    }

    private static int getLeft(int[] a, int start, int min) {
        for (int i = start; i >= 0; i--) {
            if (a[i] <= min) return i + 1;
        }
        return 0;
    }

    private static int getRight(int[] a, int end, int max) {
        for (int i = end; i < a.length; i++) {
            if (a[i] >= max) return i - 1;
        }
        return a.length - 1;
    }

    private static int getMinMid(int[] a) {
        for (int i = 1; i < a.length; ++i) {
            if (a[i] < a[i - 1]) {
                return i - 1;
            }
        }
        return a.length - 1;
    }

    private static int getMaxMid(int[] a) {
        for (int i = a.length - 1; i > 0; --i) {
            if (a[i] < a[i - 1]) {
                return i;
            }
        }
        return 0;
    }

    private static Range getMinMax(int[] a, int start, int end) {
        int max = a[start];
        int min = a[start];
        for(int i = start + 1; i <= end; i++) {
            if (a[i] > max) {
                max = a[i];
            } else if (a[i] < min) {
                min = a[i];
            }
        }
        return new Range(min, max);
    }

    // from the book
    private static int findEndOfLeftSubsequence(int[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[i - 1]) {
                return i - 1;
            }
        }
        return array.length - 1;
    }

    private static int findStartOfRightSubsequence(int[] array) {
        for (int i = array.length - 2; i >= 0; i--) {
            if (array[i] > array[i + 1]) {
                return i + 1;
            }
        }
        return 0;
    }

    private static int shrinkLeft(int[] array, int min_index, int start) {
        int comp = array[min_index];
        for (int i = start - 1; i >= 0; i--) {
            if (array[i] <= comp) {
                return i + 1;
            }
        }
        return 0;
    }

    private static int shrinkRight(int[] array, int max_index, int start) {
        int comp = array[max_index];
        for (int i = start; i < array.length; i++) {
            if (array[i] >= comp) {
                return i - 1;
            }
        }
        return array.length - 1;
    }

    public static Range findUnsortedSequence(int[] array) {
        int end_left = findEndOfLeftSubsequence(array);
        int start_right = findStartOfRightSubsequence(array);

        int min_index = end_left + 1;
        if (min_index >= array.length) {
            return null; // Already sorted
        }

        int max_index = start_right - 1;
        for (int i = end_left; i <= start_right; i++) {
            if (array[i] < array[min_index]) {
                min_index = i;
            }
            if (array[i] > array[max_index]) {
                max_index = i;
            }
        }

        // slide left until less than array[min_index]
        int left_index = shrinkLeft(array, min_index, end_left);
        // slide right until greater than array[max_index]
        int right_index = shrinkRight(array, max_index, start_right);

        return new Range(left_index, right_index);
    }

    private Range test(Function<int[], Range> getRange, String name, int[] a) {
        long t1 = System.nanoTime();
        Range r = getRange.apply(a);
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        return r;
    }

    private void test(int[] a) {
        Range r0 = test(UnsortedRange::getRangeEasy, "getRangeEasy", a);
        Range r1 = test(UnsortedRange::getRange, "getRange", a);
        Range r2 = test(UnsortedRange::findUnsortedSequence, "findUnsortedSequence", a);
        System.out.println(r1);
        assertEquals(r0, r1);
        assertEquals(r0, r2);
    }

    @Test
    public void test1() {
        test(new int[] {1, 2, 4});
        test(new int[] {1, 4, 3, 6});
        test(new int[] {1, 2, 4, 7, 10, 11, 7, 12, 6, 7, 16, 18, 19});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("UnsortedRange");
    }
}
