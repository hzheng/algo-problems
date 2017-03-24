import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem :
 * Given a sorted array of n integers that has been rotated an unknown number
 * of times, write code to find an element in the array.
 */
public class RotatedSearch {
    public static int rotatedSearch(int x, int[] arr) {
        return rotatedSearch(x, arr, 0, arr.length - 1);
    }

    private static int rotatedSearch(int x, int[] arr, int start, int end) {
        int mid = (start + end) / 2;
        int midVal = arr[mid];
        if (x == midVal) return mid;

        if (start >= end) return -1;

        if (x > midVal) {
            int endVal = arr[end];
            if (x == endVal) return end;

            if (x < endVal || midVal > endVal) {
                return rotatedSearch(x, arr, mid + 1, end - 1);
            } else if (midVal < endVal) {
                return rotatedSearch(x, arr, start, mid - 1);
            } else { // midVal == endVal
                int pos = rotatedSearch(x, arr, mid + 1, end - 1);
                return (pos >= 0) ? pos : rotatedSearch(x, arr, start, mid - 1);
            }
        }
        // x < midVal
        int startVal = arr[start];
        if (x == startVal) return start;

        if (x > startVal || startVal > midVal) {
            return rotatedSearch(x, arr, start + 1, mid - 1);
        } else if (startVal < midVal) {
            return rotatedSearch(x, arr, mid + 1, end);
        } else { // startVal == midVal
            int pos = rotatedSearch(x, arr, start + 1, mid - 1);
            return (pos >= 0) ? pos : rotatedSearch(x, arr, mid + 1, end);
        }
    }

    // from the book
    public static int search(int x, int[] arr) {
        return search(arr, 0, arr.length - 1, x);
    }

    private static int search(int a[], int left, int right, int x) {
        int mid = (left + right) / 2;
        if (x == a[mid]) { return mid; }

        if (right < left) { return -1; }

        /* While there may be an inflection point due to the rotation, either the left or
         * right half must be normally ordered.  We can look at the normally ordered half
         * to make a determination as to which half we should search.
         */
        if (a[left] < a[mid]) { // Left is normally ordered.
            if (x >= a[left] && x <= a[mid]) {
                return search(a, left, mid - 1, x);
            } else {
                return search(a, mid + 1, right, x);
            }
        } else if (a[mid] < a[left]) { // Right is normally ordered.
            if (x >= a[mid] && x <= a[right]) {
                return search(a, mid + 1, right, x);
            } else {
                return search(a, left, mid - 1, x);
            }
        } else if (a[left] == a[mid]) { // Left is either all repeats OR loops around (with the right half being all dups)
            if (a[mid] != a[right]) { // If right half is different, search there
                return search(a, mid + 1, right, x);
            } else { // Else, we have to search both halves
                int result = search(a, left, mid - 1, x);
                if (result == -1) {
                    return search(a, mid + 1, right, x);
                } else {
                    return result;
                }
            }
        }
        return -1;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    private int test(Function<Integer, int[], Integer> search, String name,
                     int x, int[] arr) {
        long t1 = System.nanoTime();
        int n = search.apply(x, arr);
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        return n;
    }

    private void test(int[] arr, int[] tgts) {
        for (int x : tgts) {
            int i1 = test(RotatedSearch::rotatedSearch, "rotatedSearch", x, arr);
            int i2 = test(RotatedSearch::search, "search", x, arr);

            if (i1 != i2) { // in case of multi-solutions
                assertTrue(i1 >= 0 && i2 >= 0);
                assertEquals(x, arr[i1]);
                assertEquals(x, arr[i2]);
            }
        }
    }

    @Test
    public void test1() {
        test(new int[] {2, 3, 2, 2, 2, 2, 2, 2, 2, 2},
             new int[] {2, 3, 4, 1, 8});
        test(new int[] {2, 2, 2, 2, 3, 4, 1, 2, 2, 2},
             new int[] {2, 3, 4, 1, 8});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RotatedSearch");
    }
}
