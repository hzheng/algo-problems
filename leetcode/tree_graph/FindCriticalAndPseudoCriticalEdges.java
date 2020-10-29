import java.util.*;

import common.Utils;
import org.junit.Test;

import static org.junit.Assert.*;

// LC1489: https://leetcode.com/problems/find-critical-and-pseudo-critical-edges-in-minimum-spanning-tree/
//
// Given a weighted undirected connected graph with n vertices numbered from 0 to n-1, and an array
// edges where edges[i] = [fromi, toi, weighti] represents a bidirectional and weighted edge between
// nodes fromi and toi. A minimum spanning tree (MST) is a subset of the edges of the graph that
// connects all vertices without cycles and with the minimum possible total edge weight.
// Find all the critical and pseudo-critical edges in the minimum spanning tree (MST) of the given
// graph. An MST edge whose deletion from the graph would cause the MST weight to increase is called
// a critical edge. A pseudo-critical edge, on the other hand, is that which can appear in some MSTs
// but not all.
// Note that you can return the indices of the edges in any order.
//
// Constraints:
// 2 <= n <= 100
// 1 <= edges.length <= min(200, n * (n - 1) / 2)
// edges[i].length == 3
// 0 <= fromi < toi < n
// 1 <= weighti <= 1000
// All pairs (fromi, toi) are distinct.
public class FindCriticalAndPseudoCriticalEdges {
    // Union Find + Sort (Kruskal's algorithm)
    // time complexity: O(E^2*log(E)), space complexity: O(N+E)
    // 19 ms(94.93%), 40 MB(5.99%) for 65 tests
    public List<List<Integer>> findCriticalAndPseudoCriticalEdges(int n, int[][] edges) {
        int m = edges.length;
        int[][] newEdges = new int[m + 1][4];
        for (int i = 0; i < m; i++) { // reserve the first element for `included`
            newEdges[i + 1] = Arrays.copyOf(edges[i], 4);
            newEdges[i + 1][3] = i; // record the original index
        }
        Arrays.sort(newEdges, Comparator.comparingInt(e -> e[2]));
        int minWeight = weigh(n, newEdges, -1, -1);
        List<List<Integer>> res = new ArrayList<>();
        res.add(new ArrayList<>());
        res.add(new ArrayList<>());
        for (int i = 1; i <= m; i++) {
            int[] e = newEdges[i];
            if (weigh(n, newEdges, -1, i) > minWeight) {
                res.get(0).add(e[3]);
            } else if (weigh(n, newEdges, i, -1) == minWeight) {
                res.get(1).add(e[3]);
            }
        }
        return res;
    }

    private int weigh(int n, int[][] edges, int included, int excluded) {
        int[] id = new int[n];
        Arrays.fill(id, -1);
        if (included >= 0) {
            edges[0] = edges[included]; // select included at first
        }
        for (int res = 0, components = n, i = (included >= 0) ? 0 : 1; i < edges.length; i++) {
            int[] edge = edges[i];
            if (i != excluded && i != included && union(id, edge[0], edge[1])) {
                res += edge[2];
                if (--components == 1) { return res; }
            }
        }
        return Integer.MAX_VALUE;
    }

    private boolean union(int[] id, int x, int y) {
        int px = root(id, x);
        int py = root(id, y);
        if (px == py) { return false; }

        if (id[py] < id[px]) {
            int tmp = px;
            px = py;
            py = tmp;
        }
        id[px] += id[py];
        id[py] = px;
        return true;
    }

    private int root(int[] id, int x) {
        int parent = x;
        for (; id[parent] >= 0; parent = id[parent]) {}
        return parent;
    }

    // Heap (Prim's algorithm)
    // time complexity: O(E*N^2), space complexity: O(N+E)
    // 222 ms(10.55%), 39.1 MB(5.50%) for 65 tests
    public List<List<Integer>> findCriticalAndPseudoCriticalEdges2(int n, int[][] edges) {
        List<int[]>[] graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        int i = 0;
        for (int[] e : edges) {
            graph[e[0]].add(new int[] {e[1], e[2], i}); // neighbor, weight, index
            graph[e[1]].add(new int[] {e[0], e[2], i});
            i++;
        }
        int minWeight = weigh(graph, 0, -1, -1);
        List<List<Integer>> res = new ArrayList<>();
        res.add(new ArrayList<>());
        res.add(new ArrayList<>());
        for (i = 0; i < edges.length; i++) {
            if (weigh(graph, 0, -1, i) > minWeight) {
                res.get(0).add(i);
            } else if (weigh(graph, edges[i][0], i, -1) == minWeight) {
                res.get(1).add(i);
            }
        }
        return res;
    }

    private int weigh(List<int[]>[] graph, int start, int included, int excluded) {
        boolean[] visited = new boolean[graph.length];
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e[1]));
        pq.offer(new int[] {start, 0, -1}); // nei, weight, index
        for (int res = 0, left = graph.length; !pq.isEmpty(); ) {
            int[] cur = pq.poll();
            int vertex = cur[0];
            if (visited[vertex]) { continue; }

            visited[vertex] = true;
            res += cur[1];
            if (--left == 0) { return res; }

            for (int[] nei : graph[vertex]) {
                if (visited[nei[0]] || nei[2] == excluded) { continue; }

                if (nei[2] == included) {
                    res += nei[1]; // make up the weight
                    nei = new int[] {nei[0], 0, nei[2]}; // promote to the first
                }
                pq.offer(nei);
            }
        }
        return Integer.MAX_VALUE;
    }

    // TODO: Tarjan's algorithm

    private void test(int n, int[][] edges, Integer[][] expected) {
        List<List<Integer>> expectedList = Utils.toList(expected);
        assertEquals(expectedList, findCriticalAndPseudoCriticalEdges(n, edges));
        assertEquals(expectedList, findCriticalAndPseudoCriticalEdges2(n, edges));
    }

    @Test public void test() {
        test(5, new int[][] {{0, 1, 1}, {1, 2, 1}, {2, 3, 2}, {0, 3, 2}, {0, 4, 3}, {3, 4, 3},
                             {1, 4, 6}}, new Integer[][] {{0, 1}, {2, 3, 4, 5}});
        test(4, new int[][] {{0, 1, 1}, {1, 2, 1}, {2, 3, 1}, {0, 3, 1}},
             new Integer[][] {{}, {0, 1, 2, 3}});
        test(6, new int[][] {{0, 1, 1}, {1, 2, 1}, {0, 2, 1}, {2, 3, 4}, {3, 4, 2}, {3, 5, 2},
                             {4, 5, 2}}, new Integer[][] {{3}, {0, 1, 2, 4, 5, 6}});
        test(3, new int[][] {{0, 1, 1}, {0, 2, 2}, {1, 2, 3}}, new Integer[][] {{0, 1}, {}});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
