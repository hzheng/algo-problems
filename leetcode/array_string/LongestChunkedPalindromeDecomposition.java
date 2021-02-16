import org.junit.Test;

import static org.junit.Assert.*;

// LC1147: https://leetcode.com/problems/longest-chunked-palindrome-decomposition/
//
// You are given a string text. You should split it to k substrings
// (subtext1, subtext2, ..., subtextk) such that:
// subtexti is a non-empty string.
// The concatenation of all the substrings is equal to text
// subtexti == subtextk - i + 1 for all valid values of i (i.e., 1 <= i <= k).
// Return the largest possible value of k.
//
// Constraints:
// 1 <= text.length <= 1000
// text consists only of lowercase English characters.
public class LongestChunkedPalindromeDecomposition {
    // Recursion
    // time complexity: O(N^2), space complexity: O(1)
    // 0 ms(100.00%), 37.2 MB(88.18%) for 83 tests
    public int longestDecomposition(String text) {
        return longestDecomposition(text.toCharArray(), 0, text.length());
    }

    public int longestDecomposition(char[] s, int start, int end) {
        outer:
        for (int k = 1; k <= (end - start) / 2; k++) {
            for (int i = start, j = end - k; i < start + k; i++, j++) {
                if (s[i] != s[j]) { continue outer; }
            }
            return 2 + longestDecomposition(s, start + k, end - k);
        }
        return (start >= end) ? 0 : 1;
    }

    // time complexity: O(N^2), space complexity: O(N)
    // 11 ms(33.50%), 39.4 MB(33.50%) for 83 tests
    public int longestDecomposition2(String text) {
        int res = 0;
        String left = "";
        String right = "";
        for (int i = 0, n = text.length(); i < n; i++) {
            left += text.charAt(i);
            right = text.charAt(n - i - 1) + right;
            if (left.equals(right)) {
                res++;
                left = "";
                right = "";
            }
        }
        return res;
    }

    // Rolling Hash
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(33.50%), 37.3 MB(85.22%) for 83 tests
    public int longestDecomposition3(String text) {
        int count = 0;
        long h1 = 0;
        long h2 = 0;
        long base1 = 26;
        long base2 = 1;
        for (int i = 0, j = text.length() - 1; i <= j; i++, j--) {
            h1 = h1 * base1 + (text.charAt(i) - 'a' + 1);
            h2 += (text.charAt(j) - 'a' + 1) * base2;
            base2 *= base1;
            if (h1 == h2) {
                count += (i == j) ? 1 : 2;
                h1 = h2 = 0;
                base2 = 1;
            }
        }
        return count + ((h1 != 0) ? 1 : 0);
    }

    private void test(String text, int expected) {
        assertEquals(expected, longestDecomposition(text));
        assertEquals(expected, longestDecomposition2(text));
        assertEquals(expected, longestDecomposition3(text));
    }

    @Test public void test() {
        test("ab", 1);
        test("aaa", 3);
        test("ghiabcdefhelloadamhelloabcdefghi", 7);
        test("merchant", 1);
        test("antaprezatepzapreanta", 11);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
