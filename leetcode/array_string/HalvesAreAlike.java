import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1704: https://leetcode.com/problems/determine-if-string-halves-are-alike/
//
// You are given a string s of even length. Split this string into two halves of equal lengths, and
// let a be the first half and b be the second half. Two strings are alike if they have the same
// number of vowels ('a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U'). Notice that s contains
// uppercase and lowercase letters.
// Return true if a and b are alike. Otherwise, return false.
//
// Constraints:
// 2 <= s.length <= 1000
// s.length is even.
// s consists of uppercase and lowercase letters.
public class HalvesAreAlike {
    // time complexity: O(N), space complexity: O(1)
    // 1 ms(100.00%), 37.5 MB(100.00%) for 113 tests
    public boolean halvesAreAlike(String s) {
        char[] cs = s.toCharArray();
        int n = cs.length;
        return count(cs, 0, n / 2) == count(cs, n / 2, n);
    }

    private int count(char[] s, int start, int end) {
        int res = 0;
        for (int i = start; i < end; i++) {
            switch (s[i]) {
            case 'a': case 'e': case 'i': case 'o': case 'u':
            case 'A': case 'E': case 'I': case 'O': case 'U':
                res++;
                break;
            }
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // 2 ms(100.00%), 37.4 MB(100.00%) for 113 tests
    public boolean halvesAreAlike2(String s) {
        int i = 0;
        int count = 0;
        int mid = s.length() / 2;
        String vowels = "aeiouAEIOU";
        for (char c : s.toCharArray()) {
            if (vowels.indexOf(c) >= 0) {
                count += (i < mid) ? 1 : -1;
            }
            i++;
        }
        return count == 0;
    }

    // Set
    // time complexity: O(N), space complexity: O(1)
    // 3 ms(50.00%), 37.1 MB(100.00%) for 113 tests
    public boolean halvesAreAlike3(String s) {
        Set<Character> vowels = Set.of('a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U');
        int count = 0;
        for (int i = 0, j = s.length() - 1; i < j; i++, j--) {
            count += vowels.contains(s.charAt(i)) ? 1 : 0;
            count -= vowels.contains(s.charAt(j)) ? 1 : 0;
        }
        return count == 0;
    }

    // Set
    // time complexity: O(N), space complexity: O(1)
    // 1 ms(100.00%), 37.1 MB(100.00%) for 113 tests
    public boolean halvesAreAlike4(String s) {
        int[] vowels = new int[128];
        for (char c : "aeiouAEIOU".toCharArray()) {
            vowels[c] = 1;
        }
        int count = 0;
        for (int i = 0, j = s.length() - 1; i < j; i++, j--) {
            count += vowels[s.charAt(i)];
            count -= vowels[s.charAt(j)];
        }
        return count == 0;
    }

    private void test(String s, boolean expected) {
        assertEquals(expected, halvesAreAlike(s));
        assertEquals(expected, halvesAreAlike2(s));
        assertEquals(expected, halvesAreAlike3(s));
        assertEquals(expected, halvesAreAlike4(s));
    }

    @Test public void test() {
        test("book", true);
        test("textbook", false);
        test("MerryChristmas", false);
        test("AbCdEfGh", true);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
