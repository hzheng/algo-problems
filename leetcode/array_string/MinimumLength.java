import org.junit.Test;

import static org.junit.Assert.*;

// LC1750: https://leetcode.com/problems/minimum-length-of-string-after-deleting-similar-ends/
//
// Given a string s consisting only of characters 'a', 'b', and 'c'. You are asked to apply the
// following algorithm on the string any number of times:
// Pick a non-empty prefix from the string s where all the characters in the prefix are equal.
// Pick a non-empty suffix from the string s where all the characters in this suffix are equal.
// The prefix and the suffix should not intersect at any index.
// The characters from the prefix and suffix must be the same.
// Delete both the prefix and the suffix.
// Return the minimum length of s after performing the above operation any number of times (possibly
// zero times).
//
// Constraints:
// 1 <= s.length <= 10^5
// s only consists of characters 'a', 'b', and 'c'.
public class MinimumLength {
    // Two Pointers
    // time complexity: O(N), space complexity: O(1)
    // 4 ms(68.69%), 39.3 MB(88.28%) for 100 tests
    public int minimumLength(String s) {
        for (int i = 0, j = s.length() - 1; i <= j; i++, j--) {
            if (i == j || s.charAt(i) != s.charAt(j)) { return j - i + 1; }

            for (; i < j && s.charAt(i) == s.charAt(i + 1); i++) {}
            for (; i < j && s.charAt(j) == s.charAt(j - 1); j--) {}
        }
        return 0;
    }

    // Two Pointers
    // time complexity: O(N), space complexity: O(1)
    // 3 ms(92.05%), 39.8 MB(62.13%) for 100 tests
    public int minimumLength2(String s) {
        int i = 0;
        int j = s.length() - 1;
        while (i < j && s.charAt(i) == s.charAt(j)) {
            char c = s.charAt(i);
            for ( ; i <= j && s.charAt(i) == c; i++) {}
            for ( ; i <= j && s.charAt(j) == c; j--) {}
        }
        return i > j ? 0 : j - i + 1;
    }

    private void test(String s, int expected) {
        assertEquals(expected, minimumLength(s));
        assertEquals(expected, minimumLength2(s));
    }

    @Test public void test() {
        test("ca", 2);
        test("cabaabac", 0);
        test("aabccabba", 3);
        test("cbc", 1);
        test("bbbbbbbbbbbbbbbbbbbbbbbbbbbabbbbbbbbbbbbbbbccbcbcbccbbabbb", 1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
