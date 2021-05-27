import org.junit.Test;

import static org.junit.Assert.*;

// LC1573: https://leetcode.com/problems/number-of-ways-to-split-a-string/
//
// Given a binary string s (a string consisting only of '0's and '1's), we can split s into 3
// non-empty strings s1, s2, s3 (s1+ s2+ s3 = s).
// Return the number of ways s can be split such that the number of characters '1' is the same in
// s1, s2, and s3.
// Since the answer may be too large, return it modulo 10^9 + 7.
//
// Constraints:
// 3 <= s.length <= 10^5
// s[i] is '0' or '1'.
public class NumWaysToSplit {
    private static final int MOD = 1000_000_007;

    // time complexity: O(N), space complexity: O(1)
    // 4 ms(100.00%), 39 MB(98.21%) for 164 tests
    public int numWays(String s) {
        char[] cs = s.toCharArray();
        int n = cs.length;
        int ones = 0;
        for (char c : cs) {
            ones += (c - '0');
        }
        if (ones % 3 != 0) { return 0; }
        if (ones == 0) { return (int)((n - 1L) * (n - 2) / 2 % MOD); }

        int firstCut = 0;
        int secondCut = 0;
        for (int i = 0, count = 0, k = ones / 3, k2 = k * 2; count <= k2; i++) {
            count += (cs[i] - '0');
            if (count == k) {
                firstCut++;
            } else if (count == k2) {
                secondCut++;
            }
        }
        return (int)((long)firstCut * secondCut % MOD);
    }

    private void test(String s, int expected) {
        assertEquals(expected, numWays(s));
    }

    @Test public void test1() {
        test("10101", 4);
        test("1001", 0);
        test("0000", 3);
        test("100100010100110", 12);
        test("1010010111011110101101011010101100001000100000100000100000011000001000010000011"
             + "000001000110000010000010001100100001001101111011111010100001100010110101010101001"
             + "0100011100011011010100001110011", 30);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
