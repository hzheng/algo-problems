import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC261: https://leetcode.com/problems/graph-valid-tree/
//
// Given n nodes labeled from 0 to n - 1 and a list of undirected edges, write a
// function to check whether these edges make up a valid tree.
// Note: you can assume that no duplicate edges will appear in edges. Since all
// edges are undirected, [0, 1] is the same as [1, 0] and thus will not appear
// together in edges.
public class GraphValidTree {
    // DFS + List + Set + Recursion
    // beats 40.14%(8 ms for 44 tests)
    public boolean validTree(int n, int[][] edges) {
        return n < 2 || dfs(0, -1, new HashSet<>(), createAdjacencyList(n, edges)) == n;
    }

    private int dfs(int cur, int prev, Set<Integer> visited, List<Integer>[] list) {
        visited.add(cur);
        for (int neighbor : list[cur]) {
            if (neighbor != prev && (visited.contains(neighbor)
                                     || dfs(neighbor, cur, visited, list) == 0)) return 0;
        }
        return visited.size();
    }

    private List<Integer>[] createAdjacencyList(int n, int[][] edges) {
        @SuppressWarnings("unchecked")
        List<Integer>[] adjacencyList = new List[n];
        for (int i = 0; i < n; i++) {
            adjacencyList[i] = new ArrayList<>();
        }
        for (int[] edge : edges) {
            adjacencyList[edge[0]].add(edge[1]);
            adjacencyList[edge[1]].add(edge[0]);
        }
        return adjacencyList;
    }

    // BFS + List + Set + Queue
    // beats 34.69%(10 ms for 44 tests)
    public boolean validTree2(int n, int[][] edges) {
        List<Integer>[] adjacencyList = createAdjacencyList(n, edges);
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[] {0, -1});
        boolean[] visited = new boolean[n];
        int count = 1;
        while (!queue.isEmpty()) {
            // for (int i = queue.size(); i > 0; i--) {
            int[] head = queue.poll();
            int cur = head[0];
            int prev = head[1];
            for (int neighbor : adjacencyList[cur]) {
                if (neighbor != prev) {
                    if (visited[neighbor]) return false;

                    visited[neighbor] = true;
                    queue.offer(new int[] {neighbor, cur});
                    count++;
                }
            }
        }
        return count == n;
    }

    // BFS + List + Queue
    // beats 34.69%(10 ms for 44 tests)
    public boolean validTree3(int n, int[][] edges) {
        List<Integer>[] adjacencyList = createAdjacencyList(n, edges);
        Queue<Integer> queue = new LinkedList<>();
        int count = 0;
        for (queue.offer(0); !queue.isEmpty(); count++) {
            int cur = queue.poll();
            List<Integer> neighbors = adjacencyList[cur];
            if (neighbors == null) return false;

            adjacencyList[cur] = null;
            for (int neighbor : neighbors) {
                adjacencyList[neighbor].remove((Object)cur);
                queue.offer(neighbor);
            }
        }
        return count == n;
    }

    void test(int n, int[][] edges, boolean expected) {
        assertEquals(expected, validTree(n, edges));
        assertEquals(expected, validTree2(n, edges));
        assertEquals(expected, validTree3(n, edges));
    }

    @Test
    public void test() {
        test(1, new int[][] {}, true);
        test(2, new int[][] {}, false);
        test(4, new int[][] {{0, 1}, {2, 3}}, false);
        test(4, new int[][] {{0, 1}, {2, 3}}, false);
        test(4, new int[][] {{0, 1}, {0, 2}, {1, 2}}, false);
        test(5, new int[][] {{0, 1}, {0, 2}, {1, 3}}, false);
        test(4, new int[][] {{2, 3}, {1, 2}, {1, 3}}, false);
        test(4, new int[][] {{2, 3}, {1, 2}, {2, 0}}, true);
        test(5, new int[][] {{0, 1}, {0, 2}, {0, 3}, {1, 4}}, true);
        test(5, new int[][] {{0, 1}, {0, 2}, {0, 3}, {2, 4}}, true);
        test(5, new int[][] {{0, 1}, {0, 2}, {1, 2}, {2, 3}, {2, 4}}, false);
        test(5, new int[][] {{0, 1}, {1, 2}, {2, 3}, {1, 3}, {1, 4}}, false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("GraphValidTree");
    }
}
