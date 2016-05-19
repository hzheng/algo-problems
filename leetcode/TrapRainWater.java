import java.util.*;
import java.math.BigInteger;

import org.junit.Test;
import static org.junit.Assert.*;

// Given n non-negative integers representing an elevation map where the width
// of each bar is 1, compute how much water it is able to trap after raining.
public class TrapRainWater {
    // beats 6.83%
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

    void test(int expected, int ... height) {
        assertEquals(expected, trap(height));
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
