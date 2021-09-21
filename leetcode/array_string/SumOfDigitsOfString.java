import org.junit.Test;

import static org.junit.Assert.*;

// LC1945: https://leetcode.com/problems/sum-of-digits-of-string-after-convert/
//
// You are given a string s consisting of lowercase English letters, and an integer k.
// First, convert s into an integer by replacing each letter with its position in the alphabet
// (i.e., replace 'a' with 1, 'b' with 2, ..., 'z' with 26). Then, transform the integer by
// replacing it with the sum of its digits. Repeat the transform operation k times in total.
// For example, if s = "zbax" and k = 2, then the resulting integer would be 8 by the following
// operations:
// Convert: "zbax" ➝ "(26)(2)(1)(24)" ➝ "262124" ➝ 262124
// Transform #1: 262124 ➝ 2 + 6 + 2 + 1 + 2 + 4 ➝ 17
// Transform #2: 17 ➝ 1 + 7 ➝ 8
// Return the resulting integer after performing the operations described above.
//
// Constraints:
// 1 <= s.length <= 100
// 1 <= k <= 10
// s consists of lowercase English letters.
public class SumOfDigitsOfString {
    // time complexity: O(N+K*log(N)), space complexity: O(1)
    // 1 ms(87.25%), 38.6 MB(64.68%) for 216 tests
    public int getLucky(String s, int k) {
        int sum = 0;
        for (int i = 0, n = s.length(); i < n; i++) {
            int val = s.charAt(i) - 'a' + 1;
            sum += val / 10 + val % 10;
        }
        for (int j = 1; j < k; j++) {
            int nextSum = 0;
            for (int x = sum; x > 0; x /= 10) {
                nextSum += x % 10;
            }
            sum = nextSum;
        }
        return sum;
    }

    private void test(String s, int k, int expected) {
        assertEquals(expected, getLucky(s, k));
    }

    @Test public void test() {
        test("iiii", 1, 36);
        test("leetcode", 2, 6);
        test("zbax", 2, 8);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
