import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1725: https://leetcode.com/problems/number-of-rectangles-that-can-form-the-largest-square/
//
// You are given an array rectangles where rectangles[i] = [li, wi] represents the ith rectangle of
// length li and width wi. You can cut the ith rectangle to form a square with a side length of k if
// both k <= li and k <= wi. For example, if you have a rectangle [4,6], you can cut it to get a
// square with a side length of at most 4. Let maxLen be the side length of the largest square you
// can obtain from any of the given rectangles.
// Return the number of rectangles that can make a square with a side length of maxLen.
//
// Constraints:
// 1 <= rectangles.length <= 1000
// rectangles[i].length == 2
// 1 <= li, wi <= 109
// li != wi
public class CountGoodRectangles {
    // time complexity: O(N), space complexity: O(1)
    // 2 ms(80.00%), 39.4 MB(60.00%) for 68 tests
    public int countGoodRectangles(int[][] rectangles) {
        int max = 0;
        for (int[] r : rectangles) {
            max = Math.max(max, Math.min(r[0], r[1]));
        }
        int res = 0;
        for (int[] r : rectangles) {
            if (max == Math.min(r[0], r[1])) {
                res++;
            }
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // 1 ms(100.00%), 39.4 MB(60.00%) for 68 tests
    public int countGoodRectangles2(int[][] rectangles) {
        int max = 0;
        int res = 0;
        for (int[] r : rectangles) {
            int cur = Math.min(r[0], r[1]);
            if (cur > max) {
                res = 1;
                max = cur;
            } else if (cur == max) {
                res++;
            }
        }
        return res;
    }

    private void test(int[][] rectangles, int expected) {
        assertEquals(expected, countGoodRectangles(rectangles));
        assertEquals(expected, countGoodRectangles2(rectangles));
    }

    @Test public void test() {
        test(new int[][] {{5, 8}, {3, 9}, {5, 12}, {16, 5}}, 3);
        test(new int[][] {{2, 3}, {3, 7}, {4, 3}, {3, 7}}, 3);
        test(new int[][] {{12, 4}, {3, 7}, {4, 3}, {3, 7}, {4, 8}}, 2);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
