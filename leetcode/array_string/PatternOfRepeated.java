import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1566: https://leetcode.com/problems/detect-pattern-of-length-m-repeated-k-or-more-times/
//
// Given an array of positive integers arr,  find a pattern of length m that is repeated k or more
// times.
// A pattern is a subarray (consecutive sub-sequence) that consists of one or more values, repeated
// multiple times consecutively without overlapping. A pattern is defined by its length and the
// number of repetitions.
// Return true if there exists a pattern of length m that is repeated k or more times, otherwise
// return false.
public class PatternOfRepeated {
    // 2-layer loops
    // time complexity: O(N * M * K), space complexity: O(1)
    // 0 ms(100%), 36.7 MB(100%) for 91 tests
    public boolean containsPattern(int[] arr, int m, int k) {
        int n = arr.length;
        outer:
        for (int i = 0; i <= n - m * k; i++) {
            for (int j = i; j < i + m * k - m; j++) {
                if (arr[j] != arr[j + m]) {
                    continue outer;
                }
            }
            return true;
        }
        return false;
    }

    // 3-layer loops
    // time complexity: O(N * M * K), space complexity: O(1)
    // 0 ms(100%), 37.5 MB(100%) for 91 tests
    public boolean containsPattern2(int[] arr, int m, int k) {
        int n = arr.length;
        outer:
        for (int i = 0; i < n - m + 1; i++) {
            for (int count = 1; count < k; count++) {
                int start = i + m * count;
                for (int j = 0; j < m; j++) {
                    int cur = j + start;
                    if (cur == n || arr[cur] != arr[i + j]) {
                        continue outer;
                    }
                }
            }
            return true;
        }
        return false;
    }

    // 1-layer loop
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100%), 37.3 MB(100%) for 91 tests
    public boolean containsPattern3(int[] arr, int m, int k) {
        for (int i = 0, j = i + m, count = 0; j < arr.length; i++, j++) {
            if (arr[i] != arr[j]) {
                count = 0;
            } else if (++count == (k - 1) * m) {
                return true;
            }
        }
        return false;
    }

    void test(int[] arr, int m, int k, boolean expected) {
        assertEquals(expected, containsPattern(arr, m, k));
        assertEquals(expected, containsPattern2(arr, m, k));
        assertEquals(expected, containsPattern3(arr, m, k));
    }

    @Test public void test() {
        test(new int[] {1, 2, 4, 4, 4, 4}, 1, 3, true);
        test(new int[] {1, 2, 1, 2, 1, 1, 1, 3}, 2, 2, true);
        test(new int[] {1, 2, 1, 2, 1, 3}, 2, 3, false);
        test(new int[] {1, 2, 3, 1, 2}, 2, 2, false);
        test(new int[] {2, 2, 2, 2}, 2, 3, false);
        test(new int[] {2, 2, 2, 2}, 2, 3, false);
        test(new int[] {1, 3, 4, 2, 1, 4, 2, 1}, 3, 2, true);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
