import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1584: https://leetcode.com/problems/min-cost-to-connect-all-points/
//
// You are given an array points representing integer coordinates of some points on a 2D-plane,
// where points[i] = [xi, yi]. The cost of connecting two points [xi, yi] and [xj, yj] is the
// manhattan distance between them: |xi - xj| + |yi - yj|, where |val| denotes the absolute value of
// val. Return the minimum cost to make all points connected. All points are connected if there is
// exactly one simple path between any two points.
// Constraints:
// 1 <= points.length <= 1000
// -10^6 <= xi, yi <= 10^6
// All pairs (xi, yi) are distinct.
public class MinCostConnectPoints {
    // Union Find + Heap (Kruskal's algorithm)
    // time complexity: O(N^2), space complexity: O(N^2)
    // 100 ms(73.36%), 60.6 MB(5.01%) for 72 tests
    public int minCostConnectPoints(int[][] points) {
        int n = points.length;
        int[][] dist = new int[n][n];
        PriorityQueue<int[]> pq =
                new PriorityQueue<>(Comparator.comparingInt(a -> dist[a[0]][a[1]]));
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                dist[i][j] = Math.abs(points[i][0] - points[j][0]) + Math
                        .abs(points[i][1] - points[j][1]);
                pq.offer(new int[] {i, j});
            }
        }
        int res = 0;
        int[] id = new int[n];
        Arrays.fill(id, -1);
        for (int components = n; !pq.isEmpty() && components > 1; ) {
            int[] p = pq.poll();
            if (union(id, p[0], p[1])) {
                res += dist[p[0]][p[1]];
                components--;
            }
        }
        return res;
    }

    private boolean union(int[] id, int x, int y) {
        x = root(id, x);
        y = root(id, y);
        if (x == y) { return false; }

        id[y] = x;
        return true;
    }

    private int root(int[] id, int x) {
        for (; id[x] >= 0; x = id[x]) {}
        return x;
    }

    // Prim's algorithm
    // time complexity: O(N^2), space complexity: O(N)
    // 22 ms(97.58%), 38.6 MB(5.14%) for 72 tests
    public int minCostConnectPoints2(int[][] points) {
        int n = points.length;
        int res = 0;
        int[] dist = new int[n];
        for (int i = 1; i < n; i++) {
            dist[i] = distance(points, 0, i);
        }
        boolean[] visited = new boolean[n];
        visited[0] = true;
        for (int next = -1, count = 1; count < n; count++, next = -1) {
            for (int i = 0; i < n; i++) {
                if (!visited[i] && (next < 0 || dist[i] < dist[next])) {
                    next = i;
                }
            }
            visited[next] = true;
            res += dist[next];
            for (int i = 0; i < n; i++) {
                if (!visited[i]) {
                    dist[i] = Math.min(dist[i], distance(points, next, i));
                }
            }
        }
        return res;
    }

    private int distance(int[][] points, int a, int b) {
        return Math.abs(points[a][0] - points[b][0]) + Math.abs(points[a][1] - points[b][1]);
    }

    private void test(int[][] points, int expected) {
        assertEquals(expected, minCostConnectPoints(points));
        assertEquals(expected, minCostConnectPoints2(points));
    }

    @Test public void test() {
        test(new int[][] {{0, 0}, {2, 2}, {3, 10}, {5, 2}, {7, 0}}, 20);
        test(new int[][] {{3, 12}, {-2, 5}, {-4, 1}}, 18);
        test(new int[][] {{0, 0}, {1, 1}, {1, 0}, {-1, 1}}, 4);
        test(new int[][] {{-1000000, -1000000}, {1000000, 1000000}}, 4000000);
        test(new int[][] {{0, 0}}, 0);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
