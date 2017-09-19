import org.junit.Test;
import static org.junit.Assert.*;

// LC531: https://leetcode.com/problems/lonely-pixel-i/
//
// Given a picture consisting of black and white pixels, find the number of black
// lonely pixels. The picture is represented by a 2D char array consisting of 'B'
// and 'W', which means black and white pixels respectively.
// A black lonely pixel is character 'B' that located at a specific position
// where the same row and same column don't have any other black pixels.
public class LonelyPixel {
    // beats 100%(34 ms for 75 tests)
    public int findLonelyPixel(char[][] picture) {
        int m = picture.length;
        int n = picture[0].length;
        int[] candidates = new int[m];
        for (int i = 0; i < m; i++) {
            candidates[i] = -1;
            for (int j = 0; j < n; j++) {
                if (picture[i][j] == 'B') {
                    if (candidates[i] < 0) {
                        candidates[i] = j;
                    } else {
                        candidates[i] = -1;
                        break;
                    }
                }
            }
        }
        int res = 0;
outer:
        for (int i = 0; i < m; i++) {
            int col = candidates[i];
            if (col < 0) continue;

            for (int j = 0; j < m; j++) {
                if (j != i && picture[j][col] == 'B') {
                    continue outer;
                }
            }
            res++;
        }
        return res;
    }

    // beats 100%(32 ms for 75 tests)
    public int findLonelyPixel2(char[][] picture) {
        int m = picture.length;
        int n = picture[0].length;
        int[] rows = new int[m];
        int[] cols = new int[n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (picture[i][j] == 'B') {
                    rows[i]++;
                    cols[j]++;
                }
            }
        }
        int res = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (picture[i][j] == 'B' && rows[i] == 1 && cols[j] == 1) {
                    res++;
                }
            }
        }
        return res;
    }

    // beats 100%(23 ms for 75 tests)
    public int findLonelyPixel3(char[][] picture) {
        int m = picture.length;
        int n = picture[0].length;
        int[] rows = new int[m];
        int[] cols = new int[n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (picture[i][j] == 'B') {
                    cols[j]++;
                    rows[i] = (rows[i] == 0) ? (j + 1) : -1;
                }
            }
        }
        int res = 0;
        for (int row : rows) {
            if (row > 0 && cols[row - 1] == 1) {
                res++;
            }
        }
        return res;
    }

    // TODO: DFS

    void test(char[][] picture, int expected) {
        assertEquals(expected, findLonelyPixel(picture));
        assertEquals(expected, findLonelyPixel2(picture));
        assertEquals(expected, findLonelyPixel3(picture));
    }

    @Test
    public void test() {
        test(new char[][] {{'W', 'W', 'B'}, {'W', 'B', 'W'}, {'B', 'W', 'W'}}, 3);
        test(new char[][] {{'W', 'W', 'B', 'W'}, {'W', 'B', 'W', 'B'}, {'B', 'W', 'W', 'W'}}, 2);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LonelyPixel");
    }
}
