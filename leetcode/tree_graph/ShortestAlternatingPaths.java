import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1129: https://leetcode.com/problems/shortest-path-with-alternating-colors/
//
// Consider a directed graph, with nodes labelled 0, 1, ..., n-1.  In this graph, each edge is
// either red or blue, and there could be self-edges or parallel edges. Each [i, j] in red_edges
// denotes a red directed edge from node i to node j. Similarly, each [i, j] in blue_edges denotes
// a blue directed edge from node i to node j. Return an array answer of length n, where each
// answer[X] is the length of the shortest path from node 0 to node X such that the edge colors
// alternate along the path (or -1 if such a path doesn't exist).
//
// Constraints:
// 1 <= n <= 100
// red_edges.length <= 400
// blue_edges.length <= 400
// red_edges[i].length == blue_edges[i].length == 2
// 0 <= red_edges[i][j], blue_edges[i][j] < n
public class ShortestAlternatingPaths {
    // BFS + Queue
    // time complexity: O(R+B), space complexity: O(R+B)
    // 6 ms(63.51%), 40 MB(32.35%) for 90 tests
    public int[] shortestAlternatingPaths(int n, int[][] red_edges, int[][] blue_edges) {
        List[] graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] e : red_edges) {
            graph[e[0]].add(e[1] + 1);
        }
        for (int[] e : blue_edges) {
            graph[e[0]].add(-e[1] - 1);
        }
        int[] res = new int[n];
        Arrays.fill(res, 1, n, -1);
        bfs(res, graph, 0);
        bfs(res, graph, 1);
        return res;
    }

    private void bfs(int[] res, List[] graph, int color) {
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(0);
        Set<Integer> visited = new HashSet<>();
        visited.add(1);
        for (int level = 1; !queue.isEmpty(); level++) {
            for (int size = queue.size(); size > 0; size--) {
                int cur = queue.poll();
                for (int nei : (List<Integer>)graph[cur]) {
                    if ((level % 2 == color) ^ (nei > 0)) { continue; }

                    if (visited.add(nei)) {
                        int index = Math.abs(nei) - 1;
                        int oldVal = res[index];
                        res[index] = Math.min(oldVal < 0 ? Integer.MAX_VALUE : oldVal, level);
                        queue.offer(index);
                    }
                }
            }
        }
    }

    // BFS + Queue
    // time complexity: O(R+B), space complexity: O(R+B)
    // 6 ms(63.51%), 39.6 MB(75.15%) for 90 tests
    public int[] shortestAlternatingPaths2(int n, int[][] red_edges, int[][] blue_edges) {
        Set<Integer>[][] graph = new Set[2][n];
        for (int i = 0; i < n; i++) {
            graph[0][i] = new HashSet<>();
            graph[1][i] = new HashSet<>();
        }
        for (int[] e : red_edges) {
            graph[0][e[0]].add(e[1]);
        }
        for (int[] e : blue_edges) {
            graph[1][e[0]].add(e[1]);
        }
        int[][] dist = new int[2][n];
        Arrays.fill(dist[0], 1, n, -1);
        Arrays.fill(dist[1], 1, n, -1);
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[] {0, 0});
        queue.offer(new int[] {0, 1});
        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int v = cur[0];
            int color = cur[1] ^ 1;
            for (int nei : graph[color][v]) {
                if (dist[color][nei] < 0) { // unvisited
                    dist[color][nei] = 1 + dist[color ^ 1][v];
                    queue.offer(new int[] {nei, color});
                }
            }
        }
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            int d = Math.min(dist[0][i], dist[1][i]);
            res[i] = (d >= 0) ? d : (dist[0][i] >= 0) ? dist[0][i] : dist[1][i];
        }
        return res;
    }

    // DFS + Recursion
    // Time Limit Exceeded
    public int[] shortestAlternatingPaths3(int n, int[][] red_edges, int[][] blue_edges) {
        List[] graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] e : red_edges) {
            graph[e[0]].add(e[1] + 1);
        }
        for (int[] e : blue_edges) {
            graph[e[0]].add(-e[1] - 1);
        }
        int[] res = new int[n];
        for (int i = 1; i < n; i++) {
            res[i] = Math.min(dfs(graph, 0, i, true, new HashSet<>()),
                              dfs(graph, 0, i, false, new HashSet<>()));
            if (res[i] > 2 * n) {
                res[i] = -1;
            }
        }
        return res;
    }

    private int dfs(List[] graph, int src, int dest, boolean red, Set<Integer> visited) {
        if (src == dest) { return 0; }

        int res = Integer.MAX_VALUE / 2;
        for (int nei : (List<Integer>)graph[src]) {
            if ((red ^ (nei > 0)) || !visited.add(nei)) { continue; }

            res = Math.min(res, 1 + dfs(graph, Math.abs(nei) - 1, dest, !red, visited));
            visited.remove(nei);
        }
        return res;
    }

    void test(int n, int[][] red_edges, int[][] blue_edges, int[] expected) {
        assertArrayEquals(expected, shortestAlternatingPaths(n, red_edges, blue_edges));
        assertArrayEquals(expected, shortestAlternatingPaths2(n, red_edges, blue_edges));
        if (n < 20) {
            assertArrayEquals(expected, shortestAlternatingPaths3(n, red_edges, blue_edges));
        }
    }

    @Test public void test() {
        test(5, new int[][] {{0, 1}, {1, 2}, {2, 3}, {3, 4}}, new int[][] {{1, 2}, {2, 3}, {3, 1}},
             new int[] {0, 1, 2, 3, 7});
        test(3, new int[][] {{0, 1}, {1, 2}}, new int[][] {}, new int[] {0, 1, -1});
        test(3, new int[][] {{0, 1}}, new int[][] {{2, 1}}, new int[] {0, 1, -1});
        test(3, new int[][] {{1, 0}}, new int[][] {{2, 1}}, new int[] {0, -1, -1});
        test(3, new int[][] {{0, 1}}, new int[][] {{1, 2}}, new int[] {0, 1, 2});
        test(3, new int[][] {{0, 1}, {0, 2}}, new int[][] {{1, 0}}, new int[] {0, 1, 1});
        test(100, new int[][] {{21, 64}, {74, 59}, {29, 42}, {74, 22}, {12, 7}, {4, 1}, {72, 57},
                               {51, 62}, {55, 39}, {2, 98}, {20, 47}, {4, 34}, {71, 47}, {65, 67},
                               {5, 24}, {88, 29}, {83, 57}, {11, 69}, {84, 24}, {10, 19}, {93, 47},
                               {31, 34}, {49, 65}, {19, 6}, {32, 60}, {58, 77}, {59, 11}, {49, 19},
                               {94, 6}, {71, 66}, {45, 38}, {63, 18}, {75, 90}, {9, 1}, {14, 35},
                               {75, 6}, {59, 80}, {61, 38}, {2, 14}, {64, 82}, {14, 25}, {78, 72},
                               {82, 78}, {49, 2}, {28, 33}, {51, 56}, {42, 18}, {78, 4}, {49, 7},
                               {7, 46}, {93, 9}, {2, 23}, {73, 75}, {69, 32}, {12, 6}, {69, 76},
                               {91, 44}, {17, 70}, {98, 98}, {20, 87}, {95, 91}, {9, 88}, {77, 47},
                               {71, 80}, {10, 2}, {23, 51}, {67, 81}, {33, 12}, {2, 66}, {65, 68},
                               {88, 94}, {73, 23}, {36, 18}, {76, 98}, {77, 0}, {96, 3}, {22, 30},
                               {54, 94}, {3, 42}, {14, 70}, {80, 30}, {81, 80}, {61, 74}, {58, 58},
                               {43, 22}, {10, 84}, {18, 13}, {71, 20}, {10, 61}, {72, 48}, {79, 65},
                               {31, 88}, {66, 76}, {73, 30}, {45, 6}, {21, 79}, {8, 95}, {40, 22},
                               {34, 1}, {25, 43}},
             new int[][] {{34, 45}, {78, 57}, {98, 17}, {8, 14}, {78, 65}, {69, 14}, {91, 1},
                          {38, 22}, {91, 89}, {84, 37}, {76, 3}, {76, 75}, {45, 46}, {75, 52},
                          {68, 34}, {84, 45}, {67, 77}, {41, 70}, {18, 99}, {41, 28}, {18, 16},
                          {65, 20}, {12, 61}, {22, 50}, {28, 1}, {26, 3}, {73, 49}, {85, 0},
                          {53, 78}, {58, 53}, {21, 27}, {14, 21}, {66, 40}, {65, 33}, {85, 2},
                          {95, 90}, {65, 52}, {92, 58}, {48, 48}, {10, 97}, {90, 85}, {74, 22},
                          {79, 79}, {80, 10}, {14, 82}, {82, 51}, {74, 86}, {60, 57}, {60, 48},
                          {13, 91}, {61, 19}, {10, 21}, {85, 5}, {57, 74}, {20, 1}, {24, 18},
                          {4, 55}, {67, 63}, {5, 25}, {93, 29}, {25, 18}, {77, 10}, {93, 26},
                          {98, 53}, {77, 55}, {9, 1}, {68, 11}, {23, 2}, {38, 77}, {7, 63},
                          {73, 93}, {38, 89}, {42, 28}, {33, 27}, {49, 38}, {60, 64}, {79, 35},
                          {11, 15}, {47, 27}, {52, 3}, {7, 36}, {27, 13}, {19, 50}, {96, 3},
                          {26, 21}, {68, 19}, {97, 3}, {76, 54}, {47, 58}, {47, 80}, {55, 63},
                          {21, 41}, {93, 43}, {83, 87}, {60, 26}, {42, 8}, {88, 23}, {73, 23},
                          {83, 6}, {64, 97}, {88, 16}, {10, 33}, {74, 21}, {90, 13}, {0, 5},
                          {16, 67}, {46, 20}, {43, 50}, {31, 91}, {4, 30}, {7, 97}, {1, 13},
                          {94, 15}, {98, 45}, {58, 62}, {39, 82}, {38, 56}, {5, 31}, {16, 19},
                          {24, 49}, {46, 71}, {29, 36}, {64, 49}, {31, 78}, {12, 11}, {9, 69},
                          {58, 55}, {71, 26}, {31, 48}, {5, 79}, {19, 71}, {11, 10}, {99, 37},
                          {34, 26}, {45, 78}, {92, 26}, {80, 63}, {60, 95}, {43, 48}, {41, 48},
                          {39, 7}, {26, 12}, {73, 74}, {39, 62}, {8, 6}, {95, 95}, {58, 44},
                          {73, 15}, {42, 91}, {39, 15}, {77, 73}, {72, 39}, {63, 67}, {64, 71},
                          {49, 1}, {30, 83}, {31, 24}, {3, 96}, {35, 48}, {88, 97}, {82, 30},
                          {35, 2}, {82, 76}, {24, 11}, {7, 53}, {39, 31}, {36, 87}, {23, 20},
                          {66, 50}, {13, 45}, {41, 75}, {67, 10}, {81, 52}, {76, 25}, {5, 12},
                          {74, 78}, {66, 21}, {7, 27}, {81, 35}, {7, 10}, {75, 24}, {69, 13},
                          {72, 70}, {89, 24}, {39, 41}, {14, 5}, {34, 11}, {32, 41}, {72, 87},
                          {55, 25}, {92, 46}, {45, 74}, {53, 84}, {8, 60}, {49, 79}, {30, 31},
                          {5, 83}, {52, 14}, {18, 88}, {60, 82}, {70, 41}, {77, 22}, {66, 6},
                          {5, 0}, {8, 13}, {45, 51}, {69, 32}, {47, 37}, {87, 59}, {46, 57},
                          {91, 21}, {19, 14}, {50, 12}, {18, 45}, {37, 42}, {2, 45}, {8, 1},
                          {79, 94}, {61, 68}, {19, 44}, {97, 8}, {25, 67}, {3, 22}, {39, 40},
                          {75, 56}, {93, 28}, {95, 24}, {12, 32}, {27, 14}, {84, 38}, {51, 17},
                          {21, 12}, {45, 58}, {80, 20}, {43, 8}, {73, 20}, {5, 4}, {70, 43},
                          {61, 33}, {99, 73}, {34, 17}, {55, 49}, {32, 75}, {43, 93}, {42, 82},
                          {13, 3}, {81, 17}, {53, 46}, {48, 12}, {6, 49}, {46, 73}, {22, 13},
                          {67, 73}, {16, 39}, {29, 41}, {47, 31}, {89, 97}, {77, 23}, {91, 58},
                          {14, 8}, {20, 14}, {88, 35}, {41, 8}, {41, 0}, {48, 14}, {15, 23},
                          {48, 56}, {4, 69}, {61, 6}, {76, 41}, {10, 70}, {98, 93}, {86, 39},
                          {93, 44}, {83, 45}, {71, 50}, {52, 17}, {53, 76}, {92, 61}, {20, 56},
                          {70, 26}, {67, 44}, {15, 11}, {15, 6}, {15, 30}, {62, 68}, {2, 29},
                          {40, 93}, {48, 47}, {33, 36}, {47, 23}, {33, 76}, {94, 18}, {64, 20},
                          {70, 55}, {47, 49}, {8, 3}, {72, 0}, {27, 27}, {80, 81}, {94, 80},
                          {12, 43}, {12, 97}, {67, 26}, {82, 59}, {58, 24}, {71, 74}, {27, 66},
                          {6, 4}, {69, 33}, {17, 49}, {37, 29}, {46, 58}, {75, 8}, {0, 40}, {1, 32},
                          {33, 7}, {80, 24}, {43, 55}, {76, 32}, {89, 60}, {50, 69}, {64, 64},
                          {34, 21}, {3, 42}, {51, 61}, {7, 88}, {38, 18}, {56, 61}, {15, 89},
                          {62, 88}, {73, 37}, {74, 48}, {15, 22}, {13, 53}},
             new int[] {0, 7, 4, 5, 7, 1, 6, 4, 7, 10, 5, 3, 6, 3, 5, 7, 7, 9, 3, 4, 5, 7, 2, 7, 2,
                        6, 7, 5, 7, 5, 8, 7, 5, 5, 8, 6, 5, 7, 6, 8, 1, 7, 6, 7, 5, 5, 10, 6, 7, 3,
                        3, 8, 5, 5, 13, 7, 7, 7, 7, 7, 6, 6, 9, 5, 7, 4, 6, 7, 7, 4, 6, 5, 10, 9, 8,
                        10, 9, 7, 8, 8, 6, 7, 7, 9, 6, 15, 9, 6, 5, 7, 9, 5, -1, 9, 6, 7, -1, 5, 8,
                        7});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
