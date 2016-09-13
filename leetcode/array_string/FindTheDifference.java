import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC389: https://leetcode.com/problems/find-the-difference/
//
// Given two strings s and t which consist of only lowercase letters.
// String t is generated by random shuffling string s and then add one more
// letter at a random position.
// Find the letter that was added in t.
public class FindTheDifference {
    // beats N/A(10 ms)
    public char findTheDifference(String s, String t) {
        int[] counts = new int[26];
        for (char c : t.toCharArray()) {
            counts[c - 'a']++;
        }
        for (char c : s.toCharArray()) {
            counts[c - 'a']--;
        }
        for (int i = 0; i < 26; i++) {
            if (counts[i] > 0) return (char)(i + 'a');
        }
        return 0;
    }

    // beats N/A(8 ms)
    public char findTheDifference2(String s, String t) {
        char res = 0;
        for (char c : t.toCharArray()) {
            res ^= c;
        }
        for (char c : s.toCharArray()) {
            res ^= c;
        }
        return res;
    }

    // Solution of Choice
    // beats N/A(7 ms)
    public char findTheDifference3(String s, String t) {
        int len = s.length();
        char res = t.charAt(len);
        for (int i = len - 1; i >= 0; i--) {
            res ^= s.charAt(i);
            res ^= t.charAt(i);
        }
        return res;
    }

    void test(String s, String t, char expected) {
        assertEquals(expected, findTheDifference(s, t));
        assertEquals(expected, findTheDifference2(s, t));
        assertEquals(expected, findTheDifference3(s, t));
    }

    @Test
    public void test1() {
        test("", "a", 'a');
        test("abcd", "abcde", 'e');
        test("abcdefghijklmn", "jiahbfxkgcndlme", 'x');
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("FindTheDifference");
    }
}