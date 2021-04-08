import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1620: https://leetcode.com/problems/coordinate-with-maximum-network-quality/
//
// You are given an array of network towers towers and an integer radius, where towers[i] =
// [xi, yi, qi] denotes the ith network tower with location (xi, yi) and quality factor qi. All the
// coordinates are integral coordinates on the X-Y plane, and the distance between two coordinates
// is the Euclidean distance.
// The integer radius denotes the maximum distance in which the tower is reachable. The tower is
// reachable if the distance is less than or equal to radius. Outside that distance, the signal
// becomes garbled, and the tower is not reachable.
// The signal quality of the ith tower at a coordinate (x, y) is calculated with the formula
// ⌊qi / (1 + d)⌋, where d is the distance between the tower and the coordinate. The network quality
// at a coordinate is the sum of the signal qualities from all the reachable towers.
// Return the integral coordinate where the network quality is maximum. If there are multiple
// coordinates with the same network quality, return the lexicographically minimum coordinate.
// Note:
// A coordinate (x1, y1) is lexicographically smaller than (x2, y2) if either x1 < x2 or x1 == x2
// and y1 < y2.
// ⌊val⌋ is the greatest integer less than or equal to val (the floor function).
//
// Constraints:
// 1 <= towers.length <= 50
// towers[i].length == 3
// 0 <= xi, yi, qi <= 50
// 1 <= radius <= 50
public class BestCoordinate {
    // time complexity: O(MAX_X*MAX_Y*N), space complexity: O(1)
    // 18 ms(95.52%), 39.4 MB(32.84%) for 99 tests
    public int[] bestCoordinate(int[][] towers, int radius) {
        int maxX = 0;
        int maxY = 0;
        for (int[] tower : towers) {
            maxX = Math.max(maxX, tower[0]);
            maxY = Math.max(maxY, tower[1]);
        }
        int[] res = new int[2];
        int max = 0;
        for (int x = 0; x <= maxX; x++) {
            for (int y = 0; y <= maxY; y++) {
                int[] point = new int[] {x, y};
                int quality = 0;
                for (int[] tower : towers) {
                    int d = distSquare(tower, point);
                    if (d <= radius * radius) {
                        quality += tower[2] / (1.0 + Math.sqrt(d));
                    }
                }
                if (quality > max) {
                    max = quality;
                    res = point;
                }
            }
        }
        return res;
    }

    // Sort + Deque
    // time complexity: O(MAX_X*MAX_Y*N), space complexity: O(log(N))
    // 38 ms(41.79%), 39.5 MB(32.84%) for 99 tests
    public int[] bestCoordinate2(int[][] towers, int radius) {
        Arrays.sort(towers, (a, b) -> (a[0] == b[0]) ? a[1] - b[1] : a[0] - b[0]);
        int n = towers.length;
        int maxX = towers[n - 1][0];
        int maxY = 0;
        for (int[] tower : towers) {
            maxY = Math.max(maxY, tower[1]);
        }
        Deque<Integer> reachedX = new LinkedList<>();
        int max = 0;
        int[] res = new int[2];
        for (int i = 0, x = towers[0][0]; x <= maxX; x++) {
            for (; i < n && x + radius >= towers[i][0]; i++) {
                reachedX.offerLast(i);
            }
            while (!reachedX.isEmpty() && towers[reachedX.peekFirst()][0] < x - radius) {
                reachedX.pollFirst();
            }
            for (int y = 0; y <= maxY; y++) {
                int[] point = new int[] {x, y};
                int quality = 0;
                for (int j : reachedX) {
                    if (towers[j][1] < y - radius || towers[j][1] > y + radius) { continue; }

                    int d = distSquare(towers[j], point);
                    if (d <= radius * radius) {
                        quality += towers[j][2] / (1 + Math.sqrt(d));
                    }
                }
                if (max < quality) {
                    max = quality;
                    res[0] = x;
                    res[1] = y;
                }
            }
        }
        return res;
    }

    private int distSquare(int[] a, int[] b) {
        return (a[0] - b[0]) * (a[0] - b[0]) + (a[1] - b[1]) * (a[1] - b[1]);
    }

    private void test(int[][] towers, int radius, int[] expected) {
        assertArrayEquals(expected, bestCoordinate(towers, radius));
        assertArrayEquals(expected, bestCoordinate2(towers, radius));
    }

    @Test public void test() {
        test(new int[][] {{1, 2, 5}, {2, 1, 7}, {3, 1, 9}}, 2, new int[] {2, 1});
        test(new int[][] {{23, 11, 21}}, 9, new int[] {23, 11});
        test(new int[][] {{1, 2, 13}, {2, 1, 7}, {0, 1, 9}}, 2, new int[] {1, 2});
        test(new int[][] {{2, 1, 9}, {0, 1, 9}}, 2, new int[] {0, 1});
        test(new int[][] {{0, 1, 2}, {2, 1, 2}, {1, 0, 2}, {1, 2, 2}}, 1, new int[] {1, 1});
        test(new int[][] {{42, 0, 0}}, 7, new int[] {0, 0});
        test(new int[][] {{50, 20, 31}, {43, 36, 29}}, 38, new int[] {50, 20});
        test(new int[][] {{30, 34, 31}, {10, 44, 24}, {14, 28, 23}, {50, 38, 1}, {40, 13, 6},
                          {16, 27, 9}, {2, 22, 23}, {1, 6, 41}, {34, 22, 40}, {40, 10, 11}}, 20,
             new int[] {1, 6});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
