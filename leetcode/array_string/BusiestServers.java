import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1606: https://leetcode.com/problems/find-servers-that-handled-most-number-of-requests/
//
// You have k servers numbered from 0 to k-1 that are being used to handle multiple requests
// simultaneously. Each server has infinite computational capacity but cannot handle more than one
// request at a time. The requests are assigned to servers according to a specific algorithm:
// The ith (0-indexed) request arrives.
// If all servers are busy, the request is dropped (not handled at all).
// If the (i % k)th server is available, assign the request to that server.
// Otherwise, assign the request to the next available server (wrapping around the list of servers
// and starting from 0 if necessary). For example, if the ith server is busy, try to assign the
// request to the (i+1)th server, then the (i+2)th server, and so on.
// You are given a strictly increasing array arrival of positive integers, where arrival[i]
// represents the arrival time of the ith request, and another array load, where load[i] represents
// the load of the ith request (the time it takes to complete). Your goal is to find the busiest
// server(s). A server is considered busiest if it handled the most number of requests successfully
// among all the servers.
// Return a list containing the IDs (0-indexed) of the busiest server(s). You may return the IDs in
// any order.
//
// Constraints:
// 1 <= k <= 10^5
// 1 <= arrival.length, load.length <= 10^5
// arrival.length == load.length
// 1 <= arrival[i], load[i] <= 10^9
// arrival is strictly increasing.
public class BusiestServers {
    // SortedMap + SortedSet
    // time complexity: O(N*log(K)), space complexity: O(N+K)
    // 174 ms(17.24%), 57.4 MB(94.25%) for 108 tests
    public List<Integer> busiestServers(int k, int[] arrival, int[] load) {
        TreeSet<Integer> free = new TreeSet<>();
        for (int i = 0; i < k ; i++) {
            free.add(i);
        }
        TreeMap<Integer, List<Integer>> job = new TreeMap<>();
        int[] counter = new int[k];
        int maxCount = 0;
        int n = arrival.length;
        for (int i = 0; i < n; i++) {
            int start = arrival[i];
            while (true) {
                var first = job.firstEntry();
                if (first == null || first.getKey() > start) { break; }

                job.pollFirstEntry();
                free.addAll(first.getValue());
            }
            if (free.isEmpty()) { continue; }

            Integer selected = free.ceiling(i % k);
            if (selected == null) {
                selected = free.first();
            }
            maxCount = Math.max(maxCount, ++counter[selected]);
            int end = start + load[i];
            if (i < n - 1 && arrival[i + 1] < end) {
                free.remove(selected);
                job.computeIfAbsent(end, x -> new ArrayList<>()).add(selected);
            }
        }
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            if (counter[i] == maxCount) {
                res.add(i);
            }
        }
        return res;
    }

    // Heap + SortedSet
    // time complexity: O(N*log(K)), space complexity: O(N+K)
    // 103 ms(89.66%), 58.4 MB(60.92%) for 108 tests
    public List<Integer> busiestServers2(int k, int[] arrival, int[] load) {
        TreeSet<Integer> free = new TreeSet<>();
        for (int i = 0; i < k ; i++) {
            free.add(i);
        }
        Queue<int[]> busyServers = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        int[] counter = new int[k];
        int maxCount = 0;
        for (int i = 0, n = arrival.length; i < n; i++) {
            int start = arrival[i];
            while (!busyServers.isEmpty() && busyServers.peek()[0] <= start) {
                free.add(busyServers.poll()[1]);
            }
            if (free.isEmpty()) { continue; }

            Integer selected = free.ceiling(i % k);
            if (selected == null) {
                selected = free.first();
            }
            maxCount = Math.max(maxCount, ++counter[selected]);
            int end = start + load[i];
            if (i < n - 1 && arrival[i + 1] < end) {
                free.remove(selected);
                busyServers.offer(new int[]{end, selected});
            }
        }
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            if (counter[i] == maxCount) {
                res.add(i);
            }
        }
        return res;
    }

    private void test(int k, int[] arrival, int[] load, Integer[] expected) {
        List<Integer> expectedList = Arrays.asList(expected);
        assertEquals(expectedList, busiestServers(k, arrival, load));
        assertEquals(expectedList, busiestServers2(k, arrival, load));
    }

    @Test public void test1() {
        test(3, new int[] {1, 2, 3, 4, 5}, new int[] {5, 2, 3, 3, 3}, new Integer[] {1});
        test(3, new int[] {1, 2, 3, 4}, new int[] {1, 2, 1, 2}, new Integer[] {0});
        test(3, new int[] {1, 2, 3}, new int[] {10, 12, 11}, new Integer[] {0, 1, 2});
        test(3, new int[] {1, 2, 3, 4, 8, 9, 10}, new int[] {5, 2, 10, 3, 1, 2, 2},
             new Integer[] {1});
        test(1, new int[] {1}, new int[] {1}, new Integer[] {0});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
