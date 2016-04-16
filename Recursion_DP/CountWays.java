import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 9.1:
 * A child is running up a staircase with n steps, and can hop either 1 step,
 * 2 steps, or 3 steps at a time. Implement a method to count how many possible
 * ways the child can run up the stairs.
 */
public class CountWays {
    public static int countWays(int n) {
        if (n < 0) return 0;
        if (n == 0) return 1;

        return countWays(n - 1) // last step is 1
               + countWays(n - 2) // last step is 2
               + countWays(n - 3); // last step is 3
    }

    public static int countWaysDP(int n, int[] map) {
        if (n < 0) return 0;
        if (n == 0) return 1;

        if (map[n] > 0) return map[n];
        map[n] = countWaysDP(n - 1, map)
                 + countWaysDP(n - 2, map) + countWaysDP(n - 3, map);
        return map[n];
    }

    public static void main(String[] args) {
        // org.junit.runner.JUnitCore.main("CountWays");
        int round = 37; // cannot be bigger or result will be overflow
        for (int i = 0; i < round; i++) {
            System.out.format("*** n = %d ***\n", i);
            long t1 = System.nanoTime();
            int count1 = countWays(i);
            System.out.format("recursive: %.3f ms ", (System.nanoTime() - t1) * 1e-6);

            long t2 = System.nanoTime();
            int[] map = new int[round + 1];
            int count2 = countWaysDP(i, map);
            System.out.format("DP: %.3f ms ", (System.nanoTime() - t2) * 1e-6);
            assertEquals(count1, count2);
            System.out.println("count=" + count2);
        }
    }
}
