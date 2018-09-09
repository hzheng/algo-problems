import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC787: https://leetcode.com/problems/cheapest-flights-within-k-stops/
//
// There are n cities connected by m flights. Each fight starts from city u and
// arrives at v with a price w. Given all the cities and fights, together with
// starting city src and the destination dst, find the cheapest price from src
// to dst with up to k stops. If there is no such route, output -1.
public class CheapestFlightsWithinKStops {
    // BFS + Queue + Hash Table
    // time complexity: O(N ^ 2 * K), space complexity: O(N ^ 2)
    // beats 53.52%(32 ms for 41 tests)
    public int findCheapestPrice(int n, int[][] flights, int src, int dst,
                                 int K) {
        Map<Integer, Map<Integer, Integer> > graph = new HashMap<>();
        for (int i = 0; i < n; i++) {
            graph.put(i, new HashMap<>());
        }
        for (int[] f : flights) {
            graph.get(f[0]).put(f[1], f[2]);
        }
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[] { src, 0 });
        int res = Integer.MAX_VALUE;
        for (int k = K + 1; k >= 0; k--) {
            for (int i = queue.size(); i > 0; i--) {
                int[] cur = queue.poll();
                int stop = cur[0];
                if (stop == dst) {
                    res = Math.min(res, cur[1]);
                    continue;
                }
                for (Map.Entry<Integer, Integer> x : graph.get(stop).entrySet()) {
                    int newCost = cur[1] + x.getValue();
                    if (newCost < res) {
                        queue.offer(new int[] { x.getKey(), newCost });
                    }
                }
            }
        }
        return (res == Integer.MAX_VALUE) ? -1 : res;
    }

    // Solution of Choice
    // BFS + Heap + Hash Table (Dijkstra's algorithm)
    // time complexity: O(N ^ 2 * K), space complexity: O(N ^ 2)
    // beats 60.07%(22 ms for 41 tests)
    public int findCheapestPrice2(int n, int[][] flights, int src, int dst,
                                  int K) {
        Map<Integer, Map<Integer, Integer> > graph = new HashMap<>();
        for (int i = 0; i < n; i++) {
            graph.put(i, new HashMap<>());
        }
        for (int[] f : flights) {
            graph.get(f[0]).put(f[1], f[2]);
        }
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) { return a[1] - b[1]; }
        });
        pq.offer(new int[] { src, 0, K + 1 });
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int stop = cur[0];
            int cost = cur[1];
            if (stop == dst) return cost;

            int more = cur[2];
            if (more <= 0) continue;

            Map<Integer, Integer> m = graph.get(stop);
            for (int v : m.keySet()) {
                pq.offer(new int[] { v, cost + m.get(v), more - 1 });
            }
        }
        return -1;
    }

    // BFS + Heap + Hash Table (Dijkstra's algorithm)
    // time complexity: O(N ^ 2 * K), space complexity: O(N ^ 2)
    // beats 95.58%(7 ms for 41 tests)
    public int findCheapestPrice3(int n, int[][] flights, int src, int dst,
                                  int K) {
        int[][] graph = new int[n][n];
        for (int[] f : flights) {
            graph[f[0]][f[1]] = f[2];
        }
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return a[1] - b[1];
            }
        });
        int[] cost = new int[n];
        Arrays.fill(cost, Integer.MAX_VALUE);
        cost[src] = 0;
        int[] stop = new int[n];
        Arrays.fill(stop, K + 1);
        stop[src] = 0;
        for (pq.offer(new int[] { src, 0, 0 }); !pq.isEmpty(); ) {
            int[] cur = pq.poll();
            if (cur[0] == dst) return cur[1];
            if (cur[2] == K + 1) continue;

            int[] nextCosts = graph[cur[0]];
            for (int i = 0; i < n; i++) {
                if (nextCosts[i] == 0) continue;

                int newCost = cur[1] + nextCosts[i];
                int newStops = cur[2] + 1;
                if (newCost < cost[i]) {
                    pq.offer(new int[] { i, newCost, newStops });
                    cost[i] = newCost;
                } else if (newStops < stop[i]) {
                    pq.offer(new int[] { i, newCost, newStops });
                    stop[i] = newStops;
                }
            }
        }
        return (cost[dst] == Integer.MAX_VALUE) ? -1 : cost[dst];
    }

    // Solution of Choice
    // DFS + Recursion + Backtracking
    // time complexity: O(N ^ 2 * K), space complexity: O(N ^ 2)
    // beats 37.53%(61 ms for 41 tests)
    public int findCheapestPrice4(int n, int[][] flights, int src, int dst,
                                  int K) {
        int[][] graph = new int[n][n];
        for (int[] f : flights) {
            graph[f[0]][f[1]] = f[2];
        }
        int[] res = new int[] { Integer.MAX_VALUE };
        dfs(graph, src, dst, K + 1, 0, new boolean[n], res);
        return (res[0] == Integer.MAX_VALUE) ? -1 : res[0];
    }

    private void dfs(int[][] graph, int src, int dst, int k, int curCost,
                     boolean[] visited, int[] res) {
        if (src == dst) {
            res[0] = curCost;
            return;
        }
        if (k == 0) return;

        visited[src] = true;
        int i = 0;
        for (int cost : graph[src]) {
            if (visited[i++] || cost == 0) continue;

            if (curCost + cost <= res[0]) {
                dfs(graph, i - 1, dst, k - 1, curCost + cost, visited, res);
            }
        }
        visited[src] = false; // Backtracking!
    }

    // Dynamic Programming
    // time complexity: O(N ^ 2 * K), space complexity: O(N * K)
    // beats 73.10%(12 ms for 41 tests)
    public int findCheapestPrice5(int n, int[][] flights, int src, int dst,
                                  int K) {
        int[][] dp = new int[K + 2][n];
        int max = Integer.MAX_VALUE / 2;
        for (int[] d : dp) {
            Arrays.fill(d, max);
        }
        dp[0][src] = 0;
        for (int i = 1; i <= K + 1; i++) {
            dp[i][src] = 0;
            for (int[] f : flights) {
                dp[i][f[1]] = Math.min(dp[i][f[1]], dp[i - 1][f[0]] + f[2]);    
            }
        }
        return (dp[K + 1][dst] >= max) ? -1 : dp[K + 1][dst];
    }

    // Solution of Choice
    // Dynamic Programming
    // time complexity: O(N ^ 2 * K), space complexity: O(N)
    // beats 90.23%(8 ms for 41 tests)
    public int findCheapestPrice6(int n, int[][] flights, int src, int dst,
                                  int K) {
        int[] dp = new int[n];
        int max = Integer.MAX_VALUE / 2;
        Arrays.fill(dp, max);
        dp[src] = 0;
        for (int i = 1; i <= K + 1; i++) {
            int[] next = dp.clone();
            for (int[] f : flights) {
                next[f[1]] = Math.min(next[f[1]], dp[f[0]] + f[2]);    
            }
            dp = next;
        }
        return (dp[dst] >= max) ? -1 : dp[dst];
    }

    void test(int n, int[][] flights, int src, int dst, int K, int expected) {
        assertEquals(expected, findCheapestPrice(n, flights, src, dst, K));
        assertEquals(expected, findCheapestPrice2(n, flights, src, dst, K));
        assertEquals(expected, findCheapestPrice3(n, flights, src, dst, K));
        assertEquals(expected, findCheapestPrice4(n, flights, src, dst, K));
        assertEquals(expected, findCheapestPrice5(n, flights, src, dst, K));
        assertEquals(expected, findCheapestPrice6(n, flights, src, dst, K));
    }

    @Test
    public void test() {
        test(4, new int[][] { { 0, 1, 1 }, { 0, 2, 5 }, { 1, 2, 1 }, { 2, 3, 1 } },
             0, 3, 1, 6);
        test(3, new int[][] { { 0, 1, 100 }, { 1, 2, 100 }, { 0, 2, 500 } }, 0,
             2, 1, 200);
        test(3, new int[][] { { 0, 1, 100 }, { 1, 2, 100 }, { 0, 2, 500 } }, 0,
             2, 0, 500);
        test(10, new int[][] { { 3, 4, 4 }, { 2, 5, 6 }, { 4, 7, 10 },
                               { 9, 6, 5 }, { 7, 4, 4 }, { 6, 2, 10 },
                               { 6, 8, 6 }, { 7, 9, 4 }, { 1, 5, 4 }, 
                               { 1, 0, 4 }, { 9, 7, 3 }, { 7, 0, 5 }, 
                               { 6, 5, 8 }, { 1, 7, 6 }, { 4, 0, 9 },
                               { 5, 9, 1 }, { 8, 7, 3 }, { 1, 2, 6 },
                               { 4, 1, 5 }, { 5, 2, 4 }, { 1, 9, 1 },
                               { 7, 8, 10 }, { 0, 4, 2 }, { 7, 2, 8 } }, 
             6, 0, 7, 14);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
