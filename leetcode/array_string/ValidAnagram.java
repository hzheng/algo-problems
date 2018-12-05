import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC242: https://leetcode.com/problems/valid-anagram/
//
// Given two strings s and t, determine if t is an anagram of s.
// You may assume the string contains only lowercase alphabets.
public class ValidAnagram {
    // Count
    // time complexity: O(N), space complexity: O(1)
    // beats 91.83%(4 ms)
    public boolean isAnagram(String s, String t) {
        int[] counts = new int[26];
        for (char c : s.toCharArray()) {
            counts[c - 'a']++;
        }
        for (char c : t.toCharArray()) {
            counts[c - 'a']--;
        }
        for (int count : counts) {
            if (count != 0) return false;
        }
        return true;
    }

    // Sort
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 81.93%(6 ms)
    public boolean isAnagram2(String s, String t) {
        char[] cs1 = s.toCharArray();
        char[] cs2 = t.toCharArray();
        if (cs1.length != cs2.length) return false;

        Arrays.sort(cs1);
        Arrays.sort(cs2);
        // return Arrays.equals(cs1, cs2); // slower?(7 ms)
        for (int i = cs1.length - 1; i >= 0; i--) {
            if (cs1[i] != cs2[i]) return false;
        }
        return true;
    }

    // Count
    // time complexity: O(N), space complexity: O(1)
    // beats 64.12%(7 ms)
    public boolean isAnagram3(String s, String t) {
        if (s.length() != t.length()) return false;

        int[] counts = new int[26];
        for (int i = s.length() - 1; i >= 0; i--) {
            counts[s.charAt(i) - 'a']++;
            counts[t.charAt(i) - 'a']--;
        }
        for (int count : counts) {
            if (count != 0) return false;
        }
        return true;
    }

    // Solution of Choice
    // Count
    // time complexity: O(N), space complexity: O(1)
    // beats 49.11%(8 ms for 34 tests)
    public boolean isAnagram4(String s, String t) {
        if (s.length() != t.length()) return false;

        int[] counts = new int[26];
        for (int i = 0; i < s.length(); i++) {
            counts[s.charAt(i) - 'a']++;
        }
        for (int i = 0; i < t.length(); i++) {
            if (--counts[t.charAt(i) - 'a'] < 0) return false;
        }
        return true;
    }

    void test(String s, String t, boolean expected) {
        assertEquals(expected, isAnagram(s, t));
        assertEquals(expected, isAnagram2(s, t));
        assertEquals(expected, isAnagram3(s, t));
        assertEquals(expected, isAnagram4(s, t));
    }

    @Test
    public void test1() {
        test("anagram", "nagaram", true);
        test("rat", "car", false);
        test("rat", "tarc", false);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
