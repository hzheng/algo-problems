import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1654: https://leetcode.com/problems/minimum-jumps-to-reach-home/
//
// A certain bug's home is on the x-axis at position x. Help them get there from position 0.
// The bug jumps according to the following rules:
// It can jump exactly a positions forward (to the right).
// It can jump exactly b positions backward (to the left).
// It cannot jump backward twice in a row.
// It cannot jump to any forbidden positions.
// The bug may jump forward beyond its home, but it cannot jump to positions numbered with negative
// integers.
// Given an array of integers forbidden, where forbidden[i] means that the bug cannot jump to the
// position forbidden[i], and integers a, b, and x, return the minimum number of jumps needed for
// the bug to reach its home. If there is no possible sequence of jumps that lands the bug on
// position x, return -1.
//
// Constraints:
// 1 <= forbidden.length <= 1000
// 1 <= a, b, forbidden[i] <= 2000
// 0 <= x <= 2000
// All the elements in forbidden are distinct.
// Position x is not forbidden.
public class MinimumJumps {
    // BFS + Queue
    // 43 ms(30.82%), 39.7 MB(56.00%) for 157 tests
    public int minimumJumps(int[] forbidden, int a, int b, int x) {
        if (x % gcd(a, b) != 0) { return -1; } // optimize

        Set<Integer> visited = new HashSet<>();
        int bound = x + a + b;
        for (int pos : forbidden) {
            visited.add(pos);
            visited.add(-pos);
            bound = Math.max(bound, pos + a + b);
        }
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[2]);
        for (int step = 0; !queue.isEmpty(); step++) {
            for (int size = queue.size(); size > 0; size--) {
                int[] cur = queue.poll();
                int pos = cur[0];
                if (pos == x) { return step; }
                if (!visited.add(pos * cur[1])) { continue; }

                if (pos + a <= bound) {
                    queue.offer(new int[] {pos + a, 1});
                }
                if (pos >= b && cur[1] >= 0) {
                    queue.offer(new int[] {pos - b, -1});
                }
            }
        }
        return -1;
    }

    private int gcd(int a, int b) {
        return (a == 0) ? b : gcd(b % a, a);
    }

    // BFS + Queue + Dynamic Programming
    // 6 ms(96.93%), 39.1 MB(85.75%) for 157 tests
    public int minimumJumps2(int[] forbidden, int a, int b, int x) {
        int bound = x + a + b;
        for (int pos : forbidden) {
            bound = Math.max(bound, pos + a + b);
        }
        int[] dp = new int[bound + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        for (int pos : forbidden) {
            dp[pos] = -1;
        }
        Queue<Integer> queue = new LinkedList<>();
        for (queue.offer(0); !queue.isEmpty(); ) {
            int pos = queue.poll();
            if (pos + a <= bound && dp[pos + a] > dp[pos] + 1) {
                queue.offer(pos + a);
                dp[pos + a] = dp[pos] + 1;
            }
            if (pos > b && dp[pos - b] > dp[pos] + 1) {
                dp[pos - b] = dp[pos] + 1;
                if (pos - b + a <= bound && dp[pos - b + a] > dp[pos] + 2) {
                    queue.offer(pos - b + a);
                    dp[pos - b + a] = dp[pos] + 2;
                }
            }
        }
        return dp[x] == Integer.MAX_VALUE ? -1 : dp[x];
    }

    private void test(int[] forbidden, int a, int b, int x, int expected) {
        assertEquals(expected, minimumJumps(forbidden, a, b, x));
        assertEquals(expected, minimumJumps2(forbidden, a, b, x));
    }

    @Test public void test() {
        test(new int[] {14, 4, 18, 1, 15}, 3, 15, 9, 3);
        test(new int[] {8, 3, 16, 6, 12, 20}, 15, 13, 11, -1);
        test(new int[] {1, 6, 2, 14, 5, 17, 4}, 16, 9, 7, 2);
        test(new int[] {3}, 14, 5, 90, 20);
        test(new int[] {162, 118, 178, 152, 167, 100, 40, 74, 199, 186, 26, 73, 200, 127, 30, 124,
                        193, 84, 184, 36, 103, 149, 153, 9, 54, 154, 133, 95, 45, 198, 79, 157, 64,
                        122, 59, 71, 48, 177, 82, 35, 14, 176, 16, 108, 111, 6, 168, 31, 134, 164,
                        136, 72, 98}, 29, 98, 80, 121);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
