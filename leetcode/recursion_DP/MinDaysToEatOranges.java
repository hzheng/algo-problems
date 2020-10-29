import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1553: https://leetcode.com/problems/minimum-number-of-days-to-eat-n-oranges/
//
// There are n oranges and you decided to eat some of these oranges every day as follows:
// Eat one orange.
// If the number of remaining oranges (n) is divisible by 2 then you can eat  n/2 oranges.
// If the number of remaining oranges (n) is divisible by 3 then you can eat  2*(n/3) oranges.
// You can only choose one of the actions per day.
// Return the minimum number of days to eat n oranges.
/// Constraints:
// 1 <= n <= 2*10^9
public class MinDaysToEatOranges {
    // Dynamic Programming
    // time complexity: O(log(N)), space complexity: O(log(N))
    // 2 ms(99.10%), 37.9 MB(6.00%) for 176 tests
    public int minDays(int n) {
        return minDays(n, new HashMap<>());
    }

    private int minDays(int n, Map<Integer, Integer> dp) {
        if (n <= 2) { return n; }

        int res = dp.getOrDefault(n, 0);
        if (res == 0) {
            res = 1 + Math.min(n % 2 + minDays(n / 2, dp), n % 3 + minDays(n / 3, dp));
            dp.put(n, res);
        }
        return res;
    }

    // BFS + Queue
    // time complexity: O(log(N)), space complexity: O(log(N))
    // 22 ms(27.44%), 38.4 MB(6.00%) for 176 tests
    public int minDays2(int n) {
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> q = new LinkedList<>();
        q.offer(n);
        for (int res = 0; ; res++) {
            for (int i = q.size(); i > 0; i--) {
                int cur = q.poll();
                if (cur == 0) { return res; }
                if (!visited.add(cur)) { continue; }

                if (cur % 3 == 0) {
                    q.offer(cur / 3);
                }
                if (cur % 2 == 0) {
                    q.offer(cur / 2);
                }
                q.offer(cur - 1);
            }
        }
    }

    private void test(int n, int expected) {
        assertEquals(expected, minDays(n));
        assertEquals(expected, minDays2(n));
    }

    @Test public void test() {
        test(10, 4);
        test(6, 3);
        test(1, 1);
        test(56, 6);
        test(820592, 22);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
