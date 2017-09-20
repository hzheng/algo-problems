import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC214: https://leetcode.com/problems/shortest-palindrome/
//
// Given a string S, you are allowed to convert it to a palindrome by adding
// characters in front of it. Find and return the shortest palindrome you can
// find by performing this transformation.
public class ShortestPalindrome {
    // time complexity: O(N ^ 2)
    // beats 21.96%(123 ms)
    public String shortestPalindrome(String s) {
        int len = s.length();
        if (len < 2) return s;

        for (int i = (len - 1) / 2; i >= 0; i--) {
            if (s.charAt(i) == s.charAt(i + 1) && (2 * i + 1) < len)  {
                String res = palindromeString(s, i, i + 1);
                if (res != null) return res;
            }
            String res = palindromeString(s, i - 1, i + 1);
            if (res != null) return res;
        }
        return null;
    }

    private String palindromeString(String s, int left, int right) {
        int i = left;
        int j = right;
        for (; i >= 0 && s.charAt(i) == s.charAt(j); i--, j++) {}
        return (i >= 0) ? null : complete(s, j);
    }

    private String complete(String s, int right) {
        StringBuilder sb = new StringBuilder();
        for (int i = s.length() - 1; i >= right; i--) {
            sb.append(s.charAt(i));
        }
        sb.append(s);
        return sb.toString();
    }

    // converted to problem: longest palindrome start from beginning
    // time complexity: O(N ^ 2)
    // Time Limit Exceeded
    public String shortestPalindrome2(String s) {
        for (int right = s.length() - 1;; right--) {
            boolean isPalindrome = true;
            for (int i = 0, j = right; i < j; i++, j--) {
                if (s.charAt(i) != s.charAt(j)) {
                    isPalindrome = false;
                    break;
                }
            }
            if (isPalindrome) return complete(s, right + 1);
        }
    }

    // Solution of Choice
    // Manacher's algorithm
    // time complexity: O(N), space complexity: O(N)
    // beats 63.83%(9 ms for 119 tests)
    public String shortestPalindrome3(String s) {
        int l = s.length();
        char[] s2 = addBoundaries(s.toCharArray());
        int[] maxPalindrome = new int[l + 1]; // max palindrome at i
        int center = 0; // center of the max palindrome currently known
        int rBound = 0; // right-most boundary of the palindrome at 'center'
        int m = 0, n = 0;  // walking indices to compare 2 elements
        for (int i = 1; i <= l; i++) {
            if (i > rBound) { // reset
                maxPalindrome[i] = 0;
                m = i - 1;
                n = i + 1;
            } else {
                int j = center * 2 - i; // mirror of i
                if (maxPalindrome[j] < (rBound - i)) {
                    maxPalindrome[i] = maxPalindrome[j];
                    m = -1;     // bypass the while loop below
                } else {
                    maxPalindrome[i] = rBound - i;
                    n = rBound + 1;
                    m = i * 2 - n;
                }
            }
            for (; m >= 0 && n < s2.length && s2[m] == s2[n]; m--, n++) {
                maxPalindrome[i]++;
            }
            if ((i + maxPalindrome[i]) > rBound) { // update palindrome
                center = i;
                rBound = i + maxPalindrome[i];
            }
        }
        // find the longest one
        int len = 0;
        for (int i = 1; i <= l; i++) {
            int curLen = maxPalindrome[i];
            if (curLen == i && len < curLen) {
                len = curLen;
            }
        }
        return complete(s, len);
    }

    private char[] addBoundaries(char[] cs) {
        final char DELIMITER = '|';
        char[] cs2 = new char[cs.length * 2 + 1];
        for (int i = 0; i < (cs2.length - 1); i += 2) {
            cs2[i] = DELIMITER;
            cs2[i + 1] = cs[i / 2];
        }
        cs2[cs2.length - 1] = DELIMITER;
        return cs2;
    }

    // Divide & Conquer + Recursion
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 87.15%(4 ms for 119 tests)
    public String shortestPalindrome4(String s) {
        int i = 0;
        for (int j = s.length() - 1; j >= 0; j--) {
            if (s.charAt(i) == s.charAt(j)) {
                i++;
            }
        }
        if (i == s.length()) return s;

        String suffix = s.substring(i);
        StringBuilder prefix = new StringBuilder(suffix).reverse();
        return prefix.append(shortestPalindrome4(s.substring(0, i)))
                     .append(suffix).toString();
    }

    // Solution of Choice
    // time complexity: O(N ^ 2), space complexity: O(1)
    // beats 66.53%(8 ms for 119 tests)
    public String shortestPalindrome5(String s) {
        int palindromeEnd = 0;
        for (int end = s.length() - 1; palindromeEnd <= end; ) {
            end = (palindromeEnd == 0) ? end : palindromeEnd - 1;
            palindromeEnd = 0;
            for (int i = end; i >= 0; i--) {
                if (s.charAt(i) == s.charAt(palindromeEnd)) {
                    palindromeEnd++;
                }
            }
        }
        return new StringBuilder(s.substring(palindromeEnd)).reverse()
               .append(s).toString();
    }

    // KMP algorithm
    // time complexity: O(N), space complexity: O(N)
    // beats 49.65%(13 ms for 120 tests)
    public String shortestPalindrome6(String s) {
        String t = s + "#" + new StringBuilder(s).reverse().toString();
        int len = t.length();
        // length of the longest prefix of t that is a suffix of t[0..i].
        int[] failTable = new int[len];
        for (int suffixPtr = 1; suffixPtr < len; suffixPtr++) {
            int prefixPtr = failTable[suffixPtr - 1];
            while (prefixPtr > 0 && t.charAt(prefixPtr) != t.charAt(suffixPtr)) {
                prefixPtr = failTable[prefixPtr - 1];
            }
            if (t.charAt(prefixPtr) == t.charAt(suffixPtr)) {
                failTable[suffixPtr] = prefixPtr + 1;
            }
        }
        return new StringBuilder(s.substring(failTable[len - 1])).reverse()
               .append(s).toString();
    }

    // Solution of Choice
    // KMP algorithm
    // time complexity: O(N), space complexity: O(N)
    // beats 58.66%(10 ms for 120 tests)
    public String shortestPalindrome7(String s) {
        String t = s + "#" + new StringBuilder(s).reverse().toString();
        int len = t.length();
        int[] kmp = new int[len];
        for (int i = 0, j = 1; i < len && j < len; j++) {
            if (t.charAt(i) == t.charAt(j)) {
                kmp[j] = ++i;
            } else if (i > 0) {
                i = kmp[i - 1];
                j--;
            }
        }
        return new StringBuilder(s.substring(kmp[len - 1])).reverse()
               .append(s).toString();
    }

    void test(Function<String, String> shortestPalindrome, String name,
              String s, String expected) {
        long t1 = System.nanoTime();
        assertEquals(expected, shortestPalindrome.apply(s));
        if (s.length() > 20) {
            System.out.format("%s: %.3f ms\n", name,
                              (System.nanoTime() - t1) * 1e-6);
        }
    }

    void test(String s, String expected) {
        ShortestPalindrome p = new ShortestPalindrome();
        test(p::shortestPalindrome, "shortestPalindrome", s, expected);
        test(p::shortestPalindrome2, "shortestPalindrome2", s, expected);
        test(p::shortestPalindrome3, "shortestPalindrome3", s, expected);
        test(p::shortestPalindrome4, "shortestPalindrome4", s, expected);
        test(p::shortestPalindrome5, "shortestPalindrome5", s, expected);
        test(p::shortestPalindrome6, "shortestPalindrome6", s, expected);
        test(p::shortestPalindrome7, "shortestPalindrome7", s, expected);
    }

    @Test
    public void test1() {
        test("aa", "aa");
        test("abb", "bbabb");
        test("abcd", "dcbabcd");
        test("aba", "aba");
        test("a", "a");
        test("ab", "bab");
        test("aacecaaa", "aaacecaaa");
        test("cbbcbdacbbc", "cbbcadbcbbcbdacbbc");
        test("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
             "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ShortestPalindrome");
    }
}
