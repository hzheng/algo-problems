import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC834: https://leetcode.com/problems/sum-of-distances-in-tree/
//
// An undirected, connected tree with N nodes labelled 0...N-1 and N-1 edges are
// given. The ith edge connects nodes edges[i][0] and edges[i][1] together.
// Return a list ans, where ans[i] is the sum of the distances between node i
// and all other nodes.
public class SumOfDistancesInTree {
    // DFS + Recursion
    // beats %(38 ms for 161 tests)
    public int[] sumOfDistancesInTree(int N, int[][] edges) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] e : edges) {
            graph.get(e[0]).add(e[1]);
            graph.get(e[1]).add(e[0]);
        }
        int[] res = new int[N];
        int[] childCount = new int[N];
        Arrays.fill(childCount, -1);
        childCount[0] = 0;
        dfs(0, 0, graph, childCount, res); // post-order
        dfs(0, graph, childCount, res);    // pre-order
        // or: bfs(graph, childCount, res);
        return res;
    }

    private int dfs(int cur, int depth, List<List<Integer>> graph,
                    int[] childCount, int[] res) {
        int count = 0;
        for (int nei : graph.get(cur)) {
            if (childCount[nei] < 0) {
                childCount[nei] = 0;
                count += dfs(nei, depth + 1, graph, childCount, res) + 1;
                res[0] += depth + 1;
            }
        }
        return childCount[cur] = count;
    }

    private void dfs(int cur, List<List<Integer>> graph,
                     int[] childCount, int[] res) {
        for (int nei : graph.get(cur)) {
            if (res[nei] == 0) {
                res[nei] = res[cur] + res.length - (childCount[nei] + 1) * 2;
                dfs(nei, graph, childCount, res);
            }
        }
    }

    private void bfs(List<List<Integer>> graph, int[] childCount, int[] res) {
        Queue<Integer> queue = new LinkedList<>();
        for (queue.offer(0); !queue.isEmpty(); ) {
            int cur = queue.poll();
            for (int nei : graph.get(cur)) {
                if (res[nei] == 0) {
                    queue.offer(nei);
                    res[nei] = res[cur] + res.length - (childCount[nei] + 1) * 2;
                }
            }
        }
    }

    void test(int N, int[][] edges, int[] expected) {
        assertArrayEquals(expected, sumOfDistancesInTree(N, edges));
    }

    @Test
    public void test() {
        test(1, new int[][] {}, new int[] { 0 });
        test(6, new int[][] {{0, 1}, {0, 2}, {2, 3}, {2, 4}, {2, 5}},
             new int[] {8, 12, 6, 10, 10, 10});
        test(7, new int[][] {{0, 1}, {0, 2}, {2, 3}, {2, 4}, {2, 5}, {5, 6}},
             new int[] {11, 16, 8, 13, 13, 11, 16});
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
