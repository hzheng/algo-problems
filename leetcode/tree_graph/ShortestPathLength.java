import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC847: https://leetcode.com/problems/shortest-path-visiting-all-nodes/
//
// An undirected connected graph of N nodes(labeled 0, 1, 2, ..., N-1) is given.
// graph.length = N, and j != i is in the list graph[i] exactly once, if and 
// only if nodes i and j are connected. Return the length of the shortest path
// that visits every node. You may start and stop at any node, you may revisit 
// nodes multiple times, and you may reuse edges.
// Note:
// 1 <= graph.length <= 12
// 0 <= graph[i].length < graph.length
public class ShortestPathLength {
    // DFS + Recursion + Bit Manipulation
    // Time Limit Exceeded
    public int shortestPathLength(int[][] graph) {
        int n = graph.length;
        int[] res = {n * n};
        for (int i = 0, mask = (1 << n) - 1; i < n; i++) {
            pathLength(graph, i, mask, new int[n], 0, res);
        }
        return res[0] - 1;
    }

    private void pathLength(int[][] graph, int cur, int mask, int[] visited, int len, int[] res) {
        if (mask == 0) { // faster than counting non-zeros in visited
            res[0] = Math.min(res[0], len);
            return;
        }
        if (len + Integer.bitCount(mask) >= res[0]) {return;}

        if (visited[cur] >= graph[cur].length) {return;}

        mask &= ~(1 << cur);
        visited[cur]++;
        for (int nei : graph[cur]) {
            pathLength(graph, nei, mask, visited, len + 1, res);
        }
        visited[cur]--;
    }

    // Dynamic Programming + Bit Manipulation
    // time complexity: O(N ^ 3 * 2 ^ N), space complexity: O(N * 2 ^ N)
    // beats %(59 ms for 46 tests)
    public int shortestPathLength2(int[][] graph) {
        int n = graph.length;
        int[][] dp = new int[1 << n][n];
        for (int[] d : dp) {
            Arrays.fill(d, n * n);
        }
        Arrays.fill(dp[0], 0);
        for (int i = 0; i < n; i++) {
            for (int visited = 0; visited < dp.length; visited++) {
                for (int cur = 0; cur < n; cur++) {
                    for (int nei : graph[cur]) {
                        int v = visited | (1 << cur);
                        dp[v][nei] = Math.min(dp[v][nei], dp[visited][cur] + 1);
                    }
                }
            }
        }
        int res = Integer.MAX_VALUE;
        for (int d : dp[dp.length - 1]) {
            res = Math.min(res, d);
        }
        return res - 1;
    }

    // Dynamic Programming + Bit Manipulation
    // time complexity: O(N * 2 ^ N), space complexity: O(N * 2 ^ N)
    // beats %(21 ms for 46 tests)
    public int shortestPathLength3(int[][] graph) {
        int n = graph.length;
        int dp[][] = new int[1 << n][n];
        for (int[] d : dp) {
            Arrays.fill(d, n * n);
        }
        for (int i = 0; i < n; i++) {
            dp[1 << i][i] = 0;
        }
        for (int visited = 0; visited < dp.length; visited++) {
            for (boolean repeat = true; repeat; ) {
                repeat = false;
                for (int cur = 0; cur < n; cur++) {
                    int d = dp[visited][cur];
                    for (int nei : graph[cur]) {
                        int v = visited | (1 << nei);
                        if (d + 1 < dp[v][nei]) {
                            dp[v][nei] = d + 1;
                            repeat |= (visited == v);
                        }
                    }
                }
            }
        }
        int res = n * n;
        for (int d : dp[dp.length - 1]) {
            res = Math.min(res, d);
        }
        return res;
    }

    // Dynamic Programming + BFS + Queue + Bit Manipulation
    // time complexity: O(N * 2 ^ N), space complexity: O(N * 2 ^ N)
    // 24 ms(52.53%), 45.9 MB(34.92%) for 51 tests
    public int shortestPathLength4(int[][] graph) {
        int n = graph.length;
        int[][] dp = new int[n][1 << n];
        Queue<int[]> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            Arrays.fill(dp[i], n * n);
            dp[i][1 << i] = 0;
            queue.offer(new int[] {i, 1 << i});
        }
        while (true) {
            int[] cur = queue.poll();
            int curNode = cur[0];
            int curMask = cur[1];
            int d = dp[curNode][curMask];
            if (curMask == dp[0].length - 1) {return d;}

            for (int nei : graph[curNode]) {
                int v = curMask | (1 << nei);
                if (d + 1 < dp[nei][v]) {
                    dp[nei][v] = d + 1;
                    queue.offer(new int[] {nei, v});
                }
            }
        }
    }

    // BFS + Queue + Bit Manipulation
    // time complexity: O(N * 2 ^ N), space complexity: O(N * 2 ^ N)
    // 87 ms(22.37%), 46.8 MB(29.29%) for 51 tests
    public int shortestPathLength5(int[][] graph) {
        int n = graph.length;
        Set<String> visited = new HashSet<>();
        Queue<Node> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            Node node = new Node(i, 1 << i);
            queue.offer(node);
            visited.add(node.toString());
        }
        for (int fullMask = (1 << n) - 1, level = 0; ; level++) {
            for (int i = queue.size(); i > 0; i--) {
                Node node = queue.poll();
                if (node.mask == fullMask) { return level; }

                for (int nei : graph[node.id]) {
                    Node nextNode = new Node(nei, node.mask | (1 << nei));
                    if (visited.add(nextNode.toString())) {
                        queue.offer(nextNode);
                    }
                }
            }
        }
    }

    class Node {
        int id;
        int mask;

        Node(int id, int mask) {
            this.id = id;
            this.mask = mask;
        }

        public String toString() {
            return id + " " + mask;
        }
    }

    void test(int[][] graph, int expected) {
        assertEquals(expected, shortestPathLength(graph));
        assertEquals(expected, shortestPathLength2(graph));
        assertEquals(expected, shortestPathLength3(graph));
        assertEquals(expected, shortestPathLength4(graph));
        assertEquals(expected, shortestPathLength5(graph));
    }

    @Test public void test() {
        test(new int[][] {{}}, 0);
        test(new int[][] {{1, 2, 3}, {0}, {0}, {0}}, 4);
        test(new int[][] {{1}, {0, 2, 4}, {1, 3, 4}, {2}, {1, 2}}, 4);
        test(new int[][] {{1}, {0, 2, 6}, {1, 3}, {2}, {5}, {4, 6}, {1, 5, 7}, {6}}, 9);
        test(new int[][] {{6, 7}, {6}, {6}, {5, 6}, {6}, {3}, {2, 0, 3, 4, 1}, {0}}, 10);
        test(new int[][] {{2, 3, 6}, {5}, {0, 3}, {0, 2, 4, 7}, {3}, {7, 1}, {0}, {3, 5}}, 8);
        test(new int[][] {{7}, {3}, {3, 9}, {1, 2, 4, 5, 7, 11}, {3}, {3}, {9}, {3, 10, 8, 0}, {7},
                          {11, 6, 2}, {7}, {3, 9}}, 17);
        test(new int[][] {{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}, {0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11},
                          {0, 1, 3, 4, 5, 6, 7, 8, 9, 10, 11}, {0, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11},
                          {0, 1, 2, 3, 5, 6, 7, 8, 9, 10, 11}, {0, 1, 2, 3, 4, 6, 7, 8, 9, 10, 11},
                          {0, 1, 2, 3, 4, 5, 7, 8, 9, 10, 11}, {0, 1, 2, 3, 4, 5, 6, 8, 9, 10, 11},
                          {0, 1, 2, 3, 4, 5, 6, 7, 9, 10, 11}, {0, 1, 2, 3, 4, 5, 6, 7, 8, 10, 11},
                          {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 11}, {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10}},
             11);
        test(new int[][] {{1, 2, 3, 4, 5, 6, 7, 8, 9}, {0, 2, 3, 4, 5, 6, 7, 8, 9},
                          {0, 1, 3, 4, 5, 6, 7, 8, 9}, {0, 1, 2, 4, 5, 6, 7, 8, 9},
                          {0, 1, 2, 3, 5, 6, 7, 8, 9}, {0, 1, 2, 3, 4, 6, 7, 8, 9},
                          {0, 1, 2, 3, 4, 5, 7, 8, 9}, {0, 1, 2, 3, 4, 5, 6, 8, 9},
                          {0, 1, 2, 3, 4, 5, 6, 7, 9, 10}, {0, 1, 2, 3, 4, 5, 6, 7, 8, 11}, {8},
                          {9}}, 11);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
