import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC992: https://leetcode.com/problems/subarrays-with-k-different-integers/
//
// Given an array A of positive integers, call a (contiguous, not necessarily distinct) subarray of
// A good if the number of different integers in that subarray is exactly K.
// Return the number of good subarrays of A.
//
// Note:
// 1 <= A.length <= 20000
// 1 <= A[i] <= A.length
// 1 <= K <= A.length
public class SubarraysWithKDistinct {
    // Sliding Window + Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 18 ms(85.45%), 42.2 MB(83.83%) for 55 tests
    public int subarraysWithKDistinct(int[] A, int K) {
        Map<Integer, Integer> lastSeen = new HashMap<>();
        int n = A.length;
        int res = 0;
        for (int i = 0, j = 0; j < n; j++) {
            lastSeen.put(A[j], j);
            if (lastSeen.size() < K) { continue; }

            for (; lastSeen.size() > K; i++) {
                if (lastSeen.get(A[i]) == i) {
                    lastSeen.remove(A[i]);
                }
            }
            res++;
            for (int k = i; lastSeen.get(A[k]) != k; res++, k++) {}
        }
        return res;
    }

    // Sliding Window + Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 33 ms(73.90%), 42.3 MB(80.37%) for 55 tests
    public int subarraysWithKDistinct2(int[] A, int K) {
        int res = 0;
        Window window1 = new Window();
        Window window2 = new Window();
        for (int left1 = 0, left2 = 0, right = 0, n = A.length; right < n; right++) {
            int a = A[right];
            window1.add(a);
            window2.add(a);
            while (window1.diffCount() > K) {
                window1.remove(A[left1++]);
            }
            while (window2.diffCount() >= K) {
                window2.remove(A[left2++]);
            }
            res += left2 - left1;
        }
        return res;
    }

    private static class Window {
        private final Map<Integer, Integer> count = new HashMap<>();

        public void add(int a) {
            count.put(a, count.getOrDefault(a, 0) + 1);
        }

        public void remove(int a) {
            int cnt = count.get(a) - 1;
            if (cnt == 0) {
                count.remove(a);
            } else {
                count.put(a, cnt);
            }
        }

        private int diffCount() {
            return count.size();
        }
    }

    // Sliding Window + Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 3 ms(100.00%), 42.4 MB(76.10%) for 55 tests
    public int subarraysWithKDistinct3(int[] A, int K) {
        int res = 0;
        int n = A.length;
        int[] map = new int[n + 1];
        for (int i = 0, j = 0, avail = K, prefix = 0; j < n; j++) {
            if (map[A[j]]++ == 0) {
                avail--;
            }
            if (avail < 0) {
                map[A[i++]]--;
                avail++;
                prefix = 0;
            }
            if (avail == 0) {
                // remove until the number in the front appears only once
                for (; map[A[i]] > 1; prefix++) {
                    map[A[i++]]--;
                }
                res += prefix + 1;
            }
        }
        return res;
    }

    // Sliding Window + Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 43 ms(43.88%), 43 MB(57.27%) for 55 tests
    public int subarraysWithKDistinct4(int[] A, int K) {
        return atMostKDistinct(A, K) - atMostKDistinct(A, K - 1);
    }

    private int atMostKDistinct(int[] A, int K) {
        int res = 0;
        Map<Integer, Integer> count = new HashMap<>();
        for (int i = 0, j = 0, allowed = K, n = A.length; j < n; j++) {
            int a = A[j];
            int cnt = count.getOrDefault(a, 0);
            if (cnt == 0) {
                allowed--;
            }
            count.put(a, cnt + 1);
            for (; allowed < 0; i++) {
                count.put(A[i], count.get(A[i]) - 1);
                if (count.get(A[i]) == 0) {
                    allowed++;
                }
            }
            res += j - i + 1;
        }
        return res;
    }

    private void test(int[] A, int K, int expected) {
        assertEquals(expected, subarraysWithKDistinct(A, K));
        assertEquals(expected, subarraysWithKDistinct2(A, K));
        assertEquals(expected, subarraysWithKDistinct3(A, K));
        assertEquals(expected, subarraysWithKDistinct4(A, K));
    }

    @Test public void test() {
        test(new int[] {1, 1, 1, 1, 1, 1}, 2, 0);
        test(new int[] {2, 1, 2, 1, 2}, 2, 10);
        test(new int[] {2, 1, 1, 1, 2}, 1, 8);
        test(new int[] {1, 2, 1, 2, 3}, 2, 7);
        test(new int[] {1, 2, 1, 3, 4}, 3, 3);
        test(new int[] {1, 2}, 1, 2);
        test(new int[] {5, 7, 5, 2, 3, 3, 4, 1, 5, 2, 7, 4, 6, 2, 3, 8, 4, 5, 7}, 7, 30);
        test(new int[] {2, 2, 1, 2, 1, 2}, 2, 14);
        test(new int[] {2, 2, 1, 2, 1, 2, 3, 2, 1, 3, 2, 2, 1}, 2, 23);
        test(new int[] {2, 2, 1, 2, 1, 2, 3, 2, 1, 3, 2, 2, 1}, 3, 53);
        test(new int[] {1, 2, 2, 2, 2, 2, 1, 1, 1, 1}, 2, 29);
        test(new int[] {1, 2, 2, 2, 2, 2}, 2, 5);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
