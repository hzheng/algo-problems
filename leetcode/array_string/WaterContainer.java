import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC011: https://leetcode.com/problems/container-with-most-water/
//
// Given n non-negative integers a[1], a[2], ..., a[n], where each represents a
// point at coordinate (i, a[i]). n vertical lines are drawn such that the two
// endpoints of line i is at (i, a[i]) and (i, 0). Find 2 lines, which together
// with x-axis forms a container, s.t. the container contains the most water.
public class WaterContainer {
    // beats 71.89%(5 ms)
    public int maxArea(int[] height) {
        int n = height.length;
        int start = 0;
        for (; start < n && height[start] <= 0; start++) {}
        int end = n - 1;
        for (; end >= 0 && height[end] <= 0; end--) {}
        if (end == start) return 0;

        List<Point> leftPoints = new ArrayList<>();
        int maxHeight = 0;
        for (int i = start; i < end; i++) {
            int h = height[i];
            if (h > maxHeight) {
                leftPoints.add(new Point(i, h));
                maxHeight = h;
            }
        }
        int m = leftPoints.size();
        Point[] lefts = leftPoints.toArray(new Point[m]);
        int maxLeft = leftPoints.get(m - 1).x;
        int maxArea = 0;
        maxHeight = 0;
        for (int i = end; i >= maxLeft; i--) {
            int h = height[i];
            if (h <= maxHeight) continue;

            maxHeight = h;
            for (Point p : lefts) {
                maxArea = Math.max(maxArea, p.area(i, h));
            }
        }
        return maxArea;
    }

    private static class Point {
        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int area(int x, int y) {
            return (x - this.x) * Math.min(this.y, y);
        }
    }

    // Two Pointers
    // http://www.programcreek.com/2014/03/leetcode-container-with-most-water-java/
    // beats 12.32%(7 ms)
    public int maxArea2(int[] height) {
        int maxArea = 0;
        for (int left = 0, right = height.length - 1; left < right; ) {
            maxArea = Math.max(maxArea, (right - left)
                               * Math.min(height[left], height[right]));
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return maxArea;
    }

    // Two Pointers
    // beats 88.21%(3 ms)
    public int maxArea3(int[] height) {
        int maxArea = 0;
        int maxLeftH = 0;
        int maxRightH = 0;
        int left = 0;
        int right = height.length - 1;
        while (true) {
            maxArea = Math.max(maxArea, (right - left)
                               * Math.min(height[left], height[right]));
            if (height[left] < height[right]) {
                while (left < right && height[left] <= maxLeftH) left++;
                if (left >= right) break;
                maxLeftH = height[left];
            } else {
                while (left < right && height[right] <= maxRightH) right--;
                if (left >= right) break;
                maxRightH = height[right];
            }
        }
        return maxArea;
    }

    // Solution of Choice
    // Two Pointers
    // beats 88.21%(3 ms)
    public int maxArea4(int[] height) {
        int maxArea = 0;
        for (int i = 0, j = height.length - 1; i < j; ) {
            int h = Math.min(height[i], height[j]);
            maxArea = Math.max(maxArea, (j - i) * h);
            while (height[i] <= h && i < j) i++;
            while (height[j] <= h && i < j) j--;
        }
        return maxArea;
    }

    void test(int[] height, int expected) {
        assertEquals(expected, maxArea(height));
        assertEquals(expected, maxArea2(height));
        assertEquals(expected, maxArea3(height));
        assertEquals(expected, maxArea4(height));
    }

    @Test
    public void test1() {
        test(new int[] {1, 1}, 1);
        test(new int[] {1, 1, 8, 9, 6, 7, 4}, 21);
        test(new int[] {1, 2, 4, 3}, 4);
        test(new int[] {2, 3, 10, 5, 7, 8, 9}, 36);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("WaterContainer");
    }
}
