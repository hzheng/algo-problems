import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1626: https://leetcode.com/problems/best-team-with-no-conflicts/
//
// You are the manager of a basketball team. For the upcoming tournament, you want to choose the
// team with the highest overall score. The score of the team is the sum of scores of all the
// players in the team. However, the basketball team is not allowed to have conflicts. A conflict
// exists if a younger player has a strictly higher score than an older player. A conflict does not
// occur between players of the same age. Given two lists, scores and ages, where each scores[i] and
// ages[i] represents the score and age of the ith player, respectively, return the highest overall
// score of all possible basketball teams.
// Constraints:
// 1 <= scores.length, ages.length <= 1000
// scores.length == ages.length
// 1 <= scores[i] <= 10^6
// 1 <= ages[i] <= 1000
public class BestTeamScore {
    // Dynamic Programming
    // time complexity: O(N^2), space complexity: O(N)
    // 35 ms(98.64%), 39.2 MB(5.59%) for 147 tests
    public int bestTeamScore(int[] scores, int[] ages) {
        int n = scores.length;
        int[][] players = new int[n][];
        for (int i = 0; i < n; i++) {
            players[i] = new int[] {scores[i], ages[i]};
        }
        Arrays.sort(players, (a, b) -> (a[1] == b[1]) ? (a[0] - b[0]) : (a[1] - b[1]));

        int res = 0;
        int[] dp = new int[n];
        for (int i = 0; i < n; i++) {
            int cur = players[i][0];
            for (int j = 0; j < i; j++) {
                if (players[j][0] <= cur) {
                    dp[i] = Math.max(dp[i], dp[j]);
                }
            }
            res = Math.max(res, dp[i] += cur);
        }
        return res;
    }

    private void test(int[] scores, int[] ages, int expected) {
        assertEquals(expected, bestTeamScore(scores, ages));
    }

    @Test public void test() {
        test(new int[] {1, 3, 5, 10, 15}, new int[] {1, 2, 3, 4, 5}, 34);
        test(new int[] {4, 5, 6, 5}, new int[] {2, 1, 2, 1}, 16);
        test(new int[] {1, 2, 3, 5}, new int[] {8, 9, 10, 1}, 6);
        test(new int[] {596, 277, 897, 622, 500, 299, 34, 536, 797, 32, 264, 948, 645, 537, 83, 589,
                        770},
             new int[] {18, 52, 60, 79, 72, 28, 81, 33, 96, 15, 18, 5, 17, 96, 57, 72, 72}, 3287);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
