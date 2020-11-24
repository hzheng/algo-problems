import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1663: https://leetcode.com/problems/smallest-string-with-a-given-numeric-value/
//
// The numeric value of a lowercase character is defined as its position(1-indexed) in the alphabet,
// so the numeric value of a is 1, the numeric value of b is 2, and so on. The numeric value of a
// string consisting of lowercase characters is defined as the sum of its characters' numeric values.
// For example, the numeric value of the string "abe" is equal to 1 + 2 + 5 = 8. You are given two
// integers n and k. Return the lexicographically smallest string with length equal to n and numeric
// value equal to k.
//
// Constraints:
// 1 <= n <= 10^5
// n <= k <= 26 * n
public class GetSmallestString {
    // time complexity: O(N), space complexity: O(N)
    // 9 ms(95.45%), 39.1 MB(63.20%) for 104 tests
    public String getSmallestString(int n, int k) {
        char[] res = new char[n];
        Arrays.fill(res, 'a');
        for (int left = k - n, i = n - 1, step = 'z' - 'a'; ; left -= step, res[i--] += step) {
            if (left <= step) {
                res[i] += left;
                return String.valueOf(res);
            }
        }
    }

    // time complexity: O(N), space complexity: O(N)
    // 10 ms(90.19%), 38.8 MB(90.89%) for 104 tests
    public String getSmallestString2(int n, int k) {
        char[] res = new char[n];
        Arrays.fill(res, 'a');
        for (int left = k - n, i = n - 1; left > 0; i--) {
            int step = Math.min(25, left);
            res[i] += step;
            left -= step;
        }
        return String.valueOf(res);
    }

    private void test(int n, int k, String expected) {
        assertEquals(expected, getSmallestString(n, k));
        assertEquals(expected, getSmallestString2(n, k));
    }

    @Test public void test() {
        test(3, 27, "aay");
        test(5, 73, "aaszz");
        test(3, 78, "zzz");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
