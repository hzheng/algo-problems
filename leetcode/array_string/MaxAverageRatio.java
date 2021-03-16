import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1792: https://leetcode.com/problems/maximum-average-pass-ratio/
//
// There is a school that has classes of students and each class will be having a final exam. You
// are given a 2D integer array classes, where classes[i] = [passi, totali]. You know beforehand
// that in the ith class, there are totali total students, but only passi number of students will
// pass the exam.
// You are also given an integer extraStudents. There are another extraStudents brilliant students
// that are guaranteed to pass the exam of any class they are assigned to. You want to assign each
// of the extraStudents students to a class in a way that maximizes the average pass ratio across
// all the classes.
// The pass ratio of a class is equal to the number of students of the class that will pass the exam
// divided by the total number of students of the class. The average pass ratio is the sum of pass
// ratios of all the classes divided by the number of the classes.
// Return the maximum possible average pass ratio after assigning the extraStudents students.
// Answers within 10^-5 of the actual answer will be accepted.
//
// Constraints:
// 1 <= classes.length <= 10^5
// classes[i].length == 2
// 1 <= passi <= totali <= 10^5
// 1 <= extraStudents <= 10^5
public class MaxAverageRatio {
    // Heap
    // time complexity: O((N+E)*log(N+E)), space complexity: O(N+E)
    // 559 ms(100.00%), 95.3 MB(100.00%) for 84 tests
    public double maxAverageRatio(int[][] classes, int extraStudents) {
        PriorityQueue<int[]> pq =
                new PriorityQueue<>((a, b) -> Double.compare(getProfit(b), getProfit(a)));
        for (int[] c : classes) {
            pq.offer(c);
        }
        for (int s = extraStudents; s > 0; s--) {
            int[] cur = pq.poll();
            pq.offer(new int[] {cur[0] + 1, cur[1] + 1});
        }
        double res = 0.0;
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            res += ((double)cur[0]) / cur[1];
        }
        return res / classes.length;
    }

    // Heap
    // time complexity: O((N+E)*log(N+E)), space complexity: O(N+E)
    // 556 ms(100.00%), 98.7 MB(100.00%) for 84 tests
    public double maxAverageRatio2(int[][] classes, int extraStudents) {
        PriorityQueue<int[]> pq =
                new PriorityQueue<>((a, b) -> Double.compare(getProfit(b), getProfit(a)));
        double sum = 0.0;
        pq.addAll(Arrays.asList(classes));
        for (int s = extraStudents; s > 0; s--) {
            int[] cur = pq.poll();
            cur[0]++;
            cur[1]++;
            pq.offer(cur);
        }
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            sum += (double)cur[0] / cur[1];
        }
        return sum / classes.length;
    }

    private double getProfit(int[] c) {
        double a = c[0];
        double b = c[1];
        return (a + 1) / (b + 1) - a / b;
    }

    private void test(int[][] classes, int extraStudents, double expected) {
        assertEquals(expected, maxAverageRatio(classes, extraStudents), 1E-5);
        assertEquals(expected, maxAverageRatio2(classes, extraStudents), 1E-5);
    }

    @Test public void test() {
        test(new int[][] {{1, 2}, {3, 5}, {2, 2}}, 2, 0.78333);
        test(new int[][] {{2, 4}, {3, 9}, {4, 5}, {2, 10}}, 4, 0.53485);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
