import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC997: https://leetcode.com/problems/find-the-town-judge/
//
// In a town, there are N people labelled from 1 to N.  There is a rumor that one of these people is
// secretly the town judge. If the town judge exists, then:
// The town judge trusts nobody.
// Everybody (except for the town judge) trusts the town judge.
// There is exactly one person that satisfies properties 1 and 2.
// You are given trust, an array of pairs trust[i] = [a, b] representing that the person labelled a
// trusts the person labelled b. If the town judge exists and can be identified, return the label of
// the town judge. Otherwise, return -1.
//
// Constraints:
// 1 <= N <= 1000
// 0 <= trust.length <= 10^4
// trust[i].length == 2
// trust[i] are all different
// trust[i][0] != trust[i][1]
// 1 <= trust[i][0], trust[i][1] <= N
public class FindJudge {
    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 3 ms(78.26%), 47 MB(28.60%) for 92 tests
    public int findJudge(int N, int[][] trust) {
        int[] cand = new int[N + 1];
        for (int[] t : trust) {
            cand[t[0]] = -1;
            if (++cand[t[1]] == 0) {
                cand[t[1]] = -1;
            }
        }
        for (int i = 1; i <= N; i++) {
            if (cand[i] == N - 1) { return i; }
        }
        return (N == 1) ? 1 : -1;
    }

    // Graph
    // time complexity: O(N), space complexity: O(N)
    // 3 ms(78.26%), 47 MB(28.60%) for 92 tests
    public int findJudge2(int N, int[][] trust) {
        int[] cand = new int[N + 1];
        for (int[] t : trust) {
            cand[t[0]] = -1; // or cand[t[0]]--
            cand[t[1]]++;
        }
        for (int i = 1; i <= N; i++) {
            if (cand[i] == N - 1) { return i; }
        }
        return -1;
    }

    // Graph
    // time complexity: O(N), space complexity: O(N^2)
    // 4 ms(33.38%), 49.1 MB(7.18%) for 92 tests
    public int findJudge3(int N, int[][] trust) {
        int[][] graph = new int[N + 1][N + 1];
        for (int[] t: trust) {
            graph[t[0]][t[1]] = 1;
        }
        return findCelebrity(N, graph);
    }

    private int findCelebrity(int n, int[][] graph) {
        int i = 1;
        for (int j = i + 1; j <= n; j++) {
            if (graph[i][j] != 0) {
                i = j;
            }
        }
        for (int j = 1; j < i; j++) {
            if (graph[i][j] != 0) { return -1; }
        }
        for (int j = 1; j <= n; j++) {
            if (i != j && graph[j][i] == 0) { return -1; }
        }
        return i;
    }

    private void test(int N, int[][] trust, int expected) {
        assertEquals(expected, findJudge(N, trust));
        assertEquals(expected, findJudge2(N, trust));
        assertEquals(expected, findJudge3(N, trust));
    }

    @Test public void test() {
        test(2, new int[][] {{1, 2}}, 2);
        test(3, new int[][] {{1, 3}, {2, 3}}, 3);
        test(3, new int[][] {{1, 3}, {2, 3}, {3, 1}}, -1);
        test(3, new int[][] {{1, 2}, {2, 3}}, -1);
        test(4, new int[][] {{1, 3}, {1, 4}, {2, 3}, {2, 4}, {4, 3}}, 3);
        test(1, new int[][] {}, 1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
