import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1443: https://leetcode.com/problems/minimum-time-to-collect-all-apples-in-a-tree/
//
// Given an undirected tree consisting of n vertices numbered from 0 to n-1, which has some apples
// in their vertices. You spend 1 second to walk over one edge of the tree. Return the minimum time
// in seconds you have to spend in order to collect all apples in the tree starting at vertex 0 and
// coming back to this vertex. The edges of the undirected tree are given in the array edges, where
// edges[i] = [fromi, toi] means that exists an edge connecting the vertices fromi and toi.
// Additionally, there is a boolean array hasApple, where hasApple[i] = true means that vertex i
// has an apple, otherwise, it does not have any apple.
// Constraints:
//
// 1 <= n <= 10^5
// edges.length == n-1
// edges[i].length == 2
// 0 <= fromi, toi <= n-1
// fromi < toi
// hasApple.length == n
public class CollectApples {
    // DFS + Recursion + Set
    // time complexity: O(N), space complexity: O(N)
    // 58 ms(43.06%), 82.7 MB(100%) for 54 tests
    public int minTime(int n, int[][] edges, List<Boolean> hasApple) {
        boolean[] visited = new boolean[n];
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int[] e : edges) {
            graph.computeIfAbsent(e[0], x -> new ArrayList<>()).add(e[1]);
            graph.computeIfAbsent(e[1], x -> new ArrayList<>()).add(e[0]);
        }
        return dfs(graph, 0, hasApple, visited);
    }

    private int dfs(Map<Integer, List<Integer>> graph, int cur, List<Boolean> hasApple,
                    boolean[] visited) {
        if (visited[cur]) { return 0; }

        visited[cur] = true;
        int res = 0;
        for (int next : graph.getOrDefault(cur, Collections.emptyList())) {
            res += dfs(graph, next, hasApple, visited);
        }
        if ((res > 0 || hasApple.get(cur)) && (cur != 0)) {
            res += 2;
        }
        return res;
    }

    // BFS + Queue + Set
    // time complexity: O(N), space complexity: O(N)
    // 61 ms(40.83%), 83.3 MB(100%) for 54 tests
    public int minTime2(int n, int[][] edges, List<Boolean> hasApple) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        int[] parent = new int[n];
        for (int[] e : edges) {
            int u = e[0];
            int v = e[1];
            graph.computeIfAbsent(u, x -> new ArrayList<>()).add(v);
            graph.computeIfAbsent(v, x -> new ArrayList<>()).add(u);
            parent[v] = u;
        }
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(0);
        boolean[] visited = new boolean[n];
        int[] dist = new int[n];
        Arrays.fill(dist, -1);
        for (dist[0] = 0; !queue.isEmpty(); ) {
            int cur = queue.poll();
            for (int next : graph.getOrDefault(cur, Collections.emptyList())) {
                if (dist[next] < 0) {
                    dist[next] = dist[cur] + 1;
                    queue.offer(next);
                }
            }
        }
        int res = 0;
        for (int u = n - 1; u >= 0; u--) {
            if (visited[u] || !hasApple.get(u)) { continue; }

            int v = u;
            for (; !visited[v] && parent[v] >= 0; v = parent[v]) {
                visited[v] = true;
            }
            visited[u] = true;
            res += (2 * (Math.abs(dist[u] - dist[v])));
        }
        return res;
    }

    @Test public void test() {
        test(7, new int[][] {{0, 1}, {0, 2}, {1, 4}, {1, 5}, {2, 3}, {2, 6}},
             new Boolean[] {false, false, true, false, true, true, false}, 8);
        test(7, new int[][] {{0, 1}, {0, 2}, {1, 4}, {1, 5}, {2, 3}, {2, 6}},
             new Boolean[] {false, false, true, false, false, true, false}, 6);
        test(7, new int[][] {{0, 1}, {0, 2}, {1, 4}, {1, 5}, {2, 3}, {2, 6}},
             new Boolean[] {false, false, false, false, false, false, false}, 0);
        test(4, new int[][] {{0, 2}, {0, 3}, {1, 2}}, new Boolean[] {false, true, false, false}, 4);
    }

    private void test(int n, int[][] edges, Boolean[] hasApple, int expected) {
        assertEquals(expected, minTime(n, edges, Arrays.asList(hasApple)));
        assertEquals(expected, minTime2(n, edges, Arrays.asList(hasApple)));
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
