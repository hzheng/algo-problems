import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1796: https://leetcode.com/problems/second-largest-digit-in-a-string/
//
// Given an alphanumeric string s, return the second largest numerical digit that appears in s, or
// -1 if it does not exist.
// An alphanumeric string is a string consisting of lowercase English letters and digits.
//
// Constraints:
// 1 <= s.length <= 500
// s consists of only lowercase English letters and/or digits.
public class SecondHighest {
    // time complexity: O(N), space complexity: O(1)
    // 1 ms(%), 38.8 MB(%) for 300 tests
    public int secondHighest(String s) {
        int max = -1;
        int secondMax = -1;
        for (char c : s.toCharArray()) {
            if (c < '0' || c > '9') { continue; }

            int x = (c - '0');
            if (x > max) {
                secondMax = max;
                max = x;
            } else if (x > secondMax && x != max) {
                secondMax = x;
            }
        }
        return secondMax;
    }

    // time complexity: O(N), space complexity: O(1)
    // 1 ms(%), 38.7 MB(%) for 300 tests
    public int secondHighest2(String s) {
        int[] freq = new int[10];
        for (char c : s.toCharArray()) {
            if (c >= '0' && c <= '9') {
                freq[c - '0']++;
            }
        }
        for (int i = freq.length - 1, max = 0; i >= 0; i--) {
            if (freq[i] == 0) { continue; }

            if (max == 0) {
                max = 1;
            } else { return i; }
        }
        return -1;
    }

    // SortedSet
    // time complexity: O(N), space complexity: O(1)
    // 3 ms(%), 39 MB(%) for 300 tests
    public int secondHighest3(String s) {
        TreeSet<Integer> set = new TreeSet<>();
        for (char c : s.toCharArray()) {
            if (c >= '0' && c <= '9') {
                set.add(c - '0');
            }
        }
        if (set.size() < 2) { return -1; }

        set.pollLast();
        return set.last();
    }

    private void test(String s, int expected) {
        assertEquals(expected, secondHighest(s));
        assertEquals(expected, secondHighest2(s));
        assertEquals(expected, secondHighest3(s));
    }

    @Test public void test() {
        test("dfa12321afd", 2);
        test("abc1111", -1);
        test("abc8c989", 8);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
