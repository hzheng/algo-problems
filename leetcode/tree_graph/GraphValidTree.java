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
    // DFS + Hash Table + Set + Recursion
    // beats 29.74%(13 ms for 44 tests)
    public boolean validTree(int n, int[][] edges) {
        Map<Integer, List<Integer> > map = new HashMap<>();
        for (int[] edge : edges) {
            save(map, edge[0], edge[1]);
            save(map, edge[1], edge[0]);
        }
        return n < 2 || map.get(0) != null && dfs(0, -1, new HashSet<>(), map) == n - 1;
    }

    private int dfs(int cur, int prev, Set<Integer> visited, Map<Integer, List<Integer> > map) {
        for (int neighbor : map.get(cur)) {
            if (neighbor != prev) {
                if (visited.contains(neighbor)) return 0;

                visited.add(neighbor);
                if (dfs(neighbor, cur, visited, map) == 0) return 0;
            }
        }
        return visited.size();
    }

    private void save(Map<Integer, List<Integer> > map, int a, int b) {
        List<Integer> neighbors = map.get(a);
        if (neighbors == null) {
            map.put(a, neighbors = new ArrayList<>());
        }
        neighbors.add(b);
    }

    // BFS + Hash Table + Set + Queue
    // beats 27.86%(14 ms for 44 tests)
    public boolean validTree2(int n, int[][] edges) {
        if (n < 2) return true;

        Map<Integer, List<Integer> > map = new HashMap<>();
        for (int[] edge : edges) {
            save(map, edge[0], edge[1]);
            save(map, edge[1], edge[0]);
        }
        if (map.get(0) == null) return false;

        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[] {0, -1});
        boolean[] visited = new boolean[n];
        int count = 1;
        while (!queue.isEmpty()) {
            // for (int i = queue.size(); i > 0; i--) {
            int[] head = queue.poll();
            int cur = head[0];
            int prev = head[1];
            for (int neighbor : map.get(cur)) {
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

    // BFS + Hash Table + Queue
    // beats 25.83%(15 ms for 44 tests)
    public boolean validTree3(int n, int[][] edges) {
        if (n < 2) return true;

        Map<Integer, List<Integer> > map = new HashMap<>();
        for (int[] edge : edges) {
            save(map, edge[0], edge[1]);
            save(map, edge[1], edge[0]);
        }
        if (map.get(0) == null) return false;

        Queue<Integer> queue = new LinkedList<>();
        int count = 0;
        for (queue.offer(0); !queue.isEmpty(); count++) {
            int cur = queue.poll();
            List<Integer> neighbors = map.remove(cur);
            if (neighbors == null) return false;

            for (int neighbor : neighbors) {
                map.get(neighbor).remove((Object)cur);
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
