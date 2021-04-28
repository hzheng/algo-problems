import java.util.*;

import common.Utils;
import org.junit.Test;

import static org.junit.Assert.*;

// LC1840: https://leetcode.com/problems/maximum-building-height/
//
// You want to build n new buildings in a city. The new buildings will be built in a line and are
// labeled from 1 to n.
// However, there are city restrictions on the heights of the new buildings:
// The height of each building must be a non-negative integer.
// The height of the first building must be 0.
// The height difference between any two adjacent buildings cannot exceed 1.
// Additionally, there are city restrictions on the maximum height of specific buildings. These
// restrictions are given as a 2D integer array restrictions where
// restrictions[i] = [idi, maxHeighti] indicates that building idi must have a height
// less than or equal to maxHeighti.
// It is guaranteed that each building will appear at most once in restrictions, and building 1 will
// not be in restrictions.
// Return the maximum possible height of the tallest building.
//
// Constraints:
// 2 <= n <= 10^9
// 0 <= restrictions.length <= min(n - 1, 10^5)
// 2 <= idi <= n
// idi is unique.
// 0 <= maxHeighti <= 10^9
public class MaxBuilding {
    // Sort + Dynamic Programming
    // time complexity: O(M*log(M)), space complexity: O(M)
    // 50 ms(76.52%), 92.5 MB(80.71%) for 50 tests
    public int maxBuilding(int n, int[][] restrictions) {
        Arrays.sort(restrictions, Comparator.comparingInt(a -> a[0]));
        int m = restrictions.length;
        int[] maxHeights = new int[m + 1];
        for (int i = 0, prev = 0; i < m; i++) { // from left to right
            int[] r = restrictions[i];
            int cur = r[0] - 1;
            maxHeights[i + 1] = Math.min(r[1], maxHeights[i] + cur - prev);
            prev = cur;
        }
        for (int i = m - 1; i > 0; i--) { // from right to left
            int dist = restrictions[i][0] - restrictions[i - 1][0];
            maxHeights[i] = Math.min(maxHeights[i], maxHeights[i + 1] + dist);
        }
        int res = 0;
        int prev = 0;
        for (int i = 0, cur; i < m; i++, prev = cur) {
            cur = restrictions[i][0] - 1;
            res = Math.max(res, (maxHeights[i + 1] + maxHeights[i] + cur - prev) / 2);
        }
        return Math.max(res, maxHeights[m] + n - prev - 1);
    }

    // Sort
    // time complexity: O(M*log(M)), space complexity: O(M)
    // 61 ms(41.25%), 91.8 MB(97.24%) for 50 tests
    public int maxBuilding2(int n, int[][] restrictions) {
        List<int[]> restrictList = new ArrayList<>();
        restrictList.add(new int[]{1, 0});
        restrictList.add(new int[]{n, n - 1});
        restrictList.addAll(Arrays.asList(restrictions));
        restrictList.sort(Comparator.comparingInt(a -> a[0]));
        check(restrictList);
        Collections.reverse(restrictList);
        return check(restrictList);
    }

    private int check(List<int[]> restrictList) {
        int res = 0;
        for (int i = 0, n = restrictList.size(); i < n - 1; i++) {
            int[] r1 = restrictList.get(i);
            int[] r2 = restrictList.get(i + 1);
            int h1 = r1[1];
            int h2 = r2[1];
            int h = h1 + Math.abs(r2[0] - r1[0]);
            if (h > h2) {
                h = h2 + (h - h2) / 2;
            }
            res = Math.max(res, h);
            r2[1] = Math.min(h2, h);
        }
        return res;
    }

    @FunctionalInterface interface Function<A, B, C> {
        C apply(A a, B b);
    }

    private void test(Function<Integer, int[][], Integer> maxBuilding) {
        try {
            String clazz = new Object() {
            }.getClass().getEnclosingClass().getName();
            Scanner scanner = new Scanner(new java.io.File("data/" + clazz));
            while (scanner.hasNextLine()) {
                int n = Integer.parseInt(scanner.nextLine());
                int[][] restrictions = Utils.readInt2Array(scanner.nextLine());
                int res = maxBuilding.apply(n, restrictions);
                int expected = Integer.parseInt(scanner.nextLine());
                assertEquals(expected, res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test public void test2() {
        MaxBuilding m = new MaxBuilding();
        test(m::maxBuilding);
        test(m::maxBuilding2);
    }

    private void test(int n, int[][] restrictions, int expected) {
        assertEquals(expected, maxBuilding(n, restrictions));
        assertEquals(expected, maxBuilding2(n, restrictions));
    }

    @Test public void test1() {
        test(5, new int[][] {{2, 1}, {4, 1}}, 2);
        test(6, new int[][] {}, 5);
        test(10, new int[][] {{5, 3}, {2, 5}, {7, 4}, {10, 3}}, 5);
        test(10,
             new int[][] {{8, 5}, {9, 0}, {6, 2}, {4, 0}, {3, 2}, {10, 0}, {5, 3}, {7, 3}, {2, 4}},
             2);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
