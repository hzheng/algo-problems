import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC486: https://leetcode.com/problems/predict-the-winner/
//
// Given an array of scores that are non-negative integers. Player 1 picks one
// of the numbers from either end of the array followed by the player 2 and then
// player 1 and so on. Each time a player picks a number, that number will not
// be available for the next player. This continues until all the scores have
// been chosen. The player with the maximum score wins.
// Given an array of scores, predict whether player 1 is the winner.
public class PredictTheWinner {
    // Recursion + Minmax
    // beats 37.83%(16 ms for 60 tests)
    public boolean PredictTheWinner(int[] nums) {
        return nums.length < 2 || predict(nums, 0, nums.length, 0, 0);
    }

    private boolean predict(int[] nums, int start, int end, int sum1, int sum2) {
        if (start >= end) return sum1 >= sum2;

        return !predict(nums, start + 1, end, sum2, sum1 + nums[start])
               || !predict(nums, start, end - 1, sum2, sum1 + nums[end - 1]);
    }

    // Recursion + Minmax
    // beats 10.69%(131 ms for 60 tests)
    public boolean PredictTheWinner2(int[] nums) {
        return diff(nums, 0, nums.length - 1) >= 0;
    }

    private int diff(int[] nums, int start, int end) {
        if (start == end) return nums[start];

        return Math.max(nums[end] - diff(nums, start, end - 1),
                        nums[start] - diff(nums, start + 1, end));
    }

    // Recursion + Minmax + Dynamic Programming(Top-Down)
    // beats 37.01%(17 ms for 60 tests)
    public boolean PredictTheWinner3(int[] nums) {
        return diff(nums, 0, nums.length - 1, new HashMap<>()) >= 0;
    }

    private int diff(int[] nums, int start, int end, Map<Long, Integer> memo) {
        if (start == end) return nums[start];

        long key = ((long)start << 32) | end;
        Integer d = memo.get(key);
        if (d == null) {
            d = Math.max(nums[end] - diff(nums, start, end - 1, memo),
                         nums[start] - diff(nums, start + 1, end, memo));
            memo.put(key, d);
        }
        return d;
    }

    // Dynamic Programming(Bottom-Up)
    // beats 94.41%(5 ms for 60 tests)
    public boolean PredictTheWinner4(int[] nums) {
        int n = nums.length;
        int[][] dp = new int[n][n];
        for (int i = 1; i < n; i++) {
            for (int j = 0; j + i < n; j++) {
                dp[j][j + i] = Math.max(nums[j + i] - dp[j][j + i - 1],
                                        nums[j] - dp[j + 1][j + i]);
            }
        }
        return dp[0][n - 1] >= 0;
    }

    void test(int[] nums, boolean expected) {
        assertEquals(expected, PredictTheWinner(nums));
        assertEquals(expected, PredictTheWinner2(nums));
        assertEquals(expected, PredictTheWinner3(nums));
        assertEquals(expected, PredictTheWinner4(nums));
    }

    @Test
    public void test() {
        test(new int[] {0}, true);
        test(new int[] {1}, true);
        test(new int[] {1, 5, 2}, false);
        test(new int[] {1, 5, 233, 7}, true);
        test(new int[] {1, 5, 23, 18, 29, 10, 6, 9, 7, 4, 20, 8}, true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PredictTheWinner");
    }
}
