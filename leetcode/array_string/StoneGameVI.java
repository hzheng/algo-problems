import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1686: https://leetcode.com/problems/stone-game-vi/
//
// Alice and Bob take turns playing a game, with Alice starting first.
// There are n stones in a pile. On each player's turn, they can remove a stone from the pile and
// receive points based on the stone's value. Alice and Bob may value the stones differently.
// You are given two integer arrays of length n, aliceValues and bobValues. Each aliceValues[i] and
// bobValues[i] represents how Alice and Bob, respectively, value the ith stone.
// The winner is the person with the most points after all the stones are chosen. If both players
// have the same amount of points, the game results in a draw. Both players will play optimally.
// Both players know the other's values.
// Determine the result of the game, and:
// If Alice wins, return 1.
// If Bob wins, return -1.
// If the game results in a draw, return 0.
//
// Constraints:
// n == aliceValues.length == bobValues.length
// 1 <= n <= 10^5
// 1 <= aliceValues[i], bobValues[i] <= 100
public class StoneGameVI {
    // Greedy + Heap
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 123 ms(33.51%), 53.1 MB(51.35%) for 95 tests
    public int stoneGameVI(int[] aliceValues, int[] bobValues) {
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(b[0], a[0]));
        for (int i = 0; i < aliceValues.length; i++) {
            pq.offer(new int[] {aliceValues[i] + bobValues[i], i});
        }
        int[] score = new int[2];
        int[][] v = new int[][] {aliceValues, bobValues};
        for (int i = 0; !pq.isEmpty(); i ^= 1) {
            score[i] += v[i][pq.poll()[1]];
        }
        return Integer.compare(score[0], score[1]);
    }

    private void test(int[] aliceValues, int[] bobValues, int expected) {
        assertEquals(expected, stoneGameVI(aliceValues, bobValues));
    }

    @Test public void test() {
        test(new int[] {1, 3}, new int[] {2, 1}, 1);
        test(new int[] {1, 2}, new int[] {3, 1}, 0);
        test(new int[] {2, 4, 3}, new int[] {1, 6, 7}, -1);
        test(new int[] {2, 4, 3, 8, 9, 3}, new int[] {1, 6, 7, 2, 8, 4}, 1);
        test(new int[] {12, 2, 4, 3, 8, 9, 3, 2}, new int[] {3, 14, 6, 7, 2, 8, 4, 19}, -1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
