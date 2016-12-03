import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC354: https://leetcode.com/problems/russian-doll-envelopes/
//
// You have a number of envelopes with widths and heights given as a pair of
// integers (w, h). One envelope can fit into another if and only if both the
// width and height of one envelope is greater than the width and height of the
// other envelope. What is the maximum number of envelopes can you Russian doll?
public class RussianDollEnvelopes {
    // beats 22.64%(564 ms)
    // time complexity: O(N ^ 2), space complexity: O(N)
    public int maxEnvelopes(int[][] envelopes) {
        Arrays.sort(envelopes, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return a[0] - b[0];
            }
        });

        int n = envelopes.length;
        int[] lens = new int[n];
        for (int i = 0; i < n; i++) {
            int max = 0;
            int[] envelope = envelopes[i];
            for (int j = 0; j < i; j++) {
                if (envelopes[j][1] < envelope[1]
                    && envelopes[j][0] < envelope[0]) {
                    max = Math.max(max, lens[j]);
                }
            }
            lens[i] = max + 1;
        }
        int max = 0;
        for (int len : lens) {
            max = Math.max(max, len);
        }
        return max;
    }

    // Solution of Choice
    // Dynamic Programming + Binary Search(LIS problem)
    // time complexity: O(N ^ log(N)), space complexity: O(N)
    // beats 91.03%(19 ms for 85 tests)
    public int maxEnvelopes2(int[][] envelopes) {
        Arrays.sort(envelopes, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                int diff = a[0] - b[0];
                // notice: b[1] - a[1] not a[1] - b[1]
                return diff == 0 ? (b[1] - a[1]) : diff;
            }
        });
        int n = envelopes.length;
        int[] seq = new int[n];
        int len = 0;
        for (int[] envelope : envelopes) {
            int index = Arrays.binarySearch(seq, 0, len, envelope[1]);
            if (index < 0) {
                index = -(index + 1);
            }
            seq[index] = envelope[1];
            if (index == len) {
                len++;
            }
        }
        return len;
    }

    // Dynamic Programming
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 20.29%(562 ms for 85 tests)
    public int maxEnvelopes3(int[][] envelopes) {
        Arrays.sort(envelopes, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) { return a[0] - b[0]; }
        });
        int n = envelopes.length;
        int[] dp = new int[n];
        int len = 0;
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            for (int j = 0; j < i; j++) {
                if (envelopes[i][0] > envelopes[j][0]
                       && envelopes[i][1] > envelopes[j][1]) {
                    dp[i] = Math.max(dp[i], 1 + dp[j]);
                }
            }
            len = Math.max(len, dp[i]);
        }
        return len;
    }

    void test(int[][] envelopes, int expected) {
        assertEquals(expected, maxEnvelopes(envelopes.clone()));
        assertEquals(expected, maxEnvelopes2(envelopes.clone()));
        assertEquals(expected, maxEnvelopes3(envelopes.clone()));
    }

    @Test
    public void test1() {
        test(new int[][] {{30, 50}, {12, 2}, {3, 4}, {12, 15}}, 3);
        test(new int[][] {{3, 4}, {10, 2}, {12, 3}, {30, 50}}, 3);
        test(new int[][] {{5, 4}, {6, 4}, {6, 7}, {2, 3}}, 3);
        test(new int[][] {{4, 5}, {4, 6}, {6, 7}, {2, 3}, {1, 1}}, 4);
        test(new int[][] {{5, 4}, {6, 4}, {1, 2}, {6, 7}, {2, 3}}, 4);
        test(new int[][] {{2, 10}, {3, 20}, {4, 30}, {5, 50}, {5, 40},
                          {5, 25}, {6, 37}, {6, 36}, {7, 38}}, 5);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RussianDollEnvelopes");
    }
}
