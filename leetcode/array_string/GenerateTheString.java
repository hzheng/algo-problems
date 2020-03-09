import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1374: https://leetcode.com/problems/generate-a-string-with-characters-that-have-odd-counts/
//
// Given an integer n, return a string with n characters such that each character in such string occurs an odd number of
// times. The returned string must contain only lowercase English letters. If there are multiples valid strings, return
// any of them.
public class GenerateTheString {
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100%), 37 MB(100%) for 103 tests
    public String generateTheString(int n) {
        char[] chars = new char[n];
        chars[0] = 'a';
        char c = (n % 2 == 1) ? 'a' : 'b';
        for (int i = 1; i < n; i++) {
            chars[i] = c;
        }
        return String.valueOf(chars);
    }

    private void check(String s) {
        char[] count = new char[26];
        for (char c : s.toCharArray()) {
            count[c - 'a']++;
        }
        for (int c : count) {
            if (c > 0) {
                assertEquals(1, c & 1);
            }
        }
    }

    void test(int n) {
        check(generateTheString(n));
    }

    @Test
    public void test() {
        test(1);
        test(2);
        test(3);
        test(4);
        test(5);
        test(6);
        test(7);
        test(8);
        test(9);
        test(19);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
