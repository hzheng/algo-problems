import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1560: https://leetcode.com/problems/most-visited-sector-in-a-circular-track/
//
// Given an integer n and an integer array rounds. We have a circular track which consists of n
// sectors labeled from 1 to n. A marathon will be held on this track, the marathon consists of m
// rounds. The ith round starts at sector rounds[i - 1] and ends at sector rounds[i]. For example,
// round 1 starts at sector rounds[0] and ends at sector rounds[1]
// Return an array of the most visited sectors sorted in ascending order.
// Notice that you circulate the track in ascending order of sector numbers in the counter-clockwise
// direction.
// Constraints:
// 2 <= n <= 100
// 1 <= m <= 100
// rounds.length == m + 1
// 1 <= rounds[i] <= n
// rounds[i] != rounds[i + 1] for 0 <= i < m
public class MostVisitedSector {
    // time complexity: O(M * N), space complexity: O(N)
    // 3 ms(68.44%), 39.5 MB(100%) for 204 tests
    public List<Integer> mostVisited(int n, int[] rounds) {
        int m = rounds.length;
        int[] count = new int[n + 1];
        int max = 0;
        for (int i = 1; i < m; i++) {
            int from = rounds[i - 1];
            int to = rounds[i];
            if (to < from) {
                to += n;
            }
            for (int j = (i == 1) ? from : from + 1; j <= to; j++) {
                int c = ++count[(j - 1) % n];
                if (c > max) {
                    max = c;
                }
            }
        }
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (count[i] == max) {
                res.add(i + 1);
            }
        }
        return res;
    }

    // time complexity: O(N * log(N)), space complexity: O(N)
    // 2 ms(87.76%), 39.5 MB(100%) for 204 tests
    public List<Integer> mostVisited2(int n, int[] rounds) {
        List<Integer> res = new ArrayList<>();
        for (int cur = rounds[0], end = rounds[rounds.length - 1]; ; cur++) {
            if (cur > n) {
                cur = 1;
            }
            res.add(cur);
            if (cur == end) {
                break;
            }
        }
        Collections.sort(res);
        return res;
    }

    // time complexity: O(N), space complexity: O(N)
    // 1 ms(97.02%), 39.8 MB(100%) for 204 tests
    public List<Integer> mostVisited3(int n, int[] rounds) {
        List<Integer> res = new ArrayList<>();
        for (int i = rounds[0]; i <= rounds[rounds.length - 1]; i++) {
            res.add(i);
        }
        if (!res.isEmpty()) { return res; }

        for (int i = 1; i <= rounds[rounds.length - 1]; i++) {
            res.add(i);
        }
        for (int i = rounds[0]; i <= n; i++) {
            res.add(i);
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(N)
    // 1 ms(97.02%), 39.8 MB(100%) for 204 tests
    public List<Integer> mostVisited4(int n, int[] rounds) {
        List<Integer> res = new ArrayList<>();
        int from = rounds[0];
        int to = rounds[rounds.length - 1];
        if (from <= to) {
            for (int i = from; i <= to; i++) {
                res.add(i);
            }
        } else {
            for (int i = 1; i <= n; i++) {
                if (i == to + 1) {
                    i = from;
                }
                res.add(i);
            }
        }
        return res;
    }

    void test(int n, int[] rounds, Integer[] expected) {
        List<Integer> expectedList = Arrays.asList(expected);
        assertEquals(expectedList, mostVisited(n, rounds));
        assertEquals(expectedList, mostVisited2(n, rounds));
        assertEquals(expectedList, mostVisited3(n, rounds));
        assertEquals(expectedList, mostVisited4(n, rounds));
    }

    @Test public void test() {
        test(4, new int[] {1, 3, 1, 2}, new Integer[] {1, 2});
        test(2, new int[] {2, 1, 2, 1, 2, 1, 2, 1, 2}, new Integer[] {2});
        test(7, new int[] {1, 3, 5, 7}, new Integer[] {1, 2, 3, 4, 5, 6, 7});
        test(4, new int[] {4, 1}, new Integer[] {1, 4});
        test(3, new int[] {3, 2, 1, 2, 1, 3, 2, 1, 2, 1, 3, 2, 3, 1}, new Integer[] {1, 3});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
