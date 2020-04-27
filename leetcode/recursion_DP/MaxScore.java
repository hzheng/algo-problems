import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1423: https://leetcode.com/problems/maximum-points-you-can-obtain-from-cards/
//
// There are several cards arranged in a row, and each card has an associated number of points The
// points are given in the integer array cardPoints. In one step, you can take one card from the
// beginning or from the end of the row. You have to take exactly k cards.
// Your score is the sum of the points of the cards you have taken.
// Given the integer array cardPoints and the integer k, return the maximum score you can obtain.
// Constraints:
// 1 <= cardPoints.length <= 10^5
// 1 <= cardPoints[i] <= 10^4
// 1 <= k <= cardPoints.length
public class MaxScore {
    // Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // 2 ms(93.81%), 50.6 MB(100%) for 40 tests
    public int maxScore(int[] cardPoints, int k) {
        int n = cardPoints.length;
        int[] left = new int[n + 1];
        int[] right = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            left[i] = left[i - 1] + cardPoints[i - 1];
        }
        for (int i = n - 1; i >= 0; i--) {
            right[i] = right[i + 1] + cardPoints[i];
        }
        int res = 0;
        for (int i = 0; i <= k; i++) {
            res = Math.max(res, left[i] + right[n + i - k]);
        }
        return res;
    }

    // Dynamic Programming
    // time complexity: O(K), space complexity: O(1)
    // 1 ms(100%), 48.7 MB(100%) for 40 tests
    public int maxScore2(int[] cardPoints, int k) {
        int res = 0;
        for (int i = 0; i < k; i++) {
            res += cardPoints[i];
        }
        for (int cur = res, end = k - 1; end >= 0; end--) {
            cur -= cardPoints[end];
            cur += cardPoints[cardPoints.length - (k - end)];
            res = Math.max(res, cur);
        }
        return res;
    }

    private void test(int[] cardPoints, int k, int expected) {
        assertEquals(expected, maxScore(cardPoints, k));
        assertEquals(expected, maxScore2(cardPoints, k));
    }

    @Test public void test() {
        test(new int[] {1, 2, 3, 4, 5, 6, 1}, 3, 12);
        test(new int[] {2, 2, 2}, 2, 4);
        test(new int[] {9, 7, 7, 9, 7, 7, 9}, 7, 55);
        test(new int[] {1, 1000, 1}, 1, 1);
        test(new int[] {1, 79, 80, 1, 1, 1, 200, 1}, 3, 202);
        test(new int[] {93, 3432, 20, 2, 1, 79, 80, 23, 1, 1, 1, 200, 1}, 4, 3726);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
