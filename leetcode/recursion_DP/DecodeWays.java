import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC091: https://leetcode.com/problems/decode-ways/
//
// A message containing letters from A-Z is being encoded to numbers using the
// following mapping: 'A' -> 1 'B' -> 2 ... 'Z' -> 26. Given an encoded message
// containing digits, determine the total number of ways to decode it.
public class DecodeWays {
    // beats 52.85%(4 ms)
    public int numDecodings(String s) {
        int len = s.length();
        if (len == 0 || s.charAt(0) == '0') return 0;

        if (len == 1) return 1;

        int combineable = canCombine(s, 0);
        if (len == 2) {
            if (combineable > 0) return 2;
            return (combineable == 0 || s.charAt(1) != '0') ? 1 : 0;
        }

        if (combineable < 0) return numDecodings(s.substring(1));

        int count = numDecodings(s.substring(2));
        if (combineable == 0) return count;

        count *= 2;
        combineable = canCombine(s, 1);
        if (combineable < 0) return count;

        if (len == 3) {
            count++;
        } else if (len > 3) {
            count += numDecodings(s.substring(3));
        }
        return count;
    }

    private int canCombine(String s, int index) {
        char c = s.charAt(index);
        if (c > '2') return -1;

        char c2 = s.charAt(index + 1);
        if (c2 == '0') return 0;

        if (c2 == '1' || c2 == '2') {
            if (index + 2 < s.length() && s.charAt(index + 2) == '0') return -1;

            return 1;
        }
        return (c == '1' || c2 < '7') ? 1 : -1;
    }

    //  Time Limit Exceeded
    public int numDecodings2(String s) {
        int len = s.length();
        if (len == 0 || s.charAt(0) == '0') return 0;

        if (len == 1) return 1;

        char c1 = s.charAt(len - 1);
        char c2 = s.charAt(len - 2);
        int count2 = numDecodings(s.substring(0, len - 2));
        if (c1 == '0') {
            return (c2 != '1' && c2 != '2') ? 0 : (count2 == 0 ? 1 : count2);
        }

        int count1 = numDecodings(s.substring(0, len - 1));
        if ((c2 != '1' && c2 != '2') || (c2 == '2' && c1 > '6')) return count1;

        if (len == 2) return 2;

        count2 *= 2;
        char c3 = s.charAt(len - 3);
        if (c3 == '1' || c3 == '2') {
            int count3 = numDecodings(s.substring(0, len - 3));
            if (count3 == 0) {
                count3 = 1;
            }
            count2 += count3;
        }
        return count2;
    }

    //  Time Limit Exceeded
    public int numDecodings3(String s) {
        int len = s.length();
        if (len == 0 || s.charAt(0) == '0') return 0;

        if (len == 1) return 1;

        int count = numDecodings(s.substring(1));
        if (s.charAt(0) == '1' || (s.charAt(0) == '2' && s.charAt(1) <= '6')) {
            count += numDecodings(s.substring(2));
            if (len == 2) {
                count++;
            }
        }
        return count;
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // beats 68.87%(2 ms)
    public int numDecodings4(String s) {
        int len = s.length();
        if (len == 0) return 0;

        int[] dp = new int[len + 1];
        dp[0] = 1;
        dp[1] = (s.charAt(0) == '0') ? 0 : 1;
        for (int i = 2; i <= len; i++) {
            if (s.charAt(i - 1) != '0') {
                dp[i] = dp[i - 1];
            }

            int num = (s.charAt(i - 2) - '0') * 10 + s.charAt(i - 1) - '0';
            if (num >= 10 && num <= 26) {
                dp[i] += dp[i - 2];
            }
        }
        return dp[len];
    }

    // Solution of Choice
    // Dynamic Programming
    // beats 91.36%(1 ms)
    // time complexity: O(N), space complexity: O(1)
    public int numDecodings5(String s) {
        int len = s.length();
        if (len == 0 || s.charAt(0) == '0') return 0;

        int prev = 1;
        for (int i = 1, prev2 = 1; i < len; i++) {
            char c1 = s.charAt(i - 1);
            char c2 = s.charAt(i);
            int cur = (c2 > '0') ? prev : 0;
            if (c1 == '1' || (c1 == '2' && c2 < '7')) {
                cur += prev2;
            }
            prev2 = prev;
            prev = cur;
        }
        return prev;
    }

    // Dynamic Programming
    // beats 68.87%(2 ms)
    // time complexity: O(N), space complexity: O(N)
    public int numDecodings6(String s) {
        int len = s.length();
        if (len == 0) return 0;

        int[] dp = new int[len + 1];
        dp[len] = 1;
        dp[len - 1] = s.charAt(len - 1) == '0' ? 0 : 1;
        for (int i = len - 2; i >= 0; i--) {
            char c1 = s.charAt(i);
            if (c1 > '0') {
                dp[i] = dp[i + 1];
                if (c1 == '1' || (c1 == '2' && s.charAt(i + 1) < '7')) {
                    dp[i] += dp[i + 2];
                }
            }
        }
        return dp[0];
    }

    void test(String s, int expected) {
        assertEquals(expected, numDecodings(s));
        assertEquals(expected, numDecodings2(s));
        assertEquals(expected, numDecodings3(s));
        assertEquals(expected, numDecodings4(s));
        assertEquals(expected, numDecodings5(s));
        assertEquals(expected, numDecodings6(s));
    }

    @Test
    public void test1() {
        test("0", 0);
        test("90", 0);
        test("1230", 0);
        test("21123451980", 0);
        test("1980", 0);
        test("123", 3);
        test("198", 2);
        test("5198", 2);
        test("345198", 2);
        test("2112345198", 16);
        test("1210", 2);
        test("10", 1);
        test("120", 1);
        test("12", 2);
        test("123421", 6);
        test("123421123451", 24);
        test("12342112345198", 48);
        test("123421123451981", 48);
        test("12342112345198101", 48);
        test("123421123451981012", 96);
        test("1234211234519810123", 144);
        test("606581228788366876483154495868328329647968287789829361216813633"
             + "4983851946827579555449329483852397155", 0);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("DecodeWays");
    }
}
