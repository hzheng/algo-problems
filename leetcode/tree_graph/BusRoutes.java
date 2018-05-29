import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC815: https://leetcode.com/problems/bus-routes/
//
// Each routes[i] is a bus route that the i-th bus repeats forever. We start at
// bus stop S (initially not on a bus), and we want to go to bus stop T.
// Travelling by buses only, what is the least number of buses we must take to
// reach our destination? Return -1 if it is not possible.
// Note:
// 1 <= routes.length <= 500.
// 1 <= routes[i].length <= 500.
// 0 <= routes[i][j] < 10 ^ 6.
public class BusRoutes {
    // BFS + Queue + Set
    // beats %(87 ms for 45 tests)
    public int numBusesToDestination(int[][] routes, int S, int T) {
        if (S == T) return 0;

        int i = 0;
        Map<Integer, Set<Integer> > stops = new HashMap<>();
        for (int[] route : routes) {
            for (int stop : route) {
                Set<Integer> buses = stops.get(stop);
                if (buses == null) {
                    stops.put(stop, buses = new HashSet<>());
                }
                buses.add(i);
            }
            i++;
        }
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(S);
        Set<Integer> visitedBuses = new HashSet<>();
        Set<Integer> visitedStops = new HashSet<>();
        for (int take = 1; !queue.isEmpty(); take++) {
            for (i = queue.size(); i > 0; i--) {
                for (int bus : stops.get(queue.poll())) {
                    if (!visitedBuses.add(bus)) continue;

                    for (int nStop : routes[bus]) {
                        if (nStop == T) return take;

                        // if (visited.add(nStop) && stops.get(nStop).size() > 1) { // 90 ms
                        // if (visited.add(nStop)) { // TLE
                        if (visitedStops.add(nStop)) {
                            queue.offer(nStop);
                        }
                    }
                }
            }
        }
        return -1;
    }

    // BFS + Queue + Set
    // beats %(86 ms for 45 tests)
    public int numBusesToDestination_2(int[][] routes, int S, int T) {
        if (S == T) return 0;

        int i = 0;
        Map<Integer, Set<Integer> > stops = new HashMap<>();
        for (int[] route : routes) {
            for (int stop : route) {
                Set<Integer> buses = stops.get(stop);
                if (buses == null) {
                    stops.put(stop, buses = new HashSet<>());
                }
                buses.add(i);
            }
            i++;
        }
        Queue<int[]> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        for (queue.offer(new int[]{S, 1}); !queue.isEmpty(); ) {
            int[] busTake = queue.poll();
            int stop = busTake[0];
            int take = busTake[1];
            for (int bus : stops.get(stop)) {
                if (!visited.add(bus)) continue; // 90 ms

                for (int nStop : routes[bus]) {
                    if (nStop == T) return take;

                    // if (visited.add(nStop) && stops.get(nStop).size() > 1) { // 74 ms
                    // if (visited.add(nStop)) { // 892 ms
                    queue.offer(new int[]{nStop, take + 1});
                    // }
                }
            }
        }
        return -1;
    }

    // BFS + Queue + Set + Sort
    // time complexity: O(N * (N + sum(bi)), space complexity: O(N ^ 2 + sum(bi))
    // where bi is the number of stops on the i-th bus.
    // beats %(71 ms for 45 tests)
    public int numBusesToDestination2(int[][] routes, int S, int T) {
        if (S == T) return 0;

        int n = routes.length;
        List<List<Integer> > graph = new ArrayList<>();
        for (int[] route : routes) {
            Arrays.sort(route);
            graph.add(new ArrayList<>());
        }
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (intersect(routes[i], routes[j])) {
                    graph.get(i).add(j);
                    graph.get(j).add(i);
                }
            }
        }
        Queue<Integer> queue = new ArrayDeque<>();
        Set<Integer> targets = new HashSet<>();
        Set<Integer> visited = new HashSet<>();
        for (int i = 0; i < n; i++) {
            if (Arrays.binarySearch(routes[i], S) >= 0) {
                visited.add(i);
                queue.offer(i);
            }
            if (Arrays.binarySearch(routes[i], T) >= 0) {
                targets.add(i);
            }
        }
        for (int take = 1; !queue.isEmpty(); take++) {
            for (int i = queue.size(); i > 0; i--) {
                int bus = queue.poll();
                if (targets.contains(bus)) return take;

                for (int nei : graph.get(bus)) {
                    if (visited.add(nei)) {
                        queue.offer(nei);
                    }
                }
            }
        }
        return -1;
    }

    private boolean intersect(int[] A, int[] B) {
        for (int i = 0, j = 0; i < A.length && j < B.length; ) {
            if (A[i] == B[j]) return true;

            if (A[i] < B[j]) {
                i++;
            } else {
                j++;
            }
        }
        return false;
    }

    void test(int[][] routes, int S, int T, int expected) {
        assertEquals(expected, numBusesToDestination(routes, S, T));
        assertEquals(expected, numBusesToDestination_2(routes, S, T));
        assertEquals(expected, numBusesToDestination2(routes, S, T));
    }

    @Test
    public void test() {
        test(new int[][] { { 1, 7 }, { 3, 5 } }, 5, 5, 0);
        test(new int[][] { { 1, 2, 7 }, { 3, 6, 7 } }, 1, 7, 1);
        test(new int[][] { { 1, 2, 7 }, { 3, 6, 7 } }, 1, 6, 2);
        int[] a = new int[300 * 300];
        for (int i = 0; i < 300; i++) {
            for (int j = 0; j < 300; j++) {
                a[i * 300 + j] = j;
            }
        }
        test(new int[][] { a, { 299, 300 } }, 0, 300, 2);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
