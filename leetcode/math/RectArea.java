import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/rectangle-area/
//
// Find the total area covered by two rectilinear rectangles in a 2D plane.
public class RectArea {
    // beats 69.73%(4 ms)
    public int computeArea(int A, int B, int C, int D, int E, int F, int G, int H) {
        int area1 = (C - A) * (D - B);
        int area2 = (G - E) * (H - F);
        int overlapArea = overlap(A, C, E, G);
        if (overlapArea > 0) {
            overlapArea *= overlap(B, D, F, H);
        }
        return area1 + area2 - overlapArea;
    }

    private int overlap(int left1, int right1, int left2, int right2) {
        // may overflow
        // return Math.max(0, Math.min(right1, right2) - Math.max(left1, left2));

        int rDiff = Math.min(right1, right2);
        int lDiff = Math.max(left1, left2);
        return rDiff > lDiff ? rDiff - lDiff : 0;
    }

    // beats 69.73%(4 ms)
    public int computeArea2(int A, int B, int C, int D, int E, int F, int G, int H) {
        int area = (C - A) * (D - B) +  (G - E) * (H - F);
        if (C < E || G < A || D < F || H < B) return area;
        // the following is slow(5ms)?
        // if (C <= E || G <= A || D <= F || H <= B) return area;

        int right = Math.min(C, G);
        int left = Math.max(A, E);
        int top = Math.min(H, D);
        int bottom = Math.max(F, B);
        return area - (right - left) * (top - bottom);
    }

    void test(int A, int B, int C, int D, int E, int F, int G, int H, int expected) {
        assertEquals(expected, computeArea(A, B, C, D, E, F, G, H));
        assertEquals(expected, computeArea2(A, B, C, D, E, F, G, H));
    }

    @Test
    public void test1() {
        test(-1500000001, 0, -1500000000, 1, 1500000000, 0, 1500000001, 1, 2);
        test(0, 0, 0, 0, -1, -1, 1, 1, 4);
        test(-3, 0, 3, 4, 0, -1, 9, 2, 45);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RectArea");
    }
}
