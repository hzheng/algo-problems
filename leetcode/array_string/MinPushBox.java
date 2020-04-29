import java.util.*;

import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import static org.junit.Assert.assertEquals;

// LC1263: https://leetcode.com/problems/minimum-moves-to-move-a-box-to-their-target-location/
//
// Storekeeper is a game in which the player pushes boxes around in a warehouse trying to get them
// to target locations. The game is represented by a grid of size m x n, where each element is a
// wall, floor, or a box. Your task is move the box 'B' to the target position 'T' under the
// following rules:
// * Player is represented by character 'S' and can move up, down, left, right in the grid if it is a
// floor (empty cell).
// * Floor is represented by character '.' that means free cell to walk.
// * Wall is represented by character '#' that means obstacle  (impossible to walk there).
// * There is only one box 'B' and one target cell 'T' in the grid.
// * The box can be moved to an adjacent free cell by standing next to the box and then moving in the
// direction of the box. This is a push.
// * The player cannot walk through the box.
// Return the minimum number of pushes to move the box to the target. If there is no way to reach
// the target, return -1.
// Constraints:
// m == grid.length
// n == grid[i].length
// 1 <= m <= 20
// 1 <= n <= 20
// grid contains only characters '.', '#',  'S' , 'T', or 'B'.
// There is only one character 'S', 'B' and 'T' in the grid.
public class MinPushBox {
    private static final int[][] MOVES = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};

    // BFS + Queue
    // time complexity: O(N ^ 2), space complexity: O(1)
    // 83 ms(44.64%), 49 MB(100%) for 18 tests
    public int minPushBox(char[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        int[] box = null;
        int[] target = null;
        int[] player = null;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                switch (grid[i][j]) {
                case 'B':
                    box = new int[] {i, j};
                    break;
                case 'T':
                    target = new int[] {i, j};
                    break;
                case 'S':
                    player = new int[] {i, j};
                    break;
                }
            }
        }
        Queue<Integer> queue = new LinkedList<>();
        int start = encode(box[0], box[1], player[0], player[1]);
        queue.offer(start);
        Map<Integer, Integer> dist = new HashMap<>();
        dist.put(start, 0);
        int res = Integer.MAX_VALUE;
        while (!queue.isEmpty()) {
            int curCode = queue.poll();
            int curDist = dist.get(curCode);
            if (curDist >= res) { continue; }

            int[] cur = decode(curCode);
            if (cur[0] == target[0] && cur[1] == target[1]) {
                res = Math.min(res, curDist);
                continue;
            }
            for (int[] move : MOVES) {
                int sx = cur[2] + move[0];
                int sy = cur[3] + move[1];
                if (sx < 0 || sx >= n || sy < 0 || sy >= m || grid[sx][sy] == '#') { continue; }

                int bx = cur[0];
                int by = cur[1];
                int nextDist = curDist;
                if (sx == cur[0] && sy == cur[1]) { // play meets the box
                    bx += move[0];
                    by += move[1];
                    if (bx < 0 || bx >= n || by < 0 || by >= m || grid[bx][by] == '#') { continue; }

                    nextDist++;
                }
                int nextCode = encode(bx, by, sx, sy);
                if (dist.getOrDefault(nextCode, Integer.MAX_VALUE) > nextDist) {
                    dist.put(nextCode, nextDist);
                    queue.offer(nextCode);
                }
            }
        }
        return (res == Integer.MAX_VALUE) ? -1 : res;
    }

    private int encode(int bx, int by, int sx, int sy) {
        return (bx << 24) | (by << 16) | (sx << 8) | sy;
    }

    private int[] decode(int num) {
        return new int[] {(num >>> 24) & 0xff, (num >>> 16) & 0xff, (num >>> 8) & 0xff, num & 0xff};
    }

    // TODO: DFS

    void test(char[][] grid, int expected) {
        assertEquals(expected, minPushBox(grid));
    }

    @Test public void test() {
        test(new char[][] {{'#', '#', '#', '#', '#', '#'}, {'#', 'T', '#', '#', '#', '#'},
                           {'#', '.', '.', 'B', '.', '#'}, {'#', '.', '#', '#', '.', '#'},
                           {'#', '.', '.', '.', 'S', '#'}, {'#', '#', '#', '#', '#', '#'}}, 3);
        test(new char[][] {{'#', '#', '#', '#', '#', '#'}, {'#', 'T', '#', '#', '#', '#'},
                           {'#', '.', '.', 'B', '.', '#'}, {'#', '#', '#', '#', '.', '#'},
                           {'#', '.', '.', '.', 'S', '#'}, {'#', '#', '#', '#', '#', '#'}}, -1);
        test(new char[][] {{'#', '#', '#', '#', '#', '#'}, {'#', 'T', '.', '.', '#', '#'},
                           {'#', '.', '#', 'B', '.', '#'}, {'#', '.', '.', '.', '.', '#'},
                           {'#', '.', '.', '.', 'S', '#'}, {'#', '#', '#', '#', '#', '#'}}, 5);
        test(new char[][] {{'#', '#', '#', '#', '#', '#', '#'}, {'#', 'S', '#', '.', 'B', 'T', '#'},
                           {'#', '#', '#', '#', '#', '#', '#'}}, -1);
        test(new char[][] {{'#', '.', '.', '#', '#', '#', '#', '#'},
                           {'#', '.', '.', 'T', '#', '.', '.', '#'},
                           {'#', '.', '.', '.', '#', 'B', '.', '#'},
                           {'#', '.', '.', '.', '.', '.', '.', '#'},
                           {'#', '.', '.', '.', '#', '.', 'S', '#'},
                           {'#', '.', '.', '#', '#', '#', '#', '#'}}, 7);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
