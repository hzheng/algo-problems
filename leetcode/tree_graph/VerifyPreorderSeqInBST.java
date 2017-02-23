import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC255: https://leetcode.com/problems/verify-preorder-sequence-in-binary-search-tree/
//
// Given an array of numbers, verify whether it is the correct preorder traversal
// sequence of a binary search tree.
// You may assume each number in the sequence is unique.
// Follow up:
// Could you do it using only constant space complexity?
public class VerifyPreorderSeqInBST {
    // Divide & Conquer + Recursion
    // time complexity: O(N * log(N)) space complexity: O(N)
    // beats 9.36%(563 ms for 61 tests)
    public boolean verifyPreorder(int[] preorder) {
        return verify(preorder, 0, preorder.length - 1);
    }

    private boolean verify(int[] preorder, int start, int end) {
        if (start >= end) return true;

        int middle = preorder[start];
        int right = start + 1;
        for (; right <= end && preorder[right] < middle; right++) {}
        for (int i = right; i <= end; i++) {
            if (preorder[i] < middle) return false;
        }
        return verify(preorder, start + 1, right - 1) && verify(preorder, right, end);
    }

    // Stack
    // time complexity: O(N * log(N)) space complexity: O(N)
    // beats 12.39%(532 ms for 61 tests)
    public boolean verifyPreorder2(int[] preorder) {
        int n = preorder.length;
        if (n < 2) return true;

        ArrayDeque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[] {0, n - 1});
        while (!stack.isEmpty()) {
            int[] range = stack.pop();
            int start = range[0];
            int end = range[1];
            int middle = preorder[start];
            int right = start + 1;
            for (; right < n && preorder[right] < middle; right++) {}
            for (int i = right; i <= end; i++) {
                if (preorder[i] < middle) return false;
            }
            if (start + 2 < right) {
                stack.push(new int[] {start + 1, right - 1});
            }
            if (right < end) {
                stack.push(new int[] {right, end});
            }
        }
        return true;
    }

    // Stack
    // time complexity: O(N * log(N)) space complexity: O(N)
    // beats 79.11%(20 ms for 61 tests)
    public boolean verifyPreorder3(int[] preorder) {
        int min = Integer.MIN_VALUE;
        ArrayDeque<Integer> stack = new ArrayDeque<>();
        for (int i : preorder) {
            if (i < min) return false;

            while (!stack.isEmpty() && i > stack.peek()) {
                min = stack.pop();
            }
            stack.push(i);
        }
        return true;
    }

    // Stack(Modified input)
    // time complexity: O(N * log(N)) space complexity: O(1)
    // beats 79.11%(20 ms for 61 tests)
    public boolean verifyPreorder3_2(int[] preorder) {
        int min = Integer.MIN_VALUE;
        int index = -1;
        for (int i : preorder) {
            if (i < min) return false;

            while (index >= 0 && i > preorder[index]) {
                min = preorder[index--];
            }
            preorder[++index] = i;
        }
        return true;
    }

    // time complexity: O(N ^ 2) space complexity: O(1)
    // beats 81.27%(6 ms for 61 tests)
    public boolean verifyPreorder4(int[] preorder) {
        int n = preorder.length;
        int min = Integer.MIN_VALUE;
        for (int i = 1, prev = n > 0 ? preorder[0] : 0; ; i++) {
            if (i >= n) return true;

            int cur = preorder[i];
            if (cur <= min) return false;

            if (cur < preorder[i - 1]) { // deeper left
            } else if (cur > prev) { // right subtree of prev
                min = prev;
                prev = cur;
            } else { // left subtree of prev
                for (int j = i - 2;; j--) {
                    if (preorder[j] > cur) {
                        min = preorder[j + 1];
                        break;
                    }
                }
            }
        }
    }

    void test(int[] preorder, boolean expected) {
        assertEquals(expected, verifyPreorder(preorder));
        assertEquals(expected, verifyPreorder2(preorder));
        assertEquals(expected, verifyPreorder3(preorder));
        assertEquals(expected, verifyPreorder3_2(preorder.clone()));
        assertEquals(expected, verifyPreorder4(preorder));
    }

    @Test
    public void test1() {
        test(new int[] {}, true);
        test(new int[] {1}, true);
        test(new int[] {-4, -3, -5}, false);
        test(new int[] {4, 1, 3, 2}, true);
        test(new int[] {4, 2, 3, 1}, false);
        test(new int[] {1, 3, 4, 2}, false);
        test(new int[] {4, 3, 2, 7, 5}, true);
        test(new int[] {4, 3, 2, 5, 7}, true);
        test(new int[] {4, 2, 1, 3, 6, 5, 7}, true);
        test(new int[] {4, 2, 1, 3, 5, 6, 7}, true);
        test(new int[] {10, 7, 4, 8, 6, 40, 23}, false);
        test(new int[] {12, 8, 2, 3, 7, 4, 5, 9, 6}, false);
        test(new int[] {12, 9, 8, 3, 7, 5, 6}, true);
        test(new int[] {12, 9, 8, 3, 7, 5, 6}, true);
        test(new int[] {12, 9, 8, 3, 7, 6, 5, 4, 10, 15}, true);
        test(new int[] {11, 9, 8, 6, 5, 4, 3, 2, 1, 7, 10, 15}, true);
        test(new int[] {12, 9, 8, 3, 7, 6, 5, 4, 2, 10, 15}, false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("VerifyPreorderSeqInBST");
    }
}
