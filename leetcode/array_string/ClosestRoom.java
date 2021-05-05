import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1847: https://leetcode.com/problems/closest-room/
//
// There is a hotel with n rooms. The rooms are represented by a 2D integer array rooms where
// rooms[i] = [roomIdi, sizei] denotes that there is a room with room number roomIdi and size equal
// to sizei. Each roomIdi is guaranteed to be unique.
// You are also given k queries in a 2D array queries where queries[j] = [preferredj, minSizej]. The
// answer to the jth query is the room number id of a room such that:
// The room has a size of at least minSizej, and
// abs(id - preferredj) is minimized, where abs(x) is the absolute value of x.
// If there is a tie in the absolute difference, then use the room with the smallest such id. If
// there is no such room, the answer is -1.
// Return an array answer of length k where answer[j] contains the answer to the jth query.
//
// Constraints:
// n == rooms.length
// 1 <= n <= 10^5
// k == queries.length
// 1 <= k <= 10^4
// 1 <= roomIdi, preferredj <= 10^7
// 1 <= sizei, minSizej <= 10^7
public class ClosestRoom {
    // SortedMap + SortedSet
    // time complexity: O((N+K)*log(N)+K*log(K)), space complexity: O(N+K)
    // 141 ms(49.57%), 96.7 MB(74.44%) for 42 tests
    public int[] closestRoom(int[][] rooms, int[][] queries) {
        int k = queries.length;
        TreeMap<Integer, TreeSet<Integer>> map = new TreeMap<>();
        for (int[] r : rooms) {
            int size = r[1];
            int id = r[0];
            map.computeIfAbsent(size, x -> new TreeSet<>()).add(id);
        }
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> -a[1]));
        for (int i = 0; i < k; i++) {
            int[] q = queries[i];
            pq.offer(new int[] {q[0], q[1], i});
        }
        int[] res = new int[k];
        TreeSet<Integer> cand = new TreeSet<>();
        for (int i = 0; i < k; i++) {
            int[] q = pq.poll();
            for (int min = q[1]; !map.isEmpty() && map.lastKey() >= min; ) {
                var entry = map.pollLastEntry();
                cand.addAll(entry.getValue());
            }
            int prefer = q[0];
            int index = q[2];
            Integer low = cand.floor(prefer);
            Integer high = cand.ceiling(prefer);
            if (low == null && high == null) {
                res[index] = -1;
            } else if (low == null) {
                res[index] = high;
            } else if (high == null) {
                res[index] = low;
            } else {
                res[index] = (Math.abs(prefer - low) <= Math.abs(prefer - high)) ? low : high;
            }
        }
        return res;
    }

    // Sort + SortedSet
    // time complexity: O((N+K)*log(N)+K*log(K)), space complexity: O(N+K)
    // 73 ms(90.43%), 100.5 MB(63.65%) for 42 tests
    public int[] closestRoom2(int[][] rooms, int[][] queries) {
        int n = rooms.length;
        int k = queries.length;
        Integer[] indices = new Integer[k];
        for (int i = 0; i < k; i++) {
            indices[i] = i;
        }
        Arrays.sort(rooms, (a, b) -> Integer.compare(b[1], a[1]));
        Arrays.sort(indices, (a, b) -> Integer.compare(queries[b][1], queries[a][1]));
        TreeSet<Integer> cand = new TreeSet<>();
        int[] res = new int[k];
        int i = 0;
        for (int index : indices) {
            for (; i < n && rooms[i][1] >= queries[index][1]; i++) {
                cand.add(rooms[i][0]);
            }
            res[index] = getRoom(cand, queries[index][0]);
        }
        return res;
    }

    private int getRoom(TreeSet<Integer> rooms, int preferred) {
        int diff = Integer.MAX_VALUE;
        int res = -1;
        Integer low = rooms.floor(preferred);
        if (low != null) {
            res = low;
            diff = Math.abs(preferred - low);
        }
        Integer high = rooms.ceiling(preferred);
        if (high != null && diff > Math.abs(preferred - high)) {
            res = high;
        }
        return res;
    }

    private void test(int[][] rooms, int[][] queries, int[] expected) {
        assertArrayEquals(expected, closestRoom(rooms, queries));
        assertArrayEquals(expected, closestRoom2(rooms, queries));
    }

    @Test public void test1() {
        test(new int[][] {{2, 2}, {1, 2}, {3, 2}}, new int[][] {{3, 1}, {3, 3}, {5, 2}},
             new int[] {3, -1, 3});
        test(new int[][] {{1, 4}, {2, 3}, {3, 5}, {4, 1}, {5, 2}},
             new int[][] {{2, 3}, {2, 4}, {2, 5}}, new int[] {2, 1, 3});
        test(new int[][] {{23, 22}, {6, 20}, {15, 6}, {22, 19}, {2, 10}, {21, 4}, {10, 18}, {16, 1},
                          {12, 7}, {5, 22}},
             new int[][] {{12, 5}, {15, 15}, {21, 6}, {15, 1}, {23, 4}, {15, 11}, {1, 24}, {3, 19},
                          {25, 8}, {18, 6}}, new int[] {12, 10, 22, 15, 23, 10, -1, 5, 23, 15});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
