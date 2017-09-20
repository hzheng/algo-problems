import org.junit.Test;
import static org.junit.Assert.*;

// LC409: https://leetcode.com/problems/longest-palindrome/
//
// Given a string which consists of lowercase or uppercase letters, find the
// length of the longest palindromes that can be built with those letters.
// Note:
// Assume the length of given string will not exceed 1,010.
public class LongestPalindrome {
    // beats N/A(13 ms for 95 tests)
    public int longestPalindrome(String s) {
        int[] counts = new int[52];
        for (char c : s.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                counts[c - 'A']++;
            } else {
                counts[c - 'a' + 26]++;
            }
        }
        int res = 0;
        boolean hasOdd = false;
        for (int c : counts) {
            if ((c & 1) == 0) {
                res += c;
            } else {
                if (!hasOdd) {
                    hasOdd = true;
                    res++;
                }
                res += c & ~1;
            }
        }
        return res;
    }

    // beats N/A(10 ms for 95 tests)
    public int longestPalindrome2(String s) {
        boolean[] map = new boolean[128];
        int len = 0;
        for (char c : s.toCharArray()) {
            map[c] = !map[c];
            if (!map[c]) {
                len += 2;
            }
        }
        return (len < s.length()) ? len + 1 : len;
    }

    void test(String s, int expected) {
        assertEquals(expected, longestPalindrome(s));
        assertEquals(expected, longestPalindrome2(s));
    }

    @Test
    public void test1() {
        test("aaaAaaaa", 7);
        test("abccccdd", 7);
        test("ccc", 3);
        test("Acc", 3);
        test("abc", 1);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LongestPalindrome");
    }
}
