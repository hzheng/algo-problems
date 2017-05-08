import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC567: https://leetcode.com/problems/permutation-in-string/
//
// Given two strings s1 and s2, write a function to return true if s2 contains
// the permutation of s1.
// Note:
// The input strings only contain lower case letters.
// The length of both given strings is in range [1, 10,000].
public class PermutationInString {
    // Two Pointers
    // time complexity: O(L1 + 26 * (L2 - L1))
    // beats 69.94%(20 ms for 102 tests)
    public boolean checkInclusion(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();
        if (len1 > len2) return false;

        int[] count = new int[26];
        for (char c : s1.toCharArray()) {
            count[c - 'a']++;
        }
        char[] cs2 = s2.toCharArray();
        for (int i = 0; i < len1; i++) {
            count[cs2[i] - 'a']--;
        }
        if (allZeros(count)) return true;

        for (int i = len1; i < len2; i++) {
            count[cs2[i] - 'a']--;
            count[cs2[i - len1] - 'a']++;
            if (allZeros(count)) return true;
        }
        return false;
    }

    private boolean allZeros(int[] count) {
        for (int i : count) {
            if (i != 0) return false;
        }
        return true;
    }

    // Two Pointers
    // time complexity: O(L1 + L2))
    // beats 89.67%(17 ms for 102 tests)
    public boolean checkInclusion2(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();
        int[] count = new int[26];
        for (int i = 0; i < len1; count[s1.charAt(i++) - 'a']--) {}
        for (int i = 0, j = 0; i <= len2 - len1; j++) {
            if (++count[s2.charAt(j) - 'a'] > 0) {
                while (--count[s2.charAt(i++) - 'a'] != 0) {}
            } else if ((j - i + 1) == len1) return true;
        }
        return len1 == 0;
    }

    // Two Pointers
    // time complexity: O(L2))
    // beats 42.00%(29 ms for 102 tests)
    public boolean checkInclusion3(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();
        if (len1 > len2) return false;

        int[] count = new int[26];
        for (int i = 0; i < len1; i++) {
            count[s1.charAt(i) - 'a']++;
            count[s2.charAt(i) - 'a']--;
        }
        int same = 0;
        for (int i = 0; i < 26; i++) {
            same += (count[i] == 0) ? 1 : 0;
        }
        for (int i = len1; same != 26 && i < len2; i++) {
            int start = s2.charAt(i - len1) - 'a';
            int end = s2.charAt(i) - 'a';
            if (--count[end] == 0) {
                same++;
            } else if (count[end] == -1) {
                same--;
            }
            if (++count[start] == 0) {
                same++;
            } else if (count[start] == 1) {
                same--;
            }
        }
        return same == 26;
    }

    void test(String s1, String s2, boolean expected) {
        assertEquals(expected, checkInclusion(s1, s2));
        assertEquals(expected, checkInclusion2(s1, s2));
        assertEquals(expected, checkInclusion3(s1, s2));
    }

    @Test
    public void test() {
        test("abc",  "abc", true);
        test("abc",  "bbbca", true);
        test("ab",  "eidbaooo", true);
        test("hello", "ooolleoooleh", false);
    }

    public static void main(String[] args) {
        String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
