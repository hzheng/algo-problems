import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/ransom-note/
//
// Given an arbitrary ransom note string and another string containing a
// from all the magazines, write a function that will return true.
// if the ransom note can be constructed from the magazines; otherwise, it will
// return false. Each letter in the magazine string can only be used once.

// Note:
// You may assume that both strings contain only lowercase letters.
public class RansomNote {
    // beats N/A(13 ms)
    public boolean canConstruct(String ransomNote, String magazine) {
        int[] counts = new int[26];
        for (char c : magazine.toCharArray()) {
            counts[c - 'a']++;
        }
        for (char c : ransomNote.toCharArray()) {
            if (--counts[c - 'a'] < 0) return false;
        }
        return true;
    }

    void test(String ransomNote, String magazine, boolean expected) {
        assertEquals(expected, canConstruct(ransomNote, magazine));
    }

    @Test
    public void test1() {
        test("a", "b", false);
        test("aa", "ab", false);
        test("aa", "aab", true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RansomNote");
    }
}
