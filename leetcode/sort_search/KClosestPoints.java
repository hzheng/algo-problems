import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC973: https://leetcode.com/problems/k-closest-points-to-origin/
//
// We have a list of points on the plane. Find the K closest points to the origin (0, 0).
// (Here, the distance between two points on a plane is the Euclidean distance.)
// You may return the answer in any order. The answer is guaranteed to be unique.
//
// Note:
// 1 <= K <= points.length <= 10000
// -10000 < points[i][0] < 10000
// -10000 < points[i][1] < 10000
public class KClosestPoints {
    // Heap
    // time complexity: O(N*log(N)), space complexity: O(K)
    // 18 ms(71.85%), 47.7 MB(42.61%) for 83 tests
    public int[][] kClosest(int[][] points, int K) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(K + 1, (x, y) -> distance(y) - distance(x));
        for (int[] point : points) {
            pq.offer(point);
            if (pq.size() > K) {
                pq.poll();
            }
        }
        return pq.toArray(new int[K][]);
    }

    private int distance(int[] point) {
        return point[0] * point[0] + point[1] * point[1];
    }

    // Sort
    // time complexity: O(N*log(N)), space complexity: O(log(N) + K)
    // 24 ms(52.99%), 48.6 MB(10.17%) for 83 tests
    public int[][] kClosest2(int[][] points, int K) {
        Arrays.sort(points, Comparator.comparingInt(this::distance));
        return Arrays.copyOfRange(points, 0, K);
    }

    // Quick Select
    // time complexity: average: O(N) worst: O(N^2), space complexity: O(K)
    // 2 ms(100.00%), 47.5 MB(60.64%) for 83 tests
    public int[][] kClosest3(int[][] points, int K) {
        for (int len = points.length, l = 0, r = len - 1; l <= r; ) {
            int mid = select(points, l, r);
            if (mid == K) { break; }

            if (mid < K) {
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }
        return Arrays.copyOfRange(points, 0, K);
    }

    private int select(int[][] points, int l, int r) {
        int[] pivot = points[l];
        for (int pivotDist = distance(pivot); l < r; ) {
            for (; l < r && distance(points[r]) >= pivotDist; r--) {}
            points[l] = points[r];
            for (; l < r && distance(points[l]) <= pivotDist; l++) {}
            points[r] = points[l];
        }
        points[l] = pivot;
        return l;
    }

    private void test(int[][] points, int K, int[][] expected) {
        KClosestPoints k = new KClosestPoints();
        test(k::kClosest, points, K, expected);
        test(k::kClosest2, points, K, expected);
        test(k::kClosest3, points, K, expected);
    }

    @FunctionalInterface interface Function<A, B, C> {
        C apply(A a, B b);
    }

    private void test(Function<int[][], Integer, int[][]> f, int[][] points, int K,
                      int[][] expected) {
        Arrays.sort(expected, (x, y) -> (x[0] == y[0]) ? x[1] - y[1] : x[0] - y[0]);
        int[][] result = f.apply(points, K);
        Arrays.sort(result, (x, y) -> (x[0] == y[0]) ? x[1] - y[1] : x[0] - y[0]);
        assertArrayEquals(expected, result);
    }

    @Test public void test() {
        test(new int[][] {{1, 3}, {-2, 2}}, 1, new int[][] {{-2, 2}});
        test(new int[][] {{3, 3}, {5, -1}, {-2, 4}}, 2, new int[][] {{3, 3}, {-2, 4}});
        test(new int[][] {{3, 4}, {5, -1}, {-2, 4}, {-3, 2}, {1, 4}, {4, 1}, {-2, 5}, {9, 10}}, 4,
             new int[][] {{-3, 2}, {1, 4}, {4, 1}, {-2, 4}});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
