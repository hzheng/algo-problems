import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1010: https://leetcode.com/problems/pairs-of-songs-with-total-durations-divisible-by-60/
//
// In a list of songs, the i-th song has a duration of time[i] seconds.
// Return the number of pairs of songs for which their total duration in seconds is divisible by 60.
// Formally, we want the number of indices i < j with (time[i] + time[j]) % 60 == 0.
// Note:
// 1 <= time.length <= 60000
// 1 <= time[i] <= 500
public class NumPairsDivisibleBy60 {
    // time complexity: O(N), space complexity: O(1)
    // 4 ms(55.39%), 42 MB(63.93%) for 34 tests
    public int numPairsDivisibleBy60(int[] time) {
        int res = 0;
        final int DIVISOR = 60;
        int[] counts = new int[DIVISOR];
        for (int t : time) {
            counts[t % DIVISOR]++;
        }
        for (int t : time) {
            counts[t % DIVISOR]--;
            res += counts[(DIVISOR - t % DIVISOR) % DIVISOR];
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // 3 ms(85.11%), 42 MB(66.84%) for 34 tests
    public int numPairsDivisibleBy60_2(int[] time) {
        int res = 0;
        final int DIVISOR = 60;
        int counts[] = new int[DIVISOR];
        for (int t : time) {
            res += counts[(DIVISOR - t % DIVISOR) % DIVISOR];
            counts[t % DIVISOR]++;
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // 2 ms(99.70%), 41.8 MB(71.20%) for 34 tests
    public int numPairsDivisibleBy60_3(int[] time) {
        int res = 0;
        final int DIVISOR = 60;
        int[] counts = new int[DIVISOR];
        for (int t : time) {
            counts[t % DIVISOR]++;
        }
        for (int i = DIVISOR / 2 - 1; i > 0; i--) {
            res += counts[i] * counts[DIVISOR - i];
        }
        res += counts[0] * (counts[0] - 1) / 2;
        return res + counts[DIVISOR / 2] * (counts[DIVISOR / 2] - 1) / 2;
    }

    void test(int[] time, int expected) {
        assertEquals(expected, numPairsDivisibleBy60(time));
        assertEquals(expected, numPairsDivisibleBy60_2(time));
        assertEquals(expected, numPairsDivisibleBy60_3(time));
    }

    @Test
    public void test() {
        test(new int[]{30, 20, 150, 100, 40}, 3);
        test(new int[]{60, 60, 60}, 3);
        test(new int[]{21, 160, 71, 89, 21, 30, 90, 39, 60, 60}, 4);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
