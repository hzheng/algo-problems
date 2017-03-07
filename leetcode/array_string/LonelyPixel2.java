import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC533: https://leetcode.com/problems/lonely-pixel-ii
//
// Given a picture consisting of black and white pixels, and a positive integer N,
// find the number of black pixels located at some specific row R and column C
// that align with all the following rules:
// Row R and column C both contain exactly N black pixels.
// For all rows that have a black pixel at column C, they should be exactly the same as row R
public class LonelyPixel2 {
    // beats 73.77%(27 ms for 118 tests)
    public int findBlackPixel(char[][] picture, int N) {
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
outer:
            for (int j = 0; j < n; j++) {
                if (cols[j] == N && rows[i] == N && picture[i][j] == 'B') {
                    for (int row = 0; row < m; row++) {
                        if (i == row || picture[row][j] != 'B') continue;
                        if (rows[row] != rows[i]) continue outer;
                        for (int col = 0; col < n; col++) {
                            if (picture[row][col] != picture[i][col]) continue outer;
                        }
                    }
                    res++;
                }
            }
        }
        return res;
    }

    // Hash Table + Set
    // beats 22.68%(55 ms for 118 tests)
    public int findBlackPixel2(char[][] picture, int N) {
        int m = picture.length;
        int n = picture[0].length;
        Map<Integer, Set<Integer> > rows = new HashMap<>();
        Map<Integer, Set<Integer> > cols = new HashMap<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (picture[i][j] == 'B') {
                    Set<Integer> row = rows.get(i);
                    if (row == null) {
                        rows.put(i, row = new HashSet<>());
                    }
                    row.add(j);
                    Set<Integer> col = cols.get(j);
                    if (col == null) {
                        cols.put(j, col = new HashSet<>());
                    }
                    col.add(i);
                }
            }
        }
        int res = 0;
        for (int i = 0; i < m; i++) {
            Set<Integer> row = rows.get(i);
            if (row == null) continue;
outer:
            for (int j = 0; j < n; j++) {
                Set<Integer> col = cols.get(j);
                if (picture[i][j] == 'B' && row.size() == N && col != null
                    && col.size() == N) {
                    for (int c : col) {
                        if (!row.equals(rows.get(c))) continue outer;
                    }
                    res++;
                }
            }
        }
        return res;
    }

    // Hash Table
    // beats 78.14%(26 ms for 118 tests)
    public int findBlackPixel3(char[][] picture, int N) {
        int m = picture.length;
        int n = picture[0].length;
        int[] rows = new int[m];
        int[] cols = new int[n];
        Map<String, Integer> counts = new HashMap<>();
        String[] keys = new String[m];
        for (int i = 0; i < m; i++) {
            String key = keys[i] = String.valueOf(picture[i]);
            counts.put(key, counts.getOrDefault(key, 0) + 1);
            for (int j = 0; j < n; j++) {
                if (picture[i][j] == 'B') {
                    rows[i]++;
                    cols[j]++;
                }
            }
        }
        int res = 0;
        for (int i = 0; i < m; i++) {
            String key = keys[i];
            for (int j = 0; j < n; j++) {
                if (picture[i][j] == 'B') {
                    if (rows[i] == N && cols[j] == N
                        && counts.getOrDefault(key, 0) == N) {
                        res++;
                    }
                }
            }
        }
        return res;
    }

    // Hash Table
    // beats 78.14%(26 ms for 118 tests)
    public int findBlackPixel4(char[][] picture, int N) {
        int m = picture.length;
        int n = picture[0].length;
        Map<String, Integer> map = new HashMap<>();
        int[] cols = new int[n];
        for (int i = 0; i < m; i++) {
            int rows = 0;
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < n; j++) {
                if (picture[i][j] == 'B') {
                    rows++;
                    cols[j]++;
                }
                sb.append(picture[i][j]);
            }
            if (rows == N) {
                String key = sb.toString();
                map.put(key, map.getOrDefault(key, 0) + 1);
            }
        }
        int res = 0;
        for (String key : map.keySet()) {
            if (map.get(key) == N) {
                for (int j = 0; j < n; j++) {
                    if (key.charAt(j) == 'B' && cols[j] == N) {
                        res += N;
                    }
                }
            }
        }
        return res;
    }

    // TODO: DFS?

    void test(String[] picture, int N, int expected) {
        char[][] pics = Arrays.stream(picture).map(x -> x.toCharArray()).toArray(char[][]::new);
        assertEquals(expected, findBlackPixel(pics, N));
        assertEquals(expected, findBlackPixel2(pics, N));
        assertEquals(expected, findBlackPixel3(pics, N));
        assertEquals(expected, findBlackPixel4(pics, N));
    }

    @Test
    public void test() {
        test(new String[] {"WBBWWBWWWWWBBWW", "WBBWWBWWWWWBBWW",
                           "WWWWWBBBWBWWWWB", "WWBWBWWWWBBWBWW",
                           "WBBWWBWWWWWBBWW", "WWBWBWWWWBBWBWW",
                           "WWBWBWWWWBBWBWW", "WWBWBWWWWBBWBWW"}, 5, 0);
        test(new String[] {"WBWBBW", "WBWBBW", "WBWBBW", "BWBWWB"}, 3, 9);
        test(new String[] {"WBWBBW", "WBWBBW", "WBWBBW", "WWBWBW"}, 3, 6);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LonelyPixel2");
    }
}
