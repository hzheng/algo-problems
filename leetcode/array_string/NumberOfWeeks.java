import java.util.stream.IntStream;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1953: https://leetcode.com/problems/maximum-number-of-weeks-for-which-you-can-work/
//
// There are n projects numbered from 0 to n - 1. You are given an integer array milestones where
// each milestones[i] denotes the number of milestones the ith project has.
// You can work on the projects following these two rules:
// Every week, you will finish exactly one milestone of one project. You must work every week.
// You cannot work on two milestones from the same project for two consecutive weeks.
// Once all the milestones of all the projects are finished, or if the only milestones that you can
// work on will cause you to violate the above rules, you will stop working. Note that you may not
// be able to finish every project's milestones due to these constraints.
// Return the maximum number of weeks you would be able to work on the projects without violating
// the rules mentioned above.
//
// Constraints:
// n == milestones.length
// 1 <= n <= 10^5
// 1 <= milestones[i] <= 10^9
public class NumberOfWeeks {
    // Greedy
    // time complexity: O(N), space complexity: O(1)
    // 2 ms(97.68%), 48.8 MB(40.93%) for 74 tests
    public long numberOfWeeks(int[] m) {
        long sum = 0;
        long max = 0;
        for (int a : m) {
            sum += a;
            max = Math.max(max, a);
        }
        return (sum + 1 < max * 2) ? (sum - max) * 2 + 1 : sum;
    }

    // Greedy
    // time complexity: O(N), space complexity: O(1)
    // 12 ms(26.20%), 48.8 MB(40.93%) for 74 tests
    public long numberOfWeeks2(int[] m) {
        long sum = IntStream.of(m).asLongStream().sum();
        long max = IntStream.of(m).max().getAsInt();
        return Math.min(sum, (sum - max) * 2 + 1);
    }

    private void test(int[] m, long expected) {
        assertEquals(expected, numberOfWeeks(m));
        assertEquals(expected, numberOfWeeks2(m));
    }

    @Test public void test() {
        test(new int[] {1}, 1);
        test(new int[] {3}, 1);
        test(new int[] {1, 2, 3}, 6);
        test(new int[] {5, 2, 1}, 7);
        test(new int[] {15, 8, 5, 2, 1, 4, 3, 19, 9, 11}, 77);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
