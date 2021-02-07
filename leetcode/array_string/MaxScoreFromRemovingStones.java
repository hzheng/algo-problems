import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1753: https://leetcode.com/problems/maximum-score-from-removing-stones/
//
// You are playing a solitaire game with three piles of stones of sizes a, b, and c respectively.
// Each turn you choose two different non-empty piles, take one stone from each, and add 1 point to
// your score. The game stops when there are fewer than two non-empty piles (meaning there are no
// more available moves). Given three integers a, b and c, return the maximum score you can get.
//
// Constraints:
// 1 <= a, b, c <= 10^5
public class MaxScoreFromRemovingStones {
    // time complexity: O(1), space complexity: O(1)
    // 0 ms(100.00%), 35.8 MB(50.00%) for 96 tests
    public int maximumScore(int a, int b, int c) {
        int max = Math.max(Math.max(a, b), c);
        int sum = a + b + c;
        return Math.min(sum - max, sum / 2);
    }

    // Heap
    // time complexity: O(A+B+C), space complexity: O(1)
    // 75 ms(25.00%), 38.7 MB(50.00%) for 96 tests
    public int maximumScore2(int a, int b, int c) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        pq.offer(a);
        pq.offer(b);
        pq.offer(c);
        int score = 0;
        for (; pq.size() >= 2; score++) {
            int max = pq.poll();
            int mid = pq.poll();
            if (--max > 0) {
                pq.offer(max);
            }
            if (--mid > 0) {
                pq.offer(mid);
            }
        }
        return score;
    }

    // Recursion
    // time complexity: O(A+B+C), space complexity: O(1)
    // 31 ms(25.00%), 59 MB(50.00%) for 96 tests
    public int maximumScore3(int a, int b, int c) {
        if (a < b) { return maximumScore(b, a, c); }
        if (b < c) { return maximumScore(a, c, b); }
        return b == 0 ? 0 : 1 + maximumScore3(a - 1, b - 1, c);
    }

    private void test(int a, int b, int c, int expected) {
        assertEquals(expected, maximumScore(a, b, c));
        assertEquals(expected, maximumScore2(a, b, c));
        assertEquals(expected, maximumScore3(a, b, c));
    }

    @Test public void test() {
        test(2, 4, 6, 6);
        test(4, 4, 6, 7);
        test(1, 8, 8, 8);
        test(9, 80, 108, 89);
        test(900, 80, 108, 188);
        test(910, 1, 138, 139);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
