import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1851: https://leetcode.com/problems/minimum-interval-to-include-each-query/
//
// You are given a 2D integer array intervals, where intervals[i] = [lefti, righti] describes the
// ith interval starting at lefti and ending at righti (inclusive). The size of an interval is
// defined as the number of integers it contains, or more formally righti - lefti + 1.
// You are also given an integer array queries. The answer to the jth query is the size of the
// smallest interval i such that lefti <= queries[j] <= righti. If no such interval exists, the
// answer is -1.
// Return an array containing the answers to the queries.
//
// Constraints:
// 1 <= intervals.length <= 10^5
// 1 <= queries.length <= 10^5
// intervals[i].length == 2
// 1 <= lefti <= righti <= 10^7
// 1 <= queries[j] <= 10^7
public class MinInterval {
    // SortedMap + Heap
    // time complexity: O(N*log(N)+Q*log(Q)), space complexity: O(N+Q)
    // 765 ms(5.01%), 91 MB(31.27%) for 108 tests
    public int[] minInterval(int[][] intervals, int[] queries) {
        int k = queries.length;
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        for (int i = 0; i < k; i++) {
            pq.offer(new int[] {queries[i], i});
        }
        PriorityQueue<int[]> events = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        for (int i = 0, n = intervals.length; i < n; i++) {
            int[] interval = intervals[i];
            events.offer(new int[] {interval[0], i + 1});
            events.offer(new int[] {interval[1] + 1, -i - 1});
        }
        TreeMap<Integer, Integer> available =
                new TreeMap<>(Comparator.comparingInt(a -> intervals[a][1] - intervals[a][0]));
        int[] res = new int[k];
        for (int i = 0; i < k; i++) {
            int[] cur = pq.poll();
            for (int q = cur[0]; !events.isEmpty(); ) {
                int[] event = events.peek();
                if (event[0] > q) { break; }

                events.poll();
                if (event[1] > 0) {
                    change(available, event[1] - 1, 1);
                } else {
                    change(available, -event[1] - 1, -1);
                }
            }
            res[cur[1]] = -1;
            int index = available.isEmpty() ? -1 : available.firstKey();
            if (index >= 0) {
                res[cur[1]] = intervals[index][1] - intervals[index][0] + 1;
            }
        }
        return res;
    }

    private void change(Map<Integer, Integer> map, int key, int diff) {
        int prev = map.getOrDefault(key, 0);
        map.put(key, prev + diff);
        if (prev + diff == 0) {
            map.remove(key);
        }
    }

    // SortedMap + Sort + Hash Table
    // time complexity: O(N*log(N)+Q*log(Q)), space complexity: O(N+Q)
    // 128 ms(52.28%), 85.7 MB(56.13%) for 108 tests
    public int[] minInterval2(int[][] intervals, int[] queries) {
        Map<Integer, Integer> resMap = new HashMap<>();
        int n = intervals.length;
        int k = queries.length;
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
        int[] sortedQueries = queries.clone();
        Arrays.sort(sortedQueries);
        TreeMap<Integer, Integer> intervalMap = new TreeMap<>();
        int i = 0;
        for (int q : sortedQueries) {
            for (; i < n && intervals[i][0] <= q; i++) {
                int l = intervals[i][0];
                int r = intervals[i][1];
                intervalMap.put(r - l + 1, r);
            }
            while (!intervalMap.isEmpty() && intervalMap.firstEntry().getValue() < q) {
                intervalMap.pollFirstEntry();
            }
            resMap.put(q, intervalMap.isEmpty() ? -1 : intervalMap.firstKey());
        }
        i = 0;
        int[] res = new int[k];
        for (int q : queries) {
            res[i++] = resMap.get(q);
        }
        return res;
    }

    private void test(int[][] intervals, int[] queries, int[] expected) {
        assertArrayEquals(expected, minInterval(intervals, queries));
        assertArrayEquals(expected, minInterval2(intervals, queries));
    }

    @Test public void test1() {
        test(new int[][] {{1, 4}, {2, 4}, {3, 6}, {4, 4}}, new int[] {2, 3, 4, 5},
             new int[] {3, 3, 1, 4});
        test(new int[][] {{2, 3}, {2, 5}, {1, 8}, {20, 25}}, new int[] {2, 19, 5, 22},
             new int[] {2, -1, 4, 6});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
