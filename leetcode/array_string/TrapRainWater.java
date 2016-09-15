import java.util.*;
import java.math.BigInteger;

import org.junit.Test;
import static org.junit.Assert.*;

// LC042: https://leetcode.com/problems/trapping-rain-water/
//
// Given n non-negative integers representing an elevation map where the width
// of each bar is 1, compute how much water it is able to trap after raining.
public class TrapRainWater {
    // Stack
    // time complexity: O(N), space complexity: O(N)
    // beats 6.83%(6 ms)
    public int trap(int[] height) {
        int len = height.length;
        if (len == 0) return 0;

        int water = 0;
        int base = height[0];
        Stack<Integer> leftIndices = new Stack<>();
        for (int i = 1; i < len; i++) {
            int curH = height[i];
            if (curH < base) {
                leftIndices.push(i - 1);
                base = curH;
            } else if (curH > base) {
                if (leftIndices.isEmpty()) {
                    base = curH;
                    continue;
                }

                // can hold water
                int left = leftIndices.peek();
                if (curH < height[left]) {
                    water += (i - left - 1) * (curH - base);
                    base = curH;
                } else {
                    water += (i - left - 1) * (height[left] - base);
                    base = height[leftIndices.pop()];
                    i--; // repeat next loop
                }
            }
        }
        return water;
    }

    // Two Pointers(3-pass)
    // http://www.geeksforgeeks.org/trapping-rain-water/
    // time complexity: O(N), space complexity: O(N)
    // beats 16.67%(2 ms)
    public int trap2(int[] height) {
        int len = height.length;
        if (len == 0) return 0;

        int[] left = new int[len];
        left[0] = height[0];
        for (int i = 1; i < len; i++) {
            left[i] = Math.max(left[i - 1], height[i]);
        }

        int[] right = new int[len];
        right[len - 1] = height[len - 1];
        for (int i = len - 2; i >= 0; i--) {
            right[i] = Math.max(right[i + 1], height[i]);
        }

        int water = 0;
        for (int i = 0; i < len; i++) {
            water += Math.min(left[i], right[i]) - height[i];
        }
        return water;
    }

    // Two Pointers(2-pass)
    // time complexity: O(N), space complexity: O(N)
    // beats 16.67%(2 ms)
    public int trap3(int[] height) {
        int len = height.length;
        if (len == 0) return 0;

        int[] left = new int[len];
        left[0] = height[0];
        for (int i = 1; i < len; i++) {
            left[i] = Math.max(left[i - 1], height[i]);
        }

        int water = 0;
        int right = height[len - 1];
        for (int i = len - 2; i >= 0; i--) {
            right = Math.max(right, height[i]);
            water += Math.min(left[i], right) - height[i];
        }
        return water;
    }

    // Two Pointers(1-pass)
    // https://discuss.leetcode.com/topic/18731/7-lines-c-c
    // time complexity: O(N), space complexity: O(1)
    // beats 16.67%(2 ms)
    public int trap4(int[] height) {
        int water = 0;
        for (int l = 0, r = height.length - 1, level = 0; l < r; ) {
            int lower = height[height[l] < height[r] ? l++ : r--];
            level = Math.max(level, lower);
            water += level - lower;
        }
        return water;
    }

    // Solution of Choice
    // Two Pointers(1-pass)
    // time complexity: O(N), space complexity: O(1)
    // beats 16.67%(2 ms)
    public int trap5(int[] height) {
        int water = 0;
        for (int l = 0, r = height.length - 1, maxL = 0, maxR = 0; l < r; ) {
            maxL = Math.max(maxL, height[l]);
            maxR = Math.max(maxR, height[r]);
            water += (maxL < maxR) ? (maxL - height[l++]) : (maxR - height[r--]);
        }
        return water;
    }

    // Stack
    // time complexity: O(N), space complexity: O(N)
    // Largest Rectangle in Histogram???
    // beats %(7 ms)
    public int trap6(int[] height) {
        Stack<Integer> leftPos = new Stack<>();
        int water = 0;
        for (int i = 0; i < height.length; ) {
            if (leftPos.isEmpty() || height[i] <= height[leftPos.peek()]) {
                leftPos.push(i++);
            } else {
                int base = leftPos.pop();
                if (!leftPos.isEmpty()) {
                    int left = leftPos.peek();
                    int depth = Math.min(height[left], height[i]) - height[base];
                water += depth * (i - left - 1);
                }
            }
        }
        return water;
    }

    void test(int expected, int ... height) {
        assertEquals(expected, trap(height));
        assertEquals(expected, trap2(height));
        assertEquals(expected, trap3(height));
        assertEquals(expected, trap4(height));
        assertEquals(expected, trap5(height));
        assertEquals(expected, trap6(height));
    }

    @Test
    public void test1() {
        test(6, 0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1);
        test(6, 0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2);
        test(5, 0, 1, 0, 2, 1, 0, 1, 3, 2, 1);
        test(8, 0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 3);
        test(27, 1, 7, 90, 3, 30);
    }

    @Test
    public void test2() {
        test(47, 1, 7, 90, 3, 30, 40);
        test(5228, 107, 740, 9013, 300, 2980, 4254, 9);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("TrapRainWater");
    }
}
