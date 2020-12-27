import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1705: https://leetcode.com/problems/maximum-number-of-eaten-apples/
//
// There is a special kind of apple tree that grows apples every day for n days. On the ith day, the
// tree grows apples[i] apples that will rot after days[i] days, that is on day i + days[i] the
// apples will be rotten and cannot be eaten. On some days, the apple tree does not grow any apples,
// which are denoted by apples[i] == 0 and days[i] == 0. You decided to eat at most one apple a day
// (to keep the doctors away). Note that you can keep eating after the first n days. Given two
// integer arrays days and apples of length n, return the maximum number of apples you can eat.
//
// Constraints:
// apples.length == n
// days.length == n
// 1 <= n <= 2 * 10^4
// 0 <= apples[i], days[i] <= 2 * 10^4
//days[i] = 0 if and only if apples[i] = 0.
public class EatenApples {
    // Heap
    // time complexity: O(N+A), space complexity: O(N+A)
    // 33 ms(100.00%), 40.9 MB(100.00%) for 63 tests
    public int eatenApples(int[] apples, int[] days) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        int res = 0;
        for (int i = 0, n = apples.length; i < n || !pq.isEmpty(); i++) {
            if (i < n && apples[i] > 0) {
                pq.offer(new int[] {apples[i], i + days[i]});
            }
            for (int[] cur = pq.peek(); cur != null; cur = pq.peek()) {
                if (cur[1] <= i || cur[0]-- == 0) {
                    pq.poll();
                } else {
                    res++;
                    break;
                }
            }
        }
        return res;
    }

    // Heap
    // time complexity: O(N+A), space complexity: O(N+A)
    // 33 ms(100.00%), 40.7 MB(100.00%) for 63 tests
    public int eatenApples2(int[] apples, int[] days) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        int res = 0;
        for (int i = 0, prevI = -1, n = apples.length; i < n || !pq.isEmpty(); i++) {
            if (i > prevI && i < n && apples[i] > 0) {
                pq.offer(new int[] {apples[i], i + days[i]});
            }
            prevI = i;
            int[] cur = pq.peek();
            if (cur == null) { continue; }

            if (cur[1] <= i || cur[0]-- == 0) {
                pq.poll();
                i--;
            } else {
                res++;
            }
        }
        return res;
    }

    private void test(int[] apples, int[] days, int expected) {
        assertEquals(expected, eatenApples(apples, days));
        assertEquals(expected, eatenApples2(apples, days));
    }

    @Test public void test() {
        test(new int[] {3, 1, 1, 0, 0, 2}, new int[] {3, 1, 1, 0, 0, 2}, 5);
        test(new int[] {1, 2, 3, 5, 2}, new int[] {3, 2, 1, 4, 2}, 7);
        test(new int[] {3, 0, 0, 0, 0, 2}, new int[] {3, 0, 0, 0, 0, 2}, 5);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
