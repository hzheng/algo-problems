import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC818: https://leetcode.com/problems/race-car/
//
// A car starts at position 0 and speed +1 on an infinite number line. (It can
// go into negative positions.) It drives automatically according to a sequence
// of instructions A (accelerate) and R (reverse). When you get an "A", it does
// the following: position += speed, speed *= 2. When you get an "R", it does
// the following: if its speed is positive then speed = -1 , otherwise speed = 1.
// (Position stays the same.)
// Now for some target position, say the length of the shortest sequence of
// instructions to get there.
public class RaceCar {
    // Dynamic Programming(Bottom-Up)
    // time complexity: O(T * log(T)), space complexity: O(T)
    // beats %(15 ms for 53 tests)
    public int racecar(int target) {
        int[] dp = new int[target + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        for (int i = 1; i <= target; i++) {
            int k = 32 - Integer.numberOfLeadingZeros(i);
            if (i == (1 << k) - 1) {
                dp[i] = k;
                continue;
            }
            // A^k R
            dp[i] = Math.min(dp[i], dp[(1 << k) - 1 - i] + k + 1);
            for (int j = 0; j < k - 1; j++) {
                // A^(k−1) R A^j R
                dp[i] = Math.min(dp[i],
                                 dp[i - (1 << (k - 1)) + (1 << j)] + k + j + 1);
            }
        }
        return dp[target];
    }

    // Dynamic Programming + Recursion(Top-Down)
    // time complexity: O(T * log(T)), space complexity: O(T)
    // beats %(5 ms for 53 tests)
    public int racecar2(int target) {
        return race(target, new int[target + 1]);
    }

    private int race(int tgt, int[] dp) {
        if (dp[tgt] > 0) return dp[tgt];

        int k = (int)(Math.log(tgt) / Math.log(2)) + 1;
        if (tgt == (1 << k) - 1) return dp[tgt] = k;

        int res = race((1 << k) - 1 - tgt, dp) + k + 1;
        for (int i = 0, m = 1 << (k - 1); i < k - 1; i++) {
            res = Math.min(res, race(tgt - m + (1 << i), dp) + k + i + 1);
        }
        return dp[tgt] = res;
    }

    // Heap(Dijkstra's algorithm)
    // time complexity: O(T * log(T)), space complexity: O(T)
    // beats %(1279 ms for tests)
    public int racecar3(int target) {
        int maxSteps = 33 - Integer.numberOfLeadingZeros(target - 1);
        int radius = 1 << maxSteps;
        int range = 2 * radius + 1;
        int[] dist = new int[range];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[target] = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<int[]>
                                      ((a, b) -> a[0] - b[1]);
        pq.offer(new int[] {0, target});
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int steps = cur[0];
            int far = cur[1];
            for (int i = 0; i <= maxSteps; i++) {
                int far2 = (1 << i) - 1 - far;
                // all intermediate steps ends with 'R' except the final one
                int steps2 = steps + i + (far2 != 0 ? 1 : 0);
                if (Math.abs(far2) <= radius
                    && dist[Math.floorMod(far2, range)] > steps2) {
                    dist[Math.floorMod(far2, range)] = steps2;
                    pq.offer(new int[] {steps2, far2});
                }
            }
        }
        return dist[0];
    }

    // BFS + Queue
    // beats %(331 ms for tests)
    public int racecar4(int target) {
        Set<String> visited = new HashSet<>();
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[] {0, 1});
        for (int level = 0;; level++) {
            for (int i = queue.size(); i > 0; i--) {
                int[] cur = queue.poll();
                int pos = cur[0];
                int speed = cur[1];
                if (pos == target) return level;

                if (Math.abs(pos - target) <= target
                    && visited.add(pos + "," + speed)) {
                    queue.offer(new int[] {pos + speed, speed * 2});
                    queue.offer(new int[] {pos, speed > 0 ? -1 : 1});
                }
            }
        }
    }

    void test(int target, int expected) {
        assertEquals(expected, racecar(target));
        assertEquals(expected, racecar2(target));
        assertEquals(expected, racecar3(target));
        assertEquals(expected, racecar4(target));
    }

    @Test
    public void test() {
        test(1, 1);
        test(2, 4);
        test(3, 2);
        test(4, 5);
        test(5, 7);
        test(6, 5);
        test(7, 3);
        test(8, 6);
        test(9, 8);
        test(10, 7);
        test(100, 19);
        test(1000, 23);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
