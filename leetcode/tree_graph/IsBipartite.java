import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC785: https://leetcode.com/problems/is-graph-bipartite/
//
// Given an undirected graph, return true if and only if it is bipartite.
public class IsBipartite {
    // DFS + Recursion
    // beats 78.75%(5 ms for 78 tests)
    public boolean isBipartite(int[][] graph) {
        int n = graph.length;
        int[] colors = new int[n];
        for (int i = 0; i < n; i++) {
            if (colors[i] == 0) {
                colors[i] = 1;
                if (!dfs(graph, i, colors)) return false;
            }
        }
        return true;
    }
    
    private boolean dfs(int[][] graph, int u, int[] colors) {
        for (int v : graph[u]) {
            if (colors[v] == colors[u]) return false;
            if (colors[v] == 0) {
                colors[v] = -colors[u];
                if (!dfs(graph, v, colors)) return false;
            }
        }
        return true;
    }

    // BFS + Queue
    // beats 35.28%(7 ms for 78 tests)
    public boolean isBipartite2(int[][] graph) {
        int n = graph.length;
        int[] colors = new int[n];
        for (int i = 0; i < n; i++) {
            if (colors[i] == 0 && !bfs(graph, i, colors)) return false;
        }
        return true;
    }

    private boolean bfs(int[][] graph, int start, int[] colors) {
        Queue<Integer> queue = new LinkedList<>();
        colors[start] = 1;
        for (queue.offer(start); !queue.isEmpty(); ) {
            int u = queue.poll();
            for (int v : graph[u]) {
                if (colors[v] == 0) {
                    colors[v] = -colors[u];
                    queue.offer(v);
                } else if (colors[v] == colors[u]) return false;
            }
        }
        return true;
    }

    void test(int[][] graph, boolean expected) {
        assertEquals(expected, isBipartite(graph));
        assertEquals(expected, isBipartite2(graph));
    }

    @Test
    public void test() {
        test(new int[][]{{1,3}, {0,2}, {1,3}, {0,2}}, true);
        test(new int[][]{{1,2,3}, {0,2}, {0,1,3}, {0,2}}, false);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
