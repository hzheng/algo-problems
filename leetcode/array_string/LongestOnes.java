import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1004: https://leetcode.com/problems/max-consecutive-ones-iii/
//
// Given an array A of 0s and 1s, we may change up to K values from 0 to 1.
// Return the length of the longest (contiguous) subarray that contains only 1s.
//
// Note:
// 1 <= A.length <= 20000
// 0 <= K <= A.length
// A[i] is 0 or 1
public class LongestOnes {
    // Sliding Window + Deque
    // time complexity: O(N), space complexity: O(N)
    // 7 ms(9.34%), 44 MB(8.27%) for 48 tests
    public int longestOnes(int[] A, int K) {
        int n = A.length;
        int res = 0;
        Deque<Integer> zeros = new LinkedList<>();
        for (int cur = 0, start = -1; cur < n; cur++) {
            if (A[cur] == 1) {
                res = Math.max(res, cur - start);
                continue;
            }
            if (K == 0) {
                start = cur;
                continue;
            }

            if (zeros.size() == K) {
                start = Math.max(start, zeros.pollFirst());
            }
            zeros.offerLast(cur);
            res = Math.max(res, cur - start);
        }
        return res;
    }

    // Sliding Window
    // time complexity: O(N), space complexity: O(1)
    // 4 ms(15.63%), 67.5 MB(5.40%) for 48 tests
    public int longestOnes2(int[] A, int K) {
        int n = A.length;
        int res = 0;
        for (int cur = 0, start = -1, zero = -1, avail = K; cur < n; cur++) {
            if (A[cur] == 1) {
                res = Math.max(res, cur - start);
                continue;
            }
            if (K == 0) {
                start = cur;
                continue;
            }

            if (--avail < 0) {
                start = zero;
                for (zero++; A[zero] != 0; zero++) {}
            } else if (zero < 0) {
                zero = cur;
            }
            res = Math.max(res, cur - start);
        }
        return res;
    }

    // Solution of Choice
    // Sliding Window
    // time complexity: O(N), space complexity: O(1)
    // 3 ms(33.76%), 67.8 MB(5.40%) for 48 tests
    public int longestOnes3(int[] A, int K) {
        int n = A.length;
        int start = 0;
        int end = 0;
        for (int avail = K; end < n; end++) {
            if (A[end] == 0) {
                avail--;
            }
            if (avail < 0 && A[start++] == 0) {
                avail++;
            }
        }
        return end - start;
    }

    private void test(int[] A, int K, int expected) {
        assertEquals(expected, longestOnes(A, K));
        assertEquals(expected, longestOnes2(A, K));
        assertEquals(expected, longestOnes3(A, K));
    }

    @Test public void test() {
        test(new int[] {1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0}, 2, 6);
        test(new int[] {0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1}, 3, 10);
        test(new int[] {1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0}, 0, 4);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
