import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC661: https://leetcode.com/problems/image-smoother/
//
// Given a 2D integer matrix M representing the gray scale of an image, you need
// to design a smoother to make the gray scale of each cell becomes the average
// gray scale (rounding down) of all the 8 surrounding cells and itself. If a
// cell has less than 8 surrounding cells, then use as many as you can.
public class ImageSmoother {
    // beats 47.58%(23 ms for 202 tests)
    public int[][] imageSmoother(int[][] M) {
        int m = M.length;
        int n = M[0].length;
        int[][] res = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int count = 0;
                int sum = 0;
                for (int p = i - 1; p <= i + 1; p++) {
                    for (int q = j - 1; q <= j + 1; q++) {
                        if (p >= 0 && p < m && q >= 0 && q < n) {
                            count++;
                            sum += M[p][q];
                        }
                    }
                }
                res[i][j] = sum / count;
            }
        }
        return res;
    }

    // beats 51.89%(22 ms for 202 tests)
    public int[][] imageSmoother2(int[][] M) {
        int m = M.length;
        int n = M[0].length;
        int[][] dirs = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 }, 
                         { -1, -1 }, { 1, 1 }, { -1, 1 }, { 1, -1 } };
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int sum = M[i][j];
                int count = 1;
                for (int k = 0; k < dirs.length; k++) {
                    int x = i + dirs[k][0], y = j + dirs[k][1];
                    if (x >= 0 && x < m && y >= 0 && y < n) {
                        sum += (M[x][y] & 0xFF);
                        count++;
                    }
                }
                M[i][j] |= ((sum / count) << 8);
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                M[i][j] >>= 8;
            }
        }
        return M;
    }

    void test(int[][] M, int[][] expected) {
        assertArrayEquals(expected, imageSmoother(M));
        assertArrayEquals(expected, imageSmoother2(M));
    }

    @Test
    public void test() {
        test(new int[][] { { 2, 3, 4 }, { 5, 6, 7 }, { 8, 9, 10 }, { 11, 12, 13 }, { 14, 15, 16 } },
             new int[][] { { 4, 4, 5 }, { 5, 6, 6 }, { 8, 9, 9 }, { 11, 12, 12 }, { 13, 13, 14 } });
    }

    public static void main(String[] args) {
        String clazz = 
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
