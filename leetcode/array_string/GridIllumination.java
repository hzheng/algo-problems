import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1001: https://leetcode.com/problems/grid-illumination/
//
// You are given a grid of size N x N, and each cell of this grid has a lamp that is initially
// turned off. You are also given an array of lamp positions lamps, where lamps[i] = [rowi, coli]
// indicates that the lamp at grid[rowi][coli] is turned on. When a lamp is turned on, it
// illuminates its cell and all other cells in the same row, column, or diagonal. Finally, you are
// given a query array queries, where queries[i] = [rowi, coli]. For the ith query, determine
// whether grid[rowi][coli] is illuminated or not. After answering the ith query, turn off the lamp
// at grid[rowi][coli] and its 8 adjacent lamps if they exist. A lamp is adjacent if its cell shares
// either a side or corner with grid[rowi][coli]. Return an array of integers ans, where ans[i]
// should be 1 if the lamp in the ith query was illuminated, or 0 if the lamp was not.
//
// Constraints:
// 1 <= N <= 10^9
// 0 <= lamps.length <= 20000
// lamps[i].length == 2
// 0 <= lamps[i][j] < N
// 0 <= queries.length <= 20000
// queries[i].length == 2
// 0 <= queries[i][j] < N
public class GridIllumination {
    // Hash Table
    // time complexity: O(L+Q), space complexity: O(L+Q)
    // 62 ms(98.39%), 50.5 MB(99.10%) for 38 tests
    public int[] gridIllumination(int N, int[][] lamps, int[][] queries) {
        Grid grid = new Grid(N);
        for (int[] lamp : lamps) {
            grid.turnOn(lamp[0], lamp[1]);
        }
        int n = queries.length;
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            int[] query = queries[i];
            res[i] = grid.isOn(query[0], query[1]) ? 1 : 0;
            grid.turnOff(query[0], query[1]);
        }
        return res;
    }

    private static class Grid {
        private final int n;
        private final Set<Integer> lightSources = new HashSet<>();
        private final Map<Integer, Integer> row = new HashMap<>();
        private final Map<Integer, Integer> column = new HashMap<>();
        private final Map<Integer, Integer> diagonal = new HashMap<>();
        private final Map<Integer, Integer> backDiagonal = new HashMap<>();

        public Grid(int n) {
            this.n = n;
        }

        public void turnOn(int x, int y) {
            lightSources.add(code(x, y));
            row.put(x, row.getOrDefault(x, 0) + 1);
            column.put(y, column.getOrDefault(y, 0) + 1);
            diagonal.put(x - y, diagonal.getOrDefault(x - y, 0) + 1);
            backDiagonal.put(x + y, backDiagonal.getOrDefault(x + y, 0) + 1);
        }

        public void turnOff(int x, int y) {
            for (int dx = -1; dx < 2; dx++) {
                for (int dy = -1; dy < 2; dy++) {
                    int nx = x + dx;
                    int ny = y + dy;
                    if (nx < 0 || nx >= n || ny < 0 || ny >= n) { continue; }

                    if (lightSources.remove(code(nx, ny))) {
                        row.put(nx, row.getOrDefault(nx, 0) - 1);
                        column.put(ny, column.getOrDefault(ny, 0) - 1);
                        diagonal.put(nx - ny, diagonal.getOrDefault(nx - ny, 0) - 1);
                        backDiagonal.put(nx + ny, backDiagonal.getOrDefault(nx + ny, 0) - 1);
                    }
                }
            }
        }

        private int code(int x, int y) {
            return x * n + y;
        }

        public boolean isOn(int x, int y) {
            return row.getOrDefault(x, 0) > 0 || column.getOrDefault(y, 0) > 0
                   || diagonal.getOrDefault(x - y, 0) > 0
                   || backDiagonal.getOrDefault(x + y, 0) > 0;
        }
    }

    private void test(int N, int[][] lamps, int[][] queries, int[] expected) {
        assertArrayEquals(expected, gridIllumination(N, lamps, queries));
    }

    @Test public void test() {
        test(5, new int[][] {{0, 0}, {4, 4}}, new int[][] {{1, 1}, {1, 0}}, new int[] {1, 0});
        test(5, new int[][] {{0, 0}, {4, 4}}, new int[][] {{1, 1}, {1, 1}}, new int[] {1, 1});
        test(5, new int[][] {{0, 0}, {0, 4}}, new int[][] {{0, 4}, {0, 1}, {1, 4}},
             new int[] {1, 1, 0});
        test(100, new int[][] {{7, 55}, {53, 61}, {2, 82}, {67, 85}, {81, 75}, {38, 91}, {68, 0},
                               {60, 43}, {40, 19}, {12, 75}, {26, 2}, {24, 89}, {42, 81}, {60, 58},
                               {77, 72}, {33, 24}, {19, 93}, {7, 16}, {58, 54}, {78, 57}, {97, 49},
                               {65, 16}, {42, 75}, {90, 50}, {89, 34}, {76, 97}, {58, 23}, {62, 47},
                               {94, 28}, {88, 65}, {3, 87}, {81, 10}, {12, 81}, {44, 81}, {54, 92},
                               {90, 54}, {17, 54}, {27, 82}, {48, 15}, {8, 46}, {4, 99}, {15, 13},
                               {90, 77}, {2, 87}, {18, 33}, {52, 90}, {4, 95}, {57, 61}, {31, 22},
                               {32, 8}, {49, 26}, {24, 65}, {88, 55}, {88, 38}, {64, 76}, {94, 76},
                               {59, 12}, {41, 46}, {80, 28}, {38, 36}, {65, 67}, {75, 37}, {56, 97},
                               {83, 57}, {2, 4}, {44, 43}, {71, 90}, {62, 40}, {79, 94}, {81, 11},
                               {96, 34}, {38, 11}, {22, 3}, {54, 96}, {78, 33}, {54, 54}, {79, 98},
                               {1, 28}, {0, 32}, {37, 11}},
             new int[][] {{24, 84}, {95, 68}, {80, 35}, {31, 53}, {69, 45}, {85, 29}, {87, 25},
                          {42, 47}, {7, 59}, {99, 3}, {31, 70}, {64, 62}, {44, 91}, {55, 25},
                          {15, 52}, {95, 33}, {21, 29}, {61, 34}, {93, 34}, {79, 27}, {30, 86},
                          {52, 0}, {18, 10}, {5, 1}, {40, 21}, {11, 48}, {55, 94}, {22, 42},
                          {81, 0}, {39, 43}, {5, 25}, {43, 29}, {45, 47}, {83, 93}, {77, 70},
                          {22, 63}, {30, 73}, {18, 48}, {39, 88}, {91, 47}},
             new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1,
                        1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 1});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
