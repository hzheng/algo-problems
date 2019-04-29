import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1030: https://leetcode.com/problems/matrix-cells-in-distance-order/
//
// We are given a matrix with R rows and C columns has cells with integer 
// coordinates (r, c), where 0 <= r < R and 0 <= c < C.
// Additionally, we are given a cell in that matrix with coordinates (r0, c0).
// Return the coordinates of all cells in the matrix, sorted by their distance 
// from (r0, c0) from smallest distance to largest distance.  Here, the distance
// between two cells (r1, c1) and (r2, c2) is the Manhattan distance,
// |r1 - r2| + |c1 - c2|.
public class MatrixCellsInDistanceOrder {
    // Sort
    // time complexity: O(R ^ C * log(R * C)), space complexity: O(log(R * C))
    // 43 ms(26.90%), 39.1 MB(100%) for 66 tests
    public int[][] allCellsDistOrder(int R, int C, int r0, int c0) {
        int[][] points = new int[R * C][2];
        for (int i = 0, k = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                points[k++] = new int[]{i, j};
            }
        }
        Arrays.sort(points,
                    (x, y) -> Math.abs(x[0] - r0) + Math.abs(x[1] - c0) - Math.abs(y[0] - r0)
                              - Math.abs(y[1] - c0));
        return points;
    }

    // Counting Sort
    // time complexity: O(R ^ C), space complexity: O(R + C)
    // 4 ms(99.77%), 39.4 MB(100%) for 66 tests
    public int[][] allCellsDistOrder2(int R, int C, int r0, int c0) {
        int[] counter = new int[R + C + 1];
        for (int r = 0; r < R; r++) {
            for (int c = 0; c < C; c++) {
                int dist = Math.abs(r - r0) + Math.abs(c - c0);
                counter[dist + 1] += 1;
            }
        }
        for (int i = 1; i < counter.length; i++) {
            counter[i] += counter[i - 1];
        }

        int[][] res = new int[R * C][];
        for (int r = 0; r < R; r++) {
            for (int c = 0; c < C; c++) {
                int dist = Math.abs(r - r0) + Math.abs(c - c0);
                res[counter[dist]++] = new int[]{r, c};
            }
        }
        return res;
    }

    // SortedMap
    // time complexity: O(R ^ C * log(R * C)), space complexity: O(log(R * C))
    // 46 ms(19.70%), 39 MB(100%) for 66 tests
    public int[][] allCellsDistOrder3(int R, int C, int r0, int c0) {
        SortedMap<Integer, List<int[]>> map = new TreeMap<>();
        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                int distance = Math.abs(r0 - i) + Math.abs(c0 - j);
                map.computeIfAbsent(distance, a -> new ArrayList<>()).add(new int[]{i, j});
            }
        }
        int[][] res = new int[R * C][2];
        int i = 0;
        for (Integer key : map.keySet()) {
            for (int[] val : map.get(key)) {
                res[i++] = val;
            }
        }
        return res;
    }

    // BFS + Queue
    // time complexity: O(R ^ C), space complexity: O(R + C)
    // 14 ms(59.91%), 38.9 MB(100%) for 66 tests
    public int[][] allCellsDistOrder4(int R, int C, int r0, int c0) {
        boolean[][] visited = new boolean[R][C];
        int[][] res = new int[R * C][2];
        int i = 0;
        Queue<int[]> queue = new LinkedList<>();
        for (queue.offer(new int[]{r0, c0}); !queue.isEmpty(); ) {
            int[] cell = queue.poll();
            int r = cell[0];
            int c = cell[1];
            if (r < 0 || r >= R || c < 0 || c >= C || visited[r][c]) {
                continue;
            }

            res[i++] = cell;
            visited[r][c] = true;
            queue.offer(new int[]{r - 1, c});
            queue.offer(new int[]{r, c - 1});
            queue.offer(new int[]{r, c + 1});
            queue.offer(new int[]{r + 1, c});
        }
        return res;
    }

    void test(int R, int C, int r0, int c0, int[][] expected) {
        assertArrayEquals(expected, allCellsDistOrder(R, C, r0, c0));
        assertArrayEquals(expected, allCellsDistOrder2(R, C, r0, c0));
        assertArrayEquals(expected, allCellsDistOrder3(R, C, r0, c0));
        assertArrayEquals(expected, allCellsDistOrder4(R, C, r0, c0));
    }

    @Test public void test() {
        test(1, 2, 0, 0, new int[][]{{0, 0}, {0, 1}});
        test(2, 3, 1, 2,
             new int[][]{{1, 2}, {0, 2}, {1, 1}, {0, 1}, {1, 0}, {0, 0}});
        test(3, 4, 1, 2,
             new int[][]{{1, 2}, {0, 2}, {1, 1}, {1, 3}, {2, 2}, {0, 1}, {0, 3}, {1, 0}, {2, 1},
                         {2, 3}, {0, 0}, {2, 0}});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
