import org.junit.Test;
import static org.junit.Assert.*;

// LC680: https://leetcode.com/problems/valid-palindrome-ii/
//
// Given a non-empty string s, you may delete at most one character. Judge
// whether you can make it a palindrome.
public class ValidPalindrome2 {
    // Two Pointers
    // time complexity: O(N), space complexity: O(1)
    // beats 62.33%(41 ms for 460 tests)
    public boolean validPalindrome(String s) {
        for (int i = 0, j = s.length() - 1; i < j; i++, j--) {
            if (s.charAt(i) != s.charAt(j)) {
                return isPalindrome(s, i) || isPalindrome(s, j);
            }
        }
        return true;
    }

    private boolean isPalindrome(String s, int skip) {
        for (int i = 0, j = s.length() - 1; i < j; ) {
            if (i == skip) {
                i++;
            } else if (j == skip) {
                j--;
            } else {
                if (s.charAt(i) != s.charAt(j)) return false;
                i++;
                j--;
            }
        }
        return true;
    }

    // Two Pointers
    // time complexity: O(N), space complexity: O(1)
    // beats 86.46%(37 ms for 460 tests)
    public boolean validPalindrome2(String s) {
        for (int i = 0, j = s.length() - 1, lastI = -1, lastJ = -1; i < j;
             i++, j--) {
            if (s.charAt(i) == s.charAt(j)) continue;

            if (lastI == -1) { // skip i
                lastI = i;
                lastJ = j;
                j++;
            } else if (lastI >= 0) { // skip j
                i = lastI - 1;
                j = lastJ;
                lastI = -2;
            } else return false;
        }
        return true;
    }

    // Two Pointers
    // time complexity: O(N), space complexity: O(1)
    // beats 62.33%(41 ms for 460 tests)
    public boolean validPalindrome3(String s) {
        for (int n = s.length() - 1, i = 0, j = n, lastI = 0; i < j; i++, j--) {
            if (s.charAt(i) == s.charAt(j)) continue;

            if (i + j == n) { // skip i
                j++;
                lastI = i;
            } else if (i + j > n) { // skip j
                i = lastI - 1;
                j = n - lastI;
            } else return false;
        }
        return true;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats 30.38%(47 ms for 460 tests)
    public boolean validPalindrome4(String s) {
        for (int i = 0, n = s.length(); i < n / 2; i++) {
            if (s.charAt(i) != s.charAt(n - 1 - i)) {
                return isPalindromeRange(s, i + 1, n - 1 - i)
                       || isPalindromeRange(s, i, n - 2 - i);
            }
        }
        return true;
    }

    private boolean isPalindromeRange(String s, int i, int j) {
        for (int k = i; k <= i + (j - i) / 2; k++) {
            if (s.charAt(k) != s.charAt(j - k + i)) return false;
        }
        return true;
    }

    void test(String s, boolean expected) {
        assertEquals(expected, validPalindrome(s));
        assertEquals(expected, validPalindrome2(s));
        assertEquals(expected, validPalindrome3(s));
        assertEquals(expected, validPalindrome4(s));
    }

    @Test
    public void test() {
        test("aba", true);
        test("abca", true);
        test("abcca", true);
        test("abccaa", false);
        test("abccbad", true);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
