import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC774: https://leetcode.com/problems/minimize-max-distance-to-gas-station/
//
// On a horizontal number line, we have gas stations at positions stations[0],
// stations[1], ..., stations[N-1]. We add K more gas stations so that D, the
// maximum distance between adjacent gas stations, is minimized.
// Return the smallest possible value of D.
// Note:
// stations.length will be an integer in range [10, 2000].
// stations[i] will be an integer in range [0, 10^8].
// K will be an integer in range [1, 10^6].
// Answers within 10^-6 of the true value will be accepted as correct.
public class MinmaxGasDist {
    // Binary Search
    // time complexity: O(log(RANGE))
    // beats %(25 ms for 61 tests)
    public double minmaxGasDist(int[] stations, int K) {
        // Arrays.sort(stations); // if statitions is not ascending
        int n = stations.length;
        double low = 0;
        for (double high = stations[n - 1] - stations[0]; low + 1E-6 < high; ) {
            double mid = (high + low) / 2;
            int count = 0;
            for (int i = 0; i < n - 1; i++) {
                count += (int)((stations[i + 1] - stations[i]) / mid);
            }
            if (count > K) {
                low = mid;
            } else  {
                high = mid;
            }
        }
        return low;
    }

    // Priority Queue + Greedy(Proportional Assignment)
    // time complexity: O(N * log(N))
    // beats %(37 ms for 61 tests)
    public double minmaxGasDist2(int[] stations, int K) {
        // Arrays.sort(stations);
        PriorityQueue<Interval> pq =
            new PriorityQueue<>(new Comparator<Interval>() {
            public int compare(Interval a, Interval b) {
                double diff = a.distance() - b.distance();
                return (diff < 0) ? 1 : ((diff > 0) ? -1 : 0);
            }
        });
        int n = stations.length;
        double span = stations[n - 1] - stations[0];
        int remaining = K;
        for (int i = 0; i < n - 1; i++) {
            int insertions =
                (int)(K * (((double)(stations[i + 1] - stations[i])) / span));
            pq.add(new Interval(stations[i], stations[i + 1], insertions));
            remaining -= insertions;
        }
        for (; remaining > 0; remaining--) {
            Interval interval = pq.poll();
            interval.insertions++;
            pq.offer(interval);
        }
        return pq.peek().distance();
    }

    private static class Interval {
        int left;
        int right;
        int insertions;

        Interval(int a, int b, int insertions) {
            this.left = a;
            this.right = b;
            this.insertions = insertions;
        }

        public double distance() {
            return (right - left) / (insertions + 1d);
        }
    }

    void test(int[] stations, int K, double expected) {
        assertEquals(expected, minmaxGasDist(stations, K), 1E-5);
        assertEquals(expected, minmaxGasDist2(stations, K), 1E-5);
    }

    @Test
    public void test() {
        test(new int[] {10, 19, 25, 27, 56, 63, 70, 87, 96, 97}, 3, 9.66667);
        test(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 9, 0.5);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
