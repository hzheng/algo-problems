import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1383: https://leetcode.com/problems/maximum-performance-of-a-team/
//
// There are n engineers numbered from 1 to n and two arrays: speed and efficiency, where speed[i]
// and efficiency[i] represent the speed and efficiency for the i-th engineer respectively. Return
// the maximum performance of a team composed of at most k engineers, since the answer can be a huge
// number, return this modulo 10^9 + 7. The performance of a team is the sum of their engineers'
// speeds multiplied by the minimum efficiency among their engineers.
// Constraints:
// 1 <= n <= 10^5
// speed.length == n
// efficiency.length == n
// 1 <= speed[i] <= 10^5
// 1 <= efficiency[i] <= 10^8
// 1 <= k <= n
public class MaxPerformance {
    private static final int MOD = 1000_000_000 + 7;

    // Sort + Heap
    // time complexity: O(N*log(N))), space complexity: O(N)
    // 44 ms(19.93%), 51.8 MB(100%) for 53 tests
    public int maxPerformance(int n, int[] speed, int[] efficiency, int k) {
        int[][] engineers = new int[n][2];
        for (int i = 0; i < n; i++) {
            engineers[i] = new int[] {speed[i], efficiency[i]};
        }
        Arrays.sort(engineers, (a, b) -> (b[1] - a[1]));

        PriorityQueue<Integer> pq = new PriorityQueue<>(k + 1);
        long res = 0;
        long sum = 0;
        for (int[] engineer : engineers) {
            sum += engineer[0];
            pq.offer(engineer[0]);
            if (pq.size() > k) {
                sum -= pq.poll();
            }
            res = Math.max(res, sum * engineer[1]);
        }
        return (int)(res % MOD);
    }

    // Sort + Heap
    // time complexity: O(N*log(N))), space complexity: O(N)
    // 41 ms(67.28%), 51.6 MB(100%) for 53 tests
    public int maxPerformance2(int n, int[] speed, int[] efficiency, int k) {
        int[][] engineers = new int[n][2];
        for (int i = 0; i < n; i++) {
            engineers[i] = new int[] {speed[i], efficiency[i]};
        }
        Arrays.sort(engineers, (a, b) -> (b[1] - a[1]));

        PriorityQueue<Integer> pq = new PriorityQueue<>(k + 1);
        long res = 0;
        long sum = 0;
        for (int i = 0; i < k; i++) {
            int[] engineer = engineers[i];
            sum += engineer[0];
            res = Math.max(res, sum * engineer[1]);
            pq.offer(engineer[0]);
        }
        for (int i = k; i < n; i++) {
            int[] engineer = engineers[i];
            if (engineer[0] <= pq.peek()) { continue; } // optimize

            sum += engineer[0] - pq.poll();
            res = Math.max(res, sum * engineer[1]);
            pq.offer(engineer[0]);
        }
        return (int)(res % MOD);
    }

    private void test(int n, int[] speed, int[] efficiency, int k, int expected) {
        assertEquals(expected, maxPerformance(n, speed, efficiency, k));
        assertEquals(expected, maxPerformance2(n, speed, efficiency, k));
    }

    @Test public void test() {
        test(6, new int[] {2, 10, 3, 1, 5, 8}, new int[] {5, 4, 3, 9, 7, 2}, 2, 60);
        test(6, new int[] {2, 10, 3, 1, 5, 8}, new int[] {5, 4, 3, 9, 7, 2}, 3, 68);
        test(3, new int[] {2, 8, 2}, new int[] {2, 7, 1}, 2, 56);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
