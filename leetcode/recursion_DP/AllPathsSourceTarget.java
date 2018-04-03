import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Utils;

// LC797: https://leetcode.com/problems/all-paths-from-source-to-target/
//
// Given a directed, acyclic graph of N nodes. Find all possible paths from node
// 0 to node N-1, and return them in any order.
// The graph is given as follows:  the nodes are 0, 1, ..., graph.length - 1.
// graph[i] is a list of all nodes j for which the edge (i, j) exists.
// Note:
// The number of nodes in the graph will be in the range [2, 15].
// You can print different paths in any order, but you should keep the order of
// nodes inside one path.
public class AllPathsSourceTarget {
    // Recursion + DFS + Backtracking
    // beats 86.30%(8 ms for 26 tests)
    public List<List<Integer> > allPathsSourceTarget(int[][] graph) {
        List<List<Integer> > res = new ArrayList<>();
        dfs(graph, 0, new ArrayList<>(), res);
        return res;
    }

    private void dfs(int[][] graph, int index, List<Integer> path,
                     List<List<Integer> > res) {
        path.add(index);
        if (index == graph.length - 1) {
            res.add(new ArrayList<>(path));
            return;
        }
        for (int next : graph[index]) {
            dfs(graph, next, path, res);
            path.remove(path.size() - 1);
        }
    }

    // Recursion + DFS
    // beats 15.27%(20 ms for 26 tests)
    public List<List<Integer> > allPathsSourceTarget2(int[][] graph) {
        return dfs(graph, 0);
    }

    public List<List<Integer> > dfs(int[][] graph, int node) {
        int n = graph.length;
        List<List<Integer> > res = new ArrayList<>();
        if (node == n - 1) {
            List<Integer> path = new ArrayList<>();
            path.add(n - 1);
            res.add(path);
            return res;
        }
        for (int next : graph[node]) {
            for (List<Integer> path : dfs(graph, next)) {
                path.add(0, node);
                res.add(path);
            }
        }
        return res;
    }

    void test(int[][] graph, int[][] expected) {
        Utils.sort(expected);
        AllPathsSourceTarget a = new AllPathsSourceTarget();
        test(graph, expected, a::allPathsSourceTarget);
        test(graph, expected, a::allPathsSourceTarget2);
    }

    void test(int[][] graph, int[][] expected,
              Function<int[][], List < List < Integer >>> allPaths) {
        int[][] res = Utils.toInts(allPaths.apply(graph));
        assertArrayEquals(expected, Utils.sort(res));
    }

    @Test
    public void test() {
        test(new int[][] {{1, 2}, {3}, {3}, {}},
             new int[][] {{0, 2, 3}, {0, 1, 3}});
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
