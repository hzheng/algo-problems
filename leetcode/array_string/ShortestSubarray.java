import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC862: https://leetcode.com/problems/shortest-subarray-with-sum-at-least-k/
//
// Return the length of the shortest non-empty contiguous subarray of A with sum
// at least K. If there is no non-empty subarray with sum at least K, return -1.
public class ShortestSubarray {
    // Solution of Choice
    // Deque (monoqueue)
    // time complexity: O(N), space complexity: O(N)
    // beats 43.42%(41 ms for 93 tests)
    public int shortestSubarray(int[] A, int K) {
        int n = A.length;
        long[] sum = new long[n + 1];
        for (int i = 0; i < n; i++) {
            sum[i + 1] = sum[i] + A[i];
        }
        int res = n + 1;
        Deque<Integer> queue = new LinkedList<>();
        for (int i = 0; i <= n; i++) {
            while (!queue.isEmpty() && sum[i] - sum[queue.peekFirst()] >= K) {
                res = Math.min(res, i - queue.removeFirst());
            }
            while (!queue.isEmpty() && sum[i] <= sum[queue.peekLast()]) {
                queue.pollLast(); // remove non-candidates
            }
            queue.offerLast(i);
        }
        return res <= n ? res : -1;
    }

    // SortedMap
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 7.33%(214 ms for 93 tests)
    public int shortestSubarray2(int[] A, int K) {
        NavigableMap<Long, Integer> sumMap = new TreeMap<>();
        int n = A.length;
        int res = n + 1;
        long total = 0;
        for (int i = 0; i < n; i++) {
            total += A[i];
            if (total >= K) {
                res = Math.min(res, i + 1);
            }
            for (Long sum = sumMap.floorKey(total - K); sum != null; ) {
                res = Math.min(res, i - sumMap.get(sum));
                sumMap.remove(sum);
                sum = sumMap.lowerKey(sum);
            }
            sumMap.put(total, i);
        }
        return res <= n ? res : -1;
    }

    // Heap
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 19.95%(68 ms for 93 tests)
    public int shortestSubarray3(int[] A, int K) {
        int n = A.length;
        int res = n + 1;
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return a[0] - b[0];
            }
        });
        for (int i = 0, sum = 0; i < n; i++) {
            sum += A[i];
            if (sum >= K) {
                res = Math.min(res, i + 1);
            }
            while (!pq.isEmpty() && sum - pq.peek()[0] >= K) {
                res = Math.min(res, i - pq.poll()[1]);
            }
            pq.offer(new int[] { sum, i });
        }
        return res <= n ? res : -1;
    }

    // Binary Search + Heap
    // time complexity: O(N * log(N) ^ 2), space complexity: O(N)
    // beats 1.76%(669 ms for 93 tests)
    public int shortestSubarray4(int[] A, int K) {
        int n = A.length;
        int low = 1;
        for (int high = n + 1; low < high; ) {
            int mid = (low + high) >>> 1;
            if (check(A, K, mid)) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low <= n ? low : -1;
    }
    
    private boolean check(int[] A, int K, int len) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return a[0] - b[0];
            }
        });
        pq.offer(new int[]{0, -1});
        for (int i = 0, sum = 0; i < A.length; i++) {
            sum += A[i];
            for (; !pq.isEmpty() && pq.peek()[1] < i - len; pq.poll()) {}
            int min = !pq.isEmpty() ? pq.peek()[0] : 0;
            if (sum - min >= K) return true;

            pq.offer(new int[]{sum, i});
        }
        return false;
    }
    
    void test(int[] A, int K, int expected) {
        assertEquals(expected, shortestSubarray(A, K));
        assertEquals(expected, shortestSubarray2(A, K));
        assertEquals(expected, shortestSubarray3(A, K));
        assertEquals(expected, shortestSubarray4(A, K));
    }

    @Test
    public void test() {
        test(new int[] { 1, 2, 3, 6 }, 8, 2);
        test(new int[] { 1 }, 1, 1);
        test(new int[] { 1, 2 }, 4, -1);
        test(new int[] { 2, -1, 2 }, 3, 3);
        test(new int[] { 84, -37, 32, 40, 95 }, 167, 3);
        test(new int[] { -28, 81, -20, 28, -29 }, 89, 3);
        test(new int[] { -22343, 3865, -4451, -17784, 95654, -1164, 29776,
                         -12588, 30046, 22886, 8422, 83190, -14593,
                         66016, -34773, 67332, -19168, 75430, 35851, 11120,
                         94256, 34272, 26780, 90964, -6217, 68039, 8190,
                         41766, 81353, -40248, 27889, 10791, 31911, 21446,
                         -31015, -3616, -46610, 28168, -4058, 1451, 50508,
                         53991, 74700, -27399, 65569, 19100, -5373, 58668,
                         -20874, 56073, -22249, -45465, 44241, 97366, 9148,
                         59977, 71072, 38495, 89385, -22326, 72499, -24082,
                         53284, -20846, 55098, -27814, -25847, 15808, 45811,
                         -13384, 73593, 78371, 27064, 95739, 13282, 1140, 6913,
                         3518, 38329, 59341, 88326, 34006, 73703, 79410,
                         80360, -207, 99614, 70274, 83930, -33809, -22158,
                         -10794, 80735, 57394, 42366, 18051, -22978, 54055,
                         28112, 59088 }, 1481582, 37);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
