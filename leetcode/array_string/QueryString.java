import org.junit.Test;

import static org.junit.Assert.*;

// LC1016: https://leetcode.com/problems/binary-string-with-substrings-representing-1-to-n/
//
// Given a binary string S (a string consisting only of '0' and '1's) and a positive integer N,
// return true if and only if for every integer X from 1 to N, the binary representation of X is a
// substring of S.
//
// Note:
// 1 <= S.length <= 1000
// 1 <= N <= 10^9
public class QueryString {
    // time complexity: O(LEN^2), space complexity: O(1)
    // 0 ms(100.00%), 36.9 MB(69.78%) for 25 tests
    public boolean queryString(String S, int N) {
        int n = S.length();
        int max = n * (n + 1) / 2; // at most different numbers by choosing any two position
        if (N > max) { return false; }

        for (int a = N; a > N / 2; a--) {
            if (!S.contains(Integer.toBinaryString(a))) { return false; }
        }
        return true;
    }

    // time complexity: O(LEN^2), space complexity: O(1)
    // 0 ms(100.00%), 36.9 MB(69.78%) for 25 tests
    public boolean queryString2(String S, int N) {
        int n = S.length();
        // 1001 ~ 2000 have 1000 different continuous 10 digits,
        // while S has at most S - 9 different continuous 10 digits.
        if (N > n * 2) { return false; }

        for (int a = N; a > N / 2; a--) {
            if (!S.contains(Integer.toBinaryString(a))) { return false; }
        }
        return true;
    }

    // time complexity: O(LEN^2), space complexity: O(1)
    // 0 ms(100.00%), 36.7 MB(80.69%) for 25 tests
    public boolean queryString3(String S, int N) {
        for (int a = N; a > N / 2; a--) {
            if (!S.contains(Integer.toBinaryString(a))) { return false; }
        }
        return true;
    }

    private void test(String S, int N, boolean expected) {
        assertEquals(expected, queryString(S, N));
        assertEquals(expected, queryString2(S, N));
        assertEquals(expected, queryString3(S, N));
    }

    @Test public void test() {
        test("1", 1, true);
        test("0110", 3, true);
        test("0110", 4, false);
        test("1100", 4, true);
        test("01100", 5, false);
        test("101100", 5, true);
        test("1011010100110000100000000000011111101010101000011111101", 10000000, false);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
