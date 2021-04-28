import org.junit.Test;

import static org.junit.Assert.*;

// LC1837: https://leetcode.com/problems/sum-of-digits-in-base-k/Given an integer n (in base 10)
// and a base k, return the sum of the digits of n after converting n from base 10 to base k.
// After converting, each digit should be interpreted as a base 10 number, and the sum should be
// returned in base 10.
// Given a string S, you are allowed to convert it to a palindrome by adding
// characters in front of it. Find and return the shortest palindrome you can
// find by performing this transformation.
//
// Constraints:
// 1 <= n <= 100
// 2 <= k <= 10
public class LongestBeautifulSubstring {
    // Two Pointers
    // time complexity: O(N), space complexity: O(1)
    // 18 ms(86.66%), 45.1 MB(96.77%) for 96 tests
    public int longestBeautifulSubstring(String word) {
        int[] map = new int[26];
        int order = 0;
        for (char c : new char[] {'a', 'e', 'i', 'o', 'u'}) {
            map[c - 'a'] = order++;
        }
        int res = 0;
        for (int i = 0, j = 0, n = word.length(); j < n; j++) {
            if (i >= j) {
                if (word.charAt(i) == 'a') {
                    j = i;
                } else {
                    i++;
                }
                continue;
            }
            int cur = map[word.charAt(j) - 'a'];
            int prev = map[word.charAt(j - 1) - 'a'];
            if (cur != prev && cur != prev + 1) {
                i = j--;
            } else if (cur == 4) {
                res = Math.max(res, j - i + 1);
            }
        }
        return res;
    }

    // Two Pointers
    // time complexity: O(N), space complexity: O(1)
    // 17 ms(87.90%), 45.4 MB(84.51%) for 96 tests
    public int longestBeautifulSubstring2(String word) {
        int res = 0;
        for (int i = 0, j = 0, count = 0, n = word.length(); j < n; j++) {
            if (i >= j) {
                if (word.charAt(i) == 'a') {
                    j = i;
                    count = 1;
                } else {
                    i++;
                }
                continue;
            }
            int diff = word.charAt(j) - word.charAt(j - 1);
            if (diff < 0) {
                i = j--;
                count = 0;
            } else {
                count += (diff > 0) ? 1 : 0;
                if (count == 5)  {
                    res = Math.max(res, j - i + 1);
                }
            }
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // 20 ms(82.79%), 45.7 MB(68.05%) for 96 tests
    public int longestBeautifulSubstring3(String word) {
        int res = 0;
        for (int i = 1, count = 1, len = 1, n = word.length(); i < n; i++) {
            int diff = word.charAt(i) - word.charAt(i - 1);
            if (diff < 0) {
                len = count = 1;
            } else {
                len++;
                count += (diff > 0) ? 1 : 0;
                if (count == 5) {
                    res = Math.max(res, len);
                }
            }
        }
        return res;
    }

    private void test(String word, int expected) {
        assertEquals(expected, longestBeautifulSubstring(word));
        assertEquals(expected, longestBeautifulSubstring2(word));
        assertEquals(expected, longestBeautifulSubstring3(word));
    }

    @Test public void test1() {
        test("aeiaaioaaaaeiiiiouuuooaauuaeiu", 13);
        test("aeeeiiiioooauuuaeiou", 5);
        test("a", 0);
        test("eauoiouieaaoueiuaieoeauoiaueoiaeoiuieuaoiaeouiaueo", 0);
        test("aeiou", 5);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
