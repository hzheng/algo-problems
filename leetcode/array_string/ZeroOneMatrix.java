import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/01-matrix/
//
// Given a matrix consists of 0 and 1, find the distance of the nearest 0 for each cell.
public class ZeroOneMatrix {
    static final int[][] MOVES = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};

    // BFS + Queue
    // beats 12.59%(58 ms for 48 tests)
    public List<List<Integer> > updateMatrix(List<List<Integer> > matrix) {
        int m = matrix.size();
        if (m == 0) return Collections.emptyList();

        int n = matrix.get(0).size();
        int[][] cells = new int[m][n];
        int i = 0;
        Queue<int[]> queue = new LinkedList<>();
        for (List<Integer> row : matrix) {
            int j = 0;
            for (int data : row) {
                if (data == 0) {
                    queue.offer(new int[] {i, j});
                } else {
                    cells[i][j] = Integer.MAX_VALUE;
                }
                j++;
            }
            i++;
        }
        while (!queue.isEmpty()) {
            int[] point = queue.poll();
            int dist = cells[point[0]][point[1]];
            for (int[] move : MOVES) {
                int x = point[0] + move[0];
                int y = point[1] + move[1];
                if (x >= 0 && x < m && y >= 0 && y < n && cells[x][y] > dist + 1) {
                    cells[x][y] = dist + 1;
                    queue.offer(new int[] {x, y});
                }
            }
        }
        return convert(cells);
    }

    private List<List<Integer> > convert(int[][] cells) {
        List<List<Integer> > res = new ArrayList<>();
        for (int[] row : cells) {
            List<Integer> list = new ArrayList<>();
            for (int r : row) {
                list.add(r);
            }
            res.add(list);
        }
        return res;
    }

    // DFS + Recursion
    // beats 33.14%(46 ms for 48 tests)
    public List<List<Integer> > updateMatrix2(List<List<Integer> > matrix) {
        int m = matrix.size();
        if (m == 0) return Collections.emptyList();

        int n = matrix.get(0).size();
        int[][] cells = new int[m][n];
        int i = 0;
        for (List<Integer> row : matrix) {
            int j = 0;
            for (int data : row) {
                if (data == 0) {
                } else if (hasZeroNeighbor(matrix, m, n, i, j)) {
                    cells[i][j] = 1; // Will be TLE if omitted
                } else {
                    cells[i][j] = Integer.MAX_VALUE;
                }
                j++;
            }
            i++;
        }
        for (i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (cells[i][j] <= 1) {
                    dfs(cells, i, j);
                }
            }
        }
        return convert(cells);
    }

    private boolean hasZeroNeighbor(List<List<Integer> > matrix, int m, int n, int row, int col) {
        for (int[] move : MOVES) {
            int x = row + move[0];
            int y = col + move[1];
            if (x >= 0 && x < m && y >= 0 && y < n && matrix.get(x).get(y) == 0) return true;
        }
        return false;
    }

    private void dfs(int[][] cells, int row, int col) {
        int dist = cells[row][col];
        for (int[] move : MOVES) {
            int x = row + move[0];
            int y = col + move[1];
            if (x >= 0 && x < cells.length && y >= 0 && y < cells[0].length && cells[x][y] > dist + 1) {
                cells[x][y] = dist + 1;
                dfs(cells, x, y);
            }
        }
    }

    // beats 41.89%(44 ms for 48 tests)
    public List<List<Integer> > updateMatrix3(List<List<Integer> > matrix) {
        int m = matrix.size();
        if (m == 0) return Collections.emptyList();

        int n = matrix.get(0).size();
        int[][] cells = new int[m][n];
        int i = 0;
        for (List<Integer> row : matrix) {
            int j = 0;
            for (int data : row) {
                if (data != 0) {
                    int left = j >= 1 ? cells[i][j - 1] : Integer.MAX_VALUE - 1;
                    int top = i >= 1 ? cells[i - 1][j] : Integer.MAX_VALUE - 1;
                    cells[i][j] = Math.min(Integer.MAX_VALUE - 1, Math.min(top, left) + 1);
                }
                j++;
            }
            i++;
        }
        for (i = m - 1; i >= 0; i--) { // reverse traverse
            for (int j = n - 1; j >= 0; j--) {
                if (cells[i][j] > 1) {
                    int down = i < m - 1 ? cells[i + 1][j] : Integer.MAX_VALUE - 1;
                    int right = j < n - 1 ? cells[i][j + 1] : Integer.MAX_VALUE - 1;
                    cells[i][j] = Math.min(cells[i][j], Math.min(down, right) + 1);
                }
            }
        }
        return convert(cells);
    }

    // beats 41.89%(44 ms for 48 tests)
    public List<List<Integer> > updateMatrix4(List<List<Integer> > matrix) {
        int m = matrix.size();
        int n = matrix.get(0).size();
        for (List<Integer> row : matrix) {
            for (int j = 0; j < n; j++) {
                row.set(j, row.get(j) * 10000);
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                relax(matrix, m, n, i, j, i, j - 1);
                relax(matrix, m, n, i, n - 1 - j, i, n - j);
                relax(matrix, m, n, i, j, i - 1, j);
                relax(matrix, m, n, m - 1 - i, j, m - i, j);
            }
        }
        return matrix;
    }

    private void relax(List<List<Integer>> matrix, int m, int n,
                       int row1, int col1, int row2, int col2) {
        if (row2 >= 0 && row2 < m && col2 >= 0 && col2 < n) {
            matrix.get(row1).set(col1,
            Math.min(matrix.get(row1).get(col1), matrix.get(row2).get(col2) + 1));
        }
    }

    void test(Function<List<List<Integer> >, List<List<Integer>>> updateMatrix,
              Integer[][] m, Integer[][] expected) {
        List<List<Integer> > matrix = new ArrayList<>();
        for (Integer[] item : m) {
            matrix.add(Arrays.asList(item));
        }
        List<List<Integer> > res = updateMatrix.apply(matrix);
        List<List<Integer> > expectedList = new ArrayList<>();
        for (Integer[] item : expected) {
            expectedList.add(Arrays.asList(item));
        }
        assertEquals(expectedList, res);
    }

    void test(Integer[][] m, Integer[][] expected) {
        ZeroOneMatrix z = new ZeroOneMatrix();
        test(z::updateMatrix, m, expected);
        test(z::updateMatrix2, m, expected);
        test(z::updateMatrix3, m, expected);
        test(z::updateMatrix4, m, expected);
    }

    @Test
    public void test() {
        test(new Integer[][] {{0, 0, 0}, {0, 1, 0}, {1, 1, 1}},
             new Integer[][] {{0, 0, 0}, {0, 1, 0}, {1, 2, 1}});
        test(new Integer[][] {{0, 0, 0}, {0, 1, 0}, {0, 0, 0}},
             new Integer[][] {{0, 0, 0}, {0, 1, 0}, {0, 0, 0}});
        test(new Integer[][] {
            {1, 1, 0, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 0, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 0, 0, 0, 1, 1, 0}, {1, 1, 1, 1, 1, 1, 0, 0, 1, 0},
            {1, 0, 0, 1, 1, 1, 0, 1, 0, 1}, {0, 0, 1, 0, 0, 1, 1, 0, 0, 1},
            {0, 1, 0, 1, 1, 1, 1, 1, 1, 1}, {1, 0, 0, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 1, 1, 1, 1, 0, 1, 1, 1}, {1, 1, 0, 0, 1, 0, 1, 0, 1, 1}
        },
             new Integer[][] {
            {2, 1, 0, 1, 2, 2, 2, 3, 3, 2}, {2, 1, 0, 1, 1, 1, 1, 2, 2, 1},
            {3, 2, 1, 1, 0, 0, 0, 1, 1, 0}, {2, 1, 1, 2, 1, 1, 0, 0, 1, 0},
            {1, 0, 0, 1, 1, 1, 0, 1, 0, 1}, {0, 0, 1, 0, 0, 1, 1, 0, 0, 1},
            {0, 1, 0, 1, 1, 1, 1, 1, 1, 1}, {1, 0, 0, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 1, 1, 2, 1, 0, 1, 1, 1}, {1, 1, 0, 0, 1, 0, 1, 0, 1, 2}
        });
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
