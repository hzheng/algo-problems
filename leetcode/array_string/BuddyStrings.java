import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC859: https://leetcode.com/problems/buddy-strings/
//
// Given two strings A and B of lowercase letters, return true if and only if we
// can swap two letters in A so that the result equals B.
public class BuddyStrings {
    // beats %(6 ms for 20 tests)
    public boolean buddyStrings(String A, String B) {
        int n = A.length();
        if (B.length() != n) return false;

        int a = -1;
        int b = -1;
        for (int i = 0; i < n; i++) {
            if (A.charAt(i) == B.charAt(i)) continue;

            if (a < 0) {
                a = i;
            } else if (b < 0) {
                b = i;
            } else return false;
        }
        if (a < 0) {
            Set<Character> set = new HashSet<>();
            for (char c : A.toCharArray()) {
                if (!set.add(c)) return true;
            }
            return false;
        }
        return (b >= 0 && A.charAt(a) == B.charAt(b)
               && A.charAt(b) == B.charAt(a));
    }

    // beats %(7 ms for 20 tests)
    public boolean buddyStrings2(String A, String B) {
        if (A.length() != B.length()) return false;

        if (A.equals(B)) {
            int[] count = new int[26];
            for (int i = 0; i < A.length(); i++) {
                count[A.charAt(i) - 'a']++;
            }
            for (int c : count) {
                if (c > 1) return true;
            }
            return false;
        }

        int a = -1;
        int b = -1;
        for (int i = A.length() - 1; i >= 0; i--) {
            if (A.charAt(i) == B.charAt(i)) continue;

            if (a < 0) {
                a = i;
            } else if (b < 0) {
                b = i;
            } else return false;
        }
        return (b >= 0 && A.charAt(a) == B.charAt(b)
               && A.charAt(b) == B.charAt(a));
    }

    void test(String A, String B, boolean expected) {
        assertEquals(expected, buddyStrings(A, B));
        assertEquals(expected, buddyStrings2(A, B));
    }

    @Test
    public void test() {
        test("ab", "ba", true);
        test("ab", "ab", false);
        test("aa", "aa", true);
        test("aaaaaaabc", "aaaaaaacb", true);
        test("", "aa", false);
        test("ab", "bac", false);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
