import org.junit.Test;

import static org.junit.Assert.*;

// LC1616: https://leetcode.com/problems/split-two-strings-to-make-palindrome/
//
// You are given two strings a and b of the same length. Choose an index and split both strings at
// the same index, splitting a into two strings: aprefix and asuffix where a = aprefix + asuffix,
// and splitting b into two strings: bprefix and bsuffix where b = bprefix + bsuffix. Check if
// aprefix + bsuffix or bprefix + asuffix forms a palindrome.
// When you split a string s into sprefix and ssuffix, either ssuffix or sprefix is allowed to be
// empty. Return true if it is possible to form a palindrome string, otherwise return false.
// Constraints:
// 1 <= a.length, b.length <= 105
// a.length == b.length
// a and b consist of lowercase English letters
public class SplitToPalindrome {
    // DFS + Recursion
    // time complexity: O(N), space complexity: O(1)
    // 15 ms(22.41%), 45.7 MB(5.02%) for 105 tests
    public boolean checkPalindromeFormation(String a, String b) {
        char[] ca = a.toCharArray();
        char[] cb = b.toCharArray();
        return isPal(ca, cb, 0, false) || isPal(cb, ca, 0, false);
    }

    private boolean isPal(char[] a, char[] b, int cur, boolean switched) {
        if (cur >= a.length / 2) { return true; }

        if (a[cur] == b[a.length - 1 - cur]) {
            if (isPal(a, b, cur + 1, switched)) { return true; }
        }
        return !switched && (isPal(b, b, cur, true) || isPal(a, a, cur, true));
    }

    // Two Pointers + Greedy
    // time complexity: O(N), space complexity: O(1)
    // 3 ms(96.20%), 39.6 MB(5.02%) for 105 tests
    public boolean checkPalindromeFormation2(String a, String b) {
        return check(a, b) || check(b, a);
    }

    private boolean check(String a, String b) {
        for (int i = 0, j = a.length() - 1; i < j; i++, j--) {
            if (a.charAt(i) != b.charAt(j)) {
                return isPal(a, i, j) || isPal(b, i, j);
            }
        }
        return true;
    }

    private boolean isPal(String s, int start, int end) {
        for (int i = start, j = end; i < j; i++, j--) {
            if (s.charAt(i) != s.charAt(j)) { return false; }
        }
        return true;
    }

    private void test(String a, String b, boolean expected) {
        assertEquals(expected, checkPalindromeFormation(a, b));
        assertEquals(expected, checkPalindromeFormation2(a, b));
    }

    @Test public void test() {
        test("x", "y", true);
        test("abdef", "fecab", true);
        test("ulacfd", "jizalu", true);
        test("xbdef", "xecab", false);
        test("cdeoo", "oooab", true);
        test("pvhmupgqeltozftlmfjjde", "yjgpzbezspnnpszebzmhvp", true);
        test("aejbaalflrmkswrydwdkdwdyrwskmrlfqizjezd", "uvebspqckawkhbrtlqwblfwzfptanhiglaabjea",
             true);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
