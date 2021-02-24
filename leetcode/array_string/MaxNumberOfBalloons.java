import java.util.*;
import java.util.stream.IntStream;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1189: https://leetcode.com/problems/maximum-number-of-balloons/
//
// Given a string text, you want to use the characters of text to form as many instances of the word
// "balloon" as possible. You can use each character in text at most once. Return the maximum number
// of instances that can be formed.
//
// Constraints:
// 1 <= text.length <= 10^4
// text consists of lower case English letters only.
public class MaxNumberOfBalloons {
    // time complexity: O(N), space complexity: O(N)
    // 1 ms(100%), 38.9 MB(50.90%) for 23 tests
    public int maxNumberOfBalloons(String text) {
        int[] freq = new int[26];
        for (char c : text.toCharArray()) {
            freq[c - 'a']++;
        }
        int res = Math.min(freq['b' - 'a'], freq[0]);
        res = Math.min(res, freq['n' - 'a']);
        res = Math.min(res, freq['l' - 'a'] / 2);
        return Math.min(res, freq['o' - 'a'] / 2);
    }

    // time complexity: O(N), space complexity: O(N)
    // 1 ms(100%), 39.1 MB(26.35%) for 23 tests
    public int maxNumberOfBalloons2(String text) {
        int[] freq = new int[26];
        for (char c : text.toCharArray()) {
            freq[c - 'a']++;
        }
        char[] balloon = "balloon".toCharArray();
        int[] target = new int[26];
        for (char c : balloon) {
            target[c - 'a']++;
        }
        int res = Integer.MAX_VALUE;
        for (char b : balloon) {
            res = Math.min(res, freq[b - 'a'] / target[b - 'a']);
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(N)
    // 1 ms(100%), 39.1 MB(26.35%) for 23 tests
    public int maxNumberOfBalloons3(String text) {
        int[] freq = new int[26];
        for (char c : text.toCharArray()) {
            freq[c - 'a']++;
        }
        return IntStream
                .of(freq['b' - 'a'], freq['a' - 'a'], freq[('l' - 'a')] / 2, freq[('o' - 'a')] / 2,
                    freq[('n' - 'a')]).min().getAsInt();
    }

    // time complexity: O(N), space complexity: O(N)
    // 1 ms(100%), 39 MB(41.75%) for 23 tests
    public int maxNumberOfBalloons4(String text) {
        int b = 0, a = 0, l = 0, o = 0, n = 0;
        for (char c : text.toCharArray()) {
            switch (c) {
            case 'b':
                b++;
                break;
            case 'a':
                a++;
                break;
            case 'l':
                l++;
                break;
            case 'o':
                o++;
                break;
            case 'n':
                n++;
                break;
            }
        }
        return Math.min(Math.min(o / 2, l / 2), Math.min(Math.min(b, a), n));
    }

    private void test(String text, int expected) {
        assertEquals(expected, maxNumberOfBalloons(text));
        assertEquals(expected, maxNumberOfBalloons2(text));
        assertEquals(expected, maxNumberOfBalloons3(text));
        assertEquals(expected, maxNumberOfBalloons4(text));
    }

    @Test public void test() {
        test("nlaebolko", 1);
        test("loonbalxballpoon", 2);
        test("leetcode", 0);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
