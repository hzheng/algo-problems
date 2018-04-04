import org.junit.Test;
import static org.junit.Assert.*;

// LC798: https://leetcode.com/problems/smallest-rotation-with-highest-score/
//
// Given an array A, we may rotate it by a non-negative integer K so that the
// array becomes A[K], A[K+1], A[K+2], ... A[A.length - 1], A[0], A[1], ...,
// A[K-1].  Afterward, any entries that are less than or equal to their index
// are worth 1 point. For example, if we have [2, 4, 1, 3, 0], and we rotate by
// K = 2, it becomes [1, 3, 0, 2, 4].  This is worth 3 points because 1 > 0
// [no points], 3 > 1 [no points], 0 <= 2 [one point], 2 <= 3 [one point],
// 4 <= 4 [one point].
// Over all possible rotations, return the rotation index K that corresponds to
// the highest score we could receive. If there are multiple answers, return
// the smallest such index K.
// Note:
// A will have length at most 20000.
// A[i] will be in the range [0, A.length].
public class BestRotation {
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 13.86%(582 ms for 36 tests)
    public int bestRotation(int[] A) {
        int n = A.length;
        int[] scores = new int[n];
        for (int i = 0; i < n; i++) {
            for (int k = 0; k <= i - A[i]; k++) {
                scores[k]++;
            }
            for (int k = Math.min(n - 1, i + n - A[i]); k > i; k--) {
                scores[k]++;
            }
        }
        int res = 0;
        int maxScore = scores[0];
        for (int i = 1; i < n; i++) {
            if (scores[i] > maxScore) {
                maxScore = scores[i];
                res = i;
            }
        }
        return res;
    }

    // Interval Stabbing
    // https://leetcode.com/articles/smallest-rotation-with-highest-score/
    // time complexity: O(N), space complexity: O(N)
    // beats 77.23%(9 ms for 36 tests)
    public int bestRotation2(int[] A) {
        int n = A.length;
        int[] bad = new int[n];
        for (int i = 0; i < n; i++) {
            int left = (i - A[i] + 1 + n) % n;
            int right = (i + 1) % n;
            bad[left]--;
            bad[right]++;
            // the following can be omitted since it makes no difference
            /*
            if (A[i] > i) { // or: if (left > right) {
                bad[0]--; // where 0 is paired with right
                bad[n]++; // where n is paired with left
            }
            */
        }
        int res = 0;
        for (int i = 0, cur = 0, max = -n; i < n; ++i) {
            cur += bad[i]; // cumulative sum
            if (cur > max) {
                max = cur;
                res = i;
            }
        }
        return res;
    }

    // Interval Stabbing
    // time complexity: O(N), space complexity: O(N)
    // beats 100%(7 ms for 36 tests)
    public int bestRotation2_2(int[] A) {
        int n = A.length;
        int[] change = new int[n];
        for (int i = 0; i < n; i++) {
            change[(i - A[i] + 1 + n) % n]--;
        }
        int res = 0;
        for (int i = 1; i < n; i++) {
            change[i] += change[i - 1] + 1;
            res = (change[i] > change[res]) ? i : res;
        }
        return res;
    }

    void test(int[] A, int expected) {
        assertEquals(expected, bestRotation(A));
        assertEquals(expected, bestRotation2(A));
        assertEquals(expected, bestRotation2_2(A));
    }

    @Test
    public void test() {
        test(new int[] {2, 3, 1, 4, 0}, 3);
        test(new int[] {1, 3, 0, 2, 4}, 0);
        test(new int[] {1, 6, 3, 5, 0, 2, 4, 7}, 2);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
