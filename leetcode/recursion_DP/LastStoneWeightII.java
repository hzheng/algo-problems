import java.util.*;
import java.util.stream.IntStream;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1049: https://leetcode.com/problems/last-stone-weight-ii/
//
// We have a collection of rocks, each rock has a positive integer weight. Each turn, we choose any
// two rocks and smash them together.  Suppose the stones have weights x and y with x <= y.  The
// result of this smash is:
// If x == y, both stones are totally destroyed;
// If x != y, the stone of weight x is totally destroyed, and the one of weight y has weight y-x.
// At the end, there is at most 1 stone left.  Return the smallest possible weight of this stone
// (the weight is 0 if there are no stones left.)
//
// Note:
// 1 <= stones.length <= 30
// 1 <= stones[i] <= 100
public class LastStoneWeightII {
    // Dynamic Programming
    // time complexity: O(N * SUM), space complexity: O(SUM)
    // 35 ms(%), 33.7 MB(100%) for 82 tests
    public int lastStoneWeightII(int[] stones) {
        int sum = IntStream.of(stones).sum();
        boolean[] dp = new boolean[sum + 1];
        dp[0] = true;
        int res = sum;
        for (int stone : stones) {
            for (int s = sum; s >= stone; s--) {
                if (dp[s] |= dp[s - stone]) {
                    res = Math.min(res, Math.abs(s * 2 - sum));
                }
            }
        }
        return res;
    }

    // Dynamic Programming
    // time complexity: O(N * SUM), space complexity: O(SUM)
    // 1 ms(%), 34 MB(100%) for 82 tests
    public int lastStoneWeightII_2(int[] stones) {
        final int MAX = 30 * 100 / 2;
        boolean[] dp = new boolean[MAX + 1];
        dp[0] = true;
        int sum = 0;
        for (int stone : stones) {
            sum += stone;
            for (int i = MAX; i >= stone; i--) {
                dp[i] |= dp[i - stone];
            }
        }
        int res = sum;
        for (int i = sum / 2; i >= 0; i--) {
            if (dp[i]) {
                res = Math.min(res, sum - i * 2);
            }
        }
        return res;
    }

    void test(int[] stones, int expected) {
        assertEquals(expected, lastStoneWeightII(stones));
        assertEquals(expected, lastStoneWeightII_2(stones));
    }

    @Test
    public void test() {
        test(new int[]{2, 7, 4, 1, 8, 1}, 1);
        test(new int[]{31, 26, 33, 21, 40}, 5);
        test(new int[]{11, 97, 31, 26, 33, 21, 40, 72, 63}, 0);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
