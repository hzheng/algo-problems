import org.junit.Test;

import static org.junit.Assert.*;

// LC1769: https://leetcode.com/problems/minimum-number-of-operations-to-move-all-balls-to-each-box/
//
// You have n boxes. You are given a binary string boxes of length n, where boxes[i] is '0' if the
// ith box is empty, and '1' if it contains one ball. In one operation, you can move one ball from
// a box to an adjacent box. Box i is adjacent to box j if abs(i - j) == 1. Note that after doing
// so, there may be more than one ball in some boxes. Return an array answer of size n, where
// answer[i] is the minimum number of operations needed to move all the balls to the ith box.
// Each answer[i] is calculated considering the initial state of the boxes.
//
// Constraints:
// n == boxes.length
// 1 <= n <= 2000
// boxes[i] is either '0' or '1'.
public class MinOperationsToMoveAllBalls {
    // time complexity: O(N^2), space complexity: O(N)
    // 106 ms(33.33%), 39.7 MB(16.67%) for 95 tests
    public int[] minOperations(String boxes) {
        int n = boxes.length();
        int[] res = new int[n];
        char[] s = boxes.toCharArray();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (s[j] == '1') {
                    res[i] += Math.abs(i - j);
                }
            }
        }
        return res;
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // 1 ms(100.00%), 39.8 MB(16.67%) for 95 tests
    public int[] minOperations2(String boxes) {
        int n = boxes.length();
        int[] res = new int[n];
        char[] s = boxes.toCharArray();
        int[] rightOnes = new int[n + 1];
        for (int i = n - 1, ones = 0; i >= 0; i--) {
            rightOnes[i] = rightOnes[i + 1] + ones;
            ones += (s[i] - '0');
        }
        for (int i = 0, moves = 0, ones = 0; i < n; i++) {
            moves += ones;
            ones += (s[i] - '0');
            res[i] = moves + rightOnes[i];
        }
        return res;
    }

    // Solution of Choice
    // Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // 1 ms(100.00%), 39.4 MB(83.33%) for 95 tests
    public int[] minOperations3(String boxes) {
        int n = boxes.length();
        int[] res = new int[n];
        char[] s = boxes.toCharArray();
        for (int i = 0, moves = 0, ones = 0; i < n; i++) {
            res[i] += moves;
            ones += (s[i] - '0');
            moves += ones;
        }
        for (int i = n - 1, moves = 0, ones = 0; i >= 0; i--) {
            res[i] += moves;
            ones += (s[i] - '0');
            moves += ones;
        }
        return res;
    }

    private void test(String boxes, int[] expected) {
        assertArrayEquals(expected, minOperations(boxes));
        assertArrayEquals(expected, minOperations2(boxes));
        assertArrayEquals(expected, minOperations3(boxes));
    }

    @Test public void test() {
        test("110", new int[] {1, 1, 3});
        test("001011", new int[] {11, 8, 5, 4, 3, 4});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
