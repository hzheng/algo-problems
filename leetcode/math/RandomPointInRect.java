import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC497: https://leetcode.com/problems/random-point-in-non-overlapping-rectangles/
//
// Given a list of non-overlapping axis-aligned rectangles rects, write a
// function which randomly and uniformily picks an integer point in the space
// covered by the rectangles.
// Note:
// A point on the perimeter of a rectangle is included in the space covered by
// the rectangles.
// ith rectangle = rects[i] = [x1,y1,x2,y2], where [x1, y1] are the coordinates 
// of the bottom-left corner, and [x2, y2] are the ones of the top-right corner.
// length and width of each rectangle does not exceed 2000.
// 1 <= rects.length <= 100
// pick return a point as an array of integer coordinates [p_x, p_y]
// pick is called at most 10000 times.
public class RandomPointInRect {
    static interface IRandomPointInRect {
        public int[] pick();
    }

    // TreeMap
    // beats 86.42%(143 ms for 35 tests)
    static class RandomPointInRect1 implements IRandomPointInRect {
        private int[][] rects;
        private NavigableMap<Integer, Integer> map = new TreeMap<>();
        private Random random = new Random();
        private int sum = 0;

        public RandomPointInRect1(int[][] rects) {
            this.rects = rects;
            int i = 0;
            for (int[] rect : rects) {
                sum += (rect[2] - rect[0] + 1) * (rect[3] - rect[1] + 1);
                map.put(sum, i++);
            }
        }

        public int[] pick() {
            int randInt = random.nextInt(sum) + 1;
            int s = map.ceilingKey(randInt);
            int[] rect = rects[map.get(s)];
            int left = rect[0];
            int bottom = rect[1];
            int right = rect[2];
            int top = rect[3];
            int index = s - randInt;
            // or : int index = (right - left + 1) * (top - bottom + 1) - s + randInt - 1;
            int x = left + index % (right - left + 1);
            int y = bottom + index / (right - left + 1);
            return new int[] { x, y };
        }
    }

    // void test(, int expected) {
    // assertEquals(expected, f());
    // }

    // TODO: add tests
    @Test
    public void test() {
        // test();
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
