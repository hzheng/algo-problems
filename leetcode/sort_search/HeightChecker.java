import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1051: https://leetcode.com/problems/height-checker/
//
// Students are asked to stand in non-decreasing order of heights for an annual photo. Return the
// minimum number of students not standing in the right positions.  (This is the number of students
// that must move in order for all students to be standing in non-decreasing order of height.)
// Note:
// 1 <= heights.length <= 100
// 1 <= heights[i] <= 100
public class HeightChecker {
    // Sort
    // time complexity: O(N * log(N)), space complexity: O(N)
    // 1 ms(99.39%), 34.1 MB(100%) for 79 tests
    public int heightChecker(int[] heights) {
        int[] sorted = heights.clone();
        Arrays.sort(sorted);
        int res = 0;
        for (int i = 0; i < heights.length; i++) {
            if (heights[i] != sorted[i]) {
                res++;
            }
        }
        return res;
    }

    // Count Sort
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 33.6 MB(100%) for 79 tests
    public int heightChecker2(int[] heights) {
        final int MAX = 100;
        int[] count = new int[MAX + 1];
        for (int h : heights) {
            count[h]++;
        }
        for (int i = 1; i <= MAX; i++) {
            count[i] += count[i - 1];
        }
        int res = 0;
        for (int i = 0; i < heights.length; i++) {
            if (i >= count[heights[i]] || i < count[heights[i] - 1]) {
                res++;
            }
        }
        return res;
    }

    void test(int[] heights, int expected) {
        assertEquals(expected, heightChecker(heights));
        assertEquals(expected, heightChecker2(heights));
    }

    @Test
    public void test() {
        test(new int[]{1, 1, 4, 2, 1, 3}, 3);
        test(new int[]{1, 7, 5, 4, 2, 1, 3, 12, 7, 18}, 8);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
