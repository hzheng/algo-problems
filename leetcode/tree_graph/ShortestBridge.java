import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC934: https://leetcode.com/problems/shortest-bridge/
//
// In a given 2D binary array A, there are two islands.  (An island is a 
// 4-directionally connected group of 1s not connected to any other 1s.)
// Now, we may change 0s to 1s so as to connect the two islands together to form
// 1 island. Return the smallest number of 0s that must be flipped.  (It is 
// guaranteed that the answer is at least 1.)
// Note:
// 1 <= A.length = A[0].length <= 100
// A[i][j] == 0 or A[i][j] == 1
public class ShortestBridge {
    private static final int[][] MOVES = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

    // DFS + Recursion + Set
    // beats %(492 ms for 96 tests)
    public int shortestBridge(int[][] A) {
        int n = A.length;
        Set<Integer> island1 = new HashSet<>();
        outer: for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (A[i][j] != 0) {
                    dfs(A, i, j, island1);
                    break outer;
                }
            }
        }
        Set<Integer> island2 = new HashSet<>();
        outer: for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (A[i][j] != 0 && !island1.contains(n * i + j)) {
                    dfs(A, i, j, island2);
                    break outer;
                }
            }
        }
        int res = n * n;
        for (int a : island1) {
            for (int b : island2) {
                res = Math.min(res, dist(a / n, a % n, b / n, b % n));
            }
        }
        return res - 1;
    }

    private void dfs(int[][] A, int x, int y, Set<Integer> island) {
        int n = A.length;
        island.add(n * x + y);
        for (int[] m : MOVES) {
            int nextX = x + m[0];
            int nextY = y + m[1];
            if (nextX >= 0 && nextX < n && nextY >= 0 && nextY < n && A[nextX][nextY] == 1) {
                if (!island.contains(n * nextX + nextY)) {
                    dfs(A, nextX, nextY, island);
                }
            }
        }
    }

    private int dist(int ax, int ay, int bx, int by) {
        return Math.abs(ax - bx) + Math.abs(ay - by);
    }

    // DFS + Stack + BFS + Queue + Set
    // beats %(67 ms for 96 tests)
    public int shortestBridge2(int[][] A) {
        int n = A.length;
        int[][] colors = getComponents(A);
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                if (colors[x][y] == 1) {
                    int val = x * n + y;
                    visited.add(val);
                    queue.offer(val);
                }
            }
        }
        for (int flips = 0;; flips++) {
            for (int i = queue.size(); i > 0; i--) {
                for (int nei : neighbors(A, queue.poll())) {
                    int x = nei / n;
                    int y = nei % n;
                    if (colors[x][y] == 0) {
                        queue.offer(nei);
                        colors[x][y] = 1;
                    } else if (colors[x][y] == 2) return flips;
                }
            }
        }
    }

    private int[][] getComponents(int[][] A) {
        int n = A.length;
        int[][] colors = new int[n][n];
        for (int x = 0, color = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                if (colors[x][y] != 0 || A[x][y] == 0) continue;

                Stack<Integer> stack = new Stack<>();
                stack.push(x * n + y);
                colors[x][y] = ++color;
                while (!stack.isEmpty()) {
                    for (int nei : neighbors(A, stack.pop())) {
                        int nx = nei / n;
                        int ny = nei % n;
                        if (A[nx][ny] == 1 && colors[nx][ny] == 0) {
                            colors[nx][ny] = color;
                            stack.push(nei);
                        }
                    }
                }
            }
        }
        return colors;
    }

    private List<Integer> neighbors(int[][] A, int pos) {
        int n = A.length;
        int x = pos / n;
        int y = pos % n;
        List<Integer> res = new ArrayList<>();
        if (x > 0) {
            res.add((x - 1) * n + y);
        }
        if (y > 0) {
            res.add(x * n + (y - 1));
        }
        if (x + 1 < n) {
            res.add((x + 1) * n + y);
        }
        if (y + 1 < n) {
            res.add(x * n + (y + 1));
        }
        return res;
    }

    void test(int[][] A, int expected) {
        assertEquals(expected, shortestBridge(A));
        assertEquals(expected, shortestBridge2(A));
    }

    @Test
    public void test() {
        test(new int[][] {{0, 1}, {1, 0}}, 1);
        test(new int[][] {{0, 1, 0}, {0, 0, 0}, {0, 0, 1}}, 2);
        test(new int[][] {{1, 1, 1, 1, 1}, {1, 0, 0, 0, 1}, {1, 0, 1, 0, 1}, {1, 0, 0, 0, 1},
                          {1, 1, 1, 1, 1}},
             1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
