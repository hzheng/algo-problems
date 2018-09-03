import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// import common.DisjointSet;

// LC685: https://leetcode.com/problems/redundant-connection-ii/
//
// A tree is a directed graph such that, there is exactly one node (the root)
// for which all other nodes are descendants of this node, plus every node has
// exactly one parent, except for the root node. The given input is a directed
//  graph that started as a rooted tree with N nodes (with distinct values 1, 2,
// ..., N), with one additional directed edge added. The added edge has two
// different vertices chosen from 1 to N, and was not an edge that already existed.
// The resulting graph is given as a 2D-array of edges. Each element of edges is
// a pair [u, v] that represents a directed edge connecting nodes u and v, where
// u is a parent of child v. Return an edge that can be removed so that the
// resulting graph is a rooted tree of N nodes. If there are multiple answers,
// return the answer that occurs last in the given 2D-array.
public class RedundantConnection2 {
    // Union Find
    // time complexity: O(N * ?), space complexity: O(N)
    // beats 68.84%(4 ms for 52 tests)
    public int[] findRedundantDirectedConnection(int[][] edges) {
        int[] cand1 = null;
        int[] cand2 = null;
        int n = edges.length;
        int[] parent = new int[n + 1];
        for (int[] edge : edges) {
            if (parent[edge[1]] == 0) {
                parent[edge[1]] = edge[0];
            } else { // found a node that has more than 1 parents
                cand1 = edge;
                cand2 = new int[] {parent[edge[1]], edge[1]};
            }
        }
        for (int i = 1; i <= n; i++) {
            parent[i] = i;
        }
        for (int[] edge : edges) {
            if (Arrays.equals(edge, cand1)) continue;

            int kid = edge[1];
            int dad = edge[0];
            if (root(parent, dad) == kid) return (cand2 == null) ? edge : cand2;

            parent[kid] = dad;
        }
        return cand1;
    }
    
    private int root(int[] parent, int i) {
        for (; i != parent[i]; i = parent[i] = parent[parent[i]]) {}
        return i;
    }

    // Union Find
    // time complexity: O(N * ?), space complexity: O(N)
    // beats 25.72%(6 ms for 52 tests)
    public int[] findRedundantDirectedConnection2(int[][] edges) {
        int n = edges.length;
        int[] parent = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            parent[i] = i;
        }
        int[] multiParentEdge = null;
        int[] cycleEdge = null;
        for (int[] e : edges) {
            int p1 = root(parent, e[0]);
            int p2 = root(parent, e[1]);
            if (p1 == p2) {
                cycleEdge = e;
            } else if (p2 != e[1]) {
                multiParentEdge = e;
            } else {
                parent[p2] = p1;
            }
        }
        if (multiParentEdge == null) return cycleEdge; 
        if (cycleEdge == null) return multiParentEdge;
        
        for (int[] e : edges) {
            if (e[1] == multiParentEdge[1]) return e;
        }
        return null;
    }

    // DFS/Recursion + Hash Table + Deque + 3-Color
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 12.77%(10 ms for 39 tests)
    public int[] findRedundantDirectedConnection3(int[][] edges) {
        int n = edges.length;
        @SuppressWarnings("unchecked")
        List<Integer>[] graph = new List[n + 1];
        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }
        int[] parent = new int[n + 1];
        int[] multiParentEdge = null;
        for (int[] edge : edges) {
            graph[edge[0]].add(edge[1]);
            if (parent[edge[1]] == 0) {
                parent[edge[1]] = edge[0];
            } else { // keep the last node that has more than 1 parents
                multiParentEdge = edge;
            }
        }
        int[] visited = new int[n + 1];
        Deque<Integer> path = new LinkedList<>();
        path.offer(0);
        if (!cyclic(graph, 1, visited, path)) return multiParentEdge;

        if (multiParentEdge == null) {
            for (int j = n - 1; ; j--) {
                int[] e = edges[j];
                if (visited[e[0]] < 0 && visited[e[1]] < 0) return e;
            }
        } else {
            path.offerLast(path.peekFirst()); // form a loop
            for (int prev = path.pollFirst(), cur; ; prev = cur) {
                cur = path.pollFirst();
                if (cur == multiParentEdge[1]) return new int[] {prev, cur};
            }
        }
    }

    private boolean cyclic(List<Integer>[] graph, int cur, int[] visited,
                           Deque<Integer> path) {
        visited[cur] = -1;
        path.offerLast(cur);
        for (int nei : graph[cur]) {
            if (visited[nei] > 0) continue;
            if (visited[nei] < 0) {
                for (;; path.pollFirst()) {
                    int v = path.peekFirst();
                    if (v == nei) return true;

                    visited[v] = 0;
                }
            }
            if (cyclic(graph, nei, visited, path)) return true;
        }
        visited[cur] = 1;
        path.pollLast();
        return false;
    }

    void test(int[][] edges, int[] expected) {
        assertArrayEquals(expected, findRedundantDirectedConnection(edges));
        assertArrayEquals(expected, findRedundantDirectedConnection2(edges));
        assertArrayEquals(expected, findRedundantDirectedConnection3(edges));
    }

    @Test
    public void test() {
        test(new int[][] { { 1, 2 }, { 1, 3 }, { 2, 3 } }, new int[] { 2, 3 });
        test(new int[][] { { 1, 2 }, { 2, 3 }, { 3, 1 }, { 4, 1 } },
             new int[] { 3, 1 });
        test(new int[][] { { 1, 2 }, { 2, 3 }, { 3, 1 }, { 1, 4 } },
             new int[] { 3, 1 });
        test(new int[][] { { 2, 1 }, { 3, 1 }, { 4, 2 }, { 1, 4 } },
             new int[] { 2, 1 });
        test(new int[][] { { 1, 2 }, { 2, 3 }, { 3, 4 }, { 4, 1 }, { 1, 5 } },
             new int[] { 4, 1 });
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
