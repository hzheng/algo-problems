import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1854: https://leetcode.com/problems/maximum-population-year/
//
// You are given a 2D integer array logs where each logs[i] = [birthi, deathi] indicates the birth
// and death years of the ith person. The population of some year x is the number of people alive
// during that year. The ith person is counted in year x's population if x is in the inclusive range
// [birthi, deathi - 1]. Note that the person is not counted in the year that they die.
// Return the earliest year with the maximum population.
//
// Constraints:
// 1 <= logs.length <= 100
// 1950 <= birthi < deathi <= 2050
public class MaximumPopulation {
    // Heap
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 2 ms(68.10%), 38.3 MB(78.05%) for 50 tests
    public int maximumPopulation(int[][] logs) {
        PriorityQueue<Integer> events = new PriorityQueue<>(
                (a, b) -> (Math.abs(a) != Math.abs(b)) ? Math.abs(a) - Math.abs(b) : a - b);
        for (int[] log : logs) {
            events.offer(log[0]);
            events.offer(-log[1]);
        }
        int res = 0;
        for (int count = 0, max = 0; !events.isEmpty(); ) {
            int cur = events.poll();
            if (cur > 0) {
                count++;
                if (count > max) {
                    max = count;
                    res = cur;
                }
            } else {
                count--;
            }
        }
        return res;
    }

    // time complexity: O(N*Y), space complexity: O(Y)
    // 1 ms(88.92%), 38.2 MB(78.05%) for 50 tests
    public int maximumPopulation2(int[][] logs) {
        int[] count = new int[2051];
        for (int[] log : logs) {
            for (int y = log[0]; y < log[1]; y++) {
                count[y]++;
            }
        }
        int res = 0;
        int max = 0;
        for (int y = 0; y < count.length; y++) {
            if (count[y] > max) {
                max = count[y];
                res = y;
            }
        }
        return res;
    }

    // Solution of Choice
    // Line Sweep
    // time complexity: O(N+Y), space complexity: O(Y)
    // 1 ms(88.92%), 38.5 MB(72.90%) for 50 tests
    public int maximumPopulation3(int[][] logs) {
        int[] population = new int[2051];
        for (int[] log : logs) {
            population[log[0]]++;
            population[log[1]]--;
        }
        int res = 0;
        for (int i = 1; i < population.length; ++i) {
            population[i] += population[i - 1];
            res = (population[i] > population[res]) ? i : res;
        }
        return res;
    }

    private void test(int[][] logs, int expected) {
        assertEquals(expected, maximumPopulation(logs));
        assertEquals(expected, maximumPopulation2(logs));
        assertEquals(expected, maximumPopulation3(logs));
    }

    @Test public void test1() {
        test(new int[][] {{1993, 1999}, {2000, 2010}}, 1993);
        test(new int[][] {{1950, 1961}, {1960, 1971}, {1970, 1981}}, 1960);
        test(new int[][] {{2008, 2026}, {2004, 2008}, {2034, 2035}, {1999, 2050}, {2049, 2050},
                          {2011, 2035}, {1966, 2033}, {2044, 2049}}, 2011);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
