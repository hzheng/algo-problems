import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC743: https://leetcode.com/problems/network-delay-time/
//
// There are N network nodes, labelled 1 to N.
// Given times, a list of travel times as directed edges times[i] = (u, v, w),
// where u is the source node, v is the target node, and w is the time it takes
// for a signal to travel from source to target.
// Now, we send a signal from a certain node K. How long will it take for all
// nodes to receive the signal? If it is impossible, return -1.
// Note:
// N will be in the range [1, 100].
// K will be in the range [1, N].
// The length of times will be in the range [1, 6000].
// All edges times[i] = (u, v, w) will have 1 <= u, v <= N and 1 <= w <= 100.
public class NetworkDelayTime {
    // Dijkstra's algorithm
    // Heap + Set
    // time complexity: O(E * log(E)), space complexity: O(N + E)
    // 20 ms(44.43%), 41.8 MB(87.88%) for 52 tests
    public int networkDelayTime(int[][] times, int N, int K) {
        Map<Integer, List<int[]>> graph = new HashMap<>();
        for (int[] edge : times) {
            List<int[]> edges = graph.computeIfAbsent(edge[0], k -> new ArrayList<>());
            edges.add(new int[] {edge[1], edge[2]});
        }
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.offer(new int[] {K, 0});
        Set<Integer> visited = new HashSet<>();
        int max = 0;
        while (!pq.isEmpty()) {
            int[] head = pq.poll();
            int target = head[0];
            if (!visited.add(target)) { continue; }

            int time = head[1];
            max = Math.max(max, time);
            for (int[] edge : graph.getOrDefault(target, Collections.emptyList())) {
                if (!visited.contains(edge[0])) {
                    pq.offer(new int[] {edge[0], time + edge[1]});
                }
            }
        }
        return (visited.size() != N) ? -1 : max;
    }

    // Dijkstra's algorithm
    // Hash Table
    // time complexity: O(N ^ 2 + E), space complexity: O(N + E)
    // beats %(41 ms for 51 tests)
    public int networkDelayTime2(int[][] times, int N, int K) {
        Map<Integer, List<int[]>> graph = new HashMap<>();
        for (int[] edge : times) {
            List<int[]> edges = graph.computeIfAbsent(edge[0], k -> new ArrayList<>());
            edges.add(new int[] {edge[1], edge[2]});
        }
        Map<Integer, Integer> timeMap = new HashMap<>();
        for (int i = 1; i <= N; i++) {
            timeMap.put(i, Integer.MAX_VALUE);
        }
        timeMap.put(K, 0);
        boolean[] visited = new boolean[N + 1];
        while (true) {
            int nearest = -1;
            int time = Integer.MAX_VALUE;
            for (int v = 1; v <= N; ++v) {
                if (!visited[v] && timeMap.get(v) < time) {
                    time = timeMap.get(v);
                    nearest = v;
                }
            }
            if (nearest < 0) { break; }

            visited[nearest] = true;
            for (int[] edge : graph.getOrDefault(nearest, Collections.emptyList())) {
                timeMap.put(edge[0], Math.min(timeMap.get(edge[0]), time + edge[1]));
            }
        }
        int res = 0;
        for (int v : timeMap.values()) {
            if (v == Integer.MAX_VALUE) { return -1; }

            res = Math.max(res, v);
        }
        return res;
    }

    // Sort + DFS + Recursion
    // time complexity: O(N ^ N + E * log(E)), space complexity: O(N + E)
    // 36 ms(19.06%), 41.7 MB(87.88%) for 52 tests
    public int networkDelayTime3(int[][] times, int N, int K) {
        Map<Integer, List<int[]>> graph = new HashMap<>();
        for (int[] edge : times) {
            List<int[]> edges = graph.computeIfAbsent(edge[0], k -> new ArrayList<>());
            edges.add(new int[] {edge[1], edge[2]});
        }
        for (List<int[]> edges : graph.values()) {
            edges.sort(Comparator.comparingInt(a -> a[1]));
        }
        int[] timeMap = new int[N];
        Arrays.fill(timeMap, Integer.MAX_VALUE);
        dfs(graph, K, 0, timeMap);
        int res = 0;
        for (int v : timeMap) {
            if (v == Integer.MAX_VALUE) { return -1; }

            res = Math.max(res, v);
        }
        return res;
    }

    private void dfs(Map<Integer, List<int[]>> graph, int cur, int time, int[] timeMap) {
        if (time >= timeMap[cur - 1]) { return; }

        timeMap[cur - 1] = time;
        for (int[] edge : graph.getOrDefault(cur, Collections.emptyList())) {
            dfs(graph, edge[0], time + edge[1], timeMap);
        }
    }

    // BFS + Queue
    // beats %(16 ms for 51 tests)
    public int networkDelayTime4(int[][] times, int N, int K) {
        int[] delay = new int[N + 1];
        Arrays.fill(delay, Integer.MAX_VALUE);
        delay[K] = 0;
        Integer[][] edges = new Integer[N + 1][N + 1];
        for (int[] time : times) {
            edges[time[0]][time[1]] = time[2];
        }
        Queue<Integer> queue = new LinkedList<>();
        for (queue.offer(K); !queue.isEmpty(); ) {
            boolean[] visited = new boolean[N + 1];
            for (int i = queue.size(); i > 0; i--) {
                int u = queue.poll();
                for (int v = 1; v <= N; v++) {
                    if (edges[u][v] != null && delay[u] + edges[u][v] < delay[v]) {
                        if (!visited[v]) {
                            visited[v] = true;
                            queue.offer(v);
                        }
                        delay[v] = delay[u] + edges[u][v];
                    }
                }
            }
        }
        int res = 0;
        for (int i = 1; i <= N; i++) {
            res = Math.max(res, delay[i]);
        }
        return res == Integer.MAX_VALUE ? -1 : res;
    }

    // Bellman–Ford algorithm
    // time complexity: O(N * E), space complexity: O(N)
    // beats %(72 ms for 51 tests)
    public int networkDelayTime5(int[][] times, int N, int K) {
        int[] delay = new int[N + 1];
        Arrays.fill(delay, Integer.MAX_VALUE);
        delay[K] = 0;
        for (int i = 1; i < N; i++) {
            for (int[] e : times) {
                int u = e[0];
                int v = e[1];
                if (delay[u] != Integer.MAX_VALUE) {
                    delay[v] = Math.min(delay[v], delay[u] + e[2]);
                }
            }
        }
        int res = 0;
        for (int i = 1; i <= N; i++) {
            if (delay[i] == Integer.MAX_VALUE) { return -1; }

            res = Math.max(res, delay[i]);
        }
        return res;
    }

    // Bellman–Ford algorithm
    // time complexity: O(N ^ 3), space complexity: O(N)
    // beats %(75 ms for 51 tests)
    public int networkDelayTime5_2(int[][] times, int N, int K) {
        int[][] edges = new int[N + 1][N + 1];
        for (int[] edge : edges) {
            Arrays.fill(edge, Integer.MAX_VALUE);
            // cannot use 0 because one of test data contains 0 weight
            // even though it conflicts to the Note
        }
        for (int[] e : times) {
            edges[e[0]][e[1]] = e[2];
        }
        int[] delay = new int[N + 1];
        Arrays.fill(delay, Integer.MAX_VALUE);
        delay[K] = 0;
        for (int i = 1; i < N; i++) {
            for (int u = 1; u <= N; u++) {
                for (int v = 1; v <= N; v++) {
                    if (edges[u][v] != Integer.MAX_VALUE && delay[u] != Integer.MAX_VALUE) {
                        delay[v] = Math.min(delay[v], delay[u] + edges[u][v]);
                    }
                }
            }
        }
        int res = 0;
        for (int i = 1; i <= N; i++) {
            if (delay[i] == Integer.MAX_VALUE) { return -1; }

            res = Math.max(res, delay[i]);
        }
        return res;
    }

    void test(int[][] times, int N, int K, int expected) {
        assertEquals(expected, networkDelayTime(times, N, K));
        assertEquals(expected, networkDelayTime2(times, N, K));
        assertEquals(expected, networkDelayTime3(times, N, K));
        assertEquals(expected, networkDelayTime4(times, N, K));
        assertEquals(expected, networkDelayTime5(times, N, K));
        assertEquals(expected, networkDelayTime5_2(times, N, K));
    }

    @Test public void test() {
        test(new int[][] {{1, 2, 1}, {2, 3, 2}, {1, 3, 4}}, 3, 1, 3);
        test(new int[][] {{2, 1, 1}, {2, 3, 1}, {3, 4, 1}}, 4, 2, 2);
        test(new int[][] {{1, 2, 1}}, 2, 2, -1);
        test(new int[][] {{15, 8, 1}, {7, 10, 41}, {7, 9, 34}, {9, 4, 31}, {12, 13, 50},
                          {14, 3, 52}, {4, 11, 99}, {4, 7, 86}, {10, 13, 57}, {9, 6, 10},
                          {1, 7, 51}, {7, 15, 38}, {1, 9, 11}, {12, 7, 94}, {9, 13, 34},
                          {11, 7, 79}, {7, 6, 28}, {5, 3, 34}, {2, 6, 97}, {14, 1, 97}, {6, 10, 90},
                          {12, 10, 37}, {13, 3, 73}, {11, 14, 7}, {15, 1, 39}, {6, 5, 90},
                          {13, 6, 43}, {6, 9, 32}, {4, 6, 45}, {11, 10, 2}, {2, 13, 4},
                          {14, 15, 29}, {1, 14, 88}, {14, 6, 19}, {6, 2, 29}, {3, 14, 72},
                          {1, 15, 4}, {11, 5, 2}, {6, 7, 56}, {8, 7, 88}, {13, 14, 70},
                          {14, 12, 58}, {14, 2, 86}, {11, 3, 57}, {5, 2, 56}, {3, 10, 26},
                          {2, 11, 21}, {14, 5, 54}, {5, 12, 40}, {14, 4, 81}, {15, 2, 99},
                          {5, 7, 57}, {13, 12, 5}, {4, 9, 60}, {12, 15, 48}, {6, 14, 1}, {9, 7, 44},
                          {13, 7, 69}, {5, 13, 42}, {4, 1, 7}, {11, 9, 76}, {8, 1, 76}, {5, 14, 29},
                          {2, 3, 69}, {7, 3, 23}, {12, 14, 28}, {11, 4, 85}, {10, 1, 10},
                          {15, 12, 36}, {1, 11, 69}, {15, 10, 96}, {11, 13, 69}, {7, 12, 49},
                          {1, 2, 95}, {6, 4, 46}, {8, 12, 94}, {12, 4, 93}, {13, 5, 31},
                          {12, 2, 60}, {6, 1, 87}, {4, 14, 20}, {5, 11, 89}, {4, 15, 88},
                          {4, 10, 21}, {1, 6, 5}, {10, 8, 26}, {8, 2, 51}, {3, 15, 23}, {7, 2, 12},
                          {11, 1, 47}, {2, 1, 75}, {3, 8, 63}, {8, 10, 19}, {6, 8, 18}, {4, 2, 55},
                          {14, 11, 80}, {10, 3, 73}, {3, 5, 22}, {12, 3, 61}, {1, 13, 33},
                          {9, 3, 98}, {9, 12, 69}, {15, 9, 6}, {7, 13, 76}, {11, 12, 22},
                          {11, 15, 51}, {13, 15, 46}, {5, 10, 58}, {1, 10, 26}, {13, 4, 85},
                          {7, 14, 58}, {5, 8, 46}, {11, 6, 32}, {10, 9, 41}, {9, 14, 35},
                          {14, 13, 60}, {3, 9, 97}, {2, 5, 39}, {7, 11, 19}, {1, 12, 27},
                          {7, 5, 13}, {8, 4, 34}, {9, 15, 25}, {5, 1, 93}, {15, 13, 97},
                          {14, 9, 35}, {8, 6, 67}, {9, 5, 39}, {13, 11, 35}, {7, 4, 21},
                          {12, 9, 64}, {14, 8, 8}, {10, 12, 94}, {8, 9, 76}, {8, 5, 71}, {2, 9, 64},
                          {10, 14, 59}, {1, 4, 74}, {7, 1, 69}, {15, 5, 55}, {6, 15, 80},
                          {13, 8, 84}, {8, 13, 63}, {8, 3, 91}, {10, 4, 87}, {1, 5, 39}, {8, 11, 0},
                          {1, 3, 79}, {4, 5, 82}, {4, 12, 87}, {3, 11, 29}, {7, 8, 92}, {10, 7, 77},
                          {6, 12, 42}, {13, 2, 40}, {9, 10, 13}, {4, 13, 65}, {2, 4, 34},
                          {3, 13, 44}, {2, 14, 69}, {3, 4, 42}, {5, 15, 98}, {14, 7, 6},
                          {15, 3, 94}, {10, 2, 37}, {15, 11, 7}, {9, 2, 15}, {13, 9, 66},
                          {4, 8, 83}, {8, 15, 23}, {13, 1, 50}, {6, 13, 57}, {2, 10, 37},
                          {10, 6, 38}, {2, 7, 45}, {9, 8, 8}, {3, 12, 28}, {3, 2, 83}, {2, 12, 75},
                          {1, 8, 91}, {4, 3, 70}, {12, 6, 48}, {3, 1, 13}, {5, 6, 42}, {6, 11, 96},
                          {3, 6, 22}, {15, 6, 34}, {11, 8, 43}, {15, 7, 40}, {9, 11, 57},
                          {11, 2, 11}, {2, 8, 22}, {9, 1, 73}, {2, 15, 40}, {12, 11, 10},
                          {15, 4, 78}, {12, 8, 75}, {10, 15, 37}, {13, 10, 44}, {8, 14, 33},
                          {3, 7, 82}, {5, 4, 46}, {12, 5, 79}, {15, 14, 43}, {10, 5, 65},
                          {5, 9, 34}, {12, 1, 54}, {6, 3, 16}, {14, 10, 83}, {10, 11, 67}}, 15, 8,
             34);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
