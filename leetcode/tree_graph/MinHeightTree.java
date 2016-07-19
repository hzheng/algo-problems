import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/minimum-height-trees/
//
// For a undirected graph with tree characteristics, we can choose any node as
// the root. The result graph is then a rooted tree. Among all possible rooted
// trees, those with minimum height are called minimum height trees (MHTs).
// Given such a graph, write a function to find all the MHTs and return a list
// of their root labels. The graph contains n nodes which are labeled from 0
// to n - 1. You will be given the number n and a list of undirected edges
// You can assume that no duplicate edges will appear in edges.
public class MinHeightTree {
    // beats 5.19%(385 ms)
    public List<Integer> findMinHeightTrees(int n, int[][] edges) {
        if (edges.length == 0 || n < 2) return Arrays.asList(0);

        Map<Integer, Set<Integer> > adjancencies = new HashMap<>();
        for (int[] edge : edges) {
            addAdjacency(adjancencies, edge[0], edge[1]);
            addAdjacency(adjancencies, edge[1], edge[0]);
        }
        while (adjancencies.size() > 2) {
            // keep removing all leaves
            List<Integer> leaves = new LinkedList<>();
            for (Map.Entry<Integer, Set<Integer> > entry
                 : adjancencies.entrySet()) {
                if (entry.getValue().size() == 1) {
                    leaves.add(entry.getKey());
                }
            }
            for (Integer leaf : leaves) {
                Integer adjancency = adjancencies.get(leaf).iterator().next();
                adjancencies.get(adjancency).remove(leaf);
                adjancencies.remove(leaf);
            }
        }
        return new ArrayList<>(adjancencies.keySet());
    }

    private void addAdjacency(Map<Integer, Set<Integer> > adjancencies,
                              int i, int j) {
        if (!adjancencies.containsKey(i)) {
            adjancencies.put(i, new HashSet<>());
        }
        adjancencies.get(i).add(j);
    }

    public List<Integer> findMinHeightTrees2(int n, int[][] edges) {
        if (edges.length == 0 || n < 2) return Arrays.asList(0);

        Map<Integer, Set<Integer> > adjancencies = new HashMap<>();
        for (int[] edge : edges) {
            addAdjacency(adjancencies, edge[0], edge[1]);
            addAdjacency(adjancencies, edge[1], edge[0]);
        }
        return null;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<Integer, int[][], List<Integer> > mhts, int n,
              int[][] edges, Integer ... expected) {
        List<Integer> res = mhts.apply(n, edges);
        assertArrayEquals(expected, res.toArray(new Integer[0]));
    }

    void test(int n, int[][] edges, Integer ... expected) {
        MinHeightTree m = new MinHeightTree();
        test(m::findMinHeightTrees, n, edges, expected);
    }

    @Test
    public void test1() {
        test(1, new int[][] {}, 0);
        test(4, new int[][] {{1, 0}, {1, 2}, {1, 3}}, 1);
        test(6, new int[][] {{0, 1}, {0, 2}, {0, 3}, {3, 4}, {4, 5}}, 3);
        test(6, new int[][] {{0, 3}, {1, 3}, {2, 3}, {4, 3}, {5, 4}}, 3, 4);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MinHeightTree");
    }
}
