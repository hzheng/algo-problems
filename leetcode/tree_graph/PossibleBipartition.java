import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC890: https://leetcode.com/problems/possible-bipartition/
//
// Given a set of N people (numbered 1, 2, ..., N), we would like to split
// everyone into two groups of any size. Each person may dislike some other
// people, and they should not go into the same group. Return true if and only
// if it is possible to split everyone into two groups in this way.
//
// Note:
// 1 <= N <= 2000
// 0 <= dislikes.length <= 10000
// 1 <= dislikes[i][j] <= N
// dislikes[i][0] < dislikes[i][1]
// There does not exist i != j for which dislikes[i] == dislikes[j].
public class PossibleBipartition {
    // BFS + Queue + Set
    // beats %(475 ms for 65 tests)
    public boolean possibleBipartition(int N, int[][] dislikes) {
        Set<Integer> edges = new HashSet<>();
        for (int[] d : dislikes) {
            edges.add(d[0] * N + d[1]);
        }
        int[] colors = new int[N + 1];
        Queue<Integer> q = new LinkedList<>();
        for (int i = 1; i <= N; i++) {
            if (colors[i] != 0) continue;

            colors[i] = 1;
            for (q.offer(i); !q.isEmpty(); ) {
                int u = q.poll();
                for (int v = 1; v <= N; v++) {
                    int hash = Math.min(u, v) * N + Math.max(u, v);
                    if (!edges.contains(hash)) continue;

                    if (colors[v] == 0) {
                        colors[v] = -colors[u];
                        q.offer(v);
                    } else if (colors[v] == colors[u]) return false;
                }
            }
        }
        return true;
    }

    // BFS + Queue + Set + Hash Table
    // beats %(37 ms for 65 tests)
    // time complexity: O(N + E), space complexity: O(N + E)
    public boolean possibleBipartition2(int N, int[][] dislikes) {
        Map<Integer, List<Integer> > graph = new HashMap<>();
        for (int i = 1; i <= N; i++) {
            graph.put(i, new ArrayList<>());
        }
        for (int[] d : dislikes) {
            graph.get(d[0]).add(d[1]);
            graph.get(d[1]).add(d[0]);
        }
        int[] colors = new int[N + 1];
        Queue<Integer> q = new LinkedList<>();
        for (int i = 1; i <= N; i++) {
            if (colors[i] != 0) continue;

            colors[i] = 1;
            for (q.offer(i); !q.isEmpty(); ) {
                int u = q.poll();
                for (int v : graph.get(u)) {
                    if (colors[v] == 0) {
                        colors[v] = -colors[u];
                        q.offer(v);
                    } else if (colors[v] == colors[u]) return false;
                }
            }
        }
        return true;
    }

    // DFS + Recursion + Hash Table
    // time complexity: O(N + E), space complexity: O(N + E)
    // beats %(38 ms for 65 tests)
    public boolean possibleBipartition3(int N, int[][] dislikes) {
        Map<Integer, List<Integer> > graph = new HashMap<>();
        for (int i = 1; i <= N; i++) {
            graph.put(i, new ArrayList<>());
        }
        for (int[] d : dislikes) {
            graph.get(d[0]).add(d[1]);
            graph.get(d[1]).add(d[0]);
        }
        int[] colors = new int[N + 1];
        for (int i = 1; i <= N; i++) {
            if (colors[i] == 0 && !dfs(i, 1, graph, colors)) return false;
        }
        return true;
    }

    private boolean dfs(int u, int c, Map<Integer, List<Integer> > graph,
                        int[] colors) {
        colors[u] = c;
        for (int v : graph.get(u)) {
            if (colors[v] == c) return false;
            if (colors[v] == 0 && !dfs(v, -c, graph, colors)) return false;
        }
        return true;
    }

    void test(int N, int[][] dislikes, boolean expected) {
        assertEquals(expected, possibleBipartition(N, dislikes));
        assertEquals(expected, possibleBipartition2(N, dislikes));
        assertEquals(expected, possibleBipartition3(N, dislikes));
    }

    @Test
    public void test() {
        test(6, new int[][] { { 5, 6 }, { 1, 2 }, { 1, 3 }, { 2, 3 } }, false);
        test(1, new int[][] {}, true);
        test(4, new int[][] { { 1, 2 }, { 1, 3 }, { 2, 4 } }, true);
        test(3, new int[][] { { 1, 2 }, { 1, 3 }, { 2, 3 } }, false);
        test(5, new int[][] { { 1, 2 }, { 2, 3 }, { 3, 4 }, { 4, 5 }, { 1, 5 } },
             false);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
