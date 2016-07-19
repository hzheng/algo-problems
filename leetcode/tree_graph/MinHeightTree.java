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
            // the following needs to search all adjancencies again,
            // while BFS(e.g. next solution) only searches those vertices that
            // are next to last leaves.
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

    // BFS
    // beats 88.15%(32 ms)
    public List<Integer> findMinHeightTrees2(int n, int[][] edges) {
        if (edges.length == 0 || n < 2) return Arrays.asList(0);

        @SuppressWarnings("unchecked")
        List<Integer>[] adjancencies = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adjancencies[i] = new ArrayList<>();
        }
        for (int i = 0; i < edges.length; i++) {
            int v1 = edges[i][0];
            int v2 = edges[i][1];
            adjancencies[v1].add(v2);
            adjancencies[v2].add(v1);
        }
        List<Integer> leaves = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (adjancencies[i].size() == 1) {
                leaves.add(i);
            }
        }

        for (int count = n; count > 2; ) {
            List<Integer> nextLeaves = new LinkedList<>();
            for (Integer leaf : leaves) {
                for (int adjancency : adjancencies[leaf]) {
                    adjancencies[adjancency].remove(leaf);
                    if (adjancencies[adjancency].size() == 1) {
                        nextLeaves.add(adjancency);
                    }
                }
            }
            count -= leaves.size();
            leaves = nextLeaves;
        }
        return leaves;
    }

    // beats 15.71%(93 ms)
    public List<Integer> findMinHeightTrees3(int n, int[][] edges) {
        if (edges.length == 0 || n < 2) return Arrays.asList(0);

        @SuppressWarnings("unchecked")
        List<Integer>[] adjancencies = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adjancencies[i] = new ArrayList<>();
        }
        for (int i = 0; i < edges.length; i++) {
            int v1 = edges[i][0];
            int v2 = edges[i][1];
            adjancencies[v1].add(v2);
            adjancencies[v2].add(v1);
        }

        int[] degrees = new int[n];
        for (int i = 0; i < n; i++) {
            degrees[i] = adjancencies[i].size();
        }

        for (int vertices = n; vertices > 2; ) {
            List<Integer> leaves = new LinkedList<>();
            for (int i = 0; i < n; i++) {
                if (degrees[i] == 1) {
                    leaves.add(i);
                    degrees[i] = -1;
                    vertices--;
                }
            }
            for (Integer leaf : leaves) {
                Integer leafAdj = adjancencies[leaf].get(0);
                degrees[leafAdj]--;
                adjancencies[leafAdj].remove(leaf);
            }
        }
        List<Integer> res = new ArrayList<>(2);
        for (int i = 0; i < n; i++) {
            if (degrees[i] != -1) {
                res.add(i);
            }
        }
        return res;
    }

    // beats 8.79%(271 ms)
    public List<Integer> findMinHeightTrees4(int n, int[][] edges) {
        @SuppressWarnings("unchecked")
        Set<Integer>[] adjancencies = new HashSet[n];
        Set<Integer> res = new HashSet<>();
        for (int i = 0; i < n; ++i) {
            res.add(i);
        }
        for (int i = 0; i < n; i++) {
            adjancencies[i] = new HashSet<>();
        }

        for (int i = edges.length - 1; i >= 0; i--) {
            int v1 = edges[i][0];
            int v2 = edges[i][1];
            adjancencies[v1].add(v2);
            adjancencies[v2].add(v1);
        }

        Set<Integer> leaves = new HashSet<>();
        while (res.size() > 2) {
            for (int v : res) {
                if (adjancencies[v].size() == 1) {
                    leaves.add(v);
                }
            }
            res.removeAll(leaves);
            Set<Integer> nextLeaves = new HashSet<>();
            for (int v : leaves) {
                int leafAdj = adjancencies[v].iterator().next();
                adjancencies[leafAdj].remove(v);
                if (adjancencies[leafAdj].size() == 1) {
                    nextLeaves.add(leafAdj);
                }
            }
            leaves = nextLeaves;
        }
        return new ArrayList<>(res);
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
        test(m::findMinHeightTrees2, n, edges, expected);
        test(m::findMinHeightTrees3, n, edges, expected);
        test(m::findMinHeightTrees4, n, edges, expected);
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
