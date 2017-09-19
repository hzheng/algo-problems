import org.junit.Test;
import static org.junit.Assert.*;

// LC634: https://leetcode.com/problems/find-the-derangement-of-an-array/
//
// A derangement is a permutation of the elements of a set, such that no element
// appears in its original position. There's originally an array consisting of n
// integers from 1 to n in ascending order, you need to find the number of
// derangement it can generate. Also, since the answer may be very large, you
// should return the output mod 10 ^ 9 + 7.
// Note: n is in the range of [1, 106].
public class Derangement {
    private static final int MOD = 1_000_000_000 + 7;

    // Dynamic Programming(Bottom-Up)
    // time complexity: O(N), space complexity: O(N)
    // beats 75.82%(24 ms for 69 tests)
    public int findDerangement(int n) {
        if (n < 2) return 1 - n;

        int[] dp = new int[n + 1];
        dp[0] = 1;
        for (int i = 2; i <= n; i++) {
            dp[i] = (int)(((i - 1L) * (dp[i - 1] + dp[i - 2])) % MOD);
        }
        return dp[n] % MOD;
    }

    // Dynamic Programming(Bottom-Up)
    // time complexity: O(N), space complexity: O(1)
    // beats 88.16%(19 ms for 69 tests)
    public int findDerangement2(int n) {
        if (n < 2) return 1 - n;

        int c = 0;
        for (int i = 2, a = 1, b = 0; i <= n; i++) {
            c = (int)((i - 1L) * (a + b) % MOD);
            a = b;
            b = c;
        }
        return (int)(c % MOD);
    }

    // Dynamic Programming(Top-Down)
    // time complexity: O(N), space complexity: O(N)
    // Stack Overflow
    public int findDerangement3(int n) {
        return find(n, new Integer[n + 1]);
    }

    private int find(int n, Integer[] memo) {
        if (n < 2) return 1 - n;

        if (memo[n] != null) return memo[n];

        memo[n] = (int)(((n - 1L) * (find(n - 1, memo) + find(n - 2, memo))) % MOD);
        return memo[n];
    }

    // https://leetcode.com/articles/find-derangements/#approach-5-using-formula-accepted
    // Math(Set theory)
    // time complexity: O(N), space complexity: O(1)
    // beats 80.35%(22 ms for 69 tests)
    public int findDerangement4(int n) {
        long mul = 1;
        long sum = 0;
        for (int i = n; i >= 0; i--) {
            sum = (sum + MOD + mul * (1 - ((i & 1) << 1))) % MOD;
            mul = (mul * i) % MOD;
        }
        return (int)sum;
    }

    // https://en.wikipedia.org/wiki/Derangement
    // Math(Set theory)
    // time complexity: O(N), space complexity: O(1)
    // beats 47.61%(28 ms for 69 tests)
    public int findDerangement5(int n) {
        long res = 1;
        for (int i = 1; i <= n; i++) {
            res = (i * res % MOD + (1 - ((i & 1) << 1))) % MOD;
        }
        return (int)res;
    }

    void test(int n, int expected) {
        assertEquals(expected, findDerangement(n));
        assertEquals(expected, findDerangement2(n));
        if (n < 15) {
            assertEquals(expected, findDerangement3(n));
        }
        assertEquals(expected, findDerangement4(n));
        assertEquals(expected, findDerangement5(n));
    }

    @Test
    public void test() {
        test(0, 1);
        test(1, 0);
        test(2, 1);
        test(3, 2);
        test(4, 9);
        test(5, 44);
        test(6, 265);
        test(7, 1854);
        test(8, 14833);
        test(9, 133496);
        test(10, 1334961);
        test(11, 14684570);
        test(12, 176214841);
        test(13, 290792918);
        test(14, 71100825);
        test(23123, 972826001);
    }

    public static void main(String[] args) {
        String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
