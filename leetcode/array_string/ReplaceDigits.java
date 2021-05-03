import org.junit.Test;

import static org.junit.Assert.*;

// LC1844: https://leetcode.com/problems/replace-all-digits-with-characters/
//
// You are given a 0-indexed string s that has lowercase English letters in its even indices and
// digits in its odd indices.
// There is a function shift(c, x), where c is a character and x is a digit, that returns the xth
// character after c.
// For example, shift('a', 5) = 'f' and shift('x', 0) = 'x'.
// For every odd index i, you want to replace the digit s[i] with shift(s[i-1], s[i]).
// Return s after replacing all digits. It is guaranteed that shift(s[i-1], s[i]) will never exceed
// 'z'.
//
// Constraints:
// 1 <= s.length <= 100
// s consists only of lowercase English letters and digits.
// shift(s[i-1], s[i]) <= 'z' for all odd indices i.
public class ReplaceDigits {
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100%), 37.3 MB(%) for 99 tests
    public String replaceDigits(String s) {
        char[] cs = s.toCharArray();
        for (int i = 0; i + 1 < cs.length; i += 2) {
            cs[i + 1] = (char)(cs[i] + cs[i + 1] - '0');
        }
        return String.valueOf(cs);
    }

    private void test(String s, String expected) {
        assertEquals(expected, replaceDigits(s));
    }

    @Test public void test1() {
        test("a1c1e1", "abcdef");
        test("a1b2c3d4e", "abbdcfdhe");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
