import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC003: https://leetcode.com/problems/longest-substring-without-repeating-characters/
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

    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // beats 79.92%(31 ms for 987 tests)
    public int lengthOfLongestSubstring2_2(String s) {
        Map<Character, Integer> map = new HashMap<>();
        int res = 0;
        for (int i = 0, n = s.length(), first = -1; i < n; i++) {
            char c = s.charAt(i);
            Integer prev = map.get(c);
            if (prev == null || prev <= first) {
                res = Math.max(res, i - first);
            } else {
                first = prev;
            }
            map.put(c, i);
        }
        return res;
    }

    // Two Pointers + Set
    // https://leetcode.com/articles/longest-substring-without-repeating-characters/
    // time complexity: O(N), space complexity: O(N)
    // beats 65.01%(34 ms for 987 tests)
    public int lengthOfLongestSubstring3(String s) {
        int n = s.length();
        Set<Character> set = new HashSet<>();
        int res = 0;
        for (int i = 0, j = 0; i < n && j < n;) {
            if (set.add(s.charAt(j))) {
                res = Math.max(res, ++j - i);
            } else {
                set.remove(s.charAt(i++));
            }
        }
        return res;
    }

    // Two Pointers + Hash Table
    // time complexity: O(N), space complexity: O(N)
    // beats 89.73%(29 ms for 987 tests)
    public int lengthOfLongestSubstring4(String s) {
        int n = s.length();
        int res = 0;
        Map<Character, Integer> map = new HashMap<>();
        for (int i = -1, j = 0; j < n; j++) {
            char c = s.charAt(j);
            i = Math.max(map.getOrDefault(c, -1), i);
            res = Math.max(res, j - i);
            map.put(c, j);
        }
        return res;
    }

    // Solution of Choice
    // Two Pointers
    // https://leetcode.com/articles/longest-substring-without-repeating-characters/
    // time complexity: O(N), space complexity: O(N)
    // beats 99.99%(21 ms for 987 tests)
    public int lengthOfLongestSubstring5(String s) {
        int res = 0;
        int[] index = new int[128];
        for (int i = 0, j = 0, n = s.length(); j < n; j++) {
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
        assertEquals(expected, lengthOfLongestSubstring2_2(s));
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
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
