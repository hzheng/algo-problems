import org.junit.Test;
import static org.junit.Assert.*;

// LC492: https://leetcode.com/problems/construct-the-rectangle/
//
// Given a specific rectangular web page’s area, your job by now is to design a
// rectangular web page, whose length and width satisfy the following requirements:
// 1. The area of the rectangular web page you designed must equal to the given target area.
// 2. The width W should not be larger than the length L, which means L >= W.
// 3. The difference between length L and width W should be as small as possible.
public class ConstructRectangle {
    // beats 60.68%(5 ms for 50 tests)
    public int[] constructRectangle(int area) {
        for (int i = (int)Math.sqrt(area); ; i--) {
            if (area % i == 0) return new int[] {area / i, i};
        }
    }

    void test(int area, int ... expected) {
        assertArrayEquals(expected, constructRectangle(area));
    }

    @Test
    public void test() {
        test(4, 2, 2);
        test(6, 3, 2);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ConstructRectangle");
    }
}
