import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// L084: Given n non-negative integers representing the histogram's bar height where the width of each bar is 1, find the area of largest rectangle in the histogram.
//
// Given n non-negative integers representing the histogram's bar height where
// the width of each bar is 1, find the area of largest rectangle.
public class LargestRect {
    // Time Limit Exceeded
    // time complexity: O(N ^ 2)
    public int largestRectangleArea(int[] heights) {
        int n = heights.length;
        if (n == 0) return 0;

        int[] area = new int[n];
        for (int i = 0; i < n; i++) {
            area[i] = Math.max(area[i], heights[i]);
            int min = heights[i];
            for (int j = i + 1; j < n; j++) {
                min = Math.min(min, heights[j]);
                area[j] = max(area[j], area[j - 1], min * (j - i + 1));
            }
        }
        return area[n - 1];
    }

    private int max(int a, int b, int c) {
        return Math.max(a, Math.max(b, c));
    }

    static class IndexedHeight implements Comparable<IndexedHeight> {
        int index;
        int height;

        IndexedHeight(int i, int h) {
            index = i;
            height = h;
        }

        public int compareTo(IndexedHeight other) {
            return height - other.height;
        }


        public String toString() {
            return "" + height + "(" + index + ")";
        }
    }

    // divide and conquer
    // Time Limit Exceeded
    public int largestRectangleArea2(int[] heights) {
        PriorityQueue<IndexedHeight> queue = new PriorityQueue<>();
        for (int i = 0; i < heights.length; i++) {
            queue.offer(new IndexedHeight(i, heights[i]));
        }
        return maxArea(queue, heights, 0, heights.length - 1);
    }

    private int maxArea(PriorityQueue<IndexedHeight> queue, int[] heights,
                        int start, int end) {
        if (start > end) return 0;
        if (start == end) return heights[start];

        IndexedHeight min = queue.poll();
        PriorityQueue<IndexedHeight> leftQueue = new PriorityQueue<>();
        PriorityQueue<IndexedHeight> rightQueue = new PriorityQueue<>();
        while (true) {
            IndexedHeight h = queue.poll();
            if (h == null) break;

            if (h.index > min.index) {
                rightQueue.offer(h);
            } else {
                leftQueue.offer(h);
            }
        }
        int leftMax = maxArea(leftQueue, heights, start, min.index - 1);
        int rightMax = maxArea(rightQueue, heights, min.index + 1, end);
        return max(min.height * (end - start + 1), leftMax, rightMax);
    }

    // divide and conque
    // beats 0.14%(53 ms)
    public int largestRectangleArea3(int[] heights) {
        int n = heights.length;
        if (n == 0) return 0;

        PriorityQueue<IndexedHeight> queue = new PriorityQueue<>();
        for (int i = 0; i < n; i++) {
            queue.offer(new IndexedHeight(i, heights[i]));
        }

        int max = 0;
        SortedMap<Integer, Integer> ranges = new TreeMap<>();
        ranges.put(0, n - 1);
        while (true) {
            IndexedHeight h = queue.poll();
            if (h == null) break;

            int rangeStart = ranges.headMap(h.index + 1).lastKey();
            int rangeEnd = ranges.get(rangeStart);
            max = Math.max(max, h.height * (rangeEnd - rangeStart + 1));
            if (rangeStart == rangeEnd) {
                ranges.remove(rangeStart);
            } else {
                if (rangeStart <= h.index - 1) {
                    ranges.put(rangeStart, h.index - 1);
                } else {
                    ranges.remove(rangeStart);
                }
                if (rangeEnd >= h.index + 1) {
                    ranges.put(h.index + 1, rangeEnd);
                }
            }
        }
        return max;
    }

    // http://www.geeksforgeeks.org/largest-rectangular-area-in-a-histogram-set-1/
    // divide and conquer
    // time complexity: O(N * logN)
    // code omitted

    // Solution of Choice
    // Stack
    // http://www.geeksforgeeks.org/largest-rectangle-under-histogram/
    // time complexity: O(N)
    // beats ?%(23 ms)
    public int largestRectangleArea4(int[] heights) {
        Stack<Integer> stack = new Stack<>();
        int maxArea = 0;
        for (int i = 0; i <= heights.length; ) {
            if (stack.empty()
                || i < heights.length && heights[i] > heights[stack.peek()]) {
                stack.push(i++);
            } else {
                int last = stack.pop();
                int width = stack.empty() ? i : (i - stack.peek() - 1);
                maxArea = Math.max(maxArea, heights[last] * width);
            }
        }
        return maxArea;
    }

    // Stack(array form)
    // time complexity: O(N)
    // beats ?%(5 ms)
    public int largestRectangleArea5(int[] heights) {
        int n = heights.length;
        int maxArea = 0;
        int[] stack = new int[n + 1];
        int index = -1;
        for (int i = 0; i <= n; ) {
            int height = (i == n) ? 0 : heights[i];
            if (index < 0 || height > heights[stack[index]]) {
                stack[++index] = i++;
            } else {
                int last = stack[index--];
                int width = (index < 0) ? i : (i - stack[index] - 1);
                maxArea = Math.max(maxArea, heights[last] * width);
            }
        }
        return maxArea;
    }

    void test(int expected, int ... heights) {
        assertEquals(expected, largestRectangleArea(heights));
        assertEquals(expected, largestRectangleArea2(heights));
        assertEquals(expected, largestRectangleArea3(heights));
        assertEquals(expected, largestRectangleArea4(heights));
        assertEquals(expected, largestRectangleArea5(heights));
    }

    @Test
    public void test1() {
        test(10, 2, 1, 5, 6, 2, 3);
        test(9, 0, 1, 2, 3, 4, 5);
        test(110, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17,
             18, 19, 20);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LargestRect");
    }
}
