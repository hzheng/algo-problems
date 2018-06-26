import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC857: https://leetcode.com/problems/minimum-cost-to-hire-k-workers/
//
// There are N workers.  The i-th worker has a quality[i] and a minimum wage
// expectation wage[i]. Now we want to hire exactly K workers to form a paid
// group.  When hiring a group of K workers, we must pay them according to the
// following rules:
// Every worker in the paid group should be paid in the ratio of their quality
// compared to other workers in the paid group.
// Every worker must be paid at least their minimum wage expectation.
// Return the least amount of money needed to form a paid group.
public class MincostToHireWorkers {
    // Sort + Heap
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats %(51 ms for 46 tests)
    public double mincostToHireWorkers(int[] quality, int[] wage, int K) {
        int n = quality.length;
        int[][] workers = new int[n][];
        for (int i = 0; i < n; i++) {
            workers[i] = new int[] { quality[i], wage[i] };
        }
        Arrays.sort(workers, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return a[1] * b[0] - b[1] * a[0];
            }
        });
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        double sum = 0;
        double res = Double.MAX_VALUE;
        for (int[] w : workers) {
            pq.offer(-w[0]);
            sum += w[0];
            if (pq.size() > K) {
                sum += pq.poll();
            }
            if (pq.size() == K) {
                res = Math.min(res, (double) w[1] / w[0] * sum);
            }
        }
        return res;
    }

    // Greedy + Sort
    // time complexity: O(N ^ 2 * log(N)), space complexity: O(N)
    // Time Limit Exceeded
    public double mincostToHireWorkers2(int[] quality, int[] wage, int K) {
        int n = quality.length;
        double res = Double.MAX_VALUE;
        for (int captain = 0; captain < n; captain++) {
            double ratio = (double) wage[captain] / quality[captain];
            double prices[] = new double[n];
            int i = 0;
            for (int worker = 0; worker < n; worker++) {
                double price = ratio * quality[worker];
                if (price >= wage[worker]) {
                    prices[i++] = price;
                }
            }
            if (i < K) continue;

            Arrays.sort(prices, 0, i);
            double cand = 0;
            for (int j = 0; j < K; j++) {
                cand += prices[j];
            }
            res = Math.min(res, cand);
        }
        return res;
    }

    void test(int[] quality, int[] wage, int K, double expected) {
        assertEquals(expected, mincostToHireWorkers(quality, wage, K), 1E-5);
        assertEquals(expected, mincostToHireWorkers2(quality, wage, K), 1E-5);
    }

    @Test
    public void test() {
        test(new int[] { 10, 20, 5 }, new int[] { 70, 50, 30 }, 2, 105);
        test(new int[] { 3, 1, 10, 10, 1 }, new int[] { 4, 8, 2, 2, 7 }, 3,
             30.66667);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
