import org.junit.Test;
import static org.junit.Assert.*;

// LC836: https://leetcode.com/problems/rectangle-overlap/
//
// A rectangle is represented as a list [x1, y1, x2, y2], where (x1, y1) are the
// coordinates of its bottom-left corner, and (x2, y2) are the coordinates of
// its top-right corner. Two rectangles overlap if the area of their
// intersection is positive. Given two rectangles, return whether they overlap.
public class RectangleOverlap {
    // beats %(4 ms for 43 tests)
    public boolean isRectangleOverlap(int[] rec1, int[] rec2) {
        if (rec1[0] > rec2[0]) return isRectangleOverlap(rec2, rec1);

        if (rec2[0] >= rec1[2]) return false;

        return (rec1[1] <= rec2[1]) ? (rec2[1] < rec1[3]) : (rec1[1] < rec2[3]);
    }

    // beats %(4 ms for 43 tests)
    public boolean isRectangleOverlap2(int[] rec1, int[] rec2) {
        return !(rec1[2] <= rec2[0] // left
                 || rec1[3] <= rec2[1] // bottom
                 || rec1[0] >= rec2[2] // right
                 || rec1[1] >= rec2[3]); // top
    }

    // beats %(5 ms for 43 tests)
    public boolean isRectangleOverlap3(int[] rec1, int[] rec2) {
        return (Math.min(rec1[2], rec2[2]) > Math.max(rec1[0], rec2[0])
                && Math.min(rec1[3], rec2[3]) > Math.max(rec1[1], rec2[1]));
    }

    void test(int[] rec1, int[] rec2, boolean expected) {
        assertEquals(expected, isRectangleOverlap(rec1, rec2));
        assertEquals(expected, isRectangleOverlap2(rec1, rec2));
        assertEquals(expected, isRectangleOverlap3(rec1, rec2));
    }

    @Test
    public void test() {
        test(new int[] { 0, 0, 2, 2 }, new int[] { 1, 1, 3, 3 }, true);
        test(new int[] { 0, 0, 1, 1 }, new int[] { 1, 0, 2, 1 }, false);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
