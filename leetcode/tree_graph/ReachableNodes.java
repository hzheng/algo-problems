import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC886: https://leetcode.com/problems/reachable-nodes-in-subdivided-graph/
//
// Starting with an undirected graph (the "original graph") with nodes from 0 to
// N-1, subdivisions are made to some of the edges. The graph is given as
// follows: edges[k] is a list of integer pairs (i, j, n) such that (i, j) is an
// edge of the original graph, and n is the total number of new nodes on that
// edge. Then, the edge (i, j) is deleted from the original graph, n new nodes
// (x_1, x_2, ..., x_n) are added to the original graph, and n+1 new edges
// (i, x_1), (x_1, x_2), (x_2, x_3), ..., (x_{n-1}, x_n), (x_n, j) are added to
// the original graph. Now, you start at node 0 from the original graph, and in
// each move, you travel along one edge.
// Return how many nodes you can reach in at most M moves.
// Note:
// 0 <= edges.length <= 10000
// 0 <= edges[i][0] < edges[i][1] < N
// There does not exist any i != j for which edges[i][0] == edges[j][0] and
// edges[i][1] == edges[j][1].
// The original graph has no parallel edges.
// 0 <= edges[i][2] <= 10000
// 0 <= M <= 10^9
// 1 <= N <= 3000
public class ReachableNodes {
    // Dijkstra's algorithm + Heap
    // beats 97.67%(41 ms for 50 tests)
    public int reachableNodes(int[][] edges, int M, int N) {
        Map<Integer, List<int[]> > graph = new HashMap<>();
        for (int[] e : edges) {
            add(e[0], e[1], e[2], graph);
            add(e[1], e[0], e[2], graph);
        }
        int[] dist = new int[N];
        PriorityQueue<Integer> pq = new PriorityQueue<>(
            new Comparator<Integer>() {
            public int compare(Integer a, Integer b) {
                return dist[a] - dist[b];
            }
        });
        Arrays.fill(dist, M + 1);
        dist[0] = 0;
        for (pq.offer(0); !pq.isEmpty(); ) {
            int u = pq.poll();
            List<int[]> nei = graph.get(u);
            if (nei == null) continue;

            for (int[] v : nei) {
                if (dist[v[0]] > dist[u] + v[1]) {
                    dist[v[0]] = dist[u] + v[1];
                    pq.offer(v[0]);
                }
            }
        }
        int res = 0;
        for (int i = 0; i < N; i++) {
            res += (dist[i] <= M) ? 1 : 0;
        }
        for (int[] e : edges) {
            int d1 = dist[e[0]];
            int d2 = dist[e[1]];
            res += Math.min(Math.max(0, M - d1) + Math.max(0, M - d2), e[2]);
        }
        return res;
    }

    // Dijkstra's algorithm + Heap
    // Time Limit Exceeded
    public int reachableNodes2(int[][] edges, int M, int N) {
        Map<Integer, List<int[]> > graph = new HashMap<>();
        for (int[] e : edges) {
            add(e[0], e[1], e[2], graph);
            add(e[1], e[0], e[2], graph);
        }
        int[] dist = new int[N];
        PriorityQueue<Integer> pq = new PriorityQueue<>(
            new Comparator<Integer>() {
            public int compare(Integer a, Integer b) {
                return dist[a] - dist[b];
            }
        });
        for (int i = 0; i < N; i++) {
            dist[i] = (i == 0) ? 0 : M + 1;
            pq.offer(i);
        }
        while (!pq.isEmpty()) {
            int u = pq.poll();
            List<int[]> nei = graph.get(u);
            if (nei == null) continue;

            for (int[] v : nei) {
                if (dist[v[0]] > dist[u] + v[1]) {
                    // inefficient decrease_priority
                    List<Integer> tmp = new ArrayList<>();
                    while (!pq.isEmpty()) {
                        int w = pq.poll();
                        tmp.add(w);
                        if (dist[w] > dist[v[0]]) break;
                    }
                    dist[v[0]] = dist[u] + v[1];
                    for (int x : tmp) {
                        pq.offer(x);
                    }
                }
            }
        }
        int res = 0;
        for (int i = 0; i < N; i++) {
            res += (dist[i] <= M) ? 1 : 0;
        }
        for (int[] e : edges) {
            int d1 = dist[e[0]];
            int d2 = dist[e[1]];
            res += Math.min(Math.max(0, M - d1) + Math.max(0, M - d2), e[2]);
        }
        return res;
    }

    private void add(int u, int v, int w, Map<Integer, List<int[]> > graph) {
        List<int[]> list = graph.get(u);
        if (list == null) {
            graph.put(u, list = new ArrayList<>());
        }
        list.add(new int[] { v, w + 1 });
    }

    // Dijkstra's algorithm + Heap + Hash Table
    // time complexity: O(E * log(N)), space complexity: O(N)
    // beats 81.00%(132 ms for 50 tests)
    public int reachableNodes3(int[][] edges, int M, int N) {
        Map<Integer, Map<Integer, Integer>> graph = new HashMap<>();
        for (int[] e : edges) {
            graph.computeIfAbsent(e[0], x -> new HashMap<>()).put(e[1], e[2]);
            graph.computeIfAbsent(e[1], x -> new HashMap<>()).put(e[0], e[2]);
        }
        PriorityQueue<int[]> pq = new PriorityQueue<int[]>((a, b) -> a[1] - b[1]);
        int[] dist = new int[N];
        Arrays.fill(dist, M + 1);
        dist[0] = 0;
        Map<Integer, Integer> reach = new HashMap<>();
        int res = 0;
        for (pq.offer(new int[]{0, 0}); !pq.isEmpty(); ) {
            int[] first = pq.poll();
            int u = first[0];
            int d = first[1];
            if (d > dist[u]) continue;

            res++;
            Map<Integer, Integer> nei = graph.get(u);
            if (nei == null) continue;

            for (int v : nei.keySet()) {
                int weight = graph.get(u).get(v);
                reach.put(N * u + v, Math.min(weight, M - d));
                int d2 = d + weight + 1;
                if (d2 < dist[v]) {
                    pq.offer(new int[]{v, d2});
                    dist[v] = d2;
                }
            }
        }
        for (int[] edge : edges) {
            res += Math.min(edge[2], 
                            reach.getOrDefault(edge[0] * N + edge[1], 0) 
                            + reach.getOrDefault(edge[1] * N + edge[0], 0));
        }
        return res;
    } 
    
    void test(int[][] edges, int M, int N, int expected) {
        assertEquals(expected, reachableNodes(edges, M, N));
        assertEquals(expected, reachableNodes2(edges, M, N));
        assertEquals(expected, reachableNodes3(edges, M, N));
    }

    @Test
    public void test() {
        test(new int[][] { { 0, 1, 10 }, { 0, 2, 1 }, { 1, 2, 2 } }, 6, 3, 13);
        test(new int[][] { { 0, 1, 4 }, { 1, 2, 6 }, { 0, 2, 8 }, { 1, 3, 1 } },
             10, 4, 23);
        test(new int[][] { { 0, 1, 4 }, { 1, 2, 6 }, { 2, 3, 5 }, { 0, 2, 8 }, 
                           { 1, 3, 1 }, { 3, 0, 5 } }, 10, 4, 33);
        test(new int[][] { { 1, 3, 23 }, { 3, 5, 19 }, { 3, 6, 17 }, { 1, 5, 14 }, 
                           { 6, 7, 20 }, { 1, 4, 10 }, { 1, 6, 0 }, { 3, 4, 20 },
                           { 1, 7, 4 }, { 0, 4, 10 }, { 0, 7, 9 }, { 2, 3, 3},
                           { 3, 7, 9 }, { 5, 7, 4 }, { 4, 5, 16 }, { 0, 1, 16 },
                           { 2, 6, 0 }, { 4, 7, 11 }, { 2, 5, 14 }, { 5, 6, 22 },
                           { 4, 6, 12 }, { 0, 6, 2 }, { 0, 2, 1 }, { 2, 4, 22 },
                           { 2, 7, 20 } }, 19, 8, 301);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
