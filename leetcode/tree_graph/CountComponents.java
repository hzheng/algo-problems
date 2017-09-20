import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC323: https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph
//
// Given n nodes labeled from 0 to n - 1 and a list of undirected edges, write a
// function to find the number of connected components in an undirected graph.
public class CountComponents {
    // Union Find
    // beats 57.74%(6 ms for 45 tests)
    public int countComponents(int n, int[][] edges) {
        int[] id = new int[n];
        for (int i = 0; i < n; i++) {
            id[i] = i;
        }
        for (int[] edge : edges) {
            int p = edge[0];
            int q = edge[1];
            for (; id[p] != p; p = id[p]) {}
            for (; id[q] != q; q = id[q]) {}
            id[q] = p;
        }
        int count = 0;
        for (int i = 0; i < n; i++) {
            count += (id[i] == i) ? 1 : 0;
        }
        return count;
    }

    // Union Find
    // beats 71.23%(3 ms for 45 tests)
    public int countComponents_2(int n, int[][] edges) {
        int[] id = new int[n];
        for (int i = 0; i < n; i++) {
            id[i] = i;
        }
        for (int[] edge : edges) {
            int p = edge[0];
            int q = edge[1];
            for (; id[p] != p; p = id[p] = id[id[p]]) {}
            for (; id[q] != q; q = id[q] = id[id[q]]) {}
            id[q] = p;
        }
        int count = 0;
        for (int i = 0; i < n; i++) {
            count += (id[i] == i) ? 1 : 0;
        }
        return count;
    }

    public int countComponents_3(int n, int[][] edges) {
        int[] id = new int[n];
        for (int i = 0; i < n; i++) {
            id[i] = i;
        }
        int count = n;
        for (int[] edge : edges) {
            int p = edge[0];
            int q = edge[1];
            for (; id[p] != p; p = id[p] = id[id[p]]) {}
            for (; id[q] != q; q = id[q] = id[id[q]]) {}
            if (p != q) {
                count--;
                id[q] = p;
            }
        }
        return count;
    }

    // DFS + Recursion + Set
    // beats 46.32%(8 ms for 45 tests)
    public int countComponents2(int n, int[][] edges) {
        List<Integer>[] adjacencyList = createAdjacencyList(n, edges);
        int count = 0;
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                dfs(i, adjacencyList, visited);
                count++;
            }
        }
        return count;
    }

    private void dfs(int cur, List<Integer>[] adjacencyList, boolean[] visited) {
        visited[cur] = true;
        for (int neighbor : adjacencyList[cur]) {
            if (!visited[neighbor]) {
                dfs(neighbor, adjacencyList, visited);
            }
        }
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

    // BFS + Queue + Set
    // beats 31.04%(18 ms for 45 tests)
    public int countComponents3(int n, int[][] edges) {
        List<Integer>[] adjacencyList = createAdjacencyList(n, edges);
        int count = 0;
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                bfs(i, adjacencyList, visited);
                count++;
            }
        }
        return count;
    }

    private void bfs(int cur, List<Integer>[] adjacencyList, boolean[] visited) {
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(cur);
        while (!queue.isEmpty()) {
            int head = queue.poll();
            visited[head] = true;
            for (int neighbor : adjacencyList[head]) {
                if (!visited[neighbor]) {
                    queue.add(neighbor);
                }
            }
        }
    }

    void test(int n, int[][] edges, int expected) {
        assertEquals(expected, countComponents(n, edges));
        assertEquals(expected, countComponents_2(n, edges));
        assertEquals(expected, countComponents_3(n, edges));
        assertEquals(expected, countComponents2(n, edges));
        assertEquals(expected, countComponents3(n, edges));
    }

    @Test
    public void test() {
        test(3, new int[][] {{1, 0}, {2, 0}}, 1);
        test(4, new int[][] {{2, 3}, {1, 2}, {1, 3}}, 2);
        test(5, new int[][] {{0, 1}, {0, 2}, {1, 2}, {2, 3}, {2, 4}}, 1);
        test(5, new int[][] {{0, 1}, {1, 2}, {3, 4}}, 2);
        test(5, new int[][] {{0, 1}, {1, 2}, {2, 3}, {3, 4}}, 1);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CountComponents");
    }
}
