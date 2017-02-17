import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC159: https://leetcode.com/problems/longest-substring-with-at-most-two-distinct-characters/
//
// Given a string, find the length of the longest substring T that contains at most 2 distinct characters.
public class LongestSubstringTwoDistinct {
    // Two Pointers
    // beats 95.32%(5 ms for 125 tests)
    public int lengthOfLongestSubstringTwoDistinct(String s) {
        int max = 0;
        int len = s.length();
        char[] cs = s.toCharArray();
        int start1 = 0;
        for (int end1 = 0, start2 = 0, end2 = 0, i = 1; i < len; i++) {
            char c = cs[i];
            if (c == cs[start1]) {
                end1 = i;
            } else if (start2 == 0) {
                start2 = end2 = i;
            } else if (c == cs[start2]) {
                end2 = i;
            } else {
                max = Math.max(max, i - start1);
                start1 = Math.min(end1, end2) + 1;
                end1 = i - 1;
                start2 = end2 = i;
            }
        }
        return Math.max(max, len - start1);
    }

    // Two Pointers
    // beats 98.04%(4 ms for 125 tests)
    public int lengthOfLongestSubstringTwoDistinct2(String s) {
        int max = 0;
        int len = s.length();
        char[] cs = s.toCharArray();
        int start1 = 0;
        for (int end2 = -1, i = 1; i < len; i++) {
            if (cs[i] == cs[i - 1]) continue;

            if (end2 >= 0 && cs[i] != cs[end2]) {
                max = Math.max(max, i - start1);
                start1 = end2 + 1;
            }
            end2 = i - 1;
        }
        return Math.max(max, len - start1);
    }

    private static final int MAX_COUNT = 2;

    // Two Pointers + Hash Table
    // beats 43.31%(31 ms for 125 tests)
    public int lengthOfLongestSubstringTwoDistinct3(String s) {
        int len = s.length();
        Map<Character, Integer> map = new HashMap<>();
        int max = 0;
        for (int start = 0, end = 0; end < len; end++) {
            map.put(s.charAt(end), end);
            if (map.size() > MAX_COUNT) {
                int leftMost = end;
                for (int i : map.values()) {
                    leftMost = Math.min(leftMost, i);
                }
                map.remove(s.charAt(leftMost));
                start = leftMost + 1;
            }
            max = Math.max(max, end - start + 1);
        }
        return max;
    }

    // Two Pointers + Hash Table
    // beats 95.32%(5 ms for 125 tests)
    public int lengthOfLongestSubstringTwoDistinct4(String s) {
        int len = s.length();
        char[] cs = s.toCharArray();
        int[] map = new int[256];
        int max = 0;
        for (int i = 0, start = 0, count = 0; i < len; i++) {
            if (++map[cs[i]] == 1) {
                for (count++; count > MAX_COUNT; start++) {
                    if (--map[cs[start]] == 0) {
                        count--;
                    }
                }
            }
            max = Math.max(max, i - start + 1);
        }
        return max;
    }

    void test(String s, int expected) {
        assertEquals(expected, lengthOfLongestSubstringTwoDistinct(s));
        assertEquals(expected, lengthOfLongestSubstringTwoDistinct2(s));
        assertEquals(expected, lengthOfLongestSubstringTwoDistinct3(s));
        assertEquals(expected, lengthOfLongestSubstringTwoDistinct4(s));
    }

    @Test
    public void test() {
        test("", 0);
        test("e", 1);
        test("ea", 2);
        test("eceba", 3);
        test("abaccc", 4);
        test("ecebaaa", 4);
        test("ececeba", 5);
        test("ababffzzeee", 5);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LongestSubstringTwoDistinct");
    }
}
