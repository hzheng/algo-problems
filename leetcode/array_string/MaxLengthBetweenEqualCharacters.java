import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1624: https://leetcode.com/problems/largest-substring-between-two-equal-characters/
//
// Given a string s, return the length of the longest substring between two equal characters,
// excluding the two characters. If there is no such substring return -1.
// A substring is a contiguous sequence of characters within a string.
// Constraints:
//
// 1 <= s.length <= 300
// s contains only lowercase English letters.
public class MaxLengthBetweenEqualCharacters {
    // Hash Table
    // time complexity: O(N), space complexity: O(1)
    // 1 ms(100%), 36.9 MB(20.00%) for 54 tests
    public int maxLengthBetweenEqualCharacters(String s) {
        Map<Character, Integer> map = new HashMap<>();
        int res = -1;
        for (int i = 0, n = s.length(); i < n; i++) {
            Integer prev = map.putIfAbsent(s.charAt(i), i);
            if (prev != null) {
                res = Math.max(res, i - prev - 1);
            }
        }
        return res;
    }

    // time complexity: O(N^2), space complexity: O(1)
    // 1 ms(100%), 36.9 MB(20.00%) for 54 tests
    public int maxLengthBetweenEqualCharacters2(String s) {
        int res = -1;
        for (char c : s.toCharArray()) {
            int firstPos = s.indexOf(c);
            int lastPos = s.lastIndexOf(c);
            res = Math.max(res, lastPos - firstPos - 1);
        }
        return res;
    }

    // Array (2 pass)
    // time complexity: O(N), space complexity: O(1)
    // 1 ms(100%), 36.9 MB(20.00%) for 54 tests
    public int maxLengthBetweenEqualCharacters3(String s) {
        int[] lastPos = new int[26];
        Arrays.fill(lastPos, -1);
        int n = s.length();
        for (int i = 0; i < n; i++) {
            lastPos[s.charAt(i) - 'a'] = i;
        }
        int res = -1;
        for (int i = 0; i < n; i++) {
            int c = s.charAt(i) - 'a';
            res = Math.max(res, lastPos[c] - i - 1);
        }
        return res;
    }

    private void test(String s, int expected) {
        assertEquals(expected, maxLengthBetweenEqualCharacters(s));
        assertEquals(expected, maxLengthBetweenEqualCharacters2(s));
        assertEquals(expected, maxLengthBetweenEqualCharacters3(s));
    }

    @Test public void test() {
        test("aa", 0);
        test("abca", 2);
        test("cbzxy", -1);
        test("cabbac", 4);
        test("mgntdygtxrvxjnwksqhxuxtrv", 18);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
