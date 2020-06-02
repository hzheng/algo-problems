import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1465: https://leetcode.com/problems/maximum-area-of-a-piece-of-cake-after-horizontal-and-vertical-cuts/
//
// Given a rectangular cake with height h and width w, and two arrays of integers horizontalCuts and
// verticalCuts where horizontalCuts[i] is the distance from the top of the rectangular cake to the
// ith horizontal cut and similarly, verticalCuts[j] is the distance from the left of the
// rectangular cake to the jth vertical cut.
// Return the maximum area of a piece of cake after you cut at each horizontal and vertical position
// provided in the arrays horizontalCuts and verticalCuts. Since the answer can be a huge number,
// return this modulo 10^9 + 7.
// Constraints:
//
// 2 <= h, w <= 10^9
// 1 <= horizontalCuts.length < min(h, 10^5)
// 1 <= verticalCuts.length < min(w, 10^5)
// 1 <= horizontalCuts[i] < h
// 1 <= verticalCuts[i] < w
// It is guaranteed that all elements in horizontalCuts are distinct.
// It is guaranteed that all elements in verticalCuts are distinct
public class MaxAreaOfCake {
    // Sort
    // time complexity: O(H*log(H)+W*log(W)), space complexity: O(log(H*W))
    // 13 ms(97.15%), 49.5 MB(100.00%) for 53 tests
    private static final int MOD = 1_000_000_007;

    public int maxArea(int h, int w, int[] hc, int[] vc) {
        long maxH = getMax(h, hc);
        long maxV = getMax(w, vc);
        return (int)((maxH * maxV) % MOD);
    }

    private int getMax(int h, int[] cut) {
        Arrays.sort(cut);
        int res = Math.max(cut[0], h - cut[cut.length - 1]);
        for (int i = 1; i < cut.length; i++) {
            res = Math.max(cut[i] - cut[i - 1], res);
        }
        return res;
    }

    void test(int h, int w, int[] hc, int[] vc, int expected) {
        assertEquals(expected, maxArea(h, w, hc, vc));
    }

    @Test public void test() {
        test(5, 4, new int[] {1, 2, 4}, new int[] {1, 3}, 4);
        test(5, 4, new int[] {3, 1}, new int[] {1}, 6);
        test(5, 4, new int[] {3}, new int[] {3}, 9);
        test(6, 5, new int[] {3, 8, 12}, new int[] {2, 9, 15, 23}, 40);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
