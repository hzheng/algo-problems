import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC757: https://leetcode.com/problems/set-intersection-size-at-least-two/
//
// An integer interval [a, b] (for integers a < b) is a set of all consecutive integers from a to b,
// including a and b. Find the minimum size of a set S such that for every integer interval A in
// intervals, the intersection of S with A has size at least 2.
//
// Note:
// intervals will have length in range [1, 3000].
// intervals[i] will have length 2, representing some integer interval.
// intervals[i][j] will be an integer in [0, 10^8].
public class IntersectionSizeTwo {
    // Sort + Greedy
    // time complexity: O(N^2), space complexity: O(N)
    // 26 ms(10.95%), 40 MB(6.43%) for 113 tests
    public int intersectionSizeTwo(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> a[0] != b[0] ? a[0] - b[0] : b[1] - a[1]);
        int n = intervals.length;
        int[] need = new int[n];
        Arrays.fill(need, 2);
        int res = 0;
        for (int i = n - 1; i >= 0; i--) {
            int start = intervals[i][0];
            int end = start + need[i];
            for (int x = start; x < end; x++) {
                for (int j = 0; j <= i; j++) {
                    if (need[j] > 0 && x <= intervals[j][1]) {
                        need[j]--;
                    }
                }
                res++;
            }
        }
        return res;
    }

    // Sort + Greedy
    // time complexity: O(N^log(N)), space complexity: O(log(N))
    // 7 ms(97.62%), 40 MB(6.43%) for 113 tests
    public int intersectionSizeTwo2(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> a[1] != b[1] ? a[1] - b[1] : b[0] - a[0]);
        int res = 0;
        int largest = -1;
        int second = -1;
        for (int[] interval : intervals) {
            int start = interval[0];
            int end = interval[1];
            if (start > largest) {
                res += 2;
                second = end - 1;
                largest = end;
            } else if (start > second) {
                res++;
                second = largest;
                largest = end;
            }
        }
        return res;
    }

    private void test(int[][] intervals, int expected) {
        assertEquals(expected, intersectionSizeTwo(intervals));
        assertEquals(expected, intersectionSizeTwo2(intervals));
    }

    @Test public void test() {
        test(new int[][] {{6, 21}, {1, 15}, {15, 20}, {10, 21}, {0, 7}}, 4);
        test(new int[][] {{1, 3}, {1, 4}, {2, 5}, {3, 5}}, 3);
        test(new int[][] {{7, 8}, {0, 14}, {3, 11}}, 2);
        test(new int[][] {{1, 2}}, 2);
        test(new int[][] {{1, 2}, {2, 3}, {2, 4}, {4, 5}}, 5);
        test(new int[][] {{1, 3}, {100, 103}}, 4);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
