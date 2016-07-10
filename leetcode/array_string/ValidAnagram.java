import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/valid-anagram/
//
// You may assume the string contains only lowercase alphabets.
public class ValidAnagram {
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

    // beats 81.93%(6 ms)
    public boolean isAnagram2(String s, String t) {
        char[] cs1 = s.toCharArray();
        char[] cs2 = t.toCharArray();
        if (cs1.length != cs2.length) return false;

        Arrays.sort(cs1);
        Arrays.sort(cs2);
        for (int i = cs1.length - 1; i >= 0; i--) {
            if (cs1[i] != cs2[i]) return false;
        }
        return true;
    }

    void test(String s, String t, boolean expected) {
        assertEquals(expected, isAnagram(s, t));
        assertEquals(expected, isAnagram2(s, t));
    }

    @Test
    public void test1() {
        test("anagram", "nagaram", true);
        test("rat", "car", false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ValidAnagram");
    }
}
