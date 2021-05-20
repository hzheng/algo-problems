import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1861: https://leetcode.com/problems/rotating-the-box/
//
// You are given an m x n matrix of characters box representing a side-view of a box. Each cell of
// the box is one of the following:
// A stone '#'
// A stationary obstacle '*'
// Empty '.'
// The box is rotated 90 degrees clockwise, causing some of the stones to fall due to gravity. Each
// stone falls down until it lands on an obstacle, another stone, or the bottom of the box. Gravity
// does not affect the obstacles' positions, and the inertia from the box's rotation does not affect
// the stones' horizontal positions.
// It is guaranteed that each stone in box rests on an obstacle, another stone, or the bottom of the
// box.
// Return an n x m matrix representing the box after the rotation described above.
//
// Constraints:
// m == box.length
// n == box[i].length
// 1 <= m, n <= 500
// box[i][j] is either '#', '*', or '.'.
public class RotateTheBox {
    // time complexity: O(M*N), space complexity: O(M*N)
    // 9 ms(81.59%), 75.2 MB(73.33%) for 87 tests
    public char[][] rotateTheBox(char[][] box) {
        int m = box.length;
        int n = box[0].length;
        char[][] res = new char[n][m];
        for (int i = 0; i < m; i++) {
            for (int j = n - 1; j >= 0; j--) {
                res[j][m - i - 1] = box[i][j];
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = n - 1, base = j; j >= 0; j--) {
                switch (res[j][i]) {
                case '*':
                    base = j - 1;
                    break;
                case '#':
                    res[j][i] = '.';
                    res[base--][i] = '#';
                    break;
                }
            }
        }
        return res;
    }

    // time complexity: O(M*N), space complexity: O(M*N)
    // 8 ms(96.07%), 75 MB(84.74%) for 87 tests
    public char[][] rotateTheBox2(char[][] box) {
        int m = box.length;
        int n = box[0].length;
        char[][] res = new char[n][m];
        for (int i = 0; i < m; i++) {
            for (int j = n - 1, base = n - 1; j >= 0; j--) {
                res[j][m - i - 1] = '.';
                if (box[i][j] != '.') {
                    base = (box[i][j] == '*') ? j : base;
                    res[base--][m - i - 1] = box[i][j];
                }
            }
        }
        return res;
    }

    private void test(char[][] box, char[][] expected) {
        assertArrayEquals(expected, rotateTheBox(box));
        assertArrayEquals(expected, rotateTheBox2(box));
    }

    @Test public void test1() {
        test(new char[][] {{'#', '.', '#'}}, new char[][] {{'.'}, {'#'}, {'#'}});
        test(new char[][] {{'#', '.', '*', '.'}, {'#', '#', '*', '.'}},
             new char[][] {{'#', '.'}, {'#', '#'}, {'*', '*'}, {'.', '.'}});
        test(new char[][] {{'#', '#', '*', '.', '*', '.'}, {'#', '#', '#', '*', '.', '.'},
                           {'#', '#', '#', '.', '#', '.'}},
             new char[][] {{'.', '#', '#'}, {'.', '#', '#'}, {'#', '#', '*'}, {'#', '*', '.'},
                           {'#', '.', '*'}, {'#', '.', '.'}});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
