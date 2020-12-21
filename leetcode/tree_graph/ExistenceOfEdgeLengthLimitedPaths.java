import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1697: https://leetcode.com/problems/checking-existence-of-edge-length-limited-paths/
//
// An undirected graph of n nodes is defined by edgeList, where edgeList[i] = [ui, vi, disi] denotes
// an edge between nodes ui and vi with distance disi. Note that there may be multiple edges between
// two nodes. Given an array queries, where queries[j] = [pj, qj, limitj], your task is to determine
// for each queries[j] whether there is a path between pj and qj such that each edge on the path has
// a distance strictly less than limitj . Return a boolean array answer, where
// answer.length == queries.length and the jth value of answer is true if there is a path for
// queries[j] is true, and false otherwise.
//
// Constraints:
// 2 <= n <= 10^5
// 1 <= edgeList.length, queries.length <= 10^5
// edgeList[i].length == 3
// queries[j].length == 3
// 0 <= ui, vi, pj, qj <= n - 1
// ui != vi
// pj != qj
// 1 <= disi, limitj <= 10^9
// There may be multiple edges between two nodes.
public class ExistenceOfEdgeLengthLimitedPaths {
    // Heap + Sort + Union Find
    // time complexity: O(N*log(N)+Q*log(N)), space complexity: O(N)
    // 112 ms(40.00%), 66.7 MB(100.00%) for 21 tests
    public boolean[] distanceLimitedPathsExist(int n, int[][] edgeList, int[][] queries) {
        PriorityQueue<int[]> queryQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p[2]));
        int i = 0;
        for (int[] query : queries) {
            queryQueue.offer(new int[] {query[0], query[1], query[2], i++});
        }
        boolean[] res = new boolean[i];
        int[] id = new int[n + 1];
        Arrays.fill(id, -1);
        Arrays.sort(edgeList, Comparator.comparingInt(e -> e[2]));
        for (i = 0; !queryQueue.isEmpty(); ) {
            int[] cur = queryQueue.poll();
            for (; i < edgeList.length && edgeList[i][2] < cur[2]; i++) {
                union(id, edgeList[i][0], edgeList[i][1]);
            }
            res[cur[3]] = (root(id, cur[0]) == root(id, cur[1]));
        }
        return res;
    }

    private boolean union(int[] id, int x, int y) {
        int px = root(id, x);
        int py = root(id, y);
        if (px == py) { return false; }

        if (id[py] < id[px]) {
            int tmp = px;
            px = py;
            py = tmp;
        }
        id[px] += id[py];
        id[py] = px;
        return true;
    }

    private int root(int[] id, int x) {
        int parent = x;
        for (; id[parent] >= 0; parent = id[parent]) {}
        return parent;
    }

    // BFS + Queue
    // time complexity: O(N*Q), space complexity: O(N)
    // Time Limit Exceeded
    public boolean[] distanceLimitedPathsExist2(int n, int[][] edgeList, int[][] queries) {
        Map<Integer, Map<Integer, Integer>> graph = new HashMap<>();
        for (int[] e : edgeList) {
            add(graph, e[0], e[1], e[2]);
            add(graph, e[1], e[0], e[2]);
        }
        boolean[] res = new boolean[queries.length];
        int i = 0;
        for (int[] query : queries) {
            int src = query[0];
            int target = query[1];
            int limit = query[2];
            boolean[] visited = new boolean[n + 1];
            Queue<Integer> queue = new LinkedList<>();
            for (queue.offer(src); !queue.isEmpty(); ) {
                int node = queue.poll();
                visited[node] = true;
                if (node == target) {
                    res[i] = true;
                    break;
                }
                Map<Integer, Integer> neighs = graph.getOrDefault(node, Collections.emptyMap());
                for (int v : neighs.keySet()) {
                    if (!visited[v] && neighs.get(v) < limit) {
                        queue.offer(v);
                    }
                }
            }
            i++;
        }
        return res;
    }

    private void add(Map<Integer, Map<Integer, Integer>> graph, int u, int v, int dist) {
        Map<Integer, Integer> map = graph.computeIfAbsent(u, k -> new HashMap<>());
        Integer old = map.get(v);
        if (old == null || old > dist) {
            map.put(v, dist);
        }
    }

    private void test(int n, int[][] edgeList, int[][] queries, boolean[] expected) {
        assertArrayEquals(expected, distanceLimitedPathsExist(n, edgeList, queries));
        assertArrayEquals(expected, distanceLimitedPathsExist2(n, edgeList, queries));
    }

    @Test public void test() {
        test(3, new int[][] {{0, 1, 2}, {1, 2, 4}, {2, 0, 8}, {1, 0, 16}},
             new int[][] {{0, 1, 2}, {0, 2, 5}}, new boolean[] {false, true});
        test(5, new int[][] {{0, 1, 10}, {1, 2, 5}, {2, 3, 9}, {3, 4, 13}},
             new int[][] {{0, 4, 14}, {1, 4, 13}}, new boolean[] {true, false});
        test(13,
             new int[][] {{9, 1, 53}, {3, 2, 66}, {12, 5, 99}, {9, 7, 26}, {1, 4, 78}, {11, 1, 62},
                          {3, 10, 50}, {12, 1, 71}, {12, 6, 63}, {1, 10, 63}, {9, 10, 88},
                          {9, 11, 59}, {1, 4, 37}, {4, 2, 63}, {0, 2, 26}, {6, 12, 98}, {9, 11, 99},
                          {4, 5, 40}, {2, 8, 25}, {4, 2, 35}, {8, 10, 9}, {11, 9, 25}, {10, 11, 11},
                          {7, 6, 89}, {2, 4, 99}, {10, 4, 63}},
             new int[][] {{9, 7, 65}, {9, 6, 1}, {4, 5, 34}, {10, 8, 43}, {3, 7, 76}, {4, 2, 15},
                          {7, 6, 52}, {2, 0, 50}, {7, 6, 62}, {1, 0, 81}, {4, 5, 35}, {0, 11, 86},
                          {12, 5, 50}, {11, 2, 2}, {9, 5, 6}, {12, 0, 95}, {10, 6, 9}, {9, 4, 73},
                          {6, 10, 48}, {12, 0, 91}, {9, 10, 58}, {9, 8, 73}, {2, 3, 44},
                          {7, 11, 83}, {5, 3, 14}, {6, 2, 33}},
             new boolean[] {true, false, false, true, true, false, false, true, false, true, false,
                            true, false, false, false, true, false, true, false, true, true, true,
                            false, true, false, false});
        test(30, new int[][] {{9, 14, 88}, {11, 27, 51}, {29, 22, 58}, {29, 27, 26}, {18, 20, 97},
                              {25, 4, 12}, {2, 4, 16}, {17, 18, 40}, {21, 9, 37}, {3, 14, 6},
                              {8, 23, 24}, {7, 27, 39}, {24, 16, 95}, {9, 29, 19}, {9, 18, 22},
                              {26, 21, 12}, {12, 14, 81}, {6, 14, 79}, {3, 16, 47}, {13, 19, 18},
                              {24, 15, 59}, {14, 20, 26}, {24, 20, 14}, {25, 26, 7}, {13, 12, 81},
                              {1, 0, 51}, {17, 4, 39}, {8, 16, 77}, {28, 9, 53}, {23, 6, 40},
                              {29, 18, 31}, {10, 9, 35}, {29, 27, 7}, {1, 29, 91}, {10, 19, 70},
                              {28, 29, 58}, {20, 17, 86}, {21, 14, 77}, {19, 4, 43}, {26, 21, 22},
                              {2, 26, 61}, {24, 22, 16}},
             new int[][] {{21, 10, 1}, {14, 2, 21}, {15, 11, 64}, {21, 27, 17}, {3, 26, 1},
                          {26, 18, 93}, {8, 6, 5}, {18, 19, 62}, {19, 18, 30}, {7, 25, 76},
                          {0, 20, 78}, {11, 6, 16}, {16, 2, 91}, {22, 21, 66}, {28, 24, 95},
                          {19, 4, 18}, {14, 23, 37}, {19, 27, 7}, {15, 10, 83}, {23, 5, 59},
                          {17, 12, 9}, {26, 5, 90}, {26, 24, 46}, {21, 29, 30}, {24, 18, 54},
                          {27, 3, 94}, {1, 23, 75}, {28, 22, 75}, {27, 11, 32}, {11, 20, 62},
                          {12, 11, 10}, {17, 14, 4}, {27, 22, 67}, {19, 0, 25}, {16, 24, 38},
                          {23, 6, 35}, {11, 21, 96}, {28, 14, 38}, {29, 17, 8}, {10, 21, 85},
                          {2, 27, 97}, {25, 13, 98}},
             new boolean[] {false, false, true, false, false, true, false, true, false, true, false,
                            false, true, true, true, false, false, false, true, false, false, false,
                            false, false, false, true, false, true, false, true, false, false, true,
                            false, false, false, true, false, false, true, true, true});
        test(50, new int[][] {{9, 30, 62}, {30, 17, 77}, {45, 3, 72}, {7, 20, 65}, {47, 21, 49},
                              {49, 36, 20}, {48, 6, 40}, {15, 37, 83}, {42, 2, 23}, {10, 28, 17},
                              {16, 41, 58}, {21, 12, 76}, {16, 9, 7}, {19, 9, 63}, {5, 47, 20},
                              {12, 3, 18}, {44, 16, 52}, {6, 32, 60}, {19, 25, 25}, {35, 10, 85},
                              {44, 25, 41}, {47, 28, 91}, {39, 44, 81}, {6, 28, 95}, {9, 34, 56},
                              {47, 42, 40}, {23, 42, 94}, {37, 14, 5}, {17, 31, 96}, {3, 0, 76},
                              {29, 0, 100}, {9, 35, 25}, {1, 13, 98}, {24, 29, 39}, {0, 22, 30},
                              {49, 37, 100}, {1, 48, 29}, {5, 4, 80}, {33, 12, 24}, {21, 27, 87},
                              {19, 24, 5}, {29, 24, 86}, {25, 40, 23}, {34, 13, 71}, {43, 31, 12},
                              {20, 47, 68}, {36, 40, 88}, {45, 2, 53}, {29, 36, 60}, {39, 37, 96},
                              {45, 42, 11}, {48, 47, 84}, {37, 43, 49}, {20, 18, 76}, {41, 37, 14},
                              {34, 17, 56}, {20, 44, 80}, {24, 5, 53}, {20, 42, 40}, {15, 48, 14},
                              {17, 34, 23}, {7, 43, 56}, {33, 1, 100}, {39, 21, 85}, {11, 31, 92},
                              {29, 18, 35}, {11, 31, 75}, {45, 33, 60}, {43, 10, 52}, {33, 20, 85},
                              {4, 36, 14}, {32, 42, 45}, {39, 43, 95}, {45, 49, 16}, {10, 46, 65},
                              {15, 21, 90}, {11, 2, 43}, {35, 23, 85}, {26, 16, 34}, {4, 1, 55},
                              {3, 4, 60}, {26, 23, 54}, {23, 19, 85}, {7, 8, 34}, {43, 28, 96},
                              {7, 44, 45}, {19, 43, 48}, {39, 16, 82}, {30, 35, 77}, {23, 18, 57},
                              {21, 2, 4}, {21, 25, 44}, {35, 42, 30}, {24, 37, 87}, {11, 18, 88},
                              {2, 16, 11}, {22, 18, 23}},
             new int[][] {{9, 35, 47}, {46, 10, 49}, {35, 44, 20}, {20, 21, 41}, {47, 41, 27},
                          {32, 42, 27}, {20, 12, 53}, {36, 37, 81}, {8, 24, 3}, {13, 31, 7},
                          {12, 1, 51}, {15, 6, 4}, {2, 9, 63}, {17, 44, 70}, {41, 35, 37},
                          {15, 0, 87}, {29, 35, 84}, {28, 18, 70}, {13, 18, 29}, {0, 42, 3},
                          {12, 5, 30}, {16, 30, 73}, {2, 49, 75}, {35, 42, 63}, {48, 6, 45},
                          {4, 49, 81}, {33, 6, 40}, {6, 42, 46}, {13, 5, 4}, {26, 2, 35},
                          {40, 41, 12}, {29, 48, 10}, {46, 19, 38}, {7, 12, 31}, {21, 10, 45},
                          {24, 0, 62}, {24, 2, 60}, {31, 49, 11}, {14, 12, 36}, {42, 1, 27},
                          {15, 12, 48}, {36, 32, 72}, {35, 7, 1}, {21, 39, 51}, {12, 0, 98},
                          {20, 36, 15}, {44, 34, 40}, {38, 9, 18}, {8, 31, 22}, {25, 42, 5},
                          {8, 18, 13}, {33, 48, 29}, {11, 36, 40}, {20, 29, 60}, {43, 1, 88},
                          {33, 49, 90}, {27, 3, 55}, {6, 5, 36}, {32, 21, 27}, {33, 25, 36},
                          {34, 6, 69}, {24, 29, 59}, {40, 39, 55}, {22, 1, 5}, {27, 39, 58},
                          {48, 30, 31}, {49, 23, 15}, {18, 36, 5}, {7, 42, 2}, {3, 31, 61},
                          {4, 5, 99}, {49, 21, 11}, {43, 33, 26}, {16, 7, 85}, {47, 48, 7},
                          {4, 16, 2}, {16, 3, 93}, {41, 4, 64}, {10, 25, 42}, {27, 8, 44},
                          {21, 20, 45}, {32, 37, 2}, {23, 9, 8}, {47, 14, 79}, {13, 36, 48},
                          {35, 0, 19}, {48, 27, 87}, {38, 14, 52}, {19, 0, 96}, {24, 49, 75},
                          {14, 16, 20}, {5, 31, 9}, {13, 38, 33}, {46, 13, 100}, {11, 23, 48},
                          {20, 39, 63}, {21, 12, 38}},
             new boolean[] {true, false, false, true, false, false, false, true, false, false,
                            false, false, true, true, false, true, true, true, false, false, false,
                            true, true, true, true, true, false, false, false, true, false, false,
                            false, false, false, true, true, false, false, false, false, true,
                            false, false, true, false, false, false, false, false, false, false,
                            false, true, true, true, false, false, false, false, true, true, false,
                            false, false, false, false, false, false, true, true, false, false,
                            true, false, false, true, true, false, false, true, false, false, true,
                            false, false, false, false, true, true, false, false, false, true,
                            false, false, false});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
