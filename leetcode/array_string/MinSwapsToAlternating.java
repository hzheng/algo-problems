import org.junit.Test;

import static org.junit.Assert.*;

// LC1864: https://leetcode.com/problems/minimum-number-of-swaps-to-make-the-binary-string-alternating/
//
// Given a binary string s, return the minimum number of character swaps to make it alternating, or
// -1 if it is impossible.
// The string is called alternating if no two adjacent characters are equal. For example, the
// strings "010" and "1010" are alternating, while the string "0100" is not.
// Any two characters may be swapped, even if they are not adjacent.
//
// Constraints:
//
// 1 <= s.length <= 1000
// s[i] is either '0' or '1'.
public class MinSwapsToAlternating {
    // Two Pointers
    // time complexity: O(N), space complexity: O(N)
    // 1 ms(%), 36.8 MB(%) for 124 tests
    public int minSwaps(String s) {
        char[] cs = s.toCharArray();
        int zeros = 0;
        for (char c : cs) {
            zeros += (c == '0') ? 1 : 0;
        }
        int diff = cs.length - zeros * 2;
        if (Math.abs(diff) > 1) { return -1; }
        if (diff == 1) { return minSwap(cs, 1); }
        if (diff == -1) { return minSwap(cs, 0); }

        return Math.min(minSwap(cs, 0), minSwap(cs, 1));
    }

    private int minSwap(char[] s, int zeroPos) {
        int res = 0;
        for (int i = 0, j = s.length - 1; i < j; ) {
            if (((s[i] - '0') ^ (i & 1)) == zeroPos) {
                i++;
                continue;
            }
            if (((s[j] - '0') ^ (j & 1)) == zeroPos) {
                j--;
                continue;
            }
            res++;
            i++;
            j--;
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(N)
    // 1 ms(%), 37.4 MB(%) for 124 tests
    public int minSwaps2(String s) {
        char[] cs = s.toCharArray();
        int zeros = 0;
        for (char c : cs) {
            zeros += (c == '0') ? 1 : 0;
        }
        int diff = cs.length - zeros * 2;
        if (Math.abs(diff) > 1) { return -1; }
        if (diff == 1) { return minSwap2(cs, '1'); }
        if (diff == -1) { return minSwap2(cs, '0'); }

        return Math.min(minSwap2(cs, '0'), minSwap2(cs, '1'));
    }

    private int minSwap2(char[] s, char c) {
        int swaps = 0;
        for (char ch : s) {
            if (ch != c) {
                swaps++;
            }
            c ^= 1;
        }
        return swaps / 2;
    }

    private void test(String s, int expected) {
        assertEquals(expected, minSwaps(s));
        assertEquals(expected, minSwaps2(s));
    }

    @Test public void test1() {
        test("111000", 1);
        test("010", 0);
        test("1110", -1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
