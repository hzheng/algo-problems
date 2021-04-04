import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1812: https://leetcode.com/problems/determine-color-of-a-chessboard-square/
//
// You are given coordinates, a string that represents the coordinates of a square of the
// chessboard. Below is a chessboard for your reference.
// Return true if the square is white, and false if the square is black.
// The coordinate will always represent a valid chessboard square. The coordinate will always have
// the letter first, and the number second.
//
// Constraints:
// coordinates.length == 2
// 'a' <= coordinates[0] <= 'h'
// '1' <= coordinates[1] <= '8'
public class SquareIsWhite {
    // time complexity: O(1), space complexity: O(1)
    // 0 ms(%), 37 MB(%) for 203 tests
    public boolean squareIsWhite(String c) {
        return (c.charAt(0) - 'a') % 2 == (c.charAt(1) - '0') % 2;
    }

    // time complexity: O(1), space complexity: O(1)
    // 0 ms(%), 37.2 MB(%) for 203 tests
    public boolean squareIsWhite2(String c) {
        return c.charAt(0) % 2 != c.charAt(1) % 2;
    }

    private void test(String c, boolean expected) {
        assertEquals(expected, squareIsWhite(c));
        assertEquals(expected, squareIsWhite2(c));
    }

    @Test public void test() {
        test("a1", false);
        test("h3", true);
        test("c7", false);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
