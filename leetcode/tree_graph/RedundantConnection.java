import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.DisjointSet;

// LC684: https://leetcode.com/problems/redundant-connection/
//
// The given input is a graph that started as a tree with N nodes (with distinct
// values 1, 2, ..., N), with one additional edge added. The added edge has two
// different vertices chosen from 1 to N, and was not an edge that already
// existed. The resulting graph is given as a 2D-array of edges. Each element of
// edges is a pair [u, v] with u < v, that represents an undirected edge
// connecting nodes u and v. Return an edge that can be removed so that the
// resulting graph is a tree of N nodes. If there are multiple answers, return
// the answer that occurs last in the given 2D-array. The answer edge [u, v]
// should be in the same format, with u < v.
public class RedundantConnection {
    // DFS/Recursion + Hash Table + Deque + 3-Color
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 23.26%(8 ms for 39 tests)
    public int[] findRedundantConnection(int[][] edges) {
        Map<Integer, List<Integer> > graph = new HashMap<>();
        for (int[] e : edges) {
            // lambda is slow in leetcode
            // graph.computeIfAbsent(e[0], x -> new ArrayList<>()).add(e[1]);
            // graph.computeIfAbsent(e[1], x -> new ArrayList<>()).add(e[0]);
            addEdge(graph, e[0], e[1]);
            addEdge(graph, e[1], e[0]);
        }
        int n = edges.length;
        int[] visited = new int[n + 1];
        Deque<Integer> path = new LinkedList<>();
        path.offer(0); // avoid empty
        dfs(graph, 1, visited, path);
        for (int i = n - 1; ; i--) {
            int[] e = edges[i];
            if (visited[e[0]] < 0 && visited[e[1]] < 0) return e;
        }
    }

    private void addEdge(Map<Integer, List<Integer> > graph, int u, int v) {
        List<Integer> nei = graph.get(u);
        if (nei == null) {
            graph.put(u, nei = new ArrayList<>());
        }
        nei.add(v);
    }

    private boolean dfs(Map<Integer, List<Integer> > graph, int cur,
                        int[] visited, Deque<Integer> path) {
        visited[cur] = -1;
        int prev = path.peekLast();
        path.offerLast(cur);
        for (int nei : graph.get(cur)) {
            if (visited[nei] > 0 || prev == nei) continue;
            if (visited[nei] < 0) {
                for (;; path.pollFirst()) {
                    int v = path.peekFirst();
                    if (v == nei) return true;

                    visited[v] = 0;
                }
            }
            if (dfs(graph, nei, visited, path)) return true;
        }
        visited[cur] = 1;
        path.pollLast();
        return false;
    }

    // DFS + Hash Table + Deque + 3-Color
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 21.51%(9 ms for 39 tests)
    public int[] findRedundantConnection2(int[][] edges) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int[] e : edges) {
            addEdge(graph, e[0], e[1]);
            addEdge(graph, e[1], e[0]);
        }
        int n = edges.length;
        int[] visited = new int[n + 1];
        Deque<Integer> path = new LinkedList<>();
        path.offerLast(0); // avoid empty
        path.offerLast(1);
        outer : while (true) {
            int cur = path.pollLast();
            int prev = path.peekLast();
            path.offerLast(cur); // put back
            visited[cur] = -1;
            for (int nei : graph.get(cur)) {
                if (visited[nei] > 0 || nei == prev) continue;

                if (visited[nei] < 0) {
                    for (;; path.pollFirst()) {
                        int v = path.peekFirst();
                        if (v == nei) break outer;

                        visited[v] = 0;
                    }
                }
                path.offerLast(nei);
                continue outer;
            }
            visited[cur] = 1;
            path.pollLast();
        }
        for (int i = n - 1; ; i--) {
            int[] e = edges[i];
            if (visited[e[0]] < 0 && visited[e[1]] < 0) return e;
        }
    }

    // DFS/Recursion + List
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 28.49%(6 ms for 39 tests)
    public int[] findRedundantConnection3(int[][] edges) {
        int n = edges.length;
        @SuppressWarnings("unchecked")
        List<Integer>[] graph = new List[n + 1];
        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }
        boolean[] visited = new boolean[n + 1];
        for (int[] edge : edges) {
            Arrays.fill(visited, false);
            int u = edge[0];
            int v = edge[1];
            if (!graph[u].isEmpty() && !graph[v].isEmpty()
                && dfs(graph, u, v, visited)) return edge;

            graph[u].add(v);
            graph[v].add(u);
        }
        throw new AssertionError();
    }

    private boolean dfs(List<Integer>[] graph, int source, int target,
                        boolean[] visited) {
        if (!visited[source]) {
            if (source == target) return true;

            visited[source] = true;
            for (int nei : graph[source]) {
                if (dfs(graph, nei, target, visited)) return true;
            }
        }
        return false;
    }

    // BFS + Queue
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 12.26%(18 ms for 39 tests)
    public int[] findRedundantConnection4(int[][] edges) {
        int n = edges.length;
        @SuppressWarnings("unchecked")
        List<Integer>[] graph = new List[n + 1];
        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }
        boolean[] visited = new boolean[n + 1];
        for (int[] e : edges) {
            Arrays.fill(visited, false);
            int source = e[0];
            int target = e[1];
            Queue<Integer> queue = new LinkedList<>();
            visited[source] = true;
            for (queue.offer(source); !queue.isEmpty(); ) {
                int cur = queue.poll();
                for (int nei : graph[cur]) {
                    if (nei == target) return e;

                    if (!visited[nei]) {
                        queue.offer(nei);
                        visited[nei] = true;
                    }
                }
            }
            graph[source].add(target);
            graph[target].add(source);
        }
        return null;
    }

    // Union Find
    // time complexity: O(N * Inverse-Ackermann(N)), space complexity: O(N)
    // beats 97.48%(3 ms for 39 tests)
    public int[] findRedundantConnection5(int[][] edges) {
        int n = edges.length;
        DisjointSet uf = new DisjointSet(n + 1);
        for (int[] edge : edges) {
            if (!uf.union(edge[0], edge[1])) return edge;
        }
        throw new AssertionError();
    }

    void test(int[][] edges, int[] expected) {
        assertArrayEquals(expected, findRedundantConnection(edges));
        assertArrayEquals(expected, findRedundantConnection2(edges));
        assertArrayEquals(expected, findRedundantConnection3(edges));
        assertArrayEquals(expected, findRedundantConnection4(edges));
        assertArrayEquals(expected, findRedundantConnection5(edges));
    }

    @Test
    public void test() {
        test(new int[][] { { 1, 2 }, { 1, 3 }, { 2, 3 } }, new int[] { 2, 3 });
        test(new int[][] { { 1, 2 }, { 2, 3 }, { 3, 4 }, { 1, 4 }, { 1, 5 } },
             new int[] { 1, 4 });
        test(new int[][] { { 2, 7 }, { 7, 8 }, { 3, 6 }, { 2, 5 }, { 6, 8 },
                         { 4, 8 }, { 2, 8 }, { 1, 8 }, { 7, 10 },
                { 3, 9 } }, new int[] { 2, 8 });
        test(new int[][] { { 16, 25 }, { 7, 9 }, { 3, 24 }, { 10, 20 }, { 15, 24 }, { 2, 8 }, { 19, 21 }, { 2, 15 },
                { 13, 20 }, { 5, 21 }, { 7, 11 }, { 6, 23 }, { 7, 16 }, { 1, 8 }, { 17, 20 }, { 4, 19 }, { 11, 22 },
                { 5, 11 }, { 1, 16 }, { 14, 20 }, { 1, 4 }, { 22, 23 }, { 12, 20 }, { 15, 18 }, { 12, 16 } },
                new int[] { 1, 4 });
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
