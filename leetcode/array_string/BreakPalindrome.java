import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1328: https://leetcode.com/problems/break-a-palindrome/
//
// Given a palindromic string of lowercase English letters palindrome, replace exactly one character
// with any lowercase English letter so that the resulting string is not a palindrome and that it is
// the lexicographically smallest one possible.
// Return the resulting string. If there is no way to replace a character to make it not a
// palindrome, return an empty string.
// A string a is lexicographically smaller than a string b (of the same length) if in the first
// position where a and b differ, a has a character strictly smaller than the corresponding
// character in b.
//
// Constraints:
// 1 <= palindrome.length <= 1000
// palindrome consists of only lowercase English letters.
public class BreakPalindrome {
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 38.7 MB(10.22%) for 30 tests
    public String breakPalindrome(String palindrome) {
        char[] chars = palindrome.toCharArray();
        int n = chars.length;
        if (n == 1) {return "";}

        for (int i = 0; i < n / 2; i++) {
            if (chars[i] != 'a') {
                chars[i] = 'a';
                return String.valueOf(chars);
            }
        }
        chars[n - 1] = 'b';
        return String.valueOf(chars);
    }

    private void test(String palindrome, String expected) {
        assertEquals(expected, breakPalindrome(palindrome));
    }

    @Test public void test() {
        test("abccba", "aaccba");
        test("a", "");
        test("aa", "ab");
        test("aba", "abb");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
