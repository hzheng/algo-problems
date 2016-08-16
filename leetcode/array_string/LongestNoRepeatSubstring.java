import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/longest-substring-without-repeating-characters/
//
// Find the length of the longest substring without repeating characters.
public class LongestNoRepeatSubstring {
    // beats 34.82%(21 ms)
    public int lengthOfLongestSubstring(String s) {
        int maxLen = 0;
        int start = 0;
        int strLen = s.length();
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < strLen; i++) {
            char c = s.charAt(i);
            if (map.containsKey(c)) {
                maxLen = Math.max(maxLen, i - start);
                int dupIndex = map.get(c);
                for (int j = start; j < dupIndex; j++) {
                    map.remove(s.charAt(j));
                }
                start = dupIndex + 1;
            }
            map.put(c, i);
        }
        return Math.max(strLen - start, maxLen);
    }

    // beats 66.41%(17 ms)
    public int lengthOfLongestSubstring2(String s) {
        int maxLen = 0;
        int start = 0;
        int strLen = s.length();
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < strLen; i++) {
            char c = s.charAt(i);
            if (map.containsKey(c)) {
                int dupIndex = map.get(c);
                if (dupIndex >= start) {
                    maxLen = Math.max(maxLen, i - start);
                    start = dupIndex + 1;
                }
            }
            map.put(c, i);
        }
        return Math.max(strLen - start, maxLen);
    }

    // Two Pointers
    // https://leetcode.com/articles/longest-substring-without-repeating-characters/
    public int lengthOfLongestSubstring3(String s) {
        int n = s.length();
        Set<Character> set = new HashSet<>();
        int res = 0;
        for (int i = 0, j = 0; i < n && j < n; ) {
            // try to extend the range [i, j]
            if (!set.contains(s.charAt(j))) {
                set.add(s.charAt(j++));
                res = Math.max(res, j - i);
            }
            else {
                set.remove(s.charAt(i++));
            }
        }
        return res;
    }

    // Two Pointers
    // https://leetcode.com/articles/longest-substring-without-repeating-characters/
    public int lengthOfLongestSubstring4(String s) {
        int n = s.length();
        int res = 0;
        Map<Character, Integer> map = new HashMap<>(); // current index of character
        // try to extend the range [i, j]
        for (int i = 0, j = 0; j < n; j++) {
            char c = s.charAt(j);
            if (map.containsKey(c)) {
                i = Math.max(map.get(c), i);
            }
            res = Math.max(res, j - i + 1);
            map.put(c, j + 1);
        }
        return res;
    }

    // Solution of Choice
    // Two Pointers
    // https://leetcode.com/articles/longest-substring-without-repeating-characters/
    // beats 98.52%(4 ms)
    public int lengthOfLongestSubstring5(String s) {
        int n = s.length();
        int res = 0;
        int[] index = new int[128]; // current index of character
        // try to extend the range [i, j]
        for (int i = 0, j = 0; j < n; j++) {
            char c = s.charAt(j);
            i = Math.max(i, index[c]);
            res = Math.max(res, j - i + 1);
            index[c] = j + 1;
        }
        return res;
    }

    void test(String s, int expected) {
        assertEquals(expected, lengthOfLongestSubstring(s));
        assertEquals(expected, lengthOfLongestSubstring2(s));
        assertEquals(expected, lengthOfLongestSubstring3(s));
        assertEquals(expected, lengthOfLongestSubstring4(s));
        assertEquals(expected, lengthOfLongestSubstring5(s));
    }

    @Test
    public void test1() {
        test("abcabcbb", 3);
        test("bbbbb", 1);
        test("pwwkew", 3);
        test("c", 1);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LongestNoRepeatSubstring");
    }
}
