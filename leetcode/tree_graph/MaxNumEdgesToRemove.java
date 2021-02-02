import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1579: https://leetcode.com/problems/remove-max-number-of-edges-to-keep-graph-fully-traversable/
//
// Alice and Bob have an undirected graph of n nodes and 3 types of edges:
// Type 1: Can be traversed by Alice only.
// Type 2: Can be traversed by Bob only.
// Type 3: Can by traversed by both Alice and Bob.
// Given an array edges where edges[i] = [typei, ui, vi] represents a bidirectional edge of type
// typei between nodes ui and vi, find the maximum number of edges you can remove so that after
// removing the edges, the graph can still be fully traversed by both Alice and Bob. The graph is
// fully traversed by Alice and Bob if starting from any node, they can reach all other nodes.
// Return the maximum number of edges you can remove, or return -1 if it's impossible for the graph
// to be fully traversed by Alice and Bob.
//
// Constraints:
// 1 <= n <= 10^5
// 1 <= edges.length <= min(10^5, 3 * n * (n-1) / 2)
// edges[i].length == 3
// 1 <= edges[i][0] <= 3
// 1 <= edges[i][1] < edges[i][2] <= n
// All tuples (typei, ui, vi) are distinct.
public class MaxNumEdgesToRemove {
    // Sort + Union Find + Set
    // time complexity: O(E), space complexity: O(N)
    // 187 ms(6.29%), 91.8 MB(83.92%) for 84 tests
    public int maxNumEdgesToRemove(int n, int[][] edges) {
        Arrays.sort(edges, Comparator.comparingInt(p -> -p[0]));
        Set<Long> set1 = extraEdges(n, edges, 1);
        if (set1 == null) { return -1; }

        Set<Long> set2 = extraEdges(n, edges, 2);
        if (set2 == null) { return -1; }

        int res = 0;
        for (long e : set1) {
            if (set2.contains(e)) {
                res++;
            }
        }
        return res;
    }

    private long encode(int[] edge) {
        return ((edge[0] * 100001L + edge[1]) << Integer.SIZE) | edge[2];
    }

    private Set<Long> extraEdges(int n, int[][] edges, int index) {
        int[] id = new int[n + 1];
        Arrays.fill(id, -1);
        Set<Long> set = new HashSet<>();
        for (int[] edge : edges) {
            if (edge[0] != index && edge[0] != 3 || !union(id, edge[1], edge[2])) {
                set.add(encode(edge));
            }
        }
        return (id[root(id, 1)] == -n) ? set : null;
    }

    // Sort + Union Find
    // time complexity: O(E), space complexity: O(N)
    // 33 ms(32.87%), 100.9 MB(53.85%) for 84 tests
    public int maxNumEdgesToRemove2(int n, int[][] edges) {
        Arrays.sort(edges, Comparator.comparingInt(p -> -p[0]));
        int[][] id = new int[2][n + 1];
        Arrays.fill(id[0], -1);
        Arrays.fill(id[1], -1);
        int addedEdges = 0;
        for (int[] edge : edges) {
            int u = edge[1];
            int v = edge[2];
            int type = edge[0];
            if (type == 3) {
                if (union(id[0], u, v) | union(id[1], u, v)) { // note that it's "|" NOT "||"
                    addedEdges++;
                }
            } else if (union(id[type - 1], u, v)) {
                addedEdges++;
            }
        }
        if (id[0][root(id[0], 1)] + id[1][root(id[1], 1)] != -n * 2) { return -1; }
        return edges.length - addedEdges;
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

    void test(int n, int[][] edges, int expected) {
        assertEquals(expected, maxNumEdgesToRemove(n, edges));
        assertEquals(expected, maxNumEdgesToRemove2(n, edges));
    }

    @Test public void test() {
        test(2, new int[][] {{1, 1, 2}, {2, 1, 2}, {3, 1, 2}}, 2);
        test(4, new int[][] {{3, 1, 2}, {3, 2, 3}, {1, 1, 3}, {1, 2, 4}, {1, 1, 2}, {2, 3, 4}}, 2);
        test(4, new int[][] {{3, 1, 2}, {3, 2, 3}, {1, 1, 4}, {2, 1, 4}}, 0);
        test(4, new int[][] {{3, 2, 3}, {1, 1, 2}, {2, 3, 4}}, -1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
